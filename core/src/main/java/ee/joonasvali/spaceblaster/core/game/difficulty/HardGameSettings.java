package ee.joonasvali.spaceblaster.core.game.difficulty;

import ee.joonasvali.spaceblaster.event.GameDifficulty;

/**
 * @author Joonas Vali March 2017
 */
public class HardGameSettings implements GameSettings {
  @Override
  public GameDifficulty getDifficulty() {
    return GameDifficulty.HARD;
  }
  @Override
  public int getEnemyFireFrequency() {
    return 25;
  }

  @Override
  public float getPowerUpSpeed() {
    return 0.4f;
  }
}
