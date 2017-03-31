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
	
	WAY_UNDEFINED,		// Udefinerbar vej.
	WAY_TRACK,			// Spor.
	WAY_PATH,			// Sti.
	WAY_SERVICE,    	// Parkeringssti.
	WAY_BREAKWATER, 	// Bølgebryder.
	WAY_PIER,			// Pier.
	WAY_POWER_LINE,		// Strømkabel.
	WAY_EMBANKMENT,		// Dæmning.
	HEDGE,				// Hæk.
	
	HIGHWAY_PRIMARY,	// Vej. ??
	HIGHWAY_SECONDARY,	// Vej. ??
	HIGHWAY_TERTIARY,	// Vej. ??
	HIGHWAY_DRIVEWAY,	// Indkørsel.
	HIGHWAY_RESIDENTIAL, // Vej i beboerområde.
	HIGHWAY_TRUNK, 		// Motor-trafikvej.
	HIGHWAY_MOTORWAY,	// Motorvej
	HIGHWAY_CYCLEWAY,	// Cykelsti.
	HIGHWAY_FOOTWAY,	// Gangsti.
	HIGHWAY_STEPS,		// Trappe.
	HIGHWAY_ROAD,		// mindre landevej. Fremgår vist som unclassified i .osm
	HIGHWAY_SERVICE,	// serviceveje
	
	WATER_STREAM,		// Bæk.
	WATER_DITCH,		// Grøft.
	WATER_DITCH_TUNNEL, // Grøft-tunnel.
	WATER_RIVER,		// Flod.
    LIGHT_RAIL, PITCH, RAILWAY, RESIDENTIAL, WATER				// Almindeligt vand.
	
}
