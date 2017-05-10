package dk.itu.n.danmarkskort.models;

import java.io.Serializable;
import java.util.EnumMap;

import dk.itu.n.danmarkskort.kdtree.KDTree;

public class ParsedData implements Serializable {
	private static final long serialVersionUID = -3747249462669985249L;
	private float minLatBoundary, minLonBoundary, maxLatBoundary, maxLonBoundary, lonFactor;
	public EnumMap<WayType, KDTree<ParsedItem>> enumMapKD;
    public EnumMap<WayType, KDTree<ParsedPlace>> enumMapPlacesKD;
    
    public float getMinLon() {
    	return this.minLonBoundary;
    }
    
    public float getMaxLon() {
    	return this.maxLonBoundary;
    }
    
    public float getMinLat() {
    	return this.minLatBoundary;
    }
    
    public float getMaxLat() {
    	return this.maxLatBoundary;
    }
    
    public void setMinLon(float minLon) { minLonBoundary = minLon; }
    public void setMaxLon(float maxLon) { maxLonBoundary = maxLon; }
    public void setMinLat(float minLat) { minLatBoundary = minLat; }
    public void setMaxLat(float maxLat) { maxLatBoundary = maxLat; }
    
    public Region getMapRegion() {
    	float x1 = getMinLon();
    	float y1 = getMinLat();
    	float x2 = getMaxLon();
    	float y2 = getMaxLat();
    	return new Region(x1, y1, x2, y2);
    }

    public float getLonFactor() {
        return lonFactor;
    }
    
    public void setLonFactor(float lonFactor) { this.lonFactor = lonFactor; }
    
    public EnumMap<WayType, KDTree<ParsedItem>> getEnumMapKD() {
    	return enumMapKD;
    }
    
    public EnumMap<WayType, KDTree<ParsedPlace>> getEnumMapPlacesKD() {
    	return enumMapPlacesKD;
    }
}
