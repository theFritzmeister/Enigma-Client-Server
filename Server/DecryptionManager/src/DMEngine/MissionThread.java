package DMEngine;

import Enigma.EnigmaMachine;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;


class MissionThread implements Runnable {
    private int id;
    private int missionSize;

    private  int difficulty;
    private  EnigmaDictionary dictionary;
    private final String secretToCrack;
    private EnigmaMachine machine;

    public String missionDuration;

    private final BlockingQueue<Candidate> candidatesQueue;
    //private final BlockingQueue<Configuration> configurationQueue;
    private Configuration configuration;

    private Progress progress;

    public MissionThread(int id, int missionSize, EnigmaDictionary dictionary, String secretToCrack, EnigmaMachine machine, int difficulty, BlockingQueue<Candidate> candidatesQueue, Configuration configuration, Progress progress)
    {
        this.id = id;
        this.missionSize = missionSize;
        this.dictionary = dictionary;
        this.secretToCrack = secretToCrack;
        this.machine = machine;
        this.difficulty = difficulty;
        this.candidatesQueue = candidatesQueue;
        this.configuration = configuration;
        this.progress = progress;
    }

    public String forward() throws Exception {
        String result = machine.EncriptString(secretToCrack);
        machine.Reset();

        return  result;
    }

    private int checkAccuracy(String text){
        List<String> words = Arrays.asList(text.split(" "));
        int matches = 0;
        for (String word: words) {
            if (dictionary.isInDictionary(word)){
                matches += 1;
            }
        }
        return matches * 100 / words.size();

    }

    public void run() {

        List<Pair<String,String>> results = new ArrayList<>();
        long start1 = System.nanoTime();

        for (int i=0; i < this.missionSize; i++) {

            try {
                if(i>0){
                    machine.forward(machine.getABC().charAt(0));
                    setConfigurationsToCheck(takeConfigFromMachine());
                }

                String res = forward();

                if(this.checkAccuracy(res) > 90){
                    results.add(new Pair<>(res, this.machine.getCode()));
                }
//                if (i%100 == 0){
//                    results.add(new Pair<>(res, this.machine.getCode()));
//                }

            } catch (Exception e) {

                throw new RuntimeException(e);
            }

        }
        long end1 = System.nanoTime();
        long sum = end1 - start1;
        this.missionDuration = sum + " MS";
        if(results.size() !=  0){
//            System.out.println("Candidate: " + results.get(0).getKey() + "  |   " + results.get(0).getValue());
            try {
                candidatesQueue.put(new Candidate(results, Thread.currentThread().getName(), missionDuration));

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.progress.finishedMission();
    }
    private Configuration takeConfigFromMachine(){
        int rotorsLen = machine.getRotorsInUseLength();
        int[] rotors = new int[rotorsLen];
        char[] startigs = new char[rotorsLen];
        for(int i=0; i< rotorsLen; i++){
            rotors[i] = machine.getRotorsInUse()[i].getId();
            startigs[rotorsLen-i-1] = machine.getRotorsInUse()[i].getCurrentChar();
        }
        String refId = machine.getReflectorId();
        Configuration res = new Configuration(rotors, startigs, refId);
        return res;
    }
    public void setConfigurationsToCheck(Configuration conf){

        try {
            machine.setRotorsInUse(conf.getRotors());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            machine.setStarts(conf.getStartingPoints());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            machine.setReflectorInUse(conf.getReflector());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}