package ee.joonasvali.spaceblaster.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import ee.joonasvali.spaceblaster.core.FontFactory;
import ee.joonasvali.spaceblaster.core.TimedText;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Joonas Vali January 2017
 */
public class UIOverlay implements Disposable, GameStepListener {
  private static final int TOTAL_WIDTH_UNITS = 1000;
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

  private final float width;
  private final float height;

  private TimedText textToDisplay;

  public UIOverlay(FontFactory fontFactory, AtomicInteger score, AtomicInteger lives) {
    this.lives = lives;
    this.score = score;
    this.font = fontFactory.createFont20(Color.YELLOW);
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    // Constructs a new OrthographicCamera, using the given viewport width and height
    // Height is multiplied by aspect ratio.
    width = TOTAL_WIDTH_UNITS;
    height = width * (h / w);

    cam = new OrthographicCamera(width, height);

    cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
    cam.update();

    texture = new Texture(Gdx.files.internal("rocket.png"));
    sprite = new Sprite(texture);
    sprite.setSize(LIFE_WIDTH, LIFE_HEIGHT);
  }

  public void draw(SpriteBatch batch) {
    batch.setProjectionMatrix(cam.combined);
    font.draw(batch, Integer.toString(score.get()), SCORE_POS_X, height - SCORE_POS_Y_FROM_TOP);
    for (int i = 0; i < lives.get() - 1; i++) {
      sprite.setX(width * LIVES_POSITION_X - (LIFE_WIDTH + SPACE_BETWEEN_LIVES) * i);
      sprite.setY(height * LIVES_POSITION_Y);
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
    textToDisplay = new TimedText(VICTORY_TEXT, font, width, height, 100);
  }

  public void displayGameOver() {
    textToDisplay = new TimedText(GAME_OVER_TEXT, font, width, height, 100);
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
    textToDisplay = new TimedText(text, font, width, height, timeToDisplay, timeToFade);
  }
}
