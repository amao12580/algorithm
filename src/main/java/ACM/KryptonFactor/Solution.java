package ACM.KryptonFactor;

import basic.Util;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/8/1
 * Time:17:22
 * <p>
 * 如果一个字符串包含两个相邻的重复子串，则称它是“容易的串”，其他串称为“困难的
 * 串”。例如，BB、ABCDACABCAB、ABCDABCD都是容易的串，而D、DC、ABDAB、
 * CBABCBA都是困难的串。
 * 输入正整数n和L，输出由前L个字符组成的、字典序第k小的困难的串。例如，当L=3
 * 时，前7个困难的串分别为A、AB、ABA、ABAC、ABACA、ABACAB、ABACABA。输入
 * 保证答案不超过80个字符。
 * 样例输入：
 * 7 3
 * 30 3
 * 样例输出：
 * ABACABA
 * ABACABCACBABCABACABCACBACABA
 * <p>
 * Krypton Factor, UVa 129
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=onlinejudge&page=show_problem&problem=65
 * <p>
 * Sample Input
 * 7 3
 * 30 3
 * 0 0
 * Sample Output
 * ABAC ABA
 * 7
 * ABAC ABCA CBAB CABA CABC ACBA CABA
 * 28
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("------------------------------------------------");
        solution.case2();
        System.out.println("------------------------------------------------");
        solution.case3();
    }

    private void case1() {
        krypton(7, 3);
    }

    private void case2() {
        krypton(30, 3);
    }


    private void case3() {
        krypton(Util.getRandomInteger(5, 100), Util.getRandomInteger(3, 26));
    }

    //A - Z  65 - 90
    private void krypton(int n, int L) {
        System.out.println(Util.contactAll("n:", n, ",", "L:", L));
        krypton("", n, L);
        print(n);
        result = null;
        resultSize = 0;
    }

    private void krypton(String sequence, int n, int L) {
        if (!sequence.isEmpty() && !addResult(sequence, n)) {
            return;
        }
        char c;
        for (int i = 0; i < L; i++) {
            c = (char) (i + 65);
            if (!hasRepeatSubSequence(sequence, c)) {
                krypton(sequence + c, n, L);
            }
        }
    }

    private void print(int n) {
        if (resultSize == n && result != null) {
            int len = 4;
            int nLen = 16;
            int cLen = 0;
            int cnLen = 0;
            StringBuilder builder = new StringBuilder();
            char[] chars = result.toCharArray();
            for (char c : chars) {
                builder = builder.append(c);
                cLen++;
                if (cLen == len) {
                    builder = builder.append(" ");
                    cLen = 0;
                    cnLen++;
                    if (cnLen == nLen) {
                        builder = builder.append("\n");
                        cnLen = 0;
                    }
                }
            }
            System.out.println(builder.toString());
            System.out.println(result.length());
        } else {
            System.out.println("No solution.");
        }
    }

    int resultSize = 0;
    String result = null;

    private boolean addResult(String sequence, int n) {
        if (resultSize == n) {
            return false;
        }
        resultSize++;
        result = sequence;
        return true;
    }


    private boolean hasRepeatSubSequence(String sequence, char c) {
        if (sequence.isEmpty()) {
            return false;
        }
        int split = sequence.length() - 1;
        if (sequence.charAt(split) == c) {
            return true;
        }
        if (split > 1) {
            int len = 2;
            String co = String.valueOf(c);
            String left, right;
            while (split - len >= 0) {
                left = sequence.substring(split - len, split);
                right = sequence.substring(split).concat(co);
                if (left.equals(right)) {
                    return true;
                }
                split--;
                len++;
            }
            return false;
        } else {
            return false;
        }
    }
}
