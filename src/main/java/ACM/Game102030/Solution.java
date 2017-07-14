package ACM.Game102030;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/7/14
 * Time:12:39
 * <p>
 * 有一种纸牌游戏叫做10-20-30。游戏使用除大王和小王之外的52张牌，J、Q、K的面值
 * 是10，A的面值是1，其他牌的面值等于它的点数。
 * 把52张牌叠放在一起放在手里，然后从最上面开始依次拿出7张牌从左到右摆成一条直
 * 线放在桌子上，每一张牌代表一个牌堆。每次取出手中最上面的一张牌，从左至右依次放在
 * 各个牌堆的最下面。当往最右边的牌堆放了一张牌以后，重新往最左边的牌堆上放牌。
 * 如果当某张牌放在某个牌堆上后，牌堆的最上面两张和最下面一张牌的和等于10、20或
 * 者30，这3张牌将会从牌堆中拿走，然后按顺序放回手中并压在最下面。如果没有出现这种
 * 情况，将会检查最上面一张和最下面两张牌的和是否为10、20或者30，解决方法类似。如果
 * 仍然没有出现这种情况，最后检查最下面的3张牌的和，并用类似的方法处理。例如，如果
 * 某一牌堆中的牌从上到下依次是5、9、7、3，那么放上6以后的布局如图6-27所示。
 * <p>
 * 如果放的不是6，而是Q，对应的情况如图6-28所示。
 * <p>
 * 如果某次操作后某牌堆中没有剩下一张牌，那么将该牌堆便永远地清除掉，并把它右边
 * 的所有牌堆顺次往左移。如果所有牌堆都清除了，游戏胜利结束；如果手里没有牌了，游戏
 * 以失败告终；有时游戏永远无法结束，这时则称游戏出现循环。给出52张牌最开始在手中的
 * 顺序，请模拟这个游戏并计算出游戏结果。
 * <p>
 * 10-20-30, ACM/ICPC World Finals 1996, UVa246
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=182
 * <p>
 * Sample Input
 * 2 6 5 10 10 4 10 10 10 4 5 10 4 5 10 9 7 6 1 7 6 9 5 3 10 10 4 10 9 2 1
 * 10 1 10 10 10 3 10 9 8 10 8 7 1 2 8 6 7 3 3 8 2
 * 4 3 2 10 8 10 6 8 9 5 8 10 5 3 5 4 6 9 9 1 7 6 3 5 10 10 8 10 9 10 10 7
 * 2 6 10 10 4 10 1 3 10 1 1 10 2 2 10 4 10 7 7 10
 * 10 5 4 3 5 7 10 8 2 3 9 10 8 4 5 1 7 6 7 2 6 9 10 2 3 10 3 4 4 9 10 1 1
 * 10 5 10 10 1 8 10 7 8 10 6 10 10 10 9 6 2 10 10
 * 0
 * Sample Output
 * Win : 66
 * Loss: 82
 * Draw: 73
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        solution.case2();
        solution.case3();
    }

    private void case1() {
        play("2 6 5 10 10 4 10 10 10 4 5 10 4 5 10 9 7 6 1 7 6 9 5 3 10 10 4 10 9 2 1 10 1 10 10 10 3 10 9 8 10 8 7 1 2 8 6 7 3 3 8 2 ");
    }

    private void case2() {
        play("4 3 2 10 8 10 6 8 9 5 8 10 5 3 5 4 6 9 9 1 7 6 3 5 10 10 8 10 9 10 10 7 2 6 10 10 4 10 1 3 10 1 1 10 2 2 10 4 10 7 7 10 ");
    }

    private void case3() {
        play("10 5 4 3 5 7 10 8 2 3 9 10 8 4 5 1 7 6 7 2 6 9 10 2 3 10 3 4 4 9 10 1 1 10 5 10 10 1 8 10 7 8 10 6 10 10 10 9 6 2 10 10");
    }

    private void play(String desc) {
        LinkedList<Integer> cards = parseCard(desc);
        int pileNum = 7;
        LinkedList<LinkedList<Integer>> piles = new LinkedList<>();
        initFillPile(cards, pileNum, piles);
        play(cards, piles);
        System.out.println((result == null ? "Draw" : result > 0 ? "Win" : "Loss") + "：" + count);
    }


    Integer result = null;
    int count = 0;

    private void play(LinkedList<Integer> cards, LinkedList<LinkedList<Integer>> piles) {
        int notEmptyPile = 0;
        for (LinkedList<Integer> pile : piles) {
            if (pile.isEmpty()) {
                continue;
            }
            notEmptyPile++;
            pile.add(cards.poll());
            reducePile(pile, cards);
            if (cards.isEmpty()) {
                break;
            }
            if (pile.isEmpty()) {
                notEmptyPile--;
            }
            count++;
        }
        if (cards.isEmpty() && notEmptyPile > 0) {
            result = -1;
            return;
        }
        if (!cards.isEmpty() && notEmptyPile == 0) {
            result = 1;
            return;
        }
        play(cards, piles);
    }

    private void reducePile(LinkedList<Integer> pile, LinkedList<Integer> cards) {
        int size = pile.size();
        if (size > 2) {
            int f1 = pile.peekFirst();
            int f2 = pile.get(1);
            int s1 = pile.peekLast();

            int sum = f1 + f2 + s1;
            if (sum == 10 || sum == 20 || sum == 30) {
                cards.add(f1);
                cards.add(f2);
                cards.add(s1);
                pile.remove(0);
                pile.remove(0);
                pile.removeLast();
                reducePile(pile, cards);
                return;
            }
            int s2 = pile.get(size - 2);
            sum = f1 + s1 + s2;
            if (sum == 10 || sum == 20 || sum == 30) {
                cards.add(f1);
                cards.add(s2);
                cards.add(s1);
                pile.remove(0);
                pile.remove(size - 3);
                pile.removeLast();
                reducePile(pile, cards);
                return;
            }
            int s3 = pile.get(size - 3);
            sum = s1 + s2 + s3;
            if (sum == 10 || sum == 20 || sum == 30) {
                cards.add(s3);
                cards.add(s2);
                cards.add(s1);
                pile.removeLast();
                pile.removeLast();
                pile.removeLast();
                reducePile(pile, cards);
//                return;
            }
        }
    }

    private void initFillPile(LinkedList<Integer> cards, int pileNum, LinkedList<LinkedList<Integer>> piles) {
        LinkedList<Integer> pile;
        for (int i = 0; i < pileNum; i++) {
            pile = new LinkedList<>();
            pile.add(cards.poll());
            piles.add(pile);
            count++;
        }
    }

    private LinkedList<Integer> parseCard(String cardsDesc) {
        LinkedList<Integer> result = new LinkedList<>();
        char[] chars = cardsDesc.toCharArray();
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
                result.add(Integer.valueOf(cardsDesc.substring(beginIndex, endIndex)));
                beginIndex = -1;
                endIndex = -1;
            }
        }
        return result;
    }
}
