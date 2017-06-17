package ACM.ParenthesesBalance;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/6/17
 * Time:16:37
 * <p>
 * 输入一个包含“( )”和“[ ]”的括号序列，判断是否合法。具体规则如下：
 * 空串合法。
 * 如果A和B都合法，则AB合法。
 * 如果A合法则(A)和[A]都合法。
 * <p>
 * Parentheses Balance, UVa 673
 * <p>
 * Sample Input
 * 3
 * ([])
 * (([()])))
 * ([()[]()])()
 * Sample Output
 * Yes
 * No
 * Yes
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("-----------------------------------------------");
        solution.case2();
        System.out.println("-----------------------------------------------");
        solution.case3();
    }

    private void case1() {
        check("([])");
    }

    private void case2() {
        check("(([()])))");
    }

    private void case3() {
        check("([()[]()])()");
    }

    private void check(String str) {
        if (checkString(str)) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
    }

    private boolean checkString(String str) {
        return checkStringByReplaceAll(str).isEmpty();
    }

    private String checkStringByReplaceAll(String str) {
        boolean find1 = false;
        if (str.contains("()")) {
            find1 = true;
            str = str.replaceAll("\\(\\)", "");
        }

        boolean find2 = false;
        if (str.contains("[]")) {
            find2 = true;
            str = str.replaceAll("\\[\\]", "");
        }
        if (!find1 && !find2) {
            return str;
        }
        return checkStringByReplaceAll(str);
    }
}
