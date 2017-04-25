package dk.itu.n.danmarkskort.gui.map.multithreading;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.multithreading.Task;

public class TileRenderTask extends Task {

	private Tile tile;
	private boolean repaintWhenDone = true;
	
	public TileRenderTask(Tile tile) {
		this.tile = tile;
	}
	
	public void work() {
		if(Main.tileController.getUselessTileKeys().contains(tile)) return;
		tile.render();
		if(repaintWhenDone) Main.mainPanel.repaint();
	}

	public void onRunStart() {}
	public void onRunEnd() {}

	public void setRepaintWhenDone(boolean repaintWhenDone) {
		this.repaintWhenDone = repaintWhenDone;
	}
	
}
