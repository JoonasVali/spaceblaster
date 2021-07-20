package ee.joonasvali.spaceblaster.core.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import ee.joonasvali.spaceblaster.core.ParticleEffectManager;
import ee.joonasvali.spaceblaster.core.game.ExplosionManager;
import ee.joonasvali.spaceblaster.core.game.GameSpeedController;
import ee.joonasvali.spaceblaster.core.game.GameState;
import ee.joonasvali.spaceblaster.core.game.GameStepListener;
import ee.joonasvali.spaceblaster.core.game.Powerup;
import ee.joonasvali.spaceblaster.core.game.weapons.CannonBullet;
import ee.joonasvali.spaceblaster.core.game.weapons.WeaponProjectile;
import ee.joonasvali.spaceblaster.core.game.weapons.WeaponProjectileManager;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Joonas Vali December 2016
 */
public class Rocket implements Disposable, GameStepListener {
  private static final Class<? extends WeaponProjectile> DEFAULT_WEAPON_CLASS = CannonBullet.class;
  public static final float ROCKET_SIZE = 3;
  private static final float ROCKET_SPEED = 1;
  public static final int TIME_TO_REBIRTH = 150;
  public static final float ROCKET_EXPLOSION_VOLUME = 0.75f;
  public static final float ROCKET_FIRE_ANGLE_VARIATION = 3;
  private final Sprite sprite;
  private final Rectangle rectangle;
  private final Texture texture;
  private final WeaponProjectileManager weaponProjectileManager;
  private final ExplosionManager explosionManager;
  private final AtomicInteger lives;
  private final GameState state;

  private Class<? extends WeaponProjectile> weaponClass = DEFAULT_WEAPON_CLASS;

  private Effect effect;
  private boolean alive;
  private int countDownToRebirth;
  private int weaponCooldown;

  private final Sound[] explosionSounds;
  private float xTarget;

  public Rocket(GameState state) {
    this.weaponProjectileManager = state.getWeaponProjectileManager();
    this.explosionManager = state.getExplosionManager();
    this.lives = state.getLives();
    this.state = state;
    texture = new Texture(Gdx.files.internal("rocket.png"));

    this.explosionSounds = new Sound[] {
        state.getSoundManager().getRocketExplosionSound(),
    };

    rectangle = new Rectangle(0, 0, ROCKET_SIZE, ROCKET_SIZE);
    sprite = new Sprite(texture, 1, 1, 31, 31);
    sprite.setSize(ROCKET_SIZE, ROCKET_SIZE);

    alive = true;
  }

  @Override
  public void dispose() {
    texture.dispose();
  }

  public void setPosition(float x, float y) {
    xTarget = Math.max(0, x);
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
  public void onStepAction(GameSpeedController.Control control) {
    cooldown();
    if (effect != null) {
      effect.onStepAction(control);
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
        explosionManager.createExplosion(m.getX(), m.getY(), m.getWidth(), m.getHeight());
        kill();
      });
    }

    Optional<Powerup> p = state.getPowerupManager().collisionWith(rectangle);
    if (p.isPresent()) {
      state.getPowerupManager().remove(p.get());
      setNewRandomWeapon();
      state.getPowerupManager().playPowerupSound();
    }
  }

  private void setNewRandomWeapon() {
    weaponCooldown = 0;
    Class<WeaponProjectile>[] wpClasses = weaponProjectileManager.getWeaponClasses(weaponClass);
    this.weaponClass = wpClasses[(int) (Math.random() * wpClasses.length)];
  }

  private void cooldown() {
    if (weaponCooldown > 0) {
      weaponCooldown--;
    }
  }

  private boolean isInvincible() {
    return effect instanceof RebirthEffect;
  }

  public void doFire() {
    if (!alive || weaponCooldown > 0) {
      return;
    }
    this.weaponProjectileManager.createProjectileAt(weaponClass,
        this,this.getX() + Rocket.ROCKET_SIZE / 2, this.getY() + Rocket.ROCKET_SIZE / 2,
        (float) Math.random() * ROCKET_FIRE_ANGLE_VARIATION - ROCKET_FIRE_ANGLE_VARIATION / 2
    );
    this.weaponCooldown = weaponProjectileManager.getCooldown(weaponClass);
  }

  private void rebirth() {
    if (lives.get() > 0) {
      alive = true;
      effect = new RebirthEffect(200, 5);
    }
    this.weaponClass = DEFAULT_WEAPON_CLASS;
  }

  public float getWidth() {
    return rectangle.getWidth();
  }

  public float getHeight() {
    return rectangle.getHeight();
  }

  public void kill() {
    if (!alive || isInvincible()) {
      return;
    }
    explosionManager.createExplosion(getX(), getY(), getWidth(), getHeight());
    state.getParticleManager().createParticleEmitter(ParticleEffectManager.EXPLOSION, getX() + getWidth() / 2, getY() + getHeight() / 2, 0);
    explosionSounds[(int) (Math.random() * explosionSounds.length)].play(ROCKET_EXPLOSION_VOLUME);
    this.alive = false;
    countDownToRebirth = TIME_TO_REBIRTH;
    lives.decrementAndGet();
    if (lives.get() <= 0) {
      state.getUi().displayGameOver();
    }
  }

  @Override
  public void onStepEffect() {
    if (effect != null) {
      effect.onStepEffect();
    }
  }
}
