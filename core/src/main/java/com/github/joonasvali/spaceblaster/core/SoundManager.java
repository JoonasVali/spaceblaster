package com.github.joonasvali.spaceblaster.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

/**
 * Handles sounds.
 *
 * Notice get vs create methods.
 *
 * get..() means SoundManager handled singleton instance which is disposed by SoundManager later.
 * create..() means SoundManager just creates the instance and the caller must dispose it when done.
 *
 *
 * @author Joonas Vali March 2017
 */
public class SoundManager implements Disposable {

  public static final String MUSIC_PATH = "sound/LB13_Nasdaq_byBobbyYarsulik.mp3";
  public static final String CANNON_SOUND_PATH = "sound/cannon.mp3";
  private final Sound menuClick;
  private final Sound enemyDestroy1;
  private final Sound enemyDestroy2;
  private final Sound enemyDestroy3;
  private final Sound hitSound1;
  private final Sound hitSound2;
  private final Sound hitSound3;
  private final Sound hitSound4;
  private final Sound missileSound;
  private final Sound missileSound2;
  private final Sound missileSound3;
  private final Sound gaussSound;
  private final Sound cannonSound;
  private final Sound powerupSound;
  private final Sound rocketExplosionSound;
  private final Sound trishotSound1;
  private final Sound trishotSound2;
  private final Sound enemiesRespawnSound;

  public SoundManager() {
    this.menuClick = Gdx.audio.newSound(Gdx.files.internal("sound/menuclick.mp3"));
    this.enemyDestroy1 = Gdx.audio.newSound(Gdx.files.internal("sound/enemy_destroy1.mp3"));
    this.enemyDestroy2 = Gdx.audio.newSound(Gdx.files.internal("sound/enemy_destroy2.mp3"));
    this.enemyDestroy3 = Gdx.audio.newSound(Gdx.files.internal("sound/enemy_destroy3.mp3"));
    this.hitSound1 = Gdx.audio.newSound(Gdx.files.internal("sound/hit.mp3"));
    this.hitSound2 = Gdx.audio.newSound(Gdx.files.internal("sound/hit2.mp3"));
    this.hitSound3 = Gdx.audio.newSound(Gdx.files.internal("sound/hit3.mp3"));
    this.hitSound4 = Gdx.audio.newSound(Gdx.files.internal("sound/hit4.mp3"));
    this.enemiesRespawnSound = Gdx.audio.newSound(Gdx.files.internal("sound/enemies.mp3"));
    this.missileSound = Gdx.audio.newSound(Gdx.files.internal("sound/missile.mp3"));
    this.missileSound2 = Gdx.audio.newSound(Gdx.files.internal("sound/missile2.mp3"));
    this.missileSound3 = Gdx.audio.newSound(Gdx.files.internal("sound/missile3.mp3"));
    this.gaussSound = Gdx.audio.newSound(Gdx.files.internal("sound/gauss.mp3"));
    this.cannonSound = Gdx.audio.newSound(Gdx.files.internal("sound/cannon.mp3"));
    this.trishotSound1 = Gdx.audio.newSound(Gdx.files.internal("sound/trishot1.mp3"));
    this.trishotSound2 = Gdx.audio.newSound(Gdx.files.internal("sound/trishot2.mp3"));
    this.rocketExplosionSound = Gdx.audio.newSound(Gdx.files.internal("sound/rocket-explosion.mp3"));
    this.powerupSound = Gdx.audio.newSound(Gdx.files.internal("sound/powerup.mp3"));
  }

  @Override
  public void dispose() {
    menuClick.dispose();
    enemyDestroy1.dispose();
    enemyDestroy2.dispose();
    enemyDestroy3.dispose();
    hitSound1.dispose();
    hitSound2.dispose();
    hitSound3.dispose();
    hitSound4.dispose();
    missileSound.dispose();
    missileSound2.dispose();
    missileSound3.dispose();
    gaussSound.dispose();
    cannonSound.dispose();
    trishotSound1.dispose();
    trishotSound2.dispose();
    rocketExplosionSound.dispose();
    powerupSound.dispose();
    enemiesRespawnSound.dispose();
  }

  public Music createMusic() {
    return Gdx.audio.newMusic(Gdx.files.internal(MUSIC_PATH));
  }

  public Sound getMenuClickSound() {
    return menuClick;
  }

  public Sound getEnemyDestroy1() {
    return enemyDestroy1;
  }

  public Sound getEnemyDestroy2() {
    return enemyDestroy2;
  }

  public Sound getEnemyDestroy3() {
    return enemyDestroy3;
  }

  public Sound getHitSound1() {
    return hitSound1;
  }

  public Sound getHitSound2() {
    return hitSound2;
  }

  public Sound getHitSound3() {
    return hitSound3;
  }

  public Sound getHitSound4() {
    return hitSound4;
  }

  public Sound getMissileSound() {
    return missileSound;
  }

  public Sound getMissileSound2() {
    return missileSound2;
  }

  public Sound getMissileSound3() {
    return missileSound3;
  }

  public Sound getGaussSound() {
    return gaussSound;
  }

  public Sound getCannonSound() {
    return cannonSound;
  }

  public Sound getPowerupSound() {
    return powerupSound;
  }

  public Sound getRocketExplosionSound() {
    return rocketExplosionSound;
  }

  public Sound getTrishotSound1() {
    return trishotSound1;
  }

  public Sound getTrishotSound2() {
    return trishotSound2;
  }

  public Sound getEnemiesRespawnSound() {
    return enemiesRespawnSound;
  }
}
