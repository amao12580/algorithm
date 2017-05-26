package ACM.OrderingTasks;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

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
 *
 * 输出的可能为多解，保持题意顺序即可，如题目要求a<b,则输出必需保持a在b之前即可，中间有无隔断无关紧要
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("----------------------------------------------------------------------------------------------");
        solution.case2();
        System.out.println("----------------------------------------------------------------------------------------------");
        solution.case3();
    }

    private void case1() {
        int[][] depends = {{1, 2}, {2, 3}, {1, 3}, {1, 5}};
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
        order(18, depends);
    }

    private void order(int taskNumbers, int[][] depends) {
        LinkedList<Integer> list = new LinkedList<>();
        int u, v, up, vp;
        for (int[] depend : depends) {
            u = depend[0];
            v = depend[1];
            up = list.indexOf(u);
            vp = list.indexOf(v);
            if (up < 0 && vp < 0) {
                list.add(u);
                list.add(v);
            } else if (up >= 0 && vp < 0) {
                if (up == list.size() - 1) {
                    list.addLast(v);
                } else {
                    list.add(up + 1, v);
                }
            } else if (up < 0 && vp >= 0) {
                if (vp == 0) {
                    list.addFirst(u);
                } else {
                    list.add(vp, u);
                }
            } else if (up >= 0 && vp >= 0) {
                if (up > vp) {
                    list.remove(vp);
                    up = list.indexOf(u);
                    if (up == list.size() - 1) {
                        list.addLast(v);
                    } else {
                        list.add(up + 1, v);
                    }
                }
            }
        }
        if (list.isEmpty()) {
            System.out.println("circle depend.");
            return;
        }
        Set<Integer> all = new HashSet<>();
        for (int i = 1; i <= taskNumbers; i++) {
            all.add(i);
        }
        all.removeAll(list);
        list.addAll(all);
        System.out.println(list.toString());
    }
}
