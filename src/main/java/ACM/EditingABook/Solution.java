package ACM.EditingABook;

import basic.Util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

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
    private Set<Move> passable = new HashSet<>();

    public static void main(String[] args) {
        new Solution().case1();
        new Solution().case2();
        new Solution().case3();
        new Solution().case4();
    }

    private void case1() {
        int[] array = {3, 4, 5, 1, 2};
        CtrlXV(array);
    }

    private void case2() {
        int[] array = {2, 4, 1, 5, 3, 6};
        CtrlXV(array);
    }

    private void case3() {
        int[] array = {5, 4, 3, 2, 1};
        CtrlXV(array);
    }

    private void case4() {
        int[] array = {5, 4, 2, 3, 1, 0};
        CtrlXV(array);

    }

    private void passable() {
        int copyLen = 1;
        int copyBeginIndex, copyEndIndex, pasteInsertIndex;
        while (copyLen < len) {
            for (int i = 0; i < len; i++) {
                copyBeginIndex = i;
                copyEndIndex = i + copyLen - 1;
                if (copyEndIndex >= len - 1) {
                    continue;
                }
                for (int j = copyEndIndex + 1; j <= len; j++) {
                    pasteInsertIndex = j;
                    passable.add(new Move(copyBeginIndex, copyEndIndex, pasteInsertIndex));
                }
            }
            copyLen++;
        }
    }

    private void CtrlXV(int[] array) {
        long s = System.currentTimeMillis();
        System.out.println(Arrays.toString(array));
        this.len = array.length;
        this.threeMaxStep = (len - 1) * 3;
        passable();
        Set<Integer> visited = new HashSet<>();
        visited.add(Arrays.toString(array).hashCode());
        CtrlXV(array, 0, visited);
        System.out.println(minStep);
        System.out.println("time:" + (System.currentTimeMillis() - s));
        System.out.println("--------------------------------------------");
    }

    private void CtrlXV(int[] array, int step, Set<Integer> visited) {
        int num = wrongSum(array);
        if (num == 0) {
            if (minStep < 0 || step < minStep) {
                minStep = step;
            }
            return;
        }
        if (3 * step + num > threeMaxStep) {//A*
            return;
        }
        int[] currentArray;
        Set<Integer> currentVisited;
        for (Move maybe : passable) {
            currentArray = Arrays.copyOf(array, len);
            currentVisited = new HashSet<>(visited);
            CtrlXV(maybe, currentArray);
            if (currentVisited.add(Arrays.toString(currentArray).hashCode())) {//回路
                CtrlXV(currentArray, step + 1, currentVisited);//DFS
            }
        }
    }

    private void CtrlXV(Move maybe, int[] array) {
        CtrlXV(maybe.getCopyBeginIndex(), maybe.getCopyEndIndex(), maybe.getPasteInsertIndex(), array);
    }

    /**
     * 将数组指定的开始结束部分，剪切到新位置  O(N) N为数组长度 线性复杂度
     *
     * @param copyBeginIndex   需要剪切的部分，开始位置
     * @param copyEndIndex     需要剪切的部分，结束位置
     * @param pasteInsertIndex 新的粘贴位置
     * @param array            数组
     */
    private void CtrlXV(int copyBeginIndex, int copyEndIndex, int pasteInsertIndex, int[] array) {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < len; i++) {
            list.add(array[i]);
        }
        int copyLen = copyEndIndex - copyBeginIndex + 1;
        int[] copyArray = new int[copyLen];
        System.arraycopy(array, copyBeginIndex, copyArray, 0, copyEndIndex + 1 - copyBeginIndex);
        for (int i = 0; i < copyLen; i++) {
            list.remove(copyBeginIndex);
        }
        pasteInsertIndex -= copyLen - 1;
        for (int i = 0; i < copyLen; i++) {
            if (pasteInsertIndex > list.size() - 1) {
                list.add(copyArray[i]);
            } else {
                list.add(pasteInsertIndex, copyArray[i]);
            }
            pasteInsertIndex++;
        }
        int index = 0;
        while (!list.isEmpty()) {
            array[index++] = list.poll();
        }
    }

    /**
     * 找到不符合升序的前一个元素值
     * <p>
     * 如果下标值为i的元素，比下标值为i+1的元素值，不是刚好小1，则记录
     * <p>
     * O(N) N为数组长度 线性复杂度
     */
    private int wrongSum(int[] array) {
        int num = 0;
        for (int i = 0; i < len - 1; i++) {
            if (array[i] != array[i + 1] - 1) {
                num++;
            }
        }
        return num;
    }

    class Move {
        int hashCode;
        int copyBeginIndex = -1;
        int copyEndIndex = -1;
        int pasteInsertIndex = -1;

        Move(int copyBeginIndex, int copyEndIndex, int pasteInsertIndex) {
            this.copyBeginIndex = copyBeginIndex;
            this.copyEndIndex = copyEndIndex;
            this.pasteInsertIndex = pasteInsertIndex;
            hashCode = buildHashCode();
        }

        int getCopyBeginIndex() {
            return copyBeginIndex;
        }

        int getCopyEndIndex() {
            return copyEndIndex;
        }

        int getPasteInsertIndex() {
            return pasteInsertIndex;
        }

        private int buildHashCode() {
            return Util.contactAll(',', copyBeginIndex, copyEndIndex, pasteInsertIndex).hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o instanceof Move) {
                Move other = (Move) o;
                return this.hashCode() == other.hashCode();
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        @Override
        public String toString() {
            return "copyBeginIndex:" + copyBeginIndex + ",copyEndIndex:" + copyEndIndex + ",pasteInsertIndex:" + pasteInsertIndex;
        }
    }
}