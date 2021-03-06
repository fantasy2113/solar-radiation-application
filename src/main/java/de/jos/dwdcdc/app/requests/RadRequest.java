package de.jos.dwdcdc.app.requests;

public final class RadRequest extends Request {

  private String endDate;
  private String startDate;
  private String type;

  public RadRequest() {
  }

  public RadRequest(double lat, double lon, String startDate, String endDate, String type) {
    super(lat, lon);
    this.startDate = startDate;
    this.endDate = endDate;
    this.type = type;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
