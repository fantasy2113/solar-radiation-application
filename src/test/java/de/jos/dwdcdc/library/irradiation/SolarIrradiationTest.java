package de.jos.dwdcdc.library.irradiation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class SolarIrradiationTest {

  private final int ae;
  private final double[] eGlobHorMonthly;
  private final double lat;
  private final double lon;
  private final int ye;
  private final int year;
  private SolarIrradiation solarIrradiation;

  public SolarIrradiationTest() {
    lon = 13.373;
    lat = 52.519;
    ae = 180;
    ye = 30;
    year = 2010;
    eGlobHorMonthly = new double[12];
    eGlobHorMonthly[0] = 18290.0;
    eGlobHorMonthly[1] = 36090.0;
    eGlobHorMonthly[2] = 72350.0;
    eGlobHorMonthly[3] = 130470.0;
    eGlobHorMonthly[4] = 106420.0;
    eGlobHorMonthly[5] = 188370.0;
    eGlobHorMonthly[6] = 192600.0;
    eGlobHorMonthly[7] = 119440.0;
    eGlobHorMonthly[8] = 83790.0;
    eGlobHorMonthly[9] = 59920.0;
    eGlobHorMonthly[10] = 17280.0;
    eGlobHorMonthly[11] = 12620.0;

  }

  @Test
  public void compute() {
    try {
      Instant start = Instant.now();
      solarIrradiation.compute();
      System.out.println(Duration.between(start, Instant.now()).toString());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void computeParallel() {
    try {
      Instant start = Instant.now();
      solarIrradiation.computeParallel();
      System.out.println(Duration.between(start, Instant.now()).toString());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void getComputedYear() {
    solarIrradiation.computeParallel();
    ComputedYear computedYear = solarIrradiation.getComputedYear();
    for (int i = 0; i < 12; i++) {
      assertTrue(computedYear.getMonthHor(i) > 0);
      assertTrue(computedYear.getMonthInc(i) > 0);
    }
  }

  @BeforeEach
  public void setUp() {
    solarIrradiation = new SolarIrradiation(lat, lon, eGlobHorMonthly, year, ye, ae);
  }

  @AfterEach
  public void tearDown() {
  }
}
