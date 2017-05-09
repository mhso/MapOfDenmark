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
	 * The factor that Theme line width and font size values get magnified by.
	 */
	public static final float LINE_MAGNIFYING_VALUE = 0.000001f, LINE_OUTLINE_WIDTH = 0.000005f, 
			FONTSIZE_MAGNIFYING_VALUE = 0.00008f, FONT_OUTLINE_WIDTH = 0.000012f;
	
	/**
	 * Standard size of KD-Tree leaves.
	 */
	public static final int KD_SIZE = 50;

	/**
	 * Longitude and Latitude bounds for the full Denmark .OSM file.
	 */
	public static final Region BOUNDS_DENMARK = new Region(7.7011D, 58.06239D, 15.65449D, 54.44065D);
	/**
	 * The factor that longitude coordinates gets multiplied by. This is because the world is spherical.
	 */
	private static final double AVERAGE_LATITUDE = BOUNDS_DENMARK.y1 + ((BOUNDS_DENMARK.y2 - BOUNDS_DENMARK.y1) / 2);
	public static final double FACTOR_LON_DENMARK = Math.cos( AVERAGE_LATITUDE / 180 * Math.PI);
	private static final double DENMARK_WIDTH  = (BOUNDS_DENMARK.x2 - BOUNDS_DENMARK.x1) * FACTOR_LON_DENMARK;
	public static final double MIN_SCALE = WINDOW_WIDTH / DENMARK_WIDTH;
    public static final double ZOOM_LOG_BASE = 1.47644;
    public static final int MAX_ZOOM = 20;
	public static final double MAX_SCALE = (MIN_SCALE * Math.pow(ZOOM_LOG_BASE, MAX_ZOOM - 1));
}
