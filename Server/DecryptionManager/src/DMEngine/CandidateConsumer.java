package DMEngine;

import java.util.concurrent.BlockingQueue;

public class CandidateConsumer {
    private final BlockingQueue<Candidate> candidateQueue;
    private final BoolianRefferance paused;
    private final BoolianRefferance stopped;




    public CandidateConsumer(BlockingQueue<Candidate> candidateQueue, BoolianRefferance paused, BoolianRefferance stopped) {
        this.candidateQueue = candidateQueue;
        this.paused = paused;
        this.stopped = stopped;
    }
}
