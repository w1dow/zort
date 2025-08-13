package project.frontend;
// ** Handles main front-end

import java.io.File;

public class Main_front {
    public static String foldername;

    public static void main(String[] args) {
        File files[] = new File("E:\\Zort\\zort\\images").listFiles();
        for(File file:files){
            fh.rnm(file.getAbsolutePath(), "img");
        }
    }

}