package controller.rds.models;

import controller.utils.MapUtils;


public class GeoLocation {
    private double latitude, longitude;
    private String nameLocation, geoLocationId;

    public GeoLocation(String geoLocationId, double latitude, double longitude, String nameLocation) {
        this.geoLocationId = geoLocationId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nameLocation = nameLocation;
    }
    public GeoLocation(double latitude, double longitude, String nameLocation) {
        this.geoLocationId = MapUtils.generateId();
        this.latitude = latitude;
        this.longitude = longitude;
        this.nameLocation = nameLocation;
    }
    public GeoLocation() {
        this.geoLocationId = MapUtils.generateId();
        this.latitude = 23.2222;
        this.longitude = 23.2222;
        this.nameLocation = "nameLocation";
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNameLocation() {
        return nameLocation;
    }

    public void setNameLocation(String nameLocation) {
        this.nameLocation = GeoLocation.this.nameLocation;
    }

    public String getGeoLocationId() {
        return geoLocationId;
    }

    public void setGeoLocationId(String geoLocationId) {
        this.geoLocationId = geoLocationId;
    }

    @Override
    public String toString() {
        return "geoLocation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", nameLocation='" + nameLocation + '\'' +
                ", geoLocationId='" + geoLocationId + '\'' +
                '}';
    }
}
