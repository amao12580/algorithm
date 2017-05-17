package ACM.Rails;

import java.util.Arrays;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/16
 * Time:15:40
 * <p>
 * 某城市有一个火车站，铁轨铺设如图6-1所示。有n节车厢从A方向驶入车站，按进站顺
 * 序编号为1～n。你的任务是判断是否能让它们按照某种特定的顺序进入B方向的铁轨并驶出
 * 车站。例如，出栈顺序(5 4 1 2 3)是不可能的，但(5 4 3 2 1)是可能的。
 * 图6-1　铁轨
 * 为了重组车厢，你可以借助中转站C。这是一个可以停放任意多节车厢的车站，但由于
 * 末端封顶，驶入C的车厢必须按照相反的顺序驶出C。对于每个车厢，一旦从A移入C，就不
 * 能再回到A了；一旦从C移入B，就不能回到C了。换句话说，在任意时刻，只有两种选择：
 * A→C和C→B。
 * <p>
 * Rails, ACM/ICPC CERC 1997, UVa 514
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=455
 */
public class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
    }

    private void case1() {
        check(new int[]{1, 2, 3, 4, 5}, new int[]{5, 4, 1, 2, 3});
        check(new int[]{1, 2, 3, 4, 5}, new int[]{5, 4, 3, 2, 1});
    }

    private void check(int[] in, int[] out) {
        Stack<Integer> stack = new Stack<>();
        int endIndex = in.length - 1;
        int len = in.length;
        int[] empty = new int[len];
        int[] maybe;
        int index;
        for (int i = 0; i <= endIndex; i++) {
            maybe = empty;
            index = 0;
            for (int j = 0; j <= endIndex; j++) {
                stack.push(in[j]);
                if (j == i || j == endIndex) {
                    while (!stack.empty()) {
                        maybe[index] = stack.pop();
                        index++;
                    }
                }
            }
            if (Arrays.equals(maybe, out)) {
                System.out.println("YES");
                return;
            }
        }
        System.out.println("NO");
    }
}
