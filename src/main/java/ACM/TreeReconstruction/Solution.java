package ACM.TreeReconstruction;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/7/14
 * Time:16:47
 * <p>
 * 输入一个n（n≤1000）结点树的BFS序列和DFS序列，你的任务是输出每个结点的子结点
 * 列表。输入序列（不管是BFS还是DFS）是这样生成的：当一个结点被扩展时，其所有子结
 * 点应该按照编号从小到大的顺序访问。
 * 例如，若BFS序列为4 3 5 1 2 8 7 6，DFS序列为4 3 1 7 2 6 5 8，则一棵满足条件的树如图
 * 6-29所示。
 * <p>
 * Tree Reconstruction, UVa 10410
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1351
 * <p>
 * Sample Input
 * 8
 * 4 3 5 1 2 8 7 6
 * 4 3 1 7 2 6 5 8
 * Sample Output
 * 1: 7
 * 2: 6
 * 3: 1 2
 * 4: 3 5
 * 5: 8
 * 6:
 * 7:
 * 8:
 * <p>
 * http://blog.csdn.net/accelerator_/article/details/38148161
 * <p>
 * unfinished,skip.
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("-------------------------------------");
        solution.case2();
    }

    private void case1() {
        tree(8, "4 3 5 1 2 8 7 6 ", "4 3 1 7 2 6 5 8 ");
    }

    private void case2() {
        tree(8, "1 2 3 4 5 6 7 8 ", "1 2 4 7 8 3 5 6 ");
    }

    private void tree(int n, String BFS, String DFS) {
        Map<Integer, Integer> BFSIndex = node2Index(BFS);
        Map<Integer, Integer> DFSIndex = node2Index(DFS);
        Map<Integer, List<Integer>> node2Child = new HashMap<>();
        for (int i = 0; i < n; i++) {
            node2Child.put(i + 1, new LinkedList<>());
        }
        index(n, BFSIndex, DFSIndex, node2Child);
        printIndex(node2Child);
    }

    private void printIndex(Map<Integer, List<Integer>> node2Child) {
        for (Map.Entry<Integer, List<Integer>> entry : node2Child.entrySet()) {
            System.out.println(entry.getKey() + "：" + printIndex(entry.getValue()));
        }
    }

    private String printIndex(List<Integer> child) {
        StringBuilder builder = new StringBuilder();
        for (Integer item : child) {
            builder = builder.append(item).append(" ");
        }
        return builder.toString();
    }

    private void index(int n, Map<Integer, Integer> bfsIndex, Map<Integer, Integer> dfsIndex, Map<Integer, List<Integer>> node2Child) {
        Integer[] bfsNodeArray = new Integer[n];
        bfsNodeArray = bfsIndex.keySet().toArray(bfsNodeArray);
        Integer[] dfsNodeArray = new Integer[n];
        dfsNodeArray = dfsIndex.keySet().toArray(dfsNodeArray);
        int end = n - 1;
        index(0, 1, end, bfsIndex, bfsNodeArray, dfsIndex, dfsNodeArray, node2Child, n - 1);
    }

    private void index(int currentNodeIndex, int childNodeBeginIndex, int childNodeEndIndex, Map<Integer, Integer> bfsIndex, Integer[] bfsNodeArray,
                       Map<Integer, Integer> dfsIndex, Integer[] dfsNodeArray, Map<Integer, List<Integer>> node2Child, int endIndex) {
        int currentNodeValue = dfsNodeArray[currentNodeIndex];
        List<Integer> currentNodeChild = node2Child.get(currentNodeValue);
        for (int i = childNodeBeginIndex; i <= childNodeEndIndex; i++) {
            if (i == childNodeBeginIndex) {
                currentNodeChild.add(dfsNodeArray[i]);
            } else {
                if (dfsNodeArray[i] < dfsNodeArray[i - 1]) {
                    int inBFSIndex = bfsIndex.get(dfsNodeArray[i]);
                    if (inBFSIndex >= endIndex) {
                        currentNodeChild.add(dfsNodeArray[i]);
                        continue;
                    }
                    if (bfsNodeArray[inBFSIndex + 1] > dfsNodeArray[i]) {
                        index(i - 1, i, dfsIndex.get(bfsNodeArray[inBFSIndex + 1]) - 1, bfsIndex, bfsNodeArray, dfsIndex, dfsNodeArray, node2Child, endIndex);
                    } else {
                        index(i - 1, i, childNodeEndIndex, bfsIndex, bfsNodeArray, dfsIndex, dfsNodeArray, node2Child, endIndex);
                    }
                }
                if (bfsIndex.get(dfsNodeArray[i]) - bfsIndex.get(dfsNodeArray[i - 1]) == 1) {
                    currentNodeChild.add(dfsNodeArray[i]);
                } else {
                    int inBFSIndex = bfsIndex.get(dfsNodeArray[i]);
                    if (inBFSIndex >= endIndex) {
                        currentNodeChild.add(dfsNodeArray[i]);
                        continue;
                    }
                    if (bfsNodeArray[inBFSIndex + 1] > dfsNodeArray[i]) {
                        index(i, i + 1, dfsIndex.get(bfsNodeArray[inBFSIndex + 1]) - 1, bfsIndex, bfsNodeArray, dfsIndex, dfsNodeArray, node2Child, endIndex);
                    } else {
                        index(i - 1, i, childNodeEndIndex, bfsIndex, bfsNodeArray, dfsIndex, dfsNodeArray, node2Child, endIndex);
                    }
                }
            }
        }
    }

    private Map<Integer, Integer> node2Index(String s) {
        Map<Integer, Integer> result = new LinkedHashMap<>();
        LinkedList<Integer> nodes = parseNode(s);
        int index = 0;
        while (!nodes.isEmpty()) {
            result.put(nodes.poll(), index++);
        }
        return result;
    }

    private LinkedList<Integer> parseNode(String desc) {
        LinkedList<Integer> result = new LinkedList<>();
        char[] chars = desc.toCharArray();
        int beginIndex = -1;
        int endIndex = -1;
        int cLen = chars.length;
        char c;
        for (int i = 0; i < cLen; i++) {
            c = chars[i];
            if (c != ' ' && beginIndex < 0) {
                beginIndex = i;
            }
            if (c == ' ') {
                endIndex = i;
            }
            if (beginIndex >= 0 && endIndex >= beginIndex) {
                result.add(Integer.valueOf(desc.substring(beginIndex, endIndex)));
                beginIndex = -1;
                endIndex = -1;
            }
        }
        return result;
    }
}
