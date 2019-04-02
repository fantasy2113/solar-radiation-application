package org.josmer.crawlers;

import org.josmer.connector.RadiationConnector;
import org.josmer.interfaces.ICrawler;
import org.josmer.utils.GridReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class RadiationCrawler implements ICrawler {
    private final String templateTargetFile;
    private final String targetUrl;
    private final String targetDir;
    private String currentTargetFile;

    public RadiationCrawler() {
        this.templateTargetFile = "grids_germany_monthly_radiation_global_{date}.zip";
        this.targetUrl = "https://opendata.dwd.de/climate_environment/CDC/grids_germany/monthly/radiation_global/";
        this.targetDir = "temp/";
    }

    private void setCurrentTargetFile(final String date) {
        this.currentTargetFile = templateTargetFile.replace("{date}", date);
    }

    @Override
    public void download(final String date) throws Exception {
        setCurrentTargetFile(date);
        URL url = new URL(targetUrl + currentTargetFile);
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();
        inputStream.transferTo(new FileOutputStream(targetDir + currentTargetFile));
    }

    @Override
    public void unzip() throws Exception {
        File destDir = new File(targetDir);
        byte[] buffer = new byte[1024];
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(targetDir + currentTargetFile));
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
    }

    @Override
    public void insert() throws Exception {
        RadiationConnector radiationConnector = new RadiationConnector();
        GridReader gridReader = new GridReader("\\r\\n", " ", 28);
        LinkedList<LinkedList<String>> grid = gridReader.get(targetDir + currentTargetFile.replace(".zip", ".asc"));

        radiationConnector.saveAll(new ArrayList<>());
    }

    @Override
    public void delete() throws Exception {
        if (new File(targetDir + currentTargetFile).delete()) {
            System.out.println(targetDir + currentTargetFile + " => deleted");
        } else {
            throw new Exception("fail");
        }
        if (new File(targetDir + currentTargetFile.replace(".zip", ".asc")).delete()) {
            System.out.println(targetDir + currentTargetFile.replace(".zip", ".asc") + " => deleted");
        } else {
            throw new Exception("fail");
        }
    }
}
