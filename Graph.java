/** -----------------------------------------------------------------------
    Graph.java

    @author William Clift
            Data Structures and Algorithms
            Ursinus College
            Project 4 - Karger's Algorithm
            13 May 2020

    Compile and Run Instructions:
            
        Compile:    javac Graph.java
        
        Run:        java Graph

    ------------------------------------------------------------------- **/

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.io.*;
import java.util.*;


public class Graph {
    private final int V, E; //original number of vertices and edges
    private final List<Edge> edges; //graph defined by edges
    private List<Subset> subsets;
    private int vertices; //vertices remaining after contraction

    public Graph (int V, int E) {
		this.V = V;
		this.E = E;
		this.vertices = V;
		this.edges = new ArrayList<Edge>(E);
		this.subsets = new ArrayList<Subset>(V); 
    }

    public int getOriginalVertexCount() {
		return V;
    }

    public int getOriginalEdgesCount() {
		return E;
    }

    public List<Edge> getEdges() {
		return edges;
    }

    public List<Subset> getSubsets() {
		return subsets;
    }


    /**
     * Decreases number of vertices after contraction.
     **/
    public void decrementVertexCount() {
		vertices--;
    }

    /**
     * Returns count of vertices afer contraction
     **/
    public int getCurrentVertexCount() {
		return vertices;
    }

    public void setCurrentVertexCount(int vertices) {
		this.vertices = vertices;
    }

    /**
     * Copy constructor to duplicate Graph
     * Usage: Graph copy = new Graph(g);
     * @param o original graph
     **/
    public Graph(Graph o) {
		this.V = o.getOriginalVertexCount();
		this.E = o.getOriginalEdgesCount();
		this.vertices = o.getCurrentVertexCount();
		this.edges = new ArrayList<Edge>(E);
		this.subsets = new ArrayList<Subset>(E);

		for(Subset s : o.getSubsets()) {
		    this.subsets.add(new Subset(s));
		}
		for(Edge e : o.getEdges()) {
		    this.edges.add(new Edge(e));
		}
    }

    /**
     * Generate new graph from stdin
     * @return new graph
     **/
    public static Graph generateGraphFromText() {
		Scanner scanner = new Scanner(System.in);
		/*try {
		  	scanner = new Scanner(new File("graph.txt"));
		  } catch(FileNotFoundException e) {
		  	System.err.println("File not found. Exiting.");
		 	 System.exit(-1);
		  }*/

		int numVertices = Integer.parseInt(scanner.nextLine());
		int numEdges = Integer.parseInt(scanner.nextLine());
		Graph g = new Graph(numVertices, numEdges);
		int lineNum = 1;
		while(scanner.hasNextLine()) {
		    String line = scanner.nextLine();
		    String[] toks = line.split(" ");
		    int s = Integer.parseInt(toks[0]); //start vertex
		    int t = Integer.parseInt(toks[1]); //end vertex
		    double weight = Double.parseDouble(toks[2]);
		    g.addEdge(s, t, weight);
		}
		return g;
    }

        /**
     * Generate new graph from File
     * @return new graph
     **/
    public static Graph generateGraphFromText(File f){
    	Graph g = new Graph(0, 0);
		try {
			Scanner scanner = new Scanner(f);
		  	int numVertices = Integer.parseInt(scanner.nextLine());
			int numEdges = Integer.parseInt(scanner.nextLine());
			g = new Graph(numVertices, numEdges);
			int lineNum = 1;
			while(scanner.hasNextLine()) {
			    String line = scanner.nextLine();
			    String[] toks = line.split(" ");
			    int s = Integer.parseInt(toks[0]); //start vertex
			    int t = Integer.parseInt(toks[1]); //end vertex
			    double weight = Double.parseDouble(toks[2]);
			    g.addEdge(s, t, weight);
			}
		} catch(FileNotFoundException e) {
		  	System.err.println("File not found. Exiting.");
		 	 System.exit(-1);
		}
		return g;
    }

    /**
     * Adds new edge with values of e
     * @param e edge representng edge to be added
     **/
    public void addEdge(Edge e) {
		addEdge(e.s(), e.t(), e.weight());
    }

    public void addEdge(int s, int t, double weight) {
		this.edges.add(new Edge(s, t, weight));
    }

    public static void main(String[] args)
    {
		//read from stdin
		Graph graph = generateGraphFromText();
    }

    /**
     * Generate a Markdown file readable by Typora
     * of graph.  Doesn't consider contractions
     **/
    public String toMarkdown(boolean includeWeights) {
		String str = "```mermaid\ngraph LR\n"; 
		for(Edge e : edges) {
		    if(includeWeights) {
			str += "    ";
			str +=  e.s() + "((" + e.s() + "))";
			str += " -- ";
			str += e.weight();
			str += " --> ";
			str += e.t() + "((" + e.t() + "))";
			str += "\n";
		    } else {
			str += "    ";
			str += e.s() + "((" + e.s() + "))";
			str +=" --> ";
			str += e.t() + "((" + e.t() + "))";
			str += "\n";
		    }
		}
		return str;
    }

    /**
     * Generate a GraphViz dot file readable by Typora
     * of graph.  Doesn't consider contractions
     **/
    public String toGraphViz(boolean includeWeights) {
		String str = "digraph G {"; 
		for(Edge e : edges) {
		    if(includeWeights) {
			str += "    ";
			str +=  e.s();
			str += " -> ";
			str += e.t();
			str += " [label=\"" + e.weight() + "\"];";
			str += "\n";
		    } else {
			str += "    ";
			str += e.s();
			str +=" -> ";
			str += e.t() + ";";
			str += "\n";
		    }
		}
		str += "}";
		return str;
    }
}