package com.github.joonasvali.spaceblaster.core.game;

import com.github.joonasvali.spaceblaster.core.event.EventLog;
import com.github.joonasvali.spaceblaster.core.game.difficulty.GameSettings;
import com.github.joonasvali.spaceblaster.core.game.player.Rocket;
import com.github.joonasvali.spaceblaster.core.game.weapons.WeaponProjectileManager;
import com.github.joonasvali.spaceblaster.core.ParticleEffectManager;
import com.github.joonasvali.spaceblaster.core.SoundManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Joonas Vali February 2017
 */
public class GameState {
  private UIOverlay ui;
  private Background background;
  private Rocket rocket;
  private GameStateManager gameStateManager;
  private WeaponProjectileManager weaponProjectileManager;
  private ExplosionManager explosionManager;
  private ParticleEffectManager particleManager;
  private SoundManager soundManager;
  private AtomicInteger score;
  private AtomicInteger lives;
  private GameSettings gameSettings;
  private boolean victory;
  private boolean defeat;
  private final int worldWidth;
  private final int worldHeight;
  private PowerupManager powerupManager;
  private EventLog eventLog;

  public GameState(int worldWidth, int worldHeight) {
    this.worldWidth = worldWidth;
    this.worldHeight = worldHeight;
  }

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

  public GameStateManager getGameStateManager() {
    return gameStateManager;
  }

  public void setEnemies(GameStateManager enemies) {
    this.gameStateManager = enemies;
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

  public int getWorldWidth() {
    return worldWidth;
  }

  public int getWorldHeight() {
    return worldHeight;
  }

  public void setPowerupManager(PowerupManager powerupManager) {
    this.powerupManager = powerupManager;
  }

  public PowerupManager getPowerupManager() {
    return powerupManager;
  }

  public EventLog getEventLog() {
    return eventLog;
  }

  public void setEventLog(EventLog eventLog) {
    this.eventLog = eventLog;
  }
}
