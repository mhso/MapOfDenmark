package dk.itu.n.danmarkskort.extras.brewj;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.function.Consumer;

public class Model extends Observable {
    Map<Object,ObjectView> objectViews = new IdentityHashMap<>();
    List<ObjectView> drawOrder = new LinkedList<>();

    public synchronized void add(Object obj, double x, double y) {
		if (!objectViews.containsKey(obj)) {
			ObjectView view = new ObjectView(obj, x, y, this);
			objectViews.put(obj, view);
			drawOrder.add(view);
			setDirty();
		}
    }

    public synchronized ObjectView get(Point2D p) {
    	ListIterator<ObjectView> it = drawOrder.listIterator(drawOrder.size());
    	while (it.hasPrevious()) {
    		ObjectView view = it.previous();
            if (view.contains(p)) {
                it.remove();
                drawOrder.add(view);
            	return view;
            }
        }
        return null;
    }

    public synchronized void forEach(Consumer<ObjectView> c) {
    	for (ObjectView objectView : drawOrder) {
    		c.accept(objectView);
	    }
    }

	public void setDirty() {
    	setChanged();
    	notifyObservers();
	}

	public synchronized void remove(Object obj) {
    	drawOrder.remove(objectViews.remove(obj));
    	setDirty();
	}
}
