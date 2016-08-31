package sort.quick;

import basic.Comparator;
import basic.sort.SortHandlerBehavior;
import basic.sort.Sortable;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/12
 * Time:16:52
 */
public class QuickHandler extends SortHandlerBehavior {

    /**
     * 思路:
     * <p>
     * 快速排序时基于分治模式处理的,
     * 对一个典型子数组A[p...r]排序的分治过程为三个步骤:
     * 1.分解:
     * A[p..r]被划分为俩个（可能空）的子数组A[p ..q-1]和A[q+1 ..r],使得
     * A[p ..q-1] <= A[q] <= A[q+1 ..r]
     * 2.解决:通过递归调用快速排序,对子数组A[p ..q-1]和A[q+1 ..r]排序。
     * 3.合并。
     * <p>
     * <p>
     * 时间复杂度:O(n*log(n))
     *
     * @param originArray 原始输入数组
     * @return 排好序的数组
     * @throws Exception 在输入参数不可解析时抛出
     */
    @Override
    public Comparable[] sort(Comparable[] originArray) throws Exception {
        init(originArray);
        int len = originArray.length;
        if (len == 1) {
            return originArray;
        }
        if (len == 2) {
            swapIfLessThan(originArray, 1, 0);
            return originArray;
        }
        sort(originArray, 0, originArray.length - 1);
        return originArray;
    }

    public void sort(Comparable[] originArray, final int beginIndex, final int endIndex) throws Exception {
        if ((endIndex - beginIndex) <= 0) {
            return;
        }

        int N = partition(originArray, beginIndex, endIndex);
        //System.out.println("N:" + N);
        if ((N - 1) >= beginIndex) {
            //System.out.println("N2:" + (N-1));
            sort(originArray, beginIndex, N - 1);
        }
        if ((N + 1) <= endIndex) {
            //System.out.println("N3:" + (N+1));
            sort(originArray, N + 1, endIndex);
        }
    }

    /**
     * 对数组元素进行移动，切分数组为两部分，使左半部分<=右半部分。
     * <p>
     * 左右两部分的分隔元素，下标值记为：N
     *
     * @param originArray 原始数组
     * @param beginIndex  切分数组的起始元素下标
     * @param endIndex    切分数组的结束元素下标
     * @return N
     * @throws Exception
     */

    public int partition(Comparable[] originArray, final int beginIndex, final int endIndex) throws Exception {
        //System.out.println("begin:"+beginIndex+",end:"+endIndex);
        int basicIndex = beginIndex;
        int leftIndex = basicIndex + 1;
        int rightIndex = endIndex;
        while (rightIndex >= leftIndex) {
            while (leftIndex <= endIndex && rightIndex >= leftIndex) {
                if (Comparator.isLT(originArray[leftIndex], originArray[basicIndex])) {
                    swapAlways(originArray, leftIndex, originArray[leftIndex], basicIndex, originArray[basicIndex]);
                    basicIndex = leftIndex;
                    leftIndex = basicIndex + 1;
                } else {
                    break;
                }
            }
            while (beginIndex <= rightIndex && rightIndex >= leftIndex) {
                if (Comparator.isLT(originArray[rightIndex], originArray[basicIndex])) {
                    swapAlways(originArray, rightIndex, originArray[rightIndex], leftIndex, originArray[leftIndex]);
                    break;
                } else {
                    rightIndex--;
                }
            }
        }
        //System.out.println("---:"+ Arrays.toString(originArray));
        return basicIndex;
    }


    public static void main(String[] args) throws Exception {
        Sortable sortable = new QuickHandler();
        Integer f[] = {13, 19, 9, 5, 12, 8, 7, 4, 11, 2, 6, 21};
        Integer f2[] = {59, -10, 0, 6, -1, 0, 8, 58, 10, 2, 3, 7, 8, 9, 10, 12, 15, -5, 4};
        System.out.println("---:" + Arrays.toString(sortable.sort(f)));
        System.out.println("---:" + Arrays.toString(sortable.sort(f2)));
        //benchmark(sortable);
    }
}
