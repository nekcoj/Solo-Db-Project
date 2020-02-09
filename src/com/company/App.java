package com.company;

import java.util.List;

class App {

    App() throws IllegalAccessException {
        Database.getInstance().create("MusicDB");
        List<String> jsonFiles = List.of("assets/artists.json", "assets/albums.json", "assets/songs.json");
        Database.getInstance().loadJsonFiles(jsonFiles);
    }
}
    
