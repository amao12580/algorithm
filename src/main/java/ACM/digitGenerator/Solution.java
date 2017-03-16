package ACM.digitGenerator;

import basic.Util;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/3/16
 * Time:15:02
 * <p>
 * 如果x加上x的各个数字之和得到y，就说x是y的生成元。给出n（1≤n≤100000），求最小生成元。无解输出0。
 * 例如，n=216，121，2005时的解分别为198(198+1+9+8=216)，0(无解)，1979(1979+1+9+7+9=2005)。
 */
public class Solution {
    public static void main(String[] args) {
        int targets[] = {216, 121, 2005, 200, 100, 99, Util.getRandomInteger(1, 100000), Util.getRandomInteger(1, 100000), Util.getRandomInteger(1, 100000), Util.getRandomInteger(1, 100000)};
        for (int target : targets) {
            System.out.println("value:" + target + ",maybe:" + gen(target));
        }

    }

    private static int gen(int target) {
        int result = 0;
        if (target > 100000 || target < 1) {
            return result;
        }
        int len = (target + "").length();//这里观察到一个简单规律：4位长度的target的result至少是3位长度，以此类推。从而剪枝，避免不必要的计算。
        for (int i = target - 1; i > 1 && (len - ("" + i).length()) < 2; i--) {
            if ((i + (sumSelf(i))) == target) {
                return i;
            }
        }
        return result;
    }

    private static int sumSelf(int n) {
        String s = n + "";
        int result = 0;
        int len = s.length();
        for (int i = 0; i < len; i++) {
            result += Integer.valueOf(s.charAt(i) + "");
        }
        return result;
    }
}
