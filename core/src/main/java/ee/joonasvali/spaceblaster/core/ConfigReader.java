package ee.joonasvali.spaceblaster.core;

import com.badlogic.gdx.files.FileHandle;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;

public class ConfigReader {
  private Config config;

  public ConfigReader(FileHandle handle) {
    try (InputStream input = handle.read()) {

      LoaderOptions loaderOptions = new LoaderOptions();
      loaderOptions.setTagInspector(tag -> true);
      Yaml yaml = new Yaml(loaderOptions);
      config = yaml.loadAs(input, Config.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Config getConfig() {
    return config;
  }
}