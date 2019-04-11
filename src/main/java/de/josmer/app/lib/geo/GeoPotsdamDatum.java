package de.josmer.app.lib.geo;

public class GeoPotsdamDatum {

    private double lonGeo;
    private double latGeo;

    public void gkToGeo(final double rw, final double hw) {
        double a = 6377397.155;
        double f = 3.34277321e-3;
        double c = a / (1 - f);
        double ex2 = (2 * f - f * f) / ((1 - f) * (1 - f));
        double ex4 = ex2 * ex2;
        double ex6 = ex4 * ex2;
        double ex8 = ex4 * ex4;
        double e0 = c * (Math.PI / 180) * (1 - 3 * ex2 / 4 + 45 * ex4 / 64 - 175 * ex6 / 256 + 11025 * ex8 / 16384);
        double f2 = (180 / Math.PI) * (3 * ex2 / 8 - 3 * ex4 / 16 + 213 * ex6 / 2048 - 255 * ex8 / 4096);
        double f4 = (180 / Math.PI) * (21 * ex4 / 256 - 21 * ex6 / 256 + 533 * ex8 / 8192);
        double f6 = (180 / Math.PI) * (151 * ex6 / 6144 - 453 * ex8 / 12288);
        double sigma = hw / e0;
        double sigmr = sigma * Math.PI / 180;
        double bf = sigma + f2 * Math.sin(2 * sigmr) + f4 * Math.sin(4 * sigmr) + f6 * Math.sin(6 * sigmr);
        double br = bf * Math.PI / 180;
        double tan1 = Math.tan(br);
        double tan2 = tan1 * tan1;
        double tan4 = tan2 * tan2;
        double cos1 = Math.cos(br);
        double cos2 = cos1 * cos1;
        double etasq = ex2 * cos2;
        double nd = c / Math.sqrt(1 + etasq);
        double nd2 = nd * nd;
        double nd4 = nd2 * nd2;
        double nd6 = nd4 * nd2;
        double nd3 = nd2 * nd;
        double nd5 = nd4 * nd;
        double kz = (int) (rw / 1e6);
        double lh = kz * 3;
        double dy = rw - (kz * 1e6 + 500000);
        double dy2 = dy * dy;
        double dy4 = dy2 * dy2;
        double dy3 = dy2 * dy;
        double dy5 = dy4 * dy;
        double dy6 = dy3 * dy3;
        double b2 = -tan1 * (1 + etasq) / (2 * nd2);
        double b4 = tan1 * (5 + 3 * tan2 + 6 * etasq * (1 - tan2)) / (24 * nd4);
        double b6 = -tan1 * (61 + 90 * tan2 + 45 * tan4) / (720 * nd6);
        double l1 = 1 / (nd * cos1);
        double l3 = -(1 + 2 * tan2 + etasq) / (6 * nd3 * cos1);
        double l5 = (5 + 28 * tan2 + 24 * tan4) / (120 * nd5 * cos1);
        this.latGeo = bf + (180 / Math.PI) * (b2 * dy2 + b4 * dy4 + b6 * dy6);
        this.lonGeo = lh + (180 / Math.PI) * (l1 * dy + l3 * dy3 + l5 * dy5);
        if (lonGeo < 5 || this.lonGeo > 16 || this.latGeo < 46 || this.latGeo > 56) {
            this.lonGeo = 0;
            this.latGeo = 0;
        }
    }

    public double getLonGeo() {
        return lonGeo;
    }

    public double getLatGeo() {
        return latGeo;
    }
}
