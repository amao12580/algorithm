package DP.coins;

import java.util.Arrays;

/**
 * http://www.hawstein.com/posts/dp-novice-to-advanced.html
 * <p>
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/6/23
 * Time:17:04
 * <p>
 * 有面值为1元、3元和5元的硬币若干枚，如何用最少的硬币凑够11元？
 * <p>
 *
 * V1 没有使用DP思想解题，直接采用递归暴力分解
 */
public class SolutionV1 {
    private static int maxValue = 0;
    private static int minValue = 0;
    private static int index = 0;
    private static int[] subSolution = null;
    private static int[] coins = null;
    private static int[] result = new int[maxValue];

    public static void main(String[] args) {
        //初始化硬币的种类和个数
        coins = new int[5 + 1];//硬币的最大面值为5
        coins[1] = 10;//假设面值为1的硬币有10个
        coins[3] = 10;//假设面值为3的硬币有10个
        coins[5] = 10;//假设面值为5的硬币有10个
        //其他没有初始化的硬币面值，比如说面值为4，个数为0.
        int sum = 11;//硬币需要凑够11元
        maxValue = 5;
        initSubSolution(sum + 1);
        minValue = firstElement(coins);
        int s = loop(sum);

        System.out.println(s);
        System.out.println("总少硬币数有" + result.length + "枚");
        System.out.println("result:" + Arrays.toString(result));
        System.out.println("subSolution:" + Arrays.toString(subSolution));
    }

    private static void initSubSolution(int len) {
        subSolution = new int[len];
        for (int i = 2; i < len; i++) {
            subSolution[i] = -1;
        }
        subSolution[0] = 0;
        subSolution[1] = 1;
        subSolution[3] = 1;
        subSolution[5] = 1;
    }

    private static int loop(int sum) {
        if (sum <= 0) {
            return 0;
        }
        int part = sum - maxValue;
        int here = subSolution[sum];
        if (here > 0) {
            append(sum);
            return here;
        } else {
            if (part < 0) {
                maxValue = previousElement(coins);
                part = sum;
            }
        }
        append(maxValue);
        return loop(part);
    }

    private static void append(int sum) {
        if (index > result.length - 1) {
            int[] newResult = new int[result.length + 1];
            System.arraycopy(result, 0, newResult, 0, result.length);
            result = newResult;
        }
        result[index] = sum;
        index++;
    }

    private static Integer previousElement(int[] coins) {
        if (maxValue < 1) {
            return minValue;
        }
        for (int i = maxValue - 1; i >= 0; i--) {
            if (coins[i] > 0) {
                return i;
            }
        }
        return minValue;
    }

    private static Integer firstElement(int[] coins) {
        for (int i = 0; i < coins.length; i++) {
            if (coins[i] > 0) {
                return i;
            }
        }
        return null;
    }
}
