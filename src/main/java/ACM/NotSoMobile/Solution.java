package ACM.NotSoMobile;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/23
 * Time:10:36
 * <p>
 * 输入一个树状天平，根据力矩相等原则判断是否平衡。如图6-5所示，所谓力矩相等，
 * 就是W l D l =W r D r ，其中W l 和W r 分别为左右两边砝码的重量，D为距离。
 * 采用递归（先序）方式输入：每个天平的格式为W l ，D l ，W r ，D r ，当W l 或W r 为0时，表
 * 示该“砝码”实际是一个子天平，接下来会描述这个子天平。当W l =W r =0时，会先描述左子天
 * 平，然后是右子天平。
 * 样例输入：
 * 1
 * 0 2 0 4
 * 0 3 0 1
 * 1 1 1 1
 * 2 4 4 2
 * 1 6 3 2
 * 其正确输出为YES，对应图6-6。
 * <p>
 * Not so Mobile, UVa 839
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
    }

    private void case1() {
        int[][] desc = {{0, 2, 0, 4}, {0, 3, 0, 1}, {1, 1, 1, 1}, {2, 4, 4, 2}, {1, 6, 3, 2}};
        balance(desc);
    }

    private void check(int[][] desc) {
        int endIndex = desc.length - 1;
        for (int i = 0; i <= endIndex; i++) {
            int[] node = desc[i];
            if (node == null) {
                continue;
            }
            if (node[0] == 0) {
                node[0] = countWeight(desc, i + 1, endIndex);
                if (node[0] == 0) {
                    throw new IllegalArgumentException();
                }
            }
            if (node[2] == 0) {
                node[2] = countWeight(desc, i + 1, endIndex);
                if (node[2] == 0) {
                    throw new IllegalArgumentException();
                }
            }
            if (node[0] * node[1] != node[2] * node[3]) {
                System.out.println("NO");
                return;
            }
        }
        System.out.println("YES");
    }

    private int countWeight(int[][] desc, int beginIndex, int endIndex) {
        for (int i = beginIndex; i <= endIndex; i++) {
            int[] node = desc[i];
            if (node == null) {
                continue;
            }
            if (node[0] == 0) {
                node[0] = countWeight(desc, i + 1, endIndex);
            }
            if (node[2] == 0) {
                node[2] = countWeight(desc, i + 1, endIndex);
            }
            if (node[0] * node[1] != node[2] * node[3]) {
                throw new IllegalArgumentException("NO");
            }
            int result = node[0] + node[2];
            desc[i] = null;
            return result;
        }
        return 0;
    }

    private void balance(int[][] desc) {
        try {
            check(desc);
        } catch (IllegalArgumentException e) {
            System.out.println("NO");
        }
    }
}
