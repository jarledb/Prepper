package no.stonehill.preppers.mapping;

import android.location.Location;

import com.esri.arcgisruntime.mapping.view.MapView;

import no.stonehill.preppers.geo.LocationProvider;

public class MapController {
    private final LocationProvider locationProvider;
    private final GraphicsHelper graphicsHelper;
    private MapView mapView;

    public MapController(LocationProvider locationProvider, GraphicsHelper graphicsHelper) {
        this.locationProvider = locationProvider;
        this.graphicsHelper = graphicsHelper;
    }

    public void start(MapView mapView) {
        this.mapView = mapView;
        locationProvider.subscribe(this::onLocationUpdate);
    }

    private void onLocationUpdate(Location location) {
        mapView.setViewpointCenterAsync(graphicsHelper.convert(location));
    }
}
