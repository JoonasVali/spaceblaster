package com.github.joonasvali.spaceblaster.event;

import java.io.IOException;

public interface NamedImageWriter<T> {
  /**
   * Writes a PNG-encoded image with a given name.
   *
   * @param name the unique name for the image
   * @param data
   * @throws IOException if an I/O error occurs during writing
   */
  void writeImage(String name, T data) throws IOException, InterruptedException;
}