package ee.joonasvali.spaceshooter.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Joonas Vali January 2017
 */
public class MissileManager implements Disposable, GameStepListener {

  private static final int MISSILE_TEXTURE_X = 12;
  private static final int MISSILE_TEXTURE_Y = 2;
  private static final int MISSILE_TEXTURE_WIDTH = 8;
  private static final int MISSILE_TEXTURE_HEIGHT = 26;
  private Texture missileTexture;
  private Sprite sprite;
  private float worldHeight;
  private float worldWidth;

  public MissileManager(float worldWidth, float worldHeight) {
    this.worldWidth = worldWidth;
    this.worldHeight = worldHeight;
    this.missileTexture = new Texture(Gdx.files.internal("missile.png"));
    this.sprite = new Sprite(missileTexture, MISSILE_TEXTURE_X, MISSILE_TEXTURE_Y, MISSILE_TEXTURE_WIDTH, MISSILE_TEXTURE_HEIGHT);
  }

  private Pool<Missile> missilePool = new Pool<Missile>() {
    @Override
    protected Missile newObject() {
      return new Missile();
    }
  };

  private List<Missile> activeMissiles = new ArrayList<>();

  private void moveAndRemoveMissiles() {
    activeMissiles.forEach(Missile::nextPosition);

    List<Missile> outOfBounds =
        activeMissiles.stream().filter(
            m -> m.getY() > worldHeight + 5 || m.getY() < -5 || m.getX() > worldWidth + 5 || m.getX() < -5
        ).collect(Collectors.toList());

    outOfBounds.forEach(this::removeMissile);
  }

  public void removeMissile(Missile m) {
    activeMissiles.remove(m);
    missilePool.free(m);
  }


  public Missile createMissileAt(float x, float y, float angle, float acceleration, float startSpeed, float size) {
    Missile missile = missilePool.obtain();
    missile.setPosition(x, y);
    missile.setAngle(angle);
    missile.setSpeed(startSpeed);
    missile.setAcceleration(acceleration);
    float height = size * sprite.getHeight() / sprite.getWidth();
    missile.setWidth(size);
    missile.setHeight(height);
    activeMissiles.add(missile);
    return missile;
  }

  public void drawMissiles(SpriteBatch batch) {
    activeMissiles.forEach(m -> {
      sprite.setOrigin(m.getWidth() / 2, m.getHeight() / 2);
      sprite.setSize(m.getWidth(), m.getHeight());
      sprite.setRotation(-m.getAngle()); // why?
      sprite.setX(m.getX());
      sprite.setY(m.getY());
      sprite.draw(batch);
    });
  }

  @Override
  public void dispose() {
    missilePool.clear();
    activeMissiles.clear();
    missileTexture.dispose();
  }

  public Optional<Missile> missileCollisionWith(Rectangle r) {
    for (Missile m : activeMissiles) {
      if (m.overlaps(r)) {
        return Optional.of(m);
      }
    }
    return Optional.empty();
  }

  @Override
  public void onStep() {
    moveAndRemoveMissiles();
  }

}
