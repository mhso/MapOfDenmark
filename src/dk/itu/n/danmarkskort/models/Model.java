package dk.itu.n.danmarkskort.models;

import java.awt.*;

public abstract class Model {
    private String name;
    private Shape shape;

    public void addShape(Shape shape) { this.shape = shape; }
    public void addName(String name) { this.name = name; }

    public String getName() { return name; }
    public Shape getShape() { return shape; }
}
