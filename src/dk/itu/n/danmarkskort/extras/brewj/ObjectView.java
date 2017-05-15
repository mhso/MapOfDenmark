package dk.itu.n.danmarkskort.extras.brewj;

import java.awt.*;
import java.awt.geom.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ObjectView {
	Model model;
    Object obj;
    List<Field> fields;
    double x, y;
    double w, h;
    double maxwidth;


    public ObjectView(Object obj, double x, double y, Model model) {
        this.obj = obj;
        this.x = x;
        this.y = y;
        this.h = height();
        this.model = model;
    }

    public boolean contains(Point2D p) {
        return p.getX() >= x && p.getX() <= x + w && p.getY() >= y && p.getY() <= y + h;
    }

    public void pan(double dx, double dy) {
        x += dx;
        y += dy;
    }

    List<Field> getNonStaticFields(Class<?> klass) {
    	if (fields == null) {
			if (klass == Object.class) {
				fields = new ArrayList<>();
			} else {
				fields = getNonStaticFields(klass.getSuperclass());
				for (Field field : klass.getDeclaredFields()) {
			 		if (!Modifier.isStatic(field.getModifiers())) {
			 			field.setAccessible(true);
		 				fields.add(field);
					}
				}
			}
		}
        return fields;
    }

    public Object select(Point2D point) {
	    try {
		    if (point.getX() < x || x + w < point.getX()) return null;
		    int index = (int) ((point.getY() - y) / 20);
		    if (index == 0) return this;
		    Class<?> klass = obj.getClass();
		    if (klass.isArray()) {
			    if (index == 1 || klass.getComponentType().isPrimitive()) return null;
			    return Array.get(obj, index-2);
		    } else {
			    for (Field field : getNonStaticFields(klass)) {
				    int modifier = field.getModifiers();
				    if (Modifier.isStatic(modifier)) continue;
				    if (index-- == 1) {
				    	if (field.getType().isPrimitive()) return null;
				    	return field.get(obj);
				    }
			    }
		    }
	    } catch (IllegalAccessException e) {
		    e.printStackTrace();
	    }
	    return null;
    }

    double height() {
        if (h == 0) {
            if (obj.getClass().isArray()) {
                h = 20 * Array.getLength(obj) + 40;
            } else {
                h = 20 + 20 * (int) getNonStaticFields(obj.getClass()).stream()
                        .filter(f -> !Modifier.isStatic(f.getModifiers()))
                        .count();
            }
        }
        return h;
    }

    public void paintRefs(Graphics2D g, Rectangle2D viewrect) {
        try {
            g.setColor(Color.BLACK);
            Class<?> klass = obj.getClass();
            int offset = 30;
            if (klass.isArray()) {
                int length = Array.getLength(obj);
	            offset += 20;
                for (int i = 0 ; i < length ; i++) {
                    Object elm = Array.get(obj, i);
                    ObjectView otherView = model.objectViews.get(elm);
                    if (otherView != null) {
	                    drawRef(g, otherView, y + offset, viewrect);
                    }
                    offset += 20;
                }
            } else {
                for (Field field : getNonStaticFields(klass)) {
                    Object elm = field.get(obj);
	                ObjectView otherView = model.objectViews.get(elm);
                    if (otherView != null) {
	                    drawRef(g, otherView, y + offset, viewrect);
                    }
                    offset += 20;
                }
            }
            g.draw(new Rectangle2D.Double(x, y, w, h));
            g.draw(new Line2D.Double(x, y+20, x+w, y+20));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

	void drawRef(Graphics2D g, ObjectView otherView, double y1, Rectangle2D viewrect) {
    	double x1, dx, dy, x2, y2;
    	x2 = otherView.x + otherView.w / 2;
    	if (x + w / 2 < otherView.x + otherView.w / 2) {
    		x1 = x + w;
    		dx = -10;
	    } else {
    		x1 = x;
    		dx = 10;
	    }
	    if (y1 < otherView.y + otherView.h / 2) {
			y2 = otherView.y;
			dy = 10;
	    } else {
    		y2 = otherView.y + otherView.h;
    		dy = -10;
	    }
	    g.draw(new Line2D.Double(x1, y1, x2+dx, y1));
    	g.draw(new QuadCurve2D.Double(x2+dx,y1, x2, y1, x2, y1+dy));
		g.draw(new Line2D.Double(x2, y1+dy, x2, y2));
		Path2D arrow = new Path2D.Double();
		arrow.moveTo(x2-5,y2 - dy);
		arrow.lineTo(x2,y2);
		arrow.lineTo(x2+5,y2 - dy);
		arrow.closePath();
		g.fill(arrow);
	}

	private int drawStringGetWidth(Graphics2D g, String s, double x, double y, Rectangle2D viewrect) {
    	if (y < viewrect.getMinY() || y - 20 > viewrect.getMaxY()) return 0;
    	int width = g.getFontMetrics().stringWidth(s);
    	if (x + width < viewrect.getMinX() || x > viewrect.getMaxX()) return width;
    	g.drawString(s, (float)x, (float)y);
		return width;
    }

	private void paintEntry(Graphics2D g, String name, Object obj, Class<?> klass, double x, double y, Rectangle2D viewrect) {
    	double linex = x + 10;
    	if (obj == null) {
		    linex += drawStringGetWidth(g, name + ": null", linex, y + 15, viewrect);
	    } else {
    		if (klass.isPrimitive()) {
    			if (klass == char.class) {
				    linex += drawStringGetWidth(g, name + ": \'" + obj + "\'", linex, y + 15, viewrect);
			    } else {
				    linex += drawStringGetWidth(g, name + ": " + obj, linex, y + 15, viewrect);
			    }
		    } else {
			    linex += drawStringGetWidth(g, name + ": ", linex, y + 15, viewrect);
			    ObjectView otherView = model.objectViews.get(obj);
			    if (otherView != null) {
				    if (x + w / 2 < otherView.x + otherView.w / 2) {
					    g.fill(new Ellipse2D.Double(linex + 3, y + 8, 4, 4));
					    g.draw(new Line2D.Double(linex + 5, y + 10, x + w, y + 10));
				    } else {
					    g.fill(new Ellipse2D.Double(x + 3, y + 8, 4, 4));
					    g.draw(new Line2D.Double(x + 5, y + 10, x, y + 10));
				    }
					linex += 10;
			    } else {
				    g.fill(new Ellipse2D.Double(linex + 3, y + 8, 4, 4));
				    g.draw(new Line2D.Double(linex + 5, y + 10, linex+10, y + 10));
				    Path2D arrow = new Path2D.Double();
				    arrow.moveTo(linex+10, y+5);
				    arrow.lineTo(linex+20, y+10);
				    arrow.lineTo(linex+10, y+15);
				    arrow.closePath();
				    g.fill(arrow);
				    linex += 25;
				    g.setColor(Color.GRAY);
				    klass = obj.getClass();
				    if (klass == String.class) {
					    linex += drawStringGetWidth(g, "\"" + obj + "\"", linex, y + 15, viewrect);
				    } else if (klass == Character.class) {
					    linex += drawStringGetWidth(g, "\'" + obj + "\'", linex, y + 15, viewrect);
				    } else if (klass == Double.class || klass == Float.class || klass == Long.class ||
						    klass == Integer.class || klass == Short.class ||
						    klass == Byte.class || klass == Boolean.class) {
					    linex += drawStringGetWidth(g, obj.toString(), linex, y + 15, viewrect);

				    }
				    g.setColor(Color.BLACK);
			    }
		    }
	    }
	    w = Math.max(w, linex + 5 - x);
    	maxwidth = Math.max(maxwidth, w);
    }

	void paintFrame(Graphics2D g, Rectangle2D viewrect) {
        try {
			int minindex = (int) Math.floor((viewrect.getMinY() - y - 20) / 20);
			int maxindex = (int) Math.ceil((viewrect.getMaxY() - y - 20) / 20);
            g.setColor(Color.WHITE);
            g.fill(new Rectangle2D.Double(x, y, w, h));
            g.setColor(Color.BLACK);
            Class<?> klass = obj.getClass();
            g.setColor(new Color(187, 197,255));
			g.fill(new Rectangle2D.Double(x, y, w, 20));
			g.setColor(Color.BLACK);
			w = Math.max(w, 5 + drawStringGetWidth(g, klass.getSimpleName(), x + 5, y + 15, viewrect) + 20);
			maxwidth = Math.max(maxwidth, w);
            g.draw(new Rectangle2D.Double(x+w-15, y+5, 10, 10));
			g.draw(new Line2D.Double(x+w-13,y+7,x+w-7,y+13));
			g.draw(new Line2D.Double(x+w-7,y+7,x+w-13,y+13));
            int offset = 20;
            if (klass.isArray()) {
	            int length = Array.getLength(obj);
            	paintEntry(g, "length", length, int.class, x, y+offset, viewrect);
                offset += 20;
                minindex = Math.max(0, minindex-1);
                maxindex = Math.min(maxindex-1, length);
                offset += 20 * minindex;
                for (int i = minindex ; i < maxindex ; i++) {
                    paintEntry(g, "["+i+"]", Array.get(obj, i), klass.getComponentType(), x, y+offset, viewrect);
                    offset += 20;
                }
            } else {
            	minindex = Math.max(0, minindex);
            	maxindex = Math.min(fields.size(), maxindex);
				offset += 20 * minindex;
            	for (int i = minindex ; i < maxindex ; i++) {
                	Field field = fields.get(i);
	                paintEntry(g, field.getName(), field.get(obj), field.getType(), x, y + offset, viewrect);
	                offset += 20;
                }
            }
            g.draw(new Rectangle2D.Double(x, y, w, h));
            g.draw(new Line2D.Double(x, y+20, x+w, y+20));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
