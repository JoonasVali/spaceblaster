package ee.joonasvali.spaceblaster.core.game.weapons;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
import ee.joonasvali.spaceblaster.core.ParticleEffectManager;

import java.util.Optional;

/**
 * @author Joonas Vali January 2017
 */
public abstract class WeaponProjectile extends Rectangle implements Pool.Poolable {
  private float speed = 0;
  private float angle = 0;
  private float acceleration = 0;
  private Object author;
  private int damage;

  @Override
  public void reset() {
    x = 0;
    y = 0;
    width = 0;
    height = 0;
    speed = 0;
    angle = 0;
    acceleration = 0;
    damage = 0;
  }

  public Object getAuthor() {
    return author;
  }

  public void setAuthor(Object author) {
    this.author = author;
  }

  public void setSpeed(float speed) {
    this.speed = speed;
  }

  public void setAngle(float angle) {
    this.angle = angle;
  }

  public void nextPosition() {
    x = (float) (x + speed * Math.sin(Math.toRadians(angle)));
    y = (float) (y + speed * Math.cos(Math.toRadians(angle)));
    speed += acceleration;
  }

  public void setAcceleration(float acceleration) {
    this.acceleration = acceleration;
  }

  public float getSpeed() {
    return speed;
  }

  public float getAngle() {
    return angle;
  }

  public float getAcceleration() {
    return acceleration;
  }

  abstract ProjectileProvider<? extends WeaponProjectile> getProjectileProvider();

  public int getDamage() {
    return damage;
  }

  public void setDamage(int damage) {
    this.damage = damage;
  }

  public Optional<String> getParticlEffectKey() {
    return Optional.empty();
  }
}
