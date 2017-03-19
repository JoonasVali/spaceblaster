package ee.joonasvali.spaceshooter.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import ee.joonasvali.spaceshooter.core.ParticleEffectManager;
import ee.joonasvali.spaceshooter.core.SpaceShooterGame;
import ee.joonasvali.spaceshooter.core.Util;
import ee.joonasvali.spaceshooter.core.game.difficulty.GameSettings;
import ee.joonasvali.spaceshooter.core.game.level.LevelProvider;
import ee.joonasvali.spaceshooter.core.game.level.LevelReader;
import ee.joonasvali.spaceshooter.core.game.player.Rocket;
import ee.joonasvali.spaceshooter.core.game.weapons.WeaponProjectileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
  private LevelProvider levelProvider;

  private static final int WORLD_WIDTH = 100;


  private SpaceShooterGame game;
  private final GameState state;
  private final Music music;


  public GameScreen(SpaceShooterGame game, FileHandle level, GameSettings gameSettings) {
    this.game = game;

    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    int worldWidth = WORLD_WIDTH;
    int worldHeight = (int) (WORLD_WIDTH * (h / w)); // Make world height match monitor ratio

    this.state = new GameState(worldWidth, worldHeight);

    this.music = game.getSoundManager().createMusic();
    this.music.setVolume(MUSIC_VOLUME);
    this.music.setLooping(true);

    state.setGameSettings(gameSettings);

    state.setSoundManager(game.getSoundManager());
    state.setScore(new AtomicInteger());
    state.setLives(new AtomicInteger(INITIAL_LIVES));
    state.setPowerupManager(new PowerupManager(state));
    state.setParticleManager(new ParticleEffectManager());
    state.setUi(new UIOverlay(game.getFontFactory(), state.getScore(), state.getLives()));

    state.setExplosionManager(new ExplosionManager());
    state.setWeaponProjectileManager(new WeaponProjectileManager(state));

    state.setRocket(new Rocket(state));

    inputHandler = new InputHandler();
    inputMultiplexer.addProcessor(inputHandler);

    state.setEnemies(new GameStateManager(state));
    LevelReader reader = new LevelReader(level);
    this.levelProvider = new LevelProvider(reader, worldWidth, worldHeight);
    state.getEnemies().setLevelProvider(levelProvider);
    state.setBackground(new Background(levelProvider.getBackgroundFileName(), worldWidth, worldHeight));

    speedController.registerGameStepListener(state.getWeaponProjectileManager());
    speedController.registerGameStepListener(state.getEnemies());
    speedController.registerGameStepListener(state.getRocket());
    speedController.registerGameStepListener(state.getBackground());
    speedController.registerGameStepListener(state.getExplosionManager());
    speedController.registerGameStepListener(state.getUi());
    speedController.registerGameStepListener(state.getPowerupManager());

    Gdx.input.setInputProcessor(inputMultiplexer);

    inputHandler.addKeyBinding(Input.Keys.ESCAPE, game::gotoMainMenu);
    inputHandler.addKeyBinding(
        Input.Keys.SPACE, () -> state.getRocket().doFire()
    );

    inputHandler.addMouseBinding(
        Input.Buttons.LEFT, () -> state.getRocket().doFire()
    );

    createCamera();

  }


  private void createCamera() {
    // Constructs a new OrthographicCamera, using the given viewport width and height
    // Height is multiplied by aspect ratio.
    cam = new OrthographicCamera(state.getWorldWidth(), state.getWorldHeight());

    cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
    cam.update();
  }


  @Override
  public void render(float delta) {
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
    state.getEnemies().drawEnemies(batch, delta);
    state.getRocket().draw(batch);
    state.getParticleManager().draw(batch, delta);

    state.getUi().draw(batch);


    batch.end();

  }

  @Override
  public void resize(int width, int height) {
    cam.viewportWidth = 100f;
    cam.viewportHeight = 100f * height / width;
    cam.update();
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
    state.getEnemies().dispose();
    state.getBackground().dispose();
    state.getUi().dispose();
    state.getParticleManager().dispose();
    state.getPowerupManager().dispose();
    levelProvider.dispose();
  }


  private void handleInput() {
    int mouseX = Gdx.input.getX();
    int mouseY = Gdx.input.getY();
    state.getRocket().setPosition(Math.min(Util.unProjectX(cam, mouseX, mouseY), WORLD_WIDTH - Rocket.ROCKET_SIZE), ROCKET_DISTANCE_FROM_BOTTOM);
  }



}
