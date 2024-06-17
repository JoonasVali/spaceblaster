package com.github.joonasvali.spaceblaster.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.joonasvali.spaceblaster.core.game.InputHandler;

/**
 * @author Joonas Vali February 2017
 */
public class ChooseLevelsContent implements MenuContent {
  private static final int WIDTH = 500;
  private static final int HEIGHT = 60;
  private static final int PADDING = 10;
  public static final String LEVELS_FOLDER = "levels";
  private SpaceBlasterGame game;

  public ChooseLevelsContent(SpaceBlasterGame game) {
    this.game = game;
  }

  @Override
  public void fill(Table table, Skin skin, InputHandler inputHandler) {
    FileHandle[] files = Gdx.files.local(LEVELS_FOLDER).list("level");

    for (final FileHandle levelFile : files) {
      String name = levelFile.nameWithoutExtension();
      name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
      TextButton button = new TextButton(name, skin);
      String episodeName = name;
      button.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
          playClick();
          game.setChooseDifficultyScreen(levelFile, episodeName);
        }
      });
      table.add(button).width(WIDTH).height(HEIGHT).pad(PADDING);
      table.row();
    }


    TextButton back = new TextButton("Back to menu", skin);
    back.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        playClick();
        game.gotoMainMenu();
      }
    });
    table.row();
    table.add(back).width(WIDTH).height(HEIGHT).pad(PADDING);
    table.row();

    inputHandler.addKeyBinding(Input.Keys.ESCAPE, () -> {
      playClick();
      game.gotoMainMenu();
    });
  }

  private void playClick() {
    game.getSoundManager().getMenuClickSound().play(0.5f);
  }
}
