package graph.metrics;

public class SimpleMetrics implements Metrics {
    private long start, end;
    private long visits, edgeVisits, pushes, pops, relaxations;

    public void startTimer(){ start = System.nanoTime(); }
    public void stopTimer(){ end = System.nanoTime(); }
    public long elapsedNanos(){ return end - start; }
    public void countVisit(){ visits++; }
    public void countEdgeVisit(){ edgeVisits++; }
    public void countPush(){ pushes++; }
    public void countPop(){ pops++; }
    public void countRelaxation(){ relaxations++; }
    public long getVisits(){ return visits; }
    public long getEdgeVisits(){ return edgeVisits; }
    public long getPushes(){ return pushes; }
    public long getPops(){ return pops; }
    public long getRelaxations(){ return relaxations; }
}
