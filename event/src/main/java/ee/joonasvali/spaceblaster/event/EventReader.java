package ee.joonasvali.spaceblaster.event;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;

public class EventReader {
  private InputStream inputStream;
  private Yaml yaml;
  public EventReader(InputStream inputStream) {
    this.inputStream = inputStream;

    LoaderOptions loaderOptions = new LoaderOptions();
    loaderOptions.setTagInspector(tag -> true);
    yaml = new Yaml(loaderOptions);
  }

  public List<Event> readContent() {
    return yaml.loadAs(inputStream, List.class);
  }
}
