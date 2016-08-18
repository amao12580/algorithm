package sort.insertion;

import basic.sort.SortHandlerBehavior;
import basic.sort.Sortable;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/12
 * Time:16:52
 */
public class InsertionHandler extends SortHandlerBehavior {

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
        //int count=0;
        for (int i = 0; i < len; i++) {
            int endIndex = i - 1;
            if (endIndex <= 0) {
                continue;
            }
            for (int j = 0; j <= endIndex; j++) {
                swapIfLessThan(originArray, i, j);
                //System.out.println("***:" + Arrays.toString(originArray));
                //count++;
            }
            //System.out.println("###:"+Arrays.toString(originArray));
        }
        //System.out.println("count:"+count);
        return originArray;
    }

    public static void main(String[] args) throws Exception {
        Sortable sortable = new InsertionHandler();
        Integer f[] = {5, 0, 8, 58, 10, 2, 3, 7, 8, 9, 10, 12, 15, -5, 4, 152};
        System.out.println("---:" + Arrays.toString(sortable.sort(f)));
        benchmark(sortable);
    }

}
