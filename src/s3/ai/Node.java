package s3.ai;

import java.util.ArrayList;

public class Node {

    private double x, y;
    private double g;
    private double heuristic, manhattan;
    private Node parent;

    public Node(){
        this.x = 0;
        this.y = 0;
        this.g = 0;
        this.heuristic = 0;


        this.parent = null;
    }

    public Node(double x, double y, double g, Node parent){
        this.x = x;
        this.y = y;
        this.g = g;
        this.parent = parent;
    }

    public double get_x() {
        return this.x;
    }

    public double get_y() {
        return this.y;
    }
    public double get_g() {
        return this.g;
    }

    public double getHeuristic(){return this.heuristic;}

    public void setXY(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Node getParent(){
        return this.parent;
    }

    public ArrayList<Node> getChildrenOf(){

        ArrayList<Node> children = new ArrayList<Node>();

        children.add(new Node(get_x()+1, get_y() , get_g() + 1, this));
        children.add(new Node(get_x() -1, get_y() , get_g() + 1, this));
        children.add(new Node(get_x(), get_y() + 1, get_g() + 1, this));
        children.add(new Node(get_x(), get_y() - 1, get_g() + 1, this));

        return children;
    }


    public void setg(double g){
        this.g = g;
    }

    public void setHeuristic(double manhattan){
        this.heuristic = this.g + manhattan;
    }

    public void setParent(Node parent){
        this.parent = parent;
    }



}
