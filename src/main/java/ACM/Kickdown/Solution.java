package ACM.Kickdown;

import basic.Util;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/3/29
 * Time:10:33
 * <p>
 * 给出两个长度分别为n 1 ，n 2 （n 1 ，n 2 ≤100）且每列高度只为1或2的长条。
 * 需要将它们放入一个高度为3的容器（如图3-8所示），问能够容纳它们的最短容器长度。
 * <p>
 * <p>
 * Kickdown, ACM/ICPC NEERC 2006, UVa1588
 */
public class Solution {
    public static void main(String[] args) {
        String[] s = {"2112112112", "2212112"};
        String[][] target = {s, generatorOne(), generatorOne()};
        for (String[] item : target) {
            System.out.println("target:" + print(item));
            System.out.println("value:" + Kickdown(item));
            System.out.println("-------------------------");
        }
    }

    private static int Kickdown(String[] item) {
        String n1 = item[0];
        int[] c1 = breakString(n1);
        int c1l = c1.length;
        String n2 = item[1];
        int[] c2 = breakString(n2);
        int c2l = c2.length;
        int beginIndex = 0;
        int[] move, fixed;
        if (c1l > c2l) {
            move = c2;
            fixed = c1;
        } else {
            move = c1;
            fixed = c2;
        }
        int fl = fixed.length;
        int ml = move.length;
        while (beginIndex < fl) {
            //begin到end，是两个长条交叉的部分
            int endIndex = ml + beginIndex > fl ? fl : ml + beginIndex;
            boolean FC = false;
            for (int i = beginIndex; i < endIndex && !FC; i++) {
                if (fixed[i] + move[i - beginIndex] > 3) {
                    beginIndex++;
                    FC = true;
                }
            }
            if (!FC) {
                System.out.println("crossed.");
                return ml + beginIndex > fl ? ml + beginIndex : fl;
            }
        }
        return ml + fl;
    }

    private static int[] breakString(String s) {
        int l = s.length();
        int[] r = new int[l];
        for (int i = 0; i < l; i++) {
            r[i] = Integer.valueOf(s.charAt(i) + "");
        }
        return r;
    }

    private static String print(String[] arrays) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : arrays) {
            StringBuilder s = new StringBuilder();
            char[] chars = string.toCharArray();
            for (char c : chars) {
                s = s.append(c);
                s = s.append(",");
            }
            stringBuilder = stringBuilder.append(s);
            stringBuilder = stringBuilder.append(";       ");
        }
        return stringBuilder.toString();
    }

    private static String[] generatorOne() {
        String[] result = new String[2];
        int len1 = Util.getRandomInteger(1, 100);
        StringBuilder s1 = new StringBuilder();
        for (int i = 0; i < len1; i++) {
            s1 = s1.append(Util.getRandomInteger(1, 2));
        }
        result[0] = s1.toString();
        int len2 = Util.getRandomInteger(1, 100);
        StringBuilder s2 = new StringBuilder();
        for (int i = 0; i < len2; i++) {
            s2 = s2.append(Util.getRandomInteger(1, 2));
        }
        result[1] = s2.toString();
        return result;
    }
}
