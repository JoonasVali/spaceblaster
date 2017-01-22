package ee.joonasvali.spaceshooter.core.game.enemy;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

/**
 * @author Joonas Vali January 2017
 */
public class Enemy extends Rectangle implements Pool.Poolable {

  private int matrixPosX;
  private int matrixPosY;

  public int getMatrixPosX() {
    return matrixPosX;
  }

  public void setMatrixPosX(int matrixPosX) {
    this.matrixPosX = matrixPosX;
  }

  public int getMatrixPosY() {
    return matrixPosY;
  }

  public void setMatrixPosY(int matrixPosY) {
    this.matrixPosY = matrixPosY;
  }

  @Override
  public void reset() {
    this.x = 0;
    this.y = 0;
    this.width = 0;
    this.height = 0;
  }

  public int getBounty() {
    return 100;
  }
}
