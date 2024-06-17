package com.github.joonasvali.spaceblaster.core.game.weapons;

/**
 * @author Joonas Vali January 2017
 */
public class GaussGunBullet extends WeaponProjectile {
  private final GaussGunBulletProvider provider;
  public GaussGunBullet(GaussGunBulletProvider gaussGunBulletProvider) {
    this.provider = gaussGunBulletProvider;
  }


  @Override
  public ProjectileProvider<? extends WeaponProjectile> getProjectileProvider() {
    return provider;
  }
}
