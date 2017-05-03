package com.example.ChoiceImpl;
import java.util.*;
public class DPAlgorithm {
	private double[] candidates;
	private int numCandidates;

	private double[] c;
	private int[] s;
	//private int[] rr;

	private int optimal;

	private SortedSet<Double> rankFinder;

	public DPAlgorithm(int numCandidates) {
		this.numCandidates = numCandidates;
		this.candidates = generateCandidates(numCandidates); //generate candidates
		//this.rr 		= generateRelativeRank(this.candidates); //populate the relative ranks y_i
		this.c 			= generateCValues();
		this.optimal    = getOptimalValue();

		printStatistics();

		//Debug from here out
		/*String cs = "";String ss = ""; String rrs = ""; String candidatess = "";
		for (int i = 0; i < numCandidates; i++) {
			cs += c[i] + " ";
			ss += s[i] + " ";
			candidatess += candidates[i] + " ";
		}
		System.out.println("ALGORITHM COMPLETE. Results:");
		System.out.println("Candidates: " + candidatess);
		System.out.println("RR: " + rrs);
		System.out.println("S[i]: " + ss);
		System.out.println("C[i]: " + cs);
		System.out.println("");
		System.out.println("Calculated Optimal: " + this.optimal);*/
	}
	
	public DPAlgorithm(double[] candidates) {
		this.numCandidates = candidates.length;
		this.candidates = candidates;
		this.c = generateCValues();
		this.optimal = getOptimalValue();
		
		printStatistics();
	}

	private double[] generateCandidates(int numCandidates) {
		double[] ret = new double[numCandidates];

		for (int i = 0; i < numCandidates; i++)
			ret[i] = Math.random();

		return ret;
	}

	private int[] generateRelativeRank(double[] cand) {
		SortedSet<Double> seen = new TreeSet<Double>();
		int[] relRank = new int[numCandidates];

		for (int i = 0; i < cand.length; i++) {
			seen.add(cand[i]);
			relRank[i] = seen.tailSet(cand[i]).size();
		}
		return relRank;
	}

	//Populates s[] and returns c[]
	private double[] generateCValues() {
		double[] tentCVal = new double[numCandidates];
		tentCVal[numCandidates - 1] = (numCandidates + 1) / 2;

		this.s = new int[numCandidates];
		s[numCandidates - 1] = (int)((numCandidates + 1) / 2); //init with s_n-1

		for (int i = numCandidates - 2; i >= 0; i--) { //n-2 -> 0
			//System.out.println("Calculating c[" + i + "]");
			//get ci
			double oneoveri = 1.0 / (i + 1.0);
			double ratio = (numCandidates + 1.0) / (i + 2.0);
			double t2 = (s[i+1] * (s[i+1] + 1.0)) / 2.0;
			double lastadd = ((i + 1.0) - s[i+1]) * tentCVal[i+1];
			double ci = oneoveri * (ratio * t2 + lastadd);
			tentCVal[i] = ci;
			//get si
			int si = (int) (((i+(1.0))/(numCandidates+(1.0))) * ci);
			s[i] = si;
			//System.out.println(tentCVal[i] + " " + s[i]);
		}
		return tentCVal;
	}

	//Returns which i satisfies rr[i] < s[i], or -1 if not found.
	//Even when the optimal I is found, it continues to fully create the sorted set. Used for stats later
	private int getOptimalValue() {
		SortedSet<Double> seen = new TreeSet<Double>();
		int[] relRank = new int[numCandidates];
		boolean continueLooking = true;
		int optimalI = -1;

		for (int i = 0; i < numCandidates; i++) {
			seen.add(candidates[i]);
			relRank[i] = seen.tailSet(candidates[i]).size();
			if (relRank[i] < s[i] & continueLooking) {
				optimalI = i;
				continueLooking = false;
			}
		}
		this.rankFinder = seen;
		return optimalI;
	}

	private void printStatistics() {
		//Tell them their input, chosen stopping point, chosen candidate, and the rank of the candidate.
		String str = "";
		str+= "Based on your input of size " + numCandidates + ", the algorithm determined the optimal stopping point was at candidate " + this.optimal + ".\n";
		str+= "Using this optimal solution, the algorithm chose the value " + candidates[optimal] + ", which is of rank " + rankFinder.tailSet(candidates[optimal]).size() + " in the total set of candidates.\n"; 
		System.out.println(str);
		System.out.println(c[0]);
	}
	
	public double getChosenValue() {
		return candidates[optimal];
	}
	public double getChosenRank() {
		return rankFinder.tailSet(candidates[optimal]).size();
	}


	public static void main(String[] args) {
		//new DPAlgorithm(100);
	}
}