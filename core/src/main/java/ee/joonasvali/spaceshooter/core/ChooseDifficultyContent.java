package ee.joonasvali.spaceshooter.core;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ee.joonasvali.spaceshooter.core.game.InputHandler;
import ee.joonasvali.spaceshooter.core.game.difficulty.DifficultyLevel;

/**
 * @author Joonas Vali March 2017
 */
public class ChooseDifficultyContent implements MenuContent {
  private static final int WIDTH = 200;
  private static final int HEIGHT = 50;
  private static final int PADDING = 10;
  private final SpaceShooterGame game;
  private final FileHandle levelFile;

  public ChooseDifficultyContent(SpaceShooterGame game, FileHandle levelFile) {
    this.game = game;
    this.levelFile = levelFile;
  }

  @Override
  public void fill(Table table, Skin skin, InputHandler inputHandler) {

    for (DifficultyLevel level : DifficultyLevel.values()) {
      TextButton easy = new TextButton(getLabelOfDifficultyLevel(level), skin);
      easy.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
          playClick();
          game.launchGame(levelFile, level);
        }
      });
      table.add(easy).width(WIDTH).height(HEIGHT).pad(PADDING);
      table.row();
    }

    TextButton back = new TextButton("Back", skin);
    back.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        playClick();
        game.setChooseLevelsScreen();
      }
    });
    table.row();
    table.add(back).width(WIDTH).height(HEIGHT).pad(PADDING);
    table.row();

    inputHandler.addKeyBinding(Input.Keys.ESCAPE, () -> {
      playClick();
      game.setChooseLevelsScreen();
    });
  }

  private String getLabelOfDifficultyLevel(DifficultyLevel level) {
    String lev = level.name().toLowerCase();
    return Character.toUpperCase(lev.charAt(0)) + lev.substring(1);
  }

  private void playClick() {
    game.getSoundManager().getMenuClickSound().play(0.5f);
  }
}