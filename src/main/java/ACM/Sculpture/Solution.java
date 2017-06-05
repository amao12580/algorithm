package ACM.Sculpture;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/6/3
 * Time:14:07
 * <p>
 * 某雕塑由n（n≤50）个边平行于坐标轴的长方体组成。每个长方体用6个整
 * 数x 0 ，y 0 ，z 0 ，x，y，z表示（均为1～500的整数），其中x 0 为长方体的顶点中x坐标的最小
 * 值，x表示长方体在x方向的总长度。其他4个值类似定义。你的任务是统计这个雕像的体积
 * 和表面积。注意，雕塑内部可能会有密闭的空间，其体积应计算在总体积中，但从“外部”看
 * 不见的面不应计入表面积。雕塑可能会由多个连通块组成。
 * <p>
 * Sculpture, ACM/ICPC NWERC 2008, UVa12171
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3323
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("-----------------------------------------");
        solution.case2();
    }

    private void case1() {
        int[][] desc = {{1, 2, 3, 3, 4, 5}, {6, 2, 3, 3, 4, 5}};
        sculpture(desc);
    }

    private void case2() {
        int[][] desc = {
                {1, 1, 1, 5, 5, 1},
                {1, 1, 10, 5, 5, 1},
                {1, 1, 2, 1, 4, 8},
                {2, 1, 2, 4, 1, 8},
                {5, 2, 2, 1, 4, 8},
                {1, 5, 2, 4, 1, 8},
                {3, 3, 4, 1, 1, 1}
        };
        sculpture(desc);
    }

    private void sculpture(int[][] desc) {
        System.out.println(Arrays.deepToString(desc));
        long area = 0;
        long volume = 0;


    }

    private class Cube {

    }
}
