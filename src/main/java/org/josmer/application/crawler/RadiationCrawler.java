package org.josmer.application.crawler;

import org.josmer.application.entities.Radiation;
import org.josmer.application.enums.RadiationTypes;
import org.josmer.application.geo.GeoPotsdamDatum;
import org.josmer.application.interfaces.IRadiationRepository;
import org.josmer.application.utils.Toolbox;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class RadiationCrawler {

    private final String templateTargetFile;
    private final String targetUrl;
    private final String targetDir;
    private final RadiationTypes type;
    private final List<Radiation> radiations;
    private final int month;
    private final int year;
    private String currentTargetFile;

    public RadiationCrawler(final int month, final int year, final RadiationTypes type) {
        this.templateTargetFile = "grids_germany_monthly_radiation_{radiation}_{date}.zip"
                .replace("{radiation}", type.name().toLowerCase(Locale.ENGLISH));
        this.targetUrl = "ftp://ftp-cdc.dwd.de/pub/CDC/grids_germany/monthly/radiation_{radiation}/"
                .replace("{radiation}", type.name().toLowerCase(Locale.ENGLISH));
        this.targetDir = "temp/";
        this.radiations = new LinkedList<>();
        this.month = month;
        this.year = year;
        this.type = type;
    }

    private void setCurrentTargetFile(final String date) {
        this.currentTargetFile = templateTargetFile.replace("{date}", date);
    }

    public void download() {
        try {
            System.out.println("downloading...");
            setCurrentTargetFile(getDate(year, month));
            URL url = new URL(getUrl());
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            inputStream.transferTo(new FileOutputStream(getPathnameZip()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void unzip() {
        try {
            System.out.println("unzip...");
            File destDir = new File(targetDir);
            byte[] buffer = new byte[1024];
            try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(getPathnameZip()))) {
                ZipEntry zipEntry = zipInputStream.getNextEntry();
                while (zipEntry != null) {
                    File file = new File(destDir, zipEntry.getName());
                    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                        int len;
                        while ((len = zipInputStream.read(buffer)) > 0) {
                            fileOutputStream.write(buffer, 0, len);
                        }
                    }
                    zipEntry = zipInputStream.getNextEntry();
                }
                zipInputStream.closeEntry();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(final IRadiationRepository radiationRepository) {
        inserting(radiationRepository);
    }

    public void insert(final String databaseUrl, final IRadiationRepository radiationRepository) {
        inserting(radiationRepository);
    }

    private void inserting(final IRadiationRepository radiationRepository) {
        System.out.println("reading...");
        initRadiations();
        System.out.println("inserting...");
        radiationRepository.save(radiations);
    }

    public void delete() {
        System.out.println("deleting....");
        if (!new File(getPathnameZip()).delete()) {
            System.out.println(getPathnameZip() + " fail");
        }
        if (!new File(getPathnameAsc()).delete()) {
            System.out.println(getPathnameAsc() + " fail");
        }
    }

    private void initRadiations() {
        final String file = Toolbox.readFile(getPathnameAsc());
        final String[] rows = file.split("\\r\\n");
        GeoPotsdamDatum geoPotsdamDatum = new GeoPotsdamDatum();
        int hochwert = 5237500;
        for (int row = rows.length - 1; row >= 28; row--) {
            final String[] columns = rows[row].split(" ");
            int rechtswert = 3280500;

            boolean isAdd7 = true;
            boolean isAdd10 = true;
            boolean isAdd13 = true;
            boolean isAdd15 = true;

            for (String column : columns) {
                geoPotsdamDatum.gkToGeo(rechtswert, hochwert);

                /*if (geoPotsdamDatum.getLonGeo() >= 7.5 && isAdd7) {
                    rechtswert += 500000;
                    isAdd7 = false;
                }*/

                if (geoPotsdamDatum.getLonGeo() >= 10.5 && isAdd10) {
                    rechtswert += 800000;
                    isAdd10 = false;
                }

                if (geoPotsdamDatum.getLonGeo() >= 13.5 && isAdd13) {
                    rechtswert += 800000;
                    isAdd13 = false;
                }

                if (geoPotsdamDatum.getLonGeo() >= 15 && isAdd15) {
                    rechtswert += 800000;
                    isAdd15 = false;
                }

                Radiation radiation = new Radiation();
                radiation.setRadiationValue(Float.parseFloat(column));
                radiation.setRadiationType(type.name());
                radiation.setRadiationDate(Integer.valueOf(getDate(year, month)));
                radiation.setGkhMin(hochwert);
                radiation.setGkhMax(hochwert + 1000);
                radiation.setGkrMin(rechtswert);
                radiation.setGkrMax(rechtswert + 1000);
                radiations.add(radiation);
                rechtswert += 1000;
            }
            hochwert += 1000;
        }
        //Collections.reverse(radiations);
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

    private String getUrl() {
        return targetUrl + currentTargetFile;
    }

    private String getPathnameZip() {
        return targetDir + currentTargetFile;
    }

    private String getPathnameAsc() {
        return targetDir + currentTargetFile.replace(".zip", ".asc");
    }
}
