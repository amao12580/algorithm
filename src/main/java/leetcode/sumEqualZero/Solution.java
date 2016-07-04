package leetcode.sumEqualZero;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/6/30
 * Time:17:41
 */
public class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> res = new LinkedList<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2; ++i) {
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            int l = i + 1, r = nums.length - 1;
            while (l < r) {
                if (nums[i] + nums[l] + nums[r] == 0) {
                    res.add(Arrays.asList(nums[i], nums[l], nums[r]));
                    while (++l < r && nums[l] == nums[l - 1]) ; // skip the same b
                    while (--r > l && nums[r] == nums[r + 1]) ; // skip the same c
                } else if (nums[i] + nums[l] + nums[r] > 0) {
                    while (--r > l && nums[r] == nums[r + 1]) ;
                } else {
                    while (++l < r && nums[l] == nums[l - 1]) ;
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[] originArray = {-7, -11, 12, -15, 14, 4, 4, 11, -11, 2, -8, 5, 8, 14, 0, 3, 2, 3, -3, -15, -2, 3, 6, 1, 2, 8, -5, -7, 3, 1, 8, 11, -3, 6, 3, -4, -13, -15, 14, -8, 2, -8, 4, -13, 13, 11, 5, 0, 0, 9, -8, 5, -2, 14, -9, -15, -1, -6, -15, 9, 10, 9, -2, -8, -8, -14, -5, -14, -14, -6, -15, -5, -7, 5, -11, 14, -7, 2, -9, 0, -4, -1, -9, 9, -10, -11, 1, -4, -2, 2, -9, -15, -12, -4, -8, -5, -11, -6, -4, -9, -4, -3, -7, 4, 9, -2, -5, -13, 7, 2, -5, -12, -14, 1, 13, -9, -3, -9, 2, 3, 8, 0, 3};
        //int[] originArray = {-1,0,1,2,-1,-4};
        Solution solution = new Solution();
        long s = System.currentTimeMillis();
        solution.threeSum(originArray);
        long e = System.currentTimeMillis();
        System.out.println(e - s);
        //System.out.println(solution.threeSum(originArray));
    }
}
