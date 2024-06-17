package com.github.joonasvali.spaceblaster.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.joonasvali.spaceblaster.core.game.InputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Joonas Vali January 2017
 */
public class MainMenuScreen implements Screen {

  private static final String CREDIT_MESSAGE = "Joonas Vali 2017-2021";
  private static final String GAME_TITLE = "Space Blaster";

  private final Logger log = LoggerFactory.getLogger(MainMenuScreen.class);

  private final SpaceBlasterGame game;

  private final float viewportWidth = 1000;
  private final float viewportHeight = 600;

  private Skin skin;
  private Stage stage;
  private final OrthographicCamera camera;
  private Sprite background;

  private InputMultiplexer inputMultiplexer = new InputMultiplexer();
  private InputHandler inputHandler = new InputHandler();

  private final BitmapFont titleFont;
  private final BitmapFont creditFont;

  private Viewport viewport;

  private final String contentId;


  public MainMenuScreen(SpaceBlasterGame spaceShooterGame, MenuContent content) {
    this.game = spaceShooterGame;
    this.titleFont = spaceShooterGame.getFontFactory().createTitlefont();
    this.creditFont = spaceShooterGame.getFontFactory().createCreditsFontSmall();
    this.contentId = content.getClass().getCanonicalName();

    camera = new OrthographicCamera();
    stage = new Stage();

    viewport = new FitViewport(viewportWidth, viewportHeight, camera);
    viewport.apply();

    camera.position.set(viewportWidth / 2f, viewportHeight / 2f, 0);


    background = new Sprite(new Texture(Gdx.files.internal("mainmenuback.png")),0,131,1024,740);
    background.setOrigin(0,0);

    skin = new Skin();
    // font disposed with skin.
    skin.add("default", game.getFontFactory().createMenufont(), BitmapFont.class);
    skin.addRegions(new TextureAtlas("skin/skin.atlas"));
    skin.load(Gdx.files.internal("skin/skin.json"));

    inputMultiplexer.addProcessor(inputHandler);
    inputMultiplexer.addProcessor(stage);

    Table table = new Table();
    table.setFillParent(true);

    content.fill(table, skin, inputHandler);

    stage.addActor(table);

    Gdx.input.setInputProcessor(inputMultiplexer);
  }

  @Override
  public void render(float delta) {
    try {
      camera.update();

      Gdx.gl.glClearColor(0f, 0f, 0f, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      game.getBatch().setProjectionMatrix(camera.combined);


      game.getBatch().begin();

      background.draw(game.getBatch());


      float fwidth = Util.getTextWidth(GAME_TITLE, titleFont);
      titleFont.draw(game.getBatch(), GAME_TITLE, viewportWidth / 2 - fwidth / 2, viewportHeight / 1.4f);

      fwidth = Util.getTextWidth(CREDIT_MESSAGE, creditFont);
      creditFont.draw(game.getBatch(), CREDIT_MESSAGE, viewportWidth / 2 - fwidth / 2, 30);

      game.getBatch().end();

      stage.act(Gdx.graphics.getDeltaTime());
      stage.draw();
    } catch (Throwable t) {
      log.error("Game crashed", t);
      dispose();
      throw new RuntimeException(t);
    }
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height);
    camera.position.set(viewportWidth / 2f, viewportHeight / 2f, 0);
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
    log.info("MainMenuScreen disposed. (" + contentId + ")");
    background.getTexture().dispose();
    skin.remove("default", BitmapFont.class);
    creditFont.dispose();
    titleFont.dispose();
    skin.dispose();
    stage.dispose();
  }
}
