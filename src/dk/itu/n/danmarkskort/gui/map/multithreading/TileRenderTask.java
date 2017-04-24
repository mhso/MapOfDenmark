package dk.itu.n.danmarkskort.gui.map.multithreading;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.multithreading.Task;

public class TileRenderTask extends Task {

	private Tile tile;
	
	public TileRenderTask(Tile tile) {
		this.tile = tile;
	}
	
	public void work() {
		if(tile.render()) Main.tileController.hotswapUselesstile(tile);
	}

	public void onRunStart() {}
	public void onRunEnd() {}

}
