package sort.count;

import basic.sort.SortHandlerBehavior;
import basic.sort.Sortable;

/**
 * https://segmentfault.com/a/1190000003054515
 * <p>
 * <p>
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/12
 * Time:16:52
 */
public class CountHandler extends SortHandlerBehavior {

    /**
     * 思路:
     * 一种特殊的桶排序，按照每个待排序的数值进行分桶
     * <p>
     * 找出待排序的数组中最大和最小的元素
     * 统计数组中每个值为i的元素出现的次数，存入数组C的第i项
     * 对所有的计数累加（从C中的第一个元素开始，每一项和前一项相加）
     * 反向填充目标数组：将每个元素i放在新数组的第C(i)项，每放一个元素就将C(i)减去1
     * <p>
     * <p>
     * k是最大值与最小值的差值，在均匀分布的序列中，k与n没有明显的差别。
     * <p>
     * 时间复杂度:O(n+k)
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
        Comparable element = originArray[0];
        if (element instanceof Integer) {
        } else {
            throw new Exception("不受支持的排序类型。" + element.getClass());
        }
        //求出数组中的最大值、最小值
        Comparable[] maxAndMin = maxAndMin(originArray);
        int maxValue = (Integer) maxAndMin[0];
        int minValue = (Integer) maxAndMin[1];
        if (maxValue == minValue) {
            return originArray;
        }
        //构建计数数组
        int bucket = maxValue - minValue + 1;
        //System.out.println("bucket："+bucket);
        int[] countArray = new int[bucket];
        //填充计数数组
        for (int i = 0; i < originArray.length; i++) {
            countArray[(Integer) originArray[i] - minValue] += 1;
        }
        //计数数组进行顺序关联
        for (int i = 1; i < countArray.length; i++) {
            countArray[i] += countArray[i - 1];
        }
        //构建结果数组
        int[] result = new int[originArray.length];
        for (int i = 0; i < originArray.length; i++) {
            int value = (Integer) originArray[i];
            int index = countArray[value - minValue] - 1;
            result[index] = value;
            countArray[value - minValue] -= 1;
        }
        return freeArray(result);
    }

    private Comparable[] freeArray(int[] sortedArray) {
        Comparable[] result = new Comparable[sortedArray.length];
        for (int i = 0; i < sortedArray.length; i++) {
            result[i] = sortedArray[i];
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        Sortable sortable = new CountHandler();
        Integer f[] = {113, 19, 0, 5, 12, 8, 7, 4, 11, 2, 6, 21};
        //System.out.println("---:" + Arrays.toString(sortable.sort(Util.getRandomIntegerNumberArray(100))));
        //System.out.println("---:" + Arrays.toString(sortable.sort(f)));

        benchmark(sortable, 0, 100000000);//不适合对无规律的数据排序，在随机整数序列中，可能最大值与最小值的差超过一个亿（意味着bucke的长度过大），从而耗尽内存
    }
}
