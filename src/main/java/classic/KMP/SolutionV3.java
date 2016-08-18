package classic.KMP;

import basic.Util;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/7/18
 * Time:10:01
 * <p>
 * <p>
 * 有一个文本串S，和一个模式串P，现在要查找P在S中的位置，怎么查找呢？
 * <p>
 * 如果用暴力匹配的思路，并假设现在文本串S匹配到 i 位置，模式串P匹配到 j 位置，则有：
 * <p>
 * 如果当前字符匹配成功（即S[i] == P[j]），则i++，j++，继续匹配下一个字符；
 * 如果失配（即S[i]! = P[j]），令i = i - (j - 1)，j = 0。相当于每次匹配失败时，i 回溯，j 被置为0。
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * https://leetcode.com/problems/implement-strstr/
 * <p>
 * <p>
 * http://blog.csdn.net/v_july_v/article/details/7041827
 * <p>
 * <p>
 * BM 算法，从后向前匹配
 * <p>
 * 假设文本串长度为N，模式串长度为M
 * 时间复杂度度（O(N+M)）
 * 空间复杂度（O(M)）
 * <p>
 * <p>
 * 坏字符规则：当文本串中的某个字符跟模式串的某个字符不匹配时，我们称文本串中的这个失配字符为坏字符，此时模式串需要向右移动，移动的位数 = 坏字符在模式串中的位置 - 坏字符在模式串中最右出现的位置。此外，如果"坏字符"不包含在模式串之中，则最右出现位置为-1。
 * <p>
 * 好后缀规则：当字符失配时，后移位数 = 好后缀在模式串中的位置 - 好后缀在模式串上一次出现的位置，且如果好后缀在模式串中没有再次出现，则为-1。
 */
public class SolutionV3 {
    public static void main(String[] args) {
        SolutionV3 solution = new SolutionV3();
//        String S = "bbababaaaababbaabbbabbbaaabbbaaababbabaabbaaaaabbaaabbbbaaabaabbaababbbaabaaababbaaabbbbbbaabbbbbaaabbababaaaaabaabbbababbaababaabbaa";
//        String P = "bbabba";
        String P1 = "abab";


//        String S = "BBC ABCDAB ABCDABCDABDE";
//        String P = "ABCDABD";

        String S = "babbbbbabb";
        String P = "bbab";

//        String S = "mississippi";
//        String P = "issipi";

//        String S = "HERE IS A SIMPLE EXAMPLE";
//        String P = "EXAMPLE";
        int index = solution.strStr(S, P);
        System.out.println("index:" + index);
    }

    public int strStr(String haystack, String needle) {
        int needleLen = needle.length();
        int haystackLen = haystack.length();
        if ((haystackLen == 0 && needleLen == 0) || (haystackLen != 0 && needleLen == 0)) {
            return 0;
        }
        if (haystackLen < needleLen) {
            return -1;
        }
        //记录模式串的每个字符出现的多处位置
        Map<Character, List<Integer>> positioner = new HashMap<>();
        for (int k = 0; k < needleLen; k++) {
            char current = needle.charAt(k);
            if (positioner.containsKey(current)) {
                positioner.get(current).add(k);
            } else {
                List<Integer> positions = new LinkedList<>();
                positions.add(k);
                positioner.put(current, positions);
            }
        }
        System.out.println("positioner:" + Util.toJson(positioner));
        int needleLast = needleLen - 1;
        int i = needleLast;
        int j = i;
        boolean islatest = false;
        Map<String, List<Integer>> goodSuffixs = new HashMap<>();
        while (j >= 0 && i >= 0 && i < haystackLen) {
            System.out.println("i:" + i + ",j:" + j);
            if (i == haystackLen - 1) {
                islatest = true;
            }
            char S = haystack.charAt(i);
            char P = needle.charAt(j);
            if (S == P) {
                if (j == 0) {
                    return i;
                }
                //记录到“好后缀”集合
                String goodSuffix = needle.substring(j);
                if (goodSuffixs.containsKey(P)) {
                    goodSuffixs.get(goodSuffix).add(j);
                } else {
                    List<Integer> positions = new ArrayList<>();
                    positions.add(j);
                    goodSuffixs.put(goodSuffix, positions);
                }
                i--;
                j--;
            } else {
                if (islatest) {
                    return -1;
                }
                if (!goodSuffixs.isEmpty()) {
                    goodSuffixs = new HashMap<>();
                }
                //计算模式串向后移动的步长

                //1.按照坏字符的规则来计算
                int position = -1;
                List<Integer> preperPositions = positioner.get(S);
                if (preperPositions != null) {
                    position = preperPositions.get(preperPositions.size() - 1);
                }
                int move = j - position;
                //2.按照好后缀的规则来计算
//                if (j != needleLast) {//第一次就失配了，不会有好后缀
//                    String suffix = needle.substring(j);
                //}
                i = i - j + move + needleLast;
                j = needleLast;
            }
        }
        return -1;
    }
}
