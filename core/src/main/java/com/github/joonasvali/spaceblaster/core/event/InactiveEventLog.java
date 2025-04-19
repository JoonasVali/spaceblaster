package com.github.joonasvali.spaceblaster.core.event;

import com.github.joonasvali.spaceblaster.core.game.difficulty.GameSettings;
import com.github.joonasvali.spaceblaster.core.game.enemy.Enemy;
import com.github.joonasvali.spaceblaster.core.game.weapons.WeaponProjectile;
import com.github.joonasvali.spaceblaster.event.MovingDirection;
import com.github.joonasvali.spaceblaster.event.Statistics;

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
  public int getQueuedScreenshotWriteCount() {
    return 0;
  }

  @Override
  public void trigger() {

  }

  @Override
  public void roundCompleted() {

  }

  @Override
  public void playerProjectileOutOfBounds(WeaponProjectile b) {

  }

  @Override
  public void playerFired(WeaponProjectile weaponClass) {

  }

  @Override
  public void playerProjectileCreated() {

  }
}
