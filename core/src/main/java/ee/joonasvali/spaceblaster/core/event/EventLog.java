package ee.joonasvali.spaceblaster.core.event;

import com.badlogic.gdx.math.Rectangle;
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
import ee.joonasvali.spaceblaster.event.PlayerWeapon;
import ee.joonasvali.spaceblaster.event.PositionX;

import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.List;

public class EventLog {
  // Timestamp -> Weapon class
  private final LinkedHashMap<Long, Class<? extends WeaponProjectile>> playerShotsFiredRecently = new LinkedHashMap<>();

  private ArrayDeque<Event> log = new ArrayDeque<>();
  private GameState gameState;
  private final Event statistics;
  public EventLog(GameState gameState) {
    this.gameState = gameState;
    this.statistics = new Event();
  }

  public void addEvent() {
    Event event = new Event();


    log.add(event);
  }

  private long startTimeGame;
  private long startTimeLevel;

  private float minPlayerX;
  private float maxPlayerX;

  public void eventLoadLevel(String levelName, List<Enemy> enemies, int currentLevel) {
    recalculateCurrentState();
    startTimeLevel = System.currentTimeMillis();
    System.out.println("EventLog.eventLoadLevel");
    statistics.initializeRound(levelName,
        Math.max(0, currentLevel + 1),
        (int) enemies.stream().filter(e -> e instanceof GaussEnemy).count(),
        (int) enemies.stream().filter(e -> e.getProjectileType() == Missile.class).count(),
        (int) enemies.stream().filter(e -> e.getProjectileType() == CannonBullet.class).count(),
        (int) enemies.stream().filter(e -> e.getProjectileType() == TripleShotBullet.class).count()
    );
    // TODO generate event:
  }

  public void eventStartGame(String episodeName, GameSettings gameSettings) {
    recalculateCurrentState();
    startTimeGame = System.currentTimeMillis();
    System.out.println("EventLog.eventStartGame");
    statistics.initializeGame(
        episodeName,
        gameSettings.getDifficulty(),
        gameState.getLives().get()
    );
    // TODO generate event:
  }

  public void enemyKilled(Enemy enemy, boolean killedByPlayer) {
    recalculateCurrentState();
    System.out.println("EventLog.enemyKilled: " + killedByPlayer);
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
    // TODO generate event:
  }

  public void playerKilled(boolean killedByTouchingEnemy) {
    recalculateCurrentState();
    System.out.println("EventLog.playerKilled");

    if (killedByTouchingEnemy) {
      statistics.enemyTouchedPlayerDeathsCount++;
    }


    // TODO generate event:

  }

  public void playerFired(Class<? extends WeaponProjectile> weaponClass) {
    // This isn't an event itself, so no recalculaion is needed here...
    playerShotsFiredRecently.put(System.currentTimeMillis(), weaponClass);
    System.out.println("EventLog.playerFired " + weaponClass);
  }

  public void playerBorn() {
    recalculateCurrentState();
    System.out.println("EventLog.playerBorn");
    // Player weapon:
    setWeapon();

    // TODO generate event:
  }

  private void setWeapon() {
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

  private void recalculateCurrentState() {
    long currentTime = System.currentTimeMillis();
    statistics.recalculateTimes(currentTime - statistics.timeSinceLastEventMs);
    statistics.playerScore = gameState.getScore().get();
    statistics.playerLivesLeft = gameState.getLives().get();
    statistics.playerDead = !gameState.getRocket().isAlive();
    statistics.playerIsMoving = gameState.getRocket().isMoving();
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
      float enemyPosition = enemyFormation.getX();
      float maxEnemyX = enemyFormation.getMaxX();
      float minEnemyX = enemyFormation.getMinX();
      if (enemyPosition < minEnemyX + (maxEnemyX - minEnemyX) / 4f) {
        statistics.enemyPositionXOnScreen = PositionX.LEFT;
      } else if (enemyPosition < minEnemyX + (maxEnemyX - minEnemyX) / 3f) {
        statistics.enemyPositionXOnScreen = PositionX.SLIGHTLY_LEFT;
      } else if (enemyPosition > maxEnemyX - (maxEnemyX - minEnemyX) / 4f) {
        statistics.enemyPositionXOnScreen = PositionX.RIGHT;
      } else if (enemyPosition > maxEnemyX - (maxEnemyX - minEnemyX) / 3f) {
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
        // TODO verify this logic:
        float d = Math.abs(closestEnemy.getX() - playerPosX) + Math.abs(closestEnemy.getY() - playerPosY);
        if (d < 100) {
          statistics.enemyCloseness = EnemyCloseness.CLOSE;
        } else if (d < 200) {
          statistics.enemyCloseness = EnemyCloseness.MEDIUM;
        } else {
          statistics.enemyCloseness = EnemyCloseness.FAR;
        }
      }

      // Calculating statistics.playerIsUnderEnemyFormation:
      statistics.playerIsUnderEnemyFormation = true;
      float formationMinimumX = enemyFormation.getX();
      float formationMaximumX = enemyFormation.getEnemies().stream().max((e1, e2) -> Float.compare(e1.getX(), e2.getX())).map(Rectangle::getX).orElse(0f);
      if (playerPosX > formationMinimumX && playerPosX < formationMaximumX) {
        statistics.playerIsUnderEnemyFormation = true;
      }

    } else {
      statistics.enemyPositionXOnScreen = PositionX.MISSING;
      statistics.playerIsUnderEnemyFormation = false;
    }

    statistics.inBetweenRounds = enemyFormation == null || enemyFormation.getEnemies().isEmpty();

    statistics.enemyBulletFlyingTowardsPlayer = gameState.getGameStateManager().getWeaponProjectileManager().getProjectiles().stream().anyMatch(p ->
        p.x < playerPosX + gameState.getRocket().getWidth() && p.x > playerPosX
    );


    retainLastThreeSecondsOfPlayerFiredEvents();
    statistics.shotsFiredLastThreeSeconds = playerShotsFiredRecently.size();
  }

  private void retainLastThreeSecondsOfPlayerFiredEvents() {
    long currentTime = System.currentTimeMillis();
    playerShotsFiredRecently.entrySet().removeIf(e -> currentTime - e.getKey() > 3000);
  }

  public void setMinMaxPlayerX(float minX, float maxX) {
    minPlayerX = minX;
    maxPlayerX = maxX;
  }

  public Event getStatistics() {
    recalculateCurrentState();
    // This should be temporary method, not how events are exposed...
    return statistics;
  }
}
