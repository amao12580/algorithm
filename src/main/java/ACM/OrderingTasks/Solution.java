package ACM.OrderingTasks;

import basic.Util;

import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/26
 * Time:10:15
 * <p>
 * 假设有n个变量，还有m个二元组(u, v)，分别表示变量u小于v。那么，所有变量从小到
 * 大排列起来应该是什么样子的呢？例如，有4个变量a, b, c, d，若已知a < b，c < b，d < c，则
 * 这4个变量的排序可能是a < d < c < b。尽管还有其他可能（如d < a < c < b），你只需找出其
 * 中一个即可。
 * <p>
 * Ordering Tasks, UVa 10305
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=onlinejudge&page=show_problem&problem=1246
 * <p>
 * Sample Input
 * 5 4
 * 1 2
 * 2 3
 * 1 3
 * 1 5
 * 0 0
 * Sample Output
 * 1 4 2 5 3
 * <p>
 * 输出的可能为多解，保持题意顺序即可，如题目要求a<b,则输出必需保持a在b之前即可，中间有无隔断无关紧要
 * <p>
 * DAG:Directed Acyclic Graph 有向无环图，通过环上的任意节点，沿单方向运动，不会回到原点。
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("----------------------------------------------------------------------------------------------");
        solution.case2();
        System.out.println("----------------------------------------------------------------------------------------------");
        solution.case3();
        System.out.println("----------------------------------------------------------------------------------------------");
        solution.case4();
    }

    private void case1() {
        int[][] depends = {
                {1, 2},
                {2, 3},
                {1, 3},
                {1, 5}};
        order(5, depends);
    }

    private void case2() {
        int[][] depends = {{1, 2}, {2, 3}, {1, 3}, {1, 5}, {3, 5}, {4, 5}};
        order(5, depends);
    }

    private void case3() {
        int[][] depends = {{1, 3}, {2, 3}, {15, 3},
                {3, 5}, {3, 4}, {3, 6},
                {5, 13}, {5, 7}, {5, 8},
                {8, 12}, {12, 14}, {4, 9},
                {9, 11}, {6, 10}, {6, 16},
                {15, 16}, {16, 17}, {15, 18}};
        Util.shuffleArray(depends);
        order(18, depends);
    }

    private void case4() {
        int[][] depends = {
                {1, 2},
                {1, 4},
                {2, 3},
                {3, 1},
                {1, 5}};
        order(5, depends);
    }

    private void order(int taskNumbers, int[][] dependPair) {
        Set<Integer> all = new ConcurrentSkipListSet<>();
        for (int i = 0; i < taskNumbers; i++) {
            all.add(i + 1);
        }
        int size = taskNumbers + 1;
        boolean[][] depend = new boolean[size][size];
        for (int[] pair : dependPair) {
            depend[pair[0]][pair[1]] = true;
        }
        LinkedList<Integer> result = new LinkedList<>();
        order(all, depend, size, result);
        if (result.size() < taskNumbers) {
            System.out.println("circle depend.");
            return;
        }
        System.out.println(result.toString());
    }

    private void order(Set<Integer> all, boolean[][] depends, int len, LinkedList<Integer> result) {
        Set<Integer> portal = findPortal(all, len, depends);
        if (portal.isEmpty()) {
            return;
        }
        for (Integer u : portal) {
            result.add(u);
            removeEdge(depends, len, u);
        }
        order(all, depends, len, result);
    }

    private Set<Integer> findPortal(Set<Integer> all, int len, boolean[][] depends) {
        Set<Integer> result = new ConcurrentSkipListSet<>();
        result.addAll(all);
        for (int i = 1; i < len; i++) {
            for (int j = 1; j < len; j++) {
                if (depends[i][j]) {
                    result.remove(j);
                }
            }
        }
        all.removeAll(result);
        return result;
    }

    private void removeEdge(boolean[][] depends, int len, int u) {
        for (int i = 1; i < len; i++) {
            depends[u][i] = false;
        }
    }
}
