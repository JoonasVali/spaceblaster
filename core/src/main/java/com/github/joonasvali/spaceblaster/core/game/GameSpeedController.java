package com.github.joonasvali.spaceblaster.core.game;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Joonas Vali January 2017
 */
public class GameSpeedController {
  private float lastTime;
  private final float secondsInStep;
  private final Set<GameStepListener> stepListeners = new HashSet<>();
  private final Map<GameStepListener, Integer> skippers = new IdentityHashMap<>();
  private final Control control;

  public GameSpeedController(int msInStep) {
    this.secondsInStep = msInStep / 1000f;
    control = new Control();
  }

  public boolean registerGameStepListener(GameStepListener listener) {
    return stepListeners.add(listener);
  }

  public boolean unRegisterGameStepListener(GameStepListener listener) {
    return stepListeners.remove(listener);
  }

  public void passTime(float delta) {
    lastTime += delta;
    while (lastTime > secondsInStep) {
      for (GameStepListener stepListener : stepListeners) {
        boolean skip = needsSkip(stepListener);
        if (!skip) {
          try {
            control.listener = stepListener;
            stepListener.onStepAction(control);
          } finally {
            control.listener = null;
          }
        }
      }

      for (GameStepListener stepListener : stepListeners) {
        boolean skip = needsSkip(stepListener);
        if (!skip) {
          stepListener.onStepEffect();
        }
      }

      countDownSkippers();
      lastTime -= secondsInStep;
    }
  }

  private boolean needsSkip(GameStepListener stepListener) {
    return skippers.getOrDefault(stepListener, 0) > 0;
  }

  private void countDownSkippers() {
    Iterator<Map.Entry<GameStepListener, Integer>> it = skippers.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<GameStepListener, Integer> e = it.next();
      int val = e.getValue();
      val--;
      if (val > 0) {
        e.setValue(val);
      } else {
        it.remove();
      }
    }
  }

  public void skip(GameStepListener listener, int steps) {
    Integer previous = skippers.get(listener);
    if (previous == null) {
      skippers.put(listener, steps);
    } else {
      skippers.put(listener, steps + previous);
    }
  }

  public class Control {
    private GameStepListener listener;
    public void skipNextSteps(int step) {
      skip(listener, step);
    }
  }
}
