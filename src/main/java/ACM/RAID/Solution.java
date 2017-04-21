package ACM.RAID;

import basic.Util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/4/21
 * Time:9:33
 * <p>
 * RAID技术用多个磁盘保存数据。每份数据在不止一个磁盘上保存，因此在某个磁盘损
 * 坏时能通过其他磁盘恢复数据。本题讨论其中一种RAID技术。数据被划分成大小
 * 为s（1≤s≤64）比特的数据块保存在d（2≤d≤6）个磁盘上，如图4-9所示，每d-1个数据块都
 * 有一个校验块，使得每d个数据块的异或结果为全0（偶校验）或者全1（奇校验）。
 * <p>
 * RAID!, ACM/ICPC World Finals 1997, UVa509
 */
public class Solution {
    private int d = 0;//磁盘个数
    private int s = 0;//每个数据长度
    private String[] b = null;//数据块
    private boolean t = true;//true=偶校验，奇校验

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
    }

    private void case1() {
        generatorOne();
        System.out.println("------------------------------------------------------------");
        print();
        System.out.println("------------------------------------------------------------");
        breakSome();
        System.out.println("------------------------------------------------------------");
        print();
        System.out.println("------------------------------------------------------------");
        parse();
    }

    private void parse() {
        int bLen = b.length;
        int beginIndex = 0;
        int parityIndex = 0;
        StringBuilder binaryDatas = new StringBuilder();
        while (beginIndex < bLen && beginIndex + parityIndex < bLen) {
            String[] datas = new String[d - 1];
            int index = 0;
            for (int i = beginIndex; i < beginIndex + d; i++) {
                if (i == (beginIndex + parityIndex)) {
                    continue;
                }
                datas[index] = b[i];
                index++;
            }
            String part = parse(b[beginIndex + parityIndex], datas);
            if (part == null) {
                return;
            } else {
                binaryDatas = binaryDatas.append(part);
            }
            beginIndex += d;
            if (parityIndex < d - 1) {
                parityIndex++;
            } else {
                parityIndex = 0;
            }
        }
        if (count > 0) {
            System.out.println("success repair count :" + count);
        }
        parseData = coverTo16Hex(binaryDatas);
        System.out.println("parse data:" + parseData);
        if (parseData.equals(originData)) {
            System.out.println("parse success");
        } else {
            System.out.println("parse failed");
        }
    }

    private String coverTo16Hex(StringBuilder binaryDatas) {
        System.out.println("binaryDatas len :" + binaryDatas.length());
        System.out.println("binaryDatas :" + binaryDatas);
        int beginIndex = 0;
        int step = 4;
        String str = binaryDatas.toString();
        StringBuilder stringBuilder = new StringBuilder();
        int len = str.length();
        while (beginIndex + step < len) {
            String c = str.substring(beginIndex, beginIndex + step);
            stringBuilder = stringBuilder.append(coverTo16Hex(c));
            beginIndex += step;
        }
        return stringBuilder.toString();
    }

    private Map<String, String> coverCache = new HashMap<>();

    private String coverTo16Hex(String c) {
        if (coverCache.containsKey(c)) {
            return coverCache.get(c);
        }
        String r = Integer.toHexString(Integer.parseInt(c, 2));
        coverCache.put(c, r);
        return r;
    }

    private char brokenFlag = '?';
    private int count = 0;

    private String parse(String parity, String[] datas) {
        StringBuilder stringBuilder = new StringBuilder();
        int len = parity.length();
        for (int i = 0; i < len; i++) {
            StringBuilder union = new StringBuilder();
            boolean pB = isBroken(parity, i, union);
            Integer brokenItemIndex = findBrokenItem(datas, i, union);
            if (brokenItemIndex == null) {
                System.out.println("find over 2 data broken,can not repair.");
                return null;
            }
            boolean dB = brokenItemIndex >= 0;
            if (!pB && !dB) {
                //验证数据一致性
                char c = getSelfLeftParity(union.toString());
                boolean failed = false;
                if (t) {
                    if (c == '0') {
                        failed = true;
                    }
                } else {
                    if (c != '0') {
                        failed = true;
                    }
                }
                if (failed) {
                    System.out.println("disk check parity failed.datas:" + Arrays.toString(datas) + ",union:" + union.toString());
                    return null;
                }
                continue;
            }
            if (pB && dB) {
                //不可修复
                System.out.println("find broken data,can not repair.");
                return null;
            }
            //修复 数据区
            if (!pB && dB) {
                char[] chars = datas[brokenItemIndex].toCharArray();
                char repair = getSelfLeftParity(union.toString());
                if (repair == '1' && t) {
                    repair = '0';
                } else if (repair == '0' && t) {
                    repair = '1';
                }
                chars[i] = repair;
                datas[brokenItemIndex] = new String(chars);
                count++;
            }
        }
        for (String item : datas) {
            stringBuilder = stringBuilder.append(item);
        }
        return stringBuilder.toString();
    }

    private char getSelfLeftParity(String s) {
        int len = s.length();
        if (len == 1) {
            char c = s.charAt(0);
            if (c != '1') {
                return '1';
            } else {
                return '0';
            }
        }
        char start = s.charAt(0);
        for (int i = 1; i < len; i++) {
            start = s.charAt(i) == start ? '0' : '1';
        }
        return getSelfLeftParity(start + "");
    }

    private boolean isBroken(String s, int index, StringBuilder union) {
        if (index > s.length() - 1) {
            return true;
        }
        char c = s.charAt(index);
        if (c == brokenFlag) {
            return true;
        } else {
            union.append(c);
            return false;
        }
    }

    private Integer findBrokenItem(String[] datas, int index, StringBuilder union) {
        int result = -1;
        for (int i = 0; i < d - 1; i++) {
            String item = datas[i];
            if (isBroken(item, index, union)) {
                if (result < 0) {
                    result = i;
                } else {
                    return null;
                }
            }
        }
        return result;
    }

    private void print() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            stringBuilder = stringBuilder.append(b[i]);
            if ((i + 1) % d != 0) {
                stringBuilder = stringBuilder.append(",");
            } else {
                System.out.println(stringBuilder.toString());
                stringBuilder = new StringBuilder();
            }
        }
    }

    private int brwakSum;

    private void breakSome() {
        System.out.println("breakSome:" + brwakSum);
        for (int i = 0; i < brwakSum; i++) {
            int index = Util.getRandomInteger(0, b.length - 1);
            breakOne(index);
        }
    }

    private void breakOne(int index) {
        String s = b[index];
        int i = Util.getRandomInteger(0, s.length() - 1);
        char[] chars = s.toCharArray();
        chars[i] = brokenFlag;
        b[index] = new String(chars);
    }

    private String originData = null;
    private String parseData = null;

    private void generatorOne() {
        d = Util.getRandomInteger(2, 6);
        s = getOne(2, 64, 2);
        int bLen = getOne(80, 100, d);
        b = new String[bLen];
        brwakSum = Util.getRandomInteger(b.length / 8, b.length / 4);
        t = Util.getRandomBoolean();
        System.out.println("d:" + d + ",s:" + s + ",t:" + t + ",bLen:" + bLen);
        int beginIndex = 0;
        int parityIndex = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while (beginIndex < bLen && beginIndex + parityIndex < bLen) {
            String dataPart = null;
            for (int i = beginIndex; i < beginIndex + d; i++) {
                if (i == (beginIndex + parityIndex)) {
                    continue;
                }
                b[i] = getOneData();
                stringBuilder = stringBuilder.append(b[i]);
                if (dataPart == null) {
                    dataPart = b[i];
                } else {
                    dataPart = getParity(b[i], dataPart);
                }
            }
            b[beginIndex + parityIndex] = getLeftParity(dataPart);
            beginIndex += d;
            if (parityIndex < d - 1) {
                parityIndex++;
            } else {
                parityIndex = 0;
            }
        }
        System.out.println(Arrays.toString(b));
        originData = coverTo16Hex(stringBuilder);
        System.out.println("origin data:" + originData);
    }

    private String getLeftParity(String s) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (t) {
                if (s.charAt(i) == '0') {
                    stringBuilder = stringBuilder.append("0");
                } else {
                    stringBuilder = stringBuilder.append("1");
                }
            } else {
                if (s.charAt(i) != '0') {
                    stringBuilder = stringBuilder.append("0");
                } else {
                    stringBuilder = stringBuilder.append("1");
                }
            }
        }
        return stringBuilder.toString();
    }

    private String getParity(String s, String s1) {
        int len = s.length();
        if (s1.length() != len) {
            throw new IllegalArgumentException("数据块长度不一致");
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            if (s.charAt(i) == s1.charAt(i)) {
                stringBuilder = stringBuilder.append("0");
            } else {
                stringBuilder = stringBuilder.append("1");
            }
        }
        return stringBuilder.toString();
    }

    private String getOneData() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < s; i++) {
            stringBuilder = stringBuilder.append(Util.getRandomBoolean() ? "1" : "0");
        }
        return stringBuilder.toString();
    }

    private int getOne(int min, int max, int base) {
        int c = Util.getRandomInteger(min, max);
        if (c % base == 0) {
            return c;
        } else {
            return getOne(min, max, base);
        }
    }
}
