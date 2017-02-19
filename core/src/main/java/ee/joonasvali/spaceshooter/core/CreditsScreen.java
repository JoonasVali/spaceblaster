package ee.joonasvali.spaceshooter.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ee.joonasvali.spaceshooter.core.game.InputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Joonas Vali January 2017
 */
public class CreditsScreen implements Screen {

  private static final String GAME_TITLE = "Space Blaster";
  private final Logger log = LoggerFactory.getLogger(CreditsScreen.class);
  public final FileHandle MAIN_FONT = Gdx.files.internal("fonts/coolvetica.ttf");
  private final SpaceShooterGame game;
  private final BitmapFont titlefont;
  private final BitmapFont normalfont;

  private Skin skin;
  private Stage stage;
  private final OrthographicCamera camera;
  private OrthographicCamera textCamera;
  private Sprite background;
  private float width;
  private float height;
  private TextButton backButton;
  private InputMultiplexer inputMultiplexer = new InputMultiplexer();
  private InputHandler inputHandler = new InputHandler();
  private float deltaCount = 0;

  public CreditsScreen(SpaceShooterGame spaceShooterGame, float viewportWidth, float viewportHeight) {
    this.game = spaceShooterGame;

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

    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(MAIN_FONT);
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    parameter.size = 30;
    parameter.shadowOffsetX = 3;
    parameter.shadowOffsetY = 3;
    parameter.shadowColor = Color.BLACK;
    parameter.color = Color.YELLOW;
    titlefont = generator.generateFont(parameter);
    parameter.shadowOffsetX = 1;
    parameter.shadowOffsetY = 1;
    parameter.size = 18;
    normalfont = generator.generateFont(parameter);

    parameter.shadowOffsetX = 0;
    parameter.shadowOffsetY = 0;
    parameter.size = 25;
    parameter.color = Color.WHITE;
    // Disposed with skin
    BitmapFont menufont = generator.generateFont(parameter);
    generator.dispose();


    skin = new Skin();
    skin.add("default", menufont, BitmapFont.class);
    skin.addRegions(new TextureAtlas("skin/skin.atlas"));
    skin.load(Gdx.files.internal("skin/skin.json"));



    Table table = new Table();
    table.setFillParent(true);

    backButton = new TextButton("Back", skin);
    backButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        goToMainMenuScreen();
      }
    });
    table.add(backButton).width(200).height(50).pad(10);

    stage.addActor(table);


    inputMultiplexer.addProcessor(inputHandler);
    inputMultiplexer.addProcessor(stage);
    Gdx.input.setInputProcessor(inputMultiplexer);

    inputHandler.addKeyBinding(Input.Keys.ESCAPE, this::goToMainMenuScreen);

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
    game.getFont16().setColor(Color.WHITE);

    float fwidth = Util.getTextWidth(GAME_TITLE, titlefont);
    titlefont.draw(game.getBatch(), GAME_TITLE, width / 2  - fwidth / 2, height / 1.4f);
    game.getBatch().end();

    stage.act(Gdx.graphics.getDeltaTime());
    stage.draw();
  }

  private void goToMainMenuScreen() {
    game.gotoMainMenu();
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
    log.info("CreditScreen disposed.");
    background.getTexture().dispose();
    titlefont.dispose();
    normalfont.dispose();
    skin.dispose();
    stage.dispose();
  }
}
