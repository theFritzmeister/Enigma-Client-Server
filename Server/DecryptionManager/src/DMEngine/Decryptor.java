package DMEngine;

import Enigma.EnigmaMachine;
import generated.CTEDecipher;
import generated.XMLParser;

import java.util.*;
import java.util.concurrent.*;

public class Decryptor {

    private final int MAX_QUEUE_LEN = 1000;
    private EnigmaMachine machine;

    private int numberOfAgents;
    private int missionSize;
    private long numberOfMissions;
    private int level;
    private long totalConfigs;
    private String msgToDecrypt;
    private EnigmaDictionary dictionary;

    private List<String> results;

    private Progress progress;

    private ThreadPoolExecutor threadExecutor;

    private BlockingQueue<Runnable> threadPoolBlockingQueue;
    private BlockingQueue<Candidate> candidatesQueue;
//    private BlockingQueue<Configuration> configurationsQueue;



    private Thread collector;
    private Thread taskProducer;



    public Decryptor( EnigmaMachine machine, CTEDecipher xmlDecipher) throws Exception {

        this.threadPoolBlockingQueue = new LinkedBlockingQueue<>(MAX_QUEUE_LEN);
        this.numberOfAgents = 1;
        EnigmaMachine.serializeMachine("Enigma-For-DM_DO_NOT_TOUCH", machine);
        this.machine = cloneMachine();
        this.dictionary = new EnigmaDictionary(xmlDecipher.getCTEDictionary(), machine.getABC());

    }

    public static CTEDecipher parseXmlDecipher(String filename) throws Exception {
        CTEDecipher res = XMLParser.parseXMLtoObject(filename).getCTEDecipher();
        return res;
    }
    public static EnigmaMachine cloneMachine(){
        try {
            return EnigmaMachine.deserializeMachine("Enigma-For-DM_DO_NOT_TOUCH.txt");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void setLevel(int level) {
        this.level = level;
    }

    public BlockingQueue<Candidate> getCandidatesQueue() {
        return candidatesQueue;
    }

    public EnigmaDictionary getDictionary() {
        return dictionary;
    }

    public BlockingQueue<Runnable> getThreadPoolBlockingQueue() {
        return threadPoolBlockingQueue;
    }


    public void setMsgToDecrypt(String msgToDecrypt) {
        this.msgToDecrypt = msgToDecrypt;
    }


    private void setMissions(){
        switch (level){
            case 1:
                this.totalConfigs = (long) Math.pow(machine.getABC().length(), machine.getRotorsInUseLength());
                break;
            case 2:
                this.totalConfigs = (long) Math.pow(machine.getABC().length(), machine.getRotorsInUseLength()) * this.machine.getReflectorsLength();
                break;
            case 3:
                this.totalConfigs = (long) (  factorial(machine.getRotorsInUseLength()) * (Math.pow(machine.getABC().length(), machine.getRotorsInUseLength()) * machine.getReflectorsLength()));
                break;
            case 4:
                this.totalConfigs = (long) ( nOnK(machine.getRotorsLength(), machine.getRotorsInUseLength()) * factorial(machine.getRotorsInUseLength()) * (Math.pow(machine.getABC().length(), machine.getRotorsInUseLength()) * machine.getReflectorsLength()));
                break;
        }
        this.numberOfMissions = this.totalConfigs / this.missionSize;
    }

    public static long nOnK(int n, int k) {
        return factorial(n) / (factorial(k) * factorial(n - k));
    }

    public static long factorial(int number) {
        long fact = 1;
        for (int i = 1; i <= number; i++) {
            fact = fact * i;
        }
        return fact;
    }

    public void setNumberOfAgents(int numberOfAgents) {
        this.numberOfAgents = numberOfAgents;
    }

    private boolean isValidMessage(String msg) {
        boolean validStr = msg.matches("[" + machine.getABC() +"]+");
        return  validStr;
    }

    private String stripInput(String input) throws Exception {
        String reg = "[" + this.dictionary.getExcluded() +"]*";
        input = input.replaceAll(reg, "").toUpperCase();
        for (String word:input.split(" ")) {
            if(!this.dictionary.isInDictionary(word)){
                throw new Exception("The word '" + word + "' is not in the dictionary!");
            }
        }
        return input;
    }

    public Progress getProgress() {
        return progress;
    }

    public void initCandidatesQueue(){
        this.candidatesQueue = new LinkedBlockingQueue<>();
    }
    public  void initProgress(){
        this.progress = new Progress();
    }
    public void startDecrypt(EnigmaMachine input_machine, int taskSize, int numOfSelectedAgents, String msgToDecrypt, int level) throws Exception {

        input_machine.Reset();
        EnigmaMachine.serializeMachine("Enigma-For-DM_DO_NOT_TOUCH", input_machine);
        this.machine = cloneMachine();
        this.level = level;
        this.numberOfAgents = numOfSelectedAgents;
        this.missionSize = taskSize;
        msgToDecrypt = msgToDecrypt.toUpperCase();
        if(isValidMessage(msgToDecrypt)){
            this.msgToDecrypt = msgToDecrypt;
        }

        else{
            throw new Exception("Message not in dictionary...");
        }


        this.setMissions();
        this.progress.setTotal(this.numberOfMissions);
        this.progress.setStopped(false);
        // setting up the collector of the candidates
//        collector = new Thread(new CandidatesCollectorTask(candidatesQueue, totalPossibleConfigurations, uiAdapter, this,
//                isBruteForceActionCancelledProperty(), isBruteForceActionPaused));
//        collector.setName("THE_COLLECTOR");

        // starting the thread pool
        threadExecutor = new ThreadPoolExecutor(numberOfAgents, numberOfAgents, 0L, TimeUnit.MILLISECONDS, threadPoolBlockingQueue);
//         setting up thr thread factory for the thread pool
        threadExecutor.setThreadFactory(new ThreadFactory() {

            private int nameCounter = 0;

            @Override
            public Thread newThread(Runnable r) {
                nameCounter++;
                return new Thread(r, String.valueOf(nameCounter));
            }
        });



//        threadExecutor.submit(new MissionThread(i+1, this.missionSize, this.dictionary, this.msgToDecrypt, cloneMachine(),this.level, this.candidatesQueue, this.configurationsQueue));

        // setting a thread that produces tasks
        taskProducer = new Thread(new ConfigurationGenerator(this.msgToDecrypt, this.dictionary, this.missionSize, this.level, this.numberOfMissions, this.totalConfigs, this.threadPoolBlockingQueue, this.candidatesQueue, this.progress));

        // trigger the threads
        threadExecutor.prestartAllCoreThreads();
        System.out.println("[deryptore start]");
        taskProducer.start(); // thread is in the air starting the missions spread.
        //collector.start();
        // main thread ends here
    }

    public void stopDecrypt() {
        progress.setPaused(false);

        // stopping the thread pool
        progress.setStopped(true);
        threadExecutor.shutdownNow();

        //  stopping the collector Task / Thread
//        collector.interrupt();

        // stopping the producer Thread;
        taskProducer.interrupt();
    }

    public void pauseDecrypt() {
        System.out.println("about to set pause to " + true);
        System.out.println("now paused is " + progress.isPaused());
        synchronized (progress) {
            System.out.println("inside sync on dm");
            progress.setPaused(true);
        }
    }

    /**
     * resume the execution after being paused
     */
    public void resumeDecrypt() {
        synchronized (progress) {
            progress.setPaused(false);
            progress.notifyAll();
            System.out.println("dm had notified all with key :" );
        }
    }

    public EnigmaMachine getMachine() {
        return machine;
    }
}
