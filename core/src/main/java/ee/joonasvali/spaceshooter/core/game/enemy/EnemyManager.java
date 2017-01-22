package ee.joonasvali.spaceshooter.core.game.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import ee.joonasvali.spaceshooter.core.game.Explosion;
import ee.joonasvali.spaceshooter.core.game.GameStepListener;
import ee.joonasvali.spaceshooter.core.game.Missile;
import ee.joonasvali.spaceshooter.core.game.MissileManager;
import ee.joonasvali.spaceshooter.core.game.TriggerCounter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Joonas Vali January 2017
 */
public class EnemyManager implements Disposable, GameStepListener {
  private static final int ENEMY_SIZE = 3;
  private static final float FORMATION_DROP = 0.02f;

  private static final int FIRE_FREQUENCY = 50;
  public static final int VERTICAL_DISTANCE_IN_MATRIX = 6;
  public static final int HORIZONTAL_DISTANCE_IN_MATRIX = 6;
  private final TriggerCounter fireTrigger;

  private final MissileManager missileManager;
  private final float worldWidth;
  private final float worldHeight;

  private final Texture explosionTexture;
  private final Sprite explosionSprite;
  private final Texture texture;
  private final Sprite sprite;

  private float formationSpeed = 0.1f;

  private final Pool<Enemy> enemyPool = new Pool<Enemy>() {
    @Override
    protected Enemy newObject() {
      return new Enemy();
    }
  };

  private final EnemyFormation formation;

  private final List<Explosion> explosions = new ArrayList<>();
  private final AtomicInteger score;

  public EnemyManager(float worldWidth, float worldHeight, MissileManager missileManager, AtomicInteger score) {
    this.missileManager = missileManager;
    this.score = score;
    this.worldWidth = worldWidth;
    this.worldHeight = worldHeight;
    this.texture = new Texture(Gdx.files.internal("Gunship_mf_Sprite.png"));
    this.fireTrigger = new TriggerCounter(this::doEnemyFire, FIRE_FREQUENCY, true);

    this.explosionTexture = new Texture(Gdx.files.internal("explosion1.png"));
    this.explosionSprite = new Sprite(explosionTexture);
    this.formation = new EnemyFormation(8, 5, () -> {
      Enemy enemy =  enemyPool.obtain();
      enemy.setSize(ENEMY_SIZE, ENEMY_SIZE);
      return enemy;
    }, HORIZONTAL_DISTANCE_IN_MATRIX, VERTICAL_DISTANCE_IN_MATRIX);

    this.formation.setX(5);
    this.formation.setY(worldHeight - (formation.getHeight() * (VERTICAL_DISTANCE_IN_MATRIX + ENEMY_SIZE) + 10)); // TODO what's with the height?

    this.sprite = new Sprite(texture, 33, 77, 190, 100);
  }

  public void drawEnemies(SpriteBatch batch) {
    for (Explosion exp : explosions) {
      explosionSprite.setX(exp.getX());
      explosionSprite.setY(exp.getY());
      explosionSprite.setSize(exp.getWidth(), exp.getHeight());
      explosionSprite.setOrigin(exp.getWidth() / 2, exp.getHeight() / 2);
      explosionSprite.setRotation((exp.getExpireTime() * 5) % 360); // Make it rotate
      explosionSprite.draw(batch);
    }
    for (Enemy e : formation.getEnemies()) {
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
    chosen.ifPresent(enemy -> missileManager.createMissileAt(enemy,
        formation.getXof(enemy) + ENEMY_SIZE / 2,
        formation.getYof(enemy) + ENEMY_SIZE / 2,
        180,
        0.01f,
        0.5f,
        0.3f
    ));
  }


  @Override
  public void dispose() {
    texture.dispose();
    explosionTexture.dispose();
  }

  private void act() {
    Iterator<Explosion> it = explosions.iterator();
    while (it.hasNext()) {
      Explosion e = it.next();
      e.setExpireTime(e.getExpireTime() - 1);
      if (e.getExpireTime() <= 0) {
        Explosion.free(e);
        it.remove();
      }
    }

    List<Enemy> death = new ArrayList<>();
    for (Enemy e : formation.getEnemies()) {
      Optional<Missile> m = missileManager.missileCollisionWith(e, e);
      if (m.isPresent()) {
        createExplosion(e);
        death.add(e);
        score.addAndGet(e.getBounty());
        missileManager.removeMissile(m.get());
      }
    }

    death.forEach(e -> enemyPool.free(e));
    formation.removeAll(death);

    for (Enemy e : formation.getEnemies()) {
      e.setX(formation.getXof(e));
      e.setY(formation.getYof(e));
    }

    moveFormation();

    fireTrigger.countDown();
  }

  private void moveFormation() {
    if (formation.isMovesLeft()) {
      if (formation.getMinX() <= 2) {
        formation.setMovesLeft(false);
        formation.setY(formation.getY() - 2);
        formationSpeed += FORMATION_DROP;
      } else {
        formation.setX(formation.getX() - formationSpeed);
      }
    } else {
      if (formation.getMaxX() >= (98 - ENEMY_SIZE)) {
        formation.setMovesLeft(true);
        formation.setY(formation.getY() - 2);
        formationSpeed += FORMATION_DROP;
      } else {
        formation.setX(formation.getX() + formationSpeed);
      }
    }
  }

  private void createExplosion(Enemy e) {
    Explosion exp = Explosion.obtain();
    exp.setExpireTime(10);
    exp.setX(e.getX());
    exp.setY(e.getY());
    exp.setWidth(e.getWidth());
    exp.setHeight(e.getHeight());
    explosions.add(exp);
  }

  @Override
  public void onStep() {
    act();
  }

}
