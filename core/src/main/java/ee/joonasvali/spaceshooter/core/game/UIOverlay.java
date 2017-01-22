package ee.joonasvali.spaceshooter.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Disposable;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Joonas Vali January 2017
 */
public class UIOverlay implements Disposable {
  public static final int UNITS = 1000;
  private OrthographicCamera cam;
  private BitmapFont font;

  private final AtomicInteger score;
  private final float width;
  private final float height;

  public UIOverlay(AtomicInteger score) {
    this.score = score;
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    // Constructs a new OrthographicCamera, using the given viewport width and height
    // Height is multiplied by aspect ratio.
    width = UNITS;
    height = width * (h / w);
    cam = new OrthographicCamera(width, height);

    cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
    cam.update();

    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/cambria.ttc"));
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    parameter.size = 20;
    parameter.color = Color.YELLOW;
    font = generator.generateFont(parameter);
  }

  public void draw(SpriteBatch batch) {
    batch.setProjectionMatrix(cam.combined);
    font.draw(batch, Integer.toString(score.get()), 20, height - 10);
  }

  @Override
  public void dispose() {
    font.dispose();
  }
}
