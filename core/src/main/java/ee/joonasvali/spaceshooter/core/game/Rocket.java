package ee.joonasvali.spaceshooter.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

import java.util.Optional;

/**
 * @author Joonas Vali December 2016
 */
public class Rocket implements Disposable, GameStepListener {

  private static final float MISSILE_START_SPEED = 0.01f;
  private static final float MISSILE_SIZE = 0.3f;
  private static final float MISSILE_ACCELERATION = 0.01f;

  public static final int ROCKET_SIZE = 3;
  private static final float ROCKET_SPEED = 1;
  public static final int TIME_TO_REBIRTH = 300;
  private final Sprite sprite;
  private final Sprite explosionSprite;
  private final Rectangle rectangle;
  private final Texture texture;
  private final Texture explosionTexture;
  private final MissileManager missileManager;

  private boolean alive;
  private Explosion explosion;
  private int countDownToRebirth;

  private float xTarget;

  public Rocket(MissileManager missileManager) {
    this.missileManager = missileManager;
    texture = new Texture(Gdx.files.internal("rocket.png"));
    explosionTexture = new Texture(Gdx.files.internal("explosion1.png"));
    this.explosionSprite = new Sprite(explosionTexture);
    rectangle = new Rectangle(0, 0, ROCKET_SIZE, ROCKET_SIZE);
    sprite = new Sprite(texture);
    sprite.setSize(ROCKET_SIZE, ROCKET_SIZE);

    alive = true;
  }

  @Override
  public void dispose() {
    texture.dispose();
    explosionTexture.dispose();
  }

  public void setPosition(float x, float y) {
    xTarget = x;
    rectangle.y = y;
    // Y is fixed right now.
    sprite.setY(y);
  }

  public void draw(SpriteBatch batch) {
    if (alive) {
      sprite.draw(batch);
    } else {
      if (explosion != null && explosion.getExpireTime() > 0) {
        explosionSprite.setX(explosion.getX());
        explosionSprite.setY(explosion.getY());
        explosionSprite.setSize(explosion.getWidth(), explosion.getHeight());
        explosionSprite.setOrigin(explosion.getWidth() / 2, explosion.getHeight() / 2);
        explosionSprite.setRotation((explosion.getExpireTime() * 5) % 360); // Make it rotate
        explosionSprite.draw(batch);
      }
    }
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


  @Override
  public void onStep() {
    if (!alive) {
      if (explosion != null) {
        explosion.setExpireTime(explosion.getExpireTime() - 1);
      }
      countDownToRebirth--;
      if (countDownToRebirth <= 0) {
        rebirth();
      }
      return;
    }
    float xMove = 0;
    if (xTarget < this.getX()) {
      xMove = -Math.min(ROCKET_SPEED, this.getX() - xTarget);
    } else if (xTarget > this.getX()) {
      xMove = Math.min(ROCKET_SPEED, xTarget - this.getX());
    }
    rectangle.x += xMove;
    sprite.setX(rectangle.getX());
    Optional<Missile> missile = missileManager.missileCollisionWith(rectangle, this);
    missile.ifPresent((m) -> {
      missileManager.removeMissile(m);
      Explosion exp = Explosion.obtain();
      exp.setExpireTime(10);
      exp.setX(getX());
      exp.setY(getY());
      exp.setWidth(getWidth());
      exp.setHeight(getHeight());
      this.explosion = exp;
      this.alive = false;
      countDownToRebirth = TIME_TO_REBIRTH;
    });

  }

  public void fireMissile() {
    if (!alive) {
      return;
    }
    this.missileManager.createMissileAt(this, this.getX() + Rocket.ROCKET_SIZE / 2, this.getY() + Rocket.ROCKET_SIZE / 2,
        (float) Math.random() * 10 - 5, MISSILE_ACCELERATION, MISSILE_START_SPEED, MISSILE_SIZE
    );
  }

  private void rebirth() {
    alive = true;
    explosion = null;
  }

  public float getWidth() {
    return rectangle.getWidth();
  }

  public float getHeight() {
    return rectangle.getHeight();
  }
}
