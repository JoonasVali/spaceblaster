package ee.joonasvali.libgdxdemo.core;

import com.badlogic.gdx.utils.Pool;

import java.awt.Point;

/**
 * @author Joonas Vali December 2016
 */
public class Missile implements Pool.Poolable {
  private float x = 0;
  private float y = 0;
  private float speed = 0;
  private float angle = 0;
  private float acceleration;
  @Override
  public void reset() {
    x = 0;
    y = 0;
    speed = 0;
    angle = 0;
    acceleration = 0.01f;
  }

  public void setPosition(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public void setSpeed(float speed) {
    this.speed = speed;
  }

  public void setAngle(float angle) {
    this.angle = angle;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public void nextPosition() {
    x = (int) (x + speed * Math.sin(Math.toRadians(angle)));
    y = (int) (y + speed * Math.cos(Math.toRadians(angle)));
    speed += acceleration;
  }
}
