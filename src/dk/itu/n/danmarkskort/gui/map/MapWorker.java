package dk.itu.n.danmarkskort.gui.map;

import java.awt.Point;

import dk.itu.n.danmarkskort.Main;

public class MapWorker implements Runnable{

	private int index;
	private Point point;
	private BufferedMapManager manager;
	
	public MapWorker(BufferedMapManager manager, int index, Point point) {
		this.manager = manager;
		this.index = index;
		this.point = point;
	}
	
	public void run() {
		Main.log("Launched worker");
		BufferedMapImage image = createTile(index, point);
		manager.onWorkerFinished(this, index, image);
	}
	
	public BufferedMapImage createTile(int index, Point point) {
		BufferedMapImage image = new BufferedMapImage(manager, point);
		image.render();
		return image;
	}

}
