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
import ee.joonasvali.spaceblaster.core.event.EventLog;
import ee.joonasvali.spaceblaster.event.Event;

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
  private BitmapFont tinyFont;

  private final AtomicInteger score;
  private final AtomicInteger lives;

  private final Texture texture;
  private final Sprite sprite;

  private boolean showEventLog = true;

  private Viewport viewport;

  private TimedText textToDisplay;
  private final EventLog eventLog;
  private String eventLogText;
  private long lastEventLogRefresh;

  public UIOverlay(FontFactory fontFactory, EventLog eventLog, AtomicInteger score, AtomicInteger lives) {
    this.lives = lives;
    this.score = score;
    this.eventLog = eventLog;
    this.font = fontFactory.createFont20(Color.YELLOW);
    this.tinyFont = fontFactory.createFont10(Color.WHITE);
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

    if (showEventLog) {
      refreshEventLogIfNeeded();
      tinyFont.draw(batch, eventLogText, 10, TOTAL_HEIGHT_UNITS - 10);
    }

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

  private void refreshEventLogIfNeeded() {
    if (eventLogText == null || lastEventLogRefresh + 300 < System.currentTimeMillis()) {
      this.lastEventLogRefresh = System.currentTimeMillis();
      Event stats = eventLog.getStatistics();
      StringBuilder strb = new StringBuilder();
      strb.append("Level: ").append(stats.levelName).append("\n");
      strb.append("Episode: ").append(stats.episodeName).append("\n");
      strb.append("Difficulty: ").append(stats.gameDifficulty).append("\n");
      strb.append("Lives: ").append(stats.playerLivesLeft).append("\n");
      strb.append("LivesOrig: ").append(stats.playerLivesOriginal).append("\n");
      strb.append("PlayerDead: ").append(stats.playerDead).append("\n");
      strb.append("PlayerMoving: ").append(stats.playerIsMoving).append("\n");
      strb.append("EnemyCloseness: ").append(stats.enemyCloseness).append("\n");
      strb.append("EnemySpeed: ").append(stats.enemySpeed).append("\n");
      strb.append("PlayerWeapon: ").append(stats.playerWeapon).append("\n");
      strb.append("PlayerPositionX: ").append(stats.playerPositionX).append("\n");
      strb.append("EnemyPositionXOnScreen: ").append(stats.enemyPositionXOnScreen).append("\n");
      strb.append("Shots fired last 3s: ").append(stats.shotsFiredLastThreeSeconds).append("\n");
      strb.append("Total score: ").append(stats.playerScore).append("\n");
      strb.append("Total powerups collected: ").append(stats.powerUpsCollectedTotalCount).append("\n");
      strb.append("Total powerups missed: ").append(stats.powerUpsMissedCount).append("\n");
      strb.append("Total powerups collected this round: ").append(stats.powerUpsCollectedThisRoundCount).append("\n");
      strb.append("Powerups collected Gauss: ").append(stats.powerUpsGaussGunCollectedCount).append("\n");
      strb.append("Powerups collected Missile: ").append(stats.powerUpsMissileCollectedCount).append("\n");
      strb.append("Powerups collected Cannon: ").append(stats.powerUpsCannonCollectedCount).append("\n");
      strb.append("Powerups collected Triple: ").append(stats.powerUpsTripleShotCollectedCount).append("\n");
      strb.append("Time since last powerup collected: ").append(timestampToSeconds(stats.lastPowerupTimestamp)).append("\n");
      strb.append("Time since last powerup missed: ").append(timestampToSeconds(stats.lastPowerupMissedTimestamp)).append("\n");
      strb.append("Time since last death: ").append(timestampToSeconds(stats.lastDeathTimestamp)).append("\n");
      strb.append("Time since last kill: ").append(timestampToSeconds(stats.lastKillTimestamp)).append("\n");
      strb.append("Time since last hit: ").append(timestampToSeconds(stats.lastHitTimestamp)).append("\n");
      strb.append("Enemy touched player deaths: ").append(stats.enemyTouchedPlayerDeathsCount).append("\n");
      strb.append("Player under enemy:").append(stats.playerIsUnderEnemyFormation).append("\n");
      strb.append("Enemy bullet flying towards player:").append(stats.enemyBulletFlyingTowardsPlayer).append("\n");
      strb.append("Total rounds: ").append(stats.totalRoundsCount).append("\n");
      strb.append("Enemies killed by player: ").append(stats.enemiesKilledThisRoundCount).append("\n");
      strb.append("Enemies killed enemies: ").append(stats.enemiesKilledEnemiesThisRoundCount).append("\n");
      strb.append("Enemies hit enemies: ").append(stats.enemiesHitEnemiesThisRoundCount).append("\n");
      strb.append("Enemies left: ").append(stats.enemiesLeftThisRoundCount).append("\n");
      strb.append("Enemies left with Gauss: ").append(stats.enemiesLeftWithGaussGunCount).append("\n");
      strb.append("Enemies left with Missile: ").append(stats.enemiesLeftWithMissileCount).append("\n");
      strb.append("Enemies left with Cannon: ").append(stats.enemiesLeftWithCannonCount).append("\n");
      strb.append("Enemies left with Triple: ").append(stats.enemiesLeftWithTripleShotCount).append("\n");
      strb.append("Enemies started with Gauss: ").append(stats.enemiesStartedWithGaussGunCount).append("\n");
      strb.append("Enemies started with Missile: ").append(stats.enemiesStartedWithMissileCount).append("\n");
      strb.append("Enemies started with Cannon: ").append(stats.enemiesStartedWithCannonCount).append("\n");
      strb.append("Enemies started with Triple: ").append(stats.enemiesStartedWithTripleShotCount).append("\n");
      strb.append("Time since round start: ").append(timestampToSeconds(stats.roundStartTimestamp)).append("\n");
      strb.append("Rounds finished: ").append(stats.roundsFinishedCount).append("\n");

      eventLogText = strb.toString();
    }
  }

  public String timestampToSeconds(Long timestamp) {
    if (timestamp == null) {
      return "N/A";
    }
    return (System.currentTimeMillis() - timestamp) / 1000 + "s";
  }

  @Override
  public void dispose() {
    texture.dispose();
    font.dispose();
    tinyFont.dispose();
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
