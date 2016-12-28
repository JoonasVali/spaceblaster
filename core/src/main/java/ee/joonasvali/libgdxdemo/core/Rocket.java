package ee.joonasvali.libgdxdemo.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

/**
 * @author Joonas Vali December 2016
 */
public class Rocket implements Disposable {
  public static final int ROCKET_SIZE = 5;
  private final Sprite sprite;
  private final Texture texture;
  public Rocket() {
    texture = new Texture(Gdx.files.internal("rocket.png"));
    sprite = new Sprite(texture);
    sprite.setSize(ROCKET_SIZE, ROCKET_SIZE);
  }

  @Override
  public void dispose() {
    texture.dispose();
  }

  public void setPosition(float x, float y) {
    sprite.setPosition(x, y);
  }

  public void draw(SpriteBatch batch) {
    sprite.draw(batch);
  }

  public float getX() {
    return sprite.getX();
  }

  public float getY() {
    return sprite.getY();
  }
}
