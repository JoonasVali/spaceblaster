package com.github.joonasvali.spaceblaster.core.game.difficulty;

import com.github.joonasvali.spaceblaster.event.GameDifficulty;

/**
 * @author Joonas Vali March 2017
 */
public class EasyGameSettings implements GameSettings {
  @Override
  public GameDifficulty getDifficulty() {
    return GameDifficulty.EASY;
  }

  @Override
  public int getEnemyFireFrequency() {
    return 45;
  }

  @Override
  public float getPowerUpSpeed() {
    return 0.2f;
  }
}
