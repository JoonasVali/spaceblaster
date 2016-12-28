package ee.joonasvali.libgdxdemo.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpaceShooterGame implements ApplicationListener {
  public static final int ROCKET_DISTANCE_FROM_BOTTOM = 1;
  public static final int MISSILE_START_SPEED = 1;
  private Logger log = LoggerFactory.getLogger(SpaceShooterGame.class);

  private InputHandler inputHandler = new InputHandler();
  private InputMultiplexer inputMultiplexer = new InputMultiplexer();

  private OrthographicCamera cam;
  private Sprite mapSprite;
  private Texture missile;

  private Pool<Missile> missilePool = new Pool<Missile>() {
    @Override
    protected Missile newObject() {
      log.info("creating new missile!!! " + System.currentTimeMillis());
      return new Missile();
    }
  };

  private List<Missile> activeMissiles = new ArrayList<>();

  static final int WORLD_WIDTH = 100;
  static final int WORLD_HEIGHT = 100;

  private SpriteBatch batch;
  private float elapsed;
  private Stage stage;

  private Rocket rocket;

  private boolean exit;

  @Override
  public void create() {
    log.info("Starting game!!!");
    rocket = new Rocket();
    batch = new SpriteBatch();
    stage = new Stage();

    inputHandler = new InputHandler();
    inputMultiplexer.addProcessor(inputHandler);
    inputMultiplexer.addProcessor(stage);
    Gdx.input.setInputProcessor(inputMultiplexer);

    inputHandler.addKeyBinding(Input.Keys.ESCAPE, () -> exit = true);
    inputHandler.addKeyBinding(Input.Keys.SPACE, () -> createMissileAt(rocket.getX(), rocket.getY()));

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


  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);

    cam.viewportWidth = 100f;
    cam.viewportHeight = 100f * height / width;
    cam.update();
  }

  @Override
  public void render() {
    if (exit) {
      Gdx.app.exit();
    }
    handleInput();
    moveAndRemoveMissiles();
    elapsed += Gdx.graphics.getDeltaTime();
    Gdx.gl.glClearColor(0, 0, 0, 0);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    cam.update();
    batch.setProjectionMatrix(cam.combined);

    batch.begin();
    mapSprite.setOrigin(mapSprite.getWidth()/2,mapSprite.getHeight()/2);
    mapSprite.setRotation(elapsed % 360);
    mapSprite.setScale(1.5f);
    mapSprite.draw(batch);

    rocket.draw(batch);

    activeMissiles.forEach(m -> batch.draw(missile, m.getX(), m.getY(), 1, 1));
    batch.end();
    stage.draw();
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

  private Missile createMissileAt(float x, float y) {
    Missile missile = missilePool.obtain();
    missile.setPosition(x, y);
    missile.setAngle(0);
    missile.setSpeed(MISSILE_START_SPEED);
    activeMissiles.add(missile);
    return missile;
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
    batch.dispose();
    rocket.dispose();
    stage.dispose();
    missile.dispose();
    mapSprite.getTexture().dispose();
  }
}
