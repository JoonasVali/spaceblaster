package ee.joonasvali.spaceshooter.core;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import ee.joonasvali.spaceshooter.core.game.InputHandler;

/**
 * @author Joonas Vali February 2017
 */
public interface MenuContent {
  void fill(Table table, Skin skin, InputHandler inputHandler);
}
