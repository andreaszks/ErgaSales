package io.github.andreaszks.ergasales.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/** Contains methods that handle the Import and Export of different data on files. */
public abstract class FileTools {

  /**
   * Writes data to specified file.
   *
   * @param file
   * @param data
   * @return true if data is written, false if not
   */
  protected static boolean writeDataToFile(File file, String data) {
    try (FileOutputStream fis = new FileOutputStream(file)) {
      fis.write(data.getBytes());
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  protected FileTools() {}
}
