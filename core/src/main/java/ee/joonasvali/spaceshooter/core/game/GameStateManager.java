package ee.joonasvali.spaceshooter.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import ee.joonasvali.spaceshooter.core.ParticleEffectManager;
import ee.joonasvali.spaceshooter.core.game.enemy.Enemy;
import ee.joonasvali.spaceshooter.core.game.enemy.EnemyFormation;
import ee.joonasvali.spaceshooter.core.game.level.LevelProvider;
import ee.joonasvali.spaceshooter.core.game.player.Rocket;
import ee.joonasvali.spaceshooter.core.game.weapons.WeaponProjectile;
import ee.joonasvali.spaceshooter.core.game.weapons.WeaponProjectileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Joonas Vali January 2017
 */
public class GameStateManager implements Disposable, GameStepListener {
  private static final Logger log = LoggerFactory.getLogger(GameStateManager.class);
  private static final float FORMATION_SPEED_INCREASE = 0.02f;

  private static final int FIRE_FREQUENCY = 35;

  private static final int FORMATION_DROP = 2;
  private static final float MAX_SPEED = 0.5f;
  public static final int STEPS_TO_SKIP_BEFORE_NEXT_LEVEL = 250;
  public static final int BIRTH_RATE = 5;
  private final TriggerCounter fireTrigger;

  private final WeaponProjectileManager weaponProjectileManager;
  private final ExplosionManager explosionManager;
  private final float worldWidth;
  private final float worldHeight;

  private final GameState state;
  private float formationSpeed;
  private EnemyFormation formation;
  private final AtomicInteger score;

  private int birthCountdown;
  private LevelProvider levels;
  private Sound[] hitSounds;
  private Sound damageSound;

  private boolean loadNextLevelInProgress;


  public GameStateManager(float screenWidth, float screenHeight, GameState state) {
    this.weaponProjectileManager = state.getWeaponProjectileManager();
    this.explosionManager = state.getExplosionManager();
    this.score = state.getScore();
    this.worldWidth = screenWidth;
    this.worldHeight = screenHeight;
    this.state = state;

    this.fireTrigger = new TriggerCounter(this::doEnemyFire, FIRE_FREQUENCY, true);
    this.hitSounds = new Sound[] {
        Gdx.audio.newSound(Gdx.files.internal("sound/hit.mp3")),
        Gdx.audio.newSound(Gdx.files.internal("sound/hit2.mp3"))
    };

    this.damageSound =  Gdx.audio.newSound(Gdx.files.internal("sound/damage.mp3"));

  }

  public void setLevelProvider(LevelProvider levelProvider) {
    this.levels = levelProvider;
  }

  private void doLoadNextLevel() {
    formationSpeed = 0.1f;
    this.formation = levels.nextLevel();
  }

  private Sprite getSprite(Enemy enemy) {
    return levels.getSprite(enemy);
  }

  public void drawEnemies(SpriteBatch batch, float delta) {
    if (formation == null) {
      return;
    }
    for (Enemy e : formation.getEnemies()) {
      if (!e.isBorn()) {
        continue;
      }
      Sprite sprite = getSprite(e);
      sprite.setX(e.getX());
      sprite.setY(e.getY());
      sprite.setSize(e.getWidth(), e.getHeight());
      sprite.draw(batch);
    }
  }


  private void doEnemyFire() {
    if (formation.hasUnbornLeft()) {
      return;
    }
    List<Enemy> enemies = formation.getEnemies();
    if (enemies.isEmpty()) {
      return;
    }

    Optional<Enemy> chosen = formation.getRandomBornFromBottom();
    chosen.ifPresent(Enemy::onFire);
    chosen.ifPresent(enemy -> weaponProjectileManager.createProjectileAt(enemy.getProjectileType(), enemy,
        formation.getXof(enemy) + formation.getEnemySize() / 2,
        formation.getYof(enemy) + formation.getEnemySize() / 2,
        180
    ));
  }


  @Override
  public void dispose() {
    for (Sound hitSound : hitSounds) {
      hitSound.dispose();
    }
    damageSound.dispose();
  }

  private ParticleEffectManager.PositionProvider getPositionProviderOf(Enemy e) {
    return new ParticleEffectManager.PositionProvider() {
      @Override
      public float getX() {
        return e.getX() + e.getWidth() / 2;
      }

      @Override
      public float getY() {
        return e.getY() + e.getHeight() / 2;
      }
    };
  }

  private void act(GameSpeedController.Control control) {
    checkIfNeedToLoadLevel();

    if (formation == null) {
      if (!loadNextLevelInProgress) {
        setLoadNextLevelAfterDelay(control);
      }
      return;
    }

    if (formation.hasUnbornLeft()) {
      if (birthCountdown++ >= BIRTH_RATE) {
        birthCountdown -= BIRTH_RATE;
        formation.getRandomUnBorn().ifPresent(
            e -> {
              e.setBorn(true);
              state.getParticleManager().createParticleEmitter(ParticleEffectManager.BIRTH, getPositionProviderOf(e), 0);
            }
        );
      }
    }

    List<Enemy> dead = new ArrayList<>();

    for (Enemy e : formation.getEnemies()) {
      Optional<WeaponProjectile> m = weaponProjectileManager.projectileCollisionWith(e, e);
      if (m.isPresent()) {
        WeaponProjectile projectile = m.get();
        explosionManager.createExplosion(projectile.getX() - projectile.getWidth() / 2, projectile.getY() - projectile.getHeight() / 2, 1, 1);
        damageSound.play(0.2f, 1f - (float) Math.random() / 5f, 0f);
        state.getParticleManager().createParticleEmitter(ParticleEffectManager.HIT, projectile.getX(), projectile.getY(), projectile.getAngle());

        if (e.decreaseHealthBy(m.get().getDamage())) {
          createExplosion(e);
          state.getParticleManager().createParticleEmitter(ParticleEffectManager.EXPLOSION, e.getX() + e.getWidth() / 2, e.getY() + e.getHeight() / 2, 0);
          dead.add(e);

          // Add score only if player shot the projectile.
          if (projectile.getAuthor() instanceof Rocket) {
            score.addAndGet(e.getBounty());
          }

          Sound sound = hitSounds[(int) (Math.random() * hitSounds.length)];
          sound.play(0.5f);

        }
        weaponProjectileManager.removeProjectile(m.get());
      }
    }


    formation.removeAll(dead);

    formation.getEnemies().stream().filter(e -> e instanceof GameStepListener).forEach(e -> ((GameStepListener)e).onStepAction(control));

    for (Enemy e : formation.getEnemies()) {
      e.setX(formation.getXof(e));
      e.setY(formation.getYof(e));
    }

    moveFormation();

    if (!loadNextLevelInProgress && formation.getEnemies().isEmpty() && !(state.isVictory() || state.isDefeat())) {
      if (levels.hasNextLevel()) {
        setLoadNextLevelAfterDelay(control);
      } else {
        state.setVictory(true);
        state.getUi().displayVictory();
      }
    }

    fireTrigger.countDown();
  }

  private void checkIfNeedToLoadLevel() {
    if (loadNextLevelInProgress) {
      doLoadNextLevel();
      loadNextLevelInProgress = false;
    }
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
      if (formation.getMaxX() >= (98 - formation.getEnemySize())) {
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
  public void onStepAction(GameSpeedController.Control control) {
    act(control);
  }

  public void onStepEffect() {
    if (formation == null) {
      return;
    }

    formation.getEnemies().stream().filter(e -> e instanceof GameStepListener).forEach(e -> ((GameStepListener)e).onStepEffect());

    for (Enemy e : formation.getEnemies()) {
      Rocket rocket = state.getRocket();
      if (rocket.isCollision(e)) {
        rocket.kill();
      }
    }
  }

  private void setLoadNextLevelAfterDelay(GameSpeedController.Control control) {
    state.getUi().displayText(levels.getNextLevelName(), 100, 100);
    control.skipNextSteps(STEPS_TO_SKIP_BEFORE_NEXT_LEVEL);
    loadNextLevelInProgress = true;
  }

}
