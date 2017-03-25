package dk.itu.n.danmarkskort.lightweight.models;

public class ParsedAddress{

    private String city, street, housenumber, postcode;
    private float[] coords;

    public void setCity(String c) { city = c; }
    public void setPostcode(String p) { postcode = p; }
    public void setStreet(String s) { street = s; }
    public void setHousenumber(String h) { housenumber = h; }
    public void setCoords(float[] c) { coords = c;}

    public String getCity() { return city; }
    public String getStreet() { return street; }
    public String getHousenumber() { return housenumber; }
    public String getPostcode() { return postcode; }
    public float[] getCoords() { return coords; }
}
