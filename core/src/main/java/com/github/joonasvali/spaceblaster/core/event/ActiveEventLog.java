package com.github.joonasvali.spaceblaster.core.event;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.github.joonasvali.spaceblaster.core.game.GameState;
import com.github.joonasvali.spaceblaster.core.game.difficulty.GameSettings;
import com.github.joonasvali.spaceblaster.core.game.enemy.Enemy;
import com.github.joonasvali.spaceblaster.core.game.enemy.EnemyFormation;
import com.github.joonasvali.spaceblaster.core.game.enemy.GaussEnemy;
import com.github.joonasvali.spaceblaster.core.game.weapons.CannonBullet;
import com.github.joonasvali.spaceblaster.core.game.weapons.GaussGunBullet;
import com.github.joonasvali.spaceblaster.core.game.weapons.Missile;
import com.github.joonasvali.spaceblaster.core.game.weapons.TripleShotBullet;
import com.github.joonasvali.spaceblaster.core.game.weapons.WeaponProjectile;
import com.github.joonasvali.spaceblaster.event.Closeness;
import com.github.joonasvali.spaceblaster.event.EnemySpeed;
import com.github.joonasvali.spaceblaster.event.Event;
import com.github.joonasvali.spaceblaster.event.EventType;
import com.github.joonasvali.spaceblaster.event.EventWriter;
import com.github.joonasvali.spaceblaster.event.MovingDirection;
import com.github.joonasvali.spaceblaster.event.PlayerWeapon;
import com.github.joonasvali.spaceblaster.event.PositionX;
import com.github.joonasvali.spaceblaster.event.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.List;

public class ActiveEventLog implements EventLog {
  private final Logger log = LoggerFactory.getLogger(ActiveEventLog.class);
  // Timestamp -> Weapon class
  private final LinkedHashMap<Long, WeaponProjectile> playerShotsFiredRecently = new LinkedHashMap<>();

  private ArrayDeque<Long> playerBulletsHitEnemy = new ArrayDeque<>();
  private ArrayDeque<Long> playerBulletsOutOfBounds = new ArrayDeque<>();
  private ArrayDeque<Long> playerBulletFired = new ArrayDeque<>();

  private GameState gameState;
  private final Statistics statistics;

  private final EventWriter eventWriter;
  private final ArrayDeque<EventType> queuedEvents = new ArrayDeque<>();
  private final OutputStream outputStream;
  private boolean isLocked;
  public ActiveEventLog(GameState gameState, Path eventLogFolder) {
    this.gameState = gameState;
    this.statistics = new Statistics();
    Path path = eventLogFolder.resolve("events-" + System.currentTimeMillis() + ".yml");

    EventWriter eventWriter = null;
    OutputStream outputStream = null;
    try {
      Files.createDirectories(path.getParent());
      outputStream = Files.newOutputStream(path);
      eventWriter = new EventWriter<>(outputStream, new FileImageWriter(eventLogFolder), (screenshotConsumer) -> Gdx.app.postRunnable(() -> {
        Pixmap scnshot = ActiveEventLog.this.captureScreenshot();
        screenshotConsumer.accept(scnshot, scnshot::dispose);
      }));
    } catch (IOException e) {
      log.error("Failed to create event log file", e);
    }
    this.outputStream = outputStream;
    this.eventWriter = eventWriter;
  }

  private void addEvent(EventType eventType) {
    if (!isLocked) {
      if (this.eventWriter != null) {
        eventWriter.write(new Event(statistics, eventType));
      }
    }
    if (eventType == EventType.VICTORY || eventType == EventType.GAME_OVER) {
      isLocked = true;
    }
  }

  private float minPlayerX;
  private float maxPlayerX;

  @Override
  public void eventLoadLevel(String levelName, List<Enemy> enemies, int currentLevel) {
    recalculateCurrentState();
    statistics.initializeRound(levelName,
        (int) enemies.stream().filter(e -> e instanceof GaussEnemy).count(),
        (int) enemies.stream().filter(e -> e.getProjectileType() == Missile.class).count(),
        (int) enemies.stream().filter(e -> e.getProjectileType() == CannonBullet.class).count(),
        (int) enemies.stream().filter(e -> e.getProjectileType() == TripleShotBullet.class).count()
    );

    addEvent(EventType.LOAD_LEVEL);
  }


  private Pixmap captureScreenshot() {
    int width = Gdx.graphics.getBackBufferWidth();
    int height = Gdx.graphics.getBackBufferHeight();

    Pixmap pixmap = Pixmap.createFromFrameBuffer(0, 0, width, height);

    return pixmap;
  }


  @Override
  public void eventStartGame(String episodeName, GameSettings gameSettings, int levelsTotal) {
    recalculateCurrentState();

    statistics.initializeGame(
        episodeName,
        gameSettings.getDifficulty(),
        gameState.getLives().get(),
        levelsTotal
    );
    addEvent(EventType.START_GAME);
  }

  @Override
  public void enemyKilled(Enemy enemy, boolean killedByPlayer) {
    recalculateCurrentState();

    if (!killedByPlayer) {
      statistics.enemiesKilledEnemiesThisRoundCount++;
    } else {
      statistics.enemiesKilledThisRoundCount++;
    }

    statistics.enemiesLeftThisRoundCount--;
    if (enemy instanceof GaussEnemy) {
      statistics.enemiesLeftWithGaussGunCount--;
    } else if (enemy.getProjectileType() == Missile.class) {
      statistics.enemiesLeftWithMissileCount--;
    } else if (enemy.getProjectileType() == CannonBullet.class) {
      statistics.enemiesLeftWithCannonCount--;
    } else if (enemy.getProjectileType() == TripleShotBullet.class) {
      statistics.enemiesLeftWithTripleShotCount--;
    }

    statistics.lastKillTimestamp = System.currentTimeMillis();

    if (!killedByPlayer) {
      addEvent(EventType.ENEMY_KILLED_BY_ENEMY);
    } else {
      addEvent(EventType.ENEMY_KILLED);
    }
  }

  @Override
  public void playerKilled(boolean killedByTouchingEnemy) {
    recalculateCurrentState();

    statistics.lastDeathTimestamp = System.currentTimeMillis();
    if (killedByTouchingEnemy) {
      statistics.enemyTouchedPlayerDeathsCount++;
    }


    addEvent(EventType.PLAYER_KILLED);
  }

  @Override
  public void playerFired(WeaponProjectile projectile) {
    // This isn't an event itself, so no recalculaion is needed here...
    playerShotsFiredRecently.put(System.currentTimeMillis(), projectile);
    playerProjectileCreated();
  }

  @Override
  public void playerProjectileCreated() {
    playerBulletFired.add(System.currentTimeMillis());
  }

  @Override
  public void playerBorn() {
    recalculateCurrentState();
    // Player weapon:
    setWeapon();

    addEvent(EventType.PLAYER_BORN);
  }

  public void setWeapon() {
    var weapon = gameState.getRocket().getWeaponClass();
    if (weapon == CannonBullet.class) {
      statistics.playerWeapon = PlayerWeapon.CANNON;
    } else if (weapon == Missile.class) {
      statistics.playerWeapon = PlayerWeapon.MISSILE;
    } else if (weapon == TripleShotBullet.class) {
      statistics.playerWeapon = PlayerWeapon.TRIPLE_SHOT;
    } else if (weapon == GaussGunBullet.class) {
      statistics.playerWeapon = PlayerWeapon.GAUSS_GUN;
    }
  }

  @Override
  public void playerNoLongerInvincible() {
    recalculateCurrentState();
    addEvent(EventType.PLAYER_NO_LONGER_INVINCIBLE);
  }

  @Override
  public void dispose() {
    if (eventWriter != null) {
      try {
        eventWriter.dispose();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      eventWriter.waitUntilDisposed();
    }

    if (outputStream != null) {
      try {
        outputStream.close();
      } catch (IOException e) {
        log.error("Failed to close event log file", e);
      }
    }
  }

  @Override
  public boolean isActive() {
    return true;
  }

  @Override
  public void trigger() {
    recalculateCurrentState();
  }

  @Override
  public void roundCompleted() {
    recalculateCurrentState();

    statistics.roundsFinishedCount++;
    addEvent(EventType.ROUND_COMPLETED);
  }

  @Override
  public void playerProjectileOutOfBounds(WeaponProjectile b) {
    playerBulletsOutOfBounds.add(System.currentTimeMillis());
  }

  private void recalculateCurrentState() {
    statistics.playerScore = gameState.getScore().get();
    statistics.playerLivesLeft = gameState.getLives().get();
    statistics.playerDead = !gameState.getRocket().isAlive();
    statistics.playerIsMoving = gameState.getRocket().isMoving();
    statistics.playerInvincible = gameState.getRocket().isInvincible();
    statistics.playerFireHitRatio = !playerBulletFired.isEmpty() ? playerBulletsHitEnemy.size() / (float) playerBulletFired.size() : 0;
    float playerPosX = gameState.getRocket().getX();
    float playerPosY = gameState.getRocket().getY();

    if (!statistics.playerDead) {
      PositionX pos = PositionX.CENTER;

      if (playerPosX < minPlayerX + (maxPlayerX - minPlayerX) / 4f) {
        pos = PositionX.LEFT;
      } else if (playerPosX < minPlayerX + (maxPlayerX - minPlayerX) / 3f) {
        pos = PositionX.SLIGHTLY_LEFT;
      } else if (playerPosX > maxPlayerX - (maxPlayerX - minPlayerX) / 4f) {
        pos = PositionX.RIGHT;
      } else if (playerPosX > maxPlayerX - (maxPlayerX - minPlayerX) / 3f) {
        pos = PositionX.SLIGHTLY_RIGHT;
      }
      statistics.playerPositionX = pos;
    } else {
      statistics.playerPositionX = PositionX.MISSING;
    }

    var enemySpeed = gameState.getGameStateManager().getFormationSpeed();
    var minSpeed = gameState.getGameStateManager().getFormationMinSpeed();
    var maxSpeed = gameState.getGameStateManager().getFormationMaxSpeed();

    if (enemySpeed < minSpeed + (maxSpeed - minSpeed) / 3f) {
      statistics.enemySpeed = EnemySpeed.SLOW;
    } else if (enemySpeed < minSpeed + (maxSpeed - minSpeed) / 2f) {
      statistics.enemySpeed = EnemySpeed.MEDIUM;
    } else {
      statistics.enemySpeed = EnemySpeed.FAST;
    }

    EnemyFormation enemyFormation = gameState.getGameStateManager().getEnemyFormation();
    if (enemyFormation != null) {
      float maxEnemyX = enemyFormation.getMaxX();
      float minEnemyX = enemyFormation.getMinX();
      float enemyRightSide = enemyFormation.getEnemies().stream().max(
          (e1, e2) -> Float.compare(e1.getX(), e2.getX())
      ).map(e -> e.getX() + e.getWidth()).orElse(0f);
      float enemyLeftSide = enemyFormation.getEnemies().stream().min(
          (e1, e2) -> Float.compare(e1.getX(), e2.getX())
      ).map(Enemy::getX).orElse(0f);

      if (enemyLeftSide < gameState.getWorldWidth() / 8f) {
        statistics.enemyPositionXOnScreen = PositionX.LEFT;
      } else if (enemyRightSide > gameState.getWorldWidth() * 7f / 8f) {
        statistics.enemyPositionXOnScreen = PositionX.RIGHT;
      } else if (enemyLeftSide < gameState.getWorldWidth() / 4f) {
        statistics.enemyPositionXOnScreen = PositionX.SLIGHTLY_LEFT;
      } else if (enemyRightSide > gameState.getWorldWidth() * 3f / 4f) {
        statistics.enemyPositionXOnScreen = PositionX.SLIGHTLY_RIGHT;
      } else {
        statistics.enemyPositionXOnScreen = PositionX.CENTER;
      }

      // Calculating enemyCloseness:
      Enemy closestEnemy = enemyFormation.getEnemies().stream()
          .min((e1, e2) -> {
            float d1 = Math.abs(e1.getX() - playerPosX) + Math.abs(e1.getY() - playerPosY);
            float d2 = Math.abs(e2.getX() - playerPosX) + Math.abs(e2.getY() - playerPosY);
            return Float.compare(d1, d2);
          }).orElse(null);

      if (closestEnemy != null) {
        float d = Math.abs(closestEnemy.getX() - playerPosX) + Math.abs(closestEnemy.getY() - playerPosY);
        if (d < 20) {
          statistics.enemyCloseness = Closeness.CLOSE;
        } else if (d < 40) {
          statistics.enemyCloseness = Closeness.MEDIUM;
        } else {
          statistics.enemyCloseness = Closeness.FAR;
        }
      }

      // Calculating statistics.playerIsUnderEnemyFormation:
      statistics.playerIsUnderEnemyFormation = true;
      float formationMinimumX = enemyFormation.getX();
      float formationMaximumX = enemyFormation.getEnemies().stream().max(
          (e1, e2) -> Float.compare(e1.getX(), e2.getX())
      ).map(e -> e.getX() + e.getWidth()).orElse(0f);
      statistics.playerIsUnderEnemyFormation = playerPosX + gameState.getRocket().getWidth() > formationMinimumX && playerPosX < formationMaximumX;

    } else {
      statistics.enemyPositionXOnScreen = PositionX.MISSING;
      statistics.enemyMovingDirection = MovingDirection.NONE;
      statistics.playerIsUnderEnemyFormation = false;
    }

    statistics.inBetweenRounds = enemyFormation == null || enemyFormation.getEnemies().isEmpty();

    var projectileFlyingTowardsPlayer = gameState.getGameStateManager().getWeaponProjectileManager().getProjectiles().stream().filter(p ->
        p.x < playerPosX + gameState.getRocket().getWidth() && p.x > playerPosX && p.getAuthor() != gameState.getRocket() && p.y > playerPosY
    ).sorted((o1, o2) -> Float.compare(Math.abs(o1.y - playerPosY), Math.abs(o2.y - playerPosY))).findAny();

    Closeness previousBulletCloseness = statistics.enemyBulletFlyingTowardsPlayerDistance;
    statistics.enemyBulletFlyingTowardsPlayerDistance = projectileFlyingTowardsPlayer.map(
        weaponProjectile ->
            Math.abs(weaponProjectile.y - (playerPosY + gameState.getRocket().getHeight())) < gameState.getRocket().getHeight() * 1.5f
        ? Closeness.CLOSE
        : Closeness.FAR
    ).orElse(null);

    if (previousBulletCloseness == Closeness.CLOSE && statistics.enemyBulletFlyingTowardsPlayerDistance == null && gameState.getRocket().isAlive()) {
      queueEvent(EventType.PLAYER_NARROWLY_ESCAPED_INCOMING_BULLET);
    }

    retainLastThreeSecondsOfPlayerFiredEvents();
    statistics.shotsFiredLastThreeSeconds = playerShotsFiredRecently.size();

    // Make sure event queue is cleared as a last thing.
    queuedEvents.forEach(this::addEvent);
    queuedEvents.clear();
  }

  private void queueEvent(EventType eventType) {
    queuedEvents.add(eventType);
  }

  @Override
  public void setVictory() {
    recalculateCurrentState();

    statistics.isVictory = true;
    statistics.roundsFinishedCount = statistics.totalRoundsCount;
    addEvent(EventType.VICTORY);
  }

  @Override
  public void setGameOver() {
    recalculateCurrentState();

    statistics.isDefeat = true;
    addEvent(EventType.GAME_OVER);
  }
  private void retainLastThreeSecondsOfPlayerFiredEvents() {
    long currentTime = System.currentTimeMillis();
    playerShotsFiredRecently.entrySet().removeIf(e -> currentTime - e.getKey() > 3000);
  }

  @Override
  public void setMinMaxPlayerX(float minX, float maxX) {
    minPlayerX = minX;
    maxPlayerX = maxX;
  }

  @Override
  public Statistics getStatistics() {
    recalculateCurrentState();
    // This should be temporary method, not how events are exposed...
    return statistics;
  }

  @Override
  public void powerUpCollected(Class<? extends WeaponProjectile> powerup) {
    recalculateCurrentState();

    statistics.powerUpsCollectedThisRoundCount++;
    statistics.powerUpsCollectedTotalCount++;

    if (powerup == GaussGunBullet.class) {
      statistics.powerUpsGaussGunCollectedCount++;
    } else if (powerup == Missile.class) {
      statistics.powerUpsMissileCollectedCount++;
    } else if (powerup == TripleShotBullet.class) {
      statistics.powerUpsTripleShotCollectedCount++;
    } else if (powerup == CannonBullet.class) {
      statistics.powerUpsCannonCollectedCount++;
    }
    statistics.lastPowerupTimestamp = System.currentTimeMillis();
    addEvent(EventType.POWERUP_COLLECTED);
  }

  @Override
  public void powerUpMissed() {
    recalculateCurrentState();

    statistics.powerUpsMissedCount++;
    statistics.lastPowerupMissedTimestamp = System.currentTimeMillis();
    addEvent(EventType.POWERUP_MISSED);
  }

  @Override
  public void powerUpCreated() {
    recalculateCurrentState();

    addEvent(EventType.POWERUP_CREATED);
  }

  @Override
  public void enemyHit(Enemy e, boolean hitByPlayer) {
    recalculateCurrentState();

    if (hitByPlayer) {
      statistics.lastHitTimestamp = System.currentTimeMillis();
      playerBulletsHitEnemy.add(System.currentTimeMillis());
    } else {
      statistics.enemiesHitEnemiesThisRoundCount++;
    }
    addEvent(EventType.ENEMY_HIT);

  }

  @Override
  public void setEnemyFormationMovement(MovingDirection movingDirection) {
    recalculateCurrentState();

    boolean changed = statistics.enemyMovingDirection != MovingDirection.NONE &&
        movingDirection != MovingDirection.NONE &&
        statistics.enemyMovingDirection != movingDirection;

    statistics.enemyMovingDirection = movingDirection;
    if (changed) {
      addEvent(EventType.ENEMY_FORMATION_CHANGES_MOVEMENT_DIRECTION);
    }
  }
}
