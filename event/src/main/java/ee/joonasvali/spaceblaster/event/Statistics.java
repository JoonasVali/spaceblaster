package ee.joonasvali.spaceblaster.event;

public class Statistics {
  /*
    Begin Round specific fields
  */
  public String levelName;
  public String episodeName;

  public long roundStartTimestamp;
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
    this.roundStartTimestamp = System.currentTimeMillis();
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
    this.powerUpsCollectedThisRoundCount = 0;
    this.enemyMovingDirection = MovingDirection.NONE;
  }

  public void initializeGame(
    String episodeName,
    GameDifficulty gameDifficulty,
    int playerLivesOriginal,
    int totalRounds
  ) {
    this.episodeName = episodeName;
    this.gameDifficulty = gameDifficulty;
    this.playerLivesOriginal = playerLivesOriginal;
    this.playerLivesLeft = playerLivesOriginal;
    this.playerWeapon = PlayerWeapon.CANNON;
    this.playerPositionX = PositionX.CENTER;
    this.totalRoundsCount = totalRounds;
    this.gameStartTimestamp = System.currentTimeMillis();
    this.enemyMovingDirection = MovingDirection.NONE;
  }

  public GameDifficulty gameDifficulty;


  public long gameStartTimestamp;
  public Long lastDeathTimestamp;
  public Long lastKillTimestamp;
  public Long lastHitTimestamp;
  public int totalRoundsCount;

  public int playerLivesLeft;
  public int playerLivesOriginal;

  public int playerScore;

  public MovingDirection enemyMovingDirection;
  public int enemyTouchedPlayerDeathsCount;

  public int powerUpsCollectedThisRoundCount;
  public int powerUpsCollectedTotalCount;
  public int powerUpsMissedCount;
  public int powerUpsGaussGunCollectedCount;
  public int powerUpsMissileCollectedCount;
  public int powerUpsTripleShotCollectedCount;
  public int powerUpsCannonCollectedCount;

  public Long lastPowerupTimestamp;
  public Long lastPowerupMissedTimestamp;

  public EnemySpeed enemySpeed;
  public EnemyCloseness enemyCloseness;

  public int shotsFiredLastThreeSeconds;
  public PlayerWeapon playerWeapon;
  public PositionX playerPositionX;
  public PositionX enemyPositionXOnScreen;

  public boolean playerDead;
  public boolean playerInvincible;
  public boolean inBetweenRounds;

  public boolean enemyBulletFlyingTowardsPlayer;
  public boolean playerIsUnderEnemyFormation;
  public boolean playerIsMoving;
  public boolean isVictory;
  public boolean isDefeat;

  protected void copyTo(Event event) {
    event.episodeName = this.episodeName;
    event.levelName = this.levelName;
    event.roundStartTimestamp = this.roundStartTimestamp;
    event.roundsFinishedCount = this.roundsFinishedCount;
    event.enemiesLeftThisRoundCount = this.enemiesLeftThisRoundCount;
    event.enemiesKilledThisRoundCount = this.enemiesKilledThisRoundCount;
    event.enemiesHitEnemiesThisRoundCount = this.enemiesHitEnemiesThisRoundCount;
    event.enemiesKilledEnemiesThisRoundCount = this.enemiesKilledEnemiesThisRoundCount;
    event.enemiesLeftWithGaussGunCount = this.enemiesLeftWithGaussGunCount;
    event.enemiesLeftWithMissileCount = this.enemiesLeftWithMissileCount;
    event.enemiesLeftWithCannonCount = this.enemiesLeftWithCannonCount;
    event.enemiesLeftWithTripleShotCount = this.enemiesLeftWithTripleShotCount;
    event.enemiesStartedWithGaussGunCount = this.enemiesStartedWithGaussGunCount;
    event.enemiesStartedWithMissileCount = this.enemiesStartedWithMissileCount;
    event.enemiesStartedWithCannonCount = this.enemiesStartedWithCannonCount;
    event.enemiesStartedWithTripleShotCount = this.enemiesStartedWithTripleShotCount;
    event.gameDifficulty = this.gameDifficulty;
    event.gameStartTimestamp = this.gameStartTimestamp;
    event.lastDeathTimestamp = this.lastDeathTimestamp;
    event.lastKillTimestamp = this.lastKillTimestamp;
    event.lastHitTimestamp = this.lastHitTimestamp;
    event.totalRoundsCount = this.totalRoundsCount;
    event.playerLivesLeft = this.playerLivesLeft;
    event.playerLivesOriginal = this.playerLivesOriginal;
    event.playerScore = this.playerScore;
    event.enemyMovingDirection = this.enemyMovingDirection;
    event.enemyTouchedPlayerDeathsCount = this.enemyTouchedPlayerDeathsCount;
    event.powerUpsCollectedThisRoundCount = this.powerUpsCollectedThisRoundCount;
    event.powerUpsCollectedTotalCount = this.powerUpsCollectedTotalCount;
    event.powerUpsMissedCount = this.powerUpsMissedCount;
    event.powerUpsGaussGunCollectedCount = this.powerUpsGaussGunCollectedCount;
    event.powerUpsMissileCollectedCount = this.powerUpsMissileCollectedCount;
    event.powerUpsTripleShotCollectedCount = this.powerUpsTripleShotCollectedCount;
    event.powerUpsCannonCollectedCount = this.powerUpsCannonCollectedCount;
    event.lastPowerupTimestamp = this.lastPowerupTimestamp;
    event.lastPowerupMissedTimestamp = this.lastPowerupMissedTimestamp;
    event.enemySpeed = this.enemySpeed;
    event.enemyCloseness = this.enemyCloseness;
    event.shotsFiredLastThreeSeconds = this.shotsFiredLastThreeSeconds;
    event.playerWeapon = this.playerWeapon;
    event.playerPositionX = this.playerPositionX;
    event.enemyPositionXOnScreen = this.enemyPositionXOnScreen;
    event.playerDead = this.playerDead;
    event.playerInvincible = this.playerInvincible;
    event.inBetweenRounds = this.inBetweenRounds;
    event.enemyBulletFlyingTowardsPlayer = this.enemyBulletFlyingTowardsPlayer;
    event.playerIsUnderEnemyFormation = this.playerIsUnderEnemyFormation;
    event.playerIsMoving = this.playerIsMoving;
    event.isVictory = this.isVictory;
    event.isDefeat = this.isDefeat;
  }
}
