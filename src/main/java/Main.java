import graph.util.Graph;
import graph.scc.SCCFinder;
import graph.topo.TopologicalSorter;
import graph.dagsp.DagShortestPaths;
import graph.metrics.SimpleMetrics;

import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        File file = new File("data/dataset_small_1.json");
        Graph g = Graph.fromJsonFile(file);
        System.out.println("Loaded graph: " + g.n + " vertices");

        SimpleMetrics sccMetrics = new SimpleMetrics();
        SCCFinder scc = new SCCFinder(g, sccMetrics);
        sccMetrics.startTimer();
        var components = scc.kosaraju();
        sccMetrics.stopTimer();

        System.out.println("\nStrongly Connected Components:");
        int id = 1;
        for (var comp : components) {
            System.out.println("SCC " + id++ + ": " + comp);
        }
        System.out.println("Total SCCs = " + components.size());
        System.out.println("DFS Visits: " + sccMetrics.getVisits());
        System.out.println("Time (ns): " + sccMetrics.elapsedNanos());

        Map<Integer,Integer> nodeToComp = new HashMap<>();
        for (int i = 0; i < components.size(); i++) {
            for (int v : components.get(i)) nodeToComp.put(v, i);
        }

        int compCount = components.size();
        @SuppressWarnings("unchecked")
        List<Integer>[] condAdj = new List[compCount];
        for (int i = 0; i < compCount; i++) condAdj[i] = new ArrayList<>();

        for (int u = 0; u < g.n; u++) {
            for (Graph.Edge e : g.adj[u]) {
                int cu = nodeToComp.get(u);
                int cv = nodeToComp.get(e.v);
                if (cu != cv && !condAdj[cu].contains(cv)) {
                    condAdj[cu].add(cv);
                }
            }
        }

        SimpleMetrics topoMetrics = new SimpleMetrics();
        TopologicalSorter topo = new TopologicalSorter(compCount, condAdj, topoMetrics);
        topoMetrics.startTimer();
        var order = topo.kahnOrder();
        topoMetrics.stopTimer();

        System.out.println("\nTopological order of SCCs: " + order);
        System.out.println("Pushes: " + topoMetrics.getPushes() + ", Pops: " + topoMetrics.getPops());
        System.out.println("Time (ns): " + topoMetrics.elapsedNanos());

        @SuppressWarnings("unchecked")
        List<long[]>[] dagAdj = new List[compCount];
        for (int i = 0; i < compCount; i++) dagAdj[i] = new ArrayList<>();

        for (int u = 0; u < g.n; u++) {
            for (Graph.Edge e : g.adj[u]) {
                int cu = nodeToComp.get(u);
                int cv = nodeToComp.get(e.v);
                if (cu != cv) dagAdj[cu].add(new long[]{cv, e.w});
            }
        }

        DagShortestPaths dagsp = new DagShortestPaths(compCount, dagAdj, new SimpleMetrics());
        int src = nodeToComp.get(g.adj[0].get(0).u);
        int[] prev = new int[compCount];
        List<Integer> topoOrder = order;

        long[] dist = dagsp.shortestFrom(src, topoOrder, prev);
        System.out.println("\nShortest distances from component " + src + ": " + Arrays.toString(dist));

        long[] longest = dagsp.longestFrom(src, topoOrder, prev);
        System.out.println("Longest distances from component " + src + ": " + Arrays.toString(longest));

        int end = -1;
        long maxLen = Long.MIN_VALUE;
        for (int i = 0; i < longest.length; i++) {
            if (longest[i] > maxLen) { maxLen = longest[i]; end = i; }
        }
        System.out.println("\nCritical path length = " + maxLen);
    }
}
