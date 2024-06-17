package com.github.joonasvali.spaceblaster.core.game;

import com.badlogic.gdx.InputAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Joonas Vali December 2016
 */
public class InputHandler extends InputAdapter {
  private Map<Integer, List<Runnable>> keyMap = new HashMap<>();
  private Map<Integer, List<Runnable>> mouseMap = new HashMap<>();

  @Override
  public boolean keyDown(int keycode) {
    List<Runnable> list = keyMap.get(keycode);
    if (list == null) {
      return false;
    }
    list.forEach(Runnable::run);
    return true;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    List<Runnable> list = mouseMap.get(button);
    if (list == null) {
      return false;
    }
    list.forEach(Runnable::run);
    return true;
  }

  public void addKeyBinding(Integer key, Runnable runnable) {
    List<Runnable> list = keyMap.computeIfAbsent(key, k -> new ArrayList<>());
    list.add(runnable);
  }


  public void addMouseBinding(int mouseButton, Runnable runnable) {
    List<Runnable> list = mouseMap.computeIfAbsent(mouseButton, k -> new ArrayList<>());
    list.add(runnable);
  }
}
