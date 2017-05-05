package ACM.CompoundWords;

import basic.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/4
 * Time:17:27
 * <p>
 * 给出一个词典，找出所有的复合词，即恰好有两个单词连接而成的单词。输入每行都是
 * 一个由小写字母组成的单词。输入已按照字典序从小到大排序，且不超过120000个单词。输
 * 出所有复合词，按照字典序从小到大排列。
 * <p>
 * Compound Words, UVa 10391
 * <p>
 * Sample Input
 * <p>
 * a
 * alien
 * born
 * less
 * lien
 * never
 * nevertheless
 * new
 * newborn
 * the
 * zebra
 * Sample Output
 * <p>
 * alien
 * newborn
 * <p>
 * 有一堆按照字典序排好的字符串，问你有多少字符串是由其它两个字符串组成。
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("------------------------------------------------------------------------------------------------------");
        Solution solution2 = new Solution();
        solution2.case2();
    }

    private String[] strings;

    private int len = 0;

    private int minLen = 0;

    private void case1() {
        strings = new String[]{"a", "alien", "born", "less", "lien", "never", "nevertheless", "new", "newborn", "the", "zebra"};
        compoundWords();
    }

    private void case2() {
        strings = generatorOne();
        compoundWords();
    }

    private String[] generatorOne() {
        int len = Util.getRandomInteger(5, 100);
        int cLen = Util.getRandomInteger(5, 10);
//        int len = Util.getRandomInteger(5, 120000);
        String[] result = new String[len + cLen];
        for (int i = 0; i < len; i++) {
            result[i] = generatorOneWord();
        }
        for (int i = len; i < len + cLen; i++) {
            result[i] = result[Util.getRandomInteger(0, len - 1)] + result[Util.getRandomInteger(0, len - 1)];
        }
        return Util.orderByDictionaryASC(result);
    }

    private String generatorOneWord() {
        int len = Util.getRandomInteger(1, 10);
        StringBuilder builder = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            builder = builder.append(Util.seeds_big_chars[Util.getRandomInteger(0, Util.seedsBigCharsEndIndex)]);
        }
        return builder.toString();
    }

    private void compoundWords() {
        System.out.println("strings:" + Arrays.toString(strings));
        int[] orderByLength = orderByLength();
        System.out.println("orderByLength:" + Arrays.toString(orderByLength));
        List<String> result = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            int index = orderByLength[i];
            String c = strings[index];
            if (checkOne(c, index, index, 0)) {
                result.add(c);
            }
        }
        System.out.println("result:" + result.toString());
    }

    private boolean checkOne(String string, int arrayIndex, int skipIndex, int indexOfBegin) {
        int myLen = string.length();
        if (indexOfBegin >= myLen) {
            return true;
        }
        if (myLen < minLen) {
            return false;
        }
        int left = myLen - indexOfBegin;
        for (int i = arrayIndex - 1; i >= 0; i--) {
            if (i == skipIndex) {
                continue;
            }
            String c = strings[i];
            int cLen = c.length();
            if (arrayIndex > 0 && c.charAt(0) != string.charAt(indexOfBegin)) {
                break;
            }
            if (arrayIndex < 0 && cLen != left) {
                continue;
            }
            boolean p = string.startsWith(c, indexOfBegin);
            if (p) {
                if (arrayIndex > 0) {
                    if (indexOfBegin + cLen >= myLen) {
                        return false;
                    }
                    if (checkOne(string, -1, skipIndex, indexOfBegin + cLen)) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        for (int i = arrayIndex + 1; i < len; i++) {
            if (i == skipIndex) {
                continue;
            }
            String c = strings[i];
            int cLen = c.length();
            if (arrayIndex > 0 && c.charAt(0) != string.charAt(indexOfBegin)) {
                break;
            }
            if (arrayIndex < 0 && cLen != left) {
                continue;
            }
            boolean p = string.startsWith(c, indexOfBegin);
            if (p) {
                if (arrayIndex > 0) {
                    if (indexOfBegin + cLen >= myLen) {
                        return false;
                    }
                    if (checkOne(string, -1, skipIndex, indexOfBegin + cLen)) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    private int[] orderByLength() {
        len = strings.length;
        Element[] elements = new Element[len];
        for (int i = 0; i < len; i++) {
            elements[i] = new Element(i, strings[i].length());
        }
        Arrays.sort(elements);
        minLen = elements[0].getLength();
        int myLen = len - 1;
        int[] result = new int[len];
        for (int i = myLen; i >= 0; i--) {
            result[myLen - i] = elements[i].getIndex();
        }
        return result;
    }

    private class Element implements Comparable<Element> {
        private int index;
        private int length;

        public Element(int index, int length) {
            this.index = index;
            this.length = length;
        }

        public int getIndex() {
            return index;
        }

        public int getLength() {
            return length;
        }

        @Override
        public int compareTo(Element o) {
            if (this.getLength() > o.getLength()) {
                return 1;
            }
            if (this.getLength() < o.getLength()) {
                return -1;
            }
            return 0;
        }
    }
}
