package DP.lis;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/7/5
 * Time:16:15
 * <p>
 * һ��������N������A[1],A[2],��,A[N]�������ǽ������еĳ��ȡ� (��DP�������ὲ����һ������LIS��longest increasing subsequence)
 * <p>
 * tips:
 * 1.��Ҫ���������е�Ԫ����ԭʼ�����������ڵ�
 * 2.��ȵ�Ԫ�أ��������໥����
 */
public class Solution {
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
        len = lengthOfLIS(array);
        System.out.println("len:" + len);
        int len1 = lengthOfLIS2(array1);
        System.out.println("len1:" + len1);
        len1 = lengthOfLIS(array1);
        System.out.println("len1:" + len1);
        int len2 = lengthOfLIS2(array2);
        System.out.println("len2:" + len2);
        len2 = lengthOfLIS(array2);
        System.out.println("len2:" + len2);
        int len3 = lengthOfLIS2(array3);
        System.out.println("len3:" + len3);
        len3 = lengthOfLIS(array3);
        System.out.println("len3:" + len3);
    }

    /**
     * O(n^2)  �ⷨ
     * <p>
     * http://www.hawstein.com/posts/dp-novice-to-advanced.html
     */
    public static int lengthOfLIS(int[] nums) {
        //System.out.println("A:" + Util.toJson(nums));
        if (nums.length == 0) {
            return 0;
        }
        int[] d = new int[nums.length];//��¼nums�����У�ÿ����Ԫ��ֵ��β�ĵ�����������󳤶�ֵ
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

    /**
     * O(n*log n)  �ⷨ
     * <p>
     * https://www.felix021.com/blog/read.php?1587
     */
    public static int lengthOfLIS2(int[] nums) {
        //System.out.println("A:" + Util.toJson(nums));
        if (nums.length == 0) {
            return 0;
        }
        int[] d = new int[nums.length];//��¼nums�����У���Ӧ����LIS����Сĩβ
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
     * �ڸ��������begin��end�±�ֵ��Χ�ڣ��ҵ����һ����keyС��Ԫ���±�ֵ
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
            //�ж���һ��ֵ�Ƿ����Ҫ��
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
