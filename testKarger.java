import java.util.*;
import java.io.*;

public class testKarger{
	

	public static void main(String[] args){
		int n = Integer.parseInt(args[0]);
		int[] v = new int[n];


		//Error input condition
		if(10 * n * 2.5 > Integer.parseInt(args[1])){
			System.out.println("Edges to Verticies ratio is too small.");
		}else{
			for(int i = 0; i < n; i++){
				v[i] = 50 * (i+1);
			}

			try{
				ArrayList<Integer> verticies = new ArrayList<Integer>();
	        	ArrayList<Float> karger = new ArrayList<Float>();
			    ArrayList<Float> mincut = new ArrayList<Float>();
			    ArrayList<Float> tmincut = new ArrayList<Float>();
			    ArrayList<Float> ks = new ArrayList<Float>();
			    ArrayList<Float> tks = new ArrayList<Float>();

			    Runtime.getRuntime().exec("javac Karger.java Graph.java Edge.java GraphGenerator.java Subset.java");
	           	for(int c = 0; c < n; c++){
	           		System.out.println((c+1) + ":-------------------------------");

			        Process gen = Runtime.getRuntime().exec("java GraphGenerator " + v[c] + " " + args[1]);

			        Process process = Runtime.getRuntime().exec("java Karger");
					BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

					String line=null;

			        while((line = input.readLine()) != null)
			        {	
			        	System.out.println(line);
			        	String[] toks = line.split(" ");
			        	
			        	if(toks.length == 5){
			        		verticies.add(v[c]);
			        		karger.add(Float.parseFloat(toks[0]));
				        	mincut.add(Float.parseFloat(toks[1]));
				        	tmincut.add(Float.parseFloat(toks[2]));
				        	ks.add(Float.parseFloat(toks[3]));
				        	tks.add(Float.parseFloat(toks[4]));	

				        	//Printouts
							System.out.println("Verticies: ");
					        for(Integer e : verticies){
					        	System.out.print(e + ", ");
					        }
							System.out.println();
							System.out.println("Karger:	");
					        for(Float e : karger){
					        	System.out.print(e + ", ");
					        }
					        System.out.println();
					      	System.out.println("Min-Cut: ");
					        for(Float e : mincut){
					        	System.out.print(e + ", ");
					        }
					        System.out.println();
					        System.out.println("Threaded Min-Cut:");
					        for(Float e : tmincut){
					        	System.out.print(e + ", ");
					        }
					        System.out.println();
					        System.out.println("Karger-Stein: ");
					        for(Float e : ks){
					        	System.out.print(e + ", ");
					        }
					        System.out.println();
					        System.out.println("Threaded Karger-Stein: ");
					        for(Float e : tks){
					        	System.out.print(e + ", ");
					        }
					        System.out.println();
					        System.out.println();
			        	}else{
			        		System.out.println("Failed Read.");
			        	}
			        			
			        }
			        input.close();
				}
	        } 
	        catch (IOException e){
	        	e.printStackTrace();
	        }
		}
	}
}