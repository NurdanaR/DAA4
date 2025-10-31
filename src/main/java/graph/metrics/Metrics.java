package graph.metrics;

public interface Metrics {
    void startTimer();
    void stopTimer();
    long elapsedNanos();
    void countVisit();
    void countEdgeVisit();
    void countPush();
    void countPop();
    void countRelaxation();
    // getters for counters
    long getVisits();
    long getEdgeVisits();
    long getPushes();
    long getPops();
    long getRelaxations();
}