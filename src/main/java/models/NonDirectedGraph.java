package models;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class NonDirectedGraph extends Graph {

    @Override
    public void addVertexFromString(String string) {
        addVertexFromString(string, true);
    }

    /**
     * Gets the minimum spanning tree from a graph
     */
    public int kruskalAlgorithm() {
        LinkedList<DoubleVertexEdge> edges = this.getEdges()
                .stream()
                .sorted(Comparator.comparingDouble(e -> e.weight))
                .collect(Collectors.toCollection(LinkedList::new));

        HashMap<String, Subset> subsets = new HashMap<>();
        this.adjacencyMap.forEach((key, value) -> subsets.put(key, new Subset()));

        this.adjacencyMap.forEach((key, value) -> {
            subsets.get(key).parent = key;
            subsets.get(key).rank = 0;
        });

        final NonDirectedGraph graph = new NonDirectedGraph();
        int numIncludedEdges = 0;
        int count = 0;

        while (numIncludedEdges < this.adjacencyMap.size() - 1) {
            DoubleVertexEdge edge = edges.get(count);

            // Find both vertexes root parents
            String x = find(subsets, edge.vertex1);
            String y = find(subsets, edge.vertex2);

            if (!x.equals(y)) {
                graph.addVertex(edge.vertex1);
                graph.addVertex(edge.vertex2);
                graph.addEdge(edge.vertex1, new Edge(edge.vertex2, edge.weight, new ArrayList<>(), LocalTime.now()));
                numIncludedEdges++;
                union(subsets, x, y);
            }
            count++;
        }

        System.out.println(graph);
        return graph.numEdges;
    }

    /**
     * A class to represent a subset for union-find
     */
    static class Subset {
        String parent;
        int rank;
    }

    /**
     * A utility function to find the root parent of a defined vertex
     *
     * @param subsets Subsets array
     * @param v       vertex parent to be found
     * @return parent of subset
     */
    String find(HashMap<String, Subset> subsets, String v) {
        // find root and make root as parent of i (path compression)
        if (!subsets.get(v).parent.equals(v))
            subsets.get(v).parent = find(subsets, subsets.get(v).parent);

        return subsets.get(v).parent;
    }

    /**
     * Unite two subsets
     * @param subsets Subsets list
     * @param x First Vertex
     * @param y Second Vertex
     */
    void union(HashMap<String, Subset> subsets, String x, String y) {
        String xRoot = find(subsets, x);
        String yRoot = find(subsets, y);

        // Attach smaller rank tree under root of high rank tree
        // (Union by Rank)
        if (subsets.get(xRoot).rank < subsets.get(yRoot).rank)
            subsets.get(xRoot).parent = yRoot;
        else if (subsets.get(xRoot).rank > subsets.get(yRoot).rank)
            subsets.get(yRoot).parent = xRoot;

            // If ranks are same, then make one as root and increment
            // its rank by one
        else {
            subsets.get(yRoot).parent = xRoot;
            subsets.get(xRoot).rank++;
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        this.adjacencyMap.forEach((key, value) -> {
            string.append(key);
            string.append("->[");
            value.forEach(edge -> string.append(" ").append(edge.destination).append(" "));
            string.append("]\n");
        });
        return string.toString();
    }
}
