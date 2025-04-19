package com.github.joonasvali.spaceblaster.event;

import org.yaml.snakeyaml.Yaml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EventWriter<T> {
  private final ExecutorService executor;
  private final Writer writer;
  private final Yaml yaml;
  private final NamedImageWriter<T> imageWriter;
  private final Consumer<BiConsumer<T, Runnable>> makeScrnshot;

  /**
   * Creates an EventWriter instance.
   * @param outputStream the output stream to write the events to
   * @param imageWriter the image writer to use for writing images
   * @param makeScrnshot a consumer that takes a BiConsumer to create a screenshot. The BiConsumer
   *                     takes a screenshot and a Runnable to dispose of the screenshot.
   */
  public EventWriter(OutputStream outputStream,
                     NamedImageWriter<T> imageWriter,
                     Consumer<BiConsumer<T, Runnable>> makeScrnshot) {
    yaml = new Yaml();
    if (makeScrnshot != null) {
      executor = Executors.newFixedThreadPool(
          Math.min(4, Runtime.getRuntime().availableProcessors())
      );
    } else {
      executor = null;
    }
    this.writer = new BufferedWriter(new OutputStreamWriter(outputStream));
    this.imageWriter = imageWriter;
    this.makeScrnshot = makeScrnshot;
  }

  public void write(Event event) {
    List<Event> eventList = new ArrayList<>();
    eventList.add(event);
    yaml.dump(eventList, this.writer);

    if (makeScrnshot != null) {
      try {
        makeScrnshot.accept((screenshot, disposeScreenshot) -> executor.submit(() -> {
          try {
            imageWriter.writeImage(String.valueOf(event.eventTimestamp), screenshot);
          } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            System.err.println("Image writing interrupted: " + ex.getMessage());
          } catch (IOException ex) {
            System.err.println("Failed to write image: " + ex.getMessage());
          } finally {
            disposeScreenshot.run();
          }
        }));
      } catch (Exception ex) {
        System.err.println("Failed to schedule image write: " + ex.getMessage());
      }
    }
  }

  /**
   * Flushes the writer and stops accepting new image tasks.
   * @throws IOException if an I/O error occurs when flushing or closing the writer.
   */
  public void dispose() throws IOException {
    try {
      writer.flush();
      writer.close();
    } catch (IOException e) {
      System.err.println("Failed to flush/close writer: " + e.getMessage());
    }

    // Stop accepting new image tasks immediately
    if (executor != null) {
      executor.shutdown();
    }
  }

  /**
   * Waits for the executor to finish executing remaining tasks, with a timeout and fallback.
   * Ensures shutdown is already called so awaitTermination doesn't wait full timeout if executor
   * isn't yet shut down.
   */
  public void waitUntilDisposed() {
    if (executor == null) {
      return;
    }
    // Ensure we have initiated shutdown
    executor.shutdown();
    try {
      // Wait up to 60 seconds for tasks to complete
      if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
        // Cancel currently executing tasks
        executor.shutdownNow();
        // Wait a further 15 seconds for tasks to respond to cancellation
        if (!executor.awaitTermination(15, TimeUnit.SECONDS)) {
          System.err.println("Executor did not terminate in time");
        }
      }
    } catch (InterruptedException ie) {
      executor.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }
}
