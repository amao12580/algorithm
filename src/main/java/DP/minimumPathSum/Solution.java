package DP.minimumPathSum;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/7/12
 * Time:16:59
 * <p>
 *
 * https://leetcode.com/problems/minimum-path-sum/
 *
 * Given a m x n grid filled with non-negative numbers, find a path from top left to bottom right which minimizes the sum of all numbers along its path.
 * Note: You can only move either down or right at any point in time.
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[][] matrix = new int[5][4];
        init(matrix);
        print(matrix);
        System.out.println(solution.minPathSum(matrix));
    }

    private static void print(int[][] matrix) {
        System.out.print("   ");
        for (int j = 0; j < matrix[0].length; j++) {
            System.out.print(j + "  ");
        }
        System.out.println();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (j == 0) {
                    System.out.print(i + "  ");
                }
                System.out.print(matrix[i][j] + "  ");
            }
            System.out.println();
        }
    }

    private static void init(int[][] matrix) {
        matrix[0][0] = 0;
        matrix[0][1] = 2;
        matrix[0][2] = 3;
        matrix[0][3] = 4;

        matrix[1][0] = 2;
        matrix[1][1] = 8;
        matrix[1][2] = 5;
        matrix[1][3] = 3;

        matrix[2][0] = 3;
        matrix[2][1] = 7;
        matrix[2][2] = 1;
        matrix[2][3] = 2;

        matrix[3][0] = 4;
        matrix[3][1] = 3;
        matrix[3][2] = 2;
        matrix[3][3] = 1;

        matrix[4][0] = 4;
        matrix[4][1] = 3;
        matrix[4][2] = 6;
        matrix[4][3] = 1;
    }

    public int minPathSum(int[][] grid) {
        int m = grid.length;
        if(m==0){
            return 0;
        }
        int n = grid[0].length;
        if(n==0){
            return 0;
        }
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                setMin(grid, dp, i - 1, j - 1);
            }
        }
        print(dp);
        return dp[m - 1][n - 1];
    }

    private void setMin(int[][] grid, int[][] dp, int upIndex, int leftIndex) {
        dp[upIndex + 1][leftIndex + 1] = grid[upIndex + 1][leftIndex + 1];
        if (upIndex < 0 && leftIndex < 0) {
            return;
        }
        if (upIndex < 0 && leftIndex >= 0) {
            dp[upIndex + 1][leftIndex + 1] += dp[upIndex + 1][leftIndex];
            return;
        }
        if (upIndex >= 0 && leftIndex < 0) {
            dp[upIndex + 1][leftIndex + 1] += dp[upIndex][leftIndex + 1];
            return;
        }
        dp[upIndex + 1][leftIndex + 1] += min(dp[upIndex + 1][leftIndex], dp[upIndex][leftIndex + 1]);
    }

    private int min(int before, int after) {
        if (before > after) {
            return after;
        }
        return before;
    }
}
