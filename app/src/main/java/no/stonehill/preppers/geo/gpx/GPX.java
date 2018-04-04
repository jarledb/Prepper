package no.stonehill.preppers.geo.gpx;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

/**
 GPS Exchange Format
 <?xml version="1.0" encoding="UTF-8"?>
 <gpx>
     <metadata>
     <time>2018-03-29T13:56:06Z</time>
     </metadata>
     <trk>
         <name>Afternoon Activity</name>
         <trkseg>
             <trkpt lat="60.6320110" lon="8.3051150">
                 <ele>1098.6</ele>
                 <time>2018-03-29T13:56:07Z</time>
             </trkpt>
             <trkpt lat="60.6333450" lon="8.3011580">
                 <ele>1106.4</ele>
                 <time>2018-03-29T15:34:12Z</time>
            </trkpt>
        </trkseg>
    </trk>
 </gpx>


 */
@Root(name = "gpx")
@Default(required=false)
public class GPX {
    public enum Type {LINE, AREA}

    Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @ElementList(inline = true)
    ArrayList<Track> tracks;

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    @Root(name = "trk")
    public static class Track {

        @Element(name = "name")
        String name;

        @ElementList(inline = true)
        ArrayList<TrackSegment> trackSegments;

        public String getName() {
            return name;
        }

        public ArrayList<TrackSegment> getTrackSegments() {
            return trackSegments;
        }
    }

    @Root(name = "trkseg")
    public static class TrackSegment {

        @ElementList(name = "trkpt", inline = true)
        ArrayList<TrackPoint> trackPoints;

        public ArrayList<TrackPoint> getTrackPoints() {
            return trackPoints;
        }
    }

    @Root(name = "trkpt")
    public static class TrackPoint {

        @Attribute(name = "lat")
        String lattitude;

        @Attribute(name = "lon")
        String longitude;

        @Element(name = "ele")
        String elevation;

        @Element(name = "time")
        String time;

        public String getLattitude() {
            return lattitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getElevation() {
            return elevation;
        }

        public String getTime() {
            return time;
        }
    }
}
