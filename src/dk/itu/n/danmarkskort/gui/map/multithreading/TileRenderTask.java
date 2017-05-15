package dk.itu.n.danmarkskort.gui.map.multithreading;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.multithreading.Task;

public class TileRenderTask extends Task {

	private Tile tile;
	private boolean repaintWhenDone = true;
	private boolean swapWhenDone = false;
	
	/**See the documentation for Task to understand what is going on here.<br>
	 * This task will render a specified Tile.*/
	public TileRenderTask(Tile tile) {
		this.tile = tile;
	}
	
	/**This will render the tile*/
	public void work() {
		if(Main.tileController.isBlurred() || tile.isUseless()) return;
		tile.render();
		if(swapWhenDone) Main.tileController.swapTileWithUselessTile(tile);
		if(repaintWhenDone) Main.mainPanel.repaint();
	}

	/**This will set if the tile should repaint the map when it is done rendering*/
	public void setRepaintWhenDone(boolean repaintWhenDone) {
		this.repaintWhenDone = repaintWhenDone;
	}
	
	/**This will set if the tile should swap with a useless tile when it is done rendering*/
	public void setSwapWhenDone(boolean swapWhenDone) {
		this.swapWhenDone = swapWhenDone;
	}
	
	public void onRunStart() {}
	public void onRunEnd() {}
	
}
