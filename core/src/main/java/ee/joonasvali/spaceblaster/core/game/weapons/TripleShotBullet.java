package ee.joonasvali.spaceblaster.core.game.weapons;

import ee.joonasvali.spaceblaster.core.game.player.Rocket;

public class TripleShotBullet extends WeaponProjectile {
  public static final int TIME_BEFORE_EXPLODE = 20;
  private final TripleShotBulletProvider provider;
  private int count = 0;
  public TripleShotBullet(TripleShotBulletProvider provider) {
    this.provider = provider;
  }


  @Override
  public ProjectileProvider<? extends WeaponProjectile> getProjectileProvider() {
    return provider;
  }

  @Override
  public void nextPosition() {
    count++;
    super.nextPosition();
    if (count == TIME_BEFORE_EXPLODE) {
      TripleShotBullet bullet = (TripleShotBullet) provider.getState().getWeaponProjectileManager()
          .createProjectileAt(TripleShotBullet.class, getAuthor(), this.getX(), this.getY(), this.getAngle() - 10);
      bullet.count = TIME_BEFORE_EXPLODE + 1;
      bullet = (TripleShotBullet) provider.getState().getWeaponProjectileManager()
          .createProjectileAt(TripleShotBullet.class, getAuthor(), this.getX(), this.getY(), this.getAngle() + 10);
      bullet.count = TIME_BEFORE_EXPLODE + 1;

      if (getAuthor() instanceof Rocket) {
        this.provider.getState().getEventLog().playerProjectileCreated();
        this.provider.getState().getEventLog().playerProjectileCreated();
      }

      this.provider.getSplitSound().play(0.3f);
    }
  }

  @Override
  public void reset() {
    super.reset();
    count = 0;
  }
}
