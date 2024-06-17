package com.github.joonasvali.spaceblaster.core.game.player;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.joonasvali.spaceblaster.core.game.GameSpeedController;

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

  @Override
  public void onStepAction(GameSpeedController.Control control) {
    step++;
  }

  @Override
  public void onStepEffect() {

  }
}
