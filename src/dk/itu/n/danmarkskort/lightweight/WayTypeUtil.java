package dk.itu.n.danmarkskort.lightweight;

import dk.itu.n.danmarkskort.models.WayType;

public class WayTypeUtil {

    public static WayType tagToType(String k, String v) {
        WayType type = null;
        switch (k) {
            case "highway":
                switch(v) {
                    case "motorway_link":
                    case "motorway":
                        type = WayType.HIGHWAY_MOTORWAY;
                        break;
                    case "trunk_link":
                    case "trunk":
                        type = WayType.HIGHWAY_TRUNK;
                        break;
                    case "primary_link":
                    case "primary":
                        type = WayType.HIGHWAY_PRIMARY;
                        break;
                    case "secondary_link":
                    case "secondary":
                        type = WayType.HIGHWAY_SECONDARY;
                        break;
                    case "tertiary_link":
                    case "tertiary":
                        type = WayType.HIGHWAY_TERTIARY;
                        break;
                    case "residential":
                        type = WayType.HIGHWAY_RESIDENTAL;
                        break;
                    case "unclassified":
                        type = WayType.HIGHWAY_ROAD;
                        break;
                    case "service":
                        type = WayType.HIGHWAY_SERVICE;
                        break;
                    case "driveway":
                        type = WayType.HIGHWAY_DRIVEWAY;
                        break;
                    case "cycleway":
                        type = WayType.HIGHWAY_CYCLEWAY;
                        break;
                    case "bridleway":
                    case "footway":
                        type = WayType.HIGHWAY_FOOTWAY;
                        break;
                    case "steps":
                        type = WayType.HIGHWAY_STEPS;
                        break;
                    default:
                        type = WayType.WAY_UNDEFINED;
                        break;
                }
                break;
            case "building":
                type = WayType.BUILDING;
                switch (v) {
                    case "school":
                        break;
                    case "train_station":
                        type = WayType.TRAIN_STATION;
                        break;
                }
                break;
            case "landuse":
                switch(v) {
                    case "residential":
                        type = WayType.RESIDENTIAL;
                        break;
                    case "forest":
                        type = WayType.FOREST;
                        break;
                    case "industrial":
                        type = WayType.INDUSTRIAL;
                        break;
                    case "grass":
                        type = WayType.GRASS;
                        break;
                    case "retail":
                        type = WayType.RETAIL;
                        break;
                    case "military":
                        type = WayType.MILITARY;
                        break;
                    case "cemetery":
                        type = WayType.CEMETERY;
                        break;
                    case "orchard":
                        type = WayType.ORCHARD;
                        break;
                    case "allotments":
                        type = WayType.ALLOTMENTS;
                        break;
                    case "construction":
                        type = WayType.CONSTRUCTION;
                        break;
                    case "railway":
                        type = WayType.RAILWAY;
                        break;
                }
                break;
            case "natural":
                switch(v) {
                    case "water":
                        type = WayType.WATER;
                        break;
                    case "coastline":
                        type = WayType.COASTLINE;
                        break;
                    case "scrub":
                        type = WayType.SCRUB;
                        break;
                    case "wetland":
                        type = WayType.WETLAND;
                        break;
                    case "sand":
                        type = WayType.SAND;
                        break;
                }
                break;
            case "railway":
                switch(v) {
                    case "light_rail":
                        type = WayType.LIGHT_RAIL;
                        break;
                }
                break;
            case "man_made":
                switch(v) {
                    case "breakwater":
                        type = WayType.WAY_BREAKWATER;
                        break;
                    case "pier":
                        type = WayType.WAY_PIER;
                        break;
                    case "embankment":
                        type = WayType.WAY_EMBANKMENT;
                        break;
                }
                break;
            case "waterway":
                switch(v) {
                    case "stream":
                        type = WayType.WATER_STREAM;
                        break;
                    case "river":
                        type = WayType.WATER_RIVER;
                        break;
                }
                break;
            case "leisure":
                switch(v) {
                    case "stadium":
                        type = WayType.STADIUM;
                        break;
                    case "park":
                        type = WayType.PARK;
                        break;
                    case "playground":
                        type = WayType.PLAYGROUND;
                        break;
                    case "pitch":
                        type = WayType.PITCH;
                        break;
                }
                break;
        }

        return type;
    }

}
