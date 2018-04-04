package no.stonehill.preppers.mapping.renderers;

import android.content.Context;
import android.graphics.Color;

import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;

import java.util.ArrayList;
import java.util.List;

import no.stonehill.preppers.geo.gpx.GPX;
import no.stonehill.preppers.geo.gpx.GpxReader;
import no.stonehill.preppers.mapping.GraphicsHelper;
import no.stonehill.preppers.mapping.OverlayManager;

import static no.stonehill.preppers.mapping.GraphicsHelper.GRAPHIC_ID;

public class GpxRenderer {
    private final GraphicsHelper graphicsHelper;
    private final GraphicsOverlay overlay;
    private final Context context;
    private final GpxReader gpxReader = new GpxReader();

    private List<GPX> filesFromDownloadDirectory;

    public GpxRenderer(GraphicsHelper graphicsHelper, OverlayManager overlayManager, Context context) {
        this.graphicsHelper = graphicsHelper;
        this.overlay = overlayManager.getOverlay(OverlayManager.Overlay.GPX_TRACKS);
        this.context = context;
    }

    public void start() {
        filesFromDownloadDirectory = gpxReader.getFilesFromDownloadDirectory();
        for (GPX gpx : filesFromDownloadDirectory) {
            if (gpx.getTracks() != null) {
                for (GPX.Track track : gpx.getTracks()) {
                    if (track.getTrackSegments() != null) {
                        for (GPX.TrackSegment trackSegment : track.getTrackSegments()) {
                            if (trackSegment.getTrackPoints() != null) {
                                createLineFromTrackPoints(track, trackSegment.getTrackPoints(), gpx.getType().equals(GPX.Type.LINE));
                            }
                        }
                    }
                }
            }
        }
    }

    private void createLineFromTrackPoints(GPX.Track track, List<GPX.TrackPoint> trackPoints, boolean isLine) {
        List<Point> points = new ArrayList<>();
        for (GPX.TrackPoint trackPoint : trackPoints) {
            points.add(graphicsHelper.convert(trackPoint));
        }
        Graphic graphic;

        if (isLine) {
            Geometry lineGeometry = new Polyline(new PointCollection(points));

            graphic = new Graphic(
                    lineGeometry,
                    new SimpleLineSymbol(SimpleLineSymbol.Style.DASH, Color.RED, 4)
            );
            graphic.getAttributes().put(GRAPHIC_ID, track.getName());

        } else {
            Geometry polygonGeometry = new Polygon(new PointCollection(points));

            graphic = new Graphic(
                    polygonGeometry,
                    new SimpleFillSymbol(
                            SimpleFillSymbol.Style.BACKWARD_DIAGONAL,
                            Color.RED,
                            new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLACK, 2)
                    ));

        }
        overlay.getGraphics().add(graphic);
    }
}
