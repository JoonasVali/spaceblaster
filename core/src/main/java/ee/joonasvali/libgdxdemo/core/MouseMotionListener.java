package ee.joonasvali.libgdxdemo.core;

@FunctionalInterface
public interface MouseMotionListener {
  boolean mouseMoved(int screenX, int screenY);
}
