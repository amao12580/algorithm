package ACM.BrokenKeyboard;

import basic.Util;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/18
 * Time:15:18
 * <p>
 * 你有一个破损的键盘。键盘上的所有键都可以正常工作，但有时Home键或者End键会自
 * 动按下。你并不知道键盘存在这一问题，而是专心地打稿子，甚至连显示器都没打开。当你
 * 打开显示器之后，展现在你面前的是一段悲剧的文本。你的任务是在打开显示器之前计算出
 * 这段悲剧文本。
 * 输入包含多组数据。每组数据占一行，包含不超过100000个字母、下划线、字符“[”或
 * 者“]”。其中字符“[”表示Home键，“]”表示End键。输入结束标志为文件结束符（EOF）。输
 * 入文件不超过5MB。对于每组数据，输出一行，即屏幕上的悲剧文本。
 * 样例输入：
 * This_is_a_[Beiju]_text
 * [[]][][]Happy_Birthday_to_Tsinghua_University
 * 样例输出：
 * BeijuThis_is_a__text
 * Happy_Birthday_to_Tsinghua_University
 * <p>
 * Broken Keyboard（a.k.a. Beiju Text）,UVa 11988
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        solution.case2();
    }

    private void case2() {
        int len = Util.getRandomInteger(1, 20);
        String[] strings = new String[len];
        for (int i = 0; i < len; i++) {
            strings[i] = generateOne();
        }
        print(strings);
    }

    String chars = Util.LETTERCHAR + "_[]";

    private String generateOne() {
//        int len = Util.getRandomInteger(10, 100000);
        int len = Util.getRandomInteger(10, 100);
        StringBuilder builder = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            builder = builder.append(chars.charAt(Util.getRandomInteger(0, chars.length() - 1)));
        }
        return builder.toString();
    }

    private void case1() {
        String[] strings = {
                "This_is_a_[Beiju]_text",
                "[[]][][]Happy_Birthday_to_Tsinghua_University"
        };
        print(strings);
    }

    private void print(String... strings) {
        for (String str : strings) {
            print(str);
            System.out.println("--------------------------------------------------");
        }
    }

    private void print(String str) {
        System.out.println("before:" + str);
        int endIndex = str.length() - 1;
        LinkedList<Character> chars = new LinkedList<>();
        Integer index = null;
        char Home = '[';
        char End = ']';
        for (int i = 0; i <= endIndex; i++) {
            char c = str.charAt(i);
            if (c == Home) {
                index = 0;
            } else if (c == End) {
                index = null;
            } else {
                if (index == null) {
                    chars.add(c);
                } else {
                    chars.add(index, c);
                    index++;
                }
            }
        }
        StringBuilder builder = new StringBuilder(chars.size());
        for (Character character : chars) {
            builder = builder.append(character);
        }
        System.out.println("after:" + builder.toString());
    }
}
