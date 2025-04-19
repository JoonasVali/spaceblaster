package com.github.joonasvali.spaceblaster.event;

import java.io.IOException;

public interface NamedImageWriter {
  /**
   * Writes a PNG-encoded image with a given name.
   *
   * @param name the unique name for the image
   * @param pngData the PNG-encoded image data as a byte array
   * @throws IOException if an I/O error occurs during writing
   */
  void writeImage(String name, byte[] pngData) throws IOException;
}