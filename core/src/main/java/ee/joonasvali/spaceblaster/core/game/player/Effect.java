package ee.joonasvali.spaceblaster.core.game.player;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ee.joonasvali.spaceblaster.core.game.GameStepListener;

/**
 * @author Joonas Vali January 2017
 */
public interface Effect extends GameStepListener {
  boolean isActive();
  void draw(Sprite sprite, SpriteBatch batch);
}
