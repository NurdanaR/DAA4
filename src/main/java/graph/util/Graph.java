package graph.util;

import java.util.*;
import java.io.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;


public class Graph {
    public final int n;
    public final boolean directed;
    public final List<Edge>[] adj;

    @SuppressWarnings("unchecked")
    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        this.adj = new List[n];
        for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();
    }

    public static class Edge { public final int u, v; public final long w;
        public Edge(int u, int v, long w){ this.u=u; this.v=v; this.w=w; }
    }

    public void addEdge(int u, int v, long w){
        adj[u].add(new Edge(u,v,w));
        if(!directed) adj[v].add(new Edge(v,u,w));
    }

    public static Graph fromJsonFile(File f) throws IOException {
        ObjectMapper m = new ObjectMapper();
        JsonNode root = m.readTree(f);
        boolean directed = root.path("directed").asBoolean(true);
        int n = root.path("n").asInt();
        Graph g = new Graph(n, directed);
        for (JsonNode e : root.path("edges")) {
            int u = e.path("u").asInt();
            int v = e.path("v").asInt();
            long w = e.path("w").asLong(1);
            g.addEdge(u, v, w);
        }
        return g;
    }
}
