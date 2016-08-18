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
 *
 *
 * Brute Force 算法，暴力匹配
 *
 * * 假设文本串长度为N，模式串长度为M
 * 时间复杂度度（O(N*M)）
 * 空间复杂度（O(1)）
 */
public class SolutionV1 {
    public static void main(String[] args) {
        SolutionV1 solution = new SolutionV1();
        String S = "BBC ABCDAB ABCDABCDABDE";
        String P = "ABCDABD";
        int index = solution.strStr(S, P);
//        int index = solution.strStr("mississippi", "a");
        System.out.println("index:" + index);
    }

    public int strStr(String haystack, String needle) {
        if ((haystack.isEmpty() && needle.isEmpty()) || (!haystack.isEmpty() && needle.isEmpty())) {
            return 0;
        }
        if (haystack.length() < needle.length()) {
            return -1;
        }
        int i = 0, j = 0;
        while (i < haystack.length() && j < needle.length()) {
            if (haystack.charAt(i) == needle.charAt(j)) {
                if (j == needle.length() - 1) {
                    return i - j;
                } else {
                    i++;
                    j++;
                }
            } else {
                i = i - j + 1;
                j = 0;
            }
        }
        return -1;
    }
}
