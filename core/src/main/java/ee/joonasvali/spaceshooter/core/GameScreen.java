package ee.joonasvali.spaceshooter.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Joonas Vali January 2017
 */
public class GameScreen implements Screen, Disposable {
  private static final int ROCKET_DISTANCE_FROM_BOTTOM = 1;
  public static final int FPS = 60;
  public static final float BACKGROUND_ROTATION_SPEED_MODIFIER = 30f;

  private Logger log = LoggerFactory.getLogger(GameScreen.class);

  private InputHandler inputHandler = new InputHandler();
  private InputMultiplexer inputMultiplexer = new InputMultiplexer();
  private GameSpeedController speedController = new GameSpeedController(1000 / FPS);

  private Rocket rocket;
  private EnemyManager enemies;

  private OrthographicCamera cam;
  private Sprite mapSprite;

  private Stage stage;
  private MissileManager missileManager;
  private static final int WORLD_WIDTH = 100;
  private static final int WORLD_HEIGHT = 100;

  private int gameStepsCounted = 0;
  private SpaceShooterGame game;

  public GameScreen(SpaceShooterGame game) {
    this.game = game;
    this.game.registerDisposable(this);
    rocket = new Rocket();
    stage = new Stage();
    inputHandler = new InputHandler();
    inputMultiplexer.addProcessor(inputHandler);
    inputMultiplexer.addProcessor(stage);

    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    missileManager = new MissileManager(WORLD_WIDTH, WORLD_HEIGHT * (h / w));
    enemies = new EnemyManager(WORLD_WIDTH, WORLD_HEIGHT, missileManager);

    speedController.registerGameStepListener(missileManager);
    speedController.registerGameStepListener(enemies);
    speedController.registerGameStepListener(rocket);
    speedController.registerGameStepListener(() -> gameStepsCounted++);

    Gdx.input.setInputProcessor(inputMultiplexer);

    inputHandler.addKeyBinding(Input.Keys.ESCAPE, game::setExit);
    inputHandler.addKeyBinding(
        Input.Keys.SPACE, () ->
          this.missileManager.createMissileAt(rocket.getX() + Rocket.ROCKET_SIZE / 2, rocket.getY() + Rocket.ROCKET_SIZE / 2,
              (float)Math.random() * 10 - 5, rocket.getMissileAcceleration(), rocket.getMissileStartSpeed(), rocket.getMissileSize()
          )
    );

    mapSprite = new Sprite(new Texture(Gdx.files.internal("space.png")));
    mapSprite.setPosition(0, 0);
    mapSprite.setSize(WORLD_WIDTH, WORLD_HEIGHT);

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
    mapSprite.setOrigin(mapSprite.getWidth() / 2, mapSprite.getHeight() / 2);
    mapSprite.setRotation((gameStepsCounted / BACKGROUND_ROTATION_SPEED_MODIFIER) % 360);
    mapSprite.setScale(1.5f);
    mapSprite.draw(batch);

    missileManager.drawMissiles(batch);
    enemies.drawEnemies(batch);
    rocket.draw(batch);


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
    missileManager.dispose();
    enemies.dispose();
    stage.dispose();
    mapSprite.getTexture().dispose();
  }


  private void handleInput() {
    int mouseX = Gdx.input.getX();
    int mouseY = Gdx.input.getY();
    rocket.setPosition(Math.min(Util.unProjectX(cam, mouseX, mouseY), WORLD_WIDTH - Rocket.ROCKET_SIZE), ROCKET_DISTANCE_FROM_BOTTOM);

  }
}
