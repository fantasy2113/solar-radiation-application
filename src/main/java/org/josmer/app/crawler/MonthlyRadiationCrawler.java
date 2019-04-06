package org.josmer.app.crawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.josmer.app.core.RadiationTypes;
import org.josmer.app.entity.Radiation;
import org.josmer.app.logic.utils.Toolbox;
import org.josmer.app.repository.MonthlyRadiationRepository;

public final class MonthlyRadiationCrawler {

    private final String templateTargetFile;
    private final String targetUrl;
    private final String targetDir;
    private final RadiationTypes type;
    private final int month;
    private final int year;
    private String currentTargetFile;
    private List<Radiation> radiations;

    public MonthlyRadiationCrawler(final int month, final int year, final RadiationTypes type) {
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
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void unzip() {
        try {
            System.out.println("unzip...");
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
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void insert() {
        MonthlyRadiationRepository radiationRepository = new MonthlyRadiationRepository();
        inserting(radiationRepository);
    }

    public void insert(final String databaseUrl) {
        MonthlyRadiationRepository radiationRepository = new MonthlyRadiationRepository(databaseUrl);
        inserting(radiationRepository);
    }

    private void inserting(final MonthlyRadiationRepository radiationRepository) {
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
        int hochwert = 5237500;
        for (int row = rows.length - 1; row >= 28; row--) {
            final String[] columns = rows[row].split(" ");
            int rechtswert = 3280500;
            for (int column = 0; column < columns.length; column++) {
                Radiation radiation = new Radiation();
                radiation.setRadiationvalue(Float.valueOf(columns[column]));
                radiation.setRadiationType(type.name());
                radiation.setRadiationDate(Integer.valueOf(getDate(year, month)));
                radiation.setGkhMin(hochwert);
                radiation.setGkhMax(hochwert + 1000);
                radiation.setGkrMin(rechtswert);
                radiation.setGkrMax(rechtswert + 1000);
                radiations.add(radiation);
                rechtswert += 4000;
            }
            hochwert += 1000;
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
