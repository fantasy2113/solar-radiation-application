package de.josmer.dwdcdc.app.controller.requests;

public final class IrrRequest extends Request {
    private int year;
    private int ae;
    private int ye;

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
}
