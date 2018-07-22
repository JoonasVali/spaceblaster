package ee.joonasvali.spaceblaster.core.game.difficulty;

/**
 * @author Joonas Vali March 2017
 */
public class EasyGameSettings implements GameSettings {
  @Override
  public int getEnemyFireFrequency() {
    return 45;
  }

  @Override
  public float getPowerUpSpeed() {
    return 0.2f;
  }
}
