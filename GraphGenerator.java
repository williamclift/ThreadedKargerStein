/** -----------------------------------------------------------------------
    GraphGenerator.java

    @author William Clift
            Data Structures and Algorithms
            Ursinus College
            Project 4 - Karger's Algorith
            13 May 2020

    Compile and Run Instructions:
            
        Compile:    javac GraphGenerator.java
        
        Run:        java GraphGenerator [vertices] [edges]

    ------------------------------------------------------------------- **/

import java.util.Random;
import java.util.*;
import java.io.*;

public class GraphGenerator {

    public static void main(String[] args) {
		if(args.length != 2) {
		    System.err.println("Usage: java GraphGenerator [vertices] [edges]");
		    return;
		}
		
		int vertices = Integer.parseInt(args[0]);
		int edges =  Integer.parseInt(args[1]);
		randomGraph(vertices, edges);
		//randomGraphFile(vertices, edges);
		
    }

    public static void randomGraph(int vertices, int edges) {
		Random rand = new Random();
		System.out.println(vertices);
		System.out.println(edges);
		for(int i = 0; i < edges; i++) {
		    final int s = rand.nextInt(vertices);
		    final int t = rand.nextInt(vertices);
		    final double weight = rand.nextDouble();
		    System.out.println(s + " " + t + " " + weight);
		}
    }

    public static void randomGraphFile(int vertices, int edges) {
		Random rand = new Random();
		try
		{
				PrintWriter print = new PrintWriter(new File("graph.txt"));
				print.println(vertices);
				print.println(edges);
				for(int i = 0; i < edges; i++) {
				    final int s = rand.nextInt(vertices);
				    final int t = rand.nextInt(vertices);
				    final double weight = rand.nextDouble();
				    print.println(s + " " + t + " " + weight);
				}
				print.close();
		}catch (IOException e){
        	e.printStackTrace();
        }

		
    }
}