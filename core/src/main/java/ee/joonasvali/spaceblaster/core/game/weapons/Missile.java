package ee.joonasvali.spaceblaster.core.game.weapons;

/**
 * @author Joonas Vali December 2016
 */
public class Missile extends WeaponProjectile {
  private final MissileProvider provider;
  public Missile(MissileProvider provider) {
    this.provider = provider;
  }

  @Override
  ProjectileProvider<Missile> getProjectileProvider() {
    return provider;
  }
}
