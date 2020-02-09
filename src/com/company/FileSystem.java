package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileSystem {

    private static boolean delete(String filePath) {
        try {
            return Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String readFile(String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void writeFile(String fileName, String data) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
            bufferedWriter.write(data);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean exists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    private static boolean createDir(String dirName) {
        return new File(dirName).mkdir();
    }

    private static File[] getDirFiles(String dirName) {
        try (Stream<Path> paths = Files.walk(Paths.get(dirName))) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .toArray(File[]::new);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static File[] getSubFolders(String databasePath){
        return new File(databasePath).listFiles(File::isDirectory);
    }
}
