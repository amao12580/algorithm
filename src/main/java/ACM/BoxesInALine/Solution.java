package ACM.BoxesInALine;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/18
 * Time:17:28
 * <p>
 * 你有一行盒子，从左到右依次编号为1, 2, 3,…, n。可以执行以下4种指令：
 * 1 X Y表示把盒子X移动到盒子Y左边（如果X已经在Y的左边则忽略此指令）。
 * 2 X Y表示把盒子X移动到盒子Y右边（如果X已经在Y的右边则忽略此指令）。
 * 3 X Y表示交换盒子X和Y的位置。
 * 4 表示反转整条链。
 * 指令保证合法，即X不等于Y。例如，当n=6时在初始状态下执行114后，盒子序列为2 3
 * 1 4 5 6。接下来执行2 3 5，盒子序列变成2 1 4 5 3 6。再执行3 1 6，得到2 6 4 5 3 1。最终执
 * 行4，得到1 3 5 4 6 2。
 * 输入包含不超过10组数据，每组数据第一行为盒子个数n和指令条
 * 数m（1≤n,m≤100000），以下m行每行包含一条指令。每组数据输出一行，即所有奇数位置
 * 的盒子编号之和。位置从左到右编号为1～n。
 * 样例输入：
 * 6 4
 * 1 1 4
 * 2 3 5
 * 3 1 6
 * 4
 * 6 3
 * 1 1 4
 * 2 3 5
 * 3 1 6
 * 100000 1
 * 4
 * 样例输出：
 * Case 1: 12
 * Case 2: 9
 * Case 3: 2500050000
 * <p>
 * Boxes in a Line, UVa 12657
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=4395
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("------------------------------------------------------------");
        solution.case2();
        System.out.println("------------------------------------------------------------");
        solution.case3();
    }

    private void case1() {
        int[][] commands = {{1, 1, 4}, {2, 3, 5}, {3, 1, 6}, {4, 0, 0}};
        System.out.println("," + oddIndexNum(6, commands));
    }

    private void case2() {
        int[][] commands = {{1, 1, 4}, {2, 3, 5}, {3, 1, 6}};
        System.out.println("," + oddIndexNum(6, commands));
    }

    private void case3() {
        int[][] commands = {{4, 0, 0}};
        System.out.println("," + oddIndexNum(100000, commands));
    }

    private long oddIndexNum(int n, int[][] commands) {
        System.out.print("n:" + n + ",commands:" + Arrays.deepToString(commands));
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            list.add(i + 1);
        }
        for (int[] command : commands) {
            executeCommand(command, list);
        }
        long sum = 0;
        for (int i = 1; i <= n; i = i + 2) {
            sum += list.pollFirst();
            list.pollFirst();
        }
        return sum;
    }

    private void executeCommand(int[] command, LinkedList<Integer> list) {
        int c = command[0];
        int p1 = command[1];
        int p2 = command[2];
        switch (c) {
            case 1:
                doLeftSwap(list, p1, p2);
                return;
            case 2:
                doRightSwap(list, p1, p2);
                return;
            case 3:
                swap(list, p1, p2);
                return;
            case 4:
                reverse(list);
                return;
            default:
                throw new IllegalArgumentException(c + " is not allowed.");

        }
    }

    private void doLeftSwap(LinkedList<Integer> list, int v1, int v2) {
        int[] ps = indexOf(list, v1, v2);
        int leftIndex = ps[1] - 1;
        if (ps[0] == leftIndex) {
            return;
        }
        if (ps[1] == 0) {
            list.remove(ps[0]);
            list.addFirst(v1);
        } else {
            list.remove(ps[0]);
            list.add(leftIndex, v1);
        }
    }

    private void doRightSwap(LinkedList<Integer> list, int v1, int v2) {
        int[] ps = indexOf(list, v1, v2);
        int rightIndex = ps[1] + 1;
        if (ps[0] == rightIndex) {
            return;
        }
        if (ps[1] == list.size() - 1) {
            list.remove(ps[0]);
            list.addLast(v1);
        } else {
            list.remove(ps[0]);
            list.add(rightIndex - 1, v1);
        }
    }

    private void swap(LinkedList<Integer> list, int v1, int v2) {
        int[] ps = indexOf(list, v1, v2);
        if (ps[0] == ps[1]) {
            return;
        }
        swap(list, ps[0], v1, ps[1], v2);
    }

    private int[] indexOf(LinkedList<Integer> list, int v1, int v2) {
        int p1 = list.indexOf(v1);
        if (p1 < 0) {
            throw new IllegalArgumentException();
        }
        int p2 = list.indexOf(v2);
        if (p2 < 0) {
            throw new IllegalArgumentException();
        }
        return new int[]{p1, p2};
    }

    private void swap(LinkedList<Integer> list, int p1, int v1, int p2, int v2) {
        list.set(p1, v2);
        list.set(p2, v1);
    }

    private void reverse(LinkedList<Integer> list) {
        Collections.reverse(list);
    }
}
