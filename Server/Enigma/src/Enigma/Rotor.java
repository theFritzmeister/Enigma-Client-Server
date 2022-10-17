package Enigma;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Rotor implements Serializable {
    private final int id;
    private char notch;
    private char startingpoint;
    private String abc;
    private char[] rightRing;
    private char[] leftRing;

    public Rotor(int id, char notch, char startingpoint, String abc) throws Exception {
        this.id = id;

        if(uniqueCharacters(abc.toUpperCase()))
        {
            this.abc = abc.toUpperCase();
        }
        else throw new Exception("[Rotor Constructor]   the ABC argument in not valid! - duplicated characters apears...");

        if(this.abc.contains(String.valueOf(Character.toUpperCase(notch))))
        {
            this.notch = Character.toUpperCase(notch);
        }
        else throw new Exception("[Rotor Constructor] notch argument in not valid!");

        if(this.abc.contains(String.valueOf(Character.toUpperCase(startingpoint))))
        {
            this.startingpoint = Character.toUpperCase(startingpoint);
        }
        else throw new Exception("[Rotor Constructor] starting point argument in not valid!");

        this.rightRing = this.abc.toCharArray();
        this.leftRing = Randomwiring();
    }

    public Rotor(int id, int notch, String abc, String right, String left) throws Exception {
        this.id = id;

        if(uniqueCharacters(abc.toUpperCase()))
        {
            this.abc = abc.toUpperCase();
        }
        else throw new Exception("[Rotor Constructor]   the ABC argument in not valid! - duplicated characters apears...");

        if(this.abc.length() >= notch && notch > 0)
        {
            this.notch = right.charAt(notch-1);
        }
        else throw new Exception("[Rotor Constructor] notch argument in not valid!       [id = " + id + "]");

        if(sameChars(right.toUpperCase(), this.abc) && sameChars(left.toUpperCase(), this.abc)){
            this.rightRing = right.toUpperCase().toCharArray();
            this.leftRing = left.toUpperCase().toCharArray();

        }
        else throw new Exception("[Rotor Constructor] the ring is not valid!      [id = " + id + "]");

        if(!uniqueCharacters(right.toUpperCase()) || !uniqueCharacters(left.toUpperCase()) )
            throw new Exception("[Rotor Constructor] the ring has duplicates!      [id = " + id + "]");

        this.startingpoint = this.rightRing[0];
    }

    private boolean sameChars(String firstStr, String secondStr) {
        char[] first = firstStr.toCharArray();
        char[] second = secondStr.toCharArray();
        Arrays.sort(first);
        Arrays.sort(second);
        return Arrays.equals(first, second);
    }

    boolean uniqueCharacters(String str)
    {
        // If at any time we encounter 2 same
        // characters, return false
        for (int i = 0; i < str.length(); i++)
            for (int j = i + 1; j < str.length(); j++)
                if (str.charAt(i) == str.charAt(j))
                    return false;

        // If no duplicate characters encountered,
        // return true
        return true;
    }

    private char[] Randomwiring() {
        StringBuilder res = new StringBuilder();
        String abcStack = this.abc;
        for(int i=0; i< this.abc.length(); i++)
        {
            int randomIndex = ThreadLocalRandom.current().nextInt(0, abcStack.length());
            char randomChar = abcStack.charAt(randomIndex);
            res.append(randomChar);
            abcStack = abcStack.substring(0,randomIndex) + abcStack.substring(randomIndex+1, abcStack.length());
        }
        return res.toString().toCharArray();
    }


    protected int forward(int index, boolean previousNotch)
    {
        if(previousNotch)
        {
            rotateRings();
        }

        char rightChar = this.rightRing[index];
        String str = String.valueOf(this.leftRing);
        int result = str.indexOf(rightChar);
        return  result;
    }

    protected int backward(int index)
    {
        char leftChar = this.leftRing[index];
        int result = String.valueOf(this.rightRing).indexOf(leftChar);
        return  result;
    }

    protected void resetRotorPosition()
    {
        int numberOfShifts = String.valueOf(this.rightRing).indexOf(this.startingpoint);
        for (int i = 0; i< numberOfShifts; i++)
        {
            rotateRings();
        }
    }


    private void rotateRings()
    {
        boolean hitNotch = false;
        char[] rotatedRing = new char[this.abc.length()];
        char carry = this.rightRing[0];
        int i;
        for (i = 1; i < this.rightRing.length; i++)
        {
            this.rightRing[i-1] = this.rightRing[i];
        }
        this.rightRing[rightRing.length -1] = carry;


        carry = this.leftRing[0];
        for (i = 1; i < this.leftRing.length; i++)
        {
            this.leftRing[i-1] = this.leftRing[i];
        }
        this.leftRing[leftRing.length -1] = carry;
    }

    public int notchDelta(){
        return String.copyValueOf(this.rightRing).indexOf(this.notch)+1;
    }
    public boolean isNotchInSlot()
    {
        if(this.rightRing[0] == this.notch)
        {
            return true;
        }
        return false;
    }

    public int getId(){
        return this.id;
    }

    public char getNotch() {
        return notch;
    }

    public char getStartingpoint() {
        return startingpoint;
    }

    public void setStartingpoint(char startingpoint) {
        this.startingpoint = startingpoint;
        resetRotorPosition();
    }

    public char getCurrentChar(){
        return rightRing[0];
    }


}