package ee.joonasvali.spaceshooter.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Joonas Vali January 2017
 */
public class EnemyManager implements Disposable, GameStepListener {
  private final MissileManager missileManager;
  private final float worldWidth;
  private final float worldHeight;

  private final Texture explosionTexture;
  private final Sprite explosionSprite;
  private final Texture texture;
  private final Sprite sprite;

  private Pool<Enemy> enemyPool = new Pool<Enemy>() {
    @Override
    protected Enemy newObject() {
      return new Enemy();
    }
  };

  private Pool<Explosion> explosionPool = new Pool<Explosion>() {
    @Override
    protected Explosion newObject() {
      return new Explosion(50);
    }
  };

  private List<Enemy> enemies = new ArrayList<>();
  private List<Explosion> explosions = new ArrayList<>();

  public EnemyManager(float worldWidth, float worldHeight, MissileManager missileManager) {
    this.missileManager = missileManager;
    this.worldWidth = worldWidth;
    this.worldHeight = worldHeight;
    this.texture = new Texture(Gdx.files.internal("rocket.png"));

    this.explosionTexture = new Texture(Gdx.files.internal("explosion1.png"));
    this.explosionSprite = new Sprite(explosionTexture);

    this.sprite = new Sprite(texture);
    this.sprite.flip(false, true);
    createEnemies(1);
  }

  private void createEnemies(int n) {
    for (int i = 0; i < n; i++) {
      Enemy e = enemyPool.obtain();
      e.set(5, worldHeight - 50, 5, 5);
      e.setSpeed(0.2f);
      enemies.add(e);

    }
  }

  public void drawEnemies(SpriteBatch batch) {
    for (Explosion exp : explosions) {
      explosionSprite.setX(exp.getX());
      explosionSprite.setY(exp.getY());
      explosionSprite.setSize(exp.getWidth(), exp.getHeight());
      explosionSprite.setOrigin(exp.getWidth() / 2, exp.getHeight() / 2);
      explosionSprite.setRotation((exp.expireTime * 5) % 360); // Make it rotate
      explosionSprite.draw(batch);
    }
    for (Enemy e : enemies) {
      sprite.setX(e.getX());
      sprite.setY(e.getY());
      sprite.setSize(e.getWidth(), e.getHeight());
      sprite.draw(batch);
    }


  }

  private void moveEnemy(Enemy e) {

    if (e.isMovingLeft()) {
      if (e.getX() < 5) {
        e.setMoving(false);
      } else {
        e.setX(e.getX() - e.getSpeed());
      }
    } else {
      if (e.getX() > worldWidth - 5) {
        e.setMoving(true);
      } else {
        e.setX(e.getX() + e.getSpeed());
      }
    }
  }

  @Override
  public void dispose() {
    texture.dispose();
    explosionTexture.dispose();
  }

  public void act() {
    Iterator<Explosion> it = explosions.iterator();
    while (it.hasNext()) {
      Explosion e = it.next();
      e.expireTime--;
      if (e.expireTime <= 0) {
        explosionPool.free(e);
        it.remove();
      }
    }

    List<Enemy> death = new ArrayList<>();
    for (Enemy e : enemies) {
      if (missileManager.missileCollisionWith(e)) {
        Explosion exp = explosionPool.obtain();
        exp.setX(e.getX());
        exp.setY(e.getY());
        exp.setWidth(e.getWidth());
        exp.setHeight(e.getHeight());
        explosions.add(exp);
        death.add(e);
      }
      else {
        moveEnemy(e);
      }
    }
    death.forEach(e -> enemyPool.free(e));
    enemies.removeAll(death);
  }

  @Override
  public void onStep() {
    act();
  }

  class Explosion extends Rectangle implements Pool.Poolable {
    private int expireTime;

    public Explosion(int expireTime) {
      this.expireTime = expireTime;
    }

    @Override
    public void reset() {
      this.expireTime = 0;
    }
  }
}
