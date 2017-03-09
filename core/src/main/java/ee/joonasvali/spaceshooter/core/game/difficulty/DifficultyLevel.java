package ee.joonasvali.spaceshooter.core.game.difficulty;

/**
 * @author Joonas Vali March 2017
 */
public enum DifficultyLevel {
  EASY(new EasyGameSettings()),
  NORMAL(new NormalGameSettings()),
  HARD(new HardGameSettings());

  private final GameSettings settings;

  DifficultyLevel(GameSettings settings) {
    this.settings = settings;
  }

  public GameSettings getGameSettings() {
    return settings;
  }
}
