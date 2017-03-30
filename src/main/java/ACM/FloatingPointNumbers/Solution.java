package ACM.FloatingPointNumbers;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/3/29
 * Time:15:00
 * <p>
 * 计算机常用阶码-尾数的方法保存浮点数。如图3-9所示，如果阶码有6位，尾数有8位，可以表达的最大浮点数为0.111111111 2 ×2 111111 2 。
 * 注意小数点后第一位必须为1，所以一共有9位小数。
 * <p>
 * 这个数换算成十进制之后就是0.998046875*2 63 =9.205357638345294*10 18 。你的任务是根据这个最大浮点数，求出阶码的位数E和尾数的位数M。
 * 输入格式为AeB，表示最大浮点数为A*10 B 。0<A<10，并且恰好包含15位有效数字。输入结束标志为0e0。
 * 对于每组数据，输出M和E。输入保证有唯一解，且0≤M≤9，1≤E≤30。在本题中，M+E+2不必为8的整数倍。
 * <p>
 * Floating-Point Numbers, UVa11809
 * <p>
 * http://blog.csdn.net/crazysillynerd/article/details/43339157
 * <p>
 * 查表法，注意输入的A的精度可能有误差，要做兼容。
 */
public class Solution {
    public static void main(String[] args) {
        double A = 9.205357638345294d;
        int B = 18;
        int[] result = FloatingPointNumbers(A, B);
        if (result == null) {
            System.out.println("no result.");
        } else {
            System.out.println("match success:" + "M=" + result[0] + ",E=" + result[1]);
        }
    }

    private static Map<Integer, List<BigDecimal>> cache_B2A = null;
    private static Map<String, BinaryLength> cache_BA2ME = null;

    private static int[] FloatingPointNumbers(double A, int B) {
        System.out.println("A:" + A + ",B:" + B);
        if (cache_B2A == null || cache_BA2ME == null) {
            init();

        }
        List<BigDecimal> bList = cache_B2A.get(B);
        if (bList == null) {
            return null;
        }
        System.out.println("bList size:" + bList.size());
        BigDecimal originA = new BigDecimal(A + "");
        BigDecimal minAbsolute = originA;
        BigDecimal targetA = originA;
        for (BigDecimal item : bList) {
            BigDecimal current = item.subtract(originA).abs();
            if (current.compareTo(minAbsolute) < 0) {
                minAbsolute = current;
                targetA = item;
            }
        }
        System.out.println("targetA:" + targetA.toString());
        String key = new ScienceCount(targetA, B).toUnionKey();
        BinaryLength result = cache_BA2ME.get(key);
        if (result == null) {
            return null;
        }
        return new int[]{result.getM(), result.getE()};
    }

    /**
     * 初始化查表
     */
    private static void init() {
        cache_B2A = new IdentityHashMap<>();
        cache_BA2ME = new HashMap<>();
        for (int M = 0; M <= 9; M++) {
            //for (int E = 1; E <= 30; E++) {//这里阶码上限为30时，计算出现无限等待，TLE
            for (int E = 1; E <= 18; E++) {
                ScienceCount sc = calculateScienceCount(M, E);
                Integer exponent = sc.getExponent();
                List<BigDecimal> list = new ArrayList<>();
                if (cache_B2A.containsKey(exponent)) {
                    list = cache_B2A.get(exponent);
                }
                list.add(sc.getCoefficient());
                cache_B2A.put(exponent, list);
                cache_BA2ME.put(sc.toUnionKey(), new BinaryLength(M, E));
            }
        }
        System.out.println("cache_B2A size:" + cache_B2A.size() + ",cache_BA2ME size:" + cache_BA2ME.size());
    }

    /**
     * 根据指定的阶码位数和尾数位数，计算对应的最大的浮点数，用科学计数法来表示
     *
     * @param M 尾数长度
     * @param E 阶码长度
     * @return 科学计数
     */
    private static ScienceCount calculateScienceCount(int M, int E) {
        BigDecimal a = new BigDecimal(1).subtract(new BigDecimal(1).divide(new BigDecimal(2).pow(M + 1)));
        BigDecimal b = new BigDecimal(2).pow(new BigDecimal(2).pow(E).subtract(new BigDecimal(1)).intValue());
        BigDecimal c = a.multiply(b);
        return new ScienceCount(c.toString());
    }

    private static class BinaryLength {
        private int M;//尾数长度
        private int E;//阶码长度

        public BinaryLength(int m, int e) {
            M = m;
            E = e;
        }

        public int getM() {
            return M;
        }

        public int getE() {
            return E;
        }
    }

    private static class ScienceCount {
        private BigDecimal coefficient;//系数 A
        private Integer exponent;//指数 B

        public ScienceCount(BigDecimal coefficient, Integer exponent) {
            this.coefficient = coefficient;
            this.exponent = exponent;
        }

        public ScienceCount(String originValue) {
            int pointIndex = originValue.indexOf(".");
            if (pointIndex == 1) {
                this.coefficient = new BigDecimal(originValue);
                this.exponent = 0;
                return;
            }
            char[] originChars = originValue.toCharArray();
            originChars = trimEndKey(originChars, '0');
            originChars = trimEndKey(originChars, '.');
            int len = originChars.length;
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < len; i++) {
                char c = originChars[i];
                if (c == '.') {
                    continue;
                }
                stringBuilder = stringBuilder.append(originChars[i]);
                if (i == 0) {
                    stringBuilder = stringBuilder.append(".");
                }
            }
            this.coefficient = new BigDecimal(stringBuilder.toString());
            this.exponent = pointIndex < 0 ? len - 1 : pointIndex - 1;
        }

        private char[] trimEndKey(char[] chars, char endKey) {
            int len = chars.length;
            if (len > 0 && chars[len - 1] == endKey) {
                char[] newChars = new char[len - 1];
                System.arraycopy(chars, 0, newChars, 0, len - 1);
                return trimEndKey(newChars, endKey);
            }
            return chars;
        }

        public BigDecimal getCoefficient() {
            return coefficient;
        }

        public Integer getExponent() {
            return exponent;
        }

        public String toUnionKey() {
            return getExponent() + ":" + getCoefficient().toString();
        }

        @Override
        public String toString() {
            return "coefficient:" + getCoefficient().toString() + ",exponent:" + getExponent();
        }
    }
}
