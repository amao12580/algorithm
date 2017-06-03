package ACM.UndrawTheTrees;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/6/1
 * Time:20:25
 * <p>
 * 你的任务是将多叉树转化为括号表示法。如图6-16所示，每个结点用除了“-”、“|”和空格
 * 的其他字符表示，每个非叶结点的正下方总会有一个“|”字符，然后下方是一排“-”字符，恰
 * 好覆盖所有子结点的上方。单独的一行“#”为数据结束标记。
 * <p>
 * Undraw the Trees, UVa 10562
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1503
 * <p>
 * Sample Input
 * 2
 * A
 * |
 * --------
 * B C D
 * | |
 * ----- -
 * E F G
 * #
 * e
 * |
 * ----
 * f g
 * #
 * Sample Output
 * (A(B()C(E()F())D(G())))
 * (e(f()g()))
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("----------------------------------------------------");
        solution.case2();
    }

    private void case1() {//(A(B()C(E()F())D(G())))
        String[] desc = {"    A   ", "    |   ", "--------", "B  C   D", "   |   |",
                " ----- -", " E   F G"};
        drawTree(desc);
    }

    private void case2() {//(e(f()g()))
        String[] desc = {"e", "|", "----", "f g"};
        drawTree(desc);
    }


    private void drawTree(String[] desc) {
        StringBuilder builder = new StringBuilder("(");
        drawTree(builder, desc);
        builder = builder.append(")");
        System.out.println("tree:" + builder.toString());
    }

    private void drawTree(StringBuilder builder, String[] desc) {
        if (desc == null) {
            return;
        }
        drawTree(builder, desc, desc.length - 1, 0, desc[0].length() - 1, 0);
    }

    private void drawTree(StringBuilder builder, String[] desc, int sumIndex, int beginIndex, int endIndex, int rowIndex) {
        String line = desc[rowIndex];
        int b, e = -1, e2;
        while (beginIndex <= endIndex) {
            beginIndex = findNodePosition(builder, line, beginIndex, endIndex);//每个节点字符长度为1
            if (beginIndex >= 0) {
                builder = builder.append("(");
                if (rowIndex + 3 <= sumIndex && desc[rowIndex + 1].charAt(beginIndex) == '|') {
                    String line2 = desc[rowIndex + 2];
                    if (line2.indexOf(' ') < 0) {
                        b = 0;
                        e = line2.length() - 1;
                    } else {
                        b = line2.indexOf('-', e + 1);
                        e2 = line2.indexOf(' ', b + 1);
                        e = e2 > 0 ? e2 - 1 : line2.length() - 1;
                    }
                    drawTree(builder, desc, sumIndex, b, e, rowIndex + 3);
                }
                builder.append(")");
                beginIndex++;
            } else {
                break;
            }
        }
    }

    private int findNodePosition(StringBuilder builder, String line, int beginIndex, int endIndex) {
        if (line == null) {
            return -1;
        }
        if (beginIndex > endIndex) {
            return -1;
        }
        if (endIndex > line.length() - 1) {
            endIndex = line.length() - 1;
        }
        char c;
        char[] chars = line.toCharArray();
        for (int i = beginIndex; i <= endIndex; i++) {
            c = chars[i];
            if (c != ' ' && c != '|' && c != '-') {
                builder.append(c);
                return i;
            }
        }
        return -1;
    }
}
