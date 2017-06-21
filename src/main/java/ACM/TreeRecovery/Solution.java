package ACM.TreeRecovery;

import basic.dataStructure.tree.binary.BinaryTree;
import basic.dataStructure.tree.binary.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/6/17
 * Time:17:42
 * <p>
 * 输入一棵二叉树的先序遍历和中序遍历序列，输出后序遍历序列，如图6-20所示。
 * <p>
 * Tree Recovery, ULM 1997, UVa 536
 * <p>
 * Sample Input
 * DBACEGF ABCDEFG
 * BCAD CBAD
 * Sample Output
 * ACBFGED
 * CDAB
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("---------------------------------");
        solution.case2();
    }

    private void case1() {
        afterOrder("DBACEGF", "ABCDEFG");
    }


    private void case2() {
        afterOrder("BCAD", "CBAD");
    }

    private void afterOrder(String behindOrder, String middleOrder) {
        int len = behindOrder.length();
        if (middleOrder.length() != len) {
            throw new IllegalArgumentException();
        }
        BinaryTree<Character> tree = new BinaryTree<>();
        Map<Character, Integer> behindIndex = new HashMap<>();
        char2Index(behindIndex, behindOrder, len);
        Map<Character, Integer> middleIndex = new HashMap<>();
        char2Index(middleIndex, middleOrder, len);
        createTree(tree, behindOrder, behindIndex, middleOrder, middleIndex, len);
        afterOrder(tree);
    }

    private void char2Index(Map<Character, Integer> index, String string, int len) {
        for (int i = 0; i < len; i++) {
            index.put(string.charAt(i), i);
        }
    }

    private void afterOrder(BinaryTree<Character> tree) {
        StringBuilder builder = new StringBuilder();
        afterOrder(tree.getRoot(), builder);
        System.out.println(builder.toString());
    }

    private void afterOrder(Node<Character> root, StringBuilder builder) {
        Node<Character> left = root.getLeft();
        if (left != null) {
            afterOrder(left, builder);
        }
        Node<Character> right = root.getRight();
        if (right != null) {
            afterOrder(right, builder);
        }
        builder.append(root.getValue());
    }

    private void createTree(BinaryTree<Character> tree, String behindOrder, Map<Character, Integer> behindIndex, String middleOrder, Map<Character, Integer> middleIndex, int len) {
        char rootValue = behindOrder.charAt(0);
        Node<Character> root = new Node<>(rootValue);
        tree.setRoot(root);
        Integer p = middleIndex.get(rootValue);
        if (p == null) {
            throw new IllegalArgumentException();
        }
        createTree(root, behindIndex, 0, p - 1, middleOrder, middleIndex, p + 1, len - 1, len - 1);
    }

    private void createTree(Node<Character> root, Map<Character, Integer> behindIndex, int leftBeginIndex, int leftEndIndex, String middleOrder,
                            Map<Character, Integer> middleIndex, int rightBeginIndex, int rightEndIndex, int maxEndIndex) {
        if (leftBeginIndex < 0 || rightBeginIndex < 0 || leftEndIndex > maxEndIndex || rightEndIndex > maxEndIndex) {
            return;
        }
        char leftRootValue = getNextRoot(middleOrder, leftBeginIndex, leftEndIndex, behindIndex);
        if (leftRootValue != ' ') {
            Node<Character> leftRoot = new Node<>(leftRootValue);
            root.setLeft(leftRoot);
            Integer lp = middleIndex.get(leftRootValue);
            if (lp == null) {
                throw new IllegalArgumentException();
            }
            createTree(leftRoot, behindIndex, leftBeginIndex, lp - 1, middleOrder, middleIndex, lp + 1, leftEndIndex, maxEndIndex);
        }
        char rightRootValue = getNextRoot(middleOrder, rightBeginIndex, rightEndIndex, behindIndex);
        if (rightRootValue != ' ') {
            Node<Character> rightRoot = new Node<>(rightRootValue);
            root.setRight(rightRoot);
            Integer rp = middleIndex.get(rightRootValue);
            if (rp == null) {
                throw new IllegalArgumentException();
            }
            createTree(rightRoot, behindIndex, rightBeginIndex, rp - 1, middleOrder, middleIndex, rp + 1, rightEndIndex, maxEndIndex);
        }
    }

    private char getNextRoot(String middleOrder, int beginIndex, int endIndex, Map<Character, Integer> behindIndex) {
        int index = middleOrder.length() - 1;
        int c;
        char c1 = ' ';
        for (int i = beginIndex; i <= endIndex; i++) {
            c = behindIndex.get(middleOrder.charAt(i));
            if (c <= index) {
                index = c;
                c1 = middleOrder.charAt(i);
            }
        }
        return c1;
    }
}
