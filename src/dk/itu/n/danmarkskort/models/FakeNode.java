package dk.itu.n.danmarkskort.models;

class FakeNode extends ParsedNode{

    static final int TOP = 0,
                     LEFT = 1,
                     BOTTOM = 2,
                     RIGHT = 3;
    private int side;

    FakeNode(float lon, float lat, int side) {
        super(lon, lat);
        this.side = side;
    }

    FakeNode(float lon, float lat) {
        super(lon, lat);
    }

    int getSide() { return side; }

    @Override
    public String toString() {
        return "lon: " + getLon() + ", lat: " + getLat() + ", side: " + getSide();
    }
}
