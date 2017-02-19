package ee.joonasvali.spaceshooter.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ee.joonasvali.spaceshooter.core.game.GameScreen;
import ee.joonasvali.spaceshooter.core.game.InputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Joonas Vali January 2017
 */
public class MainMenuScreen implements Screen {

  private static final String START_MESSAGE = "Press space to start";
  private static final String GAME_TITLE = "Space Blaster";

  private final Logger log = LoggerFactory.getLogger(MainMenuScreen.class);

  private final SpaceShooterGame game;


  private Skin skin;
  private Stage stage;
  private final OrthographicCamera camera;
  private OrthographicCamera textCamera;
  private Sprite background;
  private float width;
  private float height;
  private TextButton startButton;
  private TextButton creditsButton;
  private TextButton exitButton;
  private InputMultiplexer inputMultiplexer = new InputMultiplexer();
  private InputHandler inputHandler = new InputHandler();
  private float deltaCount = 0;
  private final BitmapFont titleFont;
  private final BitmapFont normalFont;


  public MainMenuScreen(SpaceShooterGame spaceShooterGame, float viewportWidth, float viewportHeight) {
    this.game = spaceShooterGame;
    this.titleFont = spaceShooterGame.getFontFactory().createTitlefont();
    this.normalFont = spaceShooterGame.getFontFactory().createNormalfont();

    camera = new OrthographicCamera();
    textCamera = new OrthographicCamera();
    stage = new Stage();

    camera.setToOrtho(false, viewportWidth, viewportHeight);
    width = 1000;
    height = 1000 * (viewportHeight / viewportWidth);
    textCamera.setToOrtho(false, width, height);
    background = new Sprite(new Texture(Gdx.files.internal("mainmenuback.png")),0,131,1024,740);
    background.setOrigin(0,0);
    background.setScale(width / background.getWidth());

    skin = new Skin();
    // font disposed with skin.
    skin.add("default", game.getFontFactory().createMenufont(), BitmapFont.class);
    skin.addRegions(new TextureAtlas("skin/skin.atlas"));
    skin.load(Gdx.files.internal("skin/skin.json"));



    Table table = new Table();
    table.setFillParent(true);

    startButton = new TextButton("Start game", skin);
    startButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        goToGameScreen();
      }
    });
    creditsButton = new TextButton("Credits", skin);
    creditsButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        game.gotoCredits();
      }
    });
    exitButton = new TextButton("Exit", skin);
    exitButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        game.setExit();
      }
    });

    table.add(startButton).width(200).height(50).pad(10);
    table.row();
    table.add(creditsButton).width(200).height(50).pad(10);
    table.row();
    table.add(exitButton).width(200).height(50).pad(10);

    stage.addActor(table);


    inputMultiplexer.addProcessor(inputHandler);
    inputMultiplexer.addProcessor(stage);
    Gdx.input.setInputProcessor(inputMultiplexer);

    inputHandler.addKeyBinding(Input.Keys.ESCAPE, game::setExit);
    inputHandler.addKeyBinding(Input.Keys.SPACE, this::goToGameScreen);
    inputHandler.addKeyBinding(Input.Keys.ENTER, this::goToGameScreen);
  }

  @Override
  public void render(float delta) {
    deltaCount += delta;
    Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    camera.update();
    game.getBatch().setProjectionMatrix(camera.combined);
    // render behind text here

    textCamera.update();
    game.getBatch().setProjectionMatrix(textCamera.combined);

    game.getBatch().begin();

    background.draw(game.getBatch());


    float fwidth = Util.getTextWidth(GAME_TITLE, titleFont);
    titleFont.draw(game.getBatch(), GAME_TITLE, width / 2  - fwidth / 2, height / 1.4f);

    if (((int)deltaCount) % 2 == 0) {
      fwidth = Util.getTextWidth(START_MESSAGE, normalFont);
      normalFont.draw(game.getBatch(), START_MESSAGE, width / 2 - fwidth / 2, 60);
    }

    game.getBatch().end();

    stage.act(Gdx.graphics.getDeltaTime());
    stage.draw();
  }

  private void goToGameScreen() {
    game.setScreen(new GameScreen(game));
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
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
    log.info("MainMenuScreen disposed.");
    background.getTexture().dispose();
    skin.remove("default", BitmapFont.class);
    normalFont.dispose();
    titleFont.dispose();
    skin.dispose();
    stage.dispose();
  }
}
