package com.demo.selenium.utils;

import com.demo.selenium.pages.BasePage;

import java.io.File;
import java.net.URISyntaxException;

public class CheckFile {

    public static boolean isFileDownloaded(String dirPath, String fileName) {

        File dir = new File(dirPath);
        if (!dir.exists() && !dir.isDirectory()) return false;
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return false;
        }

        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    public static void deleteFile(String dirPath, String fileName){
        File dir = new File(dirPath);
        if (!dir.exists() && !dir.isDirectory()){
            System.out.println("Dir does not exist");
            return;
        }
        else {
            File file = new File(dir + File.separator + fileName);
            boolean isFileDeleted = file.delete();
            System.out.println("File deleted "+file+" = "+isFileDeleted);
        }
    }

    public static File getFileFromResources(String path) {
        try {
            return new File(BasePage.class.getResource(path).toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
