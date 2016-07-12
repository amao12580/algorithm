package DP.lis;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/7/5
 * Time:16:15
 * <p>
 * 一个序列有N个数：A[1],A[2],…,A[N]，求出最长非降子序列的长度。 (讲DP基本都会讲到的一个问题LIS：longest increasing subsequence)
 * <p>
 * tips:
 * 1.不要求结果序列中的元素在原始数组中是相邻的
 * 2.相等的元素，不算做相互递增
 * <p>
 * 借用二分查找来降低时间复杂度
 * <p>
 * N*log N
 */
public class SolutionV2 {
    public static void main(String[] args) {
        //int[] array1 = {3};
        //int[] array1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        //System.out.println(binarySearchLatestLessThan(array1, 0, array1.length - 1, 4));
        int[] array1 = {1, 3, 6, 7, 9, 4, 10, 5, 6};
        int[] array2 = {5, 3, 4, 8, 6, 7, 2, 9, 5, 12};
        int[] array3 = {1, 6, 2, 7, 3, 8, 4, 9};
        int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int len = lengthOfLIS2(array);
        System.out.println("len:" + len);
        int len1 = lengthOfLIS2(array1);
        System.out.println("len1:" + len1);
        int len2 = lengthOfLIS2(array2);
        System.out.println("len2:" + len2);
        int len3 = lengthOfLIS2(array3);
        System.out.println("len3:" + len3);
    }

    /**
     * O(n*log n)  解法
     * <p>
     * https://www.felix021.com/blog/read.php?1587
     */
    public static int lengthOfLIS2(int[] nums) {
        //System.out.println("A:" + Util.toJson(nums));
        if (nums.length == 0) {
            return 0;
        }
        int[] d = new int[nums.length];//记录nums数组中，对应长度LIS的最小末尾
        d[0] = nums[0];
        int latestIndex = 0;
        for (int current = 1; current < nums.length; current++) {
            int expectIndex = binarySearchLatestLessThan(d, 0, latestIndex, nums[current]) + 1;
            d[expectIndex] = nums[current];
            if (expectIndex > latestIndex) {
                latestIndex = expectIndex;
            }
        }
        //System.out.println("d:" + Util.toJson(d));
        return latestIndex + 1;
    }

    /**
     * 在给定数组的begin、end下标值范围内，找到最后一个比key小的元素下标值
     */
    private static int binarySearchLatestLessThan(int[] originArray, int beginIndex, int endIndex, int key) {
        if (beginIndex > endIndex) {
            return endIndex + 1;
        }
        if (originArray[endIndex] < key) {
            return endIndex;
        }
        int middleIndex = beginIndex + ((endIndex - beginIndex) / 2);
        if (originArray[middleIndex] < key) {
            int previousIndex = middleIndex + 1;
            if (previousIndex > endIndex) {
                return middleIndex;
            }
            //判断下一个值是否符合要求
            if (originArray[previousIndex] >= key) {
                return middleIndex;
            } else {
                return binarySearchLatestLessThan(originArray, previousIndex, endIndex, key);
            }
        } else {
            if (middleIndex == 0) {
                return -1;
            }
            return binarySearchLatestLessThan(originArray, beginIndex, middleIndex, key);
        }
    }
}
