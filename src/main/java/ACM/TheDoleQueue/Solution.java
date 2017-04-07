package ACM.TheDoleQueue;

import basic.Util;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/4/7
 * Time:9:23
 * <p>
 * n(n<20)个人站成一圈，逆时针编号为1～n。有两个官员，A从1开始逆时针数，B从n开始顺时针数。
 * 在每一轮中，官员A数k个就停下来，官员B数m个就停下来（注意有可能两个官员停在同一个人上）。
 * 接下来被官员选中的人（1个或者2个）离开队伍。输入n，k，m输出每轮里被选中的人的编号（如果有两个人，先输出被A选中的）。
 * 例如，n=10，k=4，m=3，输出为4 8, 9 5, 3 1, 2 6, 10, 7。注意：输出的每个数应当恰好占3列。
 * <p>
 * The Dole Queue, UVa 133
 * <p>
 * 单向环状链表数据结构，配合出队操作（swap），可以减少寻找次数
 */
public class Solution {
    public static void main(String[] args) {
        TheDoleQueue(10, 4, 3);
        TheDoleQueue(Util.getRandomInteger(5, 19), Util.getRandomInteger(1, 19), Util.getRandomInteger(1, 19));
    }

    private static void TheDoleQueue(int n, int k, int m) {
        System.out.println("n:" + n + ",k:" + k + ",m:" + m);
        boolean[] h = new boolean[n];
        int A = -1;
        int B = n;
        int out = 0;
        while (out < n) {
            for (int i = 1; i <= k; ) {
                A++;
                if (A > n - 1) {
                    A = 0;
                }
                if (!h[A]) {
                    i++;
                }
            }
            for (int i = 1; i <= m; ) {
                B--;
                if (B < 0) {
                    B = n - 1;
                }
                if (!h[B]) {
                    i++;
                }
            }
            h[A] = true;
            out++;
            if (B != A) {
                h[B] = true;
                out++;
            }
            System.out.println((A + 1) + (A == B ? "" : "," + (B + 1)));
        }
        System.out.println("---------------------------------------");
    }
}
