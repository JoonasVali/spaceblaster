package com.github.joonasvali.spaceblaster.core;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.joonasvali.spaceblaster.core.game.GameSpeedController;
import com.github.joonasvali.spaceblaster.core.game.GameStepListener;

/**
 * @author Joonas Vali February 2017
 * prints text in middle of screen
 */
public class TimedText implements GameStepListener {
  private int timeToDisplay;
  private int timeToFade;
  private float xpos = -1;
  private final boolean fades;
  private final String text;
  private final BitmapFont font;
  private final float screenWidth;
  private final float screenHeight;

  public TimedText(String text, BitmapFont font, float screenWidth, float screenHeight, int timeToDisplay) {
    this(text, font, screenWidth, screenHeight, timeToDisplay, 0, false);
  }

  public TimedText(String text, BitmapFont font, float screenWidth, float screenHeight, int timeToDisplay, int timeToFade) {
    this(text, font, screenWidth, screenHeight, timeToDisplay, timeToFade, true);
  }

  private TimedText(String text, BitmapFont font, float screenWidth, float screenHeight, int timeToDisplay, int timeToFade, boolean fades) {
    this.timeToDisplay = timeToDisplay;
    this.timeToFade = timeToFade;
    this.font = font;
    this.text = String.valueOf(text);
    this.fades = fades;
    this.screenHeight = screenHeight;
    this.screenWidth = screenWidth;
  }

  @Override
  public void onStepAction(GameSpeedController.Control control) {
    if (timeToDisplay > 0) {
      timeToDisplay--;
    } else {
      if (fades && timeToFade > 0) {
        timeToFade--;
      }
    }
  }

  public void draw(SpriteBatch batch) {
    if (timeToDisplay <= 0 && (timeToFade > 0 || !fades)) {
      if (xpos == -1) {
        xpos = 0.5f * screenWidth - Util.getTextWidth(text, font) / 2;
      }
      font.draw(batch, text, xpos, screenHeight * 0.5f);
    }
  }
}
