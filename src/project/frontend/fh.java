// Handles renaming the file and moving it to a folder
package project.frontend;

import java.io.*;

import project.App;

public class fh {

    public static void rnm(String fpath, String fname) {
        File s = new File(fpath);
        File folder = new File(s.getParentFile(), fname);

        if (!folder.exists()) {
            // if there  is no folder with name fname
            folder.mkdir();
            System.out.println("Created folder: " + folder.getAbsolutePath());
        }

        File[] mFiles = folder.listFiles(f -> f.getName().startsWith(fname)&& !f.isDirectory());
        int count = (mFiles == null) ? 0 : mFiles.length;

        String extnnsn = "";
        int dot = s.getName().lastIndexOf('.');
        if (dot != -1) {
            extnnsn = s.getName().substring(dot);
        }

        String nwfname = fname + (count + 1) + extnnsn;
        File d = new File(s.getParentFile(), nwfname);

        boolean renamed = s.renameTo(d);
        if (renamed) {
            // System.out.println("Renamed to: " + d.getAbsolutePath());
            move(d.getAbsolutePath(), fname);
        } else {
            // System.out.println("Failed to rename file: " + s.getAbsolutePath());
        }
    }

    public static void move(String filepath, String name) {
        File file = new File(filepath);
        File fold = new File(file.getParentFile(), name);
        if (!fold.exists()) {
            fold.mkdir();
        }
        File destFile = new File(fold, file.getName());

        boolean success = file.renameTo(destFile);
        if (!success) {
            // System.out.println("Failed to move file from " + file.getAbsolutePath() + " to " + destFile.getAbsolutePath());
        }
    }

    public static void loadfiles(String fp) {
        File folder = new File(fp);
        String[] extensions = { ".jpg", ".jpeg", ".png", ".gif", ".bmp" };
        App.files = folder.listFiles(file -> {
            if (!file.exists())
                return false;
            for (String ext : extensions) {
                if (file.getName().toLowerCase().endsWith(ext))
                    return true;
            }
            return false;
        });
        App.filecount = App.files.length;
        System.out.println("Total files: " + App.files.length);
    }

}
