package ee.joonasvali.spaceshooter.core.game;

import ee.joonasvali.spaceshooter.core.ParticleEffectManager;
import ee.joonasvali.spaceshooter.core.SoundManager;
import ee.joonasvali.spaceshooter.core.game.difficulty.GameSettings;
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
  private GameStateManager enemies;
  private WeaponProjectileManager weaponProjectileManager;
  private ExplosionManager explosionManager;
  private ParticleEffectManager particleManager;
  private SoundManager soundManager;
  private AtomicInteger score;
  private AtomicInteger lives;
  private GameSettings gameSettings;
  private boolean victory;
  private boolean defeat;

  public SoundManager getSoundManager() {
    return soundManager;
  }

  public void setSoundManager(SoundManager soundManager) {
    this.soundManager = soundManager;
  }

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

  public GameStateManager getEnemies() {
    return enemies;
  }

  public void setEnemies(GameStateManager enemies) {
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

  public boolean isVictory() {
    return victory;
  }

  public void setVictory(boolean victory) {
    this.victory = victory;
  }

  public boolean isDefeat() {
    return defeat;
  }

  public void setDefeat(boolean defeat) {
    this.defeat = defeat;
  }

  public ParticleEffectManager getParticleManager() {
    return particleManager;
  }

  public void setParticleManager(ParticleEffectManager particleManager) {
    this.particleManager = particleManager;
  }

  public GameSettings getGameSettings() {
    return gameSettings;
  }

  public void setGameSettings(GameSettings gameSettings) {
    this.gameSettings = gameSettings;
  }
}
