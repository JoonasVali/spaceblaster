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
import com.badlogic.gdx.utils.Pool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Joonas Vali January 2017
 */
public class GameScreen implements Screen, Disposable {
  private static final int ROCKET_DISTANCE_FROM_BOTTOM = 1;
  private static final float MISSILE_START_SPEED = 0.01f;
  private static final int MISSILE_SIZE = 1;
  private static float MISSILE_ACCELERATION = 0.01f;

  private Logger log = LoggerFactory.getLogger(GameScreen.class);

  private InputHandler inputHandler = new InputHandler();
  private InputMultiplexer inputMultiplexer = new InputMultiplexer();

  private Rocket rocket;

  private OrthographicCamera cam;
  private Sprite mapSprite;
  private Texture missile;

  private Stage stage;


  private Pool<Missile> missilePool = new Pool<Missile>() {
    @Override
    protected Missile newObject() {
      return new Missile();
    }
  };

  private List<Missile> activeMissiles = new ArrayList<>();

  private static final int WORLD_WIDTH = 100;
  private static final int WORLD_HEIGHT = 100;

  private SpaceShooterGame game;

  public GameScreen(SpaceShooterGame game) {
    this.game = game;
    this.game.registerDisposable(this);
    rocket = new Rocket();
    stage = new Stage();
    inputHandler = new InputHandler();
    inputMultiplexer.addProcessor(inputHandler);
    inputMultiplexer.addProcessor(stage);
    Gdx.input.setInputProcessor(inputMultiplexer);

    inputHandler.addKeyBinding(Input.Keys.ESCAPE, game::setExit);
    inputHandler.addKeyBinding(Input.Keys.SPACE, () -> createMissileAt(rocket.getX() + Rocket.ROCKET_SIZE / 2, rocket.getY() + Rocket.ROCKET_SIZE / 2));

    mapSprite = new Sprite(new Texture(Gdx.files.internal("space.png")));
    mapSprite.setPosition(0, 0);
    mapSprite.setSize(WORLD_WIDTH, WORLD_HEIGHT);

    missile = new Texture(Gdx.files.internal("missile.png"));

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


  private Missile createMissileAt(float x, float y) {
    Missile missile = missilePool.obtain();
    missile.setPosition(x, y);
    missile.setAngle((float) Math.random() * 10 - 5);
    missile.setSpeed(MISSILE_START_SPEED);
    missile.setAcceleration(MISSILE_ACCELERATION);
    activeMissiles.add(missile);
    return missile;
  }

  @Override
  public void render(float delta) {
    cam.update();
    SpriteBatch batch = game.getBatch();
    batch.setProjectionMatrix(cam.combined);

    handleInput();
    moveAndRemoveMissiles();

    batch.begin();
    mapSprite.setOrigin(mapSprite.getWidth() / 2, mapSprite.getHeight() / 2);
    mapSprite.setRotation(game.getElapsed() % 360);
    mapSprite.setScale(1.5f);
    mapSprite.draw(batch);

    rocket.draw(batch);

    activeMissiles.forEach(m -> batch.draw(missile, m.getX(), m.getY(), MISSILE_SIZE, MISSILE_SIZE));
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
    missilePool.clear();
    activeMissiles.clear();
    rocket.dispose();
    missile.dispose();
    stage.dispose();
    mapSprite.getTexture().dispose();
  }

  private void moveAndRemoveMissiles() {
    activeMissiles.forEach(Missile::nextPosition);

    List<Missile> outOfBounds =
        activeMissiles.stream().filter(
            m -> m.getY() > WORLD_HEIGHT + 5 || m.getY() < -5 || m.getX() > WORLD_WIDTH + 5 || m.getX() < -5
        ).collect(Collectors.toList());

    outOfBounds.forEach(m -> {
      activeMissiles.remove(m);
      missilePool.free(m);
    });
  }

  private void handleInput() {
    int mouseX = Gdx.input.getX();
    int mouseY = Gdx.input.getY();
    rocket.setPosition(Math.min(Util.unProjectX(cam, mouseX, mouseY), WORLD_WIDTH - Rocket.ROCKET_SIZE), ROCKET_DISTANCE_FROM_BOTTOM);

  }
}
