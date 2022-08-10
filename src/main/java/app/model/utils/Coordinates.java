package app.model.utils;

import java.awt.geom.Point2D;

/**
 *      |==================================|
 *      |=========| GEO LOCATION  |========|
 *      |==================================|
 *
 *  geo location extends 'java.awt.geom.Point2D.Double'
 *
 *
 * @author  Kfir Ettinger & Amit Hajaj & Motti Dahari
 * @version 1.0
 * @since   2021-06-20
 */
public class Coordinates extends Point2D.Double{
    public Coordinates(double latitude, double longitude) {
        super(latitude, longitude);
    }

    public double getLatitude() {
        return getX();
    }

    public double getLongitude() {
        return getY();
    }

    public void setCoordinates(double latitude, double longitude) {
        x = latitude ;
        y = longitude;
    }

    @Override
    public String toString() {
        return "coordinates = (" +getLatitude() + "," + getLongitude() + ")";
    }
}
