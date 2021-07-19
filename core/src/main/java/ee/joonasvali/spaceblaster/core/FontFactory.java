package ee.joonasvali.spaceblaster.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * @author Joonas Vali February 2017
 */
public class FontFactory {
  private static final String COOLVETICA_PATH = "fonts/coolvetica.ttf";
  private static final String BEBASNEUE_PATH = "fonts/BebasNeue.otf";

  public BitmapFont createTitlefont() {
    return createCoolvetica(Color.YELLOW, 50, 1, 3, 3);
  }

  public BitmapFont createNormalfont() {
    return createCoolvetica(Color.YELLOW, 18, 1, 1, 1);
  }

  public BitmapFont createMenufont() {
    return createCoolvetica(Color.WHITE, 35, 2, 0, 0);
  }

  public BitmapFont createFont12(Color color) {
    return createBebasneue(color, 12, 1);
  }

  public BitmapFont createFont20(Color color) {
    return createBebasneue(color, 20, 1);
  }

  private BitmapFont createBebasneue(Color color, int size, int spaceX) {
    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(BEBASNEUE_PATH));
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    parameter.color = color;
    parameter.size = size;
    parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
    parameter.spaceX = spaceX;
    BitmapFont font = generator.generateFont(parameter);
    generator.dispose();
    return font;
  }

  private BitmapFont createCoolvetica(Color color, int size, int spaceX, int offsetX, int offsetY) {
    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(COOLVETICA_PATH));
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    parameter.shadowColor = Color.BLACK;
    parameter.color = color;
    parameter.size = size;
    parameter.shadowOffsetX = offsetX;
    parameter.shadowOffsetY = offsetY;
    parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
    parameter.spaceX = spaceX;
    BitmapFont font = generator.generateFont(parameter);
    generator.dispose();
    return font;
  }

  public BitmapFont createCreditsFont() {
    return createCoolvetica(Color.WHITE, 24, 1, 1, 1);
  }
  public BitmapFont createCreditsFontSmall() {
    return createCoolvetica(Color.WHITE, 15, 1, 1, 1);
  }
}
