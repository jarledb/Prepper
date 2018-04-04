package no.stonehill.preppers.mapping;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import no.stonehill.preppers.R;
import no.stonehill.preppers.geo.LocationProvider;
import no.stonehill.preppers.mapping.renderers.GpxRenderer;
import no.stonehill.preppers.mapping.renderers.OwnPositionRenderer;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MappingActivity extends AppCompatActivity {
    public static final int REQ_CODE = 9876;
    public static final int REQ_CODE_GPX = 4567;

    @BindView(R.id.mapping_activity_mapView) MapView mapView;
    @BindColor(R.color.disabled) ColorStateList disabled;
    @BindColor(R.color.enabled) ColorStateList enabled;

    @Inject OwnPositionRenderer ownPositionRenderer;
    @Inject GraphicsHelper graphicsHelper;
    @Inject OverlayManager overlayManager;
    @Inject LocationProvider locationProvider;
    @Inject MapController mapController;
    @Inject GpxRenderer gpxRenderer;

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
        mapView.setMap(mMap);
        mapView.setAttributionTextVisible(false);
        mapView.setOnTouchListener(new CustomTouchListener(getApplicationContext(), mapView));
    }

    @Override
    protected void onStart() {
        super.onStart();
        overlayManager.addOverlaysToMap(mapView);
        ownPositionRenderer.start();
        mapController.start(mapView);
        startGpxParser();
        startLocationProvider();
    }

    @Override
    protected void onStop() {
        super.onStop();
        overlayManager.removeOverlaysToMap(mapView);
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

    @OnClick(R.id.mapping_activity_map_mode)
    void switchMapMode(ImageButton button) {
        mapController.setNextMapMode();
        switch (mapController.getMapMode()) {
            case FOLLOW_NORTH_UP:
                button.setRotation(0);
                button.setImageTintList(enabled);
                break;
            case FOLLOW_HEAD_UP:
                button.setRotation(45);
                button.setImageTintList(enabled);
                break;
            case FREE_PAN:
                button.setRotation(0);
                button.setImageTintList(disabled);
                break;
        }
    }

    private void startGpxParser() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[2];
            permissions[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            permissions[1] = Manifest.permission.READ_EXTERNAL_STORAGE;
            ActivityCompat.requestPermissions(this, permissions, REQ_CODE_GPX);
            return;
        }
        gpxRenderer.start();
    }

    private void startLocationProvider() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[2];
            permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
            permissions[1] = Manifest.permission.ACCESS_COARSE_LOCATION;
            ActivityCompat.requestPermissions(this, permissions, REQ_CODE);
            return;
        }
        locationProvider.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            locationProvider.start();
        }
        if (requestCode == REQ_CODE_GPX && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            gpxRenderer.start();
        }
    }
}
