package ee.joonasvali.spaceshooter.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Joonas Vali February 2017
 */
public class ParticleEffectManager implements Disposable {
  public static final float PARTICLE_HIT_SCALE_FACTOR = 0.03f;

  private final Map<String, ParticleEffectData> effects = new HashMap<>();
  private final List<ParticleEffectPool.PooledEffect> activeEffects = new ArrayList<>();
  private final Map<ParticleEffectPool.PooledEffect, Float> activeEffectsRotation = new IdentityHashMap<>();

  public ParticleEffectManager() {
    effects.put("hit", new ParticleEffectData("particles/hit.p"));
    effects.put("explosion", new ParticleEffectData("particles/explosion.p"));
  }

  public void createParticleEmitter(String particleId, float x, float y, float rotation) {
    ParticleEffectData data = effects.get(particleId);
    if (data != null) {
      ParticleEffectPool.PooledEffect effect = data.particlePool.obtain();
      effect.setPosition(x, y);
      activeEffects.add(effect);
      activeEffectsRotation.put(effect, rotation);
    }
  }

  @Override
  public void dispose() {
    effects.values().forEach(ParticleEffectData::dispose);
  }

  public void draw(SpriteBatch batch, float delta) {
    Iterator<ParticleEffectPool.PooledEffect> it = activeEffects.iterator();
    while (it.hasNext()) {
      ParticleEffectPool.PooledEffect e = it.next();
      rotateParticleEmitter(e, activeEffectsRotation.get(e));
      e.draw(batch, delta); // Is delta really ok to use? GameStepListener maybe?
      if (e.isComplete()) {
        activeEffectsRotation.remove(e);
        it.remove();
        e.free();
      }
    }
  }

  /**
   * This quirky way seems to be the correct way (and only way?) to rotate particle emitter according to many posts in stackoverflow and other pages.
   */
  private void rotateParticleEmitter(ParticleEffect effect, float targetAngle) {
    targetAngle += 90;
    Array<ParticleEmitter> emitters = effect.getEmitters();
    for (int i = 0; i < emitters.size; i++) {
      ParticleEmitter.ScaledNumericValue angle = emitters.get(i).getAngle();

      float angleHighMin = angle.getHighMin();
      float angleHighMax = angle.getHighMax();
      float spanHigh = angleHighMax - angleHighMin;
      angle.setHigh(targetAngle - spanHigh / 2.0f, targetAngle + spanHigh / 2.0f);

      float angleLowMin = angle.getLowMin();
      float angleLowMax = angle.getLowMax();
      float spanLow = angleLowMax - angleLowMin;
      angle.setLow(targetAngle - spanLow / 2.0f, targetAngle + spanLow / 2.0f);
    }
  }

  private class ParticleEffectData implements Disposable {
    private final ParticleEffect effect;
    private final TextureAtlas particleAtlas;
    private final ParticleEffectPool particlePool;

    public ParticleEffectData(String particlePath) {
      particleAtlas = new TextureAtlas();
      Texture particleTexture = new Texture(Gdx.files.internal("particles/particle.png"));
      particleAtlas.addRegion("particle", particleTexture, 0, 0, 32, 32);
      particleAtlas.addRegion("pre_particle", particleTexture, 0, 0, 32, 32);
      effect = new ParticleEffect();
      effect.load(Gdx.files.internal(particlePath), particleAtlas);
      effect.scaleEffect(PARTICLE_HIT_SCALE_FACTOR);
      particlePool = new ParticleEffectPool(effect, 1, 10);
    }

    @Override
    public void dispose() {
      particleAtlas.dispose();
      effect.dispose();
    }
  }
}
