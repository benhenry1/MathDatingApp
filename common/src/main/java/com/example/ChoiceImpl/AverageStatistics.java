package com.example.ChoiceImpl;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ben on 5/4/2017.
 * Given a number of candidates, runs the algorithms a bunch of times to average their output.
 */

public class AverageStatistics {
    boolean done = false;
    int numIterations = 100;
    int numCandidates;

    double avgRankMOC = -1;
    double avgRankDP  = -1;
    int[] rankCounterMOC;
    int[] rankCounterDP;


    public AverageStatistics(int numCandidates) {
        this.numCandidates = numCandidates;
    }
    public AverageStatistics(int numCandidates, int numIterations) {
        this.numCandidates = numCandidates;
        this.numIterations = numIterations;
    }

    public void start() {
        if ( done )
            return;
        done = true;
        Set<DPAlgorithm> dpAlgorithms   = new HashSet<DPAlgorithm>();
        Set<MathOfChoice> mathOfChoices = new HashSet<MathOfChoice>();

        //SLOW AF! Creating each instance involves actually simulating. May change that. Threads?
        for (int i = 0; i < numIterations; i++) {
            dpAlgorithms.add(new DPAlgorithm(numCandidates));
            mathOfChoices.add(new MathOfChoice(numCandidates));
        }

        int sumDPRank= 0, sumMOCRank = 0;
        double avgDPRank = 0, avgMOCRank = 0;
        int[] dpRankCounter = new int[5]; //c[0] = rank 1
        int[] mocRankCounter= new int[5];

        //scrape out the statistics
        /*************Add more statistics here. These were the ones I thought of**************/
        //Avg rank, number of iters that had a result rank 1 - 5
        for (DPAlgorithm iteration : dpAlgorithms) {
            sumDPRank += iteration.getChosenRank();
            int rank = (int)iteration.getChosenRank();
            if ( rank <= 5 && rank >= 1 ) {
                dpRankCounter[rank - 1]++;
            }
        }
        avgDPRank = (sumDPRank) / (double)numIterations;

        for (MathOfChoice moc : mathOfChoices) {
            sumMOCRank += moc.getChosenRank();
            System.out.println(moc.getChosenRank());
            int rank = (int)moc.getChosenRank();
            if ( rank <= 5 && rank >= 1 ) {
                mocRankCounter[rank - 1]++;

            }
        }
        avgMOCRank = (sumMOCRank) / (double)numIterations;

        this.avgRankDP = avgDPRank;
        this.avgRankMOC= avgMOCRank;
        this.rankCounterDP = dpRankCounter;
        this.rankCounterMOC= mocRankCounter;
    }

    public double getDPRankCounter1() {
        return rankCounterDP[0] / (double)numIterations;
    }
    public double getDPRankCounter2() {
        return rankCounterDP[1] / (double)numIterations;
    }
    public double getDPRankCounter3() {
        return rankCounterDP[2] / (double)numIterations;
    }
    public double getDPRankCounter4() {
        return rankCounterDP[3] / (double)numIterations;
    }
    public double getDPRankCounter5() {
        return rankCounterDP[4] / (double)numIterations;
    }

    public double getMOCRankCounter1() {
        return rankCounterMOC[0] / (double)numIterations;
    }

    public double getMOCRankCounter2() {
        return rankCounterMOC[1] / (double)numIterations;
    }
    public double getMOCRankCounter3() {
        return rankCounterMOC[2] / (double)numIterations;
    }
    public double getMOCRankCounter4() {
        return rankCounterMOC[3] / (double)numIterations;
    }
    public double getMOCRankCounter5() {
        return rankCounterMOC[4] / (double)numIterations;
    }


    public double getAvgRankDP() {
        return avgRankDP;
    }

    public double getAvgRankMOC() {
        return avgRankMOC;
    }
}
