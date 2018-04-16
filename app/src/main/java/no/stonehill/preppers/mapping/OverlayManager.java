package no.stonehill.preppers.mapping;

import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;

public class OverlayManager {
    public enum Overlay {
        HISTORY,
        GPX_TRACKS,
        OWN_POS;

        private final GraphicsOverlay overlay = new GraphicsOverlay();
    }

    public void addOverlaysToMap(MapView mapView) {
        for (Overlay overlay : Overlay.values()) {
            mapView.getGraphicsOverlays().add(overlay.overlay);
        }
    }

    public void removeOverlaysToMap(MapView mapView) {
        for (Overlay overlay : Overlay.values()) {
            mapView.getGraphicsOverlays().remove(overlay.overlay);
        }
    }

    public GraphicsOverlay getOverlay(Overlay overlay) {
        return overlay.overlay;
    }
}
