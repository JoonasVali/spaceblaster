package ee.joonasvali.spaceblaster.core;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import ee.joonasvali.spaceblaster.core.game.InputHandler;

/**
 * @author Joonas Vali February 2017
 */
public interface MenuContent {
  void fill(Table table, Skin skin, InputHandler inputHandler);
}
