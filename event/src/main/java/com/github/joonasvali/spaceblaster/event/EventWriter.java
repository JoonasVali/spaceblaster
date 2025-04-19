package com.github.joonasvali.spaceblaster.event;

import org.yaml.snakeyaml.Yaml;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class EventWriter<T> {
  private final ExecutorService executor;
  private final Writer writer;
  private final Yaml yaml;
  private final NamedImageWriter imageWriter;
  private final Consumer<Consumer<T>> makeScrnshot;


  public EventWriter(OutputStream outputStream, NamedImageWriter<T> imageWriter, Consumer<Consumer<T>> makeScrnshot) {
    yaml = new Yaml();
    executor = Executors.newSingleThreadExecutor();
    this.writer = new BufferedWriter(new OutputStreamWriter(outputStream));
    this.imageWriter = imageWriter;
    this.makeScrnshot = makeScrnshot;
  }


  public void write(Event event) {
    List<Event> eventList = new ArrayList<>();
    eventList.add(event);
    yaml.dump(eventList, this.writer);

    try {
      makeScrnshot.accept(screenshot -> executor.submit(() -> {
        try {
          imageWriter.writeImage(String.valueOf(event.eventTimestamp), screenshot);
        } catch (Exception ex) {
          System.err.println("Failed to write image: " + ex.getMessage());
        }
      }));
    } catch (Exception ex) {
      System.err.println("Failed to flush writer: " + ex.getMessage());
    }
  }

  public void dispose() {
    executor.shutdown();
  }

  public void waitUntilDisposed() throws InterruptedException {
    executor.awaitTermination(20, TimeUnit.SECONDS);
  }
}
