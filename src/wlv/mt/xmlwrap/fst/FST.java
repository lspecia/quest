package wlv.mt.xmlwrap.fst;

import java.util.*;
import java.io.*;

public class FST {

    private HashMap<Integer, Node> nodes;
    private Node initial;
    BufferedWriter fw;

    public FST(Node initial) {
        this.initial = initial;
        nodes = new HashMap<Integer, Node>();
    }

    public FST(String file) {
        try {
            nodes = new HashMap<Integer, Node>();
            BufferedReader bir = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String input = null;
            String output = null;
            int start;
            int end;
            float weight;
            String line = bir.readLine();
            boolean first = true;
            while (line != null) {
                String[] comps = line.split("\t");
                Integer startIndex = new Integer(comps[0]);
                Node startNode = nodes.get(startIndex);
                if (startNode == null) {
                    startNode = new Node(startIndex.intValue());
                    nodes.put(startIndex, startNode);
                    //System.out.println(startNode);
                }
                System.out.println(comps);
                Node endNode = null;
                if (comps.length != 2) {
                    Integer endIndex = new Integer(comps[1]);
                    endNode = nodes.get(endIndex);
                    if (endNode == null) {
                        endNode = new Node(endIndex.intValue());
                        nodes.put(endIndex, endNode);
                        //System.out.println(endNode);
                    }
                    input = comps[2];
                    output = comps[3];
                    if (comps.length <= 4) {
                        weight = 0;
                    } else {
                        weight = Float.parseFloat(comps[4]);
                    }
                    Arc arc = new Arc(startNode, endNode, weight, input, output);
                    startNode.addArc(arc);
                } else {
                    weight = 0;
                }
                if (first) {
                    initial = startNode;
                    first = false;
                }

                line = bir.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public FST(String file, String out) {
        try {
            File f = new File(out);
            System.out.println("writing to " + out);
//			if (f.exists())
//				return;
            fw = new BufferedWriter(new FileWriter(out));
            nodes = new HashMap<Integer, Node>();
            BufferedReader bir = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String input = null;
            String output = null;
            int start;
            int end;
            float weight;
            String line = bir.readLine();
            boolean first = true;
            while (line != null) {
//				System.out.println(line);
                String[] comps = line.split("\t");
                Integer startIndex = new Integer(comps[0]);
                Node startNode = nodes.get(startIndex);
                if (startNode == null) {
                    startNode = new Node(startIndex.intValue());
                    nodes.put(startIndex, startNode);
                    //		 System.out.println(startNode);
                }
                Node endNode = null;
                if (comps.length >= 2) {
                    Integer endIndex = new Integer(comps[1]);
                    endNode = nodes.get(endIndex);
                    if (endNode == null) {
                        endNode = new Node(endIndex.intValue());
                        nodes.put(endIndex, endNode);
                        //System.out.println(endNode);
                    }
                    input = comps[2];
                    output = comps[3];
                    if (comps.length <= 4) {
                        weight = 0;
                    } else {
                        weight = Float.parseFloat(comps[4]);
                    }

                    Arc arc = new Arc(startNode, endNode, weight, input, output);
                    startNode.addArc(arc);
                } else {
                    weight = 0;
                    //				System.out.println("components:"+comps.length+"  "+line);
                }
                if (first) {
                    initial = startNode;
                    first = false;
                }

                line = bir.readLine();
            }
            //		System.out.println(initial);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printNodes() {
        Iterator<Node> it = nodes.values().iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
    }

    public void dfs() {
        try {
            if (initial == null) {
                return;
            }
            LinkedList<Arc> path = new LinkedList<Arc>();
            System.out.println("-------DFS---------");
            //DFS uses Stack data structure
            Stack<Node> s = new Stack<Node>();
            s.push(initial);
            System.out.println("initial:" + initial);
            initial.setVisited(true);
//		System.out.println(initial.getLabel());
            int count = 0;
            while (!s.isEmpty()) {
                Node n = s.peek();
                if (n.isFinal()) {
//				System.out.print("#"+n);
                    printPath(path);
                    //			path.removeLast();
                    count++;
                }
                Arc childArc = n.getUnvisitedChildNode();
                if (childArc != null) {
                    path.add(childArc);
                    Node child = childArc.getEndNode();
                    //child.setVisited(true);
                    childArc.setVisited(true);

                    s.push(child);
//				System.out.println("push: "+child.getLabel());
                } else {
                    n = s.peek();
                    n.setArcsUnvisited();
                    s.pop();
                    if (path.size() > 0) {
                        path.removeLast();
                    }
                }
            }
            fw.close();
            System.out.println(count + " paths");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printPath(LinkedList<Arc> path) {
        try {
            Iterator<Arc> it = path.iterator();
            Arc crtArc;
            while (it.hasNext()) {
                crtArc = it.next();
                if (!crtArc.getOutputLabel().equals("<eps>")) {
                    //			System.out.print(crtArc.getStartNode().getLabel()+"->"+crtArc.getEndNode().getLabel()+":"+crtArc.getOutputLabel()+" ");
                    fw.write(crtArc.getInputLabel() + "\t" + crtArc.getOutputLabel() + "\t" + crtArc.getWeight() + " | ");
                }
            }
//		System.out.println();
            fw.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FST fst = new FST(args[0], args[1]);
        fst.dfs();
    }
}
