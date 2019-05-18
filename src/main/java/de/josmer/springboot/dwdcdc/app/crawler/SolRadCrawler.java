package de.josmer.springboot.dwdcdc.app.crawler;

import de.josmer.springboot.dwdcdc.app.entities.SolRad;
import de.josmer.springboot.dwdcdc.app.enums.SolRadTypes;
import de.josmer.springboot.dwdcdc.app.interfaces.IFileReader;
import de.josmer.springboot.dwdcdc.app.interfaces.ISolRadCrawler;
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

public final class SolRadCrawler implements ISolRadCrawler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SolRadCrawler.class.getName());
    private String templateTargetFile;
    private String targetUrl;
    private String targetDir;
    private SolRadTypes type;
    private List<SolRad> radiations;
    private int month;
    private int year;
    private String currentTargetFile;

    @Override
    public void insert(ISolRadRepository solRadRepository, IFileReader fileReader, int month, int year, SolRadTypes type) {
        init(month, year, type);
        download();
        unzip();
        insertRadiation(solRadRepository, fileReader);
        delete();
    }

    private void init(int month, int year, SolRadTypes type) {
        try {
            this.templateTargetFile = "grids_germany_monthly_radiation_{radiation}_{date}.zip"
                    .replace("{radiation}", type.name().toLowerCase(Locale.ENGLISH));
            this.targetUrl = "ftp://ftp-cdc.dwd.de/pub/CDC/grids_germany/monthly/radiation_{radiation}/"
                    .replace("{radiation}", type.name().toLowerCase(Locale.ENGLISH));
            this.targetDir = "temp/";
            this.radiations = new LinkedList<>();
            this.month = month;
            this.year = year;
            this.type = type;
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    private void setCurrentTargetFile(final String date) {
        this.currentTargetFile = templateTargetFile.replace("{date}", date);
    }

    private void download() {
        try {
            LOGGER.info("downloading...");
            setCurrentTargetFile(getDate(year, month));
            URL url = new URL(getUrl());
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            inputStream.transferTo(new FileOutputStream(getPathnameZip()));
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    private void unzip() {
        try {
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
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }


    private void insertRadiation(ISolRadRepository solRadRepository, IFileReader fileReader) {
        try {
            LOGGER.info("reading...");
            initRadiations(fileReader);
            LOGGER.info("insertRadiation...");
            solRadRepository.save(radiations);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    private void delete() {
        try {
            LOGGER.info("deleting....");
            Files.delete(Path.of(getPathnameZip()));
            Files.delete(Path.of(getPathnameAsc()));
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    private void initRadiations(IFileReader fileReader) {
        try {
            final String file = fileReader.asString(getPathnameAsc());
            final String[] rows = file.split("\\r\\n");
            int gkh = 5237500;
            for (int row = rows.length - 1; row >= 28; row--) {
                final String[] columns = rows[row].split(" ");
                int gkr = 3280500;
                for (String column : columns) {
                    radiations.add(initSolRad(gkh, gkr, column));
                    gkr += 1000;
                }
                gkh += 1000;
            }
            Collections.reverse(radiations);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    private SolRad initSolRad(int gkh, int gkr, String column) {
        SolRad radiation = new SolRad();
        radiation.setRadiationValue(Float.parseFloat(column));
        radiation.setRadiationType(type.name());
        radiation.setRadiationDate(Integer.valueOf(getDate(year, month)));
        radiation.setGkhMin(gkh);
        radiation.setGkhMax(gkh + 1000);
        radiation.setGkrMin(gkr);
        radiation.setGkrMax(gkr + 1000);
        return radiation;
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
