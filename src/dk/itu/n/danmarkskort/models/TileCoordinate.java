package dk.itu.n.danmarkskort.models;

import java.io.Serializable;

public class TileCoordinate implements Serializable{

	private static final long serialVersionUID = 9037552041293769664L;
	private int x, y;
	
	public TileCoordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {return this.x;}
	public int getY() {return this.y;}
	public void setX(int x) {this.x = x;}
	public void setY(int y) {this.y = y;}
	
}
