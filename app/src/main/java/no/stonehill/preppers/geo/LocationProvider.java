package no.stonehill.preppers.geo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import no.stonehill.preppers.utils.ListenerManager;

public class LocationProvider {

    private String locationProvider;
    private final Context context;

    private final ListenerManager<Location> locationListenerManager = new ListenerManager<>();

    public LocationProvider(Context context) {
        this.context = context;
    }

    public void subscribe(ListenerManager.Listener<Location> listener) {
        locationListenerManager.add(listener);
    }

    public void unsubscribe(ListenerManager.Listener<Location> listener) {
        locationListenerManager.remove(listener);
    }

    @SuppressLint("MissingPermission")
    public void start() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                System.out.println("LAT: " + location.getLatitude() + " LONG: " + location.getLongitude());
                locationListenerManager.trigger(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        locationProvider = LocationManager.GPS_PROVIDER;
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);

    }
}
