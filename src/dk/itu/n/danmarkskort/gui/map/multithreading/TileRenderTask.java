package dk.itu.n.danmarkskort.gui.map.multithreading;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.multithreading.Task;

public class TileRenderTask extends Task {

	private Tile tile;
	private boolean repaintWhenDone = true;
	private boolean swapWhenDone = false;
	
	public TileRenderTask(Tile tile) {
		this.tile = tile;
	}
	
	public void work() {
		tile.render();
		if(swapWhenDone) Main.tileController.swapTileWithUselessTile(tile);
		if(repaintWhenDone) Main.mainPanel.repaint();
	}

	public void onRunStart() {}
	public void onRunEnd() {}

	public void setRepaintWhenDone(boolean repaintWhenDone) {
		this.repaintWhenDone = repaintWhenDone;
	}
	
	public void setSwapWhenDone(boolean swapWhenDone) {
		this.swapWhenDone = swapWhenDone;
	}
	
}
