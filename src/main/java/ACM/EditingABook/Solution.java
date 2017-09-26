package ACM.EditingABook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private int len;
    private int minStep = -1;
    private int threeMaxStep = -1;

    public static void main(String[] args) {
        new Solution().case1();
        new Solution().case2();
        new Solution().case3();
    }

    private void case1() {
        int[] array = {2, 4, 1, 5, 3, 6};
        CtrlXV(array);
    }

    private void case2() {
        int[] array = {3, 4, 5, 1, 2};
        CtrlXV(array);
    }

    private void case3() {
        int[] array = {5, 4, 3, 2, 1};
        CtrlXV(array);
    }

    private void CtrlXV(int[] array) {
        long s = System.currentTimeMillis();
        System.out.println(Arrays.toString(array));
        this.len = array.length;
        this.threeMaxStep = (len - 1) * 3;
        List<Integer> wrongNextIndex = wrongNextIndex(array);
        System.out.println("wrongNextIndex:" + wrongNextIndex.toString());
        if (!wrongNextIndex.isEmpty()) {
            CtrlXV(wrongNextIndex, array, 0);
        }
        System.out.println("time:" + (System.currentTimeMillis() - s));
        System.out.println("--------------------------------------------");
    }

    private void CtrlXV(List<Integer> wrongNextIndex, int[] array, int step) {
        if (wrongNextIndex.size() == 0) {
            if (minStep < 0 || step < minStep) {
                minStep = step;
            }
            return;
        }
        if (3 * step + wrongNextIndex.size() > threeMaxStep) {//A*
            return;
        }
        if (wrongNextIndex.size() == 1) {
            CtrlXV(0, wrongNextIndex.get(0), len, array);
            wrongNextIndex.clear();
            CtrlXV(wrongNextIndex, array, step + 1);
            return;
        }

    }

    /**
     * 将数组指定的开始结束部分，剪切到新位置
     *
     * @param copyBeginIndex   需要剪切的部分，开始位置
     * @param copyEndIndex     需要剪切的部分，结束位置
     * @param pasteInsertIndex 新的粘贴位置
     * @param array            数组
     */
    private void CtrlXV(int copyBeginIndex, int copyEndIndex, int pasteInsertIndex, int[] array) {
        int copyLen = copyEndIndex - copyBeginIndex + 1;
        int[] copyArray = new int[copyLen];
        System.arraycopy(array, copyBeginIndex, copyArray, copyBeginIndex - copyBeginIndex, copyEndIndex + 1 - copyBeginIndex);
        System.arraycopy(array, copyEndIndex + 1, copyArray, copyEndIndex + 1 - copyLen, pasteInsertIndex - (copyEndIndex + 1));
        System.arraycopy(copyArray, 0, array, copyBeginIndex + pasteInsertIndex - 1 - copyEndIndex, copyLen);
    }


    /**
     * 找到不符合升序的前一个元素值
     * <p>
     * 如果下标值为i的元素，比下标值为i+1的元素值要大，则记录
     * <p>
     * O(N) N为数组长度 线性复杂度
     */
    private List<Integer> wrongNextIndex(int[] array) {
        List<Integer> result = new ArrayList<>(len - 1);
        for (int i = 0; i < len - 1; i++) {
            if (array[i] > array[i + 1]) {
                result.add(i);
            }
        }
        return result;
    }
}
