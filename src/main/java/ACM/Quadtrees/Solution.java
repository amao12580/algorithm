package ACM.Quadtrees;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/23
 * Time:18:40
 * <p>
 * 如图6-8所示，可以用四分树来表示一个黑白图像，方法是用根结点表示整幅图像，然
 * 后把行列各分成两等分，按照图中的方式编号，从左到右对应4个子结点。如果某子结点对
 * 应的区域全黑或者全白，则直接用一个黑结点或者白结点表示；如果既有黑又有白，则用一
 * 个灰结点表示，并且为这个区域递归建树。
 * <p>
 * 给出两棵四分树的先序遍历，求二者合并之后（黑色部分合并）黑色像素的个数。p表
 * 示中间结点，f表示黑色（full），e表示白色（empty）。
 * 样例输入：
 * 3
 * ppeeefpffeefe
 * pefepeefe
 * peeef
 * peefe
 * peeef
 * peepefefe
 * 样例输出：
 * There are 640 black pixels.
 * There are 512 black pixels.
 * There are 384 black pixels.
 * <p>
 * Quadtrees, UVa 297
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=233
 * <p>
 * 建议看原题，有一个关键点：图片是32*32=1024个像素的
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
    }

    private void case1() {
        String[][] trees = {{"ppeeefpffeefe", "pefepeefe"}, {"peeef", "peefe"}, {"peeef", "peepefefe"}};
        for (String[] tree : trees) {
            pixel(tree);
            System.out.println("---------------------------------------------------------------------");
        }
    }

    private void pixel(String[] trees) {
        System.out.println(Arrays.toString(trees));
        Quadtree left = createTree(trees[0]);
        Quadtree right = createTree(trees[1]);
        Quadtree sum = left.plus(right);
        System.out.println("There are " + sum.countBlack() + " black pixels.");
    }

    private Quadtree createTree(String treeDesc) {
        char[] chars = treeDesc.toCharArray();
        LinkedList<Character> nodeList = new LinkedList<>();
        for (char c : chars) {
            nodeList.add(c);
        }
        Quadtree tree = new Quadtree();
        Node root = new Node(nodeList.pollFirst());
        tree.setRoot(root);
        createTree(root, nodeList);
        return tree;
    }

    private static final char key = 'p';

    private static final int measure = 32;

    /**
     * 先序遍历，反向构建四叉树
     */
    private void createTree(Node root, LinkedList<Character> treeList) {
        if (treeList.isEmpty()) {
            return;
        }
        Character rightUpValue = treeList.pollFirst();
        Node rightUp = new Node(rightUpValue);
        root.setRightUp(rightUp);
        if (rightUpValue == key) {
            createTree(rightUp, treeList);
        }

        Character leftUpValue = treeList.pollFirst();
        Node leftUp = new Node(leftUpValue);
        root.setLeftUp(leftUp);
        if (leftUpValue == key) {
            createTree(leftUp, treeList);
        }

        Character leftDownValue = treeList.pollFirst();
        Node leftDown = new Node(leftDownValue);
        root.setLeftDown(leftDown);
        if (leftDownValue == key) {
            createTree(leftDown, treeList);
        }

        Character rightDownValue = treeList.pollFirst();
        Node rightDown = new Node(rightDownValue);
        root.setRightDown(rightDown);
        if (rightDownValue == key) {
            createTree(rightDown, treeList);
        }
    }

    private class Quadtree {
        private Node root;

        public void setRoot(Node root) {
            this.root = root;
        }

        public Node getRoot() {
            return root;
        }

        public Quadtree plus(Quadtree other) {
            //this-left    other-right
            Quadtree result = new Quadtree();
            result.setRoot(this.getRoot().plus(other.getRoot()));
            return result;
        }

        public int countBlack() {
            int sum = 0;
            return this.getRoot().countBlack(sum, measure);
        }
    }

    private class Node {
        private Boolean color;//null=中间节点，true=黑色，false=白色；
        private Node rightUp;
        private Node leftUp;

        private Node leftDown;
        private Node rightDown;

        public Node(Boolean color) {
            this.color = color;
        }

        public Node(char color) {
            if (color == 'p') {
                this.color = null;
            } else if (color == 'f') {
                this.color = true;
            } else if (color == 'e') {
                this.color = false;
            }
        }

        public void setRightUp(Node rightUp) {
            this.rightUp = rightUp;
        }

        public void setLeftUp(Node leftUp) {
            this.leftUp = leftUp;
        }

        public void setLeftDown(Node leftDown) {
            this.leftDown = leftDown;
        }

        public void setRightDown(Node rightDown) {
            this.rightDown = rightDown;
        }

        public Node getRightUp() {
            return rightUp != null ? rightUp : new Node(this.color);
        }

        public Node getLeftUp() {
            return leftUp != null ? leftUp : new Node(this.color);
        }

        public Node getLeftDown() {
            return leftDown != null ? leftDown : new Node(this.color);
        }

        public Node getRightDown() {
            return rightDown != null ? rightDown : new Node(this.color);
        }

        public boolean isBlack() {
            return this.color != null && this.color;
        }

        public boolean isWhite() {
            return this.color != null && !this.color;
        }

        public Node plus(Node other) {
            if (this.isBlack() || other.isBlack()) {
                return new Node(true);
            }

            if (this.isWhite() && other.isWhite()) {
                return new Node(false);
            }
            Node node = new Node(null);
            node.setRightUp(this.getRightUp().plus(other.getRightUp()));
            node.setLeftUp(this.getLeftUp().plus(other.getLeftUp()));
            node.setLeftDown(this.getLeftDown().plus(other.getLeftDown()));
            node.setRightDown(this.getRightDown().plus(other.getRightDown()));
            return node;
        }

        public int countBlack(int sum, int measure) {
            if (this.isBlack()) {
                sum += measure * measure;
                return sum;
            }
            if (this.isWhite()) {
                return sum;
            }
            sum += this.getRightUp().countBlack(0, measure >> 1);
            sum += this.getLeftUp().countBlack(0, measure >> 1);
            sum += this.getLeftDown().countBlack(0, measure >> 1);
            sum += this.getRightDown().countBlack(0, measure >> 1);
            return sum;
        }
    }
}
