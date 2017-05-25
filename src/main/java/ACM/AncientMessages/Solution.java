package ACM.AncientMessages;

import basic.Util;
import basic.dataStructure.graph.Graph;
import basic.dataStructure.graph.Point;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/24
 * Time:16:39
 * 本题的目的是识别3000年前古埃及用到的6种象形文字，如图6-10所示。
 * <p>
 * 每组数据包含一个H行W列的字符矩阵（H≤200，W≤50），每个字符为4个相邻像素点的
 * 十六进制（例如，10011100对应的字符就是9c）。转化为二进制后1表示黑点，0表示白点。
 * 输入满足：
 * 不会出现上述6种符号之外的其他符号。
 * 输入至少包含一个符号，且每个黑像素都属于一个符号。
 * 每个符号都是一个四连块，并且不同符号不会相互接触，也不会相互包含。
 * 如果两个黑像素有公共顶点，则它们一定有一个相同的相邻黑像素（有公共边）。
 * 符号的形状一定和表6-9中的图形拓扑等价（可以随意拉伸但不能拉断）。
 * 要求按照字典序输出所有符号。例如，图6-11中的输出应为AKW。
 * <p>
 * Ancient Messages, World Finals 2011, UVa 1103
 * <p>
 * Sample Input
 * 100 25
 * 0000000000000000000000000
 * 0000000000000000000000000
 * ...(50 lines omitted)...
 * 00001fe0000000000007c0000
 * 00003fe0000000000007c0000
 * ...(44 lines omitted)...
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 150 38
 * 00000000000000000000000000000000000000
 * 00000000000000000000000000000000000000
 * ...(75 lines omitted)...
 * 0000000003fffffffffffffffff00000000000
 * 0000000003fffffffffffffffff00000000000
 * ...(69 lines omitted)...
 * 00000000000000000000000000000000000000
 * 00000000000000000000000000000000000000
 * 0 0
 * Sample Output
 * Case 1: AKW
 * Case 2: AAAAA
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3544
 * <p>
 * 建议看原题
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("--------------------------------------------------------------------");

        solution.case2();
        System.out.println("--------------------------------------------------------------------");

        solution.case3();
        System.out.println("--------------------------------------------------------------------");

        solution.case4();
        System.out.println("--------------------------------------------------------------------");
    }

    private void case4() {//A K W
        String[] desc = new String[17];
        desc[0] = "0f0f0";
        desc[1] = "0f0f0";
        desc[2] = "fffff";
        desc[3] = "00f00";
        desc[4] = "00f00";

        desc[5] = "00000";

        desc[6] = "0fff0";
        desc[7] = "0f0f0";
        desc[8] = "fffff";
        desc[9] = "00f00";
        desc[10] = "00f00";

        desc[11] = "00000";

        desc[12] = "0fff0";
        desc[13] = "0f0f0";
        desc[14] = "0fff0";
        desc[15] = "0f0f0";
        desc[16] = "0fff0";
        message(desc);
    }

    private void case3() {//W
        String[] desc = new String[5];
        desc[0] = "0f0f0";
        desc[1] = "0f0f0";
        desc[2] = "fffff";
        desc[3] = "00f00";
        desc[4] = "00f00";
        message(desc);
    }

    private void case2() {//A
        String[] desc = new String[5];
        desc[0] = "0fff0";
        desc[1] = "0f0f0";
        desc[2] = "fffff";
        desc[3] = "00f00";
        desc[4] = "00f00";
        message(desc);
    }

    private void case1() {//K
        String[] desc = new String[5];
        desc[0] = "fff";
        desc[1] = "f0f";
        desc[2] = "fff";
        desc[3] = "f0f";
        desc[4] = "fff";
        message(desc);
    }


    private static Map<Integer, Character> keys = new HashMap<>();

    static {
        keys.put(1, 'A');
        keys.put(3, 'J');
        keys.put(5, 'D');
        keys.put(4, 'S');
        keys.put(0, 'W');
        keys.put(2, 'K');
    }

    private List<Character> result = new ArrayList<>();

    private List<Graph> blackGraphs = new ArrayList<>();

    private void message(String[] strings) {
        print(strings);
        Boolean[][] desc = expand(strings);
        print(desc);
        find(desc);
        output();
        result.clear();
        blackGraphs.clear();
    }

    private void find(Boolean[][] desc) {
        int endIndex = desc.length - 1;
        int cEndIndex = desc[0].length - 1;
        createGraph(0, 0, endIndex, cEndIndex, desc);//将外围的白点消除
        print(desc);
        endIndex--;
        cEndIndex--;
        for (int i = 1; i <= endIndex; i++) {
            for (int j = 1; j <= cEndIndex; j++) {
                createGraph(i, j, endIndex, cEndIndex, desc);
            }
        }
    }

    private void createGraph(int i, int j, int iMax, int jMax, Boolean[][] desc) {
        if (desc[i][j] != null) {
            boolean isBlack = desc[i][j];
            Graph graph = new Graph();
            List<Graph> otherGraphs = new ArrayList<>();
            createGraph(i, j, iMax, jMax, desc, graph, otherGraphs, isBlack);
            if (isBlack && !graph.isEmptyPoints() && isNewGraph(blackGraphs, graph)) {
                blackGraphs.add(graph);
                fillResult(otherGraphs);
            }
        }
    }

    private void fillResult(List<Graph> thisWhiteGraphs) {
        Character character = keys.get(thisWhiteGraphs.size());
        if (character == null) {
            throw new IllegalArgumentException(String.valueOf(thisWhiteGraphs.size()));
        }
        result.add(character);
    }

    private boolean isNewGraph(List<Graph> graphs, Graph graph) {
        for (Graph item : graphs) {
            if (graph.isOverlap(item)) {
                return false;
            }
        }
        return true;
    }

    private boolean inRange(int i, int j, int iMax, int jMax) {
        return i >= 0 && i <= iMax && j >= 0 && j <= jMax;
    }

    private void createGraph(int i, int j, int iMax, int jMax, Boolean[][] desc, Graph graph, List<Graph> thisWhiteGraphs, boolean isBlack) {
        if (!inRange(i, j, iMax, jMax) || desc[i][j] == null || (!isBlack && desc[i][j])) {
            return;
        }
        if (isBlack && !desc[i][j]) {//处于黑点查找模式，但是发现了白点，这肯定是存在内部白色区域了
            Graph whiteGraph = new Graph();
            createGraph(i, j, iMax, jMax, desc, whiteGraph, thisWhiteGraphs, false);
            if (!whiteGraph.isEmptyPoints() && isNewGraph(thisWhiteGraphs, whiteGraph)) {//过滤掉已经构建好的白色图
                thisWhiteGraphs.add(whiteGraph);
            }
        }
        graph.addPoint(new Point(i, j));
        desc[i][j] = null;
        //正上
        createGraph(i - 1, j, iMax, jMax, desc, graph, thisWhiteGraphs, isBlack);
        //正左
        createGraph(i, j - 1, iMax, jMax, desc, graph, thisWhiteGraphs, isBlack);
        //正下
        createGraph(i + 1, j, iMax, jMax, desc, graph, thisWhiteGraphs, isBlack);
        //正右
        createGraph(i, j + 1, iMax, jMax, desc, graph, thisWhiteGraphs, isBlack);
    }

    private void output() {
        if (result.isEmpty()) {
            System.out.println("Not found.");
        } else {
            if (result.size() == 1) {
                System.out.println(result.get(0));
                return;
            }
            String[] ret = new String[result.size()];
            int index = 0;
            for (Character c : result) {
                ret[index++] = String.valueOf(c);
            }
            System.out.println(Arrays.toString(Util.orderByDictionaryASC(ret)));
        }
    }

    private void print(Boolean[][] desc) {
        StringBuilder builder;
        for (Boolean[] bs : desc) {
            builder = new StringBuilder(bs.length);
            for (Boolean b : bs) {
                builder.append(b == null ? " " : b ? "*" : ".");
            }
            System.out.println(builder.toString());
        }
        System.out.println();
    }

    private Boolean[][] expand(String[] desc) {
        int len = desc.length;
        int nLen = len + 2;
        int nCLen = (desc[0].length() << 2) + 2;
        boolean[][] result = new boolean[nLen][nCLen];
        int j;
        char[] chars;
        boolean[] pixel;
        for (int i = 0; i < len; i++) {
            j = 1;
            chars = desc[i].toCharArray();
            for (char c : chars) {
                pixel = expand(c);
                for (int k = 0; k < 4; k++) {
                    result[i + 1][j++] = pixel[k];
                }
            }
        }
        Boolean[][] finalResult = new Boolean[nLen][nCLen];
        for (int i = 0; i < nLen; i++) {
            for (int k = 0; k < nCLen; k++) {
                finalResult[i][k] = result[i][k];
            }
        }
        return finalResult;
    }

    private static final boolean[] empty = new boolean[4];

    private static Map<Character, boolean[]> cache = new HashMap<>();

    private boolean[] expand(char c) {
        if (c == '0') {
            return empty;
        }
        boolean[] exist = cache.get(c);
        if (exist != null) {
            return exist;
        }
        boolean[] result = new boolean[4];
        char[] chars = String.format("%04d", Integer.valueOf(Integer.toBinaryString(Integer.valueOf(String.valueOf(c), 16)))).toCharArray();
        int index = 0;
        for (char i : chars) {
            if (i == '1') {
                result[index] = true;
            }
            index++;
            if (index == 4) {
                break;
            }
        }
        cache.put(c, result);
        return result;
    }

    private void print(String[] desc) {
        char[] chars;
        for (String s : desc) {
            chars = s.toCharArray();
            for (char c : chars) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
