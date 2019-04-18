package de.josmer.application.controller.requests;

public final class ExtractorRequest extends Request {
    private int year;
    private int as;
    private int ys;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getAs() {
        return as;
    }

    public void setAs(int as) {
        this.as = as;
    }

    public int getYs() {
        return ys;
    }

    public void setYs(int ys) {
        this.ys = ys;
    }
}
