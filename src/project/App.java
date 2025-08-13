package project;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import project.backend.*;
import project.frontend.*;

public class App {
    public static String foldername = "";
    public static int filecount = 0;
    public static File[] files = {};
    public static String person_name = "";

    public static CompletableFuture<Integer> chck(int res) {
        int result = 999;
        if (res == 0) {
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
            }
        }
        return CompletableFuture.completedFuture(result);
    }

    public static CompletableFuture<Object[]> process(File file, testmodel model) {
        String fname = file.getAbsolutePath();
        Mat image = Imgcodecs.imread(fname);
        float[] mbd = model.getEmbedding(model.getfloat(model.pp(image)));
        List<test.fcmbding> database = Main_back.loadDatabase();
        double threshold = 0.6;
        String name = Main_back.findMatchingName(mbd, database, threshold);
        int result = (name != null) ? 1 : 0;
        return CompletableFuture.completedFuture(new Object[] { result, mbd, name });
    }

    public static CompletableFuture<Void> test_file(File[] files, testmodel model, int index) {
        if (index >= files.length) {
            return CompletableFuture.completedFuture(null);
        }

        File file = files[index];
        return process(file, model).thenCompose(arr -> {
            int res = (int) arr[0];
            float[] mbd = (float[]) arr[1];
            String nm = (String) arr[2];
            if (res == 0) {
                return pop.nmface(file.getAbsolutePath()).thenCompose(person_name -> {
                    System.out.println("Got name for " + file.getName() + ": " + person_name);
                    test jsonmbd = new test();
                    jsonmbd.save(person_name, mbd);
                    return test_file(files, model, index + 1);
                });
            } else {
                fh.rnm(file.getAbsolutePath(), nm);
                return test_file(files, model, index + 1);
            }
        });
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        testmodel model = new testmodel("models/facenet.pb");

        frontend.prepare().thenAccept(fold -> {
            foldername = fold;
            test_file(files, model, 0)
                    .thenRun(() -> System.out.println("All files processed."));
        });
    }
}