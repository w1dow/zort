package project.backend;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Main_back {
    public static List<test.fcmbding> loadDatabase() {
        List<test.fcmbding> db = new ArrayList<>();
        try {
            File file = new File("db/db.json");
            if (file.exists()) {
                Gson gson = new Gson();
                try (Reader reader = new FileReader(file)) {
                    db = gson.fromJson(reader, new TypeToken<List<test.fcmbding>>() {
                    }.getType());
                    if (db == null) {
                        db = new ArrayList<>();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return db;
    }

    static double cosineSimilarity(float[] a, float[] b) {
        if (a.length != b.length)
            throw new IllegalArgumentException("Embedding lengths do not match!");

        double dot = 0.0, normA = 0.0, normB = 0.0;

        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }

        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    // Method to check if input matches any embedding in the database
   public static String findMatchingName(float[] input, List<test.fcmbding> db, double threshold) {
        String matchName = null;
        double bestScore = -1;

        for (test.fcmbding entry : db) {
            double similarity = cosineSimilarity(input, entry.embedding);

            if (similarity > threshold && similarity > bestScore) {
                bestScore = similarity;
                matchName = entry.name;
            }
        }

        return matchName; // null if no match found
    }
}
