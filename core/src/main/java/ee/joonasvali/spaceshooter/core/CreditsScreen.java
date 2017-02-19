package ee.joonasvali.spaceshooter.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
import ee.joonasvali.spaceshooter.core.game.InputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Joonas Vali January 2017
 */
public class CreditsScreen implements Screen {

  private static final String GAME_TITLE = "Space Blaster";
  private static final String DISCLAIMER = GAME_TITLE + " is freeware and created for educational purposes. Joonas Vali 2017";

  private final Logger log = LoggerFactory.getLogger(CreditsScreen.class);
  private final SpaceShooterGame game;

  private final BitmapFont normalfont;
  private final BitmapFont titleFont;

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

  public CreditsScreen(SpaceShooterGame spaceShooterGame, float viewportWidth, float viewportHeight) {
    this.game = spaceShooterGame;
    this.normalfont = spaceShooterGame.getFontFactory().createNormalfont();
    this.titleFont = spaceShooterGame.getFontFactory().createTitlefont();

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
    // Skin font disposed together with skin.
    skin.add("default", spaceShooterGame.getFontFactory().createMenufont(), BitmapFont.class);
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

    table.setColor(Color.BLACK);
    table.setSkin(skin);
    table.add("Joonas Vali - Programming, Graphics, Sounds").pad(20);
    table.row();
    table.add("LibGDX skin by Czyzby").pad(20);
    table.row();
    table.add("Fonts 'BebasNeue' and 'Coolvetica' - Ryoichi Tsunekawa & Ray Larabie").pad(20);
    table.row();
    table.row();
    table.add("Music \"Nasdaq\" by Bobby Yarsulik");
    table.row();
    table.add("https://www.youtube.com/c/BobbyYarsulik");
    table.row();
    table.row();

    table.add(backButton).width(200).height(50).pad(10);

    stage.addActor(table);


    inputMultiplexer.addProcessor(inputHandler);
    inputMultiplexer.addProcessor(stage);
    Gdx.input.setInputProcessor(inputMultiplexer);

    inputHandler.addKeyBinding(Input.Keys.ESCAPE, this::goToMainMenuScreen);

  }

  @Override
  public void render(float delta) {
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

    fwidth = Util.getTextWidth(DISCLAIMER, normalfont);
    normalfont.draw(game.getBatch(), DISCLAIMER, width / 2 - fwidth / 2, 20);
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
    skin.dispose();
    normalfont.dispose();
    titleFont.dispose();
    stage.dispose();
  }
}
