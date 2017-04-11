package dk.itu.n.danmarkskort.backend;

import dk.itu.n.danmarkskort.models.ParsedWay;
import dk.itu.n.danmarkskort.models.WayType;

public class WayTypeUtil {

    public static WayType tagToType(String k, String v, WayType oldtype, ParsedWay way) {
        if(oldtype != null && oldtype == WayType.COASTLINE) return oldtype; // otherwise we could change a coastline to a different type
        switch (k) {
            case "highway":
                switch(v) {
                    case "motorway_link":
                    case "motorway":
                        return WayType.HIGHWAY_MOTORWAY;
                    case "trunk_link":
                    case "trunk":
                        return WayType.HIGHWAY_TRUNK;
                    case "primary_link":
                    case "primary":
                        return WayType.HIGHWAY_PRIMARY;
                    case "secondary_link":
                    case "secondary":
                        return WayType.HIGHWAY_SECONDARY;
                    case "tertiary_link":
                    case "tertiary":
                        return WayType.HIGHWAY_TERTIARY;
                    case "residential":
                        return WayType.HIGHWAY_RESIDENTIAL;
                    case "pedestrian":
                        return WayType.PEDESTRIAN;
                    case "unclassified":
                        return WayType.HIGHWAY_UNCLASSIFIED;
                    case "service":
                        return WayType.HIGHWAY_SERVICE;
                    case "driveway":
                        return WayType.HIGHWAY_DRIVEWAY;
                    case "cycleway":
                        return WayType.HIGHWAY_CYCLEWAY;
                    case "traffic_signal":
                        return oldtype;
                    case "path":
                    case "footway":
                    case "bridleway":
                    case "track":
                    case "steps":
                        return WayType.HIGHWAY_FOOTWAY;
                    default:
                        return WayType.HIGHWAY_UNDEFINED;
                }
            case "building":
                switch (v) {
                    case "school":
                        return WayType.BUILDING_SCHOOL;
                    case "train_station":
                        return WayType.TRAIN_STATION;
                    case "roof":
                        return WayType.ROOF;
                    default:
                        return WayType.BUILDING;
                }
            case "landuse":
                switch(v) {
                    case "residential":
                        return WayType.RESIDENTIAL;
                    case "farmland":
                        return WayType.FARMLAND;
                    case "forest":
                        return WayType.FOREST;
                    case "industrial":
                        return WayType.INDUSTRIAL;
                    case "recreation_ground":
                    case "grass":
                        return WayType.GRASS;
                    case "retail":
                        return WayType.RETAIL;
                    case "military":
                        return WayType.MILITARY;
                    case "cemetery":
                        return WayType.CEMETERY;
                    case "orchard":
                        return WayType.ORCHARD;
                    case "allotments":
                        return WayType.ALLOTMENTS;
                    case "construction":
                        return WayType.CONSTRUCTION;
                    case "railway":
                        return WayType.RAILWAY;
                }
                return oldtype;
            case "natural":
                switch(v) {
                    case "water":
                        return WayType.WATER;
                    case "coastline":
                        return WayType.COASTLINE;
                    case "scrub":
                        return WayType.SCRUB;
                    case "wood":
                        return WayType.WOOD;
                    case "wetland":
                        return WayType.WETLAND;
                    case "sand":
                        return WayType.SAND;
                }
                break;
            case "railway":
                switch(v) {
                    case "light_rail":
                        return WayType.LIGHT_RAIL;
                    case "platform":
                        return WayType.PLATFORM;
                }
                return oldtype;
            case "man_made":
                switch(v) {
                    case "breakwater":
                        return WayType.BREAKWATER;
                    case "pier":
                        return WayType.PIER;
                    case "embankment":
                        return WayType.EMBANKMENT;
                }
                return oldtype;
            case "waterway":
                switch(v) {
                    case "stream":
                        return WayType.WATER_STREAM;
                    case "river":
                        return WayType.WATER_RIVER;
                }
                return oldtype;
            case "leisure":
                switch(v) {
                    case "stadium":
                        return WayType.STADIUM;
                    case "park":
                    case "garden":
                        return WayType.PARK;
                    case "playground":
                        return WayType.PLAYGROUND;
                    case "track":
                    case "pitch":
                        return WayType.SPORT;
                }
                return oldtype;
            case "amenity":
                switch(v) {
                    case "parking":
                        return WayType.PARKING;
                }
                return oldtype;
        }
        return oldtype;
    }

}
