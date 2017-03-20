package ACM.circularSequence;

import basic.Util;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/3/16
 * Time:17:36
 * <p>
 * 长度为n的环状串有n种表示法，分别为从某个位置开始顺时针得到。例如，图3-4的环状串
 * 有10种表示：CGAGTCAGCT，GAGTCAGCTC，AGTCAGCTCG等。在这些表示法中，字典序最小的称为"最小表示"。
 * 输入一个长度为n（n≤100）的环状DNA串（只包含A、C、G、T这4种字符）的一种表示法，你的任务是输出该环状串的最小表示。
 * 例如，CTCC的最小表示是CCCT，CGAGTCAGCT的最小表示为AGCTCGAGTC。
 */
public class Solution {
    private static final char[] seeds = {'A', 'C', 'G', 'T'};
    private static final int maxLen = 100;

    public static void main(String[] args) {
        String[] target = {"CTCC", "CGAGTCAGCT", generatorOne()};
        for (String item : target) {
            System.out.print("target:" + item);
            System.out.println(",value:" + minimum(item));
        }
    }

    private static String minimum(String string) {
        int len = string.length();
        int currentIndex = 0;
        String minimumString = string;
        BigInteger minimumScore = contact(seeds.length + 1, maxLen + 1);
        while (currentIndex < len) {
            String currentString;
            if (currentIndex > 0) {
                currentString = calculateOneString(string, currentIndex);
            } else {
                currentString = string;
            }
            BigInteger currentScore = calculateOneScore(currentString);
            if (currentScore.compareTo(minimumScore) < 0) {
                minimumScore = currentScore;
                minimumString = currentString;
            }
            currentIndex++;
        }
        return minimumString;
    }

    private static BigInteger contact(int seed, int times) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < times; i++) {
            stringBuilder = stringBuilder.append(seed);
        }
        return new BigInteger(stringBuilder.toString());
    }

    private static String calculateOneString(String string, int index) {
        String string1 = string.substring(index);
        String string2 = string.substring(0, index);
        return string1 + string2;
    }

    private static BigInteger calculateOneScore(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        int len = string.length();
        for (int i = 0; i < len; i++) {
            stringBuilder = stringBuilder.append(calculateOneScore(string.charAt(i)));
        }
        return new BigInteger(stringBuilder.toString());
    }

    private static Map<Character, Integer> scores = null;

    private static int calculateOneScore(char c) {
        if (scores == null) {
            initScore();
        }
        return scores.get(c);
    }

    private static void initScore() {
        scores = new HashMap<>();
        int len = seeds.length;
        for (int i = 0; i < len; i++) {
            scores.put(seeds[i], i + 1);
        }
    }

    private static String generatorOne() {
        StringBuilder stringBuilder = new StringBuilder();
        int len = Util.getRandomInteger(4, maxLen);
        for (int i = 0; i < len; i++) {
            stringBuilder = stringBuilder.append(seeds[Util.getRandomInteger(0, 3)]);
        }
        return stringBuilder.toString();
    }
}
