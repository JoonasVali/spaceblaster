package ee.joonasvali.spaceblaster.core.event;

import ee.joonasvali.spaceblaster.core.game.difficulty.GameSettings;
import ee.joonasvali.spaceblaster.core.game.enemy.Enemy;
import ee.joonasvali.spaceblaster.core.game.weapons.WeaponProjectile;
import ee.joonasvali.spaceblaster.event.MovingDirection;
import ee.joonasvali.spaceblaster.event.Statistics;

import java.util.List;

public interface EventLog {
  void eventLoadLevel(String levelName, List<Enemy> enemies, int currentLevel);

  void eventStartGame(String episodeName, GameSettings gameSettings, int levelsTotal);

  void enemyKilled(Enemy enemy, boolean killedByPlayer);

  void playerKilled(boolean killedByTouchingEnemy);

  void playerFired(WeaponProjectile projectile);
  void playerProjectileCreated();

  void playerBorn();

  void setVictory();

  void setGameOver();

  void setMinMaxPlayerX(float minX, float maxX);

  Statistics getStatistics();

  void powerUpCollected(Class<? extends WeaponProjectile> powerup);

  void powerUpMissed();

  void powerUpCreated();

  void enemyHit(Enemy e, boolean hitByPlayer);

  void setEnemyFormationMovement(MovingDirection movingDirection);

  void setWeapon();

  void playerNoLongerInvincible();

  void dispose();

  boolean isActive();

  /**
   * Make sure event log is aware and calculating its state internally even if there are no events at the moment.
   */
  void trigger();

  void roundCompleted();
  void playerProjectileOutOfBounds(WeaponProjectile b);

}
