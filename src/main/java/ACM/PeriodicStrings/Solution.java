package ACM.PeriodicStrings;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/3/22
 * Time:10:13
 * <p>
 * 如果一个字符串可以由某个长度为k的字符串重复多次得到，则称该串以k为周期。
 * <p>
 * 例如，abcabcabcabc以3为周期（注意，它也以6和12为周期）。
 * <p>
 * 输入一个长度不超过80的字符串，输出其最小周期。
 */
public class Solution {
    public static void main(String[] args) {
        String[] target = {"abcabcabcabc", "ababcababcababcababc", "abcdefgabcdef", "abcdefgabcdefg", "abcdefgabcdefgabcdefg", "aaabaaaf"};
//        String[] target = {"ababcababcababcababc", "abcdefgabcdef", "aaabaaaf"};
//        String[] target = {"abcdefgabcdef", "aaabaaaf"};
//        String[] target = {"aaabaaaf"};
        for (String item : target) {
            System.out.print("target:" + item);
            System.out.println(",value:" + PeriodicStrings(item));
        }
    }

    private static int PeriodicStrings(String string) {
        String word = "";
        int len = string.length();
        int half = len / 2;
        for (int i = 0; i <= half; i++) {
            word += String.valueOf(string.charAt(i));
            int wLen = word.length();
            int sLen = len - wLen;
            if (sLen % wLen != 0) {
                if (i == (half - 1)) {
                    word = "";
                    break;
                }
                continue;
            }
            if (sLen < wLen) {
                word = "";
                break;
            }
            int beginIndex = wLen;
            boolean FC = false;
            while ((beginIndex + wLen - 1) < len && !FC) {
                String w = string.substring(beginIndex, beginIndex + wLen);
                if (w.equals(word)) {
                    beginIndex += wLen;
                } else {
                    FC = true;
                }
            }
            if (!FC) {//match success.
                break;
            } else {
                if (i >= (half - 1)) {
                    word = "";
                    break;
                }
            }
        }
        System.out.print(",period:" + word);
        return word.length();
    }
}
