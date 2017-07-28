package ACM.FractionsAgain;

import basic.Util;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/7/28
 * Time:15:40
 * <p>
 * 输入正整数k，找到所有的正整数x≥y，使得 。
 * 样例输入：
 * 2
 * 12
 * 样例输出：
 * 2
 * 1/2 = 1/6 + 1/3
 * 1/2 = 1/4 + 1/4
 * 8
 * 1/12 = 1/156 + 1/13
 * 1/12 = 1/84 + 1/14
 * 1/12 = 1/60 + 1/15
 * 1/12 = 1/48 + 1/16
 * 1/12 = 1/36 + 1/18
 * 1/12 = 1/30 + 1/20
 * 1/12 = 1/28 + 1/21
 * 1/12 = 1/24 + 1/24
 * <p>
 * Fractions Again?!, UVa 10976
 * <p>
 * k*(x+y)=xy
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("-------------------------------------");
        solution.case2();
        System.out.println("-------------------------------------");
        solution.case3();
    }

    private void case1() {
        fractions(2);
    }

    private void case2() {
        fractions(12);
    }

    private void case3() {
        fractions(Util.getRandomInteger(1, 1000));
    }

    private void fractions(int k) {
        int yMax = 2 * k;
        BigDecimal xMax;
        BigDecimal K = new BigDecimal(k);
        BigDecimal zero = new BigDecimal(0);
        BigDecimal Y;
        for (int y = k + 1; y <= yMax; y++) {
            Y = new BigDecimal(y);
            xMax = K.multiply(Y).divide(Y.subtract(K), 4, BigDecimal.ROUND_HALF_UP);
            if (xMax.compareTo(zero) > 0 && xMax.compareTo(new BigDecimal(xMax.intValue())) == 0) {
                System.out.println("1/" + k + " = 1/" + xMax.intValue() + " + 1/" + y);
            }
        }
    }
}
