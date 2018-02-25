package no.stonehill.preppers.mapping;

import android.location.Location;

import com.esri.arcgisruntime.mapping.view.MapView;

import no.stonehill.preppers.geo.GeoTools;
import no.stonehill.preppers.geo.LocationProvider;

public class MapController {
    public enum MAP_MODE {
        FOLLOW_NORTH_UP, FOLLOW_HEAD_UP, FREE_PAN;

        public static MAP_MODE next(MAP_MODE mapMode) {
            switch (mapMode) {
                case FOLLOW_NORTH_UP:
                    return FOLLOW_HEAD_UP;
                case FOLLOW_HEAD_UP:
                    return FREE_PAN;
                case FREE_PAN:
                    return FOLLOW_NORTH_UP;
                default:
                    return FOLLOW_NORTH_UP;
            }
        }

    }
    private final LocationProvider locationProvider;

    private final GraphicsHelper graphicsHelper;
    private MapView mapView;
    private MAP_MODE mapMode = MAP_MODE.FOLLOW_NORTH_UP;
    private Location lastLocation;
    public MapController(LocationProvider locationProvider, GraphicsHelper graphicsHelper) {
        this.locationProvider = locationProvider;
        this.graphicsHelper = graphicsHelper;
    }

    public void start(MapView mapView) {
        this.mapView = mapView;
        locationProvider.subscribe(this::onLocationUpdate);
    }

    public void setNextMapMode() {
        mapMode = MAP_MODE.next(mapMode);
    }

    private void onLocationUpdate(Location location) {
        switch (mapMode) {
            case FOLLOW_NORTH_UP:
                mapView.setViewpointRotationAsync(0);
                mapView.setViewpointCenterAsync(graphicsHelper.convert(location));
                break;
            case FOLLOW_HEAD_UP:
                Double bearing = GeoTools.bearing(lastLocation, location);
                mapView.setViewpointRotationAsync(bearing);
                mapView.setViewpointCenterAsync(graphicsHelper.convert(location));
                break;
            case FREE_PAN:
                break;
        }
        lastLocation = location;

    }
}
