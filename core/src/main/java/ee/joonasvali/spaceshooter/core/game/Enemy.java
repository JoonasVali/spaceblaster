package ee.joonasvali.spaceshooter.core.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

/**
 * @author Joonas Vali January 2017
 */
public class Enemy extends Rectangle implements Pool.Poolable {

  private boolean movingLeft;
  private float speed;

  public void setMoving(boolean movingLeft) {
    this.movingLeft = movingLeft;
  }

  public boolean isMovingLeft() {
    return movingLeft;
  }

  @Override
  public void reset() {
    this.movingLeft = false;
    this.x = 0;
    this.y = 0;
    this.width = 0;
    this.height = 0;
    this.speed = 0;
  }

  public float getSpeed() {
    return speed;
  }

  public void setSpeed(float speed) {
    this.speed = speed;
  }
}
