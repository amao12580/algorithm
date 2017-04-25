package ACM.DataMining;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/4/24
 * Time:16:16
 * <p>
 * 有两个n元素数组P和Q。P数组每个元素占S P 个字节，Q数组每个元素占S Q 个字节。有时
 * 需直接根据P数组中某个元素P(i)的偏移量P ofs (i)算出对应的Q(i)的偏移量Q ofs (i)。当两个数组
 * 的元素均为连续存储时 ，但因为除法慢，可以把式子改写成速度较快的。
 * 为了让这个式子成立，在P数组仍然连续存储的前提下，Q数组可以不连续存储（但不同数
 * 组元素的存储空间不能重叠）。这样做虽然会浪费一些空间，但是提升了速度，是一种用空
 * 间换时间的方法。
 * 输入n、S P 和S Q （N≤2 20 ，1≤S P ，S Q ≤2 10 ），你的任务是找到最优的A和B，使得占的空间
 * K尽量小。输出K、A、B的值。多解时让A尽量小，如果仍多解则让B尽量小。
 * <p>
 * Data Mining, ACM/ICPC NEERC 2003, UVa1591
 *
 * https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=4466
 *
 */
public class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
    }

    private void case1() {
        findBest(20, 3, 5);
        findBest(1024, 7, 1);
    }

    private void findBest(long n, int SP, int SQ) {
        System.out.println("n:" + n + ",SP:" + SP + ",SQ:" + SQ);
        int AC = 0;
        int A = 32;
        int BC;
        int B = 32;
        long KMin = n * SQ;
        long k = Long.MAX_VALUE;
        long pofs = (n - 1) * SP;
        while (AC <= 32) {
            BC = 0;
            while (BC <= 32) {
                long kc = ((pofs + (pofs << AC)) >> BC) + SQ;
                if (kc <= KMin) {
                    break;
                }
                if (k > kc) {
                    k = kc;
                    A = AC;
                    B = BC;
                } else if (k == kc) {
                    if (A > AC) {
                        A = AC;
                    }
                    if (B > BC) {
                        B = BC;
                    }
                }
                BC++;
            }
            AC++;
        }
        System.out.println("k:" + k + ",A:" + A + ",B:" + B);
        System.out.println("--------------------------------------------------------");
    }
}
