package ACM.InspectorsDilemma;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/7/19
 * Time:19:36
 * <p>
 * 某国家有V（V≤1000）个城市，每两个城市之间都有一条双向道路直接相连，长度
 * 为T。你的任务是找一条最短的道路（起点和终点任意），使得该道路经过E条指定的边。
 * 例如，若V=5，E=3，T=1，指定的3条边为1-2、1-3和4-5，则最优道路为3-1-2-4-5，长
 * 度为4*1=4。
 * <p>
 * Inspector's Dilemma, ACM/ICPC Dhaka 2007, UVa12118
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3270
 * <p>
 * Sample Input
 * 5 3 1
 * 1 2
 * 1 3
 * 4 5
 * 4 4 1
 * 1 2
 * 1 4
 * 2 3
 * 3 4
 * 0 0 0
 * Sample Output
 * Case 1: 4
 * Case 2: 4
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        solution.case2();
        solution.case3();
    }

    private void case1() {
        int[][] desc = {{1, 2}, {1, 3}, {4, 5}};
        path(5, 3, 1, desc);
    }

    private void case2() {
        int[][] desc = {{1, 2}, {1, 4}, {2, 3}, {3, 4}};
        path(4, 4, 1, desc);
    }

    private void case3() {
        int[][] desc = {{1, 3}, {2, 4}, {5, 6}};
        path(6, 3, 3, desc);
    }

    private void path(int V, int E, int T, int[][] desc) {
        LinkedHashSet<Integer> starts = new LinkedHashSet<>();
        Set<String> lines = new HashSet<>();
        for (int[] line : desc) {
            starts.add(line[0]);
            starts.add(line[1]);
            addLine(lines, line[0], line[1]);
        }
        LinkedList<String> miniPath = null;
        LinkedList<String> currentMiniPath;
        long maxPath = V * E;
        for (Integer start : starts) {
            currentMiniPath = miniPath(V, lines, start, maxPath);
            if (miniPath == null || currentMiniPath.size() < miniPath.size()) {
                miniPath = currentMiniPath;
            }
        }
        if (miniPath == null || miniPath.isEmpty()) {
            System.out.println("No solution.");
            return;
        }
        System.out.println(miniPath.toString());
        System.out.println(miniPath.size() * T);
    }

    private LinkedList<String> miniPath(int V, Set<String> lines, int start, long maxPath) {
        LinkedList<LinkedList<String>> paths = new LinkedList<>();
        LinkedList<String> path = new LinkedList<>();
        String line = String.valueOf(start);
        path(paths, path, new HashSet<>(), V, lines, null, start, line, null, createAllCity(V), maxPath);
        return paths.poll();
    }


    private void path(LinkedList<LinkedList<String>> paths, LinkedList<String> path, Set<String> visitedLine, int V, Set<String> lines, Integer previous, int start,
                      String line, Integer miniHasReachedPathLen, LinkedList<Integer> notVisitedCity, long maxPath) {
        if (paths.size() > maxPath || path.size() > maxPath || (miniHasReachedPathLen != null && path.size() > miniHasReachedPathLen)) {
            return;
        }
        Set<Integer> ends = new LinkedHashSet<>();
        notVisitedCity.remove(Integer.valueOf(start));
        ends.addAll(notVisitedCity);
        ends.addAll(next(V, previous, start));
        LinkedList<String> currentPath;
        LinkedList<Integer> currentNotVisitedCity;
        Set<String> currentVisitedLine;
        String currentOrderedLine, currentLine;
        for (Integer end : ends) {
            currentPath = new LinkedList<>(path);
            currentVisitedLine = new HashSet<>(visitedLine);
            currentLine = line + end;
            if (currentPath.contains(currentLine)) {//回路
                continue;
            }
            currentOrderedLine = createOrderedLine(start, end);
            currentVisitedLine.add(currentOrderedLine);
            currentPath.add(currentLine);
            currentNotVisitedCity = new LinkedList<>(notVisitedCity);
            if (reached(currentVisitedLine, lines)) {
                paths.add(currentPath);
                if (miniHasReachedPathLen == null || path.size() < miniHasReachedPathLen) {
                    miniHasReachedPathLen = path.size();
                }
                continue;
            }
            path(paths, currentPath, currentVisitedLine, V, lines, start, end, String.valueOf(end), miniHasReachedPathLen, currentNotVisitedCity, maxPath);
        }
    }

    private boolean reached(Set<String> visitedLine, Set<String> line) {
        return visitedLine.containsAll(line);
    }

    private LinkedList<Integer> allCity = new LinkedList<>();

    private LinkedList<Integer> next(int V, Integer... skips) {
        allCity(V);
        LinkedList<Integer> result = new LinkedList<>();
        result.addAll(allCity);
        if (skips != null) {
            for (Integer skip : skips) {
                if (skip != null) {
                    result.remove(skip);
                }
            }
        }
        return result;
    }

    private void allCity(int V) {
        if (allCity.isEmpty()) {
            allCity.addAll(createAllCity(V));
        }
    }

    private LinkedList<Integer> createAllCity(int V) {
        LinkedList<Integer> result = new LinkedList<>();
        for (int i = 1; i <= V; i++) {
            result.add(i);
        }
        return result;
    }


    private void addLine(Set<String> lines, int start, int end) {
        lines.add(createOrderedLine(start, end));
    }

    private String createOrderedLine(int start, int end) {
        if (end > start) {
            return start + "" + end;
        } else {
            return end + "" + start;
        }
    }
}
