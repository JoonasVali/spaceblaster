package ee.joonasvali.spaceblaster.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
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
  private Pixmap cursorImage;

  private String episodeName;

  private boolean exit;

  private FontFactory fontFactory;

  private Config config;

  @Override
  public void create() {
    log.info("Starting game");
    this.config = new ConfigReader(Gdx.files.internal("spaceblaster.yaml")).getConfig();

    this.soundManager = new SoundManager();

    batch = new SpriteBatch();
    fontFactory = new FontFactory();

    Pixmap cursorRaw = new Pixmap(Gdx.files.internal("cursor.png"));
    this.cursorImage = new Pixmap(cursorRaw.getWidth() * 2, cursorRaw.getHeight() * 2, cursorRaw.getFormat());
    cursorImage.setFilter(Pixmap.Filter.NearestNeighbour);
    cursorImage.drawPixmap(cursorRaw, 0, 0, cursorRaw.getWidth(), cursorRaw.getHeight(), 0, 0, cursorImage.getWidth(), cursorImage.getHeight());
    cursorRaw.dispose();

    Cursor cursor = Gdx.graphics.newCursor(cursorImage, 0, 0);

    Gdx.graphics.setCursor(cursor);

    gotoMainMenu();
  }

  public SoundManager getSoundManager() {
    return soundManager;
  }

  public void gotoMainMenu() {
    setScreen(new MainMenuScreen(this, new MainMenuContent(this)));
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
    cursorImage.dispose();
    soundManager.dispose();
    batch.dispose();
  }

  public SpriteBatch getBatch() {
    return batch;
  }

  public Config getConfig() {
    return config;
  }

  public void setExit() {
    exit = true;
  }

  public void launchGame(FileHandle episodeFile, DifficultyLevel difficultyLevel, String episodeName) {
    this.episodeName = episodeName;
    GameScreen screen = new GameScreen(this, episodeFile, difficultyLevel.getGameSettings());
    if (screen.isValid()) {
      setScreen(screen);
    } else {
      screen.dispose();
      gotoMainMenu();
    }
  }

  public void setChooseLevelsScreen() {
    setScreen(new MainMenuScreen(this, new ChooseLevelsContent(this)));
  }

  public void setChooseDifficultyScreen(FileHandle episodeFile, String episodeName) {
    setScreen(new MainMenuScreen(this, new ChooseDifficultyContent(this, episodeFile, episodeName)));
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
    setScreen(new CreditsScreen(this));
  }

  public String getEpisodeName() {
    return episodeName;
  }
}
