package ACM.AccordianPatience;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/7/13
 * Time:19:00
 * <p>
 * 把52张牌从左到右排好，每张牌自成一个牌堆（pile）。当某张牌与它左边那张牌或者
 * 左边第3张牌“match”（花色suit或者点数rank相同）时，就把这张牌移到那张牌上面。移动之
 * 后还要查看是否可以进行其他移动。只有位于牌堆顶部的牌才能移动或者参与match。当牌
 * 堆之间出现空隙时要立刻把右边的所有牌堆左移一格来填补空隙。如果有多张牌可以移动，
 * 先移动最左边的那张牌；如果既可以移一格也可以移3格时，移3格。按顺序输入52张牌，输
 * 出最后的牌堆数以及各牌堆的牌数。
 * 样例输入：
 * QD AD 8H 5S 3H 5H TC 4D JH KS 6H 8S JS AC AS 8D 2H QS TS 3S AH 4H TH TD 3C 6S
 * 8C 7D 4C 4S 7S 9H 7C 5D 2S KD 2D QH JD 6D 9D JC 2C KH 3D QC 6C 9S KC 7H 9C 5C
 * AC 2C 3C 4C 5C 6C 7C 8C 9C TC JC QC KC AD 2D 3D 4D 5D 6D 7D 8D TD 9D JD QD KD
 * AH 2H 3H 4H 5H 6H 7H 8H 9H KH 6S QH TH AS 2S 3S 4S 5S JH 7S 8S 9S TS JS QS KS
 * #
 * 样例输出：
 * 6 piles remaining: 40 8 1 1 1 1
 * 1 pile remaining: 52
 * <p>
 * “Accordian” Patience, UVa 127
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        solution.case2();
    }

    private void case1() {
        String cards =
                "QD AD 8H 5S 3H 5H TC 4D JH KS 6H 8S JS AC AS 8D 2H QS TS 3S AH 4H TH TD 3C 6S " +
                        "8C 7D 4C 4S 7S 9H 7C 5D 2S KD 2D QH JD 6D 9D JC 2C KH 3D QC 6C 9S KC 7H 9C 5C ";
        play(cards);
    }

    private void case2() {
        String cards =
                "AC 2C 3C 4C 5C 6C 7C 8C 9C TC JC QC KC AD 2D 3D 4D 5D 6D 7D 8D TD 9D JD QD KD " +
                        "AH 2H 3H 4H 5H 6H 7H 8H 9H KH 6S QH TH AS 2S 3S 4S 5S JH 7S 8S 9S TS JS QS KS ";
        play(cards);
    }

    private void play(String cardsDesc) {
        LinkedList<Stack<Card>> cards = parseCard(cardsDesc);
        move(cards, 1, false);
        StringBuilder builder = new StringBuilder();
        for (Stack<Card> stack : cards) {
            builder = builder.append(stack.size()).append(" ");
        }
        System.out.println(cards.size() + " piles remaining: " + builder.toString());
    }

    private void move(LinkedList<Stack<Card>> cards, int beginIndex, boolean needResize) {
        if (needResize) {
            resize(cards);
        }
        int size = cards.size();
        Stack<Card> currentPile;
        Stack<Card> left1Pile;
        Stack<Card> left3Pile;
        int os;
        boolean thisLoopNeedResize = false;
        boolean thisLoopLeft3Moved = false;
        boolean thisLoopLeft1Moved = false;
        for (int i = beginIndex; i < size; i++) {
            currentPile = cards.get(i);
            if (i - 3 >= 0) {
                left3Pile = cards.get(i - 3);
                os = currentPile.size();
                thisLoopNeedResize = move(currentPile, left3Pile);
                thisLoopLeft3Moved = currentPile.size() != os;
                if (thisLoopNeedResize || thisLoopLeft3Moved) {
                    beginIndex = i - 3;
                    break;
                }
            }
            if (i <= 0) {
                continue;
            }
            left1Pile = cards.get(i - 1);
            os = currentPile.size();
            thisLoopNeedResize = move(currentPile, left1Pile);
            thisLoopLeft1Moved = currentPile.size() != os;
            if (thisLoopNeedResize || thisLoopLeft1Moved) {
                beginIndex = i - 1;
                break;
            }
        }
        if (thisLoopLeft3Moved || thisLoopLeft1Moved) {
            move(cards, beginIndex, thisLoopNeedResize);
        }
    }

    private void resize(LinkedList<Stack<Card>> cards) {
        Iterator<Stack<Card>> iterator = cards.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isEmpty()) {
                iterator.remove();
                break;
            }
        }
    }

    private boolean move(Stack<Card> form, Stack<Card> to) {
        boolean isMoved = false;
        if (form.peek().match(to.peek())) {
            to.push(form.pop());
            isMoved = true;
        }
        return form.isEmpty() || isMoved && move(form, to);
    }

    private LinkedList<Stack<Card>> parseCard(String cardsDesc) {
        LinkedList<Stack<Card>> result = new LinkedList<>();
        char[] chars = cardsDesc.toCharArray();
        int beginIndex = -1;
        int endIndex = -1;
        int cLen = chars.length;
        char c;
        Stack<Card> stack;
        for (int i = 0; i < cLen; i++) {
            c = chars[i];
            if (c != ' ' && beginIndex < 0) {
                beginIndex = i;
            }
            if (c == ' ') {
                endIndex = i;
            }
            if (beginIndex >= 0 && endIndex >= beginIndex) {
                stack = new Stack<>();
                stack.push(new Card(cardsDesc.substring(beginIndex, endIndex)));
                result.add(stack);
                beginIndex = -1;
                endIndex = -1;
            }
        }
        return result;
    }
}

class Card {
    private char suit;
    private char rank;
    private String desc;

    public Card(String desc) {
        this.rank = desc.charAt(0);
        this.suit = desc.charAt(1);
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Card) {
            Card other = (Card) o;
            return this.hashCode() == other.hashCode();
        } else {
            return false;
        }
    }

    public char getSuit() {
        return suit;
    }

    public char getRank() {
        return rank;
    }

    public boolean match(Card other) {
        return this.getSuit() == other.getSuit() || this.getRank() == other.getRank();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return desc;
    }
}
