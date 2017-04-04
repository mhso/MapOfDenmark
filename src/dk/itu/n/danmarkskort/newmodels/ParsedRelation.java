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

    public Path2D getPath() {
        Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
        if(members.size() > 0) {
            ArrayList<ParsedItem> outers = new ArrayList<>();
            Member current;
            ParsedNode firstNode = null;

            for(int i = 0; i < members.size(); i++) {
                current = members.get(i);
                if(current.isOuter()) {
                    outers.add(current.getReference());
                }
                else if(!current.isOuter()) {
                    path.append(members.get(i).getReference().getPath(), false);
                    if(!outers.isEmpty()) {
                        path.append(connectOuters(outers), false);
                        outers = new ArrayList<>();
                    }
                }
            }
            if(!outers.isEmpty()) path.append(connectOuters(outers), false);
        }
        return path;
    }

    public Path2D connectOuters(ArrayList<ParsedItem> outerList) {
        Path2D path = new Path2D.Float();
        path.append(outerList.get(0).getPath(), false);
        ParsedNode lastNode = outerList.get(0).getLastNode();
        if(outerList.size() > 1) {
            for(int j = 1; j < outerList.size(); j++) {
                for(int k = j; k < outerList.size(); k++ ) {
                    if(lastNode == outerList.get(k).getFirstNode()) {
                        path.append(outerList.get(k).getPath(), true);
                        lastNode = outerList.get(k).getLastNode();
                        Collections.swap(outerList, j, k);
                        break;
                    }
                    else if(lastNode == outerList.get(k).getLastNode()) {
                        ArrayList<ParsedNode> reversedNodes = new ArrayList<>(outerList.get(k).getNodes());
                        Collections.reverse(reversedNodes);
                        path.append(nodesToPath(reversedNodes), true);
                        lastNode = reversedNodes.get(reversedNodes.size() - 1);
                        Collections.swap(outerList, j, k);
                        break;
                    }
                    else if(k == outerList.size() - 1) {
                        path.append(outerList.get(j).getPath(), false);
                        lastNode = outerList.get(j).getLastNode();
                    }
                }
            }
        }
        return path;
    }

    public Path2D nodesToPath(ArrayList<ParsedNode> n) {
        Path2D path = new Path2D.Float();
        path.moveTo(n.get(0).getLon(), n.get(0).getLat());
        for(int i = 1; i < n.size(); i++) {
            path.lineTo(n.get(i).getLon(), n.get(i).getLat());
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
    
    public String toString() {
    	int nodeAmount = 0;
    	if(nodes != null) nodeAmount = nodes.size();

    	return "ParsedRelation [" + "id=" + id 	+ ", firstLon=" + getFirstNode().getLon() + ", firstLat=" 	+ getFirstNode().getLat() + ", nodeAmount=" + nodeAmount +
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
