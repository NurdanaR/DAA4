package graph.scc;

import graph.util.Graph;
import graph.metrics.*;
import java.util.*;

public class SCCFinder {
    private final Graph g;
    private final Metrics metrics;

    public SCCFinder(Graph g, Metrics metrics) {
        this.g = g; this.metrics = metrics;
    }

    public List<List<Integer>> kosaraju() {
        int n = g.n;
        boolean[] vis = new boolean[n];
        Deque<Integer> order = new ArrayDeque<>();

        for (int i = 0; i < n; i++) if (!vis[i]) dfs1(i, vis, order);

        List<Integer>[] radj = new List[n];
        for (int i = 0; i < n; i++) radj[i] = new ArrayList<>();
        for (int u = 0; u < n; u++) {
            for (Graph.Edge e : g.adj[u]) {
                radj[e.v].add(u);
            }
        }

        List<List<Integer>> components = new ArrayList<>();
        boolean[] assigned = new boolean[n];
        while (!order.isEmpty()) {
            int v = order.pollLast();
            if (!assigned[v]) {
                List<Integer> comp = new ArrayList<>();
                dfs2(v, radj, assigned, comp);
                components.add(comp);
            }
        }
        return components;
    }

    private void dfs1(int u, boolean[] vis, Deque<Integer> order) {
        vis[u] = true;
        metrics.countVisit();
        for (Graph.Edge e : g.adj[u]) {
            metrics.countEdgeVisit();
            if (!vis[e.v]) dfs1(e.v, vis, order);
        }
        order.addLast(u);
    }

    private void dfs2(int u, List<Integer>[] radj, boolean[] assigned, List<Integer> comp) {
        assigned[u] = true;
        comp.add(u);
        for (int v : radj[u]) {
            if (!assigned[v]) dfs2(v, radj, assigned, comp);
        }
    }
}
