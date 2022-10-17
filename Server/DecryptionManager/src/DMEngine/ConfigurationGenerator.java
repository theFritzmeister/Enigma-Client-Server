package DMEngine;

import Enigma.EnigmaMachine;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import static java.lang.Math.pow;

public class ConfigurationGenerator implements Runnable{

    private final String secretToCrack;
    private final double fixedMachineOptions;
    private EnigmaMachine machine;
    private EnigmaDictionary dictionary;
    private int missionSize;
    private int level;

    private long totalMissions;

    private int missionCounter = 0;

    private long possibleConfigurations;

    private Progress progress;

    private final BlockingQueue<Runnable> missionsQueue;
    private final BlockingQueue<Candidate> candidatesQueue;





    public ConfigurationGenerator(String secretToCrack, EnigmaDictionary dictionary, int missionSize, int level, long totalMissions, long possibleConfigurations, BlockingQueue<Runnable> missionsQueue,  BlockingQueue<Candidate> candidatesQueue, Progress progress) {
        this.secretToCrack = secretToCrack;
        this.machine = Decryptor.cloneMachine();
        this.missionSize = missionSize;
        this.level = level;
        this.totalMissions = totalMissions;
        this.possibleConfigurations = possibleConfigurations;
        this.missionsQueue = missionsQueue;
        this.candidatesQueue = candidatesQueue;
        this.dictionary = dictionary;
        this.fixedMachineOptions =  Math.pow(this.machine.getABC().length(), this.machine.getRotorsInUseLength());
        this.progress = progress;
    }

    public long getPossibleConfigurations() {
        return possibleConfigurations;
    }

    public long getTotalMissions() {
        return totalMissions;
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

    @Override
    public void run() {
        missionCounter = 0;
        switch (this.level){
            case 1:
                generateEasyConfig();
                break;
            case 2:
                try {
                    generateMediumConfig();

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;

            case 3:
                try {
                    generateHardConfig();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;

            case 4:
                try {
                    generateImposiibleConfig();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;

        }


    }

    private void generateEasyConfig(){
        for(int i=0; i< fixedMachineOptions; i++){
            missionCounter++;
            if(i>0){
                machine.forward(machine.getABC().charAt(0));
            }

            if(i%this.missionSize == 0){
                try {
//                    System.out.println(machine.getCode());
                    Configuration conf = takeConfigFromMachine();
                    setConfigurationsToCheck(conf);
                    EnigmaMachine.serializeMachine( "MachineForMission" , machine);
                    EnigmaMachine cloned = cloneMachine();/////123AAA
//                    System.out.println(cloned.getCode());
                    this.missionsQueue.put(new MissionThread(missionCounter, this.missionSize, dictionary, this.secretToCrack, cloned, this.level, this.candidatesQueue, takeConfigFromMachine(), this.progress));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }

    }

    public static EnigmaMachine cloneMachine(){
        try {
            return EnigmaMachine.deserializeMachine("MachineForMission.txt");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
    private void  generateMediumConfig() throws Exception {
        String[] reflectorsId = machine.getReflectorsIds();

        for(int i=0; i< this.machine.getReflectorsLength(); i++){
            machine.setReflectorInUse(reflectorsId[i]);
            generateEasyConfig();
        }
    }

    private void  generateHardConfig() throws Exception {
        int[] rotors = takeConfigFromMachine().getRotors();
        long factorial = Decryptor.factorial(rotors.length);
        FactorialPermuter premuter = new FactorialPermuter(rotors);

        for(int i=0; i< factorial; i++){
            machine.setRotorsInUse(premuter.getNext());
            generateMediumConfig();
        }
    }

    private void generateImposiibleConfig() throws Exception {
        int[] allRotorsId = this.machine.getAllRotorsId();
        long bunomial = Decryptor.nOnK(allRotorsId.length, this.machine.getRotorsInUseLength());
        List<int[]> binomialCombinations = BinomialPremuter.geAllCombinations(allRotorsId.length, this.machine.getRotorsInUseLength(), allRotorsId);
        for (int[] binConfig: binomialCombinations) {
            machine.setRotorsInUse(binConfig);
            generateHardConfig();
        }
    }
}
