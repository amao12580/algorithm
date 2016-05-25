package sort.heap;

import basic.Util;
import basic.sort.SortHandlerBehavior;
import basic.sort.Sortable;

import java.util.Arrays;

/**
 *
 * http://www.cnblogs.com/kkun/archive/2011/11/23/2260286.html
 *
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/12
 * Time:16:52
 */
public class HeapHandler extends SortHandlerBehavior {

    int heapSize = 0;

    /**
     * 思路:
     * 借用完全二叉树的思想
     *
     * 设定数组初始长度为N，将数组调整为最大堆，最大堆的根节点是最大值。
     * 根节点位于数组首部，O(0)
     * 将这个最大值与O(N-1)交换
     * 将数组的遍历范围减一，即:N--
     * 在0到N之间重复上述过程
     *
     *
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
        heapSize = len;
        while (heapSize > 0) {
//            System.out.println("^^^:" + Arrays.toString(originArray));
            rebuild(originArray, heapSize);
//            System.out.println("$$$:" + Arrays.toString(originArray));
            swapAlways(originArray, 0, originArray[0], heapSize - 1, originArray[heapSize - 1]);
//            System.out.println("***:" + Arrays.toString(originArray));
            heapSize--;
        }
        return originArray;
    }

    /**
     * 重建最大堆
     *
     * @param originArray 数组
     */
    private void rebuild(Comparable[] originArray, int endIndex) {
        double high = Util.log(endIndex, 2);
        int highlen = (int) high;
        if (high > highlen) {
            highlen++;//求出树的高度值
        }
        while (highlen >= 1) {
            int floorStartIndex = (int) Util.pow(2, highlen - 2);//每次从树的倒数第二层的第一个结点遍历
            if (floorStartIndex <= 0) {
                return;
            }
            int floorEndIndex = (int) Util.pow(2, highlen - 1) - 1;////从树的倒数第二层的最后一个结点结束遍历
            //System.out.println("floorStartIndex:" + floorStartIndex + ",floorEndIndex:" + floorEndIndex + ",highlen:" + highlen);
            for (int currentIndex = floorStartIndex; currentIndex <= floorEndIndex; currentIndex++) {
                Comparable leftChildNode = leftChildNode(originArray, currentIndex);
                //System.out.println("currentIndex:" + currentIndex + ",endIndex:" + endIndex + ",heapSize:" + heapSize);
                if (leftChildNode != null) {
                    int leftChildIndex = 2 * currentIndex - 1;
                    //System.out.println("leftChildIndex:" + leftChildIndex + ",leftChildNode:" + leftChildNode);
                    swapIfLessThan(originArray, currentIndex - 1, leftChildIndex);//在当前结点与该结点的左结点之间选出最大值，将大的值交换到当前结点
                }
//                System.out.println("///:");
//                printHeapTree(originArray);
                Comparable rightChildNode = rightChildNode(originArray, currentIndex);
                if (rightChildNode != null) {
                    int rightChildIndex = 2 * currentIndex;
                    //System.out.println("rightChildIndex:" + rightChildIndex + ",rightChildNode:" + rightChildNode);
                    swapIfLessThan(originArray, currentIndex - 1, rightChildIndex);//在当前结点与该结点的右结点之间选出最大值，将大的值交换到当前结点
                }
//                System.out.println("+++:");
//                printHeapTree(originArray);
            }
            highlen--;
        }
    }

    /**
     * 左子节点
     *
     * @param i 当前节点的位置
     * @return 左子节点
     */
    private Comparable leftChildNode(Comparable[] originArray, int i) {
        int expectIndex = 2 * i - 1;
        if (expectIndex > (heapSize - 1) || expectIndex < 0) {
            return null;
        }
        return originArray[expectIndex];
    }

    /**
     * 右子节点
     *
     * @param i 当前节点的位置
     * @return 右子节点
     */
    private Comparable rightChildNode(Comparable[] originArray, int i) {
        int expectIndex = 2 * i;
        if (expectIndex > (heapSize - 1) || expectIndex < 0) {
            return null;
        }
        return originArray[expectIndex];
    }

    private void printHeapTree(Comparable[] array) {
        for (int i = 1; i < heapSize; i = i * 2) {
            for (int k = i - 1; k < 2 * (i) - 1 && k < heapSize; k++) {
                System.out.print(array[k] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws Exception {
        Sortable sortable = new HeapHandler();
        Integer f[] = {13, 19, 9, 5, 12, 8, 7, 4, 11, 2, 6, 21};
        Integer f2[] = {59, -10, 0, 6, -1, 0, 8, 58, 10, 2, 3, 7, 8, 9, 10, 12, 15, -5, 4};
        System.out.println("---:" + Arrays.toString(sortable.sort(f)));
        //System.out.println("---:" + Arrays.toString(sortable.sort(f2)));
        benchmark(sortable);
    }

}
