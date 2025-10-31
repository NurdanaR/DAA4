package graph.scc;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import graph.util.Graph;
import graph.metrics.SimpleMetrics;
import java.io.File;
import java.util.*;

public class SCCFinderTest {

    @Test
    public void testSingleCycle() throws Exception {
        // dataset_small_1.json имеет цикл (0-1-2)
        Graph g = Graph.fromJsonFile(new File("data/dataset_small_1.json"));
        SimpleMetrics m = new SimpleMetrics();
        SCCFinder sccFinder = new SCCFinder(g, m);

        var components = sccFinder.kosaraju();

        // Должно быть как минимум 1 SCC
        assertTrue(components.size() >= 1);

        // Проверяем, что вершины 0,1,2 в одной компоненте
        boolean foundCycle = components.stream().anyMatch(c -> c.containsAll(Arrays.asList(0,1,2)));
        assertTrue(foundCycle, "Цикл (0,1,2) должен быть в одной SCC");
    }

    @Test
    public void testDAG_NoCycles() throws Exception {
        // dataset_small_2.json — чистый DAG
        Graph g = Graph.fromJsonFile(new File("data/dataset_small_2.json"));
        SimpleMetrics m = new SimpleMetrics();
        SCCFinder sccFinder = new SCCFinder(g, m);

        var components = sccFinder.kosaraju();

        // В DAG каждая вершина должна быть отдельной SCC
        assertEquals(g.n, components.size(), "В DAG каждая вершина образует отдельную SCC");
    }
}
