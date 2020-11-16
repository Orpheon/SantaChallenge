package ch.mse.santachallenge;

public class Location {
    private double longitude;
    private double latitude;

    public Location(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double distanceTo(Location location) {
        // distance between latitudes and longitudes
        double dLat = Math.toRadians(location.latitude - latitude);
        double dLon = Math.toRadians(location.longitude - longitude);

        // convert to radians
        double lat1 = Math.toRadians(latitude);
        double lat2 = Math.toRadians(location.latitude);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
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
}
