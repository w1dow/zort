package project.backend;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class test {

    public static class fcmbding {
        String name;
        float[] embedding;

        fcmbding(String name, float[] embedding) {
            this.name = name;
            this.embedding = embedding;
        }
    }

    public void save(String name, float[] embedding) {
        try {
            File file = new File("db/db.json");
            Gson gson = new Gson();
            List<fcmbding> db;

            // Load existing DB if file exists
            if (file.exists()) {
                try (Reader reader = new FileReader(file)) {
                    db = gson.fromJson(reader, new TypeToken<List<fcmbding>>() {
                    }.getType());
                    if (db == null)
                        db = new ArrayList<>();
                }
            } else {
                db = new ArrayList<>();
            }

            // Check for duplicate embeddings
            boolean exists = false;
            for (fcmbding entry : db) {
                if (Arrays.equals(entry.embedding, embedding)) {
                    System.out.println("Duplicate embedding already exists for: " + entry.name);
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                db.add(new fcmbding(name, embedding));
                try (Writer writer = new FileWriter(file)) {
                    gson.toJson(db, writer);
                }
                System.out.println("Saved embedding for: " + name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
