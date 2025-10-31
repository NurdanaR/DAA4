package graph.topo;

import java.util.*;
import graph.metrics.*;
import graph.util.Graph;

public class TopologicalSorter {
    private final int n;
    private final List<Integer>[] adj;
    private final Metrics metrics;

    @SuppressWarnings("unchecked")
    public TopologicalSorter(int n, List<Integer>[] adj, Metrics metrics) {
        this.n = n; this.adj = adj; this.metrics = metrics;
    }

    public List<Integer> kahnOrder() {
        int[] indeg = new int[n];
        for (int u = 0; u < n; u++) for (int v : adj[u]) indeg[v]++;
        Deque<Integer> dq = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) dq.add(i);
        List<Integer> order = new ArrayList<>();
        while (!dq.isEmpty()) {
            int u = dq.poll();
            order.add(u);
            metrics.countPop();
            for (int v : adj[u]) {
                metrics.countEdgeVisit();
                indeg[v]--;
                if (indeg[v] == 0) {
                    dq.add(v);
                    metrics.countPush();
                }
            }
        }
        if (order.size() != n) throw new IllegalStateException("Graph has cycle (not a DAG)");
        return order;
    }
}
