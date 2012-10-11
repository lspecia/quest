package wlv.mt.xmlwrap.fst;

public class Arc {

    private Node start;
    private Node end;
    private float weight;
    private String inputLabel;
    private String outputLabel;
    private boolean visited = false;

    public Arc(Node start, Node end, float weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    public Arc(Node start, Node end, float weight, String input, String output) {
        this.start = start;
        this.end = end;
        this.weight = weight;
        this.inputLabel = input;
        this.outputLabel = output;

    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean v) {
        visited = v;
    }

    public float getWeight() {
        return weight;
    }

    public String getInputLabel() {
        return inputLabel;
    }

    public String getOutputLabel() {
        return outputLabel;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(start.getLabel() + "\t");
        sb.append(end.getLabel() + "\t");
        sb.append(inputLabel + "\t");
        sb.append(outputLabel + "\t");
        sb.append(weight + "\t");
        return sb.toString();
    }

    public Node getEndNode() {
        return end;
    }

    public Node getStartNode() {
        return start;
    }
}
