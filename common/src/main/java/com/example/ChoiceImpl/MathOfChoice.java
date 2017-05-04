package com.example.ChoiceImpl;
import java.util.Arrays;
//import java.util.ArrayUtils;

public class MathOfChoice extends ChoiceAlgorithm {
	//set by generateCandidates(int)
	private double[] candidates = null;


	//set by calculateOptimal()
	private double chanceOfMaxRank = 0;
	public int    optimalR = 0;

	//set by optimalChoiceAlgorithm()
	public double maxEltFound = 0;
	public int    maxEltFoundIndex = 0;
	public double maxEltRank = 0;
	private double maxEltFoundWithinR = 0;
	private int    maxEltFoundIndexWithinR = 0;


	public MathOfChoice(int numCandidates) {
		if (numCandidates <= 0) {
			System.out.println("Must have at least 1 candidate.");
			return;
		}
		calculateOptimal(numCandidates); 					//calculate optimal R value to stop searching
		this.candidates = generateCandidates(numCandidates);//Generate a list of random numbers of the desired size

		if (candidates != null )
			optimalChoiceAlgorithm(optimalR);	//assuming that there are candidates,
		else
			throw new IllegalStateException("Something went wrong when creating the candidate list.");

		//printStatistics();
	}
	
	public MathOfChoice(double[] candidates) {
		if (candidates == null) {
			System.out.println("Must have at least 1 candidate.");
			return;
		}
		calculateOptimal(candidates.length); 					//calculate optimal R value to stop searching
		this.candidates = candidates;//Generate a list of random numbers of the desired size

		//if (candidates != null )
			optimalChoiceAlgorithm(optimalR);	//assuming that there are candidates,
		//else
		//	throw new IllegalStateException("Something went wrong when creating the candidate list.");

		printStatistics();
	}


	private double[] generateCandidates(int numCandidates) {
		double[] ans = new double[numCandidates];

		for (int i = 0; i < numCandidates; i++) {
			ans[i] = Math.random();
		}
		return ans;
	}

	private void calculateOptimal(int numCandidates) {
		double maxChance = 0;
		int maxR = 0;
		int n = numCandidates;

		for(int r = 2; r <= n; r++) {
			double thisChance = 0;
			double mul = (double) (r - 1) / n;
			double sum = 0;
			for (int j = r; j <= n; j++) {
				sum += (double)Math.pow((j-1), -1);
			}
			thisChance = mul * sum;
			/*System.out.println(numCandidates + ": " + thisChance + " = " + mul + " * " + sum);*/
			if (thisChance > maxChance) {
				maxChance = thisChance;
				maxR = r;
			}
		}
		this.optimalR = maxR;
		this.chanceOfMaxRank = maxChance;
		//System.out.println(maxChance + "% at r = " + maxR);
		//return maxChance;
	}

	private void optimalChoiceAlgorithm(int optimalR) {
		/*System.out.println("*************************OPTCHOICE***********************************************");*/
		int counter = 0;
		double currOptimal = -1;
		int posOfCurrOptimal = 0;

		double optBeforeR = -1;
		int posBeforeR = 0;

		while (counter < candidates.length) {
			/*System.out.println("Debug: " + counter + " " + optimalR);*/
				if (counter > optimalR) {
						if (candidates[counter] >= optBeforeR) {
							currOptimal = candidates[counter];
							posOfCurrOptimal = counter;
							break;
						} else {
							//we are past optimal threshold but did not find a maximum. do nothing.
						}
				} else {
						if (candidates[counter] > optBeforeR) {
								optBeforeR = candidates[counter];
								posBeforeR = counter;
							
						} else {
							//Not past optimalR and this isn't a local max. do nothing
						}
				}

			
			counter++;
		}

		//System.out.println(counter + ": " + currOptimal + " " + optBeforeR + " index " + posOfCurrOptimal + " " + posBeforeR);
		
		if (currOptimal > 0) {
			this.maxEltFound =  currOptimal;
			this.maxEltFoundIndex = posOfCurrOptimal;
		} else {
			this.maxEltFound = candidates[candidates.length - 1];
			this.maxEltFoundIndex = candidates.length - 1;
		}
		this.maxEltFoundWithinR = optBeforeR;
		this.maxEltFoundIndexWithinR = posBeforeR;
        int rankOfAnswer = getRankOf(maxEltFoundIndex, candidates);
        this.maxEltRank = rankOfAnswer;
		//return posOfCurrOptimal;
	}

	private void printStatistics() {
		String stats = "";
		int rankOfElementBeforeR = getRankOf(maxEltFoundIndexWithinR, candidates);
		stats += "For this randomly generated " + candidates.length + "-element set, the algorithm determined the optimal place to stop evaluating was at element " + optimalR + ".\n";
		stats += "Before reaching " + optimalR + ", the largest value found was " + maxEltFoundWithinR + " at index " + maxEltFoundIndexWithinR + ". This element was of rank " + rankOfElementBeforeR + "\n";
		stats += "Once element " + optimalR + " was passed, the next largest element found was " + maxEltFound + " at index " + maxEltFoundIndex + ", which was the final choice of the algorithm.\n\n";
		stats += "The chosen element " + maxEltFound + " was of rank " + maxEltRank + " in the original set of candidates.\n";


		System.out.println(stats);

	}

	private int getRankOf(int index, double[] array) {
		if (index > array.length)
			return -1;
		double value = array[index];
		double[] manip = Arrays.copyOf(array, array.length);
		Arrays.sort(manip);

		for (int i = candidates.length - 1; i > 0; i--) {
			if (manip[i] == value) {
				return candidates.length - i;
			}
		}

		//The value was not found in the sorted array.....
		return -1;
	}

	public double getChosenValue() {
		return maxEltFound;
	}
	public double getChosenRank() {
		return maxEltRank;
	}

	public static void main(String[] args) {
		new MathOfChoice(1000);
	}

}