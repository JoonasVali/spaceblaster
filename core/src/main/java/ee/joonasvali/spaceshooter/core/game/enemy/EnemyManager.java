package ee.joonasvali.spaceshooter.core.game.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import ee.joonasvali.spaceshooter.core.game.ExplosionManager;
import ee.joonasvali.spaceshooter.core.game.GameState;
import ee.joonasvali.spaceshooter.core.game.GameStepListener;
import ee.joonasvali.spaceshooter.core.game.TriggerCounter;
import ee.joonasvali.spaceshooter.core.game.player.Rocket;
import ee.joonasvali.spaceshooter.core.game.weapons.GaussGunBullet;
import ee.joonasvali.spaceshooter.core.game.weapons.Missile;
import ee.joonasvali.spaceshooter.core.game.weapons.WeaponProjectile;
import ee.joonasvali.spaceshooter.core.game.weapons.WeaponProjectileManager;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Joonas Vali January 2017
 */
public class EnemyManager implements Disposable, GameStepListener {
  private static final int ENEMY_SIZE = 3;
  private static final float FORMATION_SPEED_INCREASE = 0.02f;

  private static final int FIRE_FREQUENCY = 50;
  public static final int VERTICAL_DISTANCE_IN_MATRIX = 6;
  public static final int HORIZONTAL_DISTANCE_IN_MATRIX = 6;
  public static final int FORMATION_HEIGHT_AMOUNT = 5;
  public static final int FORMATION_WIDTH_AMOUNT = 8;
  public static final int FORMATION_DROP = 2;
  public static final float MAX_SPEED = 0.5f;
  private final TriggerCounter fireTrigger;

  private final WeaponProjectileManager weaponProjectileManager;
  private final ExplosionManager explosionManager;
  private final float worldWidth;
  private final float worldHeight;

  private final Texture texture;

  private final Sprite sprite;
  private final Sprite sprite2;
  private final Sprite sprite3;

  private final Map<Enemy, Sprite> spriteMap = new IdentityHashMap<>();

  private final GameState state;
  private float formationSpeed = 0.1f;
  private EnemyFormation formation;
  private final AtomicInteger score;

  public EnemyManager(float screenWidth, float screenHeight, GameState state) {
    this.weaponProjectileManager = state.getWeaponProjectileManager();
    this.explosionManager = state.getExplosionManager();
    this.score = state.getScore();
    this.worldWidth = screenWidth;
    this.worldHeight = screenHeight;
    this.state = state;
    this.texture = new Texture(Gdx.files.internal("Gunship_mf_Sprite.png"));
    this.fireTrigger = new TriggerCounter(this::doEnemyFire, FIRE_FREQUENCY, true);

    this.sprite = new Sprite(texture, 33, 77, 190, 100);
    this.sprite2 = new Sprite(texture, 33, 77, 190, 100);
    sprite2.setColor(Color.YELLOW);
    this.sprite3 = new Sprite(texture, 33, 77, 190, 100);
    sprite3.setColor(Color.BLUE);
  }

  public void setFormation() {
    this.formation = new EnemyFormation(FORMATION_WIDTH_AMOUNT, FORMATION_HEIGHT_AMOUNT, (x, y) -> {
      Enemy enemy;

      if (y == 0) {
        enemy = new Enemy(GaussGunBullet.class, 3000, 200, x, y);
        spriteMap.put(enemy, sprite2);
      } else if (x == 0 || x == FORMATION_WIDTH_AMOUNT - 1) {
        enemy = new Enemy(Missile.class, 2000, 150, x, y);
        spriteMap.put(enemy, sprite3);
      } else {
        enemy = new Enemy(Missile.class, 1000, 100, x, y);
        spriteMap.put(enemy, sprite);
      }
      enemy.setSize(ENEMY_SIZE, ENEMY_SIZE);
      return enemy;
    }, HORIZONTAL_DISTANCE_IN_MATRIX, VERTICAL_DISTANCE_IN_MATRIX);

    this.formation.setX(5);
    this.formation.setY(worldHeight - Math.min(50, (FORMATION_HEIGHT_AMOUNT + 2) * (VERTICAL_DISTANCE_IN_MATRIX)));

  }

  public Sprite getSprite(Enemy enemy) {
    return spriteMap.getOrDefault(enemy, sprite);
  }

  public void drawEnemies(SpriteBatch batch) {
    for (Enemy e : formation.getEnemies()) {
      Sprite sprite = getSprite(e);
      sprite.setX(e.getX());
      sprite.setY(e.getY());
      sprite.setSize(e.getWidth(), e.getHeight());
      sprite.draw(batch);
    }
  }

  public void doEnemyFire() {
    List<Enemy> enemies = formation.getEnemies();
    if (enemies.isEmpty()) {
      return;
    }

    Optional<Enemy> chosen = formation.getRandomFromBottom();
    chosen.ifPresent(enemy -> weaponProjectileManager.createProjectileAt(enemy.getProjectileType(), enemy,
        formation.getXof(enemy) + ENEMY_SIZE / 2,
        formation.getYof(enemy) + ENEMY_SIZE / 2,
        180
    ));
  }


  @Override
  public void dispose() {
    texture.dispose();
  }

  private void act() {
    List<Enemy> dead = new ArrayList<>();
    for (Enemy e : formation.getEnemies()) {
      Optional<WeaponProjectile> m = weaponProjectileManager.projectileCollisionWith(e, e);
      if (m.isPresent()) {
        WeaponProjectile projectile = m.get();
        explosionManager.createExplosion(projectile.getX() - projectile.getWidth() / 2, projectile.getY() - projectile.getHeight() / 2, 1, 1);
        if (e.decreaseHealthBy(m.get().getDamage())) {
          createExplosion(e);
          dead.add(e);
          // Add score only if player shot the projectile.
          if (projectile.getAuthor() instanceof Rocket) {
            score.addAndGet(e.getBounty());
          }
        }
        weaponProjectileManager.removeProjectile(m.get());
      }
    }

    formation.removeAll(dead);

    for (Enemy e : formation.getEnemies()) {
      e.setX(formation.getXof(e));
      e.setY(formation.getYof(e));
    }

    moveFormation();

    fireTrigger.countDown();
  }

  private void moveFormation() {
    float bottomBorder = -formation.getLowestYPositionOffset() + 1;
    if (formation.isMovesLeft()) {
      if (formation.getMinX() <= 2) {
        formation.setMovesLeft(false);
        formation.setY(Math.max(bottomBorder, formation.getY() - FORMATION_DROP));
        formationSpeed += FORMATION_SPEED_INCREASE;
        formationSpeed = Math.min(MAX_SPEED, formationSpeed);

      } else {
        formation.setX(formation.getX() - formationSpeed);
      }
    } else {
      if (formation.getMaxX() >= (98 - ENEMY_SIZE)) {
        formation.setMovesLeft(true);
        formation.setY(Math.max(bottomBorder, formation.getY() - FORMATION_DROP));
        formationSpeed += FORMATION_SPEED_INCREASE;
        formationSpeed = Math.min(MAX_SPEED, formationSpeed);

      } else {
        formation.setX(formation.getX() + formationSpeed);
      }
    }
  }

  private void createExplosion(Enemy e) {
    explosionManager.createExplosion(e.getX(), e.getY(), e.getWidth(), e.getHeight());
  }


  @Override
  public void onStepAction() {
    act();
  }

  public void onStepEffect() {
    for (Enemy e : formation.getEnemies()) {
      Rocket rocket = state.getRocket();
      if (rocket.isCollision(e)) {
        rocket.kill();
      }
    }
  }

}
