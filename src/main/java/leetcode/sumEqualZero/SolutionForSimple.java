package leetcode.sumEqualZero;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/6/22
 * Time:13:39
 * <p>
 * 有一个随机整数数组，从中挑ABC三个整数，让ABC三个整数加起来等于零，看有多少个不重复的组合？
 */
public class SolutionForSimple {
    public static void main(String[] args) {
        int[] originArray = {0, 1, 2, 3, 4, 5, -1, -2, -3, -4, -5};
        System.out.println("input:" + Arrays.toString(originArray));
        System.out.println("-------      simple        -------");
        println(simple(originArray, 3));
    }

    private static void println(int[][] result) {
        System.out.println();
        System.out.println("-------      begin        -------");
        System.out.println("-------      count:" + result.length + "        -------");
        for (int i = 0; i < result.length; i++) {
            System.out.println((i + 1) + ":" + Arrays.toString(result[i]));
        }
        System.out.println("-------      end        -------");
    }

    /**
     * 简单实现，3重for循环，同时去重
     *
     * @param originArray 原始数组
     * @param number      几个数相加？
     * @return 结果组合
     */
    private static int[][] simple(int[] originArray, int number) {
        int length = originArray.length;
        int[][] result = new int[length][number];
        int index = 0;
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                for (int k = j + 1; k < length; k++) {
                    if ((originArray[i] + originArray[j] + originArray[k]) == 0) {
                        int[] group = {originArray[i], originArray[j], originArray[k]};
                        if (index >= result.length) {
                            result = resize(result);
                        }
                        result[index] = group;
                        index++;
                    }
                }
            }
        }
        if ((result.length - index) > 0) {
            result = trim(result, index);
        }
        return result;
    }

    private static int[][] trim(int[][] result, int index) {
        int[][] newResult = new int[index][result[0].length];
        System.arraycopy(result, 0, newResult, 0, newResult.length);
        return newResult;
    }

    private static int[][] resize(int[][] result) {
        int[][] newResult = new int[result.length + result[0].length][result[0].length];
        System.arraycopy(result, 0, newResult, 0, result.length);
        return newResult;
    }
}
