package ACM.TheFallingLeaves;

import basic.dataStructure.tree.binary.BinaryTree;
import basic.dataStructure.tree.binary.Node;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/23
 * Time:16:15
 * <p>
 * 给一棵二叉树，每个结点都有一个水平位
 * 置：左子结点在它左边1个单位，右子结点在右
 * 边1个单位。从左向右输出每个水平位置的所有
 * 结点的权值之和。如图6-7所示，从左到右的3个
 * 位置的权和分别为7，11，3。按照递归（先序）
 * 方式输入，用-1表示空树。
 * <p>
 * 样例输入：
 * 5 7 -1 6 -1 -1 3 -1 -1
 * 8 2 9 -1 -1 6 5 -1 -1 12 -1 -1 3 7 -1 -1 -1
 * -1
 * 样例输出：
 * Case 1:
 * 7 11 3
 * Case 2:
 * 9 7 21 15
 * <p>
 * <p>
 * The Falling Leaves, UVa 699
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
    }

    private void case1() {
        int[][] trees = {{5, 7, -1, 6, -1, -1, 3, -1, -1}, {8, 2, 9, -1, -1, 6, 5, -1, -1, 12, -1, -1, 3, 7, -1, -1, -1}};
        for (int[] tree : trees) {
            position(tree);
            System.out.println("------------------------------------------------------------------------------------------");
        }
    }

    private void position(int[] treeArray) {
        LinkedList<Integer> treeList = new LinkedList<>();
        for (int item : treeArray) {
            treeList.add(item);
        }
        BinaryTree<Integer> tree = new BinaryTree<>();
        Integer rootValue = treeList.pollFirst();
        if (rootValue == -1) {
            throw new IllegalArgumentException();
        }
        Node<Integer> root = new Node<>(rootValue);
        tree.setRoot(root);
        Map<Integer, List<Node<Integer>>> offsetNodes = new HashMap<>();
        addOne(offsetNodes, 0, root);
        createTree(root, treeList, 0, offsetNodes);
        System.out.println(Arrays.toString(reduce(offsetNodes)));
    }

    private long[] reduce(Map<Integer, List<Node<Integer>>> offsetNodes) {
        int len = offsetNodes.size();
        long[] result = new long[len];
        Set<Integer> offsets = offsetNodes.keySet();
        List<Integer> list = new ArrayList<>(len);
        list.addAll(offsets);
        Collections.sort(list);
        for (int i = 0; i < len; i++) {
            result[i] = reduce(offsetNodes.get(list.get(i)));
        }
        return result;
    }

    private long reduce(List<Node<Integer>> nodes) {
        int sum = 0;
        for (Node<Integer> node : nodes) {
            sum += node.getValue();
        }
        return sum;
    }

    private void addOne(Map<Integer, List<Node<Integer>>> offsetNodes, int offset, Node<Integer> node) {
        List<Node<Integer>> nodes = offsetNodes.get(offset);
        if (nodes == null) {
            nodes = new ArrayList<>();
            nodes.add(node);
            offsetNodes.put(offset, nodes);
        } else {
            nodes.add(node);
        }
    }

    /**
     * 先序遍历，反向构建二叉树
     */
    private void createTree(Node<Integer> root, LinkedList<Integer> treeList, int offset, Map<Integer, List<Node<Integer>>> offsetNodes) {
        if (treeList.isEmpty()) {
            return;
        }
        Integer leftValue = treeList.pollFirst();
        if (leftValue != -1) {
            Node<Integer> left = new Node<>(leftValue);
            root.setLeft(left);
            addOne(offsetNodes, offset - 1, left);
            createTree(left, treeList, offset - 1, offsetNodes);
        }
        Integer rightValue = treeList.pollFirst();
        if (rightValue != -1) {
            Node<Integer> right = new Node<>(rightValue);
            root.setRight(right);
            addOne(offsetNodes, offset + 1, right);
            createTree(right, treeList, offset + 1, offsetNodes);
        }
    }
}
