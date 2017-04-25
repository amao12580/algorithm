package ACM.WhereistheMarble;

import basic.Util;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/4/25
 * Time:16:01
 * <p>
 * 现有N个大理石，每个大理石上写了一个非负整数。首先把各数从小到大排序，然后回
 * 答Q个问题。每个问题问是否有一个大理石写着某个整数x，如果是，还要回答哪个大理石上
 * 写着x。排序后的大理石从左到右编号为1～N。（在样例中，为了节约篇幅，所有大理石上
 * 的数合并到一行，所有问题也合并到一行。）
 * 样例输入：
 * 2 3 5 1
 * 5
 * 4 1
 * 5 2
 * 1 3 3 3 1
 * 2 3
 * 样例输出：
 * CASE #1:
 * 5 found at 4
 * CASE #2:
 * 2 not found
 * 3 found at 3
 * <p>
 * Where is the Marble？，Uva 10474
 */
public class Solution {
    private int[] marbles = null;
    private int[] questions = null;

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.generatorOne();
        solution.find();
    }

    private void find() {
        Arrays.sort(marbles);
        int min = marbles[0];
        int len = marbles.length;
        int max = marbles[len - 1];
        System.out.println("in order marbles:" + Arrays.toString(marbles));
        System.out.println("questions:" + Arrays.toString(questions));
        for (int t : questions) {
            if (t > max || t < min) {
                System.out.println(t + " not found.");
                continue;
            }
            int index = Arrays.binarySearch(marbles, t);
            if (index < 0) {
                System.out.println(t + " not found.");
            } else {
                System.out.println(t + " found at " + (index + 1));
                int t1 = index - 1;
                while (t1 >= 0) {
                    if (marbles[t1] == t) {
                        System.out.println(t + " found at " + (t1 + 1));
                    } else {
                        break;
                    }
                    t1--;
                }
                t1 = index + 1;
                while (t1 < len) {
                    if (marbles[t1] == t) {
                        System.out.println(t + " found at " + (t1 + 1));
                    } else {
                        break;
                    }
                    t1++;
                }
            }
        }
    }


    private void generatorOne() {
        marbles = new int[Util.getRandomInteger(1, 100)];
        int len = marbles.length;
        for (int i = 0; i < len; i++) {
            marbles[i] = Util.getRandomInteger(1, 100);
        }

        questions = new int[Util.getRandomInteger(1, 100)];
        int qlen = questions.length;
        for (int i = 0; i < qlen; i++) {
            questions[i] = Util.getRandomInteger(1, 100);
        }
        System.out.println("marbles len:" + marbles.length + ",questions len:" + questions.length);
    }
}
