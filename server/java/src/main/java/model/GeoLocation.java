package model;

import java.awt.geom.Point2D;

public class GeoLocation extends Point2D.Double{

//    private Double longitude = 0.0;
//    private Point2D.Double location = new Point2D.Double(0,132.2);


    public GeoLocation(double x, double y) {
        super(x, y);
    }

    @Override
    public double distance(Point2D pt) {
        return super.distance(pt);
    }
}
