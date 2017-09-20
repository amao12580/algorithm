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
    private long[] target;

    public static void main(String[] args) {
        new Solution().case1();
//        new Solution().case2();
//        new Solution().case3();
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
        this.up = up;
        this.down = down;
        this.target = new long[2];
        this.target[0] = up;
        this.target[1] = down;
        LinkedList<Long> group = new LinkedList<>();
        long[] groupSum = new long[2];
        best(2, group, groupSum);
        print();
        System.out.println("time:" + (System.currentTimeMillis() - s));
        System.out.println("---------------------------------------------------");
    }

    private LinkedList<Long> bestGroup = new LinkedList<>();
    private long up;
    private long down;

    private void print() {
        if (bestGroup.isEmpty()) {
            System.out.println("error.");
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder = builder.append(this.up).append("/").append(this.down);
        builder = builder.append("=");
        while (!bestGroup.isEmpty()) {
            builder = builder.append(1 + "/").append(bestGroup.poll());
            if (!bestGroup.isEmpty()) {
                builder = builder.append(" + ");
            }
        }
        System.out.println(builder.toString());
    }

    private void best(long numUpper, LinkedList<Long> group, long[] groupSum) {
        long[] downBound = downBound(numUpper, group, groupSum);//A*
        long downBoundLower = downBound[0];
        long downBoundUpper = downBound[1];
        LinkedList<Long> current;
        long[] currentSum;
        for (long i = downBoundLower; i <= downBoundUpper; i++) {
            current = new LinkedList<>(group);
            current.add(i);
            currentSum = current.size() == 1 ? build(i) : sum(groupSum[0], groupSum[1], i);
            if (current.size() == numUpper) {
                check(current, currentSum);
            } else {
                best(numUpper, current, currentSum);//DFS
            }
        }
        if (bestGroup.isEmpty()) {//ID
            best(numUpper + 1, group, groupSum);
        }
    }

    private long[] build(long down) {
        long[] result = new long[2];
        result[0] = 1;
        result[1] = down;
        return result;
    }

    private void check(LinkedList<Long> group, long[] groupSum) {
        long c = compare(this.up, this.down, groupSum[0], groupSum[1]);
        if (c < 0) {
            return;
        }
        if (c == 0) {
            if (bestGroup.isEmpty() || group.size() < bestGroup.size() ||
                    (group.size() == bestGroup.size() && group.peekLast() < bestGroup.peekLast())) {
                bestGroup=group;
            }
        }
    }

    private long[] downBound(long numUpper, LinkedList<Long> group, long[] groupSum) {
        if (group.size() >= numUpper) {
            throw new IllegalArgumentException();
        }
        long[] result = new long[2];
        long[] odd;
        long lower, upper;
        if (group.isEmpty()) {
            lower = (this.down / this.up) + 1;
            odd = target;
        } else {
            odd = sub(up, down, groupSum[0], groupSum[1]);
            System.out.println(group.toString() + "    " + numUpper);
            lower = odd[1] % odd[0] == 0 ? odd[1] / odd[0] : (odd[1] / odd[0]) + 1;
            lower = lower > group.peekLast() ? lower : group.peekLast() + 1;
        }
        long u = (numUpper - group.size()) * odd[1];
        long d = odd[0];
        upper = u % d == 0 ? (u / d) - 1 : u / d;
        upper = upper >= lower ? upper : lower + 1;
        result[0] = lower;
        result[1] = upper;
        return result;
    }

    private long compare(long thisUp, long thisDown, long otherUp, long otherDown) {
        return thisUp * otherDown - thisDown * otherUp;
    }

    private long[] sum(long thisUp, long thisDown, long otherDown) {
        long[] d = new long[2];
        d[0] = thisUp * otherDown + thisDown;
        d[1] = thisDown * otherDown;
        return d;
    }

    private long[] sub(long thisUp, long thisDown, long otherUp, long otherDown) {
        long[] d = new long[2];
        if (thisUp == otherUp && thisDown == otherDown) {
            return d;
        }
        if (thisDown == 0 && thisUp != 0) {
            throw new IllegalArgumentException();
        }
        if (otherDown == 0 && otherUp != 0) {
            throw new IllegalArgumentException();
        }
        d[0] = thisUp * otherDown - thisDown * otherUp;
        d[1] = thisDown * otherDown;
        return d;
    }
}