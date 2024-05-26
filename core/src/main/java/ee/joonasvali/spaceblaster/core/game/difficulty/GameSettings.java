package ee.joonasvali.spaceblaster.core.game.difficulty;

import ee.joonasvali.spaceblaster.event.GameDifficulty;

/**
 * @author Joonas Vali March 2017
 */
public interface GameSettings {

  GameDifficulty getDifficulty();

  int getEnemyFireFrequency();

  float getPowerUpSpeed();
}
