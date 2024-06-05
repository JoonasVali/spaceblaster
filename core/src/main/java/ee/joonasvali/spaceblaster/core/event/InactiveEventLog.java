package ee.joonasvali.spaceblaster.core.event;

import ee.joonasvali.spaceblaster.core.game.difficulty.GameSettings;
import ee.joonasvali.spaceblaster.core.game.enemy.Enemy;
import ee.joonasvali.spaceblaster.core.game.weapons.WeaponProjectile;
import ee.joonasvali.spaceblaster.event.MovingDirection;
import ee.joonasvali.spaceblaster.event.Statistics;

import java.util.List;

public class InactiveEventLog implements EventLog {
  @Override
  public void eventLoadLevel(String levelName, List<Enemy> enemies, int currentLevel) {

  }

  @Override
  public void eventStartGame(String episodeName, GameSettings gameSettings, int levelsTotal) {

  }

  @Override
  public void enemyKilled(Enemy enemy, boolean killedByPlayer) {

  }

  @Override
  public void playerKilled(boolean killedByTouchingEnemy) {

  }

  @Override
  public void playerFired(Class<? extends WeaponProjectile> weaponClass) {

  }

  @Override
  public void playerBorn() {

  }

  @Override
  public void setVictory() {

  }

  @Override
  public void setGameOver() {

  }

  @Override
  public void setMinMaxPlayerX(float minX, float maxX) {

  }

  @Override
  public Statistics getStatistics() {
    return null;
  }

  @Override
  public void powerUpCollected(Class<? extends WeaponProjectile> powerup) {

  }

  @Override
  public void powerUpMissed() {

  }

  @Override
  public void powerUpCreated() {

  }

  @Override
  public void enemyHit(Enemy e, boolean hitByPlayer) {

  }

  @Override
  public void setEnemyFormationMovement(MovingDirection movingDirection) {

  }

  @Override
  public void setWeapon() {

  }

  @Override
  public void playerNoLongerInvincible() {

  }

  @Override
  public void dispose() {

  }

  @Override
  public boolean isActive() {
    return false;
  }

  @Override
  public void trigger() {

  }
}
