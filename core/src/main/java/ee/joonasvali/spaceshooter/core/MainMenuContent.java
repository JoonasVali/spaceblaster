package ee.joonasvali.spaceshooter.core;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ee.joonasvali.spaceshooter.core.game.InputHandler;

/**
 * @author Joonas Vali February 2017
 */
public class MainMenuContent implements MenuContent {
  private static final int WIDTH = 200;
  private static final int HEIGHT = 50;
  private static final int PADDING = 10;

  private SpaceShooterGame game;

  public MainMenuContent(SpaceShooterGame game) {
    this.game = game;
  }

  @Override
  public void fill(Table table, Skin skin, InputHandler inputHandler) {
    TextButton startButton = new TextButton("Start game", skin);
    startButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        playClick();
        proceedToGameScreen();
      }
    });
    TextButton creditsButton = new TextButton("Credits", skin);
    creditsButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        playClick();
        game.gotoCredits();
      }
    });
    TextButton exitButton = new TextButton("Exit", skin);
    exitButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        playClick();
        game.setExit();
      }
    });

    table.add(startButton).width(WIDTH).height(HEIGHT).pad(PADDING);
    table.row();
    table.add(creditsButton).width(WIDTH).height(HEIGHT).pad(PADDING);
    table.row();
    table.add(exitButton).width(WIDTH).height(HEIGHT).pad(PADDING);

    inputHandler.addKeyBinding(Input.Keys.ESCAPE, game::setExit);
    inputHandler.addKeyBinding(Input.Keys.SPACE, this::proceedToGameScreen);
    inputHandler.addKeyBinding(Input.Keys.ENTER, this::proceedToGameScreen);
  }

  private void playClick() {
    game.getSoundManager().getMenuClickSound().play(0.5f);
  }

  private void proceedToGameScreen() {
    game.setChooseLevelsScreen();
  }
}
