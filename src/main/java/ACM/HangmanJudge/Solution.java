package ACM.HangmanJudge;

import basic.Util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/4/6
 * Time:9:07
 * <p>
 * 刽子手游戏其实是一款猜单词游戏，如图4-1所示。游戏规则是这样的：计算机想一个单词让你猜，你每次可以猜一个字母。
 * 如果单词里有那个字母，所有该字母会显示出来；如果没有那个字母，则计算机会在一幅“刽子手”画上填一笔。
 * 这幅画一共需要7笔就能完成，因此你最多只能错6次。
 * 注意，猜一个已经猜过的字母也算错。
 * 在本题中，你的任务是编写一个“裁判”程序，输入单词和玩家的猜测，判断玩家赢了（You win.）、输了（You lose.）还是放弃了（You chickened out.）。
 * 每组数据包含3行，第1行是游戏编号（-1为输入结束标记），第2行是计算机想的单词，第3行是玩家的猜测。
 * 后两行保证只含小写字母。
 * <p>
 * Hangman Judge, UVa 489
 * <p>
 * <p>
 * <p>
 * 一个字符串的游戏  有一个你不知道的字符串  你开始有7条命  然后你每次猜一个字母
 * <p>
 * 若你猜的字母在原字符串中  原字符串就去掉所有那个字母  否则你失去一条命
 * <p>
 * 如果你在命耗完之前原字符串中所有的字母都被猜出  则你赢
 * <p>
 * 如果你在命耗完了原字符串中还有字母没被猜出  则你输
 * <p>
 * 如果你在命没耗完原字符串中也还有字母没被猜出  视为你放弃
 */
public class Solution {
    private static final char[] seeds = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public static void main(String[] args) {
        String word = "cheese";
        char[] guess1 = {'c', 'h', 'e', 's', 'e'};
        char[] guess2 = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
        char[] guess3 = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'i', 'j'};
        execute(word, guess1);
        execute(word, guess2);
        execute(word, guess3);
        execute(generatorOne(), generatorOne().toCharArray());
    }

    private static void execute(String word, char[] guess) {
        System.out.println("word:" + word);
        System.out.println("guess:" + Arrays.toString(guess));
        System.out.println("result:" + HangmanJudge(word, guess));
        System.out.println("-----------------------------------------------------");
    }

    private static String HangmanJudge(String word, char[] guess) {
        int left = 6;
        String successRet = "You win.";
        String loseRet = "You lose.";
        String abortRet = "You chickened out.";
        char[] w = word.toCharArray();
        Set<Character> success = new HashSet<>();
        Set<Character> all = new HashSet<>();
        for (char c : w) {
            all.add(c);
        }
        int sum = all.size();
        for (char c : guess) {
            if (left == 0) {
                break;
            }
            boolean r = false;
            if (all.contains(c) && success.add(c)) {
                r = true;
                printGuessSuccess(w, success);
            }
            if (!r) {
                left--;
            }
        }
        System.out.println("left:" + left);
        if (success.size() == sum) {
            return successRet;
        } else {
            if (left > 0) {
                return abortRet;
            } else {
                return loseRet;
            }
        }
    }

    private static void printGuessSuccess(char[] w, Set<Character> success) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : w) {
            if (success.contains(c)) {
                stringBuilder = stringBuilder.append(c);
            } else {
                stringBuilder = stringBuilder.append("*");
            }
        }
        System.out.println(stringBuilder.toString());
    }

    private static String generatorOne() {
        int len = Util.getRandomInteger(2, 100);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            stringBuilder = stringBuilder.append(seeds[Util.getRandomInteger(0, seeds.length - 1)]);
        }
        return stringBuilder.toString();
    }
}
