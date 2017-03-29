package ACM.AllinAll;

import basic.Util;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/3/28
 * Time:18:22
 * <p>
 * 输入两个字符串s和t，判断是否可以从t中删除0个或多个字符（其他字符顺序不变），
 * 得到字符串s。例如，abcde可以得到bce，但无法得到dc。
 * <p>
 * UVa 10340
 */
public class Solution {
    private static char[] seeds = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public static void main(String[] args) {
        String[][] target = {{"abcde", "bce"}, {"abcde", "dc"}, {"abcde", "bc"}, {"aaaaa", "aa"}, {"aaaaa", "a"}, generatorOne()};
        for (String[] item : target) {
            System.out.println("target:" + print(item));
            System.out.println("value:" + AllinAll(item));
            System.out.println("-------------------------");
        }
    }

    private static boolean AllinAll(String[] array) {
        String s = array[0];
        String t = array[1];
        char[] sArray = s.toCharArray();
        int sLen = s.length();
        int tLen = t.length();
        if (tLen > sLen) {
            return false;
        }
        int beginIndex = 0;
        for (int i = 0; i < tLen; i++) {
            int p = findPosition(sArray, beginIndex, t.charAt(i));
            if (p >= 0) {
                beginIndex = p + 1;
            } else {
                return false;
            }
        }
        return true;
    }

    private static int findPosition(char[] array, int beginIndex, char key) {
        int len = array.length;
        for (int i = beginIndex; i < len; i++) {
            if (array[i] == key) {
                return i;
            }
        }
        return -1;
    }

    private static String print(String[] arrays) {
        return arrays[0] + "," + arrays[1];
    }

    private static String[] generatorOne() {
        String[] result = new String[2];
        int sLen = Util.getRandomInteger(5, 100);
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < sLen; i++) {
            s = s.append(seeds[Util.getRandomInteger(0, seeds.length - 1)]);
        }
        result[0] = s.toString();
        int tLen = Util.getRandomInteger(5, 100);
        StringBuilder t = new StringBuilder();
        for (int i = 0; i < tLen; i++) {
            t = t.append(seeds[Util.getRandomInteger(0, seeds.length - 1)]);
        }
        result[1] = t.toString();
        return result;
    }
}
