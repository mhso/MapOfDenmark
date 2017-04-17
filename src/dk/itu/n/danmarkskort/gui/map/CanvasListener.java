package dk.itu.n.danmarkskort.gui.map;

public interface CanvasListener {
	void onSetupDone();
	void onMouseMoved();
	void onZoomLevelChanged();
	void onZoom();
}
