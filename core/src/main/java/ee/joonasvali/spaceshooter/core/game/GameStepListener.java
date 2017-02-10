package ee.joonasvali.spaceshooter.core.game;

/**
 * @author Joonas Vali January 2017
 */
public interface GameStepListener {
  void onStepAction(GameSpeedController.Control control);

  /**
   * If you want to be sure all listeners have acted this step, then here's possible to add additional action, after others.
   */
  default void onStepEffect() { }
}
