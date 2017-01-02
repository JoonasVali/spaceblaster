package ee.joonasvali.spaceshooter.core;

@FunctionalInterface
public interface MouseMotionListener {
  boolean mouseMoved(int screenX, int screenY);
}
