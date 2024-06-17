package com.github.joonasvali.spaceshooter.core.game.level;

import com.github.joonasvali.spaceblaster.core.game.level.LevelDescriptor;
import com.github.joonasvali.spaceblaster.core.game.level.LevelReader;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Joonas Vali March 2017
 */
public class LevelReaderTest {
  @Test
  public void testRead() throws Exception {
    List<String> list = Files.readAllLines(Paths.get(LevelReaderTest.class.getResource("/test.level").toURI()));
    String[] lines = list.toArray(new String[list.size()]);
    LevelReader reader = new LevelReader(lines);
    Assert.assertEquals(true, reader.isValid());
    Assert.assertEquals(4, reader.getNumberOfLevels());
    Assert.assertEquals("space.png", reader.getBackgroundFileName());
    LevelDescriptor descriptor = reader.getLevel(0);
    Assert.assertEquals("EPILOGUE", descriptor.getName());
    Assert.assertEquals(8, descriptor.getWidth());
    Assert.assertEquals(2, descriptor.getHeight());

    descriptor = reader.getLevel(1);
    Assert.assertEquals("LEVEL 1", descriptor.getName());
    Assert.assertEquals(8, descriptor.getWidth());
    Assert.assertEquals(5, descriptor.getHeight());

    descriptor = reader.getLevel(2);
    Assert.assertEquals("LEVEL 2", descriptor.getName());
    Assert.assertEquals(9, descriptor.getWidth());
    Assert.assertEquals(4, descriptor.getHeight());

    descriptor = reader.getLevel(3);
    Assert.assertEquals("LEVEL 3", descriptor.getName());
    Assert.assertEquals(9, descriptor.getWidth());
    Assert.assertEquals(3, descriptor.getHeight());
  }

  @Test
  public void testBadLevelFile1() throws Exception {
    String[] lines = new String[] {
        "background=hi.png",
        " ",
        "LEVEL 1",
        ""
    };
    LevelReader reader = new LevelReader(lines);
    Assert.assertEquals(false, reader.isValid());
  }

  @Test
  public void testBadLevelFile2() throws Exception {
    String[] lines = new String[] {
        "random text",
        "random test ",
        "what ever",
        ""
    };
    LevelReader reader = new LevelReader(lines);
    Assert.assertEquals(false, reader.isValid());
  }

  /**
   * Should fail because level lines are of different size.
   */
  @Test
  public void testBadLevelFile3() throws Exception {
    String[] lines = new String[] {
        "eoafkoefekfopaekfopflopfeplkfopefkef",
        "efe",
        "ge",
        "g",
        "e",
        "geg",
        "e",
        "ger",
        "g",
        "egpelgpeklgpeglepglkpeglpglplgpelge",
        "plepe"
    };
    LevelReader reader = new LevelReader(lines);
    Assert.assertEquals(false, reader.isValid());
  }

  @Test
  public void testMinimalLevelFile() throws Exception {
    String[] lines = new String[] {
        "LEVEL 1",
        "a"
    };
    LevelReader reader = new LevelReader(lines);
    Assert.assertEquals(true, reader.isValid());
    Assert.assertEquals(1, reader.getNumberOfLevels());
  }
}
