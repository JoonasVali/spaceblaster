package ee.joonasvali.spaceshooter.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import ee.joonasvali.spaceshooter.core.SpaceShooterGame;
import ee.joonasvali.spaceshooter.core.Util;
import ee.joonasvali.spaceshooter.core.game.enemy.EnemyManager;
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

  private UIOverlay ui;
  private Background background;

  private Rocket rocket;
  private EnemyManager enemies;


  private OrthographicCamera cam;

  private Stage stage;
  private WeaponProjectileManager weaponProjectileManager;
  private ExplosionManager explosionManager;
  private static final int WORLD_WIDTH = 100;
  private static final int WORLD_HEIGHT = 100;

  private final AtomicInteger score;
  private final AtomicInteger lives;

  private SpaceShooterGame game;

  public GameScreen(SpaceShooterGame game) {
    this.game = game;
    this.game.registerDisposable(this);
    this.background = new Background(WORLD_WIDTH, WORLD_HEIGHT);
    this.score = new AtomicInteger();
    this.lives = new AtomicInteger(INITIAL_LIVES);
    this.ui = new UIOverlay(score, lives);

    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    explosionManager = new ExplosionManager();
    weaponProjectileManager = new WeaponProjectileManager(WORLD_WIDTH, WORLD_HEIGHT * (h / w));

    rocket = new Rocket(weaponProjectileManager, explosionManager, lives);
    stage = new Stage();

    inputHandler = new InputHandler();
    inputMultiplexer.addProcessor(inputHandler);
    inputMultiplexer.addProcessor(stage);

    enemies = new EnemyManager(WORLD_WIDTH, WORLD_HEIGHT * (h / w), weaponProjectileManager, explosionManager, score);
    enemies.setFormation(); // TODO set something.

    speedController.registerGameStepListener(weaponProjectileManager);
    speedController.registerGameStepListener(enemies);
    speedController.registerGameStepListener(rocket);
    speedController.registerGameStepListener(background);
    speedController.registerGameStepListener(explosionManager);

    Gdx.input.setInputProcessor(inputMultiplexer);

    inputHandler.addKeyBinding(Input.Keys.ESCAPE, game::setExit);
    inputHandler.addKeyBinding(
        Input.Keys.SPACE, () -> rocket.fireMissile()
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
    background.draw(batch);

    explosionManager.draw(batch);
    weaponProjectileManager.drawMissiles(batch);
    enemies.drawEnemies(batch);
    rocket.draw(batch);

    ui.draw(batch);


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
    rocket.dispose();
    weaponProjectileManager.dispose();
    explosionManager.dispose();
    enemies.dispose();
    stage.dispose();
    background.dispose();
    ui.dispose();
  }


  private void handleInput() {
    int mouseX = Gdx.input.getX();
    int mouseY = Gdx.input.getY();
    rocket.setPosition(Math.min(Util.unProjectX(cam, mouseX, mouseY), WORLD_WIDTH - Rocket.ROCKET_SIZE), ROCKET_DISTANCE_FROM_BOTTOM);

  }
}
