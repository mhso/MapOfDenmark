package dk.itu.n.danmarkskort.models;

public class ParsedHighway extends ParsedWay {

    private String name;
    private int maxSpeed;
    private boolean oneDirection;

    // Setters
    public void setName(String name) { this.name = ReuseStringObj.make(name); }
    public void setMaxSpeed(int maxSpeed) { this.maxSpeed = maxSpeed; }
    public void setOneDirection(boolean oneDirection) { this.oneDirection = oneDirection; }

    // Getters
    public String getName() { return name; }
    public int getMaxSpeed() { return maxSpeed; }
    public boolean isOneDirection() { return oneDirection; }
}
