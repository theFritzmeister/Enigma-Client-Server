package Enigma;
//test
import com.sun.webkit.dom.ElementImpl;
import javafx.util.Pair;
import generated.*;
import java.io.*;
import java.util.*;


public class EnigmaMachine implements Serializable {

    private Rotor[] rotors;

    private Rotor[] rotorsInUse = null;
    private Reflector reflectorInUse = null;

    private Reflector[] reflectors;

    private PlugBoard plugs;
    private Keyboard keyboard;
    private final String ABC;
    public List<String> messegesHistory;

    public Map<String, List<String>> statistics;

    public EnigmaMachine(int numberOfRotors, String abc) throws Exception {
        rotors = new Rotor[numberOfRotors];

        for (int i = 0; i < numberOfRotors; i++) {
            rotors[i] = new Rotor(i + 1, abc.charAt(i), abc.charAt(0), abc);
        }
        reflectors = new Reflector[1];
        reflectors[0] = new Reflector("I", 6);
        this.ABC = abc.toUpperCase();
        rotorsInUse = rotors;
        reflectorInUse = reflectors[0];
        keyboard = new Keyboard(this.ABC);
        messegesHistory = new ArrayList<String>();
        statistics = new HashMap<>();
        this.plugs = new PlugBoard(this.ABC);

    }

    public EnigmaMachine(String i_abc, int rotorCount, Rotor[] i_rotors, Reflector[] i_reflectors) throws Exception {
        if (!uniqueCharacters(i_abc.toUpperCase())) {
            throw new Exception("[Enigma-machine constructor]   The abc contains duplicate characters...");
        }
        if (!(i_rotors.length >= rotorCount && rotorCount >= 2)) {
            throw new Exception("[Enigma-machine constructor]   rotor-count invalid (" + rotorCount + ")...");
        }
        this.ABC = i_abc.toUpperCase();
        this.rotorsInUse = new Rotor[rotorCount];
        this.rotors = i_rotors;
        this.reflectors = i_reflectors;
        this.messegesHistory = new ArrayList<String>();
        this.statistics = new HashMap<>();
        this.plugs = new PlugBoard(this.ABC);
        this.keyboard = new Keyboard(this.ABC);
    }

    public String EncriptString(String str) throws Exception {
        long start1 = System.nanoTime();
        StringBuilder res = new StringBuilder();
        String currentCode = this.getCode();

        for (char ch : str.toUpperCase().toCharArray()) {
            if (this.ABC.contains(String.valueOf(ch))) {
                res.append(forward(ch));
            } else {
                String msg = "the letter '" + String.valueOf(ch) + "' is not part of the ABC...";
                throw new Exception(msg);
            }
        }
        long end1 = System.nanoTime();
        messegesHistory.add(str.toUpperCase() + "-->" + res.toString() + "(" + String.valueOf((end1 - start1)) + ")");
        String operationLogs = str.toUpperCase() + "-->" + res.toString() + "(" + String.valueOf((end1 - start1)) + ")";
        if (!statistics.containsKey(currentCode)) {
            statistics.put(currentCode, new ArrayList<>());

        }
        statistics.get(currentCode).add(operationLogs);
        return res.toString();
    }

    public char forward(char ch) {
        char fromPlugs = this.plugs.forward(ch);
        int index = keyboard.forward(fromPlugs);
        for (int i = 0; i < rotorsInUse.length; i++) {
            boolean rotate = false;
            if (i == 0) {
                rotate = true;
            } else if (rotorsInUse[i - 1].isNotchInSlot()) {
                rotate = true;
            }
            index = rotorsInUse[i].forward(index, rotate);
        }
        index = reflectorInUse.reflect(index);
        for (int i = rotorsInUse.length - 1; i >= 0; i--) {
            index = rotorsInUse[i].backward(index);
        }
        char toPlugs = keyboard.Backwards(index);

        return this.plugs.forward(toPlugs);
    }

    public void Reset() {
        for (Rotor r : rotors) {
            r.resetRotorPosition();
        }
    }

    public Map<String, List<String>> getHistory() {
        return this.statistics;
    }

    public String getABC() {
        return ABC;
    }

    public String getCode() {
        StringBuilder res = new StringBuilder();

        //<rotors id><starting points><reflector id><plugs>

        res.append('<');
        for (int i = rotorsInUse.length - 1; i >= 0; i--) {
            res.append(rotorsInUse[i].getId());
            if (i != 0) {
                res.append(',');
            }
        }
        res.append("><");
        for (int i = rotorsInUse.length - 1; i >= 0; i--) {
            res.append(rotorsInUse[i].getStartingpoint() + "(" + rotorsInUse[i].notchDelta() + ")");
        }
        res.append('>');

        res.append('<' + reflectorInUse.getId() + '>');

        if (plugs.getSize() != 0) {
            res.append('<' + plugs.toString() + '>');
        }
        res.append(System.lineSeparator());

        return res.toString();

    }


    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();

        res.append("Number of optional rotors: " + this.rotors.length + System.lineSeparator());
        res.append("Number of rotors in use: " + this.rotorsInUse.length + System.lineSeparator());
        res.append("Number of reflectors: " + this.reflectors.length + System.lineSeparator());
        res.append("notch position in each rotor: " + System.lineSeparator());
        for (int i = 0; i < rotors.length; i++) {
            int id = rotors[i].getId();
            char notch = rotors[i].getNotch();
            res.append("Rotor " + id + " - " + (keyboard.forward(notch) + 1) + System.lineSeparator());

        }
        res.append(System.lineSeparator());

        res.append("number of messages transferred: " + messegesHistory.size() + System.lineSeparator());
        if (this.reflectorInUse != null) {
            res.append(getCode());
        }

        return res.toString();

    }

    public static boolean uniqueCharacters(String str) {
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


    public void setRotorsInUse(int[] rots) throws Exception {
        if (rots.length > this.rotors.length || rots.length < 2) {
            throw new Exception("number of rotors to use is invalid" + rots.length);
        }
        if (!areDistinct(rots)) {
            throw new Exception("Cant Set same rotor twice.");
        }

        for (int i = 0; i < rots.length; i++)
            this.rotorsInUse = new Rotor[rots.length];
        for (int i = 0; i < rots.length; i++) {
            int id = rots[i];
            if (id <= 0 || id > this.rotors.length)
                throw new Exception("there is no rotor with id=" + id);

            try {
                for (int j = 0; j < rotors.length; j++) {
                    if (id == rotors[j].getId()) {
                        this.rotorsInUse[i] = this.rotors[j];
                        break;
                    }

                }
            } catch (Exception e) {
                throw new Exception("there is no rotor with id=" + rots[i]);
            }
        }
    }


    public static boolean areDistinct(int array[]) {
        Integer[] arr = Arrays.stream(array).boxed().toArray(Integer[]::new);
        // Put all array elements in a HashSet
        Set<Integer> s = new HashSet<Integer>(Arrays.asList(arr));
        // If all elements are distinct, size of
        // HashSet should be same array.
        return (s.size() == arr.length);
    }

    public void setStarts(char[] startingPoints) throws Exception {
        if (startingPoints.length != rotorsInUse.length) {
            throw new Exception("number of starting points is incorrect..." + System.lineSeparator() + "should be " + rotorsInUse.length);
        }
        for (char ch : startingPoints) {
            if (!this.ABC.contains(String.valueOf(ch)))
                throw new Exception(ch + " is not in the ABC...");
        }
        for (int i = 0; i < this.rotorsInUse.length; i++) {
            rotorsInUse[i].setStartingpoint(startingPoints[startingPoints.length - 1- i] );
        }
    }

    public void setReflectorInUse(String id) throws Exception {
        if (id == null) {
            throw new Exception("invalid id...");

        }
        if (Reflector.checkID(id)) {
            boolean found = false;
            for (Reflector ref : reflectors) {
                if (ref.getId().equals(id)) {
                    reflectorInUse = ref;
                    found = true;
                }
            }
            if (!found) {
                throw new Exception("reflector not found by id...");
            }
        } else {
            throw new Exception("The givven reflector's id is invalid. (must be I-V in roman numbers)...");
        }
    }

    public void setPlugs(Pair<Character, Character>[] pairs) throws Exception {
        if (pairs == null) {
            this.plugs = new PlugBoard(this.ABC);
        } else {
            this.plugs = new PlugBoard(this.ABC, pairs);
        }
    }

    public int getRotorsLength() {
        return rotors.length;
    }

    public int getReflectorsLength() {
        return reflectors.length;
    }

    public int getRotorsInUseLength() {
        return rotorsInUse.length;
    }

    public static void serializeMachine(String fileName, EnigmaMachine machine) {
        try {

            FileOutputStream fileOutputStream = new FileOutputStream(fileName + ".txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(machine);
            objectOutputStream.flush();
            objectOutputStream.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public static EnigmaMachine deserializeMachine(String fileName) throws Exception {
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            EnigmaMachine machine = (EnigmaMachine) objectInputStream.readObject();
            objectInputStream.close();
            return machine;
        } catch (Exception e) {
            throw new Exception("failed deseralize " + fileName);
        }
    }

    public static EnigmaMachine parseXML(CTEEnigma cteEnigma) throws Exception {
        //CTEEnigma cteEnigma = XMLParser.parseXMLtoObject(fileName);
        Rotor[] rotorsFromXML = parseRotorsFromXML(cteEnigma);
        Reflector[] reflectorsFromXML = parseReflectorsFromXML(cteEnigma);
        String abcFromXML = parseAbcFromXML(cteEnigma);
        int rCount = cteEnigma.getCTEMachine().getRotorsCount();


        EnigmaMachine res = new EnigmaMachine(abcFromXML, rCount, rotorsFromXML, reflectorsFromXML);

        return res;


    }

    private static Reflector[] parseReflectorsFromXML(CTEEnigma cteEnigma) throws Exception {

        List<CTEReflector> cteReflectors = cteEnigma.getCTEMachine().getCTEReflectors().getCTEReflector();
        List<Reflector> reflectors = new ArrayList<Reflector>();
        List<Integer> idis = new ArrayList<Integer>();
        String abc = parseAbcFromXML(cteEnigma);

        for (CTEReflector ref : cteReflectors) {
            String id = ref.getId();
            idis.add(Reflector.romanToInt(id));
            Map<Integer, Integer> mapping = parseReflectMap(ref);

            reflectors.add(new Reflector(abc.length(), id, mapping));
        }

        Collections.sort(idis);
        for (int i = 0; i < idis.size(); i++) {
            if (idis.get(i) != i + 1) {
                throw new Exception("reflectors id's are not serial...");
            }
        }

        Reflector[] res = new Reflector[reflectors.size()];
        res = reflectors.toArray(res);
        return res;
    }

    private static Map<Integer, Integer> parseReflectMap(CTEReflector ref) throws Exception {
        Map<Integer, Integer> mapping = new HashMap<Integer, Integer>();
        List<CTEReflect> reflects = ref.getCTEReflect();
        for (CTEReflect item : reflects) {
            if (mapping.containsKey(item.getInput() - 1) || mapping.containsKey(item.getOutput() - 1))
                throw new Exception("[Reflector Parser]     reflector is invalid... (duplicate inputs)");
            mapping.put(item.getInput() - 1, item.getOutput() - 1);
        }
        return mapping;
    }
    public String getCurrentRotorsState() {
        String letters = "";

        for (Rotor r: rotorsInUse) {
            letters += r.getCurrentChar();
        }

        StringBuilder res = new StringBuilder();
        res.append(letters);
        res.reverse();

        return res.toString();
    }
    private static Rotor[] parseRotorsFromXML(CTEEnigma cteEnigma) throws Exception {
        List<CTERotor> cteRotors = cteEnigma.getCTEMachine().getCTERotors().getCTERotor();
        String abc = parseAbcFromXML(cteEnigma);
        List<Rotor> rotors = new ArrayList<Rotor>();
        List<Integer> idis = new ArrayList<Integer>();
        for (CTERotor rot : cteRotors) {
            int id = rot.getId();
            if (idis.contains(id)) {
                throw new Exception("The rotor id '" + id + "' appears multiple times...");
            }
            idis.add(id);
            if (id > cteRotors.size()) {
                throw new Exception("The rotor id '" + id + "' is out of range of rotors...");
            }
            int notch = rot.getNotch();
            String right = getRing(rot, "R");
            String left = getRing(rot, "L");

            rotors.add(new Rotor(id, notch, abc, right, left));
        }
        Collections.sort(idis);
        for (int i = 0; i < idis.size(); i++) {
            if (idis.get(i) != i + 1) {
                throw new Exception("Rotors id's are not serial...");
            }
        }
        Rotor[] res = new Rotor[rotors.size()];
        res = rotors.toArray(res);
        return res;
    }

    private static String getRing(CTERotor rot, String str) {
        List<CTEPositioning> positions = rot.getCTEPositioning();
        StringBuilder right = new StringBuilder();
        StringBuilder left = new StringBuilder();

        for (CTEPositioning pos : positions) {
            right.append(pos.getRight());
            left.append(pos.getLeft());
        }
        if (str.equals("R")) {
            return right.toString();
        } else {
            return left.toString();
        }
    }

    private static String parseAbcFromXML(CTEEnigma cteEnigma) throws Exception {
        String res = cteEnigma.getCTEMachine().getABC().trim();
        if (uniqueCharacters(res)) {
            if (res.length() % 2 == 0) {
                return res;
            } else
                throw new Exception("[XML Parser]   the ABC argument in not valid! - odd number of characters apears...");

        } else throw new Exception("[XML parserr]   the ABC argument in not valid! - duplicated characters apears...");
    }

    public Rotor[] getRotorsInUse() {
        return rotorsInUse;
    }

    public String getReflectorId(){
        return reflectorInUse.getId();
    }

    public String[] getReflectorsIds() {
        String[] res = new String[reflectors.length];
        for (int i=0; i<reflectors.length; i++){
            res[i] = reflectors[i].getId();
        }
        return res;
    }

    public int[] getAllRotorsId() {
        int[] res = new int[rotors.length];
        for (int i=0; i<rotors.length; i++){
            res[i] =rotors[i].getId();
        }
        return res;
    }


}
