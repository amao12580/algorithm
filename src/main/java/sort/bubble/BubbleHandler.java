package sort.bubble;

import basic.sort.SortHandlerBehavior;
import basic.sort.Sortable;

/**
 * http://blog.csdn.net/morewindows/article/details/6657829
 * <p>
 * <p>
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/12
 * Time:16:52
 */
public class BubbleHandler extends SortHandlerBehavior {

    /**
     * 设数组长度为N。
     * 1．比较相邻的前后二个数据，如果前面数据大于后面的数据，就将二个数据交换。
     * 2．这样对数组的第0个数据到N-1个数据进行一次遍历后，最大的一个数据就“沉”到数组第N-1个位置。
     * 3．N=N-1，如果N不为0就重复前面二步，否则排序完成。
     *
     * O(n^2)
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
        int length = originArray.length;
        while (length > 0) {
            for (int i = 0; i < length; i++) {
                int nextIndex = i + 1;
                if (nextIndex >= length) {
                    break;
                }
                swapIfLessThan(originArray, nextIndex, i);
            }
            length--;
        }
        //System.out.println(Arrays.toString(originArray));
        return originArray;
    }

    public static void main(String[] args) throws Exception {
        Sortable sortable = new BubbleHandler();
        Integer f[] = {113, 19, 0, 5, 12, 8, 7, 4, 11, 2, 6, 21};
        //System.out.println("---:" + Arrays.toString(sortable.sort(Util.getRandomIntegerNumberArray(100))));
        //System.out.println("---:" + Arrays.toString(sortable.sort(f)));
        benchmark(sortable, 0, 10000);
    }
}
