package ACM.ThrowingCardsAwayI;

import basic.Util;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/4
 * Time:16:19
 * <p>
 * 桌上有n（n≤50）张牌，从第一张牌（即位于顶面的牌）开始，从上往下依次编号为1～
 * n。当至少还剩下两张牌时进行以下操作：把第一张牌扔掉，然后把新的第一张牌放到整叠
 * 牌的最后。输入每行包含一个n，输出每次扔掉的牌以及最后剩下的牌。
 * <p>
 * Throwing cards away I, UVa 10935
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.throwCard(Util.getRandomInteger(1, 50));
    }

    private void throwCard(int sum) {
        System.out.println("sum:" + sum);
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < sum; i++) {
            list.add(i + 1);
        }
        LinkedList<Integer> throwCard = new LinkedList<>();
        while (list.size() >= 2) {
            throwCard.add(list.pollFirst());
            if (list.size() > 2) {
                list.add(list.pollFirst());
            }
        }
        System.out.println("throw card:" + throwCard.toString());
        System.out.println("left card:" + list.toString());
    }
}
