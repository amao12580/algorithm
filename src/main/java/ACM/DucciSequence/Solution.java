package ACM.DucciSequence;

import basic.Util;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/4
 * Time:15:52
 * <p>
 * 对于一个n元组(a 1 , a 2 , …, a n )，可以对于每个数求出它和下一个数的差的绝对值，得到
 * 一个新的n元组(|a 1 -a 2 |, |a 2 -a 3 |, …, |a n -a 1 |)。重复这个过程，得到的序列称为Ducci序列，例如：
 * (8, 11, 2, 7) -> (3, 9, 5, 1) -> (6, 4, 4, 2) -> (2, 0, 2, 4) -> (2, 2, 2, 2) -> (0, 0, 0, 0).
 * 也有的Ducci序列最终会循环。输入n元组（3≤n≤15），你的任务是判断它最终会变成0
 * 还是会循环。输入保证最多1000步就会变成0或者循环。
 * <p>
 * Ducci Sequence, ACM/ICPC Seoul 2009, UVa1594
 */
public class Solution {
    private int maxCheck = 1000;

    private int length = 0;

    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] seed = solution.generatorOne();
        solution.print(seed);
        solution.check(seed);
    }

    private void print(int[] seed) {
        System.out.println(Arrays.toString(seed));
    }

    private int[] generatorOne() {
        int len = Util.getRandomInteger(3, 15);
        int[] result = new int[len];
        for (int i = 0; i < len; i++) {
            result[i] = Util.getRandomInteger(0, 20);
        }
        return result;
    }


    private void check(int[] array) {
        if (length == 0) {
            length = array.length;
        }
        int[] newArray = new int[length];
        boolean isAllZero = true;
        boolean isAllSame = true;
        Integer c = null;
        for (int i = 0; i < length; i++) {
            if (i != length - 1) {
                newArray[i] = Math.abs(array[i] - array[i + 1]);
            } else {
                newArray[i] = Math.abs(array[i] - array[0]);
            }
            if (newArray[i] != 0 && isAllZero) {
                isAllZero = false;
            }
            if (c == null) {
                c = newArray[i];
            } else if (newArray[i] != c && isAllSame) {
                isAllSame = false;
            }
        }
        maxCheck--;
        if (maxCheck < 0) {
            System.out.println("over check.");
            return;
        }
        if (isAllZero || isAllSame) {
            System.out.println("left check:" + maxCheck);
            System.out.println("Ducci Sequence.");
            return;
        }
        check(newArray);
    }
}
