package pl.futurecollars.invoicing.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class IdService {

  private final Path idFilepath;

  private int nextId = 1;

  public IdService(Path idFilepath) {
    this.idFilepath = idFilepath;
    try {
      List<String> lines = FileService.readLinesFromFile(idFilepath);
      if (lines.isEmpty()) {
        FileService.writeToFile(idFilepath, "1");
      } else {
        nextId = Integer.parseInt(lines.get(0));
      }
    } catch (IOException exception) {
      throw new RuntimeException("Error during idService initialization", exception);
    }
  }

  public int getNextIdAndSaveIncremented() {
    try {
      FileService.writeToFile(idFilepath, String.valueOf(nextId + 1));
      return nextId++;
    } catch (IOException exception) {
      throw new RuntimeException("Failed to return next record id", exception);
    }
  }
}