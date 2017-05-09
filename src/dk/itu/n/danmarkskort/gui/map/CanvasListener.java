package dk.itu.n.danmarkskort.gui.map;

/** An Interfaces that classes can implement to receive events from the MapCanvas. 
 *  We used this approach instead of Observers/Observable, because this gives you more freedom and more 
 *  options. */
public interface CanvasListener {
	/** Called when the Canvas setup is done and the Canvas is visible and ready to be used. */
	void onSetupDone();
	/** Called when the Mouse moves over the Canvas. */
	void onMouseMoved();
	/** Called when the zoom level changes, this is not called when the user zooms, only when the actual
	 *  level changes (the levels range from 1, fully zoomed out, to 20, fully zoomed in) */
	void onZoomLevelChanged();
	/** Called when the Canvas zoom changes, regardless of zoom levels changing. */
	void onZoom();
}
