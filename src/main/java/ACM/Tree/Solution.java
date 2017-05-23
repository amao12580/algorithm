package ACM.Tree;

import basic.dataStructure.tree.binary.BinaryTree;
import basic.dataStructure.tree.binary.Node;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/22
 * Time:16:34
 * <p>
 * 给一棵点带权（权值各不相同，都是小于10000的正整数）的二叉树的中序和后序遍
 * 历，找一个叶子使得它到根的路径上的权和最小。如果有多解，该叶子本身的权应尽量小。
 * 输入中每两行表示一棵树，其中第一行为中序遍历，第二行为后序遍历。
 * 样例输入：
 * 3 2 1 4 5 7 6
 * 3 1 2 5 6 7 4
 * 7 8 11 3 5 16 12 18
 * 8 3 11 7 16 18 12 5
 * 255
 * 255
 * 样例输出：
 * 1
 * 3
 * 255
 * <p>
 * Tree, UVa 548
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
    }

    private void case1() {
        String[][] trees = {{"3 2 1 4 5 7 6", "3 1 2 5 6 7 4"}, {"7 8 11 3 5 16 12 18", "8 3 11 7 16 18 12 5"}, {"255", "255"}};
        for (String[] tree : trees) {
            find(tree);
            System.out.println("----------------------------------------------------------------------------");
        }
    }

    private void find(String[] treeStr) {
        System.out.println(Arrays.toString(treeStr));
        String[] ms = treeStr[0].split(" ");
        int mlen = ms.length;
        int[] middleArray = new int[mlen];
        Map<Integer, Integer> middle = parse(ms, mlen, middleArray);
        String[] as = treeStr[1].split(" ");
        int[] afterArray = new int[mlen];
        Map<Integer, Integer> after = parse(as, mlen, afterArray);
        BinaryTree<Integer> tree = new BinaryTree<>();
        tree.setRoot(new Node<>(afterArray[mlen - 1]));
        createTree(tree, middle, middleArray, after, afterArray, mlen);
        List<WeightLeafNode> nodes = findLeafNode(tree);
        Collections.sort(nodes);
        System.out.println(nodes.get(0).getValue());
    }

    private List<WeightLeafNode> findLeafNode(BinaryTree<Integer> tree) {
        List<WeightLeafNode> leafNodes = new ArrayList<>();
        findLeafNode(tree.getRoot(), leafNodes, 0);
        return leafNodes;
    }

    private void findLeafNode(Node<Integer> root, List<WeightLeafNode> leafNodes, int currentSum) {
        if (root == null) {
            return;
        }
        int value = root.getValue();
        currentSum += value;
        if (root.isLeafNode()) {
            leafNodes.add(new WeightLeafNode(value, currentSum));
            return;
        }
        findLeafNode(root.getLeft(), leafNodes, currentSum);
        findLeafNode(root.getRight(), leafNodes, currentSum);
    }

    private void createTree(BinaryTree<Integer> tree, Map<Integer, Integer> middle, int[] middleArray, Map<Integer, Integer> after, int[] afterArray, int len) {
        Node<Integer> root = tree.getRoot();
        int rootIndex = middle.get(root.getValue());
        if (rootIndex == 0) {
            return;
        }
        createTree(root, middle, middleArray, after, afterArray, 0, rootIndex - 1, rootIndex + 1, len - 1);
    }

    private class WeightLeafNode implements Comparable<WeightLeafNode> {
        private int value;
        private long sum;

        public WeightLeafNode(int value, long sum) {
            this.value = value;
            this.sum = sum;
        }

        public int getValue() {
            return value;
        }

        public long getSum() {
            return sum;
        }

        @Override
        public int compareTo(WeightLeafNode other) {
            if (this.getSum() != other.getSum()) {
                return this.getSum() - other.getSum() < 0 ? -1 : 1;
            }
            return this.getValue() - other.getValue();
        }
    }

    private void createTree(Node<Integer> root, Map<Integer, Integer> middle, int[] middleArray, Map<Integer, Integer> after, int[] afterArray,
                            int leftTreeBeginIndex, int leftTreeEndIndex, int rightTreeBeginIndex, int rightTreeEndIndex) {
        int leftIndex = findNextRootInMiddleIndex(leftTreeBeginIndex, leftTreeEndIndex, middle, middleArray, after, afterArray);
        Node<Integer> left = null;
        if (leftIndex >= 0) {
            left = new Node<>(middleArray[leftIndex]);
            root.setLeft(left);
        }
        int rightIndex = findNextRootInMiddleIndex(rightTreeBeginIndex, rightTreeEndIndex, middle, middleArray, after, afterArray);
        Node<Integer> right = null;
        if (rightIndex >= 0) {
            right = new Node<>(middleArray[rightIndex]);
            root.setRight(right);
        }
        if (leftIndex < 0 && rightIndex < 0) {
            return;
        }
        createTree(left, middle, middleArray, after, afterArray, leftTreeBeginIndex, leftIndex - 1, leftIndex + 1, leftTreeEndIndex);
        createTree(right, middle, middleArray, after, afterArray, rightTreeBeginIndex, rightIndex - 1, rightIndex + 1, rightTreeEndIndex);
    }

    private int findNextRootInMiddleIndex(int beginIndex, int endIndex, Map<Integer, Integer> middle, int[] middleArray, Map<Integer, Integer> after, int[] afterArray) {
        int leftIndex = -1;
        int afterIndex = -1;
        int afterIndexMaybe;
        for (int i = beginIndex; i <= endIndex; i++) {
            afterIndexMaybe = after.get(middleArray[i]);
            if (afterIndexMaybe > afterIndex) {
                afterIndex = afterIndexMaybe;
            }
        }
        if (afterIndex >= 0) {
            leftIndex = middle.get(afterArray[afterIndex]);
        }
        return leftIndex;
    }

    private Map<Integer, Integer> parse(String[] ss, int len, int[] array) {
        Map<Integer, Integer> result = new LinkedHashMap<>();
        for (int i = 0; i < len; i++) {
            int w = Integer.valueOf(ss[i]);
            result.put(w, i);
            array[i] = w;
        }
        return result;
    }
}
