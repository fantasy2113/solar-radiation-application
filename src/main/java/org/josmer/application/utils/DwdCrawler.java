package org.josmer.application.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DwdCrawler {
    private final String targetFile;
    private final String targetUrl;
    private final String targetDir;

    public DwdCrawler(final String targetFile) {
        this.targetFile = targetFile;
        this.targetUrl = "https://opendata.dwd.de/climate_environment/CDC/grids_germany/monthly/radiation_global/";
        this.targetDir = "temp/";
    }

    public void download() {

        try {
            URL url = new URL(targetUrl + targetFile);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            inputStream.transferTo(new FileOutputStream(targetDir + targetFile));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void unzip() {
        try {
            File destDir = new File(targetDir);
            byte[] buffer = new byte[1024];
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(targetDir + targetFile));
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                File file = new File(destDir, zipEntry.getName());
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                int len;
                while ((len = zipInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, len);
                }
                fileOutputStream.close();
                zipEntry = zipInputStream.getNextEntry();
            }
            zipInputStream.closeEntry();
            zipInputStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void save() {
    }

    public void delete() {
        File file = new File(targetDir + targetFile);
        if (file.delete()) {
            System.out.println("File deleted");
        }
        file = new File(targetDir + targetFile.replace(".zip", ".asc"));
        if (file.delete()) {
            System.out.println("File deleted");
        }
    }
}
