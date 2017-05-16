package dk.itu.n.danmarkskort.kdtree.unittests;

import java.awt.geom.Point2D;

import dk.itu.n.danmarkskort.kdtree.KDComparable;
import dk.itu.n.danmarkskort.kdtree.KDTree;
import dk.itu.n.danmarkskort.kdtree.KDTreeNode;

public class NearestNeighborTestTree<T extends KDComparable> {
	private String resultString = "";
	
	/**
     * Finds the element, among all the objects stored in the KDTree, that has a line segment with the shortest
     * distance to the query point.
     *
     * @param query The query point used to match against.
     * @param currentShortest The shortest known distance (from a different part of the KDTree).
     * @param sortByLon Whether this depth is ordered according to longitude (true) or latitude (false) values.
     * @return Null if no element in this part of the tree can possibly be closer to the query point, than the already
     * known shortestDistance, or the method didn't find a candidate with shorter distance.
     * Else returns the element that is closest to the query point.
     */
    public T nearest(Point2D.Float query, double currentShortest, boolean sortByLon, KDTreeNode<T> tree) {
        if(query == null) {
        	resultString += "1T";
        	return null;
        }
        resultString += "1F";

        double nearestPossibleLT, nearestPossibleRB;
        double shortest = currentShortest;
        T candidate = null;

        // calculate the nearest possible candidate from either side
        if(sortByLon) {
        	resultString += "2T";
            nearestPossibleLT = (query.x < tree.getLeftSplit()) ? 0 : KDTree.calcDistance(query, new Point2D.Float(tree.getLeftSplit(), query.y));
            if(nearestPossibleLT == 0) resultString += "3T";
            else resultString += "3F";
            nearestPossibleRB = (query.x > tree.getRightSplit()) ? 0 : KDTree.calcDistance(query, new Point2D.Float(tree.getRightSplit(), query.y));
            if(nearestPossibleRB == 0) resultString += "4T";
            else resultString += "4F";
        } else {
        	resultString += "2F";
            nearestPossibleLT = (query.y < tree.getLeftSplit()) ? 0 : KDTree.calcDistance(query, new Point2D.Float(query.x, tree.getLeftSplit()));
            if(nearestPossibleLT == 0) resultString += "5T";
            else resultString += "5F";
            nearestPossibleRB = (query.y > tree.getRightSplit()) ? 0 : KDTree.calcDistance(query, new Point2D.Float(query.x, tree.getRightSplit()));
            if(nearestPossibleRB == 0) resultString += "6T";
            else resultString += "6F";
        }

        // if no possible candidate has shorter path than det one already known, abort the operation
        if(shortest < nearestPossibleLT && shortest < nearestPossibleRB) {
        	resultString += "7T";
        	return null;
        }
        resultString += "7F";

        // if query point is inside the left/top
        if(nearestPossibleLT == 0 && tree.getLeftChild() != null) {
        	resultString += "8T";
            T leftCandidate = tree.getLeftChild().nearest(query, shortest, !sortByLon);
            if(leftCandidate != null) {
            	resultString += "9T";
                shortest = KDTree.shortestDistance(query, leftCandidate.getCoords());
                if(nearestPossibleRB > shortest) {
                	resultString += "10T";
                	return leftCandidate;
                }
                else {
                	resultString += "10F";
                	candidate = leftCandidate;
                }
            }
            else resultString += "9F";
        }
        else resultString += "8F";
        // if query point is inside the right/bottom
        if(nearestPossibleRB == 0 && tree.getRightChild() != null) {
        	resultString += "11T";
            T rightCandidate = tree.getRightChild().nearest(query, shortest, !sortByLon);
            if(rightCandidate != null) {
            	resultString += "12T";
                shortest = KDTree.shortestDistance(query, rightCandidate.getCoords());
                if(nearestPossibleLT > shortest) {
                	resultString += "13T";
                	return rightCandidate;
                }
                else {
                	resultString += "13F";
                	candidate = rightCandidate;
                }
            }
            else resultString += "12F";
        }
        else resultString += "11F";

        // query point outside of left/top
        if(nearestPossibleLT > 0 && tree.getLeftChild() != null) {
        	resultString += "14T";
            T leftCandidate = tree.getLeftChild().nearest(query, shortest, !sortByLon);
            if(leftCandidate != null) {
            	resultString += "15T";
                shortest = KDTree.shortestDistance(query, leftCandidate.getCoords());
                if(nearestPossibleRB > shortest) {
                	resultString += "16T";
                	return leftCandidate;
                }
                else {
                	resultString += "16F";
                	candidate = leftCandidate;
                }
            }
            else resultString += "15F";
        }
        else resultString += "14F";

        // query point outside of right/bottom
        if(nearestPossibleRB > 0 && tree.getRightChild() != null) {
        	resultString += "17T";
            T rightCandidate = tree.getRightChild().nearest(query, shortest, !sortByLon);
            if(rightCandidate != null) {
            	resultString += "18T";
            	return rightCandidate;
            }
            else resultString += "18F";
        }
        else resultString += "17F";

        // maybe nothing of interest found (it could be null, but it could also be amazing)
        return candidate;
    }
    
    public String getResultString() {
    	return resultString;
    }
}
