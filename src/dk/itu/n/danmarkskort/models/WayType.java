package dk.itu.n.danmarkskort.models;

public enum WayType {
	UNDEFINED, 			// Udefinerbar.
	
	BUILDING, 			// Bygning.
	COASTLINE, 			// Kystlinje.
	FOREST, 			// Skov.
	GRASS,				// Græs.
	FARMLAND,			// Landbrug.
	PARK,				// Park.
	ORCHARD,			// Plantage.
	INDUSTRIAL,			// Industriområde.
	ROUTE_FERRY,		// Færgeroute.
	WETLAND,			// Vådområde.
	SPORT,				// Torturområde.
	PLAYGROUND,			// Legeplads.
	ALLOTMENTS,			// Koloni
	SAND,				// Sand.
	CONSTRUCTION,		// Byggeplads.
	TRAIN_STATION,		// Togstation.
	SCRUB,				// Krat.
	RETAIL,				// Købe-ting-sted.
	MILITARY,			// Militærområde.
	CEMETERY,			// Kirkegård.
	STADIUM,
	
	WAY_BREAKWATER, 	// Bølgebryder.
	WAY_PIER,			// Pier.
	WAY_POWER_LINE,		// Strømkabel.
	WAY_EMBANKMENT,		// Dæmning.
	HEDGE,				// Hæk.

	HIGHWAY_MOTORWAY,	// Motorvej
	HIGHWAY_TRUNK, 		// Motor-trafikvej.
	HIGHWAY_PRIMARY,	// Vej. ??
	HIGHWAY_SECONDARY,	// Vej. ??
	HIGHWAY_TERTIARY,	// Vej. ??
	HIGHWAY_DRIVEWAY,	// Indkørsel.
	HIGHWAY_RESIDENTIAL, // Vej i beboerområde.
	HIGHWAY_CYCLEWAY,	// Cykelsti.
	HIGHWAY_FOOTWAY,	// Gangsti.
	HIGHWAY_STEPS,		// Trappe.
	HIGHWAY_SERVICE,	// serviceveje
	HIGHWAY_UNDEFINED,
    HIGHWAY_UNCLASSIFIED, // mindre veje, ikke det samme som undefined
	HIGHWAY_PATH,
	HIGHWAY_TRACK,

	WATER_STREAM,		// Bæk.
	WATER_DITCH,		// Grøft.
	WATER_DITCH_TUNNEL, // Grøft-tunnel.
	WATER_RIVER,		// Flod.
    LIGHT_RAIL,
	PITCH,
	RAILWAY,
	RESIDENTIAL,
	BUILDING_SCHOOL,
	PEDESTRIAN,
    PARKING, PLATFORM, WOOD, ROOF, WATER				// Almindeligt vand.
	
}
