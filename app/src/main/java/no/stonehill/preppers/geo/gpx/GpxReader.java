package no.stonehill.preppers.geo.gpx;

import android.os.Environment;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GpxReader {
    private final Persister persister;

    public GpxReader() {
        persister = new Persister(new AnnotationStrategy());
    }

    public List<GPX> getFilesFromDownloadDirectory() {
        List<GPX> gpxList = new ArrayList<>();
        File externalFilesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        if (externalFilesDir == null) return gpxList;

        for (File file : externalFilesDir.listFiles(name -> name.getAbsolutePath().toLowerCase().endsWith(".gpx"))) {
            GPX gpx = read(file);
            if (gpx != null) {
                gpx.setType(GPX.Type.LINE);
                gpxList.add(gpx);
            }
        }

        for (File file : externalFilesDir.listFiles(name -> name.getAbsolutePath().toLowerCase().endsWith(".area"))) {
            GPX gpx = read(file);
            if (gpx != null) {
                gpx.setType(GPX.Type.AREA);
                gpxList.add(gpx);
            }
        }

        return gpxList;
    }

    public GPX read(File file) {
        try {
            return persister.read(GPX.class, file, false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
