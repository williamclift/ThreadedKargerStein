/** -----------------------------------------------------------------------
    Subset.java

    @author William Clift
            Data Structures and Algorithms
            Ursinus College
            Project 4 - Karger's Algorithm
            13 May 2020

    Compile and Run Instructions:
            
        Compile:    javac Subset.java
        
        Run:        java Subset

    ------------------------------------------------------------------- **/

import java.util.List;

public class Subset {
    int parent, rank;

    public Subset(Subset o) {
		this.parent = o.parent;
		this.rank = o.rank;
    }

    public Subset() {};

	@Override
	public boolean equals(Object o) {
		Subset s = (Subset)o;
		if(s.parent == this.parent
		   && s.rank == this.rank) {
		    return true;
		}
		return false;
    }

    public static void union(List<Subset> subsets, int x, int y) {
		int xroot = find(subsets, x);
		int yroot = find(subsets, y);

		if (subsets.get(xroot).rank < subsets.get(yroot).rank) {
		    subsets.get(xroot).parent = yroot;
		}
		else if (subsets.get(xroot).rank > subsets.get(yroot).rank) {
		    subsets.get(yroot).parent = xroot;
		}
		else {
		    subsets.get(yroot).parent = xroot;
		    subsets.get(xroot).rank++;
		}
	} 
	    
	public static int find(List<Subset> subsets, int i) {
		if (subsets.get(i).parent != i) {
		    subsets.get(i).parent = find(subsets, subsets.get(i).parent);
		}
		return subsets.get(i).parent;
    }
}