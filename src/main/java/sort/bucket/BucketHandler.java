package sort.bucket;

import basic.Util;
import basic.sort.SortHandlerBehavior;
import basic.sort.Sortable;
import sort.count.CountHandler;

import java.util.Arrays;

/**
 * https://segmentfault.com/a/1190000003054515
 * <p>
 * <p>
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/12
 * Time:16:52
 */
public class BucketHandler extends SortHandlerBehavior {

    //常量定义：每个桶的最小size
    private static final int MIN_BUCKET_SIZE = 64;

    /**
     * 桶排序是计数排序的变种，把计数排序中相邻的m个"小桶"放到一个"大桶"中，在分完桶后，对每个桶进行排序（一般用快排），然后合并成最后的结果。
     * <p>
     * 基本思想：
     * <p>
     * 桶排序假设序列由一个随机过程产生，该过程将元素均匀而独立地分布在区间[0,1)上。我们把区间[0,1)划分成n个相同大小的子区间，称为桶。将n个记录分布到各个桶中去。
     * 如果有多于一个记录分到同一个桶中，需要进行桶内排序。最后依次把各个桶中的记录列出来记得到有序序列。
     * <p>
     * <p>
     * 效率分析：
     * <p>
     * 桶排序的平均时间复杂度为线性的O(N+C)，其中C为桶内快排的时间复杂度。如果相对于同样的N，桶数量M越大，其效率越高，最好的时间复杂度达到O(N)。
     * 当然桶排序的空间复杂度 为O(N+M)，如果输入数据非常庞大，而桶的数量也非常多，则空间代价无疑是昂贵的。此外，桶排序是稳定的。
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
        int length = maxValue - minValue;
        if (length == 0) {
            return originArray;
        }

        //计算桶的个数
        int bucketCount = length / MIN_BUCKET_SIZE;
        if (bucketCount < (length * MIN_BUCKET_SIZE)) {
            bucketCount++;
        }
        //System.out.println("bucketCount：" + bucketCount);
        int[][] bucketArray = new int[bucketCount][MIN_BUCKET_SIZE];

        //辅助性数组，用来保存每个桶内最后元素的下标值
        int[] buckeCountArray = new int[bucketCount];
        //填充桶
        for (Comparable anOriginArray : originArray) {
            int value = (Integer) anOriginArray;
            int bucketIndex = getBucketIndex(value, minValue, bucketCount);
            int subBucketIndex = buckeCountArray[bucketIndex];
            if (subBucketIndex > MIN_BUCKET_SIZE - 1) {
                resize(bucketArray, bucketIndex);//预分配的小桶容量不够啦，做一下resize：容量+=MIN_BUCKET_SIZE
            }
            bucketArray[bucketIndex][buckeCountArray[bucketIndex]] = value;
            buckeCountArray[bucketIndex] += 1;
        }
        //对小桶的数组进行排序

        Sortable sortable = new CountHandler();
        for (int i = 0; i < bucketArray.length; i++) {
            int count = buckeCountArray[i];
            if (count - 1 > 0) {
                bucketArray[i] = freeArray(sortable.sort(getPartArray(bucketArray[i], count)));
            }
        }
        //可以构建结果集了
        int index = 0;
        for (int i = 0; i < bucketArray.length; i++) {
            for (int j = 0; j < buckeCountArray[i]; j++) {
                originArray[index] = bucketArray[i][j];
                index++;
            }
        }
        return originArray;
    }

    private void resize(int[][] bucketArray, int bucketIndex) {
        int[] originBucketArrays = bucketArray[bucketIndex];
        int[] newBucketArrays = new int[originBucketArrays.length + MIN_BUCKET_SIZE];
        for (int i = 0; i < originBucketArrays.length; i++) {
            int value = originBucketArrays[i];
            if (value != 0) {
                newBucketArrays[i] = value;
            }
        }
        bucketArray[bucketIndex] = newBucketArrays;
    }

    private int[] freeArray(Comparable[] array) {
        int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = Integer.valueOf(array[i].toString());
        }
        return result;
    }

    private Comparable[] getPartArray(int[] array, int endIndex) {
        Comparable[] result = new Comparable[endIndex];
        for (int i = 0; i < endIndex; i++) {
            result[i] = array[i];
        }
        return result;
    }

    private int getBucketIndex(int value, int minValue, int bucketCount) throws Exception {
        int index = 0;
        while (index < bucketCount) {
            int st = index * MIN_BUCKET_SIZE + minValue;
            if (st <= value && value < (st + MIN_BUCKET_SIZE)) {
                return index;
            } else {
                index++;
            }
        }
        throw new Exception("元素值：" + value + "无法分配到桶");
    }

    public static void main(String[] args) throws Exception {
        Sortable sortable = new BucketHandler();
        Integer f[] = {113, 19, 0, 5, 12, 8, 7, 4, 11, 2, 6, 21};
        System.out.println("---:" + Arrays.toString(sortable.sort(Util.getRandomIntegerNumberArray(1000000, 0, 100))));
        //System.out.println("---:" + Arrays.toString(sortable.sort(f)));
        //benchmark(sortable, 0, 10000);
    }
}
