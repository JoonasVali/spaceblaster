package com.github.joonasvali.spaceblaster.core;

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
  private static final String PRICEDOWN_PATH = "fonts/pricedown-bl.otf";
  private static final String ETHNOCENTRIC_PATH = "fonts/ethnocentric-rg.ttf";

  public BitmapFont createTitlefont() {
    return createEthnocentricShadowed(Color.YELLOW, 50, 1, 3, 3);
  }

  public BitmapFont createNormalfont() {
    return createEthnocentricShadowed(Color.YELLOW, 18, 1, 1, 1);
  }

  public BitmapFont createMenufont() {
    return createPricedown(Color.WHITE, 35, 5, 0, 0);
  }

  public BitmapFont createFont12(Color color) {
    return createEthnocentric(color, 12, 1);
  }

  public BitmapFont createFont20(Color color) {
    return createEthnocentric(color, 20, 1);
  }

  private BitmapFont createEthnocentric(Color color, int size, int spaceX) {
    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(ETHNOCENTRIC_PATH));
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    parameter.color = color;
    parameter.size = size;
    parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
    parameter.spaceX = spaceX;
    BitmapFont font = generator.generateFont(parameter);
    generator.dispose();
    return font;
  }

  private BitmapFont createEthnocentricShadowed(Color color, int size, int spaceX, int offsetX, int offsetY) {
    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(ETHNOCENTRIC_PATH));
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

  private BitmapFont createPricedown(Color color, int size, int spaceX, int offsetX, int offsetY) {
    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(PRICEDOWN_PATH));
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    parameter.shadowColor = Color.BLACK;
    parameter.color = color;
    parameter.size = size;
    parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
    parameter.spaceX = spaceX;
    BitmapFont font = generator.generateFont(parameter);
    generator.dispose();
    return font;
  }

  public BitmapFont createCreditsFont() {
    return createEthnocentricShadowed(Color.WHITE, 24, 1, 1, 1);
  }
  public BitmapFont createCreditsFontSmall() {
    return createEthnocentricShadowed(Color.WHITE, 12, 1, 1, 1);
  }

  public BitmapFont createFont10(Color color) {
    return createEthnocentric(color, 10, 1);
  }
}
