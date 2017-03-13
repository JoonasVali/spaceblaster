package ee.joonasvali.spaceshooter.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Joonas Vali March 2017
 */
public class PowerupManager implements Disposable, GameStepListener {
  private static final Logger log = LoggerFactory.getLogger(PowerupManager.class);
  private final Pool<Powerup> pool = new Pool<Powerup>() {
    @Override
    protected Powerup newObject() {
      return new Powerup();
    }
  };

  private final GameState state;
  private final Texture powerup;

  private final ArrayList<Powerup> powerups = new ArrayList<>();

  public PowerupManager(GameState state) {
    this.powerup = new Texture(Gdx.files.internal("powerup.png"));
    this.state = state;
  }

  @Override
  public void dispose() {
    powerup.dispose();
    powerups.clear();
  }



  @Override
  public void onStepAction(GameSpeedController.Control control) {
    powerups.forEach(p -> p.setY(p.getY() - p.getSpeed()));

    powerups.removeIf(p -> {
      if (p.getY() < -10) {
        pool.free(p);
        return true;
      }
      return false;
    });
  }

  public void createPowerup(float x, float y, float speed) {
    Powerup p = pool.obtain();
    p.setX(x);
    p.setY(y);
    p.setWidth(3);
    p.setHeight(3);
    p.setSpeed(speed);
    powerups.add(p);
  }

  public void draw(SpriteBatch batch) {
    powerups.forEach(p -> {
      batch.draw(powerup, p.getX(), p.getY(), p.getWidth(), p.getHeight());
    });
  }

  public Optional<Powerup> collisionWith(Rectangle r) {
    for (Powerup m : powerups) {
      if (m.overlaps(r)) {
        return Optional.of(m);
      }
    }
    return Optional.empty();
  }

  public void remove(Powerup p) {
    powerups.remove(p);
    pool.free(p);
  }
}
