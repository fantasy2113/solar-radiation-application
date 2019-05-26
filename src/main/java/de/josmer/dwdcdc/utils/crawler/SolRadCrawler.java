package de.josmer.dwdcdc.utils.crawler;

import de.josmer.dwdcdc.utils.entities.SolRad;
import de.josmer.dwdcdc.utils.enums.SolRadTypes;
import de.josmer.dwdcdc.utils.interfaces.IFileReader;
import de.josmer.dwdcdc.utils.interfaces.ISaveSolRad;
import de.josmer.dwdcdc.utils.interfaces.ISolRadCrawler;
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
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class SolRadCrawler implements ISolRadCrawler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SolRadCrawler.class.getName());
    private final String templateTargetFile;
    private final String targetUrl;
    private final String targetDir;
    private final SolRadTypes solRadType;
    private final int month;
    private final int year;
    private String currentTargetFile;

    public SolRadCrawler(SolRadTypes solRadType, int month, int year) {
        this.solRadType = solRadType;
        this.templateTargetFile = "grids_germany_monthly_radiation_{radiation}_{date}.zip"
                .replace("{radiation}", getSolRadType());
        this.targetUrl = "ftp://ftp-cdc.dwd.de/pub/CDC/grids_germany/monthly/radiation_{radiation}/"
                .replace("{radiation}", getSolRadType());
        this.targetDir = "./temp/";
        this.month = month;
        this.year = year;
    }

    @Override
    public void insert(ISaveSolRad solRadRepository, IFileReader fileReader) {
        if (solRadRepository.isAlreadyExist(Integer.valueOf(getDate(year, month)), solRadType)) {
            LOGGER.info("month already exists");
            return;
        }
        download();
        unzip();
        insertRadiation(solRadRepository, fileReader);
        delete();
    }

    private void setCurrentTargetFile(final String date) {
        this.currentTargetFile = templateTargetFile.replace("{date}", date);
    }

    private void download() {
        try {
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


    private void insertRadiation(ISaveSolRad solRadRepository, IFileReader fileReader) {
        try {
            solRadRepository.save(getSolRads(fileReader));
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    private void delete() {
        try {
            Files.delete(Path.of(getPathnameZip()));
            Files.delete(Path.of(getPathnameAsc()));
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    private LinkedList<SolRad> getSolRads(IFileReader fileReader) {
        LinkedList<SolRad> solRads = new LinkedList<>();
        try {
            final String[] rows = getColumns(fileReader.asString(getPathnameAsc()), "\\r\\n");
            rightVersionGuard(rows[2]);
            int gkh = 5237500;
            for (int rowIndex = getLastRowIndex(rows); rowIndex >= 28; rowIndex--) {
                int gkr = 3280500;
                for (String column : getColumns(rows[rowIndex], " ")) {
                    solRads.add(initSolRad(gkh, gkr, column));
                    gkr = increment(gkr);
                }
                gkh = increment(gkh);
            }
            Collections.reverse(solRads);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return solRads;
    }

    private int increment(int gk) {
        return gk + 1000;
    }

    private String[] getColumns(String row, String s) {
        return row.split(s);
    }

    private int getLastRowIndex(String[] rows) {
        return rows.length - 1;
    }

    private void rightVersionGuard(String version) {
        if (!isRightVersion(version)) {
            throw new IllegalArgumentException("wrong version");
        }
    }

    private boolean isRightVersion(String version) {
        return version.equals("Datensatz_Version=V003") || version.equals("Datensatz_Version=V0.3");
    }

    private SolRad initSolRad(int gkh, int gkr, String column) {
        SolRad solRad = new SolRad();
        solRad.setRadiationValue(Float.parseFloat(column));
        solRad.setRadiationType(solRadType.name());
        solRad.setRadiationDate(Integer.valueOf(getDate(year, month)));
        solRad.setGkhMin(gkh);
        solRad.setGkhMax(gkh + 1000);
        solRad.setGkrMin(gkr);
        solRad.setGkrMax(gkr + 1000);
        return solRad;
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


    private String getSolRadType() {
        return this.solRadType.name().toLowerCase(Locale.ENGLISH);
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