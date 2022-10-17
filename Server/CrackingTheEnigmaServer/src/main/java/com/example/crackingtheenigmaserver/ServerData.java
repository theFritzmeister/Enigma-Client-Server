package com.example.crackingtheenigmaserver;

import DMEngine.Decryptor;
import Enigma.EnigmaMachine;
import generated.CTEBattlefield;
import generated.CTEEnigma;
import generated.XMLParser;

import java.io.*;

import java.util.ArrayList;

import java.util.List;

public class ServerData {

    int numberOfBattlefields = 0;
    private final List<Battlefield> battlefields = new ArrayList<Battlefield>();

    boolean isOn = true;

    public String getEnigmaInfo(int competitionID) {

        if (numberOfBattlefields == 0){
            return "No competitions yet...";
        }

        EnigmaMachine machine = battlefields.get(competitionID).uBoat.getEnigmaMachine();
        if (machine == null){
            return "No EnigmaMachine yet in UBoat" + battlefields.get(competitionID).getName() +  "...";
        }
        return machine.toString();
    }

    public boolean isUBoatNameAvailable(String uBoatName) {
        for (Battlefield bf : battlefields) {
            if (uBoatName.equals(bf.uBoat.uBoatName)) {
                return false;
            }
        }

        return true;
    }

    public void initializeBattleField(UBoat uBoat) {
        battlefields.add(new Battlefield(uBoat));
        numberOfBattlefields++;
    }

    public String setUBoatsXmlFile(String xmlFile, String uBoatName) throws Exception {
        CTEEnigma xmlObject = XMLParser.parseXMLStringToObject(xmlFile);
        EnigmaMachine machine = EnigmaMachine.parseXML(xmlObject);
        Decryptor dm = new Decryptor(machine, xmlObject.getCTEDecipher());
        CTEBattlefield cteBf = xmlObject.getCTEBattlefield();
        String bfName = cteBf.getBattleName();
        int allies = cteBf.getAllies();
        String level = cteBf.getLevel();

        Battlefield bf = searchBf(uBoatName);

        bf.setNumberOfAllies(allies);
        bf.setDm(dm);
        bf.setLevel(levelToInt(level.toLowerCase()));
        bf.setName(bfName);

        return bf.uBoat.getEnigmaMachine().toString();
    }

    private int levelToInt(String level) {
        int res;
        switch (level){
            case "easy":
                res=1;
                break;
            case "medium":
                res=2;
                break;
            case "hard":
                res=3;
                break;
            case "impossible":
                res=4;
                break;
            default:
                res=-1;
        }
        return res;
    }

    private Battlefield searchBf(String uBoatName) {

        if(numberOfBattlefields == 0)
            return null;

        for (Battlefield bf: battlefields) {
            if( bf.uBoat.uBoatName.equals(uBoatName))
                return bf;
        }
        return null;
    }


    private FileOutputStream setFileContent(String xmlFileInBytes) throws IOException {

        System.out.println(xmlFileInBytes);
//        int i = 0;
//        while ((i != xmlFileInBytes.length() - 1)) {
//
//            System.out.print((char)i);
//        }
        FileOutputStream res = null;
        return res;
    }

    public String setMachineAutomatically(String uBoatName){
        Battlefield bf = searchBf(uBoatName);
        if(bf == null){
            return "no Machine...";
        }
        try {
            ControllerToEnigma.setMachineAutomatically(bf.uBoat.getEnigmaMachine());
            return bf.uBoat.getEnigmaMachine().getCode();
        } catch (Exception e) {
            return e.getMessage();
        }

    }
}
