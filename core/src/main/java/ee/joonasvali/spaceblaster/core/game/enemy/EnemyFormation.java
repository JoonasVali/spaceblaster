package ee.joonasvali.spaceblaster.core.game.enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Joonas Vali January 2017
 */
public class EnemyFormation {
  private final int width;
  private final int height;
  private final Enemy[][] formation;
  private List<Enemy> enemies;

  private int maxXIndex;
  private int minXIndex;

  private float horizontalDistance;
  private float verticalDistance;

  private float x;
  private float y;

  private boolean movesLeft;
  private final float enemySize;
  private boolean noUnbornLeft;

  public EnemyFormation(int width, int height, float enemySize, EnemyProvider generator, float horizontalDistance, float verticalDistance) {
    this.horizontalDistance = horizontalDistance;
    this.verticalDistance = verticalDistance;
    this.enemySize = enemySize;

    this.width = width;
    this.height = height;
    this.formation = new Enemy[width][height];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        formation[i][j] = generator.get(i, j);
      }
    }
    this.enemies = new ArrayList<>();
    refreshEnemiesList();
  }

  private void refreshEnemiesList() {
    // initial values are opposite maximum
    this.maxXIndex = 0;
    this.minXIndex = width;

    enemies.clear();
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        if (formation[i][j] != null) {
          this.maxXIndex = Math.max(this.maxXIndex, formation[i][j].getMatrixPosX());
          this.minXIndex = Math.min(this.minXIndex, formation[i][j].getMatrixPosX());
          enemies.add(formation[i][j]);
        }
      }
    }
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public List<Enemy> getEnemies() {
    return enemies;
  }

  public void removeAll(List<Enemy> death) {
    if (!death.isEmpty()) {
      for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
          if (formation[i][j] != null && death.contains(formation[i][j])) {
            formation[i][j] = null;
          }
        }
      }
      refreshEnemiesList();
    }
  }

  public float getMaxX() {
    return getX() + (maxXIndex * horizontalDistance);
  }

  public float getMinX() {
    return getX() + (minXIndex * horizontalDistance);
  }

  public float getXof(Enemy enemy) {
    return getX() + (enemy.getMatrixPosX() * horizontalDistance) + enemy.getOffsetX();
  }

  /* Inverse the matrix because Y points up in the game engine, but for human readability it's better if array
  * matches what's on screen. Otherwise the lowest row in array would be highest in screen...
  * */
  public float getYof(Enemy enemy) {
    return getY() + (height - enemy.getMatrixPosY()) * verticalDistance  + enemy.getOffsetY();
  }


  public float getX() {
    return x;
  }

  public void setX(float x) {
    this.x = x;
  }

  public float getY() {
    return y;
  }

  public void setY(float y) {
    this.y = y;
  }

  public boolean isMovesLeft() {
    return movesLeft;
  }

  public void setMovesLeft(boolean movesLeft) {
    this.movesLeft = movesLeft;
  }

  public Optional<Enemy> getRandomBornFromBottom() {
    List<Enemy> candidates = new ArrayList<>();
    for (int i = 0; i < width; i++) {
      Enemy e = null;
      for (int j = 0; j < height; j++) {
        if (formation[i][j] != null && formation[i][j].isBorn()) {
          e = formation[i][j];
        }
      }
      if (e != null) {
        candidates.add(e);
      }
    }
    if (!candidates.isEmpty()) {
      return Optional.of(candidates.get((int) (Math.random() * candidates.size())));
    } else {
      return Optional.empty();
    }
  }

  public Optional<Enemy> getRandomUnBorn() {
    if (noUnbornLeft) {
      return Optional.empty();
    }
    List<Enemy> candidates = new ArrayList<>();
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        if (formation[i][j] != null && !formation[i][j].isBorn()) {
          candidates.add(formation[i][j]);
        }
      }
    }
    if (!candidates.isEmpty()) {
      return Optional.of(candidates.get((int) (Math.random() * candidates.size())));
    } else {
      noUnbornLeft = true;
      return Optional.empty();
    }
  }

  public float getLowestYPositionOffset() {
    for (int j = height - 1; j >= 0; j--) {
      for (int i = 0; i < width; i++) {
        if (formation[i][j] != null) {
          return (height - j) * verticalDistance;
        }
      }
    }
    return 0;
  }

  public float getEnemySize() {
    return enemySize;
  }

  public boolean hasUnbornLeft() {
    return !noUnbornLeft;
  }
}
