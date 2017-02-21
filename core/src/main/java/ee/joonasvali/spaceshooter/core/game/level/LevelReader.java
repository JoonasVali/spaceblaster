package ee.joonasvali.spaceshooter.core.game.level;

import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Reads levels from text file
 *
 * @author Joonas Vali February 2017
 */
public class LevelReader {
  List<Level> levels = new ArrayList<>();
  public LevelReader (FileHandle file) {
    String[] lines = file.readString().split("\\r?\\n");

    LinkedList<String> currentLevel = new LinkedList<>();
    boolean readingLevel = false;
    for (int i = 0; i < lines.length; i++) {
      String line = lines[i].trim();
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
  }

  public int getNumberOfLevels() {
    return levels.size();
  }

  public LevelDescriptor getLevel(int index) {
    return levels.get(index);
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
