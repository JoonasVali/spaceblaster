package ee.joonasvali.spaceblaster.core.game.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import ee.joonasvali.spaceblaster.core.game.GameState;

/**
 * @author Joonas Vali March 2017
 */
public class TripleShotBulletProvider implements ProjectileProvider<TripleShotBullet>, Disposable {
  private static final float START_SPEED = 0.5f;
  private static final float SIZE = 0.3f;
  private static final float ACCELERATION = 0f;

  private final Texture texture;
  private final Sprite sprite;
  private final Sound sound;
  private final GameState state;

  public TripleShotBulletProvider(GameState state) {
    this.texture = new Texture(Gdx.files.internal("gauss.png"));
    this.sprite = new Sprite(texture);
    this.sound = state.getSoundManager().getGaussSound();
    this.state = state;
  }

  public GameState getState() {
    return state;
  }

  private Pool<TripleShotBullet> missilePool = new Pool<TripleShotBullet>() {
    @Override
    protected TripleShotBullet newObject() {
      return new TripleShotBullet(TripleShotBulletProvider.this);
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
  public TripleShotBullet obtain() {
    TripleShotBullet projectile = missilePool.obtain();
    projectile.setSpeed(START_SPEED);
    projectile.setAcceleration(ACCELERATION);
    float height = SIZE * sprite.getHeight() / sprite.getWidth();
    projectile.setWidth(SIZE);
    projectile.setHeight(height);
    projectile.setDamage(1300);
    return projectile;
  }

  @Override
  public void free(WeaponProjectile projectile) {
    missilePool.free((TripleShotBullet) projectile);
  }

  @Override
  public void dispose() {
    texture.dispose();
  }
}
