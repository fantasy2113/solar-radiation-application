package de.jos.dwdcdc.library.crawler;

import de.jos.dwdcdc.library.enums.SolRadTypes;
import de.jos.dwdcdc.share.IBasicSolRad;
import de.jos.dwdcdc.share.IDataReader;
import de.jos.dwdcdc.share.ISolRad;
import de.jos.dwdcdc.share.ISolRadCrawler;
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

public final class SolRadCrawler<T extends ISolRad> implements ISolRadCrawler {

  private static final Logger LOGGER = LoggerFactory.getLogger(SolRadCrawler.class.getName());
  private final Class<T> solRadClass;
  private final SolRadTypes solRadType;
  private final String targetDir;
  private final String targetUrl;
  private final String templateTargetFile;

  public SolRadCrawler(SolRadTypes solRadType, Class<T> solRadClass) {
    this.solRadType = solRadType;
    this.templateTargetFile = "grids_germany_monthly_radiation_{radiation}_{date}.zip".replace("{radiation}",
        getSolRadTypeAsString());
    this.targetUrl = "ftp://opendata.dwd.de/climate_environment/CDC/grids_germany/monthly/radiation_{radiation}/"
        .replace("{radiation}", getSolRadTypeAsString());
    this.targetDir = "./temp/";
    this.solRadClass = solRadClass;
  }

  private void delete(String targetFile) {
    if (targetFile == null) {
      return;
    }
    deleteFile(Path.of(getPathnameZip(targetFile)));
    deleteFile(Path.of(getPathnameAsc(targetFile)));
  }

  private void deleteFile(Path file) {
    try {
      Files.delete(file);
    } catch (Exception e) {
      LOGGER.info(e.toString());
    }
  }

  private void download(String targetFile) throws Exception {
    URL url = new URL(getUrl(targetFile));
    URLConnection connection = url.openConnection();
    InputStream inputStream = connection.getInputStream();
    inputStream.transferTo(new FileOutputStream(getPathnameZip(targetFile)));
  }

  private String[] getColumns(String row, String s) {
    return row.split(s);
  }

  private String getCurrentTargetFile(final String date) {
    return templateTargetFile.replace("{date}", date);
  }

  private int getDateAsInt(String dateAsString) {
    return Integer.parseInt(dateAsString);
  }

  private String getDateAsString(final int year, final Integer month) {
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

  private int getLastRowIndex(String[] rows) {
    return rows.length - 1;
  }

  private String getPathnameAsc(String targetFile) {
    return targetDir + targetFile.replace(".zip", ".asc");
  }

  private String getPathnameZip(String targetFile) {
    return targetDir + targetFile;
  }

  private LinkedList<ISolRad> getSolRads(IDataReader fileReader, int dateAsInt, String targetFile) throws Exception {
    LinkedList<ISolRad> solRads = new LinkedList<>();
    final String[] rows = getColumns(fileReader.getDataAsString(getPathnameAsc(targetFile)), "\\r\\n");
    rightVersionGuard(rows[2]);
    int gkh = 5237500;
    for (int rowIndex = getLastRowIndex(rows); rowIndex >= 28; rowIndex--) {
      int gkr = 3280500;
      for (String column : getColumns(rows[rowIndex], " ")) {
        solRads.add(initSolRad(gkh, gkr, column, dateAsInt));
        gkr = increment(gkr);
      }
      gkh = increment(gkh);
    }
    Collections.reverse(solRads);
    return solRads;
  }

  @Override
  public SolRadTypes getSolRadType() {
    return this.solRadType;
  }

  private String getSolRadTypeAsString() {
    return this.solRadType.name().toLowerCase(Locale.ENGLISH);
  }

  private String getUrl(String targetFile) {
    return targetUrl + targetFile;
  }

  private int increment(int gk) {
    return gk + 1000;
  }

  private ISolRad initSolRad(int gkh, int gkr, String column, int dateAsInt) throws Exception {
    ISolRad solRad = solRadClass.getDeclaredConstructor().newInstance();
    solRad.setRadiationValue(Float.parseFloat(column));
    solRad.setRadiationType(solRadType.name());
    solRad.setRadiationDate(dateAsInt);
    solRad.setGkhMin(gkh);
    solRad.setGkhMax(gkh + 1000);
    solRad.setGkrMin(gkr);
    solRad.setGkrMax(gkr + 1000);
    return solRad;
  }

  @Override
  public void insert(IBasicSolRad basicSolRad, IDataReader fileReader, int month, int year) {
    String targetFile = null;
    try {
      final String dateAsString = getDateAsString(year, month);
      nullGuard(dateAsString);
      final int dateAsInt = getDateAsInt(dateAsString);
      isAlreadyExistGuard(basicSolRad, dateAsInt);
      targetFile = getCurrentTargetFile(dateAsString);
      nullGuard(targetFile);
      download(targetFile);
      unzip(targetFile);
      insertRadiation(basicSolRad, fileReader, dateAsInt, targetFile);
    } catch (Exception e) {
      LOGGER.info(e.toString());
    } finally {
      delete(targetFile);
    }
  }

  private void insertRadiation(IBasicSolRad basicSolRad, IDataReader fileReader, int dateAsInt, String targetFile)
      throws Exception {
    basicSolRad.save(getSolRads(fileReader, dateAsInt, targetFile));
  }

  private void isAlreadyExistGuard(IBasicSolRad basicSolRad, int dateAsInt) throws Exception {
    if (basicSolRad.isAlreadyExist(dateAsInt, solRadType)) {
      throw new Exception("month already exists");
    }
  }

  private boolean isRightVersion(String version) {
    return version.equals("Datensatz_Version=V003") || version.equals("Datensatz_Version=V0.3");
  }

  private void nullGuard(Object nullAble) throws Exception {
    if (nullAble == null) {
      throw new Exception("Guard: null");
    }
  }

  private void rightVersionGuard(String version) {
    if (!isRightVersion(version)) {
      throw new IllegalArgumentException("wrong version");
    }
  }

  private void unzip(String targetFile) throws Exception {
    File destDir = new File(targetDir);
    byte[] buffer = new byte[1024];
    try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(getPathnameZip(targetFile)))) {
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
}
