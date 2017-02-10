package ee.joonasvali.spaceshooter.core.game.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

/**
 * @author Joonas Vali January 2017
 */
public class CannonBulletProvider implements ProjectileProvider<CannonBullet>, Disposable {
  private static final float START_SPEED = 0.7f;
  private static final float SIZE = 0.3f;
  private static final float ACCELERATION = 0f;

  private final Texture texture;
  private final Sprite sprite;
  private final Sound sound;

  public CannonBulletProvider() {
    this.texture = new Texture(Gdx.files.internal("cannon.png"));
    this.sprite = new Sprite(texture);
    this.sound = Gdx.audio.newSound(Gdx.files.internal("sound/cannon.mp3"));
  }


  private Pool<CannonBullet> missilePool = new Pool<CannonBullet>() {
    @Override
    protected CannonBullet newObject() {
      return new CannonBullet(CannonBulletProvider.this);
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
  public CannonBullet obtain() {
    CannonBullet projectile = missilePool.obtain();
    projectile.setSpeed(START_SPEED);
    projectile.setAcceleration(ACCELERATION);
    float height = SIZE * sprite.getHeight() / sprite.getWidth();
    projectile.setWidth(SIZE);
    projectile.setHeight(height);
    projectile.setDamage(1000);
    return projectile;
  }

  @Override
  public void free(WeaponProjectile projectile) {
    missilePool.free((CannonBullet) projectile);
  }

  @Override
  public void dispose() {
    sound.dispose();
    texture.dispose();
  }
}
