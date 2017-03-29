package ACM.Box;

import basic.Util;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/3/29
 * Time:9:20
 * <p>
 * 给定6个矩形的长和宽w i 和h i （1≤w i ，h i ≤1000），判断它们能否构成长方体的6个面。
 * <p>
 * Box, ACM/ICPC NEERC 2004, UVa1587
 */
public class Solution {
    public static void main(String[] args) {
        int[][] s = {{2, 3}, {2, 4}, {3, 4}, {2, 3}, {2, 4}, {3, 4}};
        int[][][] target = {s, generatorOne()};
        for (int[][] cube : target) {
            System.out.println("target:" + print(cube));
            System.out.println("value:" + Box(cube));
            System.out.println("-------------------------");
        }
    }

    private static boolean Box(int[][] cube) {
        Set<Integer> set = new HashSet<>();
        for (int[] c : cube) {
            for (int aC : c) {
                set.add(aC);
                if (set.size() > 3) {
                    return false;
                }
            }
        }
        return true;
    }


    private static String print(int[][] arrays) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int[] c : arrays) {
            for (int aC : c) {
                stringBuilder = stringBuilder.append(aC);
                stringBuilder = stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }

    private static int[][] generatorOne() {
        int[][] result = new int[6][2];
        for (int i = 0; i < 6; i++) {
            int wi = Util.getRandomInteger(1, 1000);
            int hi = Util.getRandomInteger(1, 1000);
            int[] rectangle = {wi, hi};
            result[i] = rectangle;
        }
        return result;
    }
}
