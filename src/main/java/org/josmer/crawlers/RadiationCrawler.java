package org.josmer.crawlers;

import org.josmer.entities.Radiation;
import org.josmer.repositories.RadiationRepository;
import org.josmer.utils.Toolbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class RadiationCrawler {
    private final String templateTargetFile;
    private final String targetUrl;
    private final String targetDir;
    private final String typ;
    private final int month;
    private final int year;
    private String currentTargetFile;
    private List<Radiation> radiations;

    public RadiationCrawler(final int month, final int year, final String typ) {
        this.templateTargetFile = "grids_germany_monthly_radiation_global_{date}.zip";
        this.targetUrl = "https://opendata.dwd.de/climate_environment/CDC/grids_germany/monthly/radiation_global/";
        this.targetDir = "temp/";
        this.radiations = new LinkedList<>();
        this.month = month;
        this.year = year;
        this.typ = typ;
    }

    private void setCurrentTargetFile(final String date) {
        this.currentTargetFile = templateTargetFile.replace("{date}", date);
    }

    public void download() throws Exception {
        setCurrentTargetFile(getDate(year, month));
        URL url = new URL(targetUrl + currentTargetFile);
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();
        inputStream.transferTo(new FileOutputStream(getPathnameZip()));
    }

    public void unzip() throws Exception {
        File destDir = new File(targetDir);
        byte[] buffer = new byte[1024];
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(getPathnameZip()));
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


    public void insert() throws Exception {
        initRadiations();
        int newRows = new RadiationRepository().saveAll(radiations);
    }


    public void delete() throws Exception {
        if (new File(getPathnameZip()).delete()) {
            System.out.println(getPathnameZip() + " => deleted");
        } else {
            throw new Exception("fail");
        }
        if (new File(getPathnameAsc()).delete()) {
            System.out.println(getPathnameAsc() + " => deleted");
        } else {
            throw new Exception("fail");
        }
    }


    private void initRadiations() {
        final String file = Toolbox.readFile(getPathnameAsc());
        final String[] rows = file.split("\\r\\n");
        int y = 5237500;
        for (int row = rows.length - 1; row >= 28; row--) {
            final String[] columns = rows[row].split(" ");
            int x = 3280500;
            Radiation radiation = new Radiation();
            for (int column = 0; column < columns.length; column++) {
                radiation.setRadiation(Double.valueOf(columns[column]));
                radiation.setTyp(typ);
                radiation.setMonth(month);
                radiation.setYear(year);
                radiation.setyMin(y);
                radiation.setyMax(y + 1000);
                radiation.setxMin(x);
                radiation.setxMax(x + 1000);
            }
            y += 1000;
            radiations.add(radiation);
        }
        Collections.reverse(radiations);
    }


    private String getDate(final Integer year, final Integer month) {
        StringBuilder date = new StringBuilder();
        date.append(year);
        if (month.toString().length() == 2) {
            date.append(month);
        } else {
            date.append(0);
            date.append(month);
        }
        return date.toString();
    }

    private String getPathnameZip() {
        return targetDir + currentTargetFile;
    }

    private String getPathnameAsc() {
        return targetDir + currentTargetFile.replace(".zip", ".asc");
    }
}
