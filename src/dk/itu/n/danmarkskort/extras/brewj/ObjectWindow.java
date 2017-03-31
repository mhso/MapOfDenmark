package dk.itu.n.danmarkskort.extras.brewj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

public class ObjectWindow implements Observer {
	Model model;
	JFrame window;
	ObjectCanvas canvas;

	public ObjectWindow(Model model) {
		this.model = model;
		model.addObserver(this);
		window = new JFrame("BrewJ");
		window.setLayout(new BorderLayout());
		canvas = new ObjectCanvas(model);
		canvas.setPreferredSize(new Dimension(500, 500));
		window.add(canvas, BorderLayout.CENTER);
		window.pack();
		window.setVisible(true);
		new ObjectCanvasKeyController();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (window != null) window.repaint();
	}

	private class ObjectCanvasKeyController extends KeyAdapter {
		public ObjectCanvasKeyController() {
			window.addKeyListener(this);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_PAGE_DOWN:
					canvas.pan(0, -canvas.getHeight());
					break;
				case KeyEvent.VK_PAGE_UP:
					canvas.pan(0, canvas.getHeight());
					break;
			}
		}
	}

}
