package ee.joonasvali.spaceshooter.core.game;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Joonas Vali January 2017
 */
public class GameSpeedController {
  private float lastTime;
  private final float msInStep;
  private final Set<GameStepListener> stepListeners = new HashSet<>();

  public GameSpeedController(int msInStep) {
    this.msInStep = msInStep / 1000f;
  }

  public boolean registerGameStepListener(GameStepListener listener) {
    return stepListeners.add(listener);
  }

  public boolean unRegisterGameStepListener(GameStepListener listener) {
    return stepListeners.remove(listener);
  }

  public void passTime(float delta) {
    lastTime += delta;
    while (lastTime > msInStep) {
      stepListeners.forEach(GameStepListener::onStepAction);
      stepListeners.forEach(GameStepListener::onStepEffect);
      lastTime -= msInStep;
    }
  }
}
