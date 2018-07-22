package ee.joonasvali.spaceblaster.core.game.difficulty;

/**
 * @author Joonas Vali March 2017
 */
public class NormalGameSettings implements GameSettings {
  @Override
  public int getEnemyFireFrequency() {
    return 35;
  }

  @Override
  public float getPowerUpSpeed() {
    return 0.3f;
  }


}
