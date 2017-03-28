package ACM.RepeatingDecimals;

import basic.Util;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/3/28
 * Time:10:05
 * <p>
 * 输入整数a和b（0≤a≤3000，1≤b≤3000），输出a/b的循环小数表示以及循环节长度。
 * <p>
 * 例如a=5，b=43，小数表示为0.(116279069767441860465)，循环节长度为21。
 * <p>
 * <p>
 * 如果无限小数的小数点后，从某一位起向右进行到某一位止的一节数字循环出现，首尾衔接，称这种小数为循环小数，这一节数字称为循环节．
 * <p>
 * Repeating Decimals, ACM/ICPC World Finals 1990, UVa202
 */
public class Solution {
    public static void main(String[] args) {
        int[][] target = {{5, 43}, {100, 3}, generatorOne()};
        for (int[] item : target) {
            System.out.println("target:" + print(item));
            System.out.println("value:" + print(RepeatingDecimals(item)));
            System.out.println("-------------------------");
        }
    }

    private static String print(String[] arrays) {
        return arrays[0] + "," + arrays[1];
    }

    private static String print(int[] arrays) {
        return arrays[0] + "," + arrays[1];
    }

    private static String[] RepeatingDecimals(int[] number) {
        int a = number[0];
        int b = number[1];
        String[] result = new String[2];
        int s = a / b;
        result[0] = s + "";
        int y = a - s * b;
        if (y == 0) {
            result[1] = 0 + "";
            return result;
        }
        int beginningY = y;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            int c = y * 10;
            int s1 = c / b;
            str = str.append(s1);
            y = c - s1 * b;
            if (beginningY == y) {
                break;
            }
        }
        result[1] = str.toString();
        return result;
    }

    private static int[] generatorOne() {
        int[] result = new int[2];
        result[0] = Util.getRandomInteger(0, 3000);
        result[1] = Util.getRandomInteger(1, 3000);
        return result;
    }
}
