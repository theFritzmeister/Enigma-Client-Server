package DMEngine;

import java.util.ArrayList;
import java.util.List;

public class BinomialPremuter {

    public static List<int[]> geAllCombinations(int n, int k, int[] allRotors) {
        List<int[]> combinations = new ArrayList<>();
        int[] combination = new int[k];

        // initialize with lowest lexicographic combination
        for (int i = 0; i < k; i++) {
            combination[i] = i;
        }

        while (combination[k - 1] < n) {
            int[] rotorsComb = new int[k];
            for (int i=0; i<k; i++){
                rotorsComb[i] = allRotors[combination[i]];
            }
            combinations.add(rotorsComb);

            // generate next combination in lexicographic order
            int t = k - 1;
            while (t != 0 && combination[t] == n - k + t) {
                t--;
            }
            combination[t]++;
            for (int i = t + 1; i < k; i++) {
                combination[i] = combination[i - 1] + 1;
            }
        }

        return combinations;
    }
}
