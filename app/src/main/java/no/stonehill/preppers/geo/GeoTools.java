package no.stonehill.preppers.geo;

import android.location.Location;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

public class GeoTools {
    private static final double EARTH_DIAMETER = 6371010;

    public static double distance(Location locationA, Location locationB) {

        double diff = toRadians(abs(locationB.getLongitude() - locationA.getLongitude()));
        double aRad = toRadians(locationA.getLatitude());
        double bRad = toRadians(locationB.getLatitude());

        double c = acos((sin(aRad) * sin(bRad)) + (cos(aRad) * cos(bRad) * cos(diff)));

        return EARTH_DIAMETER * c;
    }

    public static double bearing(Location locationA, Location locationB) {


        double y = sin(toRadians(locationB.getLongitude() - locationA.getLongitude())) * cos(toRadians(locationB.getLatitude()));
        double x = cos(toRadians(locationA.getLatitude())) * sin(toRadians(locationB.getLatitude()))
                - sin(toRadians(locationA.getLatitude())) * cos(toRadians(locationB.getLatitude())) * cos(toRadians(locationB.getLongitude()
                - locationA.getLongitude()));

        double bearing = (atan2(y, x) + 2 * PI) % (2 * PI);

        return toDegrees(bearing);
    }
}
