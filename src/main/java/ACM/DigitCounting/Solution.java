package ACM.DigitCounting;

import basic.Util;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/3/22
 * Time:9:13
 * <p>
 * 把前n（n≤10000）个整数顺次写在一起：123456789101112…数一数0～9各出现多少次（输出10个整数，分别是0，1，…，9出现的次数）。
 */
public class Solution {
    public static void main(String[] args) {
        String[] target = {"123456789101112", generatorOne()};
        for (String item : target) {
            System.out.print("target:" + item);
            System.out.println(",value:" + Arrays.toString(DigitCounting(item)));
        }
    }

    private static int[] DigitCounting(String string) {
        int[] result = new int[10];
        int rLen = result.length;
        int len = string.length();
        for (int i = 0; i < len; i++) {
            int c = Integer.valueOf(string.charAt(i) + "");
            if (c < rLen) {
                result[c] += 1;
            }
        }
        return result;
    }


    private static String generatorOne() {
        StringBuilder stringBuilder = new StringBuilder();
        int len = Util.getRandomInteger(5, 10000);
        stringBuilder = stringBuilder.append(Util.getRandomInteger(1, 1000));
        for (int i = 0; i < len; i++) {
            stringBuilder = stringBuilder.append(Util.getRandomInteger(0, 1000));
        }
        return stringBuilder.toString();
    }
}
