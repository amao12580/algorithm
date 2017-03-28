package ACM.DNAConsensusString;

import basic.Util;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/3/27
 * Time:14:42
 * <p>
 * <p>
 * 输入m个长度均为n的DNA序列，求一个DNA序列，到所有序列的总Hamming距离尽量小。
 * 两个等长字符串的Hamming距离等于字符不同的位置个数，例如，ACGT和GCGA的Hamming距离为2（左数第1, 4个字符不同）。
 * 输入整数m和n（4≤m≤50, 4≤n≤1000），以及m个长度为n的DNA序列（只包含字母 A，C，G，T），输出到m个序列的Hamming距离和最小的DNA序列和对应的距离。
 * 如有多解，要求为字典序最小的解。
 * 例如，对于下面5个DNA序列，最优解为TAAGCTAC。
 * <p>
 * TATGATAC
 * TAAGCTAC
 * AAAGATCC
 * TGAGATAC
 * TAAGATGT
 * <p>
 * DNA Consensus String, ACM/ICPC Seoul 2006, UVa1368
 */
public class Solution {
    private static final char[] seeds = {'A', 'C', 'G', 'T'};

    public static void main(String[] args) {
        String[] s = {"TATGATAC",
                "TAAGCTAC",
                "AAAGATCC",
                "TGAGATAC",
                "TAAGATGT"};
        String[][] target = {s, generatorArray(), generatorArray()};
//        String[][] target = {s};
        for (String[] item : target) {
            System.out.println("target:");
            print(item);
            System.out.println("value:" + DNAConsensusString(item));
            System.out.println("-------------------------");
        }
    }

    private static int hammingDistance(String s1, String s2) {
        int len = s1.length();
        int result = 0;
        for (int i = 0; i < len; i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                result++;
            }
        }
//        System.out.println(s1 + "," + s2 + ":" + result);
        return result;
    }

    private static String DNAConsensusString(String[] array) {
        int len = array.length;
        Map<String, Integer> distances = new HashMap<>();
        DNAConsensusString(array, len, 0, distances);
        Set<String> maybe = findMaybe(distances, array);
        return maybe.size() > 1 ? minimumDictionary(maybe) : getFirst(maybe);
    }

    private static String getFirst(Set<String> maybe) {
        String result = null;
        for (String item : maybe) {
            result = item;
            break;
        }
        return result;
    }

    private static Set<String> findMaybe(Map<String, Integer> distances, String[] array) {
        int min = array.length * array[0].length();
        Set<String> result = new HashSet<>();
        for (Map.Entry<String, Integer> item : distances.entrySet()) {
            String key = item.getKey();
            Integer value = item.getValue();
            if (key.length() == 1 && value <= min) {
                if (value < min) {
                    min = value;
                    result.clear();
                }
                result.add(array[Integer.valueOf(key)]);
            }
        }
        return result;
    }

    private static String minimumDictionary(Set<String> maybe) {
        String string = null;
        BigInteger min = null;
        for (String item : maybe) {
            BigInteger current = calculateOneScore(item);
            if (min == null || current.compareTo(min) < 0) {
                string = item;
                min = current;
            }
        }
        return string;
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

    private static void DNAConsensusString(String[] array, int length, int beginIndex, Map<String, Integer> distance) {
        if (beginIndex == length - 1) {
            return;
        }
        for (int i = 0; i < length; i++) {
            if (i == beginIndex) {
                continue;
            }
            int d;
            if (distance.containsKey(beginIndex + "" + i) || distance.containsKey(i + "" + beginIndex)) {
                d = distance.containsKey(beginIndex + "" + i) ? distance.get(beginIndex + "" + i) : distance.get(i + "" + beginIndex);
            } else {
                d = hammingDistance(array[beginIndex], array[i]);
                distance.put(beginIndex + "" + i, d);

            }
            if (distance.containsKey("" + beginIndex)) {
                distance.put("" + beginIndex, distance.get("" + beginIndex) + d);
            } else {
                distance.put("" + beginIndex, d);
            }
        }
        DNAConsensusString(array, length, beginIndex + 1, distance);
    }

    private static void print(String[] arrays) {
        for (String array : arrays) {
            System.out.println(array);
        }
    }


    private static String[] generatorArray() {
        int len = Util.getRandomInteger(4, 50);
        int iLen = Util.getRandomInteger(4, 1000);
        String[] strings = new String[len];
        for (int i = 0; i < len; i++) {
            strings[i] = generatorOne(iLen);
        }
        return strings;
    }

    private static String generatorOne(int len) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            stringBuilder = stringBuilder.append(seeds[Util.getRandomInteger(0, 3)]);
        }
        return stringBuilder.toString();
    }
}
