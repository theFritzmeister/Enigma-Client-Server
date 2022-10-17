package Enigma;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Reflector implements Serializable {
    private String id;
    private Map<Integer, Integer> map;

    public Reflector(String id, int size) throws Exception {
        if(checkID(id))
        {
            this.id = id;
        }
        else throw new Exception("[Reflector constructor]   id is not valid! must be one of 'I' 'II' 'III' 'IV' 'V' ");

        if(size%2 == 0)
        {
            this.map = randomReflector(size);

        }
        else throw new Exception("[Reflector constructor]   length of the ABC must be even ");
    }

    public Reflector(int abcLength ,String id, Map<Integer, Integer> reflectMap) throws Exception {
        if(checkID(id))
        {
            this.id = id;
        }
        else throw new Exception("[Reflector constructor]   id is not valid! must be one of 'I' 'II' 'III' 'IV' 'V' ");
        if(reflectMap.size() < (abcLength / 2)){
            throw new Exception("[Reflector constructor]  reflections are not complete  [id = " + this.id + "]");
        }
        for (Map.Entry<Integer,Integer> entry : reflectMap.entrySet()) {
            if (reflectMap.containsKey(entry.getValue())){
                throw new Exception("[Reflector constructor]    duplicated mapping      [id = " + this.id + "]");
            }
            if (entry.getKey() < 0 || entry.getValue() < 0 || entry.getKey() > abcLength-1 || entry.getValue()>abcLength-1){
                throw new Exception("[Reflector constructor]  reflect index is out of range...");
            }
        }
        this.map = reflectMap;

    }

    public static boolean checkID(String id) {
        return (id.equals("I") ||
                id.equals("II") ||
                id.equals("III") ||
                id.equals("IV") ||
                id.equals("V"));
    }

    public static int romanToInt(String roman){
        int res;
        switch (roman){
            case "I":
                res = 1;
                break;
            case "II":
                res = 2;
                break;
            case "III":
                res = 3;
                break;
            case "IV":
                res = 4;
                break;
            case "V":
                res = 5;
                break;
            default:return 0;
        }
        return res;
    }

    public static String intToRoman(int number){
        String res;
        switch (number){
            case 1:
                res = "I";
                break;
            case 2:
                res ="II";
                break;
            case 3:
                res = "III";
                break;
            case 4:
                res = "IV";
                break;
            case 5:
                res = "V";
                break;
            default:return "";
        }
        return res;
    }

    private Map<Integer, Integer> randomReflector(int size) {
        Map<Integer, Integer> res = new HashMap<Integer, Integer>();
        String used = "";
        for (int i= 0; i< (size/2) ; i++)
        {
            int randomIndex = ThreadLocalRandom.current().nextInt(size/2, size);
            while (used.contains(String.valueOf(randomIndex)))
            {
                randomIndex = ThreadLocalRandom.current().nextInt(size/2, size);
            }
            res.put(i, randomIndex);
            used = used + String.valueOf(randomIndex);
        }
        return res;
    }

    public int reflect(int index) {
        int res =-1;
        if(map.containsKey(index))
        {
            res = map.get(index);
        }
        else if (map.containsValue(index))
        {
            for(Map.Entry<Integer, Integer> entry: map.entrySet())
            {
                if (Objects.equals(index, entry.getValue()))
                {
                    res = entry.getKey();
                }
            }
        }
        return res;
    }

    public String getId() {
        return id;
    }
}
