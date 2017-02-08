package ee.joonasvali.spaceshooter.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Disposable;
import ee.joonasvali.spaceshooter.core.game.enemy.Enemy;
import ee.joonasvali.spaceshooter.core.game.enemy.EnemyFormation;
import ee.joonasvali.spaceshooter.core.game.enemy.GaussEnemy;
import ee.joonasvali.spaceshooter.core.game.weapons.Missile;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author Joonas Vali February 2017
 */
public class LevelProvider implements Disposable {
  private static final int LEVEL1_VERTICAL_DISTANCE_IN_MATRIX = 6;
  private static final int LEVEL1_HORIZONTAL_DISTANCE_IN_MATRIX = 6;
  private static final int LEVEL1_FORMATION_HEIGHT_AMOUNT = 5;
  private static final int LEVEL1_FORMATION_WIDTH_AMOUNT = 8;

  private static final int LEVEL2_VERTICAL_DISTANCE_IN_MATRIX = 6;
  private static final int LEVEL2_HORIZONTAL_DISTANCE_IN_MATRIX = 6;
  private static final int LEVEL2_FORMATION_HEIGHT_AMOUNT = 3;
  private static final int LEVEL2_FORMATION_WIDTH_AMOUNT = 9;

  private final Texture texture;
  private final Sprite sprite;
  private final Sprite sprite2;
  private final Sprite sprite3;

  private final Map<Enemy, Sprite> spriteMap = new IdentityHashMap<>();

  private int activeLevel = 0;
  private final int maxLevel = 3;

  private final float worldWidth;
  private final float worldHeight;

  public LevelProvider(float worldWidth, float worldHeight) {
    this.worldWidth = worldWidth;
    this.worldHeight = worldHeight;
    this.texture = new Texture(Gdx.files.internal("Gunship_mf_Sprite.png"));

    this.sprite = new Sprite(texture, 33, 77, 190, 100);
    this.sprite2 = new Sprite(texture, 33, 77, 190, 100);
    sprite2.setColor(Color.YELLOW);
    this.sprite3 = new Sprite(texture, 33, 77, 190, 100);
    sprite3.setColor(Color.BLUE);
  }

  public EnemyFormation nextLevel(float enemySize) {
    spriteMap.clear();
    EnemyFormation formation = null;
    switch (activeLevel) {
      case 0: formation = createLevel1(enemySize);
        System.out.println("loading level 1"); break;
      case 1: formation = createLevel2(enemySize);
        System.out.println("loading level 2"); break;
      case 2: formation = createLevel3(enemySize);
        System.out.println("loading level 3"); break;
    }
    activeLevel++;
    return formation;
  }

  private EnemyFormation createLevel1(float enemySize) {
    EnemyFormation formation = new EnemyFormation(LEVEL1_FORMATION_WIDTH_AMOUNT, LEVEL1_FORMATION_HEIGHT_AMOUNT, (x, y) -> {
      Enemy enemy;
      if (y == 0) {
        enemy = new Enemy(Missile.class, 2000, 150, x, y);
        spriteMap.put(enemy, sprite3);
      } else if (y == 1) {
        enemy = null;
      } else {
        enemy = new Enemy(Missile.class, 1000, 100, x, y);
        spriteMap.put(enemy, sprite);
      }
      if (enemy != null) {
        enemy.setSize(enemySize, enemySize);
      }
      return enemy;
    }, LEVEL1_HORIZONTAL_DISTANCE_IN_MATRIX, LEVEL1_VERTICAL_DISTANCE_IN_MATRIX);

    formation.setX(5);
    formation.setY(worldHeight - Math.min(50, (LEVEL1_FORMATION_HEIGHT_AMOUNT + 2) * (LEVEL1_VERTICAL_DISTANCE_IN_MATRIX)));
    return formation;
  }

  private EnemyFormation createLevel2(float enemySize) {
    EnemyFormation formation = new EnemyFormation(LEVEL1_FORMATION_WIDTH_AMOUNT, LEVEL1_FORMATION_HEIGHT_AMOUNT, (x, y) -> {
      Enemy enemy;
      if (y == 0) {
        enemy = new GaussEnemy(3000, 200, x, y);
        spriteMap.put(enemy, sprite2);
      } else if (x == 0 || x == LEVEL1_FORMATION_WIDTH_AMOUNT - 1) {
        enemy = new Enemy(Missile.class, 2000, 150, x, y);
        spriteMap.put(enemy, sprite3);
      } else {
        enemy = new Enemy(Missile.class, 1000, 100, x, y);
        spriteMap.put(enemy, sprite);
      }
      enemy.setSize(enemySize, enemySize);
      return enemy;
    }, LEVEL1_HORIZONTAL_DISTANCE_IN_MATRIX, LEVEL1_VERTICAL_DISTANCE_IN_MATRIX);

    formation.setX(5);
    formation.setY(worldHeight - Math.min(50, (LEVEL1_FORMATION_HEIGHT_AMOUNT + 2) * (LEVEL1_VERTICAL_DISTANCE_IN_MATRIX)));
    return formation;
  }

  private EnemyFormation createLevel3(float enemySize) {
    EnemyFormation formation = new EnemyFormation(LEVEL2_FORMATION_WIDTH_AMOUNT, LEVEL2_FORMATION_HEIGHT_AMOUNT, (x, y) -> {
      Enemy enemy;

      enemy = new GaussEnemy(3000, 200, x, y);
      spriteMap.put(enemy, sprite2);

      enemy.setSize(enemySize, enemySize);
      return enemy;
    }, LEVEL2_HORIZONTAL_DISTANCE_IN_MATRIX, LEVEL2_VERTICAL_DISTANCE_IN_MATRIX);

    formation.setX(5);
    formation.setY(worldHeight - Math.min(50, (LEVEL2_FORMATION_HEIGHT_AMOUNT + 2) * (LEVEL2_VERTICAL_DISTANCE_IN_MATRIX)));
    return formation;
  }

  public Sprite getSprite(Enemy enemy) {
    return spriteMap.getOrDefault(enemy, sprite);
  }

  @Override
  public void dispose() {
    texture.dispose();
  }

  public boolean hasNextLevel() {
    return activeLevel < maxLevel;
  }

  public int getNextLevel() {
    return activeLevel + 1;
  }
}
