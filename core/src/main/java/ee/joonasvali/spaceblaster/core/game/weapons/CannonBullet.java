package ee.joonasvali.spaceblaster.core.game.weapons;

/**
 * @author Joonas Vali January 2017
 */
public class CannonBullet extends WeaponProjectile {
  private final CannonBulletProvider provider;
  public CannonBullet(CannonBulletProvider CannonBulletProvider) {
    this.provider = CannonBulletProvider;
  }


  @Override
  public ProjectileProvider<? extends WeaponProjectile> getProjectileProvider() {
    return provider;
  }
}
