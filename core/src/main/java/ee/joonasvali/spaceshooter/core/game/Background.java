package ee.joonasvali.spaceshooter.core.game;

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
  private final Sprite mapSprite;
  private int gameStepsCounted = 0;

  public Background(float renderSize) {
    mapSprite = new Sprite(new Texture(Gdx.files.internal("space.png")));
    mapSprite.setPosition(0, 0);
    // Background doesn't need to scale to screen height, so we set its size rectangular
    mapSprite.setSize(renderSize, renderSize);
  }

  @Override
  public void dispose() {
    mapSprite.getTexture().dispose();
  }

  public void draw(SpriteBatch batch) {
    mapSprite.setOrigin(mapSprite.getWidth() / 2, mapSprite.getHeight() / 2);
    mapSprite.setRotation((gameStepsCounted / BACKGROUND_ROTATION_SPEED_MODIFIER) % 360);
    mapSprite.setScale(1.5f);
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
