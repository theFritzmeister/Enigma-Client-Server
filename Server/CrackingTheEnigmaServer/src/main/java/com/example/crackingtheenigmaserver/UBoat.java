package com.example.crackingtheenigmaserver;

import DMEngine.Decryptor;
import Enigma.EnigmaMachine;

import java.util.ArrayList;
import java.util.List;

public class UBoat {

    String uBoatName;
    private EnigmaMachine enigmaMachine = null;

    String massage = "";

    public UBoat(String uBoatName) {
        this.uBoatName = uBoatName;
    }
    public EnigmaMachine getEnigmaMachine() {
        return enigmaMachine;
    }



    public Allies determineWinner() {
        return null;
    }

    public void setEnigmaMachine(EnigmaMachine enigmaMachine) {
        this.enigmaMachine = enigmaMachine;
    }

}
