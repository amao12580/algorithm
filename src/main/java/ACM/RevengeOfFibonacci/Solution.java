package ACM.RevengeOfFibonacci;

import java.math.BigInteger;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/12
 * Time:18:29
 * <p>
 * Fibonacci数的定义为：F(0)=F(1)=1，然后从F(2)开始，F(i)=F(i-1)+F(i-2)。例如，前10
 * 项Fibonacci数分别为1, 1, 2, 3, 5, 8, 13, 21, 34, 55……
 * 有一天晚上，你梦到了Fibonacci，它告诉你一个有趣的Fibonacci数。醒来以后，你只记
 * 得了它的开头几个数字。你的任务是找出以它开头的最小Fibonacci数的序号。例如以12开头
 * 的最小Fibonacci数是F(25)。输入不超过40个数字，输出满足条件的序号。
 * 如果序号小于100000的Fibonacci数均不满足条件，输出-1。
 * <p>
 * <p>
 * Revenge of Fibonacci, ACM/ICPC Shanghai 2011, UVa12333
 */
public class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        long s = System.currentTimeMillis();
        solution.createFibonacci(100000);
        System.out.println("tire tree create time:" + (System.currentTimeMillis() - s) + "ms");
        solution.case1();
    }

    private void case1() {
        BigInteger[] values = new BigInteger[]{
                BigInteger.valueOf(1),
                BigInteger.valueOf(12),
                BigInteger.valueOf(123), BigInteger.valueOf(1234),
                BigInteger.valueOf(12345), BigInteger.valueOf(9),
                BigInteger.valueOf(98), BigInteger.valueOf(987),
                BigInteger.valueOf(9876), BigInteger.valueOf(98765),
                BigInteger.valueOf(89), BigInteger.valueOf(32),
                new BigInteger("51075176167176176176"), BigInteger.valueOf(347746739),
                BigInteger.valueOf(5610)};
        Map<Byte, Node> nodes = fibonacci.getNodes();
        for (BigInteger item : values) {
            find(nodes, item);
        }
    }

    private void find(Map<Byte, Node> nodes, BigInteger valueStart) {
        String valueStartStr = valueStart.toString();
        int len = valueStartStr.length();
        int result;
        if (len > 40) {
            result = -1;
        } else if (len == 1) {
            result = fibonacciLittle.containsKey(valueStart.intValue()) ? fibonacciLittle.get(valueStart.intValue()) : -1;
            if (result == -1) {
                result = find(nodes, valueStartStr);
            }
        } else {
            result = find(nodes, valueStartStr);
        }
        System.out.println(valueStart + "," + result);
    }

    private int find(Map<Byte, Node> nodes, String valueStartStr) {
        int len = valueStartStr.length();
        int endIndex = len - 1;
        byte c;
        Node node;
        for (int i = 0; i <= endIndex; i++) {
            c = Byte.valueOf(String.valueOf(valueStartStr.charAt(i)));
            if (nodes != null) {
                node = nodes.get(c);
                if (node != null) {
                    if (i == endIndex) {
                        if (node.canPause()) {
                            return node.getIndex();
                        } else {
                            if (node.haveNext()) {
                                return findShortestPause(node.next());
                            } else {
                                return -1;
                            }
                        }
                    } else {
                        nodes = node.next();
                    }
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        }
        return 0;
    }

    private int findShortestPause(Map<Byte, Node> nodes) {
        List<Integer> list = new ArrayList<>();
        findShortestPause(list, nodes);
        if (list.isEmpty()) {
            return -1;
        } else {
            if (list.size() > 1) {
                Collections.sort(list);
            }
            return list.get(0);
        }
    }

    private void findShortestPause(List<Integer> results, Map<Byte, Node> nodes) {
        Collection<Node> list = nodes.values();
        for (Node node : list) {
            if (node.canPause()) {
                results.add(node.getIndex());
            } else {
                if (node.haveNext()) {
                    findShortestPause(results, node.next());
                }
            }
        }
    }

    private TrieTree fibonacci = new TrieTree();
    private Map<Integer, Integer> fibonacciLittle = new HashMap<>();

    private void createFibonacci(int endIndex) {
        BigInteger fp2 = BigInteger.valueOf(1);
        BigInteger fp1 = BigInteger.valueOf(1);
        fibonacciLittle.put(fp1.intValue(), 1);
        fibonacciLittle.put(fp2.intValue(), 0);
        BigInteger split = BigInteger.valueOf(10);
        for (int i = 2; i <= endIndex; i++) {
            BigInteger fi = fp1.add(fp2);
            fp1 = fp2;
            fp2 = fi;
            if (fi.compareTo(split) >= 0) {
                appendTrieTree(i, fi);
            } else {
                fibonacciLittle.put(fi.intValue(), i);
            }
        }
    }

    private void appendTrieTree(int index, BigInteger element) {
        String elementStr = element.toString();
        int len = elementStr.length();
        if (index % 10000 == 0) {
            System.out.println(index + "," + elementStr);
        }
        int endIndex = len - 1;
        if (len > 40) {
            endIndex = 39;
            elementStr = elementStr.substring(0, 40);
        }
        Map<Byte, Node> nodes = fibonacci.getNodes();
        Node pre = null;
        Node myNode;
        byte c;
        Node node;
        char[] chars = elementStr.toCharArray();
        for (int i = 0; i <= endIndex; i++) {
            c = Byte.valueOf(String.valueOf(chars[i]));
            if (nodes != null) {
                node = nodes.get(c);
                if (node != null) {
                    pre = node;
                    nodes = node.next();
                } else {
                    myNode = new Node(c, i == endIndex ? index : null);
                    nodes.put(c, myNode);
                    if (i != endIndex) {
                        pre = myNode;
                        nodes = myNode.next();
                    }
                }
            } else {
                myNode = new Node(c, i == endIndex ? index : null);
                pre.appendNext(myNode);
                if (i != endIndex) {
                    pre = myNode;
                    nodes = myNode.next();
                }
            }
        }
    }

    private class TrieTree {
        private Map<Byte, Node> nodes = new HashMap<>(9);

        public Map<Byte, Node> getNodes() {
            return nodes;
        }
    }

    private class Node {
        private byte value;
        private Integer index;
        private Map<Byte, Node> next;


        public Node(byte value, Integer index) {
            this.value = value;
            this.index = index;
        }

        public boolean canPause() {
            return index != null;
        }

        public boolean haveNext() {
            return !next.isEmpty();
        }

        public Map<Byte, Node> next() {
            return next;
        }

        public byte getValue() {
            return value;
        }

        public Integer getIndex() {
            return index;
        }

        public void appendNext(Node node) {
            if (next == null) {
                next = new HashMap<>(10);
            }
            next.put(node.getValue(), node);
        }
    }
}
