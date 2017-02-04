package ee.joonasvali.spaceshooter.core.game;

import ee.joonasvali.spaceshooter.core.game.enemy.EnemyManager;
import ee.joonasvali.spaceshooter.core.game.player.Rocket;
import ee.joonasvali.spaceshooter.core.game.weapons.WeaponProjectileManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Joonas Vali February 2017
 */
public class GameState {
  private UIOverlay ui;
  private Background background;
  private Rocket rocket;
  private EnemyManager enemies;
  private WeaponProjectileManager weaponProjectileManager;
  private ExplosionManager explosionManager;
  private AtomicInteger score;
  private AtomicInteger lives;

  public UIOverlay getUi() {
    return ui;
  }

  public void setUi(UIOverlay ui) {
    this.ui = ui;
  }

  public Background getBackground() {
    return background;
  }

  public void setBackground(Background background) {
    this.background = background;
  }

  public Rocket getRocket() {
    return rocket;
  }

  public void setRocket(Rocket rocket) {
    this.rocket = rocket;
  }

  public EnemyManager getEnemies() {
    return enemies;
  }

  public void setEnemies(EnemyManager enemies) {
    this.enemies = enemies;
  }

  public WeaponProjectileManager getWeaponProjectileManager() {
    return weaponProjectileManager;
  }

  public void setWeaponProjectileManager(WeaponProjectileManager weaponProjectileManager) {
    this.weaponProjectileManager = weaponProjectileManager;
  }

  public ExplosionManager getExplosionManager() {
    return explosionManager;
  }

  public void setExplosionManager(ExplosionManager explosionManager) {
    this.explosionManager = explosionManager;
  }

  public AtomicInteger getScore() {
    return score;
  }

  public void setScore(AtomicInteger score) {
    this.score = score;
  }

  public AtomicInteger getLives() {
    return lives;
  }

  public void setLives(AtomicInteger lives) {
    this.lives = lives;
  }
}
