package ACM.TheRotationGame;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/9/27
 * Time:10:20
 * <p>
 * 如图7-20所示形状的棋盘上分别有8个1、2、3，要往A～H方向旋转棋盘，使中间8个方
 * 格数字相同。图7-20（a）进行A操作后变为图7-20（b），再进行C操作后变为图7-
 * 20（c），这正是一个目标状态（因为中间8个方格数字相同）。要求旋转次数最少。如果有
 * 多解，操作序列的字典序应尽量小。
 * <p>
 * The Rotation Game, Shanghai 2004, UVa1343
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=4089
 * <p>
 * Sample Input
 * 1 1 1 1 3 2 3 2 3 1 3 2 2 3 1 2 2 2 3 1 2 1 3 3
 * 1 1 1 1 1 1 1 1 2 2 2 2 2 2 2 2 3 3 3 3 3 3 3 3
 * 0
 * Sample Output
 * AC
 * 2
 * DDHH
 * 2
 */
public class Solution {
    public static void main(String[] args) {
        new Solution().case1();
        new Solution().case2();
    }

    private void case1() {
        board("111132323132231222312133");
    }

    private void case2() {
        board("111111112222222233333333");
    }

    private void board(String descString) {
        System.out.println("descString:" + descString);
        long s = System.currentTimeMillis();
        print(descString);
        System.out.println("time:" + (System.currentTimeMillis() - s));
        System.out.println("--------------------------------------------");
    }

    private void print(String descString) {
        char[] chars = descString.toCharArray();
        StringBuilder builder = new StringBuilder();
        print(builder, chars[0], chars[1]);
        print(builder, chars[2], chars[3]);
        print(builder, 4, 10, chars);
        print(builder, chars[11], chars[12]);
        print(builder, 13, 19, chars);
        print(builder, chars[20], chars[21]);
        print(builder, chars[22], chars[23]);
        System.out.println(builder.toString());
    }

    private void print(StringBuilder builder, char a, char b) {
        builder = builder.append("  ");
        builder = builder.append(a);
        builder = builder.append(" ");
        builder = builder.append(b);
        builder.append("\n");
    }

    private void print(StringBuilder builder, int s, int e, char[] array) {
        for (int i = s; i <= e; i++) {
            builder = builder.append(array[i]);
        }
        builder.append("\n");
    }
}
