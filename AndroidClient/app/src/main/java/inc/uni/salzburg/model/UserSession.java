package inc.uni.salzburg.model;

/**
 * Created by Stefan Sprenger on 15.03.16.
 *
 */
public class UserSession {

    private String geoResolution;
    private double latitude;
    private double longitude;

    private int radius;

    public UserSession(String geoResolution, double latitude, double longitude, int radius) {
        this.geoResolution = geoResolution;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }


    public String getGeoResolution() {
        return geoResolution;
    }

    public void setGeoResolution(String geoResolution) {
        this.geoResolution = geoResolution;
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

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}