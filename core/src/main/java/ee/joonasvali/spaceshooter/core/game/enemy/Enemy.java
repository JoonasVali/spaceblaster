package ee.joonasvali.spaceshooter.core.game.enemy;

import com.badlogic.gdx.math.Rectangle;
import ee.joonasvali.spaceshooter.core.game.weapons.WeaponProjectile;

/**
 * @author Joonas Vali January 2017
 */
public class Enemy extends Rectangle {

  private int matrixPosX;
  private int matrixPosY;
  private int bounty;
  private int health;
  private float offsetX;
  private float offsetY;
  private Class<? extends WeaponProjectile> weapon;
  private boolean born;
  private float chanceOfPowerup;

  public Enemy(Class<? extends WeaponProjectile> weapon, int health, int bounty, float chanceOfPowerup, int x, int y) {
    this.weapon = weapon;
    this.bounty = bounty;
    this.matrixPosX = x;
    this.matrixPosY = y;
    this.health = health;
    this.chanceOfPowerup = chanceOfPowerup;
  }

  public float getOffsetX() {
    return offsetX;
  }

  public void setOffsetX(float offsetX) {
    this.offsetX = offsetX;
  }

  public float getOffsetY() {
    return offsetY;
  }

  public void setOffsetY(float offsetY) {
    this.offsetY = offsetY;
  }

  public int getMatrixPosX() {
    return matrixPosX;
  }

  public int getMatrixPosY() {
    return matrixPosY;
  }

  public int getBounty() {
    return bounty;
  }

  public int getHealth() {
    return health;
  }

  public Class<? extends WeaponProjectile> getProjectileType() {
    return weapon;
  }

  public boolean isBorn() {
    return born;
  }

  public void setBorn(boolean born) {
    this.born = born;
  }

  /**
   *
   * @return true if dead
   */
  public boolean decreaseHealthBy(int damage) {
    health -= damage;
    return health <= 0;
  }

  public void onFire() {

  }

  public float getChanceOfPowerup() {
    return chanceOfPowerup;
  }
}
