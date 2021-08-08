package ee.joonasvali.spaceblaster.core.game.weapons;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import ee.joonasvali.spaceblaster.core.ParticleEffectManager;
import ee.joonasvali.spaceblaster.core.game.GameSpeedController;
import ee.joonasvali.spaceblaster.core.game.GameState;
import ee.joonasvali.spaceblaster.core.game.GameStepListener;
import ee.joonasvali.spaceblaster.core.game.enemy.Enemy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author Joonas Vali January 2017
 */
public class WeaponProjectileManager implements Disposable, GameStepListener {
  private float worldHeight;
  private float worldWidth;

  private List<WeaponProjectile> activeProjectiles = new CopyOnWriteArrayList<>();
  private Map<Class<? extends WeaponProjectile>, ProjectileProvider> providers = new HashMap<>();
  private Class<WeaponProjectile>[] weaponClasses;
  private ParticleEffectManager particleManager;

  public WeaponProjectileManager(GameState state) {
    this.particleManager = state.getParticleManager();
    this.worldWidth = state.getWorldWidth();
    this.worldHeight = state.getWorldHeight();
    this.providers.put(Missile.class, new MissileProvider(state));
    this.providers.put(GaussGunBullet.class, new GaussGunBulletProvider(state));
    this.providers.put(CannonBullet.class, new CannonBulletProvider(state));
    this.providers.put(TripleShotBullet.class, new TripleShotBulletProvider(state));
    weaponClasses = this.providers.keySet().toArray(new Class[this.providers.size()]);
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
  public ParticleEffectManager.PositionProvider createPositionProvider(WeaponProjectile projectile) {
    return new ParticleEffectManager.PositionProvider() {
      @Override
      public float getX() {
        return projectile.getX() + projectile.getWidth() / 2;
      }

      @Override
      public float getY() {
        return projectile.getY() + projectile.getHeight() / 2;
      }
    };
  }

  public WeaponProjectile createProjectileAt(Class<? extends WeaponProjectile> type, Object author, float x, float y, float angle) {
    ProjectileProvider<?> provider = providers.get(type);
    WeaponProjectile projectile = createProjectileSoundless(type, author, x, y, angle);
    Sound sound = provider.getSound();
    if (sound != null) {
      sound.play(0.5f);
    }
    return projectile;
  }

  public WeaponProjectile createProjectileSoundless(Class<? extends WeaponProjectile> type, Object author, float x, float y, float angle) {
    ProjectileProvider<?> provider = providers.get(type);
    if (provider == null) {
      throw new IllegalArgumentException("Projectile provider missing for: " + type);
    }
    WeaponProjectile projectile = provider.obtain();
    projectile.setPosition(x, y);
    projectile.setAngle(angle);
    projectile.setAuthor(author);
    activeProjectiles.add(projectile);
    projectile.getParticlEffectKey().ifPresent(
        (key) -> particleManager.createParticleEmitter(key, createPositionProvider(projectile), 0)
    );
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
  public void onStepAction(GameSpeedController.Control control) {
    moveAndRemoveProjectiles();
  }

  @Override
  public void onStepEffect() {

  }

  public int getCooldown(Class<? extends WeaponProjectile> projectileClass) {
    ProjectileProvider provider = providers.get(projectileClass);
    if (provider != null) {
      return provider.getCoolDown();
    }
    return 0;
  }

  @SuppressWarnings("unchecked")
  public Class<WeaponProjectile>[] getWeaponClasses(Class<? extends WeaponProjectile> exclude) {
    List<Class<? extends WeaponProjectile>> values = new ArrayList<>(Arrays.asList(weaponClasses));
    values.remove(exclude);
    return values.toArray(new Class[values.size()]);
  }

  public Class<WeaponProjectile>[] getWeaponClasses() {
    return weaponClasses;
  }
}
