package ACM.STrees;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/6/17
 * Time:17:04
 * <p>
 * 给出一棵满二叉树，每一层代表一个01变量，取0时往左走，取1时往右走。例如，图6-
 * 19（a）和图6-19（b）都对应表达式 。
 * <p>
 * 给出所有叶子的值以及一些查询（即每个变量x i 的取值），求每个查询到达的叶子的
 * 值。例如，有4个查询：000、010、111、110，则输出应为0011。
 * <p>
 * S-Trees, UVa 712
 * <p>
 * Sample Input
 * 3
 * x1 x2 x3
 * 00000111
 * 4
 * 000
 * 010
 * 111
 * 110
 * <p>
 * <p>
 * 3
 * x3 x1 x2
 * 00010011
 * 4
 * 000
 * 010
 * 111
 * 110
 * 0
 * Sample Output
 * S-Tree #1:
 * 0011
 * S-Tree #2:
 * 0011
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("--------------------------------------------");
        solution.case2();
    }

    private void case1() {
        String[] query = {"000", "010", "111", "110"};
        tree(3, "00000111", query);
    }

    private void case2() {
        String[] query = {"000", "010", "111", "110"};
        tree(3, "00010011", query);
    }

    private void tree(int trunkNum, String leafs, String[] query) {
        if (leafs.length() != ((trunkNum + 1) << 1)) {
            System.out.println("wrong args.");
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (String string : query) {
            builder = builder.append(tree(leafs, string));
        }
        System.out.println(builder.toString());
    }

    private char tree(String leafs, String query) {
        char[] chars = query.toCharArray();
        for (char c : chars) {
            leafs = tree(leafs, c);
        }
        if (leafs.length() == 1) {
            return leafs.charAt(0);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private String tree(String leafs, char query) {
        if (query == '0') {
            return leafs.substring(0, leafs.length() / 2);
        } else {
            return leafs.substring(leafs.length() / 2);
        }
    }
}
