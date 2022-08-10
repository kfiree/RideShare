package app.model.utils;

import app.model.graph.Node;
import org.jetbrains.annotations.NotNull;

public class AlgoNode implements Comparable<AlgoNode>{
    private Node node;
    private double h = 0, g = 0, f = 0;

    public AlgoNode(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AlgoNode) {
            if (((AlgoNode) o).getNode().equals(this.getNode())) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public int compareTo(@NotNull AlgoNode other) {
        if(other.getF() < f){
            return 1;
        }
        else if(other.getF() > f){
            return -1;
        }
        else{
            return Integer.compare(other.node.getId().compareTo(this.node.getId()), 0);
        }
    }
}
