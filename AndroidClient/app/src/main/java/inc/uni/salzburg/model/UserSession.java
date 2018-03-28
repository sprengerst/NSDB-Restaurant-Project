package inc.uni.salzburg.model;

/**
 * Created by Stefan Sprenger on 15.03.16.
 *
 */
public class UserSession {

    private String geoResolution;
    private float latitude;
    private float longitude;

    public UserSession(String geoResolution, float latitude, float longitude) {
        this.geoResolution = geoResolution;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getGeoResolution() {
        return geoResolution;
    }

    public void setGeoResolution(String geoResolution) {
        this.geoResolution = geoResolution;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}