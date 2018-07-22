package ee.joonasvali.spaceblaster.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

/**
 * @author Joonas Vali January 2017
 */
public class Background implements Disposable, GameStepListener {
  private static final float BACKGROUND_ROTATION_SPEED_MODIFIER = 30f;
  public static String DEFAULT_FILE_NAME = "space.png";
  private final Sprite mapSprite;
  private int gameStepsCounted = 0;
  private float heightMid;
  private float widthMid;

  public Background(String backgroundFileName, float worldWidth, float worldHeight) {
    mapSprite = new Sprite(new Texture(Gdx.files.local("backgrounds/" + backgroundFileName)));
    heightMid = worldHeight / 2;
    widthMid = worldWidth / 2;
    // Background doesn't need to scale to screen height, so we set its size rectangular
    mapSprite.setSize(worldWidth * 1.2f, worldWidth * 1.2f);
    mapSprite.setPosition(worldWidth / 2 - (mapSprite.getWidth() / 2),worldHeight / 2 - (mapSprite.getHeight() / 2));
  }

  @Override
  public void dispose() {
    mapSprite.getTexture().dispose();
  }

  public void draw(SpriteBatch batch) {
    mapSprite.setOrigin(mapSprite.getWidth() / 2, mapSprite.getHeight() / 2);
    mapSprite.setRotation((gameStepsCounted / BACKGROUND_ROTATION_SPEED_MODIFIER) % 360);
    mapSprite.draw(batch);
  }

  @Override
  public void onStepAction(GameSpeedController.Control control) {
    gameStepsCounted++;
  }

  @Override
  public void onStepEffect() {

  }
}
