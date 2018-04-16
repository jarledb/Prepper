package no.stonehill.preppers.mapping.renderers;

import android.location.Location;

import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.MarkerSymbol;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;

import no.stonehill.preppers.R;
import no.stonehill.preppers.geo.GeoTools;
import no.stonehill.preppers.geo.LocationProvider;
import no.stonehill.preppers.mapping.GraphicsHelper;
import no.stonehill.preppers.mapping.OverlayManager;

import static no.stonehill.preppers.mapping.GraphicsHelper.GRAPHIC_ID;

public class OwnPositionRenderer {

    private final GraphicsHelper graphicsHelper;
    private final GraphicsOverlay overlay;
    private final LocationProvider locationProvider;

    private Graphic scooterGraphic;
    private Location lastLocation;
    private PictureMarkerSymbol scooterSymbol;

    public OwnPositionRenderer(GraphicsHelper graphicsHelper, OverlayManager overlayManager, LocationProvider locationProvider) {
        this.graphicsHelper = graphicsHelper;
        overlay = overlayManager.getOverlay(OverlayManager.Overlay.OWN_POS);
        this.locationProvider = locationProvider;
    }

    public void start() {
        locationProvider.subscribe(this::onLocationUpdate);
    }

    private void onLocationUpdate(Location location) {
        createOrUpdateGraphic(location);
    }

    private synchronized void createOrUpdateGraphic(Location location) {
        Geometry geometry = graphicsHelper.convert(location);
        if (scooterGraphic == null) {
            scooterSymbol = graphicsHelper.createPictureSymbol(R.drawable.scooter_vector);
            scooterSymbol.setAngleAlignment(MarkerSymbol.AngleAlignment.MAP);

            scooterGraphic = new Graphic(geometry, scooterSymbol);


            scooterGraphic.getAttributes().put(GRAPHIC_ID, "ME!!!");

            overlay.getGraphics().add(scooterGraphic);
        } else {
            scooterGraphic.setGeometry(geometry);
            scooterSymbol.setAngle(((Double) GeoTools.bearing(lastLocation, location)).floatValue());
        }
        lastLocation = location;
    }
}
