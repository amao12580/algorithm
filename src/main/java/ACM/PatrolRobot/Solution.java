package ACM.PatrolRobot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/7/11
 * Time:12:47
 * <p>
 * 机器人要从一个m*n（1≤m，n≤20）网格的左上角(1,1)走到右下角(m,n)。网格中的一些
 * 格子是空地（用0表示），其他格子是障碍（用1表示）。机器人每次可以往4个方向走一
 * 格，但不能连续地穿越k（0≤k≤20）个障碍，求最短路长度。起点和终点保证是空地。例
 * 如，对于图6-22（a）中的数据，图6-22（b）中显示的是最优解，路径长度为10。
 * <p>
 * Patrol Robot, ACM/ICPC Hanoi 2006, UVa 1600
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=4475
 * <p>
 * Sample Input
 * 3
 * 2 5
 * 0
 * 0 1 0 0 0
 * 0 0 0 1 0
 * 4 6
 * 1
 * 0 1 1 0 0 0
 * 0 0 1 0 1 1
 * 0 1 1 1 1 0
 * 0 1 1 1 0 0
 * 2 2
 * 0
 * 0 1
 * 1 0
 * Sample Output
 * 7
 * 10
 * -1
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        solution.case2();
        solution.case3();
    }

    private void case1() {
        int[][] desc = {{0, 1, 0, 0, 0}, {0, 0, 0, 1, 0}};
        maze(2, 5, 0, desc);
    }

    private void case2() {
        int[][] desc = {{0, 1, 1, 0, 0, 0}, {0, 0, 1, 0, 1, 1}, {0, 1, 1, 1, 1, 0}, {0, 1, 1, 1, 0, 0}};
        maze(4, 6, 1, desc);
    }

    private void case3() {
        int[][] desc = {{0, 1}, {1, 0}};
        maze(2, 2, 0, desc);
    }

    private void maze(int rows, int columns, int k, int[][] desc) {
        int min = -1;
        List<Integer> pathLengths = new ArrayList<>();
        maze(rows, columns, k, desc, "11" + k, rows + "" + columns + "" + k, pathLengths);
        if (pathLengths.isEmpty()) {
            System.out.println(min);
            return;
        }
        if (pathLengths.size() > 1) {
            Collections.sort(pathLengths);
        }
        System.out.println(pathLengths.get(0));
    }

    private void maze(int rows, int columns, int originK, int[][] desc, String start, String end, List<Integer> pathLengths) {
        List<LinkedList<String>> paths = new ArrayList<>();
        LinkedList<String> cPath = new LinkedList<>();
        paths.add(cPath);
        cPath.add(start);
        maze(rows, columns, originK, desc, end, cPath, paths);
        int pe;
        for (LinkedList<String> path : paths) {
            pe = path.indexOf(end);
            if (pe >= 0) {
                pathLengths.add(pe);
            }
        }
    }

    private void maze(int rows, int columns, int originK, int[][] desc, String end, LinkedList<String> currentPath, List<LinkedList<String>> paths) {
        String start = currentPath.peekLast();
        if (start.equals(end)) {
            return;
        }
        List<String> nexts = next(rows, columns, desc, start, originK);
        int way = 0;
        LinkedList<String> originCurrentPath = new LinkedList<>(currentPath);
        for (String next : nexts) {
            if (existPath(currentPath, start, next, 0, currentPath.size() - 2)) {//不允许形成环路
                continue;
            }
            currentPath.add(next);
            if (next.equals(end)) {
                continue;
            }
            if (way != 0) {
                currentPath = new LinkedList<>(originCurrentPath);
                currentPath.add(next);
                paths.add(currentPath);
            }
            way++;
            maze(rows, columns, originK, desc, end, currentPath, paths);
        }
    }

    private boolean existPath(LinkedList<String> path, String start, String end, int beginIndex, int endIndex) {
        List<String> tmp = new ArrayList<>(path);
        for (int i = beginIndex; i <= endIndex; i++) {
            String c = tmp.get(i);
            if (c.equals(start) && (i + 1) <= endIndex && tmp.get(i + 1).equals(end)) {
                return true;
            }
        }
        tmp.clear();
        return false;
    }

    private List<String> next(int rows, int columns, int[][] desc, String point, int originK) {
        List<String> result = new ArrayList<>();
        int row = Integer.valueOf(String.valueOf(point.charAt(0)));
        int column = Integer.valueOf(String.valueOf(point.charAt(1)));
        int currentK = Integer.valueOf(String.valueOf(point.charAt(2)));
        int bs = result.size();
        ifInRange(row, column + 1, rows, columns, desc, originK, currentK, result);
        ifInRange(row + 1, column, rows, columns, desc, originK, currentK, result);
        int as = result.size();
        if (as == bs) {//优先向右和向下走，只有这两个方向全走不通，才考虑向左和向上
            ifInRange(row - 1, column, rows, columns, desc, originK, currentK, result);
            ifInRange(row, column - 1, rows, columns, desc, originK, currentK, result);
        }
        return result;
    }

    private void ifInRange(int row, int column, int rows, int columns, int[][] desc, int originK, int currentK, List<String> result) {
        if (row >= 1 && row <= rows && column >= 1 && column <= columns) {
            int v = desc[row - 1][column - 1];
            if (v == 1) {
                if (currentK > 0) {
                    result.add(row + "" + column + "" + (currentK - 1));
                }
            } else {
                result.add(row + "" + column + "" + originK);
            }
        }
    }
}
