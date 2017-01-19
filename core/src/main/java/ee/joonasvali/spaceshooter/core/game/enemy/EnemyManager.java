package ee.joonasvali.spaceshooter.core.game.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import ee.joonasvali.spaceshooter.core.game.GameStepListener;
import ee.joonasvali.spaceshooter.core.game.Missile;
import ee.joonasvali.spaceshooter.core.game.MissileManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author Joonas Vali January 2017
 */
public class EnemyManager implements Disposable, GameStepListener {
  public static final int ENEMY_SIZE = 3;
  public static final float FORMATION_DROP = 0.02f;
  private final MissileManager missileManager;
  private final float worldWidth;
  private final float worldHeight;

  private final Texture explosionTexture;
  private final Sprite explosionSprite;
  private final Texture texture;
  private final Sprite sprite;

  private float formationSpeed = 0.1f;

  private Pool<Enemy> enemyPool = new Pool<Enemy>() {
    @Override
    protected Enemy newObject() {
      return new Enemy();
    }
  };

  private Pool<Explosion> explosionPool = new Pool<Explosion>() {
    @Override
    protected Explosion newObject() {
      return new Explosion();
    }
  };

  private EnemyFormation formation;

  private List<Explosion> explosions = new ArrayList<>();

  public EnemyManager(float worldWidth, float worldHeight, MissileManager missileManager) {
    this.missileManager = missileManager;
    this.worldWidth = worldWidth;
    this.worldHeight = worldHeight;
    this.texture = new Texture(Gdx.files.internal("rocket.png"));

    this.explosionTexture = new Texture(Gdx.files.internal("explosion1.png"));
    this.explosionSprite = new Sprite(explosionTexture);
    this.formation = new EnemyFormation(8, 5, () -> {
      Enemy enemy =  enemyPool.obtain();
      enemy.setSize(ENEMY_SIZE, ENEMY_SIZE);
      return enemy;
    }, 6,6);

    this.formation.setX(5);
    this.formation.setY(worldHeight - 75); // TODO

    this.sprite = new Sprite(texture);
    this.sprite.flip(false, true);
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
    for (Enemy e : formation.getEnemies()) {
      sprite.setX(e.getX());
      sprite.setY(e.getY());
      sprite.setSize(e.getWidth(), e.getHeight());
      sprite.draw(batch);
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
    for (Enemy e : formation.getEnemies()) {
      Optional<Missile> m = missileManager.missileCollisionWith(e);
      if (m.isPresent()) {
        createExplosion(e);
        death.add(e);
        missileManager.removeMissile(m.get());
      }
    }

    death.forEach(e -> enemyPool.free(e));
    formation.removeAll(death);

    for (Enemy e : formation.getEnemies()) {
      e.setX(formation.getXof(e));
      e.setY(formation.getYof(e));
    }

    moveFormation();
  }

  private void moveFormation() {
    if (formation.isMovesLeft()) {
      if (formation.getMinX() <= 2) {
        formation.setMovesLeft(false);
        formation.setY(formation.getY() - 2);
        formationSpeed += FORMATION_DROP;
      } else {
        formation.setX(formation.getX() - formationSpeed);
      }
    } else {
      if (formation.getMaxX() >= (98 - ENEMY_SIZE)) {
        formation.setMovesLeft(true);
        formation.setY(formation.getY() - 2);
        formationSpeed += FORMATION_DROP;
      } else {
        formation.setX(formation.getX() + formationSpeed);
      }
    }
  }

  private void createExplosion(Enemy e) {
    Explosion exp = explosionPool.obtain();
    exp.expireTime = 10;
    exp.setX(e.getX());
    exp.setY(e.getY());
    exp.setWidth(e.getWidth());
    exp.setHeight(e.getHeight());
    explosions.add(exp);
  }

  @Override
  public void onStep() {
    act();
  }

  class Explosion extends Rectangle implements Pool.Poolable {
    private int expireTime;

    @Override
    public void reset() {
      this.expireTime = 0;
    }
  }
}
