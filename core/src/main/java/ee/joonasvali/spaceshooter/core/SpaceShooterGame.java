package ee.joonasvali.spaceshooter.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class SpaceShooterGame extends Game {

  private static final Logger log = LoggerFactory.getLogger(SpaceShooterGame.class);

  private BitmapFont font16;
  private SpriteBatch batch;
  private float elapsed;
  private Set<Disposable> disposables = new HashSet<>();

  private boolean exit;

  @Override
  public void create() {
    log.info("Starting game!!!");

    batch = new SpriteBatch();

    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/cambria.ttc"));
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

    parameter.size = 16;
    parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
    parameter.spaceX = 1;

    font16 = generator.generateFont(parameter);

    generator.dispose();

    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();
    this.setScreen(new MainMenuScreen(this, 100, 100 * (h / w)));

  }

  @Override
  public void render() {
    if (exit) {
      Gdx.app.exit();
    }
    elapsed += Gdx.graphics.getDeltaTime();
    Gdx.gl.glClearColor(0, 0, 0, 0);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    super.render();

  }

  public float getElapsed() {
    return elapsed;
  }

  @Override
  public void pause() {
  }

  @Override
  public void resume() {
  }

  @Override
  public void dispose() {
    batch.dispose();
    font16.dispose();

    disposables.forEach(Disposable::dispose);
    disposables.clear();
  }

  public SpriteBatch getBatch() {
    return batch;
  }

  public BitmapFont getFont16() {
    return font16;
  }

  public void setExit() {
    exit = true;
  }

  public void registerDisposable(Disposable disposable) {
    this.disposables.add(disposable);
  }
}
