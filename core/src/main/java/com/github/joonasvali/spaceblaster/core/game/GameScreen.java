package com.github.joonasvali.spaceblaster.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.joonasvali.spaceblaster.core.event.ActiveEventLog;
import com.github.joonasvali.spaceblaster.core.event.InactiveEventLog;
import com.github.joonasvali.spaceblaster.core.game.difficulty.GameSettings;
import com.github.joonasvali.spaceblaster.core.game.level.LevelProvider;
import com.github.joonasvali.spaceblaster.core.game.level.LevelReader;
import com.github.joonasvali.spaceblaster.core.game.player.Rocket;
import com.github.joonasvali.spaceblaster.core.game.weapons.WeaponProjectileManager;
import com.github.joonasvali.spaceblaster.core.ParticleEffectManager;
import com.github.joonasvali.spaceblaster.core.SpaceBlasterGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Joonas Vali January 2017
 */
public class GameScreen implements Screen, Disposable {
  private static final int ROCKET_DISTANCE_FROM_BOTTOM = 1;
  private static final int FPS = 60;
  private static final int INITIAL_LIVES = 3;
  public static final float MUSIC_VOLUME = 0.3f;

  private Logger log = LoggerFactory.getLogger(GameScreen.class);

  private InputHandler inputHandler = new InputHandler();
  private InputMultiplexer inputMultiplexer = new InputMultiplexer();
  private GameSpeedController speedController = new GameSpeedController(1000 / FPS);

  private OrthographicCamera cam;
  private Viewport viewport;
  private LevelProvider levelProvider;

  private static final int WORLD_WIDTH = 100;
  private static final int WORLD_HEIGHT = 60;

  private final boolean isValid;
  private SpaceBlasterGame game;
  private final GameState state;
  private final Music music;

  public GameScreen(SpaceBlasterGame game, FileHandle episodeFile, GameSettings gameSettings) {
    this.game = game;

    int worldWidth = WORLD_WIDTH;
    int worldHeight = WORLD_HEIGHT;

    this.state = new GameState(worldWidth, worldHeight);



    this.music = game.getSoundManager().createMusic();
    this.music.setVolume(MUSIC_VOLUME);
    this.music.setLooping(true);


    createCamera();
    this.state.setEventLog(game.getConfig().eventMode ? new ActiveEventLog(state, viewport, Paths.get(game.getConfig().eventLogFolder), game.getConfig().eventModeScreenshotsEnabled) : new InactiveEventLog());

    state.setGameSettings(gameSettings);

    state.setSoundManager(game.getSoundManager());
    state.setScore(new AtomicInteger());
    state.setLives(new AtomicInteger(INITIAL_LIVES));
    state.setPowerupManager(new PowerupManager(state));
    state.setParticleManager(new ParticleEffectManager());
    state.setUi(new UIOverlay(game.getFontFactory(), state.getEventLog(), state.getScore(), state.getLives()));

    state.setExplosionManager(new ExplosionManager());
    state.setWeaponProjectileManager(new WeaponProjectileManager(state));

    state.setRocket(new Rocket(state));

    inputHandler = new InputHandler();
    inputMultiplexer.addProcessor(inputHandler);

    state.setEnemies(new GameStateManager(state));
    LevelReader reader = new LevelReader(episodeFile);
    isValid = reader.isValid();
    this.levelProvider = new LevelProvider(reader, worldWidth, worldHeight, game.getEpisodeName());
    state.getGameStateManager().setLevelProvider(levelProvider);
    state.setBackground(new Background(levelProvider.getBackgroundFileName(), worldWidth, worldHeight));

    speedController.registerGameStepListener(state.getWeaponProjectileManager());
    speedController.registerGameStepListener(state.getGameStateManager());
    speedController.registerGameStepListener(state.getRocket());
    speedController.registerGameStepListener(state.getBackground());
    speedController.registerGameStepListener(state.getExplosionManager());
    speedController.registerGameStepListener(state.getUi());
    speedController.registerGameStepListener(state.getPowerupManager());

    Gdx.input.setInputProcessor(inputMultiplexer);

    inputHandler.addKeyBinding(Input.Keys.ESCAPE, game::gotoMainMenu);
    inputHandler.addKeyBinding(Input.Keys.D,
        () -> state.getUi().setShowEventLog(game.getConfig().eventMode && !state.getUi().isShowEventLog())
    );
    inputHandler.addKeyBinding(
        Input.Keys.SPACE, () -> state.getRocket().doFire()
    );

    inputHandler.addMouseBinding(
        Input.Buttons.LEFT, () -> state.getRocket().doFire()
    );

    state.getEventLog().eventStartGame(levelProvider.getEpisodeName(), gameSettings, levelProvider.getLevelsTotal());
  }

  public boolean isValid() {
    return isValid;
  }


  private void createCamera() {
    cam = new OrthographicCamera();
    viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, cam);
    viewport.apply();

    cam.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
  }


  @Override
  public void render(float delta) {
    try {
      cam.update();
      SpriteBatch batch = game.getBatch();
      batch.setProjectionMatrix(cam.combined);

      handleInput();
      speedController.passTime(delta);

      batch.begin();
      state.getBackground().draw(batch);

      state.getExplosionManager().draw(batch);
      state.getWeaponProjectileManager().drawMissiles(batch);
      state.getPowerupManager().draw(batch);
      state.getGameStateManager().drawEnemies(batch, delta);
      state.getRocket().draw(batch);
      state.getParticleManager().draw(batch, delta);

      state.getUi().draw(batch);


      batch.end();
    } catch (Throwable t) {
      log.error("Game crashed.", t);
      game.gotoMainMenu();
    }
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height);
    cam.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
    state.getEventLog().setMinMaxPlayerX(0, WORLD_WIDTH - Rocket.ROCKET_SIZE);
  }

  @Override
  public void show() {
    this.music.play();
  }

  @Override
  public void hide() {
    this.music.pause();
  }

  @Override
  public void pause() {
    this.music.pause();
  }

  @Override
  public void resume() {
    this.music.play();
  }

  @Override
  public void dispose() {
    log.info("GameScreen disposed.");
    music.stop();
    music.dispose();
    state.getRocket().dispose();
    state.getWeaponProjectileManager().dispose();
    state.getExplosionManager().dispose();
    state.getGameStateManager().dispose();
    state.getBackground().dispose();
    state.getUi().dispose();
    state.getParticleManager().dispose();
    state.getPowerupManager().dispose();
    levelProvider.dispose();
    state.getEventLog().dispose();
  }


  private void handleInput() {
    int mouseX = Gdx.input.getX();
    int mouseY = Gdx.input.getY();
    state.getRocket().setPosition(
        Math.max(0, Math.min(viewport.unproject(new Vector2(mouseX, mouseY)).x - (Rocket.ROCKET_SIZE / 2), WORLD_WIDTH - Rocket.ROCKET_SIZE)),
        ROCKET_DISTANCE_FROM_BOTTOM
    );
  }



}
