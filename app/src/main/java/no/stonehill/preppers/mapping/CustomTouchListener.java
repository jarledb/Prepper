package no.stonehill.preppers.mapping;

import android.content.Context;
import android.graphics.Point;
import android.view.MotionEvent;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static no.stonehill.preppers.mapping.GraphicsHelper.GRAPHIC_ID;

public class CustomTouchListener extends DefaultMapViewOnTouchListener {

    public CustomTouchListener(Context context, MapView mapView) {
        super(context, mapView);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        ListenableFuture<List<IdentifyGraphicsOverlayResult>> listListenableFuture = mMapView.identifyGraphicsOverlaysAsync(new Point((int) e.getX(), (int) e.getY()),15d, false );
        listListenableFuture.addDoneListener(() -> {
            try {
                List<IdentifyGraphicsOverlayResult> identifyGraphicsOverlayResults = listListenableFuture.get();
                for (IdentifyGraphicsOverlayResult result : identifyGraphicsOverlayResults) {
                    for (Graphic graphic : result.getGraphics()) {
                        if (graphic.getAttributes().containsKey(GRAPHIC_ID)) {
                            System.out.println("ID: " + graphic.getAttributes().get(GRAPHIC_ID));
                        }
                    }
                }


            } catch (InterruptedException | ExecutionException e1) {
                e1.printStackTrace();
            }

        });
        return super.onSingleTapConfirmed(e);
    }
}