package ACM.Flooded;

import basic.Util;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/4/25
 * Time:11:18
 * <p>
 * 有一个n*m（1≤m，n<30）的网格，每个格子是边长10米的正方形，网格四周是无限大
 * 的墙壁。输入每个格子的海拔高度，以及网格内雨水的总体积，输出水位的海拔高度以及有
 * 多少百分比的区域有水（即高度严格小于水平面）。
 * <p>
 * Flooded! ACM/ICPC World Finals 1999, UVa815
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.generatorOne();
        solution.flood();
    }

    private void flood() {
        int sum = houses.length * houses[0].length;
        int[] noOrder = new int[sum];
        int index = 0;
        for (int[] item : houses) {
            for (int i : item) {
                noOrder[index] = i;
                index++;
            }
        }
        Arrays.sort(noOrder);
        int flooded = 0;
        int depth = 0;
        for (int i = 0; i < sum; i++) {
            int c = noOrder[i];
            long myCubage = 100 * c;
            if (cubage >= myCubage) {
                flooded++;
                depth = c;
                cubage -= myCubage;
            } else {
                break;
            }
        }
        System.out.println("water depth:" + depth + ",flooded sum:" + flooded + ",flooded:" + (new BigDecimal(flooded).divide(new BigDecimal(sum)).multiply(new BigDecimal(100)).toString()) + "%");
    }

    private int[][] houses = null;//房屋高度配置
    private long cubage;//体积

    private void generatorOne() {
        int n = Util.getRandomInteger(10, 30 - 1);
        int m = Util.getRandomInteger(10, 30 - 1);
        houses = new int[n][m];
        long max = 0;
        for (int[] item : houses) {
            int len = item.length;
            for (int i = 0; i < len; i++) {
                item[i] = Util.getRandomInteger(1, 100);
                max += 100 * item[i];
            }
        }
        cubage = Util.getRandomInteger((int) max / 3, (int) max * 2);
        System.out.println("n:" + n + ",m:" + m + ",max:" + max + ",cubage:" + cubage);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(houses[i][j]);
                if (j != m - 1) {
                    System.out.print(",");
                }
            }
            System.out.println();
        }
    }
}
