package de.josmer.dwdcdc.app.requests;


public final class IrrRequest extends Request {
    private int year;
    private int ae;
    private int ye;

    public IrrRequest(double lat, double lon, int year, int ae, int ye) {
        super(lat, lon);
        this.year = year;
        this.ae = ae;
        this.ye = ye;
    }

    public IrrRequest() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getAe() {
        return ae;
    }

    public void setAe(int ae) {
        this.ae = ae;
    }

    public int getYe() {
        return ye;
    }

    public void setYe(int ye) {
        this.ye = ye;
    }

    @Override
    public String toString() {
        return "lon=" + this.lon + ";" + "lat=" + this.lat + ";" + "ae=" + this.ae + ";" + "ye=" + this.ye + ";" + "year=" + this.year;
    }

    public String getKey() {
        return this.toString();
    }

    public int getId() {
        return getKey().hashCode();
    }
}
