/** -----------------------------------------------------------------------
    Edge.java

    @author William Clift
            Data Structures and Algorithms
            Ursinus College
            Project 4 - Karger's Algorithm
            13 May 2020

    Compile and Run Instructions:
            
        Compile:    javac Edge.java
        
        Run:        java Edge

    ------------------------------------------------------------------- **/


public class Edge {
    private int startVertex;
    private int endVertex;
    private double weight;
    
    public Edge(int startVertex, int endVertex) {
    	setStartVertex(startVertex);
    	setEndVertex(endVertex);
    	setWeight(1D);
    }

    public Edge(int startVertex, int endVertex, double weight) {
    	setStartVertex(startVertex);
    	setEndVertex(endVertex);
    	setWeight(weight);

    }

    public Edge(Edge o) {
    	setStartVertex(o.s());
    	setEndVertex(o.t());
    	setWeight(o.weight());
    }

    public int other(int v) {
    	int r = v == s() ? t() : s();
    	return r;
    }

    public int s() {
	   return getStartVertex();
    }

    public int t() {
	   return getEndVertex();
    }

    public void setS(int s) {
	   this.startVertex = s;
    }

    public void setT(int t) {
	   this.endVertex = t;
    }

    public double getWeight() {
	   return weight;
    }

    public double weight() {
	   return weight;
    }

    public void setWeight(double weight) {
	   this.weight = weight;
    }

    public int getStartVertex() {
	   return startVertex;
    }

    public int getEndVertex() {
	   return endVertex;
    }

    public void setStartVertex(int startVertex) {
	   this.startVertex = startVertex;
    }

    public void setEndVertex(int endVertex) {
	   this.endVertex = endVertex;
    }

    @Override
    public int hashCode() {
    	String hashString = startVertex + "-" + endVertex;
    	return hashString.hashCode();
    }

    @Override
    public String toString() {
	   return startVertex + " " + endVertex + " " + weight;
    }
}