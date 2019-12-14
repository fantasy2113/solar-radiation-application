package de.josmer.dwdcdc.app.requests;

import de.josmer.dwdcdc.app.interfaces.Identifiable;

public final class IrrRequest extends Request implements Identifiable {

    private int ae;
    private int ye;
    private int year;

    public IrrRequest() {
    }

    public IrrRequest(double lat, double lon, int year, int ae, int ye) {
        super(lat, lon);
        this.year = year;
        this.ae = ae;
        this.ye = ye;
    }

    public int getAe() {
        return ae;
    }

    public void setAe(int ae) {
        this.ae = ae;
    }

    @Override
    public int getId() {
        return getKey().hashCode();
    }

    @Override
    public String getKey() {
        return this.toString();
    }

    public int getYe() {
        return ye;
    }

    public void setYe(int ye) {
        this.ye = ye;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "lon=" + this.lon + ";" + "lat=" + this.lat + ";" + "ae=" + this.ae + ";" + "ye=" + this.ye + ";"
                + "year=" + this.year;
    }
}
