package ACM.EditingABook;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/9/20
 * Time:18:49
 * <p>
 * 你有一篇由n（2≤n≤9）个自然段组成的文章，希望将它们排列成1, 2,…, n。可以用
 * Ctrl+X（剪切）和Ctrl+V（粘贴）快捷键来完成任务。每次可以剪切一段连续的自然段，粘
 * 贴时按照顺序粘贴。注意，剪贴板只有一个，所以不能连续剪切两次，只能剪切和粘贴交
 * 替。
 * 例如，为了将{2,4,1,5,3,6}变为升序，可以剪切1将其放到2前，然后剪切3将其放到4
 * 前。再如，对于排列{3,4,5,1,2}，只需一次剪切和一次粘贴即可——将{3,4,5}放在{1,2}后，
 * 或者将{1,2}放在{3,4,5}前。
 * <p>
 * 求最少的复制粘贴次数.
 * <p>
 * Editing a Book, UVa 11212
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2153
 * <p>
 * Sample Input
 * 6
 * 2 4 1 5 3 6
 * 5
 * 3 4 5 1 2
 * 0
 * Sample Output
 * Case 1: 2
 * Case 2: 1
 */
public class Solution {
    public static void main(String[] args) {
        new Solution().case1();
        new Solution().case2();
    }

    private void case1() {
        int[] array = {2, 4, 1, 5, 3, 6};
        CtrlXV(array);
    }

    private void case2() {
        int[] array = {3, 4, 5, 1, 2};
        CtrlXV(array);
    }

    private void CtrlXV(int[] array) {
        long s = System.currentTimeMillis();
        System.out.println(Arrays.toString(array));

        System.out.println("time:" + (System.currentTimeMillis() - s));
        System.out.println("--------------------------------------------");
    }
}
