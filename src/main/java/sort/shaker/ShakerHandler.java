package sort.shaker;

import basic.sort.SortHandlerBehavior;
import basic.sort.Sortable;

/**
 * http://bubkoo.com/2014/01/15/sort-algorithm/shaker-sort/
 * <p>
 * <p>
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/12
 * Time:16:52
 */
public class ShakerHandler extends SortHandlerBehavior {

    /**
     * 鸡尾酒排序是冒泡排序的轻微变形。不同的地方在于，鸡尾酒排序是从低到高然后从高到低来回排序，而冒泡排序则仅从低到高去比较序列里的每个元素。
     * 他可比冒泡排序的效率稍微好一点，原因是冒泡排序只从一个方向进行比对(由低到高)，每次循环只移动一个项目。
     * <p>
     * <p>
     * 排序过程：
     * 先对数组从左到右进行冒泡排序（升序），则最大的元素去到最右端
     * 再对数组从右到左进行冒泡排序（降序），则最小的元素去到最左端
     * 以此类推，依次改变冒泡的方向，并不断缩小未排序元素的范围，直到最后一个元素结束
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
        int endIndex = originArray.length - 1;
        int beginIndex = 0;
        while (beginIndex <= endIndex) {
            endIndex = ascending(originArray, beginIndex, endIndex);
            beginIndex = descending(originArray, beginIndex, endIndex);
        }
        return originArray;
    }

    private int ascending(Comparable[] originArray, int beginIndex, int endIndex) {
        for (int i = beginIndex; i <= endIndex; i++) {
            int nextIndex = i + 1;
            if (nextIndex > endIndex) {
                break;
            }
            swapIfLessThan(originArray, nextIndex, i);
        }
        endIndex--;
        return endIndex;
    }

    private int descending(Comparable[] originArray, int beginIndex, int endIndex) {
        for (int i = endIndex; i >= beginIndex; i--) {
            int previousIndex = i - 1;
            if (previousIndex < beginIndex) {
                break;
            }
            swapIfLessThan(originArray, i, previousIndex);
        }
        beginIndex++;
        return beginIndex;
    }

    public static void main(String[] args) throws Exception {
        Sortable sortable = new ShakerHandler();
        Integer f[] = {113, 19, 0, 5, 12, 8, 7, 4, 11, 2, 6, 21};
        //System.out.println("---:" + Arrays.toString(sortable.sort(Util.getRandomIntegerNumberArray(1000000, 0, 100))));
        //System.out.println("---:" + Arrays.toString(sortable.sort(f)));
        benchmark(sortable, 0, 10000);
    }
}
