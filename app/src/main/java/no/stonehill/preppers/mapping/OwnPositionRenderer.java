package no.stonehill.preppers.mapping;

import android.location.Location;

import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.Symbol;

import no.stonehill.preppers.R;
import no.stonehill.preppers.location.LocationProvider;

import static no.stonehill.preppers.mapping.MappingActivity.WGS_84;

public class OwnPositionRenderer {

    private final GraphicsHelper graphicsHelper;
    private final GraphicsOverlay overlay;
    private final LocationProvider locationProvider;

    private Graphic scooterGraphic;

    public OwnPositionRenderer(GraphicsHelper graphicsHelper, OverlayManager overlayManager, LocationProvider locationProvider) {
        this.graphicsHelper = graphicsHelper;
        overlay = overlayManager.getOverlay(OverlayManager.Overlay.OWN_POS);
        this.locationProvider = locationProvider;
    }

    public void start() {
        locationProvider.subscribe(this::onLocationUpdate);
    }

    private void onLocationUpdate(Location location) {
        Geometry geometry = new Point(location.getLongitude(), location.getLatitude(), WGS_84);
        createGraphic(geometry);
    }

    private synchronized void createGraphic(Geometry geometry) {
        if (scooterGraphic == null) {
            Symbol scooter = graphicsHelper.createPictureSymbol(R.drawable.scooter_vector);

            scooterGraphic = new Graphic(geometry, scooter);
            overlay.getGraphics().add(scooterGraphic);
        } else {
            scooterGraphic.setGeometry(geometry);
        }
    }
}
