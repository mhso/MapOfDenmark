package dk.itu.n.danmarkskort.models;

public enum WayType {
	// Highway:
	HIGHWAY_MOTORWAY,	// Motorvej
	HIGHWAY_TRUNK, 		// Motor-trafikvej.
	HIGHWAY_PRIMARY,	// Vej. ??
	HIGHWAY_SECONDARY,	// Vej. ??
	HIGHWAY_TERTIARY,	// Vej. ??
	HIGHWAY_RESIDENTIAL, // Vej i beboerområde.
	HIGHWAY_CYCLEWAY,	// Cykelsti.
	HIGHWAY_FOOTWAY,	// Gangsti.
	HIGHWAY_SERVICE,	// serviceveje
	HIGHWAY_UNDEFINED,	// udefineret vej
	HIGHWAY_UNCLASSIFIED, // mindre veje, ikke det samme som undefined
	PEDESTRIAN_AREA,	// gå-gade-område
	HIGHWAY_PEDESTRIAN,	// gå-gade

	// Landuse:
	RESIDENTIAL,		// beboelsesområde
	FOREST, 			// Skov.
	INDUSTRIAL,			// Industriområde.
	GRASS,				// Græs.
	RETAIL,				// Købe-ting-sted.
	MILITARY,			// Militærområde.
	CEMETERY,			// Kirkegård.
	ORCHARD,			// Plantage.
	ALLOTMENTS,			// Kolonihaver
	CONSTRUCTION,		// Byggeplads.
	RAILWAY,			// tog-område

	// Leisure:
	STADIUM,			// stadion
	PARK,				// Park.
	PLAYGROUND,			// Legeplads.
	SPORT,				// Torturområde.

	// Building:
	BUILDING, 			// Bygning.
	BUILDING_SCHOOL,	// skole
	TRAIN_STATION,		// Togstation.
	ROOF,				// tag

	// Natural:
	WATER,				// Almindeligt vand.
	COASTLINE, 			// Kystlinje.
	SCRUB,				// Krat.
	WOOD,				// træer
	WETLAND,			// Vådområde.
	SAND,				// Sand.

	// Amenity:
	PARKING,			// parkering

	// Man_made:
	BREAKWATER,			// bølgebryder
	PIER,				// mole
	EMBANKMENT,			// dæmning

	// Waterway:
	WATER_STREAM,		// Bæk.
	WATER_RIVER,		// Flod.

	// Railway:
	RAIL,				// intercity tog (vidst nok).
	LIGHT_RAIL,			// s-tog, og lignende tog
	PLATFORM,			// tog-perron

	// Ikke i brug lige nu, af forskellige grunde
	FARMLAND,			// Landbrug.
	//ROUTE_FERRY,		// Færgeroute.
	//HEDGE,				// Hæk.
	//HIGHWAY_PATH,		// gangsti?
	//HIGHWAY_TRACK,		// Vandrerute?
	//HIGHWAY_STEPS,		// Trappe.
	//WATER_DITCH,		// Grøft.
	//WATER_DITCH_TUNNEL, // Grøft-tunnel.
	PITCH,
	HIGHWAY_TRACK,
	HIGHWAY_PATH,
	//UNDEFINED 			// Udefinerbar.

	PLACE_TOWN,				// En by
	PLACE_SUBURB			// En forstad
}
