package ee.joonasvali.spaceblaster.core.event;

import ee.joonasvali.spaceblaster.core.game.difficulty.GameSettings;
import ee.joonasvali.spaceblaster.core.game.enemy.Enemy;
import ee.joonasvali.spaceblaster.core.game.weapons.WeaponProjectile;
import ee.joonasvali.spaceblaster.event.Event;

import java.util.List;

public interface EventLog {
  void addEvent();

  void eventLoadLevel(String levelName, List<Enemy> enemies, int currentLevel);

  void eventStartGame(String episodeName, GameSettings gameSettings, int levelsTotal);

  void enemyKilled(Enemy enemy, boolean killedByPlayer);

  void playerKilled(boolean killedByTouchingEnemy);

  void playerFired(Class<? extends WeaponProjectile> weaponClass);

  void playerBorn();

  void setVictory();

  void setGameOver();

  void setMinMaxPlayerX(float minX, float maxX);

  Event getStatistics();

  void powerUpCollected(Class<? extends WeaponProjectile> powerup);

  void powerUpMissed();

  void enemyHit(Enemy e, boolean hitByPlayer);
}
