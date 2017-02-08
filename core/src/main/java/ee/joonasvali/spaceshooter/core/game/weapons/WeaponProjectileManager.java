package ee.joonasvali.spaceshooter.core.game.weapons;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import ee.joonasvali.spaceshooter.core.game.GameStepListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Joonas Vali January 2017
 */
public class WeaponProjectileManager implements Disposable, GameStepListener {
  private float worldHeight;
  private float worldWidth;

  private List<WeaponProjectile> activeProjectiles = new ArrayList<>();
  private Map<Class, ProjectileProvider> providers = new HashMap<>();


  public WeaponProjectileManager(float worldWidth, float worldHeight) {
    this.worldWidth = worldWidth;
    this.worldHeight = worldHeight;
    this.providers.put(Missile.class, new MissileProvider());
    this.providers.put(GaussGunBullet.class, new GaussGunBulletProvider());
  }

  private void moveAndRemoveProjectiles() {
    activeProjectiles.forEach(WeaponProjectile::nextPosition);

    List<WeaponProjectile> outOfBounds =
        activeProjectiles.stream().filter(
            m -> m.getY() > worldHeight + 5 || m.getY() < -5 || m.getX() > worldWidth + 5 || m.getX() < -5
        ).collect(Collectors.toList());

    outOfBounds.forEach(this::removeProjectile);
  }

  public void removeProjectile(WeaponProjectile m) {
    activeProjectiles.remove(m);
    m.getProjectileProvider().free(m);
  }


  public WeaponProjectile createProjectileAt(Class<? extends WeaponProjectile> type, Object author, float x, float y, float angle) {
    ProjectileProvider provider = providers.get(type);
    if (provider == null) {
      throw new IllegalArgumentException("Projectile provider missing for: " + type);
    }
    WeaponProjectile projectile = provider.obtain();
    projectile.setPosition(x, y);
    projectile.setAngle(angle);
    projectile.setAuthor(author);
    Sound sound = provider.getSound();
    if (sound != null) {
      sound.play(0.5f);
    }

    activeProjectiles.add(projectile);
    return projectile;
  }

  public void drawMissiles(SpriteBatch batch) {
    activeProjectiles.forEach(m -> m.getProjectileProvider().draw(batch, m));
  }

  @Override
  public void dispose() {
    providers.forEach((a, b) -> {
      if (b instanceof Disposable) {
        ((Disposable) b).dispose();
      }
    });
    activeProjectiles.clear();
  }

  public Optional<WeaponProjectile> projectileCollisionWith(Rectangle r, Object excludeMissilesFromAuthor) {
    for (WeaponProjectile m : activeProjectiles) {
      if (m.overlaps(r) && !m.getAuthor().equals(excludeMissilesFromAuthor)) {
        return Optional.of(m);
      }
    }
    return Optional.empty();
  }

  @Override
  public void onStepAction() {
    moveAndRemoveProjectiles();
  }

  @Override
  public void onStepEffect() {

  }
}
