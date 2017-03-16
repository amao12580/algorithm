package DP.knapsack;

import basic.Util;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/9/1
 * Time:11:06
 * <p>
 * 话说有一哥们去森林里玩发现了一堆宝石，他数了数，一共有n个。 但他身上能装宝石的就只有一个背包，背包的容量为C。
 * 这哥们把n个宝石排成一排并编上号： 0,1,2,…,n-1。第i个宝石对应的体积和价值分别为V[i]和W[i] 。
 * 排好后这哥们开始思考： 背包总共也就只能装下体积为C的东西，那我要装下哪些宝石才能让我获得最大的利益呢？
 */
public class Solution {
    private static int capacity = 0;

    static class Gem {
        int bulk;//所需体积
        int value;//价值

        Gem(int bulk, int value) {
            this.bulk = bulk;
            this.value = value;
        }
    }

    public static void main(String[] args) {
        int[] gem = {2, 10, 20, 15, 25, 5, 5, 30, 15, 40};//第一枚宝石的价值是10，第二枚宝石的价值是20...
        Gem[] gems = {new Gem(1, 2), new Gem(2, 10), new Gem(2, 10)};
        capacity = 161;//背包的容量
        int[] result = maxValues(gem);
        System.out.println("宝石的总数量:" + gem.length + ",带走了总价值为" + sum(result) + "的" + result.length + "枚宝石.剩余的宝石个数:" + (gem.length - result.length) + ",背包剩余容量:" + capacity);
        System.out.println(Arrays.toString(result));
    }

    private static int[] maxValues(int[] gem) {
        int length = gem.length;
        int[] result = new int[length];
        int index = 0;
        Arrays.sort(gem);
        System.out.println(Arrays.toString(gem));
        for (int i = length - 1; i >= 0; i--) {
            int match = Util.binarySearchLatestLessThan(gem, 0, i, capacity);
            if (match > 0) {
                match = gem[match];
                result[index] = match;
                index++;
                capacity -= match;
            }
        }
        if (index < length - 1) {
            int[] finalResult = new int[index];
            System.arraycopy(result, 0, finalResult, 0, index);
            return finalResult;
        }
        return result;
    }

    private static int sum(int[] result) {
        int sum = 0;
        for (int item : result) {
            sum += item;
        }
        return sum;
    }
}
