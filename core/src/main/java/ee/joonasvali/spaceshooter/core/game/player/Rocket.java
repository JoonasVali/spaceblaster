package ee.joonasvali.spaceshooter.core.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import ee.joonasvali.spaceshooter.core.game.Explosion;
import ee.joonasvali.spaceshooter.core.game.ExplosionManager;
import ee.joonasvali.spaceshooter.core.game.GameStepListener;
import ee.joonasvali.spaceshooter.core.game.RebirthEffect;
import ee.joonasvali.spaceshooter.core.game.weapons.Missile;
import ee.joonasvali.spaceshooter.core.game.weapons.WeaponProjectile;
import ee.joonasvali.spaceshooter.core.game.weapons.WeaponProjectileManager;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Joonas Vali December 2016
 */
public class Rocket implements Disposable, GameStepListener {
  public static final int ROCKET_SIZE = 3;
  private static final float ROCKET_SPEED = 1;
  public static final int TIME_TO_REBIRTH = 150;
  private final Sprite sprite;
  private final Rectangle rectangle;
  private final Texture texture;
  private final WeaponProjectileManager weaponProjectileManager;
  private final ExplosionManager explosionManager;
  private final AtomicInteger lives;

  private Effect effect;
  private boolean alive;
  private int countDownToRebirth;

  private float xTarget;

  public Rocket(WeaponProjectileManager weaponProjectileManager, ExplosionManager explosionManager, AtomicInteger lives) {
    this.weaponProjectileManager = weaponProjectileManager;
    this.explosionManager = explosionManager;
    this.lives = lives;
    texture = new Texture(Gdx.files.internal("rocket.png"));

    rectangle = new Rectangle(0, 0, ROCKET_SIZE, ROCKET_SIZE);
    sprite = new Sprite(texture);
    sprite.setSize(ROCKET_SIZE, ROCKET_SIZE);

    alive = true;
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
    if (alive) {
      if (effect == null) {
        sprite.draw(batch);
      } else {
        effect.draw(sprite, batch);
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
    if (effect != null) {
      effect.onStep();
      if (!effect.isActive()) {
        effect = null;
      }
    }
    if (!alive) {
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
    if (!isInvincible()) {
      Optional<WeaponProjectile> missile = weaponProjectileManager.projectileCollisionWith(rectangle, this);
      missile.ifPresent((m) -> {
        weaponProjectileManager.removeProjectile(m);
        explosionManager.createExplosion(getX(), getY(), getWidth(), getHeight());
        this.alive = false;
        countDownToRebirth = TIME_TO_REBIRTH;
        lives.decrementAndGet();
      });
    }
  }

  private boolean isInvincible() {
    return effect instanceof RebirthEffect;
  }

  public void fireMissile() {
    if (!alive) {
      return;
    }
    this.weaponProjectileManager.createProjectileAt(Missile.class,
        this,this.getX() + Rocket.ROCKET_SIZE / 2, this.getY() + Rocket.ROCKET_SIZE / 2,
        (float) Math.random() * 10 - 5
    );
  }

  private void rebirth() {
    if (lives.get() > 0) {
      alive = true;
      effect = new RebirthEffect(200, 5);
    }
  }

  public float getWidth() {
    return rectangle.getWidth();
  }

  public float getHeight() {
    return rectangle.getHeight();
  }
}
