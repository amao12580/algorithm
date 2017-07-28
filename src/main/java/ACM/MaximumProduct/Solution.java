package ACM.MaximumProduct;

import basic.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/7/27
 * Time:17:41
 * <p>
 * 输入n个元素组成的序列S，你需要找出一个乘积最大的连续子序列。如果这个最大的乘
 * 积不是正数，应输出0（表示无解）。1≤n≤18，-10≤S i ≤10。
 * 样例输入：
 * 3
 * 2 4-3
 * 5
 * 2 5 -1 2 -1
 * 样例输出：
 * 8
 * 20
 * <p>
 * Maximum Product, UVa 11059
 */
public class Solution {
    public static void main(String[] args) {
        new Solution().case1();
        System.out.println("-----------------------------------");
        new Solution().case2();
        System.out.println("-----------------------------------");
        new Solution().case3();
    }

    private void case1() {
        byte[] desc = {2, 4, -3};
        product(desc);
    }

    private void case2() {
        byte[] desc = {2, 5, -1, 2, -1};
        product(desc);
    }

    private void case3() {
        byte[] desc = generate();
        product(desc);
    }

    private byte[] generate() {
        int len = Util.getRandomInteger(1, 18);
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Util.getRandomBoolean() ? (byte) Util.getRandomInteger(0, 10) : ((byte) (0 - Util.getRandomInteger(0, 10)));
        }
        return result;
    }

    private void product(byte[] desc) {
        System.out.println("desc:" + Arrays.toString(desc));
        Long positive = null;
        List<Byte> negative = new ArrayList<>();
        for (byte item : desc) {
            if (item > 0) {
                if (positive == null) {
                    positive = (long) item;
                } else {
                    positive *= (long) item;
                }
            }

            if (item < 0) {
                negative.add(item);
            }
        }
        if (positive == null && negative.isEmpty()) {//没有正数也没有负数，可能的情况是：全为零或空数组
            System.out.println(0);
            return;
        }
        if (positive != null && negative.isEmpty()) {//有正数，但没有负数
            System.out.println(positive);
            return;
        }
        int size = negative.size();
        long max = positive == null ? 1 : positive;
        if (size == 1) {//负数只有一个
            System.out.println(max);
            return;
        }
        Collections.sort(negative);
        if (size % 2 == 0) {//负数的个数为偶数个
            System.out.println(max * product(negative, 0, size - 1));
        } else {
            System.out.println(max * product(negative, 0, size - 2));
        }
    }

    private long product(List<Byte> seed, int beginIndex, int endIndex) {
        long result = 1;
        for (int i = beginIndex; i <= endIndex; i++) {
            result *= seed.get(i);
        }
        return result;
    }
}
