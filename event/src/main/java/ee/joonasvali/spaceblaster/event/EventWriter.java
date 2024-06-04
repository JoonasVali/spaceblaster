package ee.joonasvali.spaceblaster.event;

import org.yaml.snakeyaml.Yaml;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EventWriter {
  private final ExecutorService executor;
  private final Writer writer;
  private final Yaml yaml;
  public EventWriter(OutputStream outputStream) {
    yaml = new Yaml();
    executor = Executors.newSingleThreadExecutor();
    this.writer = new BufferedWriter(new OutputStreamWriter(outputStream));
  }

  public void write(Event event) {
    yaml.dump(event, this.writer);
  }

  public void dispose() {
    executor.shutdown();
  }

  public void waitUntilDisposed() throws InterruptedException {
    executor.awaitTermination(20, TimeUnit.SECONDS);
  }
}
