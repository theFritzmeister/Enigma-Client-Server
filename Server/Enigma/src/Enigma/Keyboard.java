package Enigma;

import java.io.Serializable;

public class Keyboard implements Serializable {
    String abc;

    public Keyboard(String abc)
    {
        this.abc = abc;
    }

    public int forward(char ch)
    {
        return this.abc.indexOf(ch);
    }

    public char Backwards(int index)
    {
        return this.abc.charAt(index);
    }
}
