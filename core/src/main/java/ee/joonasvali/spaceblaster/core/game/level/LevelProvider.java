package ee.joonasvali.spaceblaster.core.game.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Disposable;
import ee.joonasvali.spaceblaster.core.game.enemy.Enemy;
import ee.joonasvali.spaceblaster.core.game.enemy.EnemyFormation;
import ee.joonasvali.spaceblaster.core.game.enemy.GaussEnemy;
import ee.joonasvali.spaceblaster.core.game.weapons.CannonBullet;
import ee.joonasvali.spaceblaster.core.game.weapons.Missile;
import ee.joonasvali.spaceblaster.core.game.weapons.TripleShotBullet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author Joonas Vali February 2017
 */
public class LevelProvider implements Disposable {
  private static final Logger log = LoggerFactory.getLogger(LevelProvider.class);
  private static final int VERTICAL_DISTANCE_IN_MATRIX = 6;
  private static final int HORIZONTAL_DISTANCE_IN_MATRIX = 6;

  private static final int ENEMY_SIZE = 3;

  private final Texture texture;

  private final Sprite sprite;
  private final Sprite sprite2;
  private final Sprite sprite3;
  private final Sprite sprite4;

  private final Map<Enemy, Sprite> spriteMap = new IdentityHashMap<>();

  private int nextLevel = 0;
  private final int maxLevel;

  private final float worldWidth;
  private final float worldHeight;
  private final LevelReader reader;
  private final String backgroundFileName;
  private final String episodeName;
  private String levelName;

  public LevelProvider(LevelReader reader, float worldWidth, float worldHeight, String episodeName) {
    this.episodeName = episodeName;
    this.reader = reader;
    this.worldWidth = worldWidth;
    this.worldHeight = worldHeight;
    this.maxLevel = reader.getNumberOfLevels();
    this.texture = new Texture(Gdx.files.internal("enemy1.png"));
    this.sprite = new Sprite(texture);
    this.sprite2 = new Sprite(texture);
    sprite2.setColor(new Color(255 / 255f, 162 / 255f, 29 / 255f, 1f));
    this.sprite3 = new Sprite(texture);
    sprite3.setColor(new Color(173 / 255f, 207 / 255f, 255 / 255f, 1f));
    this.sprite4 = new Sprite(texture);
    sprite4.setColor(new Color(145 / 255f, 0 / 255f, 14 / 255f, 1f));
    this.sprite.flip(false, true);
    this.sprite2.flip(false, true);
    this.sprite3.flip(false, true);
    this.sprite4.flip(false, true);
    backgroundFileName = reader.getBackgroundFileName();
  }

  public EnemyFormation nextLevel() {
    spriteMap.clear();
    EnemyFormation formation;
    LevelDescriptor levelDesc = reader.getLevel(nextLevel);
    log.info("loading level " + levelDesc.getName());
    formation = createLevel(levelDesc);
    this.levelName = levelDesc.getName();
    nextLevel++;
    return formation;
  }

  private EnemyFormation createLevel(LevelDescriptor levelDesc) {
    EnemyFormation formation = new EnemyFormation(levelDesc.getWidth(), levelDesc.getHeight(), ENEMY_SIZE, (x, y) -> {
      Enemy enemy = null;

      if (x >= 0 && x < levelDesc.getWidth() && y >= 0 && y < levelDesc.getHeight()) {
        char enemyDesc = levelDesc.getLevel()[y].charAt(x);
        if (isValidEnemy(enemyDesc)) {
          switch (enemyDesc) {
            case 'a' : enemy = createAClassEnemy(x, y); break;
            case 'b' : enemy = createBClassEnemy(x, y); break;
            case 'c' : enemy = createCClassEnemy(x, y); break;
            case 'd' : enemy = createDClassEnemy(x, y); break;
          }
        }
      }

      if (enemy != null) {
        enemy.setSize(ENEMY_SIZE, ENEMY_SIZE);
      }
      return enemy;
    }, HORIZONTAL_DISTANCE_IN_MATRIX, VERTICAL_DISTANCE_IN_MATRIX);

    formation.setX(5);
    formation.setY(worldHeight - Math.min(50, (levelDesc.getHeight() + 2) * (VERTICAL_DISTANCE_IN_MATRIX)));
    return formation;
  }

  private boolean isValidEnemy(char enemyDesc) {
    return enemyDesc == 'a' || enemyDesc == 'b' || enemyDesc == 'c' || enemyDesc == 'd';
  }

  private Enemy createAClassEnemy(int x, int y) {
    Enemy e = new Enemy(CannonBullet.class, 1000, 100, 0.025f, x, y);
    spriteMap.put(e, sprite);
    return e;
  }

  private Enemy createBClassEnemy(int x, int y) {
    Enemy e = new Enemy(Missile.class, 2000, 150, 0.05f, x, y);
    spriteMap.put(e, sprite2);
    return e;
  }

  private Enemy createCClassEnemy(int x, int y) {
    Enemy e = new GaussEnemy(3000, 200, 0.055f, x, y);
    spriteMap.put(e, sprite3);
    return e;
  }

  private Enemy createDClassEnemy(int x, int y) {
    Enemy e = new Enemy(TripleShotBullet.class, 4000, 300, 0.055f, x, y);
    spriteMap.put(e, sprite4);
    return e;
  }


  public Sprite getSprite(Enemy enemy) {
    return spriteMap.getOrDefault(enemy, sprite);
  }

  @Override
  public void dispose() {
    texture.dispose();
  }

  public boolean hasNextLevel() {
    return nextLevel < maxLevel;
  }

  public String getNextLevelName() {
    if (hasNextLevel()) {
      return reader.getLevel(nextLevel).getName();
    }
    return null;
  }

  public String getBackgroundFileName() {
    return backgroundFileName;
  }

  public String getEpisodeName() {
    return episodeName;
  }

  public String getLevelName() {
    return levelName;
  }

  public int getCurrentLevel() {
    return nextLevel - 1;
  }
}
