package com.example.crackingtheenigmaserver;

import DMEngine.Decryptor;
import Enigma.EnigmaMachine;

import java.util.ArrayList;
import java.util.List;

public class Allies {

    EnigmaMachine enigmaMachine = null;
    Decryptor decryptor = null;
    List<Agent> agents = new ArrayList<Agent>();
    String decrypedMessage = "";

    public void setDecrypedMessage(String decryptedMessage) {
        this.decrypedMessage = decryptedMessage;
    }
}
