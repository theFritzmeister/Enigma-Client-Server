package DMEngine;

public class Configuration {
    private int[] rotors;
    private char[] startingPoints;
    private  String reflector;


    public Configuration(int[] rotors, char[] startingPoints, String reflector) {
        this.rotors = rotors;
        this.startingPoints = startingPoints;
        this.reflector = reflector;
    }

    public String getReflector() {
        return reflector;
    }

    public char[] getStartingPoints() {
        return startingPoints;
    }

    public int[] getRotors() {
        return rotors;
    }
}
