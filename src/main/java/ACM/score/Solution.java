package ACM.score;

import basic.Util;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/3/21
 * Time:15:03
 * <p>
 * 给出一个由O和X组成的串（长度为1～80），统计得分。每个O的得分为目前连续出现的O的个数，X的得分为0。
 * <p>
 * 例如，OOXXOXXOOO的得分为1+2+0+0+1+0+0+1+2+3。
 */
public class Solution {
    private static final char[] seeds = {'O', 'X'};

    public static void main(String[] args) {
        String[] target = {"OOXXOXXOOO", generatorOne()};
        for (String item : target) {
            System.out.print("target:" + item);
            System.out.println(",value:" + score(item));
        }
    }

    private static int score(String string) {
        int len = string.length();
        int score = 0, targetLen = 0;
        for (int i = 0; i < len; i++) {
            char current = string.charAt(i);
            switch (current) {
                case 'O':
                    score += ++targetLen;
                    continue;
                case 'X':
                    targetLen = 0;
            }
        }
        return score;
    }


    private static String generatorOne() {
        StringBuilder stringBuilder = new StringBuilder();
        int len = Util.getRandomInteger(1, 80);
        for (int i = 0; i < len; i++) {
            stringBuilder = stringBuilder.append(seeds[Util.getRandomInteger(1, 2) - 1]);
        }
        return stringBuilder.toString();
    }
}
