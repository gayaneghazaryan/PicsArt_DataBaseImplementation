package services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileService {

    public static void createFile(String path) {
        File file = new File(path);
        try{
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeInFile(String path, String text) {
        try {
            Files.write(Paths.get(path),text.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearFile(String path) throws IOException {
        Files.newBufferedWriter(Paths.get(path),StandardOpenOption.TRUNCATE_EXISTING);
    }



    public static List<String> readFromFile(String path) throws IOException {
        return Files.readAllLines(Paths.get(path));

    }


}
