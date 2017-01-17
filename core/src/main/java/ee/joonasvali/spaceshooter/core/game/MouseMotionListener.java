package ee.joonasvali.spaceshooter.core.game;

@FunctionalInterface
public interface MouseMotionListener {
  boolean mouseMoved(int screenX, int screenY);
}
