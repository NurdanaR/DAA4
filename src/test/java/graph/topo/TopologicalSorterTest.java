package graph.topo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import graph.metrics.SimpleMetrics;
import java.util.*;

public class TopologicalSorterTest {

    @Test
    public void testSimpleOrder() {
        // Простой DAG: 0 → 1 → 2 → 3
        int n = 4;
        @SuppressWarnings("unchecked")
        List<Integer>[] adj = new List[n];
        for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();
        adj[0].add(1);
        adj[1].add(2);
        adj[2].add(3);

        SimpleMetrics m = new SimpleMetrics();
        TopologicalSorter sorter = new TopologicalSorter(n, adj, m);

        var order = sorter.kahnOrder();
        assertEquals(Arrays.asList(0, 1, 2, 3), order, "Топологический порядок должен быть 0,1,2,3");
    }

    @Test
    public void testCycleDetection() {
        // Цикл: 0 → 1 → 2 → 0
        int n = 3;
        @SuppressWarnings("unchecked")
        List<Integer>[] adj = new List[n];
        for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();
        adj[0].add(1);
        adj[1].add(2);
        adj[2].add(0);

        SimpleMetrics m = new SimpleMetrics();
        TopologicalSorter sorter = new TopologicalSorter(n, adj, m);

        assertThrows(IllegalStateException.class, sorter::kahnOrder,
                "Должно выбросить исключение, так как граф содержит цикл");
    }
}
