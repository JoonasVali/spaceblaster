package com.github.joonasvali.spaceblaster.core;

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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.joonasvali.spaceblaster.core.game.InputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Joonas Vali January 2017
 */
public class CreditsScreen implements Screen {

  private static final String GAME_TITLE = "Space Blaster";
  private static final String DISCLAIMER = GAME_TITLE + " is freeware and created for educational purposes. Joonas Vali 2017-2021";

  private final Logger log = LoggerFactory.getLogger(CreditsScreen.class);
  private final SpaceBlasterGame game;

  private final BitmapFont smallfont;
  private final BitmapFont titleFont;

  private Skin skin;
  private Skin tableSkin;
  private Stage stage;
  private final OrthographicCamera camera;
  private Sprite background;

  private InputMultiplexer inputMultiplexer = new InputMultiplexer();
  private InputHandler inputHandler = new InputHandler();

  private final float viewportWidth = 1000;
  private final float viewportHeight = 600;

  private Viewport viewport;

  public CreditsScreen(SpaceBlasterGame spaceShooterGame) {
    this.game = spaceShooterGame;
    this.smallfont = spaceShooterGame.getFontFactory().createCreditsFontSmall();
    this.titleFont = spaceShooterGame.getFontFactory().createTitlefont();

    camera = new OrthographicCamera();
    stage = new Stage();

    viewport = new FitViewport(viewportWidth, viewportHeight, camera);
    viewport.apply();

    camera.position.set(viewportWidth / 2f, viewportHeight / 2f, 0);

    background = new Sprite(new Texture(Gdx.files.internal("mainmenuback.png")),0,131,1024,740);
    background.setOrigin(0,0);

    skin = new Skin();
    // Skin font disposed together with skin.
    skin.add("default", spaceShooterGame.getFontFactory().createMenufont(), BitmapFont.class);
    skin.addRegions(new TextureAtlas("skin/skin.atlas"));
    skin.load(Gdx.files.internal("skin/skin.json"));


    Table table = new Table();
    table.setFillParent(true);

    TextButton backButton = new TextButton("Back", skin);
    backButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        goToMainMenuScreen();
      }
    });

    tableSkin = new Skin();
    // Skin font disposed together with skin.
    tableSkin.add("default", spaceShooterGame.getFontFactory().createCreditsFont(), BitmapFont.class);
    tableSkin.addRegions(new TextureAtlas("skin/skin.atlas"));
    tableSkin.load(Gdx.files.internal("skin/skin.json"));

    table.setColor(Color.BLACK);
    table.setSkin(tableSkin);
    table.add("Joonas Vali - Programming, Graphics, Sounds").pad(20);
    table.row();
    table.add("LibGDX skin by Czyzby").pad(20);
    table.row();
    table.add("Fonts 'Ethnocentric' and 'Pricedown' - Ray Larabie").pad(20);
    table.row();
    table.row();
    table.add("Music \"Nasdaq\" by Bobby Yarsulik");
    table.row();
    table.add("https://www.youtube.com/c/BobbyYarsulik");
    table.row();
    table.row();

    table.add(backButton).width(200).height(50).pad(50);

    stage.addActor(table);


    inputMultiplexer.addProcessor(inputHandler);
    inputMultiplexer.addProcessor(stage);
    Gdx.input.setInputProcessor(inputMultiplexer);

    inputHandler.addKeyBinding(Input.Keys.ESCAPE, this::goToMainMenuScreen);

  }

  private void playClick() {
    game.getSoundManager().getMenuClickSound().play(0.5f);
  }

  @Override
  public void render(float delta) {
    camera.update();

    Gdx.gl.glClearColor(0f, 0f, 0f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    game.getBatch().setProjectionMatrix(camera.combined);
    game.getBatch().begin();

    background.draw(game.getBatch());

    float fwidth = Util.getTextWidth(GAME_TITLE, titleFont);
    titleFont.draw(game.getBatch(), GAME_TITLE, viewportWidth / 2  - fwidth / 2, viewportHeight / 1.4f);

    fwidth = Util.getTextWidth(DISCLAIMER, smallfont);
    smallfont.draw(game.getBatch(), DISCLAIMER, viewportWidth / 2 - fwidth / 2, 20);
    game.getBatch().end();

    stage.act(Gdx.graphics.getDeltaTime());
    stage.draw();
  }

  private void goToMainMenuScreen() {
    playClick();
    game.gotoMainMenu();
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
    log.info("CreditScreen disposed.");
    background.getTexture().dispose();
    skin.dispose();
    tableSkin.dispose();
    smallfont.dispose();
    titleFont.dispose();
    stage.dispose();
  }
}
