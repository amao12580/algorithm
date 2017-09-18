package ACM.EgyptDenominator;

import basic.Util;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/9/18
 * Time:18:12
 * <p>
 * 要求给你个真分数，你需要将其化简为最少的若干特殊真分数之和，你要输出这个序列（序列按递增序）。
 * 如果有不同的方案，则分数个数相同的情况下使最大的分母最小。若还相同，则使次大的分母最大……以此类推。
 * 如：2/3=1/2+1/6，但不允许2/3=1/3+1/3，因为加数中有相同的。
 * 对于一个分数a/b，表示方法有很多种，但是哪种最好呢？
 * 首先，加数少的比加数多的好，其次，加数个数相同的，最小的分数越大越好。如：
 * 19/45=1/3 + 1/12 + 1/180
 * 19/45=1/3 + 1/15 + 1/45
 * 19/45=1/3 + 1/18 + 1/30
 * 19/45=1/4 + 1/6 + 1/180
 * 19/45=1/5 + 1/6 + 1/18
 * 最好的是最后一种，因为18 比180, 45, 30,都小。
 * <p>
 * <p>
 * 真分数，指的是分子比分母小的分数。真分数的分数值小于一。
 * <p>
 * <p>
 * 样例输入：
 * 495 499
 * 样例输出：
 * Case 1: 495/499=1/2+1/5+1/6+1/8+1/3992+1/14970
 */
public class Solution {
    public static void main(String[] args) {
//        new Solution().case1();
//        new Solution().case2();
        new Solution().case3();
//        new Solution().case4();
    }

    private void case1() {
        best(19, 45);
    }

    private void case2() {
        best(495, 499);
    }

    private void case3() {
        best(6, 72);
    }


    private void case4() {
        best(Util.getRandomInteger(2, 20000), Util.getRandomInteger(20001, 100000));
    }

    private void best(long up, long down) {
        long s = System.currentTimeMillis();
        System.out.println("up:" + up + ",down:" + down);
        if (up == 1 || down % up == 0) {
            System.out.println(up + "/" + down + "=1/" + (up == 1 ? down : down / up));
            return;
        }
        System.out.println("time:" + (System.currentTimeMillis() - s));
        System.out.println("---------------------------------------------------");
    }

    private boolean isSumMatch(long thisUp, long thisDown, LinkedList<Long> downs) {
        long[] other = sum(downs);
        long otherUp = other[0];
        long otherDown = other[1];
        return otherUp == thisUp && otherDown == thisDown || thisUp * otherDown == thisDown * otherUp;
    }

    private long[] sum(LinkedList<Long> downs) {
        long[] d = new long[2];
        d[0] = 1;
        d[1] = downs.poll();
        while (!downs.isEmpty()) {
            d = sum(d[0], d[1], 1, downs.poll());
        }
        return d;
    }

    private long[] sum(long thisUp, long thisDown, long otherUp, long otherDown) {
        long[] d = new long[2];
        d[0] = thisUp * otherDown + thisDown * otherUp;
        d[1] = thisDown * otherDown;
        return d;
    }
}
