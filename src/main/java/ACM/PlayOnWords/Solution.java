package ACM.PlayOnWords;

import basic.Util;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/31
 * Time:17:56
 * <p>
 * 输入n（n≤100000）个单词，是否可以把所有这些单词排成一个序列，使得每个单词的
 * 第一个字母和上一个单词的最后一个字母相同（例如acm、malform、mouse）。每个单词最
 * 多包含1000个小写字母。输入中可以有重复单词。
 * <p>
 * Play On Words, UVa 10129
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1070
 * <p>
 * Sample Input
 * 3
 * 2
 * acm
 * ibm
 * 3
 * acm
 * malform
 * mouse
 * 2
 * ok
 * ok
 * Sample Output
 * The door cannot be opened.
 * Ordering is possible.
 * The door cannot be opened.
 * <p>
 * 关于欧拉道路（from Titanium大神）：
 * <p>
 * 判断有向图是否有欧拉路
 * <p>
 * 1.判断有向图的基图（即有向图转化为无向图）连通性，用简单的DFS即可。如果图都不连通，一定不存在欧拉路
 * <p>
 * 2.在条件1的基础上
 * <p>
 * 对于欧拉回路，要求苛刻一点，所有点的入度都要等于出度，那么就存在欧拉回路了
 * <p>
 * 对于欧拉道路，要求松一点，只有一个点，出度比入度大1，这个点一定是起点； 一个点，入度比出度大1，这个点一定是终点.其余点的出度等于入度
 * <p>
 * （注意，只能大1，而且这样的点分别只能有1个,而且存在起点就一定要存在终点，存在终点就一定要存在起点）
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("--------------------------------------------------");
        solution.case2();
        System.out.println("--------------------------------------------------");
        solution.case3();
        System.out.println("--------------------------------------------------");
        solution.case4();
        System.out.println("--------------------------------------------------");
        solution.case5();
    }

    private void case1() {
        String[] strings = {"acm", "ibm"};
        Util.shuffleArray(strings);
        link(strings);
    }

    private void case2() {
        String[] strings = {"acm", "malform", "mouse"};
        Util.shuffleArray(strings);
        link(strings);
    }

    private void case3() {
        String[] strings = {"ok", "ok"};
        Util.shuffleArray(strings);
        link(strings);
    }

    private void case4() {
        String[] strings = {"acm", "malform", "mouse", "mom", "mail", "large", "english", "map", "point", "tomcat", "app", "pmp", "ppt"};
        Util.shuffleArray(strings);
        link(strings);
    }

    private void case5() {
        int len = Util.getRandomInteger(10, 100000 / 100);
        String[] strings = new String[len];
        String end = "";
        for (int i = 0; i < len; i++) {
            String base = Util.generateLowerLetterString(Util.getRandomInteger(1, 1000 / 100));
            if (Util.getRandomBoolean()) {
                base = end.concat(base);
                end = String.valueOf(base.charAt(base.length() - 1));
            }
            strings[i] = base;
        }
        Util.shuffleArray(strings);
        link(strings);
    }

    private void link(String[] strings) {
        System.out.println(Arrays.toString(strings));
        String noWay = "The door cannot be opened.";
        String haveWay = "Ordering is possible.";

        Map<Character, List<Edge>> starts = new HashMap<>();
        Map<Character, List<Edge>> ends = new HashMap<>();
        Map<String, Edge> edges = new HashMap<>();
        char s, e;
        for (String str : strings) {
            s = str.charAt(0);
            e = str.charAt(str.length() - 1);
            addOne(edges, starts, s, e, s, str);
            addOne(edges, ends, s, e, e, str);
        }
        int[] in = new int[26];//26个字母
        int[] out = new int[26];//26个字母

        DFS(in, out, starts, ends, strings[0].charAt(0));
        if (!checkConnectivity(strings, in, out)) {
            System.out.println(noWay);
            return;
        }
        if (!checkEulerianPath(in, out)) {
            System.out.println(noWay);
            return;
        }
        System.out.println(haveWay);
    }

    private boolean checkEulerianPath(int[] ins, int[] outs) {
        int oddSum = 0;
        int in, out;
        for (int i = 0; i < 25; i++) {
            if (ins[i] != outs[i]) {
                if (ins[i] > outs[i]) {
                    in = ins[i] - outs[i];
                    if (in != 1) {
                        return false;
                    }
                }
                if (ins[i] < outs[i]) {
                    out = outs[i] - ins[i];
                    if (out != 1) {
                        return false;
                    }
                }
                oddSum++;
            }
        }
        return oddSum <= 2;
    }

    private boolean checkConnectivity(String[] strings, int[] in, int[] out) {
        int sum = 0;
        for (int item : in) {
            sum += item;
        }
        for (int item : out) {
            sum += item;
        }
        return strings.length * 2 == sum;
    }

    private void DFS(int[] in, int[] out, Map<Character, List<Edge>> starts, Map<Character, List<Edge>> ends, char key) {
        List<Edge> startEdges = getEdges(starts, key);
        startEdges.stream().filter(edge -> !edge.isUse()).forEach(edge -> {
            useEdge(edge, in, out);
            DFS(in, out, starts, ends, edge.getEnd());
        });
        List<Edge> endEdges = getEdges(ends, key);
        endEdges.stream().filter(edge -> !edge.isUse()).forEach(edge -> DFS(in, out, starts, ends, edge.getStart()));
    }

    private void useEdge(Edge edge, int[] in, int[] out) {
        String str = edge.getValue();
        useEdge(str.charAt(0), out);
        useEdge(str.charAt(str.length() - 1), in);
        edge.setIsUse();
    }

    private void useEdge(char key, int[] points) {
        points[key - 97]++;
    }

    private List<Edge> getEdges(Map<Character, List<Edge>> map, char key) {
        return map.containsKey(key) ? map.get(key) : new ArrayList<>();
    }

    private void addOne(Map<String, Edge> edges, Map<Character, List<Edge>> map, char s, char e, char key, String str) {
        Edge edge = edges.containsKey(str) ? edges.get(str) : new Edge(s, e, str);
        List<Edge> list = map.get(key);
        if (list == null) {
            list = new ArrayList<>();
            list.add(edge);
            map.put(key, list);
        } else {
            list.add(edge);
        }
        edges.put(str, edge);
    }

    private class Edge {
        private boolean isUse;
        private char start;
        private char end;
        private String value;

        public char getStart() {
            return start;
        }

        public char getEnd() {
            return end;
        }

        public boolean isUse() {
            return isUse;
        }

        public void setIsUse() {
            this.isUse = true;
        }

        public String getValue() {
            return value;
        }

        public Edge(char start, char end, String value) {
            this.start = start;
            this.end = end;
            this.value = value;
        }


        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o instanceof Edge) {
                Edge other = (Edge) o;
                return this.hashCode() == other.hashCode();
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
