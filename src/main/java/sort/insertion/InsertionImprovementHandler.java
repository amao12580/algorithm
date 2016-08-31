package sort.insertion;

import basic.Comparator;
import basic.sort.SortHandlerBehavior;
import basic.sort.Sortable;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/12
 * Time:16:52
 * <p>
 * 尝试对插入排序进行改进
 * <p>
 * <p>
 * 改进要点：每次在前面的有序数组段，寻找合适位置进行swap时，不需要遍历整个数组段
 * <p>
 * 使用二分查找进行时间复杂度降低：O(n)  -->   O(log n)
 * <p>
 * wiki定义：
 * 如果比较操作的代价比交换操作大的话，可以采用二分查找法来减少比较操作的数目。该算法可以认为是插入排序的一个变种，称为二分查找插入排序。
 */
public class InsertionImprovementHandler extends SortHandlerBehavior {

    /**
     * 思路：
     * 在第i次迭代中，将第i个元素与每一个它左边且比它大的的元素交换位置
     * <p>
     * <p>
     * 时间复杂度：O(n^2)
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
        for (int i = 1; i < len; i++) {
            int endIndex = i - 1;
            if (endIndex < 0) {
                continue;
            }
            int possibleIndex = binarySearch(originArray, 0, i - 1, originArray[i]);
            if (possibleIndex < i - 1) {
                swapAlways(originArray, i, possibleIndex);
                move(originArray, possibleIndex + 1, endIndex);
            }
            if (possibleIndex == i - 1) {
                swapAlways(originArray, i, i - 1);
            }
        }
        return originArray;
    }

    /**
     * 在给定数组的begin、end下标值范围内，将每一个元素想后移动1格
     */
    private void move(Comparable[] originArray, int beginIndex, int endIndex) {
        while (beginIndex <= endIndex) {
            swapAlways(originArray, endIndex, endIndex);
            endIndex--;
        }
    }

    /**
     * 在给定数组的begin、end下标值范围内，找到第一个比key大的元素下标值
     */
    private int binarySearch(Comparable[] originArray, int beginIndex, int endIndex, Comparable key) {
        if (beginIndex > endIndex) {
            return endIndex + 1;
        }
        if (Comparator.isLT(originArray[endIndex], key)) {
            return endIndex + 1;
        }
        int middleIndex = beginIndex + ((endIndex - beginIndex) / 2);
        if (Comparator.isGT(originArray[middleIndex], key)) {
            int previousIndex = middleIndex - 1;
            if (previousIndex < beginIndex) {
                return middleIndex;
            }
            //判断上一个值是否符合要求
            if (Comparator.isGT(originArray[previousIndex], key)) {
                return binarySearch(originArray, beginIndex, previousIndex, key);
            } else {
                return middleIndex;
            }
        } else {
            return binarySearch(originArray, middleIndex + 1, endIndex, key);
        }
    }

    public static void main(String[] args) throws Exception {
        Sortable sortable = new InsertionImprovementHandler();
        Integer f[] = {5, 0, 8, 58, 10, 2, 3, 7, 8, 9, 10, 12, 15, -5, 4, 152};
        System.out.println("---:" + Arrays.toString(f));
        System.out.println("---:" + Arrays.toString(sortable.sort(f)));
        benchmark(sortable);
    }
}
