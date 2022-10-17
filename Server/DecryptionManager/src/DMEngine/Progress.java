package DMEngine;

public class Progress {
    private long complited = 0;
    private long total = 0;
    private boolean finished = false;
    private boolean paused = false;
    private boolean stopped = false;

    public Progress() {
    }

    public void finishedMission(){
        complited++;
//        System.out.println(complited);
        if(complited ==total){
            finished = true;
        }
    }

    public  void stop(){
        stopped = true;
    }
    public void pause(){
        paused = true;
    }

    public long getComplited() {
        return complited;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }


    public boolean isFinished() {
        return finished;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
}
