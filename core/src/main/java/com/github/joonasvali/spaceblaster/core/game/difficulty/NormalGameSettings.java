package com.github.joonasvali.spaceblaster.core.game.difficulty;

import com.github.joonasvali.spaceblaster.event.GameDifficulty;

/**
 * @author Joonas Vali March 2017
 */
public class NormalGameSettings implements GameSettings {
  @Override
  public GameDifficulty getDifficulty() {
    return GameDifficulty.MEDIUM;
  }

  @Override
  public int getEnemyFireFrequency() {
    return 35;
  }

  @Override
  public float getPowerUpSpeed() {
    return 0.3f;
  }


}
