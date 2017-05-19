package ACM.DroppingBalls;

import basic.dataStructure.tree.binary.BinaryTree;
import basic.dataStructure.tree.binary.Node;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/19
 * Time:10:50
 * <p>
 * 有一棵二叉树，最大深度为D，且所有叶子的深度都相同。所有结点从上到下从左到右
 * 编号为1, 2, 3,…, 2 D -1。在结点1处放一个小球，它会往下落。每个内结点上都有一个开关，
 * 初始全部关闭，当每次有小球落到一个开关上时，状态都会改变。当小球到达一个内结点
 * 时，如果该结点上的开关关闭，则往左走，否则往右走，直到走到叶子结点，如图6-2所
 * 示。
 * <p>
 * 一些小球从结点1处依次开始下落，最后一个小球将会落到哪里呢？输入叶子深度D和
 * 小球个数I，输出第I个小球最后所在的叶子编号。假设I不超过整棵树的叶子个数。D≤20。
 * 输入最多包含1000组数据。
 * 样例输入：
 * 4 2
 * 3 4
 * 10 1
 * 2 2
 * 8 128
 * 16 12345
 * 样例输出：
 * 12
 * 7
 * 512
 * 3
 * 255
 * 36358
 * <p>
 * Dropping Balls, UVa 679
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
    }

    private void case1() {
        int[][] seeds = {{4, 2}, {3, 4}, {10, 1}, {2, 2}, {8, 128}, {16, 12345}};
        for (int[] item : seeds) {
            drop(item[0], item[1]);
            reset();
        }
    }

    private void drop(int depth, int num) {
        System.out.print("depth:" + depth + ",num:" + num);
        BinaryTree<Integer> binaryTree = createBinaryTreeByDepth(depth);
        for (int i = 0; i < num; i++) {
            int index = last(binaryTree);
            if (i == num - 1) {
                System.out.println(",last:" + index);
            }
        }
    }

    private int last(BinaryTree<Integer> binaryTree) {
        return last((SwitchNode<Integer>) binaryTree.getRoot(), -1);
    }

    private int last(SwitchNode<Integer> root, int preValue) {
        if (root == null) {
            return preValue;
        }
        if (root.isClose()) {
            root.change();
            root = (SwitchNode<Integer>) root.getLeft();
        } else {
            root.change();
            root = (SwitchNode<Integer>) root.getRight();
        }
        if (root == null) {
            return preValue;
        }
        return last(root, root.getValue());
    }

    private int nextNumber = 0;

    private int getNextNumber() {
        return ++nextNumber;
    }

    private void reset() {
        this.nextNumber = 0;
    }

    private class SwitchNode<T> extends Node<T> {
        private boolean isOpen;

        public SwitchNode(T value) {
            super(value);
        }

        public boolean isClose() {
            return !isOpen;
        }

        public void change() {
            this.isOpen = !this.isOpen;
        }
    }


    private BinaryTree<Integer> createBinaryTreeByDepth(int depth) {
        SwitchNode<Integer> root = new SwitchNode<>(getNextNumber());
        BinaryTree<Integer> binaryTree = new BinaryTree<>();
        binaryTree.setRoot(root);
        LinkedList<Node<Integer>> nodes = new LinkedList<>();
        nodes.add(root);
        Node<Integer> node;
        int max = (1 << depth) - 1;
        while (this.nextNumber < max) {
            node = nodes.pollFirst();
            createNextLeftAndRight(node);
            nodes.add(node.getLeft());
            nodes.add(node.getRight());
        }
        return binaryTree;
    }


    private void createNextLeftAndRight(Node<Integer> root) {
        root.setLeft(new SwitchNode<>(getNextNumber()));
        root.setRight(new SwitchNode<>(getNextNumber()));
    }
}
