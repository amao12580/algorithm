package ACM.OilDeposits;

import basic.dataStructure.graph.Graph;
import basic.dataStructure.graph.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/24
 * Time:11:36
 * <p>
 * 输入一个m行n列的字符矩阵，统计字符“@”组成多少个八连块。如果两个字符“@”所在
 * 的格子相邻（横、竖或者对角线方向），就说它们属于同一个八连块。例如，图6-9中有两
 * 个八连块。
 * <p>
 * Sample Input
 * 1 1
 * <p>
 * 3 5
 *
 * @*@* *@**
 * @*@* 1 8
 * @@****@* 5 5
 * ***@
 * @@*@
 * @**@
 * @@@*@
 * @@**@ 0 0
 * Sample Output
 * 0
 * 1
 * 2
 * 2
 * <p>
 * Oil Deposits, UVa 572
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
    }

    private void case1() {
        String[][] oils = {
                {"*"},
                {"*@*@*", "**@**", "*@*@*"},
                {"@@****@*"},
                {"****@", "*@@*@", "*@**@", "@@@*@", "@@**@"}

        };
        for (String[] oilDesc : oils) {
            check(oilDesc);
            System.out.println("----------------------------------------------");
        }
    }

    private Set<Point> points = new HashSet<>();
    private List<Graph> graphs = new ArrayList<>();

    private static final char key = '@';

    private void check(String[] oilDesc) {
        print(oilDesc);
        int endIndex = oilDesc.length - 1;
        int cEndIndex = oilDesc[0].length() - 1;
        for (int i = 0; i <= endIndex; i++) {
            for (int j = 0; j <= cEndIndex; j++) {
                createGraph(i, j, endIndex, cEndIndex, oilDesc);
            }
        }
        System.out.println(graphs.size());
        graphs.clear();
        points.clear();
    }

    private void createGraph(int i, int j, int iMax, int jMax, String[] oilDesc) {
        Graph graph = new Graph();
        createGraph(i, j, iMax, jMax, oilDesc, graph);
        if (!graph.isEmptyPoints() && isNewGraph(graph)) {
            graphs.add(graph);
        }
    }

    private boolean isNewGraph(Graph graph) {
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

    private void createGraph(int i, int j, int iMax, int jMax, String[] oilDesc, Graph graph) {
        if (!inRange(i, j, iMax, jMax) || oilDesc[i].charAt(j) != key) {
            return;
        }
        Point point = new Point(i, j);
        if (!points.add(point)) {
            return;
        }
        graph.addPoint(point);
        //右上
        createGraph(i - 1, j + 1, iMax, jMax, oilDesc, graph);
        //正上
        createGraph(i - 1, j, iMax, jMax, oilDesc, graph);
        //左上
        createGraph(i - 1, j - 1, iMax, jMax, oilDesc, graph);
        //正左
        createGraph(i, j - 1, iMax, jMax, oilDesc, graph);
        //左下
        createGraph(i + 1, j - 1, iMax, jMax, oilDesc, graph);
        //正下
        createGraph(i + 1, j, iMax, jMax, oilDesc, graph);
        //右下
        createGraph(i + 1, j + 1, iMax, jMax, oilDesc, graph);
        //正右
        createGraph(i, j + 1, iMax, jMax, oilDesc, graph);
    }

    private void print(String[] oilDesc) {
        for (String s : oilDesc) {
            char[] chars = s.toCharArray();
            for (char c : chars) {
                System.out.print(c + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
