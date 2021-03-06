package de.jos.dwdcdc.app.entities;

abstract class Export {

  private String date;
  private String dim;
  private String lat;
  private String lon;
  private String source;
  private String unit;

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getDim() {
    return dim;
  }

  public void setDim(String dim) {
    this.dim = dim;
  }

  public String getLat() {
    return lat;
  }

  public void setLat(String lat) {
    this.lat = lat;
  }

  public String getLon() {
    return lon;
  }

  public void setLon(String lon) {
    this.lon = lon;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }
}
