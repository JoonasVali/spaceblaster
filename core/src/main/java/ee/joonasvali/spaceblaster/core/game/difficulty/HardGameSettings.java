package ee.joonasvali.spaceblaster.core.game.difficulty;

/**
 * @author Joonas Vali March 2017
 */
public class HardGameSettings implements GameSettings {
  @Override
  public int getEnemyFireFrequency() {
    return 25;
  }

  @Override
  public float getPowerUpSpeed() {
    return 0.4f;
  }
}
