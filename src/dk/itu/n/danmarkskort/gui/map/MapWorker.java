package dk.itu.n.danmarkskort.gui.map;

import java.util.ArrayList;

import dk.itu.n.danmarkskort.Main;


public class MapWorker implements Runnable{

	private ArrayList<BufferedMapImage> queue = new ArrayList<BufferedMapImage>();
	private boolean isRunning = false;
	private boolean clearAfterNext = false;
	
	public void addToQueue(BufferedMapImage image) {
		if(!queue.contains(image)) queue.add(image);
	}
	
	public void clearQueue() {
		if(isRunning) clearAfterNext = true;
		else queue.clear();
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public void run() {
		isRunning = true;
		while(Main.map.scaleCurrentLayer) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		while(queue.size() > 0) {
			queue.get(0).render();
			Main.mainPanel.repaint();
			if(clearAfterNext) {
				clearAfterNext = false;
				queue.clear();
			} else {
				queue.remove(0);
			}
		}
		
		
		isRunning = false;
		
	}

}
