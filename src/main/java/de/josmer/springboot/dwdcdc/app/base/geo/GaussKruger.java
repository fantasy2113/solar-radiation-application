package de.josmer.springboot.dwdcdc.app.base.geo;

public final class GaussKruger {
    private double lon;
    private double lat;
    private double rechtswert;
    private double hochwert;

    public GaussKruger(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public void compute() {
        wgs84ToPot();
        geoToGk();
    }

    private void geoToGk() {
        double a = 6377397.155;
        double f = 3.34277321e-3;
        double c = a / (1.0 - f);
        double ex2 = (2.0 * f - f * f) / ((1.0 - f) * (1.0 - f));
        double ex4 = ex2 * ex2;
        double ex6 = ex4 * ex2;
        double ex8 = ex4 * ex4;
        double e0 = c * (Math.PI / 180) * (1.0 - 3.0 * ex2 / 4.0 + 45.0 * ex4 / 64.0 - 175.0 * ex6 / 256.0 + 11025.0 * ex8 / 16384.0);
        double e2 = c * (-3.0 * ex2 / 8.0 + 15.0 * ex4 / 32 - 525 * ex6 / 1024.0 + 2205.0 * ex8 / 4096.0);
        double e4 = c * (15.0 * ex4 / 256.0 - 105.0 * ex6 / 1024.0 + 2205.0 * ex8 / 16384.0);
        double e6 = c * (-35.0 * ex6 / 3072.0 + 315.0 * ex8 / 12288.0);
        double br = this.lat * Math.PI / 180.0;
        double tan1 = Math.tan(br);
        double tan2 = tan1 * tan1;
        double tan4 = tan2 * tan2;
        double cos1 = Math.cos(br);
        double cos2 = cos1 * cos1;
        double cos4 = cos2 * cos2;
        double cos3 = cos2 * cos1;
        double cos5 = cos4 * cos1;
        double etasq = ex2 * cos2;
        double nd = c / Math.sqrt(1.0 + etasq);
        double g = e0 * this.lat + e2 * Math.sin(2 * br) + e4 * Math.sin(4.0 * br) + e6 * Math.sin(6.0 * br);
        double kz = (int) ((this.lon + 1.5) / 3.0);
        double lh = kz * 3.0;
        double dl = (this.lon - lh) * Math.PI / 180.0;
        double dl2 = dl * dl;
        double dl4 = dl2 * dl2;
        double dl3 = dl2 * dl;
        double dl5 = dl4 * dl;
        this.hochwert = (g + nd * cos2 * tan1 * dl2 / 2.0 + nd * cos4 * tan1 * (5.0 - tan2 + 9.0 * etasq) * dl4 / 24.0);
        this.rechtswert = (nd * cos1 * dl + nd * cos3 * (1.0 - tan2 + etasq) * dl3 / 6.0 + nd * cos5 * (5.0 - 18.0 * tan2 + tan4) * dl5 / 120.0 + kz * 1e6 + 500000.0);
        double nk = this.hochwert - (int) this.hochwert;
        if (nk < 0.5) {
            this.hochwert = (int) this.hochwert;
        } else {
            this.hochwert = (int) (this.hochwert) + 1.0;
        }
        nk = this.rechtswert - (int) this.rechtswert;
        if (nk < 0.5) {
            this.rechtswert = (int) this.rechtswert;
        } else {
            this.rechtswert = (int) this.rechtswert + 1.0;
        }
    }

    private void wgs84ToPot() {
        double a = 6378137.000;
        double fq = 3.35281066e-3;
        double f = fq - 1.003748e-5;
        double dx = -587;
        double dy = -16;
        double dz = -393;
        double e2q = (2 * fq - fq * fq);
        double e2 = (2 * f - f * f);
        double b1 = this.lat * (Math.PI / 180);
        double l1 = this.lon * (Math.PI / 180);
        double nd = a / Math.sqrt(1 - e2q * Math.sin(b1) * Math.sin(b1));
        double xw = nd * Math.cos(b1) * Math.cos(l1);
        double yw = nd * Math.cos(b1) * Math.sin(l1);
        double zw = (1 - e2q) * nd * Math.sin(b1);
        double x = xw + dx;
        double y = yw + dy;
        double z = zw + dz;
        double rb = Math.sqrt(x * x + y * y);
        double b2 = (180 / Math.PI) * Math.atan((z / rb) / (1 - e2));
        double l2 = 0;
        if (x > 0) {
            l2 = (180 / Math.PI) * Math.atan(y / x);
        } else if (x < 0 && y > 0) {
            l2 = (180 / Math.PI) * Math.atan(y / x) + 180;
        } else if (x < 0 && y < 0) {
            l2 = (180 / Math.PI) * Math.atan(y / x) - 180;
        }
        this.lon = l2;
        this.lat = b2;
    }

    public double getRechtswert() {
        return rechtswert;
    }

    public double getHochwert() {
        return hochwert;
    }

}
