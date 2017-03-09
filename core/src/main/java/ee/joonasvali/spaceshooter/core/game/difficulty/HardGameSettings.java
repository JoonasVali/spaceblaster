package ee.joonasvali.spaceshooter.core.game.difficulty;

/**
 * @author Joonas Vali March 2017
 */
public class HardGameSettings implements GameSettings {
  @Override
  public int getEnemyFireFrequency() {
    return 25;
  }
}
