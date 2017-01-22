package ee.joonasvali.spaceshooter.core.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author Joonas Vali January 2017
 */
public interface Effect extends GameStepListener {
  boolean isActive();
  void draw(Sprite sprite, SpriteBatch batch);
}
