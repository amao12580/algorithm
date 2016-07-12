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
 * 借用DP思想
 * <p>
 * 时间复杂度：N^2
 */
public class SolutionV1 {
    public static void main(String[] args) {
        //int[] array1 = {3};
        //int[] array1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        //System.out.println(binarySearchLatestLessThan(array1, 0, array1.length - 1, 4));
        int[] array1 = {1, 3, 6, 7, 9, 4, 10, 5, 6};
        int[] array2 = {5, 3, 4, 8, 6, 7, 2, 9, 5, 12};
        int[] array3 = {1, 6, 2, 7, 3, 8, 4, 9};
        int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int len = lengthOfLIS(array);
        System.out.println("len:" + len);
        int len1 = lengthOfLIS(array1);
        System.out.println("len1:" + len1);
        int len2 = lengthOfLIS(array2);
        System.out.println("len2:" + len2);
        int len3 = lengthOfLIS(array3);
        System.out.println("len3:" + len3);
    }

    /**
     * O(n^2)  解法
     * <p>
     * http://www.hawstein.com/posts/dp-novice-to-advanced.html
     */
    public static int lengthOfLIS(int[] nums) {
        //System.out.println("A:" + Util.toJson(nums));
        if (nums.length == 0) {
            return 0;
        }
        int[] d = new int[nums.length];//记录nums数组中，每个以元素值结尾的递增子序列最大长度值
        int maxLen = 1;
        for (int current = 0; current < nums.length; current++) {
            d[current] = 1;
            for (int previous = current - 1; previous >= 0; previous--) {
                if (nums[previous] < nums[current] && d[previous] + 1 > d[current]) {
                    d[current] = d[previous] + 1;
                    if (d[current] > maxLen) {
                        maxLen = d[current];
                    }
                }
            }
        }
        //System.out.println("d:" + Util.toJson(d));
        return maxLen;
    }
}
