package ee.joonasvali.spaceshooter.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joonas Vali January 2017
 */
public class EnemyManager implements Disposable, GameStepListener {
  private final MissileManager missileManager;
  private final float worldWidth;
  private final float worldHeight;

  private final Texture texture;
  private final Sprite sprite;

  private Pool<Enemy> enemyPool = new Pool<Enemy>() {
    @Override
    protected Enemy newObject() {
      return new Enemy();
    }
  };

  private List<Enemy> enemies = new ArrayList<>();

  public EnemyManager(float worldWidth, float worldHeight, MissileManager missileManager) {
    this.missileManager = missileManager;
    this.worldWidth = worldWidth;
    this.worldHeight = worldHeight;
    this.texture = new Texture(Gdx.files.internal("rocket.png"));
    this.sprite = new Sprite(texture);
    this.sprite.flip(false, true);
    createEnemies(1);
  }

  private void createEnemies(int n) {
    for (int i = 0; i < n; i++) {
      Enemy e = enemyPool.obtain();
      e.set(5, worldHeight - 50, 5, 5);
      enemies.add(e);

    }
  }

  public void drawEnemies(SpriteBatch batch) {
    for (Enemy e : enemies) {
      sprite.setX(e.getX());
      sprite.setY(e.getY());
      sprite.setSize(e.getWidth(), e.getHeight());
      sprite.draw(batch);
    }
  }

  private void moveEnemy(Enemy e, float delta) {

    if (e.isMovingLeft()) {
      if (e.getX() < 5) {
        e.setMoving(false);
      } else {
        e.setX(e.getX() - delta);
      }
    } else {
      if (e.getX() > worldWidth - 5) {
        e.setMoving(true);
      } else {
        e.setX(e.getX() + delta);
      }
    }
  }

  @Override
  public void dispose() {
    texture.dispose();
  }

  public void act() {
    List<Enemy> death = new ArrayList<>();
    for (Enemy e : enemies) {
      if (missileManager.missileCollisionWith(e)) {
        death.add(e);
      }
      else {
        moveEnemy(e, 1);
      }
    }
    death.forEach(e -> enemyPool.free(e));
    enemies.removeAll(death);
  }

  @Override
  public void onStep() {
    act();
  }
}
