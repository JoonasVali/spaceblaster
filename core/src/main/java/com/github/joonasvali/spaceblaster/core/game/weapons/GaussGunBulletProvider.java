package com.github.joonasvali.spaceblaster.core.game.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.github.joonasvali.spaceblaster.core.game.GameState;

/**
 * @author Joonas Vali January 2017
 */
public class GaussGunBulletProvider implements ProjectileProvider<GaussGunBullet>, Disposable {
  private static final float START_SPEED = 3f;
  private static final float SIZE = 0.3f;
  private static final float ACCELERATION = 0f;

  private final Texture texture;
  private final Sprite sprite;
  private final Sound sound;

  public GaussGunBulletProvider(GameState state) {
    this.texture = new Texture(Gdx.files.internal("gauss.png"));
    this.sprite = new Sprite(texture);
    this.sound = state.getSoundManager().getGaussSound();
  }


  private Pool<GaussGunBullet> missilePool = new Pool<GaussGunBullet>() {
    @Override
    protected GaussGunBullet newObject() {
      return new GaussGunBullet(GaussGunBulletProvider.this);
    }
  };

  @Override
  public void draw(SpriteBatch batch, WeaponProjectile m) {
    sprite.setOrigin(m.getWidth() / 2, m.getHeight() / 2);
    sprite.setSize(m.getWidth(), m.getHeight());
    sprite.setRotation(-m.getAngle()); // why?
    sprite.setX(m.getX());
    sprite.setY(m.getY());
    sprite.draw(batch);
  }

  @Override
  public Sound getSound() {
    return sound;
  }

  @Override
  public int getCoolDown() {
    return 30;
  }

  @Override
  public GaussGunBullet obtain() {
    GaussGunBullet projectile = missilePool.obtain();
    projectile.setSpeed(START_SPEED);
    projectile.setAcceleration(ACCELERATION);
    float height = SIZE * sprite.getHeight() / sprite.getWidth();
    projectile.setWidth(SIZE);
    projectile.setHeight(height);
    projectile.setDamage(3000);
    return projectile;
  }

  @Override
  public void free(WeaponProjectile projectile) {
    missilePool.free((GaussGunBullet) projectile);
  }

  @Override
  public void dispose() {
    texture.dispose();
  }
}
