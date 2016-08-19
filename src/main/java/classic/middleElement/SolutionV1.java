package classic.middleElement;

import basic.sort.Sortable;
import sort.quick.QuickHandler;

import java.util.Arrays;

/**
 * 快速从无序数组中找到中位数
 * <p>
 * 中位数的定义：中位数的值处于有序数组的中间位置
 * <p>
 * 思路：
 * <p>
 * 1.排序后取中间位置
 */
public class SolutionV1 {
    public static void main(String[] args) throws Exception {
        Comparable originArray[] = {113, 19, 0, 5, 12, 8, 7, 4, 11, 2, 6, 21, 9};
        Sortable sortable = new QuickHandler();
        originArray = sortable.sort(originArray);
        System.out.println("sorted:" + Arrays.toString(originArray));
        System.out.println("middle:" + originArray[originArray.length / 2].toString());
    }

}
