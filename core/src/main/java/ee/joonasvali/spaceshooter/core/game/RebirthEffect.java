package ee.joonasvali.spaceshooter.core.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ee.joonasvali.spaceshooter.core.game.player.Effect;

/**
 * @author Joonas Vali January 2017
 */
public class RebirthEffect implements Effect {
  private int step;
  private final int totalSteps;
  private int blinkingFrequency;

  public RebirthEffect(int totalSteps, int blinkingFrequency) {
    this.totalSteps = totalSteps;
    this.blinkingFrequency = blinkingFrequency;
  }

  @Override
  public void onStep() {
    step++;
  }

  @Override
  public boolean isActive() {
    return totalSteps > step;
  }

  @Override
  public void draw(Sprite sprite, SpriteBatch batch) {
    // Blinking effect
    if ((step / blinkingFrequency) % 2 == 0) {
      sprite.draw(batch);
    }
  }
}
