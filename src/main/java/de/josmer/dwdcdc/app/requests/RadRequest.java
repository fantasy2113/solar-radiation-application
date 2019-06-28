package de.josmer.dwdcdc.app.requests;

public final class RadRequest extends Request {

    private String startDate;
    private String endDate;
    private String type;

    public RadRequest(double lat, double lon, String startDate, String endDate, String type) {
        super(lat, lon);
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    public RadRequest() {
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
