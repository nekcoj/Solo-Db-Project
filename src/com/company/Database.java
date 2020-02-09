package com.company;

import com.github.cliftonlabs.json_simple.*;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class Database {

    private String dbName;
    private static final Database INSTANCE = new Database();

    public static Database getInstance() {
        return Database.INSTANCE;
    }

    public void create(String dbName) throws IllegalAccessException {
        this.dbName = dbName;
        createDir(dbName);
    }

    public void loadJsonFile(String filePath) throws IllegalAccessException {
        createSubdirsFromJSON(List.of(filePath), dbName);
    }

    public void loadJsonFiles(List<String> filePaths) throws IllegalAccessException {
        createSubdirsFromJSON(filePaths, dbName);
    }

    private void createSubdirsFromJSON(List<String> pathNames, String dbDir) throws IllegalAccessException {
        for (String path : pathNames) {
            if (!exists(path)) continue;
            String[] split = path.split("\\.");

            List<String> pathList;
            pathList = Arrays.asList(split);

            String[] dbPathSplit = pathList.get(0).split("/");

            List<String> getFilePath = Arrays.asList(dbPathSplit);
            String filePath = dbDir + "/" + getFilePath.get(getFilePath.size() - 1);

            if (!exists(filePath)) {
                createDir(filePath);
                createFileFromJSON(path, filePath);
                System.out.printf("Loaded data from JSON file '%s'\n", path);
            }
        }
    }

    private void createFileFromJSON(String filePath, String dirPath) {
        try (FileReader fileReader = new FileReader(filePath)) {
            JsonArray ja = (JsonArray) Jsoner.deserialize(fileReader);

            for (Object ob : ja) {
                JsonObject temp = (JsonObject) ob;
                String fileName = dirPath + "/" + temp.get("id").toString();

                StringBuilder sb = new StringBuilder();
                for (var entry : temp.entrySet())
                    sb.append(String.format("%s=%s\n", entry.getKey(), entry.getValue()));

                createFile(fileName, sb.toString());
            }
        } catch (IOException | JsonException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /*writeFile using reflection*/
    private void createFile(String dirPath, String data) throws IllegalAccessException{
        try{
            Method method = FileSystem.class.getDeclaredMethod("writeFile", String.class, String.class);
            method.setAccessible(true);
            method.invoke(null, dirPath, data);
        } catch (NoSuchMethodException | SecurityException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void createDir(String dirPath) throws IllegalAccessException{
        try{
            Method method = FileSystem.class.getDeclaredMethod("createDir", String.class);
            method.setAccessible(true);
            method.invoke(null, dirPath);
        } catch (NoSuchMethodException | SecurityException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private boolean exists(String filePath){
        try{
            Method method = FileSystem.class.getDeclaredMethod("exists", String.class);
            method.setAccessible(true);
            return (Boolean) method.invoke(null, filePath);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return true;
    }

    public String getDbName() {
        return dbName;
    }
}
