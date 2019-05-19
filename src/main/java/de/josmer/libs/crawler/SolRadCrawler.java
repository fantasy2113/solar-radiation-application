package de.josmer.libs.crawler;

import de.josmer.libs.entities.SolRad;
import de.josmer.libs.enums.SolRadTypes;
import de.josmer.libs.interfaces.IFileReader;
import de.josmer.libs.interfaces.ISolRadCrawler;
import de.josmer.libs.interfaces.ISolRadRepository;
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
    private final String templateTargetFile;
    private final String targetUrl;
    private final String targetDir;
    private final SolRadTypes solRadType;
    private final List<SolRad> radiations;
    private final int month;
    private final int year;
    private String currentTargetFile;

    public SolRadCrawler(SolRadTypes solRadType, int month, int year) {
        this.templateTargetFile = "grids_germany_monthly_radiation_{radiation}_{date}.zip"
                .replace("{radiation}", solRadType.name().toLowerCase(Locale.ENGLISH));
        this.targetUrl = "ftp://ftp-cdc.dwd.de/pub/CDC/grids_germany/monthly/radiation_{radiation}/"
                .replace("{radiation}", solRadType.name().toLowerCase(Locale.ENGLISH));
        this.targetDir = "./temp/";
        this.radiations = new LinkedList<>();
        this.month = month;
        this.year = year;
        this.solRadType = solRadType;
    }

    @Override
    public void insert(ISolRadRepository solRadRepository, IFileReader fileReader) {
        if (solRadRepository.isInTable(Integer.valueOf(getDate(year, month)), solRadType.name())) {
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
            final String[] rows = fileReader.asString(getPathnameAsc()).split("\\r\\n");
            versionGuard(rows[2]);
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

    private void versionGuard(String version) throws Exception {
        if (!version.equals("Datensatz_Version=V003")) {
            throw new Exception("wrong version");
        }
    }

    private SolRad initSolRad(int gkh, int gkr, String column) {
        SolRad radiation = new SolRad();
        radiation.setRadiationValue(Float.parseFloat(column));
        radiation.setRadiationType(solRadType.name());
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
