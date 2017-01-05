package ee.joonasvali.spaceshooter.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

/**
 * @author Joonas Vali December 2016
 */
public class Rocket implements Disposable {

  private static final float MISSILE_START_SPEED = 0.01f;
  private static final float MISSILE_SIZE = 0.3f;
  private static final float MISSILE_ACCELERATION = 0.01f;

  public static final int ROCKET_SIZE = 5;
  private final Sprite sprite;
  private final Rectangle rectangle;
  private final Texture texture;

  public Rocket() {
    texture = new Texture(Gdx.files.internal("rocket.png"));
    rectangle = new Rectangle(0,0,ROCKET_SIZE, ROCKET_SIZE);
    sprite = new Sprite(texture);
    sprite.setSize(ROCKET_SIZE, ROCKET_SIZE);
  }

  @Override
  public void dispose() {
    texture.dispose();
  }

  public void setPosition(float x, float y) {
    rectangle.setPosition(x, y);
    sprite.setPosition(x, y);
  }

  public void draw(SpriteBatch batch) {
    sprite.draw(batch);
  }

  public float getX() {
    return rectangle.getX();
  }

  public float getY() {
    return rectangle.getY();
  }

  public boolean isCollision(Rectangle rectangle) {
    return this.rectangle.overlaps(rectangle);
  }

  public float getMissileStartSpeed() {
    return MISSILE_START_SPEED;
  }

  public float getMissileSize() {
    return MISSILE_SIZE;
  }

  public float getMissileAcceleration() {
    return MISSILE_ACCELERATION;
  }
}
