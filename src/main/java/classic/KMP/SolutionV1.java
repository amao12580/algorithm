package classic.KMP;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/7/18
 * Time:10:01
 *
 *
 * 有一个文本串S，和一个模式串P，现在要查找P在S中的位置，怎么查找呢？

 * 如果用暴力匹配的思路，并假设现在文本串S匹配到 i 位置，模式串P匹配到 j 位置，则有：

 * 如果当前字符匹配成功（即S[i] == P[j]），则i++，j++，继续匹配下一个字符；
 * 如果失配（即S[i]! = P[j]），令i = i - (j - 1)，j = 0。相当于每次匹配失败时，i 回溯，j 被置为0。
 *
 *
 * https://leetcode.com/problems/implement-strstr/
 */
public class SolutionV1 {
    public int strStr(String haystack, String needle) {
        return 0;
    }
}
