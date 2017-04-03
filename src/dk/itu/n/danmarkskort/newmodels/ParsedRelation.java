package dk.itu.n.danmarkskort.newmodels;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collections;

public class ParsedRelation extends ParsedItem {

    private long id;
    private ArrayList<ParsedNode> nodes;
    private ArrayList<Member> members;
    private Shape shape;

    public ParsedRelation(long id) {
        this.id = id;
        nodes = new ArrayList<>();
        members = new ArrayList<>();
    }

    public void addNode(ParsedNode node) { nodes.add(node); }
    public void addMember(ParsedItem reference, boolean isOuter) { members.add(new Member(reference, isOuter)); }

    public long getID() { return id; }

    public ArrayList<Member> getMembers() { return members; }

    public ArrayList<ParsedNode> getNodes() {
        ArrayList<ParsedNode> nodeList = new ArrayList<>();
        if(members.size() > 0) {
            for(Member member : members) nodeList.addAll(member.getReference().getNodes());
        } else if(nodes != null) {
            return nodes;
        }
        return nodeList;
    }

    public Path2D getPath() {
        Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
        if(members.size() > 0) {
            ParsedNode lastNode = members.get(0).getReference().getLastNode();
            path.append(members.get(0).getReference().getPath(), true);
            for(int i = 1; i < members.size(); i++) {
                ParsedItem current = members.get(i).getReference();
                if(lastNode != current.getFirstNode()) {
                    Collections.reverse(current.getNodes());
                }
                if(lastNode == current.getFirstNode()) {
                    path.append(current.getPath(), true);
                } else {
                    path.append(current.getPath(), false);
                }
                lastNode = current.getLastNode();
            }
        }
        return path;
    }

    @Override
    public ParsedNode getFirstNode() { return null;}

    @Override
    public ParsedNode getLastNode() { return null; }

    @Override
    public float getFirstLon() {
        if(nodes != null && nodes.size() > 0) return nodes.get(0).getLon();
        else if(members.size() > 0) return members.get(0).getReference().getFirstLon();
        return -1;
    }

    @Override
    public float getFirstLat() {
        if(nodes != null && nodes.size() > 0) return nodes.get(0).getLat();
        else if(members.size() > 0) return members.get(0).getReference().getFirstLat();
        return -1;
    }
    
    public String toString() {
    	int nodeAmount = 0;
    	if(nodes != null) nodeAmount = nodes.size();
    	return "ParsedRelation [" + "id=" + id 	+ ", firstLon=" + getFirstLon() + ", firstLat=" 	+ getFirstLat() + ", nodeAmount=" + nodeAmount + 
    			", itemAmount=" + members.size() + "]";
    }

    public class Member {
        private ParsedItem reference;
        private boolean isOuter;

        public Member(ParsedItem reference, boolean isOuter) {
            this.reference = reference;
            this.isOuter = isOuter;
        }

        public ParsedItem getReference() { return reference; }
        public boolean isOuter() { return isOuter; }
    }
}
