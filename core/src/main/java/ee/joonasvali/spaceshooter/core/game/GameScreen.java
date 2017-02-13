package ee.joonasvali.spaceshooter.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import ee.joonasvali.spaceshooter.core.MainMenuScreen;
import ee.joonasvali.spaceshooter.core.SpaceShooterGame;
import ee.joonasvali.spaceshooter.core.Util;
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

  private Logger log = LoggerFactory.getLogger(GameScreen.class);

  private InputHandler inputHandler = new InputHandler();
  private InputMultiplexer inputMultiplexer = new InputMultiplexer();
  private GameSpeedController speedController = new GameSpeedController(1000 / FPS);

  private OrthographicCamera cam;
  private LevelProvider levelProvider;

  private Stage stage;

  private static final int WORLD_WIDTH = 100;
  private static final int WORLD_HEIGHT = 100;

  private SpaceShooterGame game;
  private final GameState state;

  public GameScreen(SpaceShooterGame game) {
    this.game = game;
    this.state = new GameState();
    state.setBackground(new Background(WORLD_WIDTH, WORLD_HEIGHT));
    state.setScore(new AtomicInteger());
    state.setLives(new AtomicInteger(INITIAL_LIVES));
    state.setUi(new UIOverlay(state.getScore(), state.getLives()));

    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    state.setExplosionManager(new ExplosionManager());
    state.setWeaponProjectileManager(new WeaponProjectileManager(WORLD_WIDTH, WORLD_HEIGHT * (h / w)));

    state.setRocket(new Rocket(state));
    stage = new Stage();

    inputHandler = new InputHandler();
    inputMultiplexer.addProcessor(inputHandler);
    inputMultiplexer.addProcessor(stage);

    state.setEnemies(new GameStateManager(WORLD_WIDTH, WORLD_HEIGHT * (h / w), state));
    this.levelProvider = new LevelProvider(WORLD_WIDTH, WORLD_HEIGHT * (h / w));
    state.getEnemies().setLevelProvider(levelProvider);

    speedController.registerGameStepListener(state.getWeaponProjectileManager());
    speedController.registerGameStepListener(state.getEnemies());
    speedController.registerGameStepListener(state.getRocket());
    speedController.registerGameStepListener(state.getBackground());
    speedController.registerGameStepListener(state.getExplosionManager());
    speedController.registerGameStepListener(state.getUi());

    Gdx.input.setInputProcessor(inputMultiplexer);

    inputHandler.addKeyBinding(Input.Keys.ESCAPE, game::gotoMainMenu);
    inputHandler.addKeyBinding(
        Input.Keys.SPACE, () -> state.getRocket().doFire()
    );

    createCamera();

  }


  private void createCamera() {
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    // Constructs a new OrthographicCamera, using the given viewport width and height
    // Height is multiplied by aspect ratio.
    cam = new OrthographicCamera(100, 100 * (h / w));

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
    state.getEnemies().drawEnemies(batch, delta);
    state.getRocket().draw(batch);

    state.getUi().draw(batch);


    batch.end();
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
    cam.viewportWidth = 100f;
    cam.viewportHeight = 100f * height / width;
    cam.update();
  }

  @Override
  public void show() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void dispose() {
    log.info("GameScreen disposed.");
    state.getRocket().dispose();
    state.getWeaponProjectileManager().dispose();
    state.getExplosionManager().dispose();
    state.getEnemies().dispose();
    stage.dispose();
    state.getBackground().dispose();
    state.getUi().dispose();
    levelProvider.dispose();
  }


  private void handleInput() {
    int mouseX = Gdx.input.getX();
    int mouseY = Gdx.input.getY();
    state.getRocket().setPosition(Math.min(Util.unProjectX(cam, mouseX, mouseY), WORLD_WIDTH - Rocket.ROCKET_SIZE), ROCKET_DISTANCE_FROM_BOTTOM);

  }



}
