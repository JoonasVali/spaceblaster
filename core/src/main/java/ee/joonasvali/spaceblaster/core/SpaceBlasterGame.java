package ee.joonasvali.spaceblaster.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ee.joonasvali.spaceblaster.core.game.GameScreen;
import ee.joonasvali.spaceblaster.core.game.difficulty.DifficultyLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpaceBlasterGame extends Game {

  private static final Logger log = LoggerFactory.getLogger(SpaceBlasterGame.class);

  private SoundManager soundManager;
  private SpriteBatch batch;
  private float elapsed;

  private boolean exit;

  private float viewportWidth;
  private float viewportHeight;

  private FontFactory fontFactory;

  @Override
  public void create() {
    log.info("Starting game");

    this.soundManager = new SoundManager();

    batch = new SpriteBatch();
    fontFactory = new FontFactory();

    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();
    this.viewportWidth = 100;
    this.viewportHeight = 100 * (h / w);

    gotoMainMenu();
  }

  public SoundManager getSoundManager() {
    return soundManager;
  }

  public void gotoMainMenu() {
    setScreen(new MainMenuScreen(this, new MainMenuContent(this), viewportWidth, viewportHeight));
  }

  @Override
  public void render() {
    if (exit) {
      disposeActiveScreen();
      Gdx.app.exit();
    }
    elapsed += Gdx.graphics.getDeltaTime();
    Gdx.gl.glClearColor(0, 0, 0, 0);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    super.render();

  }

  private void disposeActiveScreen() {
    Screen old = getScreen();
    if (old != null) {
      old.dispose();
    }
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
    log.info("SpaceBlasterGame disposed.");
    soundManager.dispose();
    batch.dispose();
  }

  public SpriteBatch getBatch() {
    return batch;
  }


  public void setExit() {
    exit = true;
  }

  public void launchGame(FileHandle level, DifficultyLevel difficultyLevel) {
    GameScreen screen = new GameScreen(this, level, difficultyLevel.getGameSettings());
    if (screen.isValid()) {
      setScreen(screen);
    } else {
      screen.dispose();
      gotoMainMenu();
    }
  }

  public void setChooseLevelsScreen() {
    setScreen(new MainMenuScreen(this, new ChooseLevelsContent(this), viewportWidth, viewportHeight));
  }

  public void setChooseDifficultyScreen(FileHandle level) {
    setScreen(new MainMenuScreen(this, new ChooseDifficultyContent(this, level), viewportWidth, viewportHeight));
  }

  @Override
  public void setScreen(Screen screen) {
    disposeActiveScreen();
    super.setScreen(screen);
  }

  public FontFactory getFontFactory() {
    return fontFactory;
  }

  public void gotoCredits() {
    setScreen(new CreditsScreen(this, viewportWidth, viewportHeight));
  }
}
