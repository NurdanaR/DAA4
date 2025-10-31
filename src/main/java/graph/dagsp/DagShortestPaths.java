package graph.dagsp;

import java.util.*;
import graph.metrics.*;
import graph.util.Graph;

public class DagShortestPaths {
    private final int n;
    private final List<long[]>[] adj; // each element: [v, w]
    private final Metrics metrics;

    @SuppressWarnings("unchecked")
    public DagShortestPaths(int n, List<long[]>[] adj, Metrics metrics) {
        this.n = n; this.adj = adj; this.metrics = metrics;
    }

    public static final long INF = Long.MAX_VALUE / 4;

    public long[] shortestFrom(int src, List<Integer> topoOrder, int[] prevNode) {
        long[] dist = new long[n];
        Arrays.fill(dist, INF);
        Arrays.fill(prevNode, -1);
        dist[src] = 0;
        for (int u : topoOrder) {
            if (dist[u] == INF) continue;
            for (long[] vw : adj[u]) {
                int v = (int) vw[0]; long w = vw[1];
                metrics.countRelaxation();
                if (dist[v] > dist[u] + w) {
                    dist[v] = dist[u] + w;
                    prevNode[v] = u;
                }
            }
        }
        return dist;
    }

    public long[] longestFrom(int src, List<Integer> topoOrder, int[] prevNode) {
        long[] longest = new long[n];
        Arrays.fill(longest, Long.MIN_VALUE / 4);
        Arrays.fill(prevNode, -1);
        longest[src] = 0;
        for (int u : topoOrder) {
            if (longest[u] == Long.MIN_VALUE / 4) continue;
            for (long[] vw : adj[u]) {
                int v = (int) vw[0]; long w = vw[1];
                metrics.countRelaxation();
                if (longest[v] < longest[u] + w) {
                    longest[v] = longest[u] + w;
                    prevNode[v] = u;
                }
            }
        }
        return longest;
    }
}
