package ACM.SpatialStructures;

import basic.Util;
import basic.dataStructure.tree.binary.MultiNode;
import basic.dataStructure.tree.binary.MultiTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/7/13
 * Time:12:59
 * <p>
 * 黑白图像有两种表示法：点阵表示和路径表示。路径表示法首先需要把图像转化为四分
 * 树，然后记录所有黑结点到根的路径。例如，对于如图6-25所示的图像。
 * <p>
 * NW、NE、SW、SE分别用1、2、3、4表示。最后把得到的数字串看成是五进制的，转
 * 化为十进制后排序。例如上面的树在转化、排序后的结果是：9 14 17 22 23 44 63 69 88 94
 * 113。
 * 你的任务是在这两种表示法之间进行转换。在点阵表示法中，1表示黑色，0表示白色。
 * 图像总是正方形的，且长度n为2的整数幂，并满足n≤64。输入输出细节请参见原题。
 * <p>
 * Spatial Structures, ACM/ICPC World Finals 1998, UVa806
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=747
 * <p>
 * Sample Input
 * 8
 * 00000000
 * 00000000
 * 00001111
 * 00001111
 * 00011111
 * 00111111
 * 00111100
 * 00111000
 * -8
 * 9 14 17 22 23 44 63 69 88 94 113 -1
 * 2
 * 00
 * 00
 * -4
 * 0 -1
 * 0
 * Sample Output
 * Image 1
 * 9 14 17 22 23 44 63 69 88 94 113
 * Total number of black nodes = 11
 * Image 2
 * ........
 * ........
 * ....****
 * ....****
 * ...*****
 * ..******
 * ..****..
 * ..***...
 * Image 3
 * Total number of black nodes = 0
 * Image 4
 * ***
 * ***
 * ***
 * ***
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
        System.out.println("Image 1");
        String[] desc = {
                "00000000",
                "00000000",
                "00001111",
                "00001111",
                "00011111",
                "00111111",
                "00111100",
                "00111000"
        };
        image(8, desc);
        System.out.println();
    }

    private void case2() {
        System.out.println("Image 2");
        String[] desc = {"9 14 17 22 23 44 63 69 88 94 113 -1"};
        image(-8, desc);
        System.out.println();
    }

    private void case3() {
        System.out.println("Image 3");
        String[] desc = {
                "00",
                "00"
        };
        image(2, desc);
        System.out.println();
    }

    private void case4() {
        System.out.println("Image 4");
        String[] desc = {"0 -1"};
        image(-4, desc);
        System.out.println();
    }

    private void image(int len, String[] desc) {
        if (len > 0) {
            latticeImage(len, desc);
        } else {
            quadtreeImage(Math.abs(len), desc);
        }
    }

    private void quadtreeImage(int len, String[] desc) {//四分树表示法
        boolean[][] array = new boolean[len][len];
        String s = desc[0];
        if (s.isEmpty()) {//全白
            printImage(array);
            return;
        }
        if ("0 -1".equals(s)) {//全黑
            printImage(array, true);
            return;
        }
        char[] chars = s.toCharArray();
        int beginIndex = -1;
        int endIndex = -1;
        int cLen = chars.length;
        char c;
        String black_10;
        for (int i = 0; i < cLen; i++) {
            c = chars[i];
            if (c != ' ' && beginIndex < 0) {
                beginIndex = i;
            }
            if (c == ' ') {
                endIndex = i;
            }
            if (beginIndex >= 0 && endIndex >= beginIndex) {
                black_10 = s.substring(beginIndex, endIndex);
                if ("-1".equals(black_10)) {
                    break;
                } else {
                    paintImage(array, Integer.toUnsignedString(Integer.parseInt(black_10, 10), 5), len);
                    beginIndex = -1;
                    endIndex = -1;
                }
            }
        }
        printImage(array);
    }

    private void printImage(boolean[][] array) {
        StringBuilder builder;
        for (boolean[] arr : array) {
            builder = new StringBuilder();
            for (boolean c : arr) {
                builder = builder.append(c ? "* " : ". ");
            }
            System.out.println(builder.toString());
        }
    }

    private void printImage(boolean[][] array, boolean black) {
        StringBuilder builder;
        for (boolean[] arr : array) {
            builder = new StringBuilder();
            for (boolean ignored : arr) {
                builder = builder.append(black ? "* " : ". ");
            }
            System.out.println(builder.toString());
        }
    }

    private void paintImage(boolean[][] array, String black_5, int len) {
        black_5 = Util.reverse(black_5);
        int cr = 0;
        int cc = 0;
        char[] chars = black_5.toCharArray();
        int[] moved;
        for (char c : chars) {
            len >>= 1;
            moved = move(cr, cc, Integer.valueOf(String.valueOf(c)), len);
            cr = moved[0];
            cc = moved[1];
        }
        for (int i = cr; i < cr + len; i++) {
            for (int j = cc; j < cc + len; j++) {
                array[i][j] = true;
            }
        }
    }

    private void latticeImage(int len, String[] desc) {//点阵表示法
        List<Integer> blackPaths = new ArrayList<>();
        createTree(len, desc, blackPaths);
        if (blackPaths.size() > 1) {
            Collections.sort(blackPaths);
        }
        if (!blackPaths.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (Integer i : blackPaths) {
                builder = builder.append(i).append(" ");
            }
            builder = builder.append("-1");
            System.out.println(builder.toString());
        }
        System.out.println("Total number of black nodes = " + blackPaths.size());
    }

    private void createTree(int len, String[] desc, List<Integer> blackPaths) {
        Boolean scan = scan(desc, 0, 0, len);
        if (scan == null) {
            MultiTree<Boolean> tree = new MultiTree<>();
            tree.setRoot(new MultiNode<>(null));
            createTree(len, desc, "", blackPaths, tree.getRoot(), 0, 0);
        }
    }

    private void createTree(int len, String[] desc, String path, List<Integer> blackPaths, MultiNode<Boolean> node, int cr, int cc) {
        Boolean scan;
        MultiNode<Boolean> next;
        int[] moved;
        len >>= 1;
        for (int i = 1; i <= 4; i++) {
            moved = move(cr, cc, i, len);
            scan = scan(desc, moved[0], moved[1], len);
            next = new MultiNode<>(scan);
            node.appendNextNode(next);
            if (scan == null) {
                if (len > 1) {
                    createTree(len, desc, path + i, blackPaths, next, moved[0], moved[1]);
                }
            } else if (scan) {
                blackPaths.add(Integer.valueOf(Integer.toUnsignedString(Integer.parseInt(Util.reverse(path + i), 5), 10)));
            }
        }
    }

    private int[] move(int rowIndex, int columnIndex, int location, int len) {
        int[] result = new int[2];
        switch (location) {
            case 1:
                break;
            case 2:
                columnIndex += len;
                break;
            case 3:
                rowIndex += len;
                break;
            case 4:
                columnIndex += len;
                rowIndex += len;
                break;
        }
        result[0] = rowIndex;
        result[1] = columnIndex;
        return result;
    }

    /**
     * 返回值：true 区域内为全黑，false 区域内为全白，null 区域内有黑也有白
     */
    private Boolean scan(String[] desc, int rowIndex, int columnIndex, int len) {
        boolean findBlack = false;
        boolean findWhite = false;
        char[] chars;
        char c;
        for (int i = rowIndex; i < rowIndex + len; i++) {
            chars = desc[i].toCharArray();
            for (int j = columnIndex; j < columnIndex + len; j++) {
                c = chars[j];
                if (c == '0') {
                    findWhite = true;
                } else {
                    findBlack = true;
                }
                if (findBlack && findWhite) {
                    return null;
                }
            }
        }
        return findBlack;
    }
}
