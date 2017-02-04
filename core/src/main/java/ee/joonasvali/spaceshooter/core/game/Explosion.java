package ee.joonasvali.spaceshooter.core.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

public class Explosion extends Rectangle implements Pool.Poolable {

  private int expireTime;

  @Override
  public void reset() {
    this.expireTime = 0;
    this.x = 0;
    this.y = 0;
    this.width = 0;
    this.height = 0;
  }

  public void setExpireTime(int expireTime) {
    this.expireTime = expireTime;
  }

  public int getExpireTime() {
    return expireTime;
  }
}