package no.stonehill.preppers.mapping.renderers;

import android.graphics.Color;
import android.location.Location;

import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;

import java.util.ArrayList;
import java.util.List;

import no.stonehill.preppers.geo.LocationProvider;
import no.stonehill.preppers.mapping.GraphicsHelper;
import no.stonehill.preppers.mapping.OverlayManager;

import static no.stonehill.preppers.mapping.GraphicsHelper.GRAPHIC_ID;

public class LocationHistoryRenderer {

    private final GraphicsHelper graphicsHelper;
    private final GraphicsOverlay overlay;
    private final LocationProvider locationProvider;

    private Graphic historyGraphic;
    private List<Point> history = new ArrayList<>();

    public LocationHistoryRenderer(GraphicsHelper graphicsHelper, OverlayManager overlayManager, LocationProvider locationProvider) {
        this.graphicsHelper = graphicsHelper;
        overlay = overlayManager.getOverlay(OverlayManager.Overlay.HISTORY);
        this.locationProvider = locationProvider;
    }

    public void start() {
        locationProvider.subscribe(this::onLocationUpdate);
    }

    public void reset() {
        history.clear();
        createOrUpdateGraphic();
    }

    private void onLocationUpdate(Location location) {
        history.add(graphicsHelper.convert(location));
        createOrUpdateGraphic();
    }

    private synchronized void createOrUpdateGraphic() {
        Geometry geometry = new Polyline(new PointCollection(history));
        if (historyGraphic == null) {
            historyGraphic = new Graphic(
                    geometry,
                    new SimpleLineSymbol(SimpleLineSymbol.Style.DASH, Color.GREEN, 4)
            );

            historyGraphic.getAttributes().put(GRAPHIC_ID, "Location history");
            overlay.getGraphics().add(historyGraphic);
        } else {
            historyGraphic.setGeometry(geometry);
        }
    }
}
