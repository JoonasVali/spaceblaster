package ee.joonasvali.spaceblaster.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ee.joonasvali.spaceblaster.core.FontFactory;
import ee.joonasvali.spaceblaster.core.TimedText;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Joonas Vali January 2017
 */
public class UIOverlay implements Disposable, GameStepListener {
  private static final int TOTAL_WIDTH_UNITS = 1000;
  private static final int TOTAL_HEIGHT_UNITS = 600;

  private static final int LIFE_WIDTH = 20;
  private static final int LIFE_HEIGHT = 20;
  private static final float LIVES_POSITION_X = 0.95f;
  private static final float LIVES_POSITION_Y = 0.95f;
  private static final int SPACE_BETWEEN_LIVES = 2;

  private static final int SCORE_POS_X = 20;
  private static final int SCORE_POS_Y_FROM_TOP = 10;
  public static final String VICTORY_TEXT = "VICTORY";
  public static final String GAME_OVER_TEXT = "GAME OVER";

  private OrthographicCamera cam;
  private BitmapFont font;

  private final AtomicInteger score;
  private final AtomicInteger lives;

  private final Texture texture;
  private final Sprite sprite;

  private Viewport viewport;

  private TimedText textToDisplay;

  public UIOverlay(FontFactory fontFactory, AtomicInteger score, AtomicInteger lives) {
    this.lives = lives;
    this.score = score;
    this.font = fontFactory.createFont20(Color.YELLOW);
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    cam = new OrthographicCamera();
    viewport = new FitViewport(TOTAL_WIDTH_UNITS, TOTAL_HEIGHT_UNITS, cam);
    viewport.apply();

    cam.position.set(TOTAL_WIDTH_UNITS / 2f, TOTAL_HEIGHT_UNITS / 2f, 0);

    texture = new Texture(Gdx.files.internal("rocket.png"));
    sprite = new Sprite(texture);
    sprite.setSize(LIFE_WIDTH, LIFE_HEIGHT);
  }

  public void draw(SpriteBatch batch) {
    cam.update();
    batch.setProjectionMatrix(cam.combined);
    font.draw(batch, Integer.toString(score.get()), SCORE_POS_X, TOTAL_HEIGHT_UNITS - SCORE_POS_Y_FROM_TOP);
    for (int i = 0; i < lives.get() - 1; i++) {
      sprite.setX(TOTAL_WIDTH_UNITS * LIVES_POSITION_X - (LIFE_WIDTH + SPACE_BETWEEN_LIVES) * i);
      sprite.setY(TOTAL_HEIGHT_UNITS * LIVES_POSITION_Y);
      sprite.draw(batch);
    }

    if (textToDisplay != null) {
      textToDisplay.draw(batch);
    }
  }

  @Override
  public void dispose() {
    texture.dispose();
    font.dispose();
  }

  public void displayVictory() {
    textToDisplay = new TimedText(VICTORY_TEXT, font, TOTAL_WIDTH_UNITS, TOTAL_HEIGHT_UNITS, 100);
  }

  public void displayGameOver() {
    textToDisplay = new TimedText(GAME_OVER_TEXT, font, TOTAL_WIDTH_UNITS, TOTAL_HEIGHT_UNITS, 100);
  }

  @Override
  public void onStepAction(GameSpeedController.Control control) {
    if (textToDisplay != null) {
      textToDisplay.onStepAction(control);
    }
  }

  @Override
  public void onStepEffect() {
    if (textToDisplay != null) {
      textToDisplay.onStepEffect();
    }
  }

  public void displayText(String text, int timeToDisplay, int timeToFade) {
    textToDisplay = new TimedText(text, font, TOTAL_WIDTH_UNITS, TOTAL_HEIGHT_UNITS, timeToDisplay, timeToFade);
  }
}
