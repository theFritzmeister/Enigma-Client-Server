package com.example.crackingtheenigmaserver;

import DMEngine.Decryptor;
import Enigma.EnigmaMachine;

import generated.CTEDecipher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ControllerToEnigma {

    public static Decryptor UploadDecryptorFromXML(EnigmaMachine e, String fileName) throws Exception {
        CTEDecipher xmlDecypher = Decryptor.parseXmlDecipher(fileName);
        Decryptor d = new Decryptor(e, xmlDecypher);
        return d;
    }


    public static void setMachineAutomatically(EnigmaMachine e) throws Exception {
        int bound = e.getRotorsLength() < 100 ? e.getRotorsLength() : 100;
        int numberOfRotors = ThreadLocalRandom.current().nextInt(2, bound + 1);
        int[] rots = new int[numberOfRotors];
        List<Integer> used = new ArrayList<Integer>();
        for (int i = 0; i < rots.length; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(1, e.getRotorsLength() + 1);

            while (used.contains(randomIndex))
                randomIndex = ThreadLocalRandom.current().nextInt(1, e.getRotorsLength() + 1);
            rots[i] = randomIndex;
            used.add(randomIndex);
        }
        UpdateRotorsCode(rots, e);
        String startingpts = "";
        for (int i = 0; i < rots.length; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(0, e.getABC().length());
            startingpts = startingpts + e.getABC().charAt(randomIndex);
        }
        UpdateStartingPointsCode(startingpts.toCharArray(), e);

        int randomIndex = ThreadLocalRandom.current().nextInt(1, e.getReflectorsLength() + 1);
        UpdateReflectorCode(MakeRoman(randomIndex), e);

    }

//    public static void setMachineManually(EnigmaMachine e) throws Exception {
//        e.setRotorsInUse(SetCodeManuallyController.rotors);
//        e.setReflectorInUse(SetCodeManuallyController.reflector);
//        e.setStarts(SetCodeManuallyController.startingPoints);
//        e.setPlugs(SetCodeManuallyController.plugs);
//    }

    private static void UpdateReflectorCode(String reflectorId, EnigmaMachine e) throws Exception {
        e.setReflectorInUse(reflectorId);
    }

    private static void UpdateStartingPointsCode(char[] startingPtsStr, EnigmaMachine e) throws Exception {
            e.setStarts(startingPtsStr);
    }

    private static void UpdateRotorsCode(int[] rots, EnigmaMachine e) throws Exception {
        e.setRotorsInUse(rots);
    }

    private static String MakeRoman(int randomIndex) {
        String res = null;
        switch (randomIndex){
            case 1:
                res = "I";
                break;
            case 2:
                res = "II";
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
        }
        return res;
    }

    public static String sendMessage(EnigmaMachine e, String message) throws Exception{
        String line = message.toUpperCase();
        return e.EncriptString(line);
    }

    public static String getStatistics(EnigmaMachine e) {

        if (e != null) {
            String res = "";
            Map<String, List<String>> history = e.getHistory();
            // using for-each loop for iteration over Map.entrySet()
            for (Map.Entry<String, List<String>> entry : history.entrySet())
                res += "code : " + entry.getKey() + System.lineSeparator() +
                        "messages : " + System.lineSeparator() + entry.getValue();

            return res;
        }

        else{
            return "";
        }
    }
}