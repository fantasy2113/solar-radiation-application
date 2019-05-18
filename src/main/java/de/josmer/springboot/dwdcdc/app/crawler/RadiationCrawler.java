package de.josmer.springboot.dwdcdc.app.crawler;

import de.josmer.springboot.dwdcdc.app.interfaces.IFileReader;
import de.josmer.springboot.dwdcdc.app.interfaces.ISolRad;
import de.josmer.springboot.dwdcdc.app.interfaces.ISolRadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class RadiationCrawler<TSolRad extends ISolRad> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RadiationCrawler.class.getName());
    private final String templateTargetFile;
    private final String targetUrl;
    private final String targetDir;
    private final RadTypes type;
    private final List<ISolRad> radiations;
    private final int month;
    private final int year;
    private final Class<TSolRad> solRadClass;
    private String currentTargetFile;

    public RadiationCrawler(final int month, final int year, final RadTypes type, Class<TSolRad> solRadClass) {
        this.solRadClass = solRadClass;
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

    public void insert(final ISolRadRepository radiationRepository, IFileReader fileReader) {
        try {
            download();
            unzip();
            insertRadiation(radiationRepository, fileReader);
            delete();
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    private void setCurrentTargetFile(final String date) {
        this.currentTargetFile = templateTargetFile.replace("{date}", date);
    }

    private void download() throws Exception {
        LOGGER.info("downloading...");
        setCurrentTargetFile(getDate(year, month));
        URL url = new URL(getUrl());
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();
        inputStream.transferTo(new FileOutputStream(getPathnameZip()));
    }

    private void unzip() throws Exception {
        LOGGER.info("unzip...");
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
    }


    private void insertRadiation(final ISolRadRepository radiationRepository, IFileReader fileReader) throws Exception {
        LOGGER.info("reading...");
        initRadiations(fileReader);
        LOGGER.info("insertRadiation...");
        radiationRepository.save(radiations);
    }

    private void delete() throws Exception {
        LOGGER.info("deleting....");
        Files.delete(Path.of(getPathnameZip()));
        Files.delete(Path.of(getPathnameAsc()));
    }

    private void initRadiations(IFileReader fileReader) throws Exception {
        final String file = fileReader.asString(getPathnameAsc());
        final String[] rows = file.split("\\r\\n");
        int hochwert = 5237500;
        for (int row = rows.length - 1; row >= 28; row--) {
            final String[] columns = rows[row].split(" ");
            int rechtswert = 3280500;
            for (String column : columns) {
                ISolRad radiation = solRadClass.getDeclaredConstructor().newInstance();
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
