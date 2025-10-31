package graph.dagsp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import graph.metrics.SimpleMetrics;
import java.util.*;

public class DagShortestPathsTest {

    @Test
    public void testShortestPath() {
        // DAG: 0 → 1 (2), 0 → 2 (4), 1 → 3 (3), 2 → 3 (1)
        int n = 4;
        @SuppressWarnings("unchecked")
        List<long[]>[] adj = new List[n];
        for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();
        adj[0].add(new long[]{1, 2});
        adj[0].add(new long[]{2, 4});
        adj[1].add(new long[]{3, 3});
        adj[2].add(new long[]{3, 1});

        SimpleMetrics m = new SimpleMetrics();
        DagShortestPaths dsp = new DagShortestPaths(n, adj, m);

        List<Integer> topo = Arrays.asList(0, 1, 2, 3);
        int[] prev = new int[n];
        long[] dist = dsp.shortestFrom(0, topo, prev);

        assertEquals(5, dist[3], "Кратчайший путь 0→2→3 имеет длину 5");
        assertEquals(2, prev[3], "Предшественник вершины 3 должен быть 2");
    }

    @Test
    public void testLongestPath() {
        // DAG: 0 → 1 (3), 1 → 2 (4), 0 → 2 (5)
        int n = 3;
        @SuppressWarnings("unchecked")
        List<long[]>[] adj = new List[n];
        for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();
        adj[0].add(new long[]{1, 3});
        adj[1].add(new long[]{2, 4});
        adj[0].add(new long[]{2, 5});

        SimpleMetrics m = new SimpleMetrics();
        DagShortestPaths dsp = new DagShortestPaths(n, adj, m);

        List<Integer> topo = Arrays.asList(0, 1, 2);
        int[] prev = new int[n];
        long[] longest = dsp.longestFrom(0, topo, prev);

        assertEquals(7, longest[2], "Длина самого длинного пути 0→1→2 равна 7");
        assertEquals(1, prev[2], "Предшественник вершины 2 должен быть 1");
    }
}
