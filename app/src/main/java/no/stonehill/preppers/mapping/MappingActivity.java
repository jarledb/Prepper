package no.stonehill.preppers.mapping;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.Symbol;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import no.stonehill.preppers.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MappingActivity extends AppCompatActivity {
    private final SpatialReference wgs84 = SpatialReference.create(4326);


    @BindView(R.id.mapping_activity_mapView) MapView mapView;

    @Inject OwnPositionRenderer ownPositionRenderer;

    private final GraphicsOverlay symbolsOverlay = new GraphicsOverlay();
    private ArcGISMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);


        ArcGISRuntimeEnvironment.setLicense(License.KEY);
        setContentView(R.layout.activity_mapping);
        getSupportActionBar().hide();
        ButterKnife.bind(this);

        mMap = new ArcGISMap(Basemap.Type.OPEN_STREET_MAP, 60.09, 10.29, 14);
        // set the map to be displayed in this view
        this.mapView.setMap(mMap);
        this.mapView.setAttributionTextVisible(false);

        addLayers();
        addGraphics();
    }

    private void addLayers() {
        this.mapView.getGraphicsOverlays().add(symbolsOverlay);
    }

    private void addGraphics() {

        Symbol scooter = createPictureMarkerSymbol(R.drawable.scooter_vector);

        Geometry location = new Point(10.29, 60.10, wgs84);
        this.symbolsOverlay.getGraphics().add(new Graphic(location, scooter));
    }

    private PictureMarkerSymbol createPictureMarkerSymbol(@DrawableRes int resource) {
        Drawable drawable = getResources().getDrawable(resource);
        try {
            return PictureMarkerSymbol.createAsync(drawableToBitmap(drawable)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BitmapDrawable drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            return bitmapDrawable;
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        if (drawable.getBounds().width() == 0 && drawable.getBounds().height() == 0) {
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        }
        drawable.draw(canvas);
        return new BitmapDrawable(bitmap);
    }

    @OnClick(R.id.mapping_activity_zoom_in)
    void zoomIn() {
        double mapScale = Math.max(mapView.getMapScale() / 2, 1000);
        mapView.setViewpointScaleAsync(mapScale);
    }

    @OnClick(R.id.mapping_activity_zoom_out)
    void zoomOut() {
        double mapScale = Math.min(mapView.getMapScale() * 2, 100000000);
        mapView.setViewpointScaleAsync(mapScale);
    }


}
