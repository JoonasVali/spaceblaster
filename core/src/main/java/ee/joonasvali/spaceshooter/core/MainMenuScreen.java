package ee.joonasvali.spaceshooter.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * @author Joonas Vali January 2017
 */
public class MainMenuScreen implements Screen {

  final SpaceShooterGame game;

  final OrthographicCamera camera;
  private OrthographicCamera textCamera;

  public MainMenuScreen(SpaceShooterGame spaceShooterGame, float viewportWidth, float viewportHeight) {
    this.game = spaceShooterGame;

    camera = new OrthographicCamera();
    textCamera = new OrthographicCamera();
    camera.setToOrtho(false, viewportWidth, viewportHeight);
    textCamera.setToOrtho(false, 1000, 1000 * (viewportHeight / viewportWidth));
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    camera.update();
    game.getBatch().setProjectionMatrix(camera.combined);
    // render behind text here

    textCamera.update();
    game.getBatch().setProjectionMatrix(textCamera.combined);

    game.getBatch().begin();
    game.getFont16().setColor(Color.WHITE);

    game.getFont16().draw(game.getBatch(), "Welcome!!! ", 5, 15);
    game.getFont16().draw(game.getBatch(), "Tap anywhere to begin!", 5, 35);
    game.getBatch().end();

    if (Gdx.input.isTouched()) {
      game.setScreen(new GameScreen(game));
      dispose();
    }
  }

  @Override
  public void resize(int width, int height) {

  }

  @Override
  public void show() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void dispose() {

  }
}
