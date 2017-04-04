package dk.itu.n.danmarkskort.newmodels;

import dk.itu.n.danmarkskort.Main;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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

    @Override
    public Path2D getPath() {
        Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
        if(members.size() > 0) {
            ArrayList<ParsedItem> outers = new ArrayList<>();
            Member current;
            ParsedNode firstNode = null;

            for(Member member : members) {
                current = member;
                if (current.isOuter()) {
                    outers.add(current.getReference());
                } else if (!current.isOuter()) {
                    path.append(member.getReference().getPath(), false);
                    if (!outers.isEmpty()) {
                        path.append(connectOuters(outers), false);
                        outers = new ArrayList<>();
                    }
                }
            }
            if(!outers.isEmpty()) path.append(connectOuters(outers), false);
        }
        return path;
    }

    @Override
    public Path2D getReversedPath() {
        return getPath();
        // This is not correct, but I don't think its an issue.
        // relations in relations is only something we have with busroutes and the likes
        // and that's not something we are displaying at the moment, and probably never will
    }

    private Path2D connectOuters(ArrayList<ParsedItem> outerList) {
        Path2D path = new Path2D.Float();
        path.append(outerList.get(0).getPath(), false);
        ParsedNode lastNode = outerList.get(0).getLastNode();
        if(outerList.size() > 1) {
            for(int i = 1; i < outerList.size(); i++) {
                for(int j = i; j < outerList.size(); j++ ) {
                    if(lastNode == outerList.get(j).getFirstNode()) {
                        path.append(outerList.get(j).getPath(), true);
                        lastNode = outerList.get(j).getLastNode();
                        Collections.swap(outerList, i, j);
                        break;
                    }
                    else if(lastNode == outerList.get(j).getLastNode()) {
                        path.append(outerList.get(j).getReversedPath(), true);
                        lastNode = outerList.get(j).getFirstNode();
                        Collections.swap(outerList, i, j);
                        break;
                    }
                    else if(j == outerList.size() - 1) {
                        path.append(outerList.get(i).getPath(), false);
                        lastNode = outerList.get(i).getLastNode();
                    }
                }
            }
        }
        return path;
    }

    @Override
    public ParsedNode getFirstNode() {
        if(nodes != null && nodes.size() > 0) return nodes.get(0);
        else if(members.size() > 0) return members.get(0).getReference().getFirstNode();
        return null;
    }

    @Override
    public ParsedNode getLastNode() {
        if(nodes != null && nodes.size() > 0) return nodes.get(nodes.size() - 1);
        else if(members.size() > 0) return members.get(0).getReference().getLastNode();
        return null;
    }

    @Override
    public void appendParsedItem(ParsedItem item) {
        members.add(new Member(item, false));
    }
    
    public String toString() {
    	int nodeAmount = 0;
    	if(nodes != null) nodeAmount = nodes.size();
    	return "ParsedRelation [" + "id=" + id 	+ ", firstLon=" + getFirstNode().getLon() + ", firstLat=" 	+ getFirstNode().getLat() + ", nodeAmount=" + nodeAmount +
    			", itemAmount=" + members.size() + "]";
    }

    public class Member {
        private ParsedItem reference;
        private boolean isOuter;

        Member(ParsedItem reference, boolean isOuter) {
            this.reference = reference;
            this.isOuter = isOuter;
        }

        ParsedItem getReference() { return reference; }
        boolean isOuter() { return isOuter; }
    }
}
