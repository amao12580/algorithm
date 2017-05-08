package ACM.BugHunt;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/6
 * Time:11:14
 * <p>
 * 输入并模拟执行一段程序，输出第一个bug所在的行。每行程序有两种可能：
 * 数组定义，格式为arr[size]。例如a[10]或者b[5]，可用下标分别是0～9和0～4。定义之
 * 后所有元素均为未初始化状态。
 * 赋值语句，格式为arr[index]=value。例如a[0]=3或者a[a[0]]=a[1]。
 * 赋值语句可能会出现两种bug：下标index越界；使用未初始化的变量（index和value都可
 * 能出现这种情况）。
 * 程序不超过1000行，每行不超过80个字符且所有常数均为小于2 31 的非负整数。
 * <p>
 * Bug Hunt, ACM/ICPC Tokyo 2007, UVa1596
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("------------------------------------------------------------------------");
        solution.case2();
        System.out.println("------------------------------------------------------------------------");
        solution.case3();
        System.out.println("------------------------------------------------------------------------");
        solution.case4();
    }

    private void case1() {
        bugHunt(new String[]{"a[2147483647]", "a[0]=1", "B[2]", "B[a[0]]=2", "a[B[a[0]]]=3", "a[2147483646]=a[2]"});
    }

    private void case2() {
        bugHunt(new String[]{"g[2]", "G[10]", "g[0]=0", "g[1]=G[0]"});
    }

    private void case3() {
        bugHunt(new String[]{"b[2]", "b[0]=2", "b[1]=b[b[0]]", "b[0]=b[1]"});
    }

    private void case4() {
        bugHunt(new String[]{"a[0]", "a[0]=1"});
    }

    private void bugHunt(String[] codes) {
        Statement.parse(codes);
    }
}
