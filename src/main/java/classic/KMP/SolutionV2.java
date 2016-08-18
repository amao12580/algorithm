package classic.KMP;

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
 * KMP 算法，预处理Next数组
 */
public class SolutionV2 {
    public static void main(String[] args) {
        SolutionV2 solution = new SolutionV2();
//        String S = "bbababaaaababbaabbbabbbaaabbbaaababbabaabbaaaaabbaaabbbbaaabaabbaababbbaabaaababbaaabbbbbbaabbbbbaaabbababaaaaabaabbbababbaababaabbaa";
//        String P = "bbabba";
        String P1 = "abab";


        String S = "BBC ABCDAB ABCDABCDABDE";
        String P = "ABCDABD";
        int index = solution.strStr(S, P);
        System.out.println("index:" + index);
    }

    public int strStr(String haystack, String needle) {
        int needleLen = needle.length();
        if ((haystack.isEmpty() && needle.isEmpty()) || (!haystack.isEmpty() && needle.isEmpty())) {
            return 0;
        }
        if (haystack.length() < needle.length()) {
            return -1;
        }
        int[] next = new int[needleLen];
        next[0] = -1;

        int k = -1;
        int j = 0;
        while (j < needleLen) {
            if (k == -1 || needle.charAt(j) == needle.charAt(k)) {
                ++j;
                ++k;
                if (j < needleLen) {
                    if (needle.charAt(j) != needle.charAt(k))
                        next[j] = k;
                    else
                        next[j] = next[k];
                }
            } else {
                k = next[k];
            }
        }
        int i = 0, h = 0;
        while (i < haystack.length()) {
            if (haystack.charAt(i) == needle.charAt(h)) {
                if (h == needleLen - 1) {
                    return i - h;
                } else {
                    i++;
                    h++;
                }
            } else {
                if (next[h] < 0) {
                    i++;
                    h = 0;
                } else {
                    h = next[h];
                }
            }
        }
        return -1;
    }
}
