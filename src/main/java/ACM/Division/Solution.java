package ACM.Division;

import basic.Util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/7/21
 * Time:19:29
 * <p>
 * 输入正整数n，按从小到大的顺序输出所有形如abcde/fghij = n的表达式，其中a～j恰好
 * 为数字0～9的一个排列（可以有前导0），2≤n≤79。
 * <p>
 * Division, UVa 725
 * <p>
 * 样例输入：
 * 62
 * 样例输出：
 * 79546 / 01283 = 62
 * 94736 / 01528 = 62
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("---------------------------");
        solution.case2();
    }

    private void case1() {
        division(62);
    }


    private void case2() {
        division(Util.getRandomInteger(2, 79));
    }

    private Set<Byte> bits = new HashSet<>();

    private ConcurrentSkipListSet<Byte> getBits() {
        if (bits.isEmpty()) {
            for (byte i = 0; i < 10; i++) {
                bits.add(i);
            }
        }
        ConcurrentSkipListSet<Byte> result = new ConcurrentSkipListSet<>();
        result.addAll(bits);
        return result;
    }

    private void division(int n) {
        division(10, n);
    }

    private void division(int len, int n) {
        System.out.println(n);
        LinkedList<String> allNum = new LinkedList<>();
        division(len / 2, getBits(), "", allNum);
        String bottom;
        Integer top;
        while (!allNum.isEmpty()) {
            bottom = allNum.poll();
            top = Integer.valueOf(bottom) * n;
            if (allShowUp(bottom, top, len)) {
                System.out.println(top + " / " + bottom + " = " + n);
            }
        }
    }

    private boolean allShowUp(String bottom, Integer top, int len) {
        String str = bottom + top;
        if (str.length() != len) {
            return false;
        }
        Set<Byte> allBits = new HashSet<>();
        char[] chars = str.toCharArray();
        for (char c : chars) {
            allBits.add((byte) c);
        }
        return allBits.size() == len;
    }

    private void division(int len, ConcurrentSkipListSet<Byte> bits, String num, LinkedList<String> allNum) {
        if (len <= 0) {
            allNum.add(num);
            return;
        }
        ConcurrentSkipListSet<Byte> nextLoopBits;
        for (Byte item : bits) {
            nextLoopBits = new ConcurrentSkipListSet<>();
            nextLoopBits.addAll(bits);
            nextLoopBits.remove(item);
            division(len - 1, nextLoopBits, num + item, allNum);
        }
    }
}
