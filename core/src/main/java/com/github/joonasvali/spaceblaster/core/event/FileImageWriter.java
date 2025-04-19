package com.github.joonasvali.spaceblaster.core.event;

import com.github.joonasvali.spaceblaster.event.NamedImageWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileImageWriter implements NamedImageWriter {
  private final Logger log = LoggerFactory.getLogger(FileImageWriter.class);
  Path folder;
  FileImageWriter(Path folder) {
    this.folder = folder;
  }

  @Override
  public void writeImage(String name, byte[] pngData) throws IOException {
    // Create a Path instance for the file
    Path filePath = folder.resolve(name);

    // Write the byte array to the file on disk
    Files.write(filePath, pngData);

    log.info("Image " + name + " written to disk with " + pngData.length + " bytes.");
  }
}
