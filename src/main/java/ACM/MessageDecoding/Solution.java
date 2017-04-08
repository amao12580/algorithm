package ACM.MessageDecoding;

import basic.Util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/4/7
 * Time:11:17
 * <p>
 * <p>
 * 考虑下面的01串序列：
 * 0, 00, 01, 10, 000, 001, 010, 011, 100, 101, 110, 0000, 0001, …, 1101, 1110, 00000, …
 * <p>
 * 首先是长度为1的串，然后是长度为2的串，依此类推。
 * 如果看成二进制，相同长度的后 一个串等于前一个串加1。注意上述序列中不存在全为1的串。
 * <p>
 * 你的任务是编写一个解码程序。首先输入一个编码头（例如AB#TANCnrtXc），则上述序列的每个串依次对应编码头的每个字符。
 * 例如，0对应A，00对应B，01对应#，…，110对 应X，0000对应c。
 * 接下来是编码文本（可能由多行组成，你应当把它们拼成一个长长的01 串）。
 * 编码文本由多个小节组成，每个小节的前3个数字代表小节中每个编码的长度（用二进制表示，例如010代表长度为2），然后是各个字符的编码，
 * 以全1结束（例如，编码长度为2的小节以11结束）。
 * 编码文本以编码长度为000的小节结束。例如，编码头为$#**\，编码文本为0100000101101100011100101000，
 * 应这样解码：010(编码长度为2)00(#)00(#)10(*)11(小节结束)011(编码长度为3)000(\)111(小节结束)001(编码长度为1)0($)1(小节结束)000(编码结束)。
 * <p>
 * <p>
 * Message Decoding, ACM/ICPC World Finals 1991, UVa 213
 * <p>
 * page:145
 * <p>
 * 理解难？这篇文章帮助理解：http://blog.csdn.net/ramay7/article/details/50319767
 */
public class Solution {
    public static void main(String[] args) {
        new Solution().decode("$#**\\", "0100000101101100011100101000");
        Solution solution = new Solution();
        String[] random = solution.generatorOne();
        solution.decode(random[0], random[1]);
    }

    private String decode(String key, String encryptMessage) {
        System.out.println("key:" + key);
        System.out.println("encryptMessage:" + encryptMessage);
        init(3);
        char[] chars = key.toCharArray();
        int index = 0;
        for (char c : chars) {
            if (index > (segments.size() - 1)) {
                init(maxLen + 1);
                continue;
            }
            decodeMap.put(segments.get(index), c);
            index++;
        }
        decode(encryptMessage, 0, encryptMessage.length() - 3);//以三位0结束
        StringBuilder stringBuilder = new StringBuilder();
        for (Character character : decodes) {
            stringBuilder = stringBuilder.append(character);
        }
        String result = stringBuilder.toString();
        System.out.println("result:" + result);
        System.out.println("----------------------------------------------------");
        return result;
    }


    private void decode(String encryptMessage, int beginIndex, int endIndex) {
        if (beginIndex >= endIndex) {
            return;
        }
        int lenKeyEndIndex = beginIndex + 3;//开头3位是每个子片段长度
        int lenKey = Integer.valueOf(encryptMessage.substring(beginIndex, lenKeyEndIndex), 2);
        String endKey = Integer.toBinaryString((int) Util.pow(2, lenKey) - 1);//根据子片段长度计算该片段的结束标记
        String next = encryptMessage.substring(lenKeyEndIndex, lenKeyEndIndex + lenKey);
        while (!next.equals(endKey)) {
            decodes.add(decodeMap.get(next));
            lenKeyEndIndex += lenKey;
            next = encryptMessage.substring(lenKeyEndIndex, lenKeyEndIndex + lenKey);
        }
        decode(encryptMessage, lenKeyEndIndex + lenKey, endIndex);
    }

    private int maxLen = 1;
    private List<String> segments = new LinkedList();
    private List<Character> decodes = new LinkedList();
    private Map<String, Character> decodeMap = new HashMap<>();

    /**
     * 构建基础解码序列
     *
     * @param len 最大长度
     */
    private void init(int len) {
        if (len < 1 || len <= maxLen) {
            return;
        }
        for (int i = maxLen; i <= len; i++) {
            int current = 0;
            int max = (int) Util.pow(2, i);
            max--;
            while (current < max) {
                segments.add(String.format("%0" + i + "d", current == 0 ? 0 : Integer.valueOf(Integer.toBinaryString(current))));
                current++;
            }
        }
        maxLen = len;
    }

    private char[] seeds = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private String[] generatorOne() {
        String[] result = new String[2];
        StringBuilder encrypt = new StringBuilder();
        int roundSum = Util.getRandomInteger(4, 10);//4-10个片段
        int maxKeyLen = 0;
        for (int i = 1; i <= roundSum; i++) {
            int lenKey = Util.getRandomInteger(1, 7);//当前片段的起始3位长度
            if (lenKey > maxKeyLen) {
                maxKeyLen = lenKey;
            }
            int max = ((int) Util.pow(2, lenKey)) - 2;
            encrypt = encrypt.append(String.format("%03d", Integer.valueOf(Integer.toBinaryString(lenKey))));
            int segmentSum = Util.getRandomInteger(1, 5);//1-5个子片段
            for (int j = 1; j <= segmentSum; j++) {
                encrypt = encrypt.append(String.format("%0" + lenKey + "d", Integer.valueOf(Integer.toBinaryString(Util.getRandomInteger(1, max)))));
            }
            encrypt = encrypt.append(Integer.toBinaryString((int) Util.pow(2, lenKey) - 1));
        }
        encrypt = encrypt.append("000");
        init(maxKeyLen);
        StringBuilder key = new StringBuilder();
        for (String ignored : segments) {
            key = key.append(seeds[Util.getRandomInteger(0, seeds.length - 1)]);
        }
        result[0] = key.toString();
        result[1] = encrypt.toString();
        return result;
    }
}
