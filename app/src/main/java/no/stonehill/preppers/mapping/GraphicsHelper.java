package no.stonehill.preppers.mapping;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.annotation.DrawableRes;

import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;

import java.util.List;
import java.util.concurrent.ExecutionException;

import no.stonehill.preppers.geo.gpx.GPX;

public class GraphicsHelper {
    public static final SpatialReference WGS_84 = SpatialReference.create(4326);
    public static final String GRAPHIC_ID = "GRAPHIC_ID";
    private final Context context;

    public GraphicsHelper(Context context) {
        this.context = context;
    }

    public PictureMarkerSymbol createPictureSymbol(@DrawableRes int resource) {
        Drawable drawable = context.getResources().getDrawable(resource, context.getTheme());
        try {
            return PictureMarkerSymbol.createAsync(drawableToBitmap(drawable)).get();
        } catch (InterruptedException | ExecutionException e) {
            //Ignore
        }
        return null;
    }

    private BitmapDrawable drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            return (BitmapDrawable) drawable;
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
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public Point convert(Location location) {
        return new Point(location.getLongitude(), location.getLatitude(), WGS_84);
    }

    public Point convert(GPX.TrackPoint trackPoint) {
        return new Point(
                Double.parseDouble(trackPoint.getLongitude()),
                Double.parseDouble(trackPoint.getLattitude()),
                WGS_84);
    }

    public Geometry createLineGeometry(List<Point> points) {
        return new Polyline(new PointCollection(points));
    }
}
