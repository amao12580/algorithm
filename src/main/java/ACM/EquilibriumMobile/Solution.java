package ACM.EquilibriumMobile;

import basic.dataStructure.tree.binary.BinaryTree;
import basic.dataStructure.tree.binary.Node;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/7/12
 * Time:11:18
 * <p>
 * 给一个深度不超过16的二叉树，代表一个天平。每根杆都悬挂在中间，每个秤砣的重量
 * 已知。至少修改多少个秤砣的重量才能让天平平衡？如图6-23所示，把7改成3即可。
 * <p>
 * Equilibrium Mobile, NWERC 2008, UVa12166
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3318
 * <p>
 * Sample Input
 * 3
 * [[3,7],6]
 * 40
 * [[2,3],[4,5]]
 * Sample Output
 * 1
 * 0
 * 3
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        solution.case2();
        solution.case3();
        solution.case4();
    }

    private void case1() {
        change("[[3,7],6]");
    }

    private void case2() {
        change("40");
    }

    private void case3() {
        change("[[2,3],[4,5]]");
    }


    private void case4() {
        change("[[[10,[[10,[250,890]],[20,[10,8]]]],30],[4,50]]");
    }


    private void change(String desc) {
        Map<Integer, List<Node<Integer>>> nodes = new HashMap<>();
        BinaryTree<Integer> tree = createTree(desc, nodes);
        List<Integer> changes = new ArrayList<>();
        change(tree, nodes, changes);
        Collections.sort(changes);
        System.out.println(changes.get(0));
    }

    private void change(BinaryTree<Integer> tree, Map<Integer, List<Node<Integer>>> nodes, List<Integer> changes) {
        Node<Integer> root = tree.getRoot();
        if (root.getValue() > 0) {
            changes.add(0);
            return;
        }
        List<Node<Integer>> currentLevelNodes;
        for (Map.Entry<Integer, List<Node<Integer>>> entry : nodes.entrySet()) {
            currentLevelNodes = entry.getValue();
            changes.addAll(currentLevelNodes.stream().map(node -> change(tree, node, entry.getKey())).collect(Collectors.toList()));
        }
    }

    private Integer change(BinaryTree<Integer> tree, Node<Integer> node, int level) {
        return change(tree.getRoot(), node.getValue() * ((int) Math.pow(2, level - 1)), 0);
    }

    private int change(Node<Integer> node, int sum, int count) {
        sum >>= 1;
        Node<Integer> left = node.getLeft();
        if (left.getValue() > 0) {
            if (left.getValue() != sum) {
                count++;
            }
        } else {
            count = change(left, sum, count);
        }
        Node<Integer> right = node.getRight();
        if (right.getValue() > 0) {
            if (right.getValue() != sum) {
                count++;
            }
        } else {
            count = change(right, sum, count);
        }
        return count;
    }

    private BinaryTree<Integer> createTree(String desc, Map<Integer, List<Node<Integer>>> nodes) {
        int level = 1;
        BinaryTree<Integer> tree = new BinaryTree<>();
        if (desc.startsWith("[") && desc.endsWith("]")) {
            Node<Integer> root = new Node<>(-1);
            tree.setRoot(root);
            String childDesc = desc.substring(1, desc.length() - 1);
            int p = indexSplit(childDesc);
            if (p >= 0) {
                Node<Integer> left = new Node<>(-1);
                root.setLeft(left);
                createTree(childDesc.substring(0, p), left, nodes, level + 1);
                Node<Integer> right = new Node<>(-1);
                root.setRight(right);
                createTree(childDesc.substring(p + 1), right, nodes, level + 1);
            }
        } else {
            Node<Integer> node = new Node<>(Integer.valueOf(desc));
            addNode(nodes, node, level);
            tree.setRoot(node);
        }
        return tree;
    }

    private void addNode(Map<Integer, List<Node<Integer>>> nodes, Node<Integer> node, int level) {
        List<Node<Integer>> cell = nodes.get(level);
        if (cell == null) {
            cell = new ArrayList<>();
            nodes.put(level, cell);
        }
        cell.add(node);
    }

    private void createTree(String desc, Node<Integer> node, Map<Integer, List<Node<Integer>>> nodes, int level) {
        if (desc.startsWith("[") && desc.endsWith("]")) {
            String childDesc = desc.substring(1, desc.length() - 1);
            int p = indexSplit(childDesc);
            if (p >= 0) {
                Node<Integer> left = new Node<>(-1);
                node.setLeft(left);
                createTree(childDesc.substring(0, p), left, nodes, level + 1);
                Node<Integer> right = new Node<>(-1);
                node.setRight(right);
                createTree(childDesc.substring(p + 1), right, nodes, level + 1);
            }
        } else {
            node.setValue(Integer.valueOf(desc));
            addNode(nodes, node, level);
        }
    }

    private int indexSplit(String desc) {
        if (!desc.startsWith("[")) {
            return desc.indexOf(",");
        }
        int ls = -1;
        int rs = -1;
        int offset = 0;
        char[] chars = desc.toCharArray();
        for (char c : chars) {
            if (c == '[') {
                if (ls < 0) {
                    ls = 0;
                }
                ls++;
            }

            if (c == ']') {
                if (rs < 0) {
                    rs = 0;
                }
                rs++;
            }

            if (rs > 0 && rs == ls) {
                return offset + 1;
            }
            offset++;
        }
        return -1;
    }
}
