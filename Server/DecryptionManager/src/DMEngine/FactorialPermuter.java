package DMEngine;

public class FactorialPermuter {

    private int[] perms;
    private int[] indexPerms;
    private int[] directions;
    private int[] iSwap;
    private int N; //permute 0..N-1
    private int movingPerm = N;

    private int FORWARD = +1;
    private int BACKWARD = -1;

    private int[] ids;

    FactorialPermuter(int[] ids) {
        this.N = ids.length;
        this.ids = ids;
        perms = new int[N];     // permutations
        indexPerms = new int[N];     // index to where each permutation value 0..N-1 is
        directions = new int[N];     // direction = forward(+1) or backward (-1)
        iSwap = new int[N]; //number of swaps we make for each integer at each level
        for (int i = 0; i < N; i++) {
            directions[i] = BACKWARD;
            perms[i] = i;
            indexPerms[i] = i;
            iSwap[i] = i;
        }
        movingPerm = N;
    }

    int[] getNext() {
        //each call returns the next permutation
        do {
            if (movingPerm == N) {
                movingPerm--;
                int[] res = new int[this.ids.length];
                for (int i=0; i< this.perms.length; i++) {
                    res[i] = ids[perms[i]];
                }
                return res;


            } else if (iSwap[movingPerm] > 0) {
                //swap
                int swapPerm = perms[indexPerms[movingPerm] + directions[movingPerm]];
                perms[indexPerms[movingPerm]] = swapPerm;
                perms[indexPerms[movingPerm] + directions[movingPerm]] = movingPerm;
                indexPerms[swapPerm] = indexPerms[movingPerm];
                indexPerms[movingPerm] = indexPerms[movingPerm] + directions[movingPerm];
                iSwap[movingPerm]--;
                movingPerm = N;
            } else {
                iSwap[movingPerm] = movingPerm;
                directions[movingPerm] = -directions[movingPerm];
                movingPerm--;
            }
        } while (movingPerm > 0);
        return null;
    }
}


