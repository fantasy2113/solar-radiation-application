package org.josmer.application.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileCrawler {
    private final String filePath;
    private final String sampleUrl;

    public FileCrawler(String filePath, String sampleUrl) {
        this.filePath = filePath;
        this.sampleUrl = sampleUrl;
    }

    private void create() {
        File file = new File(filePath);
        try {
            if (file.createNewFile()) {
                System.out.println("File is created!");
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void download() {


        /*URL urlObj = null;
        ReadableByteChannel rbcObj = null;
        FileOutputStream fOutStream = null;

        // Checking If The File Exists At The Specified Location Or Not
        Path filePathObj = Paths.get(filePath);
        boolean fileExists = Files.exists(filePathObj);
        if (fileExists) {
            try {
                urlObj = new URL(sampleUrl);
                rbcObj = Channels.newChannel(urlObj.openStream());
                fOutStream = new FileOutputStream(filePath);

                fOutStream.getChannel().transferFrom(rbcObj, 0, Long.MAX_VALUE);
                System.out.println("! File Successfully Downloaded From The Url !");
            } catch (IOException ioExObj) {
                System.out.println("Problem Occured While Downloading The File= " + ioExObj.getMessage());
            } finally {
                try {
                    if (fOutStream != null) {
                        fOutStream.close();
                    }
                    if (rbcObj != null) {
                        rbcObj.close();
                    }
                } catch (IOException ioExObj) {
                    System.out.println("Problem Occured While Closing The Object= " + ioExObj.getMessage());
                }
            }
        } else {
            System.out.println("File Not Present! Please Check!");
        }*/


        try {
            URL url = new URL(sampleUrl + filePath);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            inputStream.transferTo(new FileOutputStream(filePath));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
