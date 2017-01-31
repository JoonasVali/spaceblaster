package ee.joonasvali.spaceshooter.core.game.player;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ee.joonasvali.spaceshooter.core.game.GameStepListener;

/**
 * @author Joonas Vali January 2017
 */
public interface Effect extends GameStepListener {
  boolean isActive();
  void draw(Sprite sprite, SpriteBatch batch);
}
