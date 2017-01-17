package ee.joonasvali.spaceshooter.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

/**
 * @author Joonas Vali December 2016
 */
public class Rocket implements Disposable, GameStepListener {

  private static final float MISSILE_START_SPEED = 0.01f;
  private static final float MISSILE_SIZE = 0.3f;
  private static final float MISSILE_ACCELERATION = 0.01f;

  public static final int ROCKET_SIZE = 5;
  private static final float ROCKET_SPEED = 1;
  private final Sprite sprite;
  private final Rectangle rectangle;
  private final Texture texture;

  private float xTarget;

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
    xTarget = x;
    rectangle.y = y;
    // Y is fixed right now.
    sprite.setY(y);
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

  @Override
  public void onStep() {
    float xMove = 0;
    if (xTarget < this.getX()) {
      xMove = -Math.min(ROCKET_SPEED, this.getX() - xTarget);
    } else if (xTarget > this.getX()) {
      xMove = Math.min(ROCKET_SPEED, xTarget - this.getX());
    }
    rectangle.x += xMove;
    sprite.setX(rectangle.getX());
  }
}
