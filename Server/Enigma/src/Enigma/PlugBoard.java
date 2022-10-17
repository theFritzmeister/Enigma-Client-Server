package Enigma;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlugBoard implements Serializable {
    private Map<Character, Character> map;
    String abc;
    private int size;

    public PlugBoard(String abc, Pair<Character, Character>[] pairs) throws Exception {
        this.map = new HashMap<Character, Character>() ;
        this.abc = abc.toUpperCase();
        this.size = 0;
        if(pairs != null)
        {
            Plug(pairs);
        }
    }

    public PlugBoard(String abc)
    {
        this.map = new HashMap<Character, Character>() ;
        this.abc = abc.toUpperCase();
        this.size = 0;
    }

    public void Plug(Pair<Character, Character>[] pairs) throws Exception {
        this.map = new HashMap<Character, Character>() ;
        String used = "";
        for (Pair<Character,Character> pair: pairs)
        {
            if(this.abc.contains(pair.getKey().toString()) && this.abc.contains(pair.getValue().toString()))
            {
                if(used.contains(pair.getKey().toString()) || used.contains(pair.getValue().toString()) || (pair.getKey().equals(pair.getValue())) )
                {
                    throw new Exception("[Plug-board constructor]  some duplicate characters in plugboard's pairs...");
                }
                map.put(pair.getKey(), pair.getValue());
                map.put( pair.getValue(), pair.getKey());
                used = used + pair.getKey() + pair.getValue();
            }
            else throw new Exception("[Plug-board constructor]  some of the characters are not in ABC...");
        }

        this.size = map.size();

    }

    public char forward(char ch)
    {
        if(map.containsKey(ch))
        {
            return map.get(ch);
        }
        return ch;
    }

    public int getSize() {
        return size;
    }

    public Map<Character, Character> getMap() {
        return map;
    }


    @Override
    public String toString() {
        if (map.size() == 0)
        {
            return null;
        }
        StringBuilder str = new StringBuilder();
        String used = "";
        for (Map.Entry<Character,Character> entry : map.entrySet())
        {
            char key = entry.getKey();
            char value = entry.getValue();
            if(used.contains(String.valueOf(key)) || used.contains(String.valueOf(value)))
            {
                continue;
            }
            else {
                str.append(String.valueOf(key) + "|" + String.valueOf(value) + ",");
                used = used + key + value;
            }
        }
        str.deleteCharAt(str.length() - 1);
        return str.toString();
    }
}
