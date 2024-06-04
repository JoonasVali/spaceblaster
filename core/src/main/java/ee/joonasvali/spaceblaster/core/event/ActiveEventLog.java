package ee.joonasvali.spaceblaster.core.event;

import ee.joonasvali.spaceblaster.core.CreditsScreen;
import ee.joonasvali.spaceblaster.core.game.GameState;
import ee.joonasvali.spaceblaster.core.game.difficulty.GameSettings;
import ee.joonasvali.spaceblaster.core.game.enemy.Enemy;
import ee.joonasvali.spaceblaster.core.game.enemy.EnemyFormation;
import ee.joonasvali.spaceblaster.core.game.enemy.GaussEnemy;
import ee.joonasvali.spaceblaster.core.game.weapons.CannonBullet;
import ee.joonasvali.spaceblaster.core.game.weapons.GaussGunBullet;
import ee.joonasvali.spaceblaster.core.game.weapons.Missile;
import ee.joonasvali.spaceblaster.core.game.weapons.TripleShotBullet;
import ee.joonasvali.spaceblaster.core.game.weapons.WeaponProjectile;
import ee.joonasvali.spaceblaster.event.EnemyCloseness;
import ee.joonasvali.spaceblaster.event.EnemySpeed;
import ee.joonasvali.spaceblaster.event.Event;
import ee.joonasvali.spaceblaster.event.EventType;
import ee.joonasvali.spaceblaster.event.EventWriter;
import ee.joonasvali.spaceblaster.event.MovingDirection;
import ee.joonasvali.spaceblaster.event.PlayerWeapon;
import ee.joonasvali.spaceblaster.event.PositionX;
import ee.joonasvali.spaceblaster.event.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;

public class ActiveEventLog implements EventLog {
  private final Logger log = LoggerFactory.getLogger(ActiveEventLog.class);
  // Timestamp -> Weapon class
  private final LinkedHashMap<Long, Class<? extends WeaponProjectile>> playerShotsFiredRecently = new LinkedHashMap<>();
  private GameState gameState;
  private final Statistics statistics;

  private final EventWriter eventWriter;
  private final OutputStream outputStream;
  public ActiveEventLog(GameState gameState, String eventLogFolder) {
    this.gameState = gameState;
    this.statistics = new Statistics();
    Path path = Paths.get(eventLogFolder, "events-" + System.currentTimeMillis() + ".yml");

    EventWriter eventWriter = null;
    OutputStream outputStream = null;
    try {
      Files.createDirectories(path.getParent());
      outputStream = Files.newOutputStream(path);
      eventWriter = new EventWriter(outputStream);
    } catch (IOException e) {
      log.error("Failed to create event log file", e);
    }
    this.outputStream = outputStream;
    this.eventWriter = eventWriter;
  }

  private void addEvent(EventType eventType) {
    if (this.eventWriter != null) {
      eventWriter.write(new Event(statistics, eventType));
    }
  }

  private float minPlayerX;
  private float maxPlayerX;

  @Override
  public void eventLoadLevel(String levelName, List<Enemy> enemies, int currentLevel) {
    recalculateCurrentState();
    statistics.initializeRound(levelName,
        Math.max(0, currentLevel + 1),
        (int) enemies.stream().filter(e -> e instanceof GaussEnemy).count(),
        (int) enemies.stream().filter(e -> e.getProjectileType() == Missile.class).count(),
        (int) enemies.stream().filter(e -> e.getProjectileType() == CannonBullet.class).count(),
        (int) enemies.stream().filter(e -> e.getProjectileType() == TripleShotBullet.class).count()
    );

    addEvent(EventType.LOAD_LEVEL);
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
    addEvent(EventType.ENEMY_KILLED);
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
  public void playerFired(Class<? extends WeaponProjectile> weaponClass) {
    // This isn't an event itself, so no recalculaion is needed here...
    playerShotsFiredRecently.put(System.currentTimeMillis(), weaponClass);
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
    InterruptedException ex = null;
    if (eventWriter != null) {
      eventWriter.dispose();
      try {
        eventWriter.waitUntilDisposed();
      } catch (InterruptedException e) {
        ex = e;
      }
    }

    if (outputStream != null) {
      try {
        outputStream.close();
      } catch (IOException e) {
        log.error("Failed to close event log file", e);
      }
    }
    if (ex != null) {
      Thread.currentThread().interrupt();
    }
  }

  private void recalculateCurrentState() {
    statistics.playerScore = gameState.getScore().get();
    statistics.playerLivesLeft = gameState.getLives().get();
    statistics.playerDead = !gameState.getRocket().isAlive();
    statistics.playerIsMoving = gameState.getRocket().isMoving();
    statistics.playerInvincible = gameState.getRocket().isInvincible();
    float playerPosX = gameState.getRocket().getX();

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
      float playerPosY = gameState.getRocket().getY();
      Enemy closestEnemy = enemyFormation.getEnemies().stream()
          .min((e1, e2) -> {
            float d1 = Math.abs(e1.getX() - playerPosX) + Math.abs(e1.getY() - playerPosY);
            float d2 = Math.abs(e2.getX() - playerPosX) + Math.abs(e2.getY() - playerPosY);
            return Float.compare(d1, d2);
          }).orElse(null);

      if (closestEnemy != null) {
        float d = Math.abs(closestEnemy.getX() - playerPosX) + Math.abs(closestEnemy.getY() - playerPosY);
        if (d < 20) {
          statistics.enemyCloseness = EnemyCloseness.CLOSE;
        } else if (d < 40) {
          statistics.enemyCloseness = EnemyCloseness.MEDIUM;
        } else {
          statistics.enemyCloseness = EnemyCloseness.FAR;
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

    statistics.enemyBulletFlyingTowardsPlayer = gameState.getGameStateManager().getWeaponProjectileManager().getProjectiles().stream().anyMatch(p ->
        p.x < playerPosX + gameState.getRocket().getWidth() && p.x > playerPosX
    );


    retainLastThreeSecondsOfPlayerFiredEvents();
    statistics.shotsFiredLastThreeSeconds = playerShotsFiredRecently.size();
  }

  @Override
  public void setVictory() {
    recalculateCurrentState();

    statistics.isVictory = true;
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
