package com.example.ChoiceImpl;

import java.util.LinkedList;
import java.util.zip.CheckedOutputStream;

/**
 * Created by Ben on 5/6/2017.
 * Pretty much identical to DPAlgorithm, but made to work with the Date app flow.
 */

public class DateStoppingAlgorithm extends ChoiceAlgorithm {
    private int numCandidates;
    private LinkedList<Date> datesInChronoOrder;

    private double[] c;
    private int[] s;

    public DateStoppingAlgorithm(int numTotalDates) {
        numCandidates = numTotalDates;
        datesInChronoOrder = new LinkedList<Date>();
        c = new double[numCandidates];
        s = new int[numCandidates];
        c = generateCValues();
    }

    //Populates s[] and returns c[]
    private double[] generateCValues() {
        double[] tentCVal = new double[numCandidates];
        tentCVal[numCandidates - 1] = (numCandidates + 1) / 2;
        //System.out.println(numCandidates + " NC")
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
            System.out.println(tentCVal[i] + " " + s[i]);
        }
        return tentCVal;
    }

//    //Returns which i satisfies rr[i] < s[i], or -1 if not found.
//    //Even when the optimal I is found, it continues to fully create the sorted set. Used for stats later
//    private int getOptimalValue() {
//        SortedSet<Double> seen = new TreeSet<Double>();
//        int[] relRank = new int[numCandidates];
//        boolean continueLooking = true;
//        int optimalI = -1;
//
//        for (int i = 0; i < numCandidates; i++) {
//            seen.add(candidates[i]);
//            relRank[i] = seen.tailSet(candidates[i]).size();
//            if (relRank[i] < s[i] & continueLooking) {
//                optimalI = i;
//                continueLooking = false;
//            }
//        }
//        this.rankFinder = seen;
//        return optimalI;
//    }

    //RET TRUE if this is the stopping element
    public boolean addNewDate(Date d, int relrank) {
        datesInChronoOrder.add(d);
        System.out.println("relrank, s " + relrank + " " + s[datesInChronoOrder.size()] + " " + datesInChronoOrder.size());
        if (relrank <= s[datesInChronoOrder.size()]) //RR 1 indexed, dates 0 indexed
            return true;
        return false;
    }

    @Override
    public double getChosenValue() {
        return 0;
    }

    @Override
    public double getChosenRank() {
        return 0;
    }
}
