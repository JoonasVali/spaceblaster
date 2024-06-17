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
public class MissileProvider implements ProjectileProvider<Missile>, Disposable {

  private static final float MISSILE_START_SPEED = 0.01f;
  private static final float MISSILE_SIZE = 0.3f;
  private static final float MISSILE_ACCELERATION = 0.01f;

  private static final int MISSILE_TEXTURE_X = 12;
  private static final int MISSILE_TEXTURE_Y = 1;
  private static final int MISSILE_TEXTURE_WIDTH = 8;
  private static final int MISSILE_TEXTURE_HEIGHT = 30;

  private final Texture missileTexture;
  private final Sprite missileSprite;
  private final Sound[] sounds;


  public MissileProvider(GameState state) {
    this.missileTexture = new Texture(Gdx.files.internal("missile.png"));
    this.missileSprite = new Sprite(missileTexture, MISSILE_TEXTURE_X, MISSILE_TEXTURE_Y, MISSILE_TEXTURE_WIDTH, MISSILE_TEXTURE_HEIGHT);
    this.sounds = new Sound[] {
        state.getSoundManager().getMissileSound(),
        state.getSoundManager().getMissileSound2(),
        state.getSoundManager().getMissileSound3(),
    };
  }


  private Pool<Missile> missilePool = new Pool<Missile>() {
    @Override
    protected Missile newObject() {
      return new Missile(MissileProvider.this);
    }
  };

  @Override
  public void draw(SpriteBatch batch, WeaponProjectile m) {
    missileSprite.setOrigin(m.getWidth() / 2, m.getHeight() / 2);
    missileSprite.setSize(m.getWidth(), m.getHeight());
    missileSprite.setRotation(-m.getAngle()); // why?
    missileSprite.setX(m.getX());
    missileSprite.setY(m.getY());
    missileSprite.draw(batch);
  }

  @Override
  public Sound getSound() {
    return sounds[(int) (Math.random() * sounds.length)];
  }

  @Override
  public int getCoolDown() {
    return 25;
  }

  @Override
  public Missile obtain() {
    Missile projectile = missilePool.obtain();
    projectile.setSpeed(MISSILE_START_SPEED);
    projectile.setAcceleration(MISSILE_ACCELERATION);
    float height = MISSILE_SIZE * missileSprite.getHeight() / missileSprite.getWidth();
    projectile.setWidth(MISSILE_SIZE);
    projectile.setHeight(height);
    projectile.setDamage(1000);
    return projectile;
  }

  @Override
  public void free(WeaponProjectile projectile) {
    missilePool.free((Missile) projectile);
  }

  @Override
  public void dispose() {
    missileTexture.dispose();
  }

}
