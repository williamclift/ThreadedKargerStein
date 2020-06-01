/** -----------------------------------------------------------------------
    Karger.java

    @author William Clift
            Data Structures and Algorithms
            Ursinus College
            Project 4 - Karger's Algorith
            13 May 2020

    Compile and Run Instructions:
            
        Compile:    javac Karger.java Graph.java Edge.java GraphGenerator.java Subset.java
        
        Run:        java Karger

        To run a random Graph of size [Vertices][Edges], run the following:

        			java GraphGenerator [Verticies] [Edges] | java Karger

    ------------------------------------------------------------------- **/

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.io.*;

@SuppressWarnings("unchecked")
public class Karger extends RecursiveAction implements Runnable{
    ConcurrentSkipListSet<Double> cutsSet; //TODO: share this between instances to store cuts of multiple runs
    Graph g;
    private static final int processors = Runtime.getRuntime().availableProcessors();
    int n;
    int t;

    public Karger(Graph graph) {
		this.g = graph;
		this.cutsSet = new ConcurrentSkipListSet<Double>();
    }

    public Karger(Graph graph, int n, ConcurrentSkipListSet<Double> cuts) {
		this.g = graph;
		this.cutsSet = cuts;
		this.n = n;
    }

    public Karger(Graph graph, ConcurrentSkipListSet<Double> cuts) {
		this.g = graph;
		this.cutsSet = cuts;
    }

    public Karger(Graph graph, int t) {
		this.g = graph;
		this.t = t;
    }

    public static void main(String[] args) {
    	//Generate from File
		//Graph graph = Graph.generateGraphFromText(new File("graph.txt"));

		//Generate from Stdin
		Graph graph = Graph.generateGraphFromText();

		Karger k = new Karger(graph);

		//System.out.println(graph.toMarkdown(true));
		ConcurrentSkipListSet<Double> cutsSet = new ConcurrentSkipListSet<Double>();
		//test(graph, cutsSet);

		long start = 0;
		long end = 0;

		start = System.currentTimeMillis();
			karger(new Graph(graph));
		end = System.currentTimeMillis();
		System.out.println("Karger: \t\t" + ((float)(end - start) / 1000F) + "\tseconds.");

		start = System.currentTimeMillis();
			mincut(new Graph(graph));
		end = System.currentTimeMillis();
		System.out.println("Min-Cut: \t\t" + ((float)(end - start) / 1000F) + "\tseconds.");

		start = System.currentTimeMillis();
			minCutThreaded(new Graph(graph), cutsSet);
		end = System.currentTimeMillis();
		System.out.println("Threaded Min-Cut: \t" + ((float)(end - start) / 1000F) + "\tseconds.");

		start = System.currentTimeMillis();
			fastMincut(new Graph(graph));
		end = System.currentTimeMillis();
		System.out.println("Karger-Stein: \t\t" + ((float)(end - start) / 1000F) + "\tseconds.");

		start = System.currentTimeMillis();
			fastMincutThreaded(new Graph(graph), 0, cutsSet);
		end = System.currentTimeMillis();
		System.out.println("Threaded Karger-Stein: \t" + ((float)(end - start) / 1000F) + "\tseconds.");

/*
		System.out.println(karger(new Graph(graph)));
		System.out.println(mincut(new Graph(graph)));
		System.out.println(minCutThreaded(new Graph(graph), cutsSet));    
		System.out.println(fastMincut(new Graph(graph)));			   
		System.out.println(fastMincutThreaded(new Graph(graph), 0, cutsSet));
*/
    }

    /**
     * Prints out the results of a test on the system (used for analysis)
     * @param graph graph that will be contracted
     * @param cutsSet the cuts set of the minimum cut
     **/
    public static void test(Graph graph, ConcurrentSkipListSet<Double> cutsSet){
    	Graph g = graph;
    	ConcurrentSkipListSet<Double> cuts = cutsSet;

    	long start = 0;
		long end = 0;

    	//Test Printouts
		String printout = "";
		start = System.currentTimeMillis();
			karger(new Graph(g));
		end = System.currentTimeMillis();
		printout+=((float)(end - start) / 1000F) + " ";

		start = System.currentTimeMillis();
			mincut(new Graph(g));
		end = System.currentTimeMillis();
		printout+=((float)(end - start) / 1000F) + " ";

		start = System.currentTimeMillis();
			minCutThreaded(new Graph(g), cuts);
		end = System.currentTimeMillis();
		printout+=((float)(end - start) / 1000F) + " ";

		start = System.currentTimeMillis();
			fastMincut(new Graph(g));
		end = System.currentTimeMillis();
		printout+=((float)(end - start) / 1000F) + " ";

		start = System.currentTimeMillis();
			fastMincutThreaded(new Graph(g), 0, cuts);
		end = System.currentTimeMillis();
		printout+=((float)(end - start) / 1000F);
		System.out.println(printout);
    }

     /**
     * The threaded mincut algorithm
     * @param g graph that will be contracted
     * @param cuts the cuts list
     * @return mincut weight of the minimum cut
     **/
    public static double minCutThreaded(Graph g, ConcurrentSkipListSet<Double> cuts){
    	//ExecutorService executor = Executors.newWorkStealingPool(nThreads);
		ExecutorService executor = Executors.newCachedThreadPool();
		//ExecutorService executor = Executors.newFixedThreadPool(nThreads);

		int V = g.getOriginalVertexCount();
		final int MAX = 2 * (V * V);

		for(int i = 0; i < MAX; i++){
			if(Math.pow(2, i) <= processors){
				Runnable thread = new Karger(new Graph(g), cuts); 
				executor.execute(thread);
			}else{
				cuts.add(karger(new Graph(g)));
			}
		}

		executor.shutdown();

		try { 
	    	executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch(InterruptedException e) {
		    System.err.println(e.getMessage());
		}

		return cuts.first();				//Returns the minimum
    }

    /**
     * Recursive override
     **/
     @Override
    public void run() {
		//TODO for parallel Karger
		cutsSet.add(karger(g));
    }

     /**
     * Runts 2*V^2 iterations of karger for 98% confidence
     * Contrac the graph by one node
     * @param g graph that will be contracted
     * @param n the iteration of the recursive algorithm
     * @param cuts the minimum cuts lift
     * @return cutWeight weight of the minimum cut
     **/
	public static double fastMincutThreaded(Graph g, int n, ConcurrentSkipListSet<Double> cuts) {
		double cutWeight = Double.POSITIVE_INFINITY;

		double td = g.getOriginalVertexCount() / Math.sqrt(2) + 1;
		int t = (int) td;

		if(g.getCurrentVertexCount() < t || g.getCurrentVertexCount()< 6){
			cutWeight = mincut(new Graph(g));
		}else{
			Graph c1 = new Graph(g);
			Graph c2 = new Graph(g);

			if(Math.pow(2, n) <= processors && n >= 0){
				invokeAll(new Karger(c1, n++, cuts), new Karger(c2, n++, cuts));
			}
			else{
				Karger k1 = new Karger(c1, n++, cuts);
				Karger k2 = new Karger(c2, n++, cuts);
				k1.compute();
				k2.compute();
			}
			cutWeight = cuts.first();
		}

		return cutWeight;
    }

     /**
     * Recursive override
     **/
     @Override
    public void compute() {
		//TODO for parallel Karger-Stein
    	double td = g.getCurrentVertexCount() / Math.sqrt(2) + 1;
		int t = (int) td;

		while(g.getCurrentVertexCount() > t) {
			this.g = contract(g);
		}

    	cutsSet.add(fastMincutThreaded(g, n, cutsSet));
    }

     /**
     * Runts 2*V^2 iterations of karger for 98% confidence
     * Contrac the graph by one node
     * @param g graph that will be contracted
     * @return mincut weight of the minimum cut
     **/
	public static double fastMincut(Graph g) {
		double mincut = Double.POSITIVE_INFINITY;

		ConcurrentSkipListSet<Double> cuts = new ConcurrentSkipListSet<Double>();

		mincut = fastMincutThreaded(g, -1, cuts);

		return mincut;
    }

    /**
     * Runts 2*V^2 iterations of karger for 98% confidence
     * Contrac the graph by one node
     * @param g graph that will be contracted
     * @return mincut weight of the minimum cut
     **/
    public static double mincut(Graph g) {
		double mincut = Double.POSITIVE_INFINITY;
		//TODO: run karger(g) tn^2=t|V|^2times and take the min
		int V = g.getCurrentVertexCount();
		final int MAX = 2 * (V * V);
		for(int i = 0; i < MAX; i++){
			Graph copy = new Graph(g);
			double cut = karger(copy);
			if(cut < mincut){
				mincut = cut;
			}
		}

		return mincut;
    }

    /**
     * Runs one iteration
     * Contrac the graph by one node
     * @param g graph that will be contracted
     * @return g graph returned
     **/
    public static Graph contract(Graph g){
    	// Get data of given graph
		int V = g.getOriginalVertexCount();
		int E = g.getOriginalEdgesCount();
		List<Edge> edges = g.getEdges();	
		List<Subset> subsets = g.getSubsets();

		for (int v = 0; v < V; v++) {
		    subsets.add(new Subset());
		    subsets.get(v).parent = v;
		    subsets.get(v).rank = 0;
		} 

 		boolean done = false;
		while(!done){
		    // Pick a random edge
		    int i = (int)(Math.random() * E);
	
		    int subset1 = Subset.find(subsets, edges.get(i).s());
		    int subset2 = Subset.find(subsets, edges.get(i).t());
		    
		    if (subset1 != subset2) {
				g.decrementVertexCount();
				Subset.union(subsets, subset1, subset2);
				done = true;
			}
		}

		return g;
    }


    /**
     * Runs one iteration
     * Karger's algorithm down to t vertices.
     * Unless you intend to modify original graph,
     * it is recommended that you copy the graph 
     * before calling.
     * @param graph graph that will be contracted
     * @param t the number of iterations down to 
     * @return cut weight after run
     **/
    public static double fastKarger(Graph g, double t) {
		double cutWeight = Double.POSITIVE_INFINITY;
		// Get data of given graph
		int V = g.getOriginalVertexCount();

		g.setCurrentVertexCount(V);    
		while (g.getCurrentVertexCount() > t) {
		    g = contract(g);
		}

		ConcurrentSkipListSet<Double> cutsSet = new ConcurrentSkipListSet<Double>();
		for(int i = 0; i < t; i++){
			cutsSet.add(karger(new Graph(g)));
		}

		cutWeight = cutsSet.first();

		return cutWeight;
    }

    /**
     * Runs one iteration
     * Karger's algorithm down to 2 vertices.
     * Unless you intend to modify original graph,
     * it is recommended that you copy the graph 
     * before calling.
     * @param graph graph that will be contracted
     * @return cutWeight
     **/
    public static double karger(Graph graph) {
		// Get data of given graph
		int V = graph.getOriginalVertexCount();
		int E = graph.getOriginalEdgesCount();
		List<Edge> edges = graph.getEdges();	
		List<Subset> subsets = graph.getSubsets();
		
		while (graph.getCurrentVertexCount() > 2) {
			graph = contract(graph);
		}

		double cutWeight = 0D;
		for (int i = 0; i < E; i++) {
		    int subset1 = Subset.find(subsets, edges.get(i).s());
		    int subset2 = Subset.find(subsets, edges.get(i).t());
		    if (subset1 != subset2) {
				cutWeight += edges.get(i).getWeight(); //TODO: change to handle weights
			} 
		}
		return cutWeight;
	}
}