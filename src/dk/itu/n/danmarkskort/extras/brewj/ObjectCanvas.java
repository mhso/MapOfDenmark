package dk.itu.n.danmarkskort.extras.brewj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;

public class ObjectCanvas extends JComponent implements Observer {
	Model model;
	AffineTransform transform = new AffineTransform();

	public ObjectCanvas(Model model) {
		this.model = model;
		model.addObserver(this);
		new ObjectCanvasMouseController();
	}

	@Override
	protected void paintComponent(Graphics _g) {
		Graphics2D g = (Graphics2D) _g;
		Rectangle2D viewrect = null;
		try {
			viewrect = transform.createInverse().createTransformedShape(new Rectangle2D.Double(0,0,getWidth(),getHeight())).getBounds2D();
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		model.forEach(objectView -> objectView.w = 20);
		g.setTransform(transform); // first round of draw must be with correct transform
		Rectangle2D finalViewrect = viewrect;
		model.forEach(objectView -> objectView.paintFrame(g, finalViewrect)); // first call sets correct width;
		g.setTransform(new AffineTransform());
		g.setColor(new Color(255, 221, 180));
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);
		g.setTransform(transform);
		model.forEach(objectView -> objectView.paintRefs(g, finalViewrect));
		model.forEach(objectView -> objectView.paintFrame(g, finalViewrect));
	}

	public void pan(double dx, double dy) {
		transform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
		repaint();
	}

	public void zoom(double factor) {
		transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
		repaint();
	}

	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}

	public Point2D toModelCoords(Point2D lastMousePosition) {
		try {
			return transform.inverseTransform(lastMousePosition, null);
		} catch (NoninvertibleTransformException e) {
			throw new RuntimeException(e);
		}
	}


	private class ObjectCanvasMouseController extends MouseAdapter {
		Point2D lastMousePosition;
		ObjectView draggedObjectView;

		public ObjectCanvasMouseController() {
			addMouseListener(this);
			addMouseMotionListener(this);
			addMouseWheelListener(this);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			lastMousePosition = e.getPoint();
			Point2D mouseInModel = toModelCoords(lastMousePosition);
			ObjectView objectView = model.get(mouseInModel);
			if (objectView != null) {
				if (Math.max(Math.abs(objectView.x + objectView.w - 10 - mouseInModel.getX()),
						Math.abs(objectView.y + 10 - mouseInModel.getY())) <= 4) {
					model.remove(objectView.obj);
				} else {
					Object select = objectView.select(mouseInModel);
					if (select == objectView) {
						draggedObjectView = objectView;
					} else if (select != null) {
						double x = objectView.x + objectView.w + 50;
						double y = Math.round((mouseInModel.getY() - objectView.y - 10) / 20) * 20 + 30 + objectView.y;
						model.add(select, x, y);
					}
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			draggedObjectView = null;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			Point2D currentMousePosition = e.getPoint();
			double dx = currentMousePosition.getX() - lastMousePosition.getX();
			double dy = currentMousePosition.getY() - lastMousePosition.getY();
			double zoom = transform.getScaleX();
			if (draggedObjectView != null) {
				draggedObjectView.pan(dx/zoom, dy/zoom);
				repaint();
			} else {
				pan(dx, dy);
			}
			lastMousePosition = currentMousePosition;
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			double factor = Math.pow(0.9, e.getWheelRotation());
			Point2D currentMousePosition = e.getPoint();
			double dx = currentMousePosition.getX();
			double dy = currentMousePosition.getY();
			pan(-dx, -dy);
			zoom(factor);
			pan(dx, dy);
		}
	}
}
