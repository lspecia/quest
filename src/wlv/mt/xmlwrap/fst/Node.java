package wlv.mt.xmlwrap.fst;

import java.util.*;

public class Node {

    int label;
    ArrayList<Arc> outArcs;
    boolean visited;

    public Node(int label) {
        this.label = label;
        outArcs = new ArrayList<Arc>();
        visited = false;
    }

    public void addArc(Arc a) {
        outArcs.add(a);
    }

    public boolean isFinal() {
        return (outArcs.size() == 0);
    }

    public void setVisited(boolean v) {
        visited = v;
    }

    public ArrayList<Arc> getArcs() {
        return outArcs;
    }

    public int getLabel() {
        return label;
    }

    public Arc getUnvisitedChildNode() {
        Iterator<Arc> it = outArcs.iterator();
        boolean found = false;
        Arc crtArc;
        while (it.hasNext() && !found) {
            crtArc = it.next();
            if (!crtArc.isVisited()) {
                found = true;
                return crtArc;
            }
        }
        return null;
    }

    public void setArcsUnvisited() {
        Iterator<Arc> it = outArcs.iterator();
        boolean found = false;
        Arc crtArc;
        while (it.hasNext() && !found) {
            crtArc = it.next();
            crtArc.setVisited(false);
        }
    }

    public boolean isVisited() {
        return visited;
    }

    public String toString() {
        return getLabel() + "\t" + "arcs: " + outArcs.size();
    }
}
