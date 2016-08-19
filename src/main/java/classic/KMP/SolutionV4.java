package classic.KMP;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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
 * Sunday 算法，从前向后匹配
 * <p>
 * 假设文本串长度为N，模式串长度为M
 * 时间复杂度度（O(N+M)）
 * 空间复杂度（O(M)）
 * <p>
 * <p>
 * Sunday算法是从前往后匹配，在匹配失败时关注的是文本串中参加匹配的最末位字符的下一位字符。
 * 如果该字符没有在模式串中出现则直接跳过，即移动位数 = 匹配串长度 + 1；
 * 否则，其移动位数 = 模式串中最右端的该字符到末尾的距离+1。
 * <p>
 * <p>
 * 使用Sunday算法不需要固定地从左到右匹配或者从右到左的匹配(这是因为失配之后我们用的是目标串中后一个没有匹配过的字符)，
 * 我们可以对模式串中的字符出现的概率事先进行统计，每次都使用概率最小的字符所在的位置来进行比较，
 * 这样失配的概率会比较大，所以可以减少比较次数，加快匹配速度。
 */
public class SolutionV4 {
    public static void main(String[] args) {
        SolutionV4 solution = new SolutionV4();
//        String S = "bbababaaaababbaabbbabbbaaabbbaaababbabaabbaaaaabbaaabbbbaaabaabbaababbbaabaaababbaaabbbbbbaabbbbbaaabbababaaaaabaabbbababbaababaabbaa";
//        String P = "bbabba";
        String P1 = "abab";


//        String S = "BBC ABCDAB ABCDABCDABDE";
//        String P = "ABCDABD";

//        String S = "babbbbbabb";
//        String P = "bbab";

//        String S = "mississippi";
//        String P = "issipi";


        String S = "mississippi";
        String P = "sippia";

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
        Map<Character, LinkedList<Integer>> positioner = new HashMap<>();
        for (int k = 0; k < needleLen; k++) {
            char current = needle.charAt(k);
            if (positioner.containsKey(current)) {
                positioner.get(current).add(k);
            } else {
                LinkedList<Integer> positions = new LinkedList<>();
                positions.add(k);
                positioner.put(current, positions);
            }
        }
//        System.out.println("positioner:" + Util.toJson(positioner));
        int needleLast = needleLen - 1;
        int i = 0;
        int j = 0;
        int m = 0;
        while (m < haystackLen && i < haystackLen) {
//            System.out.println("i:" + i + ",j:" + j);
            char S = haystack.charAt(i);
            char P = needle.charAt(j);
            if (S == P) {
                if (j == needleLast) {
                    return i - j;
                }
                i++;
                j++;
            } else {
                int tail = m + needleLast + 1;
                if (tail > haystackLen - 1) {
                    return -1;
                }
                LinkedList<Integer> preperPositions = positioner.get(haystack.charAt(tail));
                if (preperPositions != null) {
                    m += needleLast - preperPositions.peekLast() + 1;
                } else {
                    m += needleLen + 1;
                }
                i = m;
                j = 0;
            }
        }
        return -1;
    }
}
