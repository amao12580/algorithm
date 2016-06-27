package DP.coins;

import basic.Comparator;

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
 * V2用了DP思想来解题
 */
public class SolutionV2 {
    private static int[] existedSolution = null;
    private static int[] coins = {5, 3, 1};//只有3种硬币;

    public static void main(String[] args) {
        int sum = 11;//硬币需要凑够11元
        initExistedSolution(sum + 1);
        int count = loop(sum);
        System.out.println(count);
        System.out.println("cached:" + Arrays.toString(existedSolution));
    }

    /**
     * 初始化一些已知的答案，例如凑够5元，最少需一枚；凑够3元，最少需一枚。
     */
    private static void initExistedSolution(int len) {
        existedSolution = new int[len];
        for (int i = 0; i < len; i++) {
            if (i == 0) {
                existedSolution[0] = 0;
            } else {
                if (i < coins.length) {
                    existedSolution[coins[i]] = 1;
                } else {
                    existedSolution[i] = -1;
                }
            }
        }
    }


    private static int loop(int amount) {
        if (amount <= 0) {
            return existedSolution[0];
        }
        //如果这是一个已知的答案，就取出缓存
        if (existedSolution[amount] >= 0) {
            return existedSolution[amount];
        }
        //拆分为同性质的子问题
        int[] group = new int[coins.length];
        int index = 0;
        for (int coin : coins) {
            //状态方程：每一步，均可能出现3中硬币
            int part = amount - coin;
            int count;
            if (part < 0) {
                count = -1;
            } else {
                count = loop(part) + 1;
            }
            group[index] = count;
            index++;
        }
        //状态转移方程：在所有子问题的答案中，取得最小值
        //同时将已有的答案缓存，答案一经解出就不再会变化了。
        existedSolution[amount] = min(group);
        return existedSolution[amount];
    }

    /**
     * 计算数组中的最小元素，小于零的元素不参与计算
     */
    public static int min(int[] array) {
        array = lessThanOrEqualZero(array);
        int min = array[0];
        for (int e : array) {
            if (Comparator.isGT(min, e)) {
                min = e;
            }
        }
        return min;
    }

    /**
     * 剔除掉数组中所有小于0的元素，并resize数组
     */
    public static int[] lessThanOrEqualZero(int[] array) {
        int[] result = new int[array.length];
        int index = 0;
        for (int e : array) {
            if (e >= 0) {
                result[index] = e;
                index++;
            }
        }
        if (index < array.length) {
            int[] finalResult = new int[index];
            System.arraycopy(result, 0, finalResult, 0, index);
            result = finalResult;
        }
        return result;
    }
}
