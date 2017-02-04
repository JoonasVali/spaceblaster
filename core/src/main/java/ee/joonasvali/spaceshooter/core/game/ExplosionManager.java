package ee.joonasvali.spaceshooter.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Joonas Vali February 2017
 */
public class ExplosionManager implements Disposable, GameStepListener {

  public static final int DEFAULT_EXPIRE_TIME = 10;
  private final Texture explosionTexture;
  private final Sprite explosionSprite;

  private final List<Explosion> explosions = new ArrayList<>();

  public ExplosionManager() {
    this.explosionTexture = new Texture(Gdx.files.internal("explosion1.png"));
    this.explosionSprite = new Sprite(explosionTexture);
  }

  public void draw(SpriteBatch batch) {
    for (Explosion exp : explosions) {
      explosionSprite.setX(exp.getX());
      explosionSprite.setY(exp.getY());
      float sizeModifier = Math.max(0, (float) Math.sin(exp.getExpireTime() / 4));
      explosionSprite.setSize(exp.getWidth() - sizeModifier, exp.getHeight() - sizeModifier);
      explosionSprite.setOrigin(exp.getWidth() / 2, exp.getHeight() / 2);
      explosionSprite.setRotation((exp.getExpireTime() * 5) % 360); // Make it rotate
      explosionSprite.draw(batch);
    }
  }


  @Override
  public void dispose() {
    explosionTexture.dispose();
  }


  public void createExplosion(float x, float y, float width, float height) {
    Explosion exp = Explosion.obtain();
    exp.setExpireTime(DEFAULT_EXPIRE_TIME);
    exp.setX(x);
    exp.setY(y);
    exp.setWidth(width);
    exp.setHeight(height);
    explosions.add(exp);
  }

  @Override
  public void onStep() {
    Iterator<Explosion> it = explosions.iterator();
    while (it.hasNext()) {
      Explosion e = it.next();
      e.setExpireTime(e.getExpireTime() - 1);
      if (e.getExpireTime() <= 0) {
        Explosion.free(e);
        it.remove();
      }
    }
  }
}
