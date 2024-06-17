package com.github.joonasvali.spaceblaster.core.game.weapons;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author Joonas Vali January 2017
 */
public interface ProjectileProvider<T extends WeaponProjectile> {
  T obtain();
  void free(WeaponProjectile projectile);
  void draw(SpriteBatch batch, WeaponProjectile projectile);
  Sound getSound();
  int getCoolDown();
}
