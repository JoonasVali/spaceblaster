package ee.joonasvali.spaceblaster.core.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

/**
 * @author Joonas Vali March 2017
 */
public class Powerup extends Rectangle implements Pool.Poolable {
  private float speed;

  @Override
  public void reset() {
    x = 0;
    y = 0;
    speed = 0;
  }

  public float getSpeed() {
    return speed;
  }

  public void setSpeed(float speed) {
    this.speed = speed;
  }
}
