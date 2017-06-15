package ACM.IdealPath;

import basic.Util;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/6/14
 * Time:10:13
 * <p>
 * 给一个n个点m条边（2≤n≤100000，1≤m≤200000）的无向图，每条边上都涂有一种颜
 * 色。求从结点1到结点n的一条路径，使得经过的边数尽量少，在此前提下，经过边的颜色序
 * 列的字典序最小。一对结点间可能有多条边，一条边可能连接两个相同结点。输入保证结点
 * 1可以达到结点n。颜色为1～10 9 的整数。
 * <p>
 * Ideal Path, NEERC 2010, UVa1599
 * <p>
 * <p>
 * Sample Input
 * 4 6
 * 1 2 1
 * 1 3 2
 * 3 4 3
 * 2 3 1
 * 2 4 4
 * 3 1 1
 * Sample Output
 * 2
 * 1 3
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("---------------------------------");
        solution.case2();
        System.out.println("---------------------------------");
        solution.case3();
        System.out.println("---------------------------------");
        solution.case4();
    }

    private void case1() {
        int[][] desc = {{1, 2, 1}, {1, 3, 2}, {3, 4, 3}, {2, 3, 1}, {2, 4, 4}, {3, 1, 1}};
        path(desc, 4);
    }

    private void case2() {
        int[][] desc = {{1, 1, 1}, {1, 2, 2}, {2, 3, 3}, {1, 3, 4}};
        path(desc, 3);
    }


    private void case3() {
        int[][] desc = {{2, 1, 1}, {1, 2, 2}};
        path(desc, 2);
    }

    private void case4() {
        int n = Util.getRandomInteger(2, 100000);
        int m = Util.getRandomInteger(1, 200000);
        int[][] desc = new int[m][];
        for (int i = 0; i < m; i++) {
            desc[i] = new int[]{Util.getRandomInteger(1, n), Util.getRandomInteger(1, n), Util.getRandomInteger(1, 1000000000)};
        }
        path(desc, n);
    }

    private void path(int[][] desc, int n) {
        Graph graph = new Graph();
        for (int[] edge : desc) {
            graph.addEdge(edge[0], edge[1], edge[2]);
        }
        Integer[] distance = new Integer[n + 1];
        inverseBFS(graph, distance, n);
        if (distance[1] == null) {
            System.out.println("1 can not reach " + n);
            return;
        }
        List<Integer> path = DFS(graph, distance, 1, n);
        if(path.isEmpty()){
            System.out.println("1 can not reach " + n);
            return;
        }
        System.out.println(path.size());
        System.out.println(path.toString());
    }

    /**
     * 从起点到终点
     */
    private List<Integer> DFS(Graph graph, Integer[] distance, int start, int end) {
        List<List<Integer>> paths = new LinkedList<>();
        DFS(graph, distance, start, paths, new LinkedList<>(), new HashSet<>());
        return getIdealPath(paths, end);
    }

    private List<Integer> getIdealPath(List<List<Integer>> paths, int end) {
        Integer minLength = null;
        List<List<Integer>> filterByLength = new LinkedList<>();
        int size;
        for (List<Integer> path : paths) {
            size = path.size();
            if (path.get(size - 1) != end) {
                continue;
            }
            if (minLength == null) {
                minLength = size;
                filterByLength.add(path);
            } else {
                if (size < minLength) {
                    minLength = size;
                    filterByLength.clear();
                }
                if (size == minLength) {
                    filterByLength.add(path);
                }
            }
        }
        List<Integer> result = new LinkedList<>();
        if (minLength == null) {
            return result;
        }
        List<Integer> temp;
        for (int i = 0; i < minLength; i++) {
            temp = new LinkedList<>();
            for (List<Integer> list : filterByLength) {
                temp.add(list.get(i));
            }
            Collections.sort(temp);
            result.add(temp.get(0));
        }
        return result;
    }

    private void DFS(Graph graph, Integer[] distance, int start, List<List<Integer>> paths, List<Integer> path, Set<Integer> hasVisitStarts) {
        Map<Integer, Set<Edge>> p2e = graph.getP2e();
        path.add(start);
        List<Edge> bestEdges = getBestEdge(p2e, distance, start, distance[start] - 1, hasVisitStarts);
        hasVisitStarts.add(start);
        List<Integer> branch;
        for (Edge bestEdge : bestEdges) {
            branch = new LinkedList<>();
            branch.addAll(path);
            paths.add(branch);
            DFS(graph, distance, bestEdge.getEnd(), paths, branch, hasVisitStarts);
        }
    }

    private List<Edge> getBestEdge(Map<Integer, Set<Edge>> p2e, Integer[] distances, int start, int distance, Set<Integer> hasVisitStarts) {
        List<Edge> result = new LinkedList<>();
        Set<Edge> edges = p2e.get(start);
        result.addAll(edges.stream().filter(edge -> edge.getStart() == start && distances[edge.getEnd()] <= distance && !hasVisitStarts.contains(edge.getStart())).collect(Collectors.toList()));
        if (result.isEmpty() || result.size() == 1) {
            return result;
        }
        Collections.sort(result);
        int minColor = result.get(0).getColor();
        return result.stream().filter(edge -> edge.getColor() == minColor).collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * 从终点到起点
     * <p>
     * 找到每个结点到终点的最短距离
     */
    private void inverseBFS(Graph graph, Integer[] distance, int destination) {
        Map<Integer, Set<Edge>> p2e = graph.getP2e();
        Set<Edge> edges = p2e.get(destination);//连接到终点的所有边
        Set<Edge> hasVisited = new HashSet<>();
        Set<Integer> destinations = new HashSet<>();
        distance[destination] = 0;
        for (Edge edge : edges) {
            distance[edge.getStart()] = 1;
            destinations.add(edge.getStart());
            hasVisited.add(edge);
        }
        inverseBFS(p2e, distance, destinations, hasVisited);
    }

    private void inverseBFS(Map<Integer, Set<Edge>> p2e, Integer[] distance, Set<Integer> destinations, Set<Edge> hasVisited) {
        if (destinations.isEmpty()) {
            return;
        }
        Set<Integer> nextDestinations = new HashSet<>();
        int start, nextDistance;
        Integer origin;
        Set<Edge> edges;
        for (Integer destination : destinations) {
            edges = p2e.get(destination);//连接到终点的所有边
            nextDistance = distance[destination] + 1;
            for (Edge edge : edges) {
                if (hasVisited.contains(edge)) {
                    continue;
                }
                start = edge.getStart();
                start = start == destination ? edge.getEnd() : start;
                origin = distance[start];
                distance[start] = origin == null ? nextDistance : origin > nextDistance ? nextDistance : origin;
                nextDestinations.add(start);
                hasVisited.add(edge);
            }
        }
        nextDestinations.removeAll(destinations);
        destinations.clear();
        inverseBFS(p2e, distance, nextDestinations, hasVisited);
    }

    private class Graph {
        private Map<Integer, Set<Edge>> p2e = new HashMap<>();

        private void addEdge(int start, int end, int color) {
            Edge edge = new Edge(start, end, color);
            addP2E(start, edge);
            addP2E(end, edge);
        }

        private void addP2E(int p, Edge edge) {
            Set<Edge> set = p2e.get(p);
            if (set == null) {
                set = new HashSet<>();
                set.add(edge);
                p2e.put(p, set);
            } else {
                set.add(edge);
            }
        }

        public Map<Integer, Set<Edge>> getP2e() {
            return p2e;
        }
    }

    private class Edge implements Comparable<Edge> {//每条边的长度为1
        private String uniqueCode;
        private int start;
        private int end;
        private int color;

        public Edge(int start, int end, int color) {
            if (start > end) {//低位在前，高位在后
                int temp = start;
                start = end;
                end = temp;
            }
            this.start = start;
            this.end = end;
            this.color = color;
            this.uniqueCode = String.valueOf(start).concat(".").concat(String.valueOf(end)).concat(".").concat(String.valueOf(color));
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o instanceof Edge) {
                Edge other = (Edge) o;
                return this.hashCode() == other.hashCode();
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return uniqueCode.hashCode();
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public int getColor() {
            return color;
        }

        @Override
        public int compareTo(Edge other) {
            return this.getColor() - other.getColor();
        }
    }
}
