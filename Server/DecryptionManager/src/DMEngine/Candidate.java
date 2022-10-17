package DMEngine;

import javafx.util.Pair;

import java.util.List;

public class Candidate {
    private List<Pair<String, String>> solutions;
    private String agentId;
    private String duration;
    public Candidate(List<Pair<String, String>> solutions, String agentId, String duration) {
        this.solutions = solutions;
        this.agentId = agentId;
        this.duration = duration;
    }

    public List<Pair<String, String>> getSolutions() {
        return solutions;
    }

    public String getAgentId() {
        return agentId;
    }

    public String getDuration() {
        return duration;
    }
}
