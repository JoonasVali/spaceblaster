package com.github.joonasvali.spaceblaster.core.game.difficulty;

import com.github.joonasvali.spaceblaster.event.GameDifficulty;

/**
 * @author Joonas Vali March 2017
 */
public interface GameSettings {

  GameDifficulty getDifficulty();

  int getEnemyFireFrequency();

  float getPowerUpSpeed();
}
