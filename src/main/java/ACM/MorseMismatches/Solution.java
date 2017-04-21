package ACM.MorseMismatches;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/4/19
 * Time:18:02
 * <p>
 * 输入每个字母的Morse编码，一个词典以及若干个编码。对于每个编码，判断它可能是
 * 哪个单词。如果有多个单词精确匹配，任选一个输出并且后面加上“!”；如果无法精确匹
 * 配，可以在编码尾部增加或删除一些字符以后匹配某个单词（增加或删除的字符应尽量
 * 少）。如果有多个单词可以这样匹配上，任选一个输出并且在后面加上“?”。
 * <p>
 * 莫尔斯电码的细节参见原题:http://www.voidcn.com/blog/shannonnansen/article/p-2305120.html
 */
public class Solution {
    private Map<Character, String> decodes = null;//每个字母的词典
    private Set<String> maybes = null;//可能的明文
    private Set<String> codes = null;//密文

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("----------------------------------------------------------------");
        solution.case2();
        System.out.println("----------------------------------------------------------------");
    }

    private void case1() {
        case1Major();
        codeAndPrint("WHAT");
        codeAndPrint("HATH");
        codeAndPrint("GOD");
        codeAndPrint("WROTH");
        codeAndPrint("WHAT");
        codeAndPrint("AN");
        codeAndPrint("EARTHQUAKE");
        codeAndPrint("EAT");
        codeAndPrint("READY");
        codeAndPrint("TO");
        codeAndPrint("EAT");
    }

    private void codeAndPrint(String code) {
        System.out.println("decode:  " + code + "  ,code:  " + code(code));
    }

    private String code(String code) {
        StringBuilder result = new StringBuilder();
        int len = code.length();
        for (int i = 0; i < len; i++) {
            result = result.append(decodes.get(code.charAt(i)));
        }

        return result.toString();
    }

    private void case2() {
        case1Major();
        decode();
    }

    private void decode() {
        for (String item : codes) {
            String[] codes = item.split(" ");
            for (String code : codes) {
                suffix = "";
                decodeOne(code);
            }
        }
    }

    private String suffix = null;
    private int maxLen = 0;

    private void decodeOne(String code) {
        System.out.print("decodeOne: code :   " + code + "   ,");
//        Set<String> preResult = new HashSet<>();
        int len = code.length();
        String fullMatch = findFullMatch(code);
        if (fullMatch != null) {
            String minDictOrderStr = fullMatch;
            String current = findFullMatch(code, len, 0, len - 1, new StringBuilder()).toString();
            if (!current.isEmpty() && !current.equals(minDictOrderStr) && maybes.contains(current)) {
                suffix = "!";
            }
            minDictOrderStr = findMinDictOrderStr(minDictOrderStr, current);
            System.out.println("Full match :   " + minDictOrderStr + suffix);
            return;
        }
        suffix = "?";
        //加字符
        addStart = code;
        String maybe = findAddMatch("");
        if (maybe == null || !maybes.contains(maybe)) {
            System.out.println("add match failed.");
        } else {
            System.out.println("add match success :" + maybe + suffix);
            return;
        }
        //减字符
        maybe = findDeleteMatch(code, len, len);
        if (maybe == null || !maybes.contains(maybe)) {
            System.out.println("delete match failed.");
        } else {
            System.out.println("delete match success :" + maybe + suffix);
        }
    }

    private char p = '.';
    private char r = '-';


    private String addStart = null;

    private String findAddMatch(String start) {
        if (start.isEmpty()) {
            start = new String(addStart.toCharArray());
        }
        if (start.length() > maxLen) {
            return null;
        }
        String mP = findFullMatch(start + p);
        if (mP != null) {
            return mP;
        } else {
            String mR = findFullMatch(start + r);
            if (mR != null) {
                return mR;
            } else {
                String rS = findAddMatch(start + r);
                if (rS != null) {
                    return rS;
                }
                String pS = findAddMatch(addStart + p);
                if (pS != null) {
                    return pS;
                }
                return null;
            }
        }
    }

    private String findDeleteMatch(String start, int len, int endIndex) {
        if (endIndex < 0) {
            return null;
        }
        start = start.substring(0, endIndex);
        String mP = findFullMatch(start, len, 0, start.length(), new StringBuilder()).toString();
        if (!mP.isEmpty()) {
            return mP;
        } else {
            return findDeleteMatch(start, len, endIndex - 1);
        }
    }

    private StringBuilder findFullMatch(String origin, int originLength, int beginIndex, int maxCutLength, StringBuilder result) {
        if ((beginIndex + maxCutLength) > originLength) {
            maxCutLength = originLength - beginIndex;
        }
        if (maxCutLength <= 0) {
            return result;
        }
        int endIndex = beginIndex + maxCutLength;
        String c = findFullMatch(origin.substring(beginIndex, endIndex));
        if (c != null) {
            result = result.append(c);
            return findFullMatch(origin, originLength, endIndex, maxCutLength, result);
        } else {
            return findFullMatch(origin, originLength, beginIndex, maxCutLength - 1, result);
        }
    }

    /**
     * 找出两个字符串之间，字典序较小的一个
     * <p>
     * 若已存在与预定义集合，则不计算字典序
     */
    private String findMinDictOrderStr(String key, String compare) {
        if (key == null) {
            return compare;
        }
        if (key.equals(compare)) {
            return key;
        }
        if (maybes.contains(key) && !maybes.contains(compare)) {
            return key;
        } else if (!maybes.contains(key) && maybes.contains(compare)) {
            return compare;
        }
        int kLen = key.length();
        int cLen = compare.length();
        if (cLen < kLen) {
            return compare;
        }
        if (cLen == kLen) {
            for (int i = 0; i < kLen; i++) {
                if (compare.charAt(i) < key.charAt(i)) {
                    return compare;
                }
            }
        }
        return key;
    }

    private String findFullMatch(String decode) {
        if (decode == null || decode.isEmpty()) {
            return null;
        }
        initDecodeReversal();
        return decodeReversal.get(decode);
    }

    private Map<String, String> decodeReversal = null;

    private void initDecodeReversal() {
        if (decodeReversal == null) {
            decodeReversal = new HashMap<>();
            for (Map.Entry<Character, String> entry : decodes.entrySet()) {
                decodeReversal.put(entry.getValue(), entry.getKey() + "");
            }
            for (String item : maybes) {
                String code = code(item);
                if (code.length() > maxLen) {
                    maxLen = code.length();
                }
                decodeReversal.put(code, item);
            }
        }
    }

    private void case1Major() {
        maybes = new HashSet<>();
        maybes.add("AN");
        maybes.add("EARTHQUAKE");
        maybes.add("EAT");
        maybes.add("GOD");
        maybes.add("HATH");
        maybes.add("IM");
        maybes.add("READY");
        maybes.add("TO");
        maybes.add("WHAT");
        maybes.add("WROTH");

        decodes = new HashMap<>();
        decodes.put('A', ".-");
        decodes.put('B', "-...");
        decodes.put('C', "-.-.");
        decodes.put('D', "-..");
        decodes.put('E', ".");
        decodes.put('F', "..-.");
        decodes.put('G', "--.");
        decodes.put('H', "....");
        decodes.put('I', "..");
        decodes.put('J', ".---");
        decodes.put('K', "-.-");
        decodes.put('L', ".-..");
        decodes.put('M', "--");
        decodes.put('N', "-.");
        decodes.put('O', "---");
        decodes.put('P', ".--.");
        decodes.put('Q', "--.-");
        decodes.put('R', ".-.");
        decodes.put('S', "...");
        decodes.put('T', "-");
        decodes.put('U', "..-");
        decodes.put('V', "...-");
        decodes.put('W', ".--");
        decodes.put('X', "-..-");
        decodes.put('Y', "-.--");
        decodes.put('Z', "--..");
        decodes.put('0', "------");
        decodes.put('1', ".-----");
        decodes.put('2', "..---");
        decodes.put('3', "...--");
        decodes.put('4', "....-");
        decodes.put('5', ".....");
        decodes.put('6', "-....");
        decodes.put('7', "--...");
        decodes.put('8', "---..");
        decodes.put('9', "----.");

        codes = new LinkedHashSet<>();
        codes.add(".--.....-- .....--....");
        codes.add("--.----.. .--.-.----..");
        codes.add(".--.....-- .--.");
        codes.add("..-.-.-....--.-..-.--.-.");
        codes.add("..-- .-...--..-.--");
        codes.add("---- ..--");
    }
}