package ee.joonasvali.spaceblaster.event;

public class Event {
  /*
    Begin Round specific fields
  */
  public String levelName;
  public String episodeName;

  public long timeSinceRoundStartMs;
  public int roundsFinishedCount;

  public int enemiesLeftThisRoundCount;

  public int enemiesKilledThisRoundCount;
  public int enemiesHitEnemiesThisRoundCount;
  public int enemiesKilledEnemiesThisRoundCount;

  public int enemiesLeftWithGaussGunCount;
  public int enemiesLeftWithMissileCount;
  public int enemiesLeftWithCannonCount;
  public int enemiesLeftWithTripleShotCount;

  public int enemiesStartedWithGaussGunCount;
  public int enemiesStartedWithMissileCount;
  public int enemiesStartedWithCannonCount;
  public int enemiesStartedWithTripleShotCount;
  /*
    End Round specific fields
  */

  public void initializeRound(
    String levelName,
    int roundsFinishedCount,
    int enemiesStartedWithGaussGunCount,
    int enemiesStartedWithMissileCount,
    int enemiesStartedWithCannonCount,
    int enemiesStartedWithTripleShotCount
  ) {
    this.levelName = levelName;
    this.timeSinceRoundStartMs = 0;
    this.roundsFinishedCount = roundsFinishedCount;
    this.enemiesLeftThisRoundCount = enemiesStartedWithGaussGunCount + enemiesStartedWithMissileCount + enemiesStartedWithCannonCount + enemiesStartedWithTripleShotCount;
    this.enemiesKilledThisRoundCount = 0;
    this.enemiesHitEnemiesThisRoundCount = 0;
    this.enemiesKilledEnemiesThisRoundCount = 0;
    this.enemiesLeftWithGaussGunCount = enemiesStartedWithGaussGunCount;
    this.enemiesLeftWithMissileCount = enemiesStartedWithMissileCount;
    this.enemiesLeftWithCannonCount = enemiesStartedWithCannonCount;
    this.enemiesLeftWithTripleShotCount = enemiesStartedWithTripleShotCount;
    this.enemiesStartedWithGaussGunCount = enemiesStartedWithGaussGunCount;
    this.enemiesStartedWithMissileCount = enemiesStartedWithMissileCount;
    this.enemiesStartedWithCannonCount = enemiesStartedWithCannonCount;
    this.enemiesStartedWithTripleShotCount = enemiesStartedWithTripleShotCount;
  }

  public void initializeGame(
    String episodeName,
    GameDifficulty gameDifficulty,
    int playerLivesOriginal
  ) {
    this.episodeName = episodeName;
    this.gameDifficulty = gameDifficulty;
    this.playerLivesOriginal = playerLivesOriginal;
    this.playerLivesLeft = playerLivesOriginal;
    this.playerWeapon = PlayerWeapon.CANNON;
    this.playerPositionX = PositionX.CENTER;
  }

  public void recalculateTimes(long timePassedSinceLastEvent) {
    this.totalGameTimeMs += timePassedSinceLastEvent;
    this.timeSinceRoundStartMs += timePassedSinceLastEvent;
    this.timeSinceLastEventMs = timePassedSinceLastEvent;
    if (timeSinceLastDeathMs != null) {
      timeSinceLastDeathMs += timePassedSinceLastEvent;
    }
    if (timeSinceLastKillMs != null) {
      timeSinceLastKillMs += timePassedSinceLastEvent;
    }
    if (timeSinceLastHitMs != null) {
      timeSinceLastHitMs += timePassedSinceLastEvent;
    }
    if (timeFromLastPowerupCollectedMs != null) {
      timeFromLastPowerupCollectedMs += timePassedSinceLastEvent;
    }
    if (timeFromLastPowerupMissedMs != null) {
      timeFromLastPowerupMissedMs += timePassedSinceLastEvent;
    }
  }

  public GameDifficulty gameDifficulty;


  public long totalGameTimeMs;
  public long timeSinceLastEventMs;
  public Long timeSinceLastDeathMs;
  public Long timeSinceLastKillMs;
  public Long timeSinceLastHitMs;
  public int totalRoundsCount;

  public int playerLivesLeft;
  public int playerLivesOriginal;

  public int playerScore;



  public int enemyTouchedPlayerDeathsCount;

  public int powerUpsCollectedThisRoundCount;
  public int powerUpsCollectedTotalCount;
  public int powerUpsMissedCount;
  public int powerUpsGaussGunCollectedCount;
  public int powerUpsMissileCollectedCount;
  public int powerUpsTripleShotCollectedCount;
  public int powerUpsCannonCollectedCount;

  public Long timeFromLastPowerupCollectedMs;
  public Long timeFromLastPowerupMissedMs;

  public EnemySpeed enemySpeed;
  public EnemyCloseness enemyCloseness;

  public int shotsFiredLastThreeSeconds;
  public PlayerWeapon playerWeapon;
  public PositionX playerPositionX;
  public PositionX enemyPositionXOnScreen;

  public boolean playerDead;
  public boolean inBetweenRounds;

  public boolean enemyBulletFlyingTowardsPlayer;
  public boolean playerIsUnderEnemyFormation;
  public boolean playerIsMoving;
  public boolean isVictory;
  public boolean isDefeat;

}
