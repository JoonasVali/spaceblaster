package ee.joonasvali.spaceblaster.core;

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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Joonas Vali February 2017
 */
public class ParticleEffectManager implements Disposable {
  private static final float PARTICLE_SCALE_FACTOR = 0.03f;
  public static final String HIT = "hit";
  public static final String MISSILE = "missile";
  public static final String EXPLOSION = "explosion";
  public static final String BIRTH = "birth";

  private final Map<String, ParticleEffectType> effects = new HashMap<>();
  private final List<ParticleEffectData> activeEffects = new ArrayList<>();

  public ParticleEffectManager() {
    effects.put(HIT, new ParticleEffectType("particles/hit.p", PARTICLE_SCALE_FACTOR));
    effects.put(MISSILE, new ParticleEffectType("particles/missile.p", PARTICLE_SCALE_FACTOR));
    effects.put(EXPLOSION, new ParticleEffectType("particles/explosion.p", PARTICLE_SCALE_FACTOR));
    effects.put(BIRTH, new ParticleEffectType("particles/birth.p", 0.2f));
  }

  public void createParticleEmitter(String particleId, float x, float y, float rotation) {
    ParticleEffectType data = effects.get(particleId);
    if (data != null) {
      ParticleEffectPool.PooledEffect effect = data.particlePool.obtain();
      activeEffects.add(new StableEmitter(effect, rotation, x, y));
    }
  }

  public void createParticleEmitter(String particleId, PositionProvider provider, float rotation) {
    ParticleEffectType data = effects.get(particleId);
    if (data != null) {
      ParticleEffectPool.PooledEffect effect = data.particlePool.obtain();
      activeEffects.add(new MovingEmitter(effect, rotation, provider));
    }
  }

  @Override
  public void dispose() {
    effects.values().forEach(ParticleEffectType::dispose);
  }

  public void draw(SpriteBatch batch, float delta) {
    Iterator<ParticleEffectData> it = activeEffects.iterator();
    while (it.hasNext()) {
      ParticleEffectData data = it.next();
      ParticleEffectPool.PooledEffect e = data.getEffect();

      rotateParticleEmitter(e, data.getRotation());
      e.setPosition(data.getX(), data.getY());

      e.draw(batch, delta); // Is delta really ok to use? GameStepListener maybe?
      if (e.isComplete()) {
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

  private class ParticleEffectType implements Disposable {
    private final ParticleEffect effect;
    private final TextureAtlas particleAtlas;
    private final ParticleEffectPool particlePool;

    public ParticleEffectType(String particlePath, float scaleFactor) {
      particleAtlas = new TextureAtlas();
      Texture particleTexture = new Texture(Gdx.files.internal("particles/particle.png"));
      particleAtlas.addRegion("particle", particleTexture, 0, 0, 32, 32);
      particleAtlas.addRegion("pre_particle", particleTexture, 0, 0, 32, 32);
      effect = new ParticleEffect();
      effect.load(Gdx.files.internal(particlePath), particleAtlas);
      effect.scaleEffect(scaleFactor);
      particlePool = new ParticleEffectPool(effect, 1, 10);
    }

    @Override
    public void dispose() {
      particleAtlas.dispose();
      effect.dispose();
    }
  }

  private interface ParticleEffectData {
    float getRotation();
    ParticleEffectPool.PooledEffect getEffect();
    float getX();
    float getY();
  }

  private class StableEmitter implements ParticleEffectData {
    private final ParticleEffectPool.PooledEffect emitter;
    private final float rotation;
    private final float x;
    private final float y;

    public StableEmitter(ParticleEffect emitter, float rotation, float x, float y) {
      this.emitter = (ParticleEffectPool.PooledEffect) emitter;
      this.rotation = rotation;
      this.x = x;
      this.y = y;
    }

    @Override
    public float getRotation() {
      return rotation;
    }

    @Override
    public ParticleEffectPool.PooledEffect getEffect() {
      return emitter;
    }

    @Override
    public float getX() {
      return x;
    }

    @Override
    public float getY() {
      return y;
    }
  }

  private class MovingEmitter implements ParticleEffectData {
    private final ParticleEffectPool.PooledEffect emitter;
    private final float rotation;
    private final PositionProvider positionProvider;

    public MovingEmitter(ParticleEffect emitter, float rotation, PositionProvider positionProvider) {
      this.emitter = (ParticleEffectPool.PooledEffect) emitter;
      this.rotation = rotation;
      this.positionProvider = positionProvider;
    }

    @Override
    public float getRotation() {
      return rotation;
    }

    @Override
    public ParticleEffectPool.PooledEffect getEffect() {
      return emitter;
    }

    @Override
    public float getX() {
      return positionProvider.getX();
    }

    @Override
    public float getY() {
      return positionProvider.getY();
    }
  }

  public interface PositionProvider {
    float getX();
    float getY();
  }
}
