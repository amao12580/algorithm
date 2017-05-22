package ACM.TreesOnTheLevel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/19
 * Time:19:11
 * <p>
 * 输入一棵二叉树，你的任务是按从上到下、从左到右的顺序输出各个结点的值。每个结
 * 点都按照从根结点到它的移动序列给出（L表示左，R表示右）。在输入中，每个结点的左
 * 括号和右括号之间没有空格，相邻结点之间用一个空格隔开。每棵树的输入用一对空括
 * 号“()”结束（这对括号本身不代表一个结点），如图6-3所示。
 * <p>
 * 注意，如果从根到某个叶结点的路径上有的结点没有在输入中给出，或者给出超过一
 * 次，应当输出-1。结点个数不超过256。
 * 样例输入：
 * (11,LL) (7,LLL) (8,R) (5,) (4,L) (13,RL) (2,LLR) (1,RRR) (4,RR) ()
 * (3,L) (4,R) ()
 * 样例输出：
 * 5 4 8 11 13 4 7 2 1
 * -1
 * <p>
 * <p>
 * Trees on the level, Duke 1993, UVa 122
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
    }

    private void case1() {
        String[] treeString = {"(11,LL) (7,LLL) (8,R) (5,) (4,L) (13,RL) (2,LLR) (1,RRR) (4,RR) ()", "(3,L) (4,R) ()"};
        for (String str : treeString) {
            getValues(str);
            System.out.println("---------------------------------------------------");
        }
    }

    private void getValues(String treeString) {
        LinkedList<NodePrint> list = parse(treeString);
        if (list.isEmpty()) {
            System.out.println("No node.");
            return;
        }
        NodePrint first = list.pollFirst();
        if (!first.isRoot()) {
            System.out.println("-1");
            return;
        }
        Map<String, NodePrint> path2nodes = new HashMap<>();
        for (NodePrint node : list) {
            String path = node.getPath();
            if (path == null) {
                throw new IllegalArgumentException();
            }
            if (path2nodes.containsKey(path)) {
                path2nodes.put(path, new NodePrint(-1, path));
            } else {
                path2nodes.put(path, node);
            }
        }
        createTree(first, path2nodes);
    }

    private void createTree(NodePrint root, Map<String, NodePrint> path2nodes) {
        LinkedList<NodePrint> nodes = new LinkedList<>();
        root.setPath("");
        nodes.add(root);
        System.out.print(root.getValue() + " ");
        createTree(nodes, path2nodes);
        System.out.println();
    }

    private void createTree(LinkedList<NodePrint> nodes, Map<String, NodePrint> path2nodes) {
        String path;
        NodePrint node;
        NodePrint left;
        NodePrint right;
        while (!nodes.isEmpty()) {
            node = nodes.pollFirst();
            path = node.getPath();
            String lk = path + "L";
            left = path2nodes.get(lk);
            if (left != null) {
                System.out.print(left.getValue() + " ");
                nodes.addLast(left);
            }
            String rk = path + "R";
            right = path2nodes.get(rk);
            if (right != null) {
                System.out.print(right.getValue() + " ");
                nodes.addLast(right);
            }
        }
    }

    private class NodePrint {
        private int value;
        private String path;

        public NodePrint(Integer value, String path) {
            if (path != null && path.isEmpty()) {
                path = null;
            }
            this.path = path;
            this.value = value;
        }

        public String getPath() {
            return path;
        }

        public int getValue() {
            return value;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public boolean isRoot() {
            return path == null;
        }
    }

    private LinkedList<NodePrint> parse(String treeString) {
        LinkedList<NodePrint> nodes = new LinkedList<>();
        parse(treeString, nodes, 0, treeString.length() - 1);
        return nodes;
    }

    private void parse(String treeString, LinkedList<NodePrint> nodes, int beginIndex, int endIndex) {
        if (beginIndex > endIndex) {
            return;
        }
        int pb = treeString.indexOf("(", beginIndex);
        if (pb < 0) {
            return;
        }
        int pe = treeString.indexOf(")", beginIndex);
        if (pe < 0) {
            return;
        }
        int pp = treeString.indexOf(",", beginIndex);
        if (pp < 0) {
            return;
        }
        String path = treeString.substring(pp + 1, pe);
        if (path.isEmpty()) {
            nodes.addFirst(new NodePrint(Integer.valueOf(treeString.substring(pb + 1, pp)), path));
        } else {
            nodes.add(new NodePrint(Integer.valueOf(treeString.substring(pb + 1, pp)), path));
        }
        parse(treeString, nodes, pe + 2, endIndex);
    }
}
