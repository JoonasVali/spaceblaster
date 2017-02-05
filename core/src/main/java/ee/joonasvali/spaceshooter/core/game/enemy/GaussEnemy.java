package ee.joonasvali.spaceshooter.core.game.enemy;

import ee.joonasvali.spaceshooter.core.game.GameStepListener;
import ee.joonasvali.spaceshooter.core.game.weapons.GaussGunBullet;

/**
 * @author Joonas Vali February 2017
 */
public class GaussEnemy extends Enemy implements GameStepListener {
  public GaussEnemy(int health, int bounty, int x, int y) {
    super(GaussGunBullet.class, health, bounty, x, y);
  }

  @Override
  public void onStepAction() {
    float offsetX = getOffsetX();
    float offsetY = getOffsetY();
    if (offsetX < 0) {
      offsetX += Math.min(-offsetX, 0.1);
    }
    if (offsetX > 0) {
      offsetX -= Math.min(offsetX, 0.1);
    }
    setOffsetX(offsetX);

    if (offsetY < 0) {
      offsetY += Math.min(-offsetY, 0.1);
    }
    if (offsetY > 0) {
      offsetY -= Math.min(offsetY, 0.1);
    }
    setOffsetY(offsetY);
  }

  @Override
  public void onFire() {
    float yOffset = getOffsetY();
    setOffsetY(yOffset + 1);
  }
}
