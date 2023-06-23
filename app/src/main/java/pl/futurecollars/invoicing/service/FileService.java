package pl.futurecollars.invoicing.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileService {

  public static void appendToFile(Path path, String line) throws IOException {
    Files.writeString(path, line + '\n', StandardOpenOption.APPEND);
  }

  public static void writeToFile(Path path, String line) throws IOException {
    Files.writeString(path, line, StandardOpenOption.TRUNCATE_EXISTING);
  }

  public static void writeLinesToFile(Path path, List<String> lines) throws IOException {
    Files.write(path, lines, StandardOpenOption.TRUNCATE_EXISTING);
  }

  public static List<String> readLinesFromFile(Path path) throws IOException {
    return Files.readAllLines(path);
  }

}
