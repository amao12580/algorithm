package ACM.PrimeRingProblem;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/7/31
 * Time:17:29
 * <p>
 * 输入正整数n，把整数1, 2, 3,…, n组成一个环，使得相邻两个整数之和均为素数。输出
 * 时从整数1开始逆时针排列。同一个环应恰好输出一次。n≤16。
 * 样例输入：
 * 6
 * 样例输出：
 * 1 4 3 2 5 6
 * 1 6 5 2 3 4
 * <p>
 * Prime Ring Problem, UVa 524
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
    }

    private void case1() {
        ring(6);
    }

    private void ring(int n) {

    }
}
