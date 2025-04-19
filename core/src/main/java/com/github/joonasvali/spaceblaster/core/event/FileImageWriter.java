package com.github.joonasvali.spaceblaster.core.event;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.files.FileHandle; // added import
import com.github.joonasvali.spaceblaster.event.NamedImageWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileImageWriter implements NamedImageWriter<Pixmap> {
  private final Logger log = LoggerFactory.getLogger(FileImageWriter.class);
  Path folder;
  FileImageWriter(Path folder) {
    this.folder = folder;
  }

  @Override
  public void writeImage(String name, Pixmap data) throws IOException {
    String fileName = name + ".png";
    // Create a Path instance for the file
    Path filePath = folder.resolve(fileName);
    // Ensure the directory exists
    Files.createDirectories(folder);
    // Create a File and wrap it in a FileHandle
    java.io.File file = filePath.toFile();
    FileHandle fileHandle = new FileHandle(file);
    // Write the Pixmap data to file as PNG using FileHandle
    PixmapIO.writePNG(fileHandle, data);
    log.info("Image " + name + " written to disk.");
  }
}
