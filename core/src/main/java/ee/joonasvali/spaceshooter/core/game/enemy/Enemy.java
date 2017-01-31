package ee.joonasvali.spaceshooter.core.game.enemy;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

/**
 * @author Joonas Vali January 2017
 */
public class Enemy extends Rectangle {

  private int matrixPosX;
  private int matrixPosY;
  private int bounty;
  private int health;

  public Enemy(int health, int bounty, int x, int y) {
    this.bounty = bounty;
    this.matrixPosX = x;
    this.matrixPosY = y;
    this.health = health;
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

  /**
   *
   * @return true if dead
   */
  public boolean decreaseHealthBy(int damage) {
    health -= damage;
    return health <= 0;
  }
}
