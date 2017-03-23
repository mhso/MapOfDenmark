package dk.itu.n.danmarkskort.lightweight.models;

public class ParsedAddress extends ParsedItem{

    private String city, street, housenumber, postcode;
    private float lat, lon;

    public ParsedAddress(String city) {
        this.city = city;
    }

    public void setStreet(String street) { this.street = street; }
    public void setHousenumber(String housenumber) { this.housenumber = housenumber;}
    public void setPostcode(String postcode) { this.postcode = postcode; }
    public void setPoint(float lon, float lat) {
        this.lon = lon;
        this.lat = lat;
    }
}
