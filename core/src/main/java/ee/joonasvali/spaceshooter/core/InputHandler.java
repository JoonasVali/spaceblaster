package ee.joonasvali.spaceshooter.core;

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

  @Override
  public boolean keyDown(int keycode) {
    List<Runnable> list = keyMap.get(keycode);
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

}
