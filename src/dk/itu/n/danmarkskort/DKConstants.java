package dk.itu.n.danmarkskort;

import dk.itu.n.danmarkskort.models.Region;

public class DKConstants {
	/**
	 * The File Path for User Preference Bin File, where things like Default Map files, Current Map Theme etc. lies.
	 */
	public final static String USERPREF_PATH = "userpref.bin";
	
	/**
	 * Determines the default size of Main Window.
	 */
	public final static int WINDOW_WIDTH = 1000, WINDOW_HEIGHT = 800;
	
	/**
	 * The factor that Theme line width values gets magnified by.
	 */
	public static final float LINE_MAGNIFYING_VALUE = 0.000001f, LINE_OUTLINE_WIDTH = 0.000005f;
	
	/**
	 * Standard size of KD-Tree leaves.
	 */
	public static final int KD_SIZE = 1;
	
	/**
	 * Longitude and Latitude bounds for the full Denmark .OSM file.
	 */
	public static Region BOUNDS_DENMARK = new Region(7.7011D, 58.06239D, 15.65449D, 54.44065D);
	/**
	 * The factor that longitude coordinates gets multiplied by. This is because the world is spherical.
	 */
	public static final float FACTOR_LON_DENMARK = (float)(Math.cos((BOUNDS_DENMARK.y1 + (BOUNDS_DENMARK.y2 - BOUNDS_DENMARK.y1) / 2)) / 180 * Math.PI);
}
