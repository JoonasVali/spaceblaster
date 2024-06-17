package com.github.joonasvali.spaceblaster.core.game.enemy;

/**
 * @author Joonas Vali January 2017
 */
@FunctionalInterface
public interface EnemyProvider {
  /**
   *
   * @param x matrix position
   * @param y matrix position
   */
  Enemy get(int x, int y);
}
