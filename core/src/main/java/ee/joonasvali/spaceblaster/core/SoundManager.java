package ee.joonasvali.spaceblaster.core;

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
  private final Sound menuClick;
  private final Sound hit;
  private final Sound hit2;
  private final Sound damageSound;
  private Sound missileSound;
  private Sound missileSound2;
  private Sound missileSound3;
  private Sound gaussSound;
  private Sound cannonSound;
  private Sound powerupSound;
  private Sound rocketExplosionSound;
  private Sound trishotSound1;
  private Sound trishotSound2;

  public SoundManager() {
    this.menuClick = Gdx.audio.newSound(Gdx.files.internal("sound/menuclick.mp3"));
    this.hit = Gdx.audio.newSound(Gdx.files.internal("sound/hit.mp3"));
    this.hit2 = Gdx.audio.newSound(Gdx.files.internal("sound/hit2.mp3"));
    this.damageSound = Gdx.audio.newSound(Gdx.files.internal("sound/damage.mp3"));
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
    hit.dispose();
    hit2.dispose();
    damageSound.dispose();
    missileSound.dispose();
    missileSound2.dispose();
    missileSound3.dispose();
    gaussSound.dispose();
    cannonSound.dispose();
    trishotSound1.dispose();
    trishotSound2.dispose();
    rocketExplosionSound.dispose();
    powerupSound.dispose();
  }

  public Music createMusic() {
    return Gdx.audio.newMusic(Gdx.files.internal(MUSIC_PATH));
  }

  public Sound getMenuClickSound() {
    return menuClick;
  }

  public Sound getHitSound() {
    return hit;
  }

  public Sound getHit2Sound() {
    return hit2;
  }

  public Sound getDamageSound() {
    return damageSound;
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
}
