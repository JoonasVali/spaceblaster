package ee.joonasvali.libgdxdemo.core;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

/**
 * @author Joonas Vali December 2016
 */
public class Util {
  private static final Vector3 vector = new Vector3();
  public static float unProjectX(Camera cam, int origX, int origY) {
    vector.set(origX, origY, 0);
    return cam.unproject(vector).x;
  }

  public static float unProjectY(Camera cam, int origX, int origY) {
    vector.set(origX, origY, 0);
    return cam.unproject(vector).y;
  }
}
