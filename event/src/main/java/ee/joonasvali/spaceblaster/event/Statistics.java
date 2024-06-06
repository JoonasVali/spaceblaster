package ee.joonasvali.spaceblaster.event;

public class Statistics {
  /*
    Begin Round specific fields
  */
  public volatile String levelName;
  public volatile String episodeName;

  public volatile long roundStartTimestamp;
  public volatile int roundsFinishedCount;

  public volatile int enemiesLeftThisRoundCount;

  public volatile int enemiesKilledThisRoundCount;
  public volatile int enemiesHitEnemiesThisRoundCount;
  public volatile int enemiesKilledEnemiesThisRoundCount;

  public volatile int enemiesLeftWithGaussGunCount;
  public volatile int enemiesLeftWithMissileCount;
  public volatile int enemiesLeftWithCannonCount;
  public volatile int enemiesLeftWithTripleShotCount;

  public volatile int enemiesStartedWithGaussGunCount;
  public volatile int enemiesStartedWithMissileCount;
  public volatile int enemiesStartedWithCannonCount;
  public volatile int enemiesStartedWithTripleShotCount;

  /*
    End Round specific fields
  */

  public void initializeRound(
    String levelName,
    int enemiesStartedWithGaussGunCount,
    int enemiesStartedWithMissileCount,
    int enemiesStartedWithCannonCount,
    int enemiesStartedWithTripleShotCount
  ) {
    this.levelName = levelName;
    this.roundStartTimestamp = System.currentTimeMillis();
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

  public volatile GameDifficulty gameDifficulty;


  public volatile long gameStartTimestamp;
  public volatile Long lastDeathTimestamp;
  public volatile Long lastKillTimestamp;
  public volatile Long lastHitTimestamp;
  public volatile int totalRoundsCount;

  public volatile int playerLivesLeft;
  public volatile int playerLivesOriginal;

  public volatile int playerScore;

  public volatile MovingDirection enemyMovingDirection;
  public volatile int enemyTouchedPlayerDeathsCount;

  public volatile int powerUpsCollectedThisRoundCount;
  public volatile int powerUpsCollectedTotalCount;
  public volatile int powerUpsMissedCount;
  public volatile int powerUpsGaussGunCollectedCount;
  public volatile int powerUpsMissileCollectedCount;
  public volatile int powerUpsTripleShotCollectedCount;
  public volatile int powerUpsCannonCollectedCount;

  public volatile Long lastPowerupTimestamp;
  public volatile Long lastPowerupMissedTimestamp;

  public volatile EnemySpeed enemySpeed;
  public volatile Closeness enemyCloseness;

  public volatile float playerFireHitRatio;

  public volatile int shotsFiredLastThreeSeconds;
  public volatile PlayerWeapon playerWeapon;
  public volatile PositionX playerPositionX;
  public volatile PositionX enemyPositionXOnScreen;

  public volatile boolean playerDead;
  public volatile boolean playerInvincible;
  public volatile boolean inBetweenRounds;
  public volatile Closeness enemyBulletFlyingTowardsPlayerDistance;
  public volatile boolean playerIsUnderEnemyFormation;
  public volatile boolean playerIsMoving;
  public volatile boolean isVictory;
  public volatile boolean isDefeat;

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
    event.playerFireHitRatio = this.playerFireHitRatio;
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
    event.enemyBulletFlyingTowardsPlayerDistance = this.enemyBulletFlyingTowardsPlayerDistance;
    event.playerIsUnderEnemyFormation = this.playerIsUnderEnemyFormation;
    event.playerIsMoving = this.playerIsMoving;
    event.isVictory = this.isVictory;
    event.isDefeat = this.isDefeat;
  }
}
