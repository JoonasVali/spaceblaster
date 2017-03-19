package ee.joonasvali.spaceshooter.core.game.level;

import com.badlogic.gdx.files.FileHandle;
import ee.joonasvali.spaceshooter.core.game.Background;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Reads levels from text file
 *
 * @author Joonas Vali February 2017
 */
public class LevelReader {
  private static final Logger log = LoggerFactory.getLogger(LevelReader.class);
  private List<Level> levels = new ArrayList<>();
  private final String backgroundFileName;

  public LevelReader (FileHandle handle) {
    String[] lines = handle.readString().split("\\r?\\n");
    String background = Background.DEFAULT_FILE_NAME;
    LinkedList<String> currentLevel = new LinkedList<>();
    boolean readingLevel = false;
    for (int i = 0; i < lines.length; i++) {
      String line = lines[i].trim();

      // See if this line is defining background of the level:
      if (line.startsWith("background")) {
        try {
          line = line.replaceAll("\\s+", "");
          if (line.startsWith("background=")) {
            background = line.split("=")[1];
          } else {
            log.error("background invalid format: " + line);
          }
        } catch (Exception ex) {
          log.error("background invalid format: " + line, ex);
        }
        continue;
      }

      if (line.startsWith("#")) {
        // Comment
        continue;
      }

      if (line.isEmpty()) {
        if (readingLevel) {
          String name = currentLevel.removeFirst(); // The first of level defines a level name
          levels.add(new Level(currentLevel.toArray(new String[currentLevel.size()]), name));
          currentLevel.clear();
          readingLevel = false;
        }
        continue;
      }
      readingLevel = true;
      currentLevel.add(line);
    }
    if (readingLevel) {
      readingLevel = false;
      String name = currentLevel.removeFirst(); // The first of level defines a level name
      levels.add(new Level(currentLevel.toArray(new String[currentLevel.size()]), name));
      currentLevel.clear();
    }
    this.backgroundFileName = background;
  }

  public int getNumberOfLevels() {
    return levels.size();
  }

  public LevelDescriptor getLevel(int index) {
    return levels.get(index);
  }

  public String getBackgroundFileName() {
    return backgroundFileName;
  }

  public class Level implements LevelDescriptor {
    private final String[] formation;
    private final int width;
    private final int height;
    private final String name;

    public Level(String[] formation, String name) {
      this.formation = formation;
      this.name = name;
      int maxW = 0;
      for (String line : formation) {
        maxW = Math.max(maxW, line.length());
      }
      width = maxW;
      height = formation.length;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public String[] getLevel() {
      return formation;
    }

    @Override
    public int getWidth() {
      return width;
    }

    @Override
    public int getHeight() {
      return height;
    }
  }
}
