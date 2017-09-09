package ACM.MobileComputing;

import basic.Util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/8/3
 * Time:17:22
 * <p>
 * 给出房间的宽度r和s个挂坠的重量w i 。设计一个尽量宽（但宽度不能超过房间宽度r）的
 * 天平，挂着所有挂坠。
 * 天平由一些长度为1的木棍组成。木棍的每一端要么挂一个挂坠，要么挂另外一个木
 * 棍。如图7-9所示，设n和m分别是两端挂的总重量，要让天平平衡，必须满足n*a=m*b。
 * <p>
 * 例如，如果有3个重量分别为1, 1, 2的挂坠，有3种平衡的天平，如图7-10所示。
 * <p>
 * 挂坠的宽度忽略不计，且不同的子天平可以相互重叠。如图7-11所示，宽度为(1/3)+1+
 * (1/4)。
 * 输入第一行为数据组数。每组数据前两行为房间宽度r和挂坠数
 * 目s（0<r<10，1≤s≤6）。以下s行每行为一个挂坠的重量W i （1≤weight i ≤1000）。输入保证不存在
 * 天平的宽度恰好在r-10 -5 和r+10 -5 之间（这样可以保证不会出现精度问题）。对于每组数据，
 * 输出最优天平的宽度。如果无解，输出-1。你的输出和标准答案的绝对误差不应超过10 -8 。
 * <p>
 * <p>
 * Mobile Computing, ACM/ICPC Tokyo 2005, UVa1354
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=onlinejudge&page=show_problem&problem=4100
 * <p>
 * Sample Input
 * 5
 * 1.3
 * 3
 * 1
 * 2
 * 1
 * 1.4
 * 3
 * 1
 * 2
 * 1
 * 2.0
 * 3
 * 1
 * 2
 * 1
 * 1.59
 * 4
 * 2
 * 1
 * 1
 * 3
 * 1.7143
 * 4
 * 1
 * 2
 * 3
 * 5
 * Sample Output
 * -1
 * 1.3333333333333335
 * 1.6666666666666667
 * 1.5833333333333335
 * 1.7142857142857142
 */
public class Solution {
    public static void main(String[] args) {
        new Solution().case1();
        new Solution().case2();
        new Solution().case3();
        new Solution().case4();
        new Solution().case5();
        new Solution().case6();
    }

    private void case1() {
        double[] W = {1, 2, 1};
        computing(1.3, 3, W);
    }

    private void case2() {
        double[] W = {1, 2, 1};
        computing(1.4, 3, W);
    }

    private void case3() {
        double[] W = {1, 2, 1};
        computing(2.0, 3, W);
    }

    private void case4() {
        double[] W = {2, 1, 1, 3};
        computing(1.59, 4, W);
    }

    private void case5() {
        double[] W = {1, 2, 3, 5};
        computing(1.7143, 4, W);
    }

    private void case6() {
        double r = Util.getRandomDouble(0.001, 9.999);
        int s = Util.getRandomInteger(2, 6);
        double[] W = new double[s];
        for (int i = 0; i < s; i++) {
            W[i] = Util.getRandomDouble(1, 1000);
        }
        computing(r, s, W);
    }

    /**
     * 房间的宽度r和s个挂坠的重量w i
     *
     * @param r 房间的宽度
     * @param s s个挂坠
     * @param w 每个挂坠的重量
     *          <p>
     *          1.最笨的办法，求出所有组合
     *          <p>
     *          2.优化：尝试剪枝
     */
    private void computing(double r, int s, double[] w) {
        System.out.println("-----------------------------------");
        System.out.println("r:" + r + ",s:" + s + ",w:" + Arrays.toString(w));
        this.limit = r;
        this.weight = w;
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < s; i++) {
            nodes.add(new Node(i));
        }
        computing(new boolean[s], new Node(), nodes);
        print();
    }

    private void print() {
        System.out.println(globalMax);
    }

    private double[] weight = null;
    private double limit = 0;
    private double globalMax = -1;

    private void computing(boolean[] array, Node tree, List<Node> nodes) {
        if (nodes.size() == 1) {
            double width = nodes.get(0).width();
            if (width < limit && width > globalMax) {
                globalMax = width;
            }
            return;
        }
        Node currentTree, currentNode;
        List<Node> currentNodes;
        int len = array.length;
        for (int i = 0; i < len; i++) {
            if (!array[i]) {
                array[i] = true;
                currentTree = tree.clone();
                currentNodes = new ArrayList<>(nodes);
                currentNode = currentNodes.get(i);
                currentTree.appendNext(currentNode);
                if (currentTree.isFull()) {
                    System.out.println(currentTree.toString() + "   " + currentTree.width());
                    if (currentTree.width() < limit) {
                        remove(currentNodes, currentTree.getNext());
                        currentNodes.add(currentTree);
                        computing(new boolean[currentNodes.size()], new Node(), currentNodes);
                    }
                } else {
                    computing(array, currentTree, currentNodes);
                }
                array[i] = false;
            }
        }
    }

    private void remove(List<Node> nodes, List<Node> target) {
        nodes.removeAll(target);
    }

    private static final BigDecimal len = new BigDecimal(1);
    private static final double minLen = 1.0E-6;

    /**
     * 给出左右两个挂坠质量，求天平平衡时，左右两边的长度值
     */
    private double[] computing(double left, double right) {
        BigDecimal lb = new BigDecimal(left);
        BigDecimal lrb = new BigDecimal(right + left);
        double[] pair = new double[2];
        BigDecimal rc = len.multiply(lb).divide(lrb, 6, BigDecimal.ROUND_HALF_UP);
        pair[0] = len.subtract(rc).doubleValue();//左边
        pair[1] = rc.doubleValue();//右边
        if ((left >= right ? pair[0] : pair[1]) < minLen) {
            System.out.println("Deny,len:" + len.doubleValue() + ",left:" + left + ",right:"
                    + right + ",pair[0]:" + pair[0] + ",pair[1]:" + pair[1]);
            throw new IllegalArgumentException();
        }
        return pair;
    }

    private int no = 1;

    private int getNextNo() {
        return no++;
    }

    private double getWeight0(int index) {
        return this.weight[index];
    }

    class Node implements Cloneable {
        private int no = 0;
        private Integer index;
        private LinkedList<Node> next = new LinkedList<>();
        private double[] pair;
        private double weight;

        public Node() {
            this.no = getNextNo();
        }

        public Node(Integer index) {
            this.no = getNextNo();
            this.index = index;
            if (index != null) {
                this.weight = getWeight0(index);
            }
        }

        public Node(Integer index, LinkedList<Node> next) {
            this.no = getNextNo();
            this.index = index;
            if (index != null) {
                this.weight = getWeight0(index);
            }
            this.next = new LinkedList<>(next);
        }

        public Integer getIndex() {
            return index;
        }

        public LinkedList<Node> getNext() {
            return next;
        }

        double[] getPair() {
            return pair;
        }

        double getWeight() {
            return weight;
        }

        boolean isFull() {
            return next.size() == 2;
        }

        @Override
        protected Node clone() {
            return new Node(this.getIndex(), this.getNext());
        }

        void appendNext(Node node) {
            if (isFull()) {
                throw new IllegalArgumentException("No space.");
            }
            this.next.add(node);
            if (isFull()) {
                this.weight = this.next.peekFirst().getWeight() + this.next.peekLast().getWeight();
                pair = computing(this.next.peekFirst().getWeight(), this.next.peekLast().getWeight());
            }
        }

        double width() {
            return width(this, null);
        }

        double width(Node node, Boolean isLeft) {
            double len = 0;
            double[] pair = node.getPair();
            LinkedList<Node> nodes = node.getNext();
            Node left = nodes.peekFirst();
            if ((isLeft == null || isLeft) && left != null) {
                len += pair[0] + width(left, true);
            }
            Node right = nodes.peekLast();
            if ((isLeft == null || !isLeft) && right != null) {
                len += pair[1] + width(right, false);
            }
            return len;
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o != null && getClass() == o.getClass() && no == ((Node) o).no;
        }

        @Override
        public int hashCode() {
            return no;
        }

        @Override
        public String toString() {
            return buildToString(this, new StringBuilder(""));
        }

        private String buildToString(Node node, StringBuilder desc) {
            desc = desc.append(node.index == null ? "(" : "");
            desc = desc.append(node.index == null ? "" : node.index);
            LinkedList<Node> nodes = node.getNext();
            boolean isSpilt = false;
            for (Node next : nodes) {
                desc = desc.append(buildToString(next, new StringBuilder("")));
                if (!isSpilt) {
                    isSpilt = true;
                    desc = desc.append(",");
                }
            }
            desc = desc.append(node.index == null ? ")" : "");
            return desc.toString();
        }
    }
}