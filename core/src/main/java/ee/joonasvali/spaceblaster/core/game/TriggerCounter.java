package ee.joonasvali.spaceblaster.core.game;

/**
 * @author Joonas Vali January 2017
 */
public class TriggerCounter {
  private final int triggerAt;
  private Runnable runnable;
  private int count;
  private final boolean repeat;

  public TriggerCounter(Runnable runnable, int triggerAt, boolean repeat) {
    this(runnable, triggerAt, 0, repeat);
  }

  public TriggerCounter(Runnable runnable, int triggerAt, int count, boolean repeat) {
    this.triggerAt = triggerAt;
    this.runnable = runnable;
    this.count = count;
    this.repeat = repeat;
  }

  public void countDown() {
    if (runnable != null && ++count >= triggerAt) {
      count %= triggerAt;
      try {
        runnable.run();
      } finally {
        if (!repeat) {
          runnable = null;
        }
      }
    }
  }
}
