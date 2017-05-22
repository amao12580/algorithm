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
 * <p>
 * 相较于上一版本的解法，这一版的思路是，直接模拟最后一个球的下落，前面的球被跳过处理，极大的加快运算
 *
 * 非常关键的improvement
 */
public class SolutionV2 {
    public static void main(String[] args) {
        SolutionV2 solution = new SolutionV2();
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
        int index = last(binaryTree, num % 2 != 0);
        System.out.println(",last:" + index);
    }

    private int last(BinaryTree<Integer> binaryTree, boolean isOdd) {
        return last(binaryTree.getRoot(), -1, isOdd);
    }

    private int last(Node<Integer> root, int preValue, boolean isOdd) {
        if (root == null) {
            return preValue;
        }
        if (isOdd) {
            root = root.getLeft();
        } else {
            root = root.getRight();
        }
        if (root == null) {
            return preValue;
        }
        return last(root, root.getValue(), isOdd);
    }

    private int nextNumber = 0;

    private int getNextNumber() {
        return ++nextNumber;
    }

    private void reset() {
        this.nextNumber = 0;
    }


    private BinaryTree<Integer> createBinaryTreeByDepth(int depth) {
        Node<Integer> root = new Node<>(getNextNumber());
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
        nodes.clear();
        return binaryTree;
    }


    private void createNextLeftAndRight(Node<Integer> root) {
        root.setLeft(new Node<>(getNextNumber()));
        root.setRight(new Node<>(getNextNumber()));
    }
}
