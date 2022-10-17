package com.example.crackingtheenigmaserver;

import DMEngine.Decryptor;
import Enigma.EnigmaMachine;

import java.util.ArrayList;
import java.util.List;

public class Battlefield {

    UBoat uBoat = null;

    Decryptor dm;
    private String name;
    List<Allies> allieses = new ArrayList<Allies>();

    int numberOfAllies;
    int level = 0;

//    String massage = "";

    public Battlefield(UBoat uBoat){
        this.uBoat = uBoat;
    }


    public void spreadDecryptedMessage(String decryptedMessage) {
        for(Allies allies : allieses){
            allies.setDecrypedMessage(decryptedMessage);
        }
    }

    public void setNumberOfAllies(int numberOfAllies) {
        this.numberOfAllies = numberOfAllies;
    }

    public void setDm(Decryptor dm) {
        this.dm = dm;
        this.uBoat.setEnigmaMachine(dm.getMachine());
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setuBoat(UBoat uBoat) {
        this.uBoat = uBoat;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
