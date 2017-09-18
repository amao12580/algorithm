package ACM.TheMorningAfterHalloween;

import basic.Util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/9/15
 * Time:11:44
 * <p>
 * w*h（w,h≤16）网格上有n（n≤3）个小写字母（代表鬼）。要求把它们分别移动到对应
 * 的大写字母里。每步可以有多个鬼同时移动（均为往上下左右4个方向之一移动），但每步
 * 结束之后任何两个鬼不能占用同一个位置，也不能在一步之内交换位置。例如如图7-17所示
 * 的局面：一共有4种移动方式，如图7-18所示。
 * <p>
 * 输入保证所有空格连通，所有障碍格也连通，且任何一个2*2子网格中至少有一个障碍
 * 格。输出最少的步数。输入保证有解。
 * <p>
 * The Morning after Halloween, Japan 2007, UVa1601
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=4476
 * <p>
 * Sample Input
 * 5 5 2
 * #####
 * #A#B#
 * # #
 * #b#a#
 * #####
 * 16 4 3
 * ################
 * ## ########## ##
 * # ABCcba #
 * ################
 * 16 16 3
 * ################
 * ### ## # ##
 * ## # ## # c#
 * # ## ########b#
 * # ## # # # #
 * # # ## # # ##
 * ## a# # # # #
 * ### ## #### ## #
 * ## # # # #
 * # ##### # ## ##
 * #### #B# # #
 * ## C# # ###
 * # # # ####### #
 * # ###### A## #
 * # # ##
 * ################
 * 0 0 0
 * Sample Output
 * 7
 * 36
 * 77
 */
public class Solution {
    private String[] puzzle;
    private int rowNum;
    private int columnNum;
    private int ghostNum;
    private LinkedList<LinkedList<Integer>> moves;

    public static void main(String[] args) {
        new Solution().case1();
//        new Solution().case2();
//        new Solution().case3();
    }

    /**
     * 计算所有移动方案，sum=ghost数量
     */
    private LinkedList<LinkedList<Integer>> shouldMoveGhost(int sum) {//只需要计算一次
        LinkedList<LinkedList<Integer>> result = new LinkedList<>();
        for (int i = 1; i <= sum; i++) {
            shouldMoveGhost(sum, i, result);
        }
        return result;
    }

    private void shouldMoveGhost(int sum, int num, LinkedList<LinkedList<Integer>> result) {
        LinkedList<Integer> current = new LinkedList<>();
        boolean[] b = new boolean[sum];
        shouldMoveGhost(sum, num, b, result, current);
    }

    private void shouldMoveGhost(int sum, int num, boolean[] b,
                                 LinkedList<LinkedList<Integer>> result,
                                 LinkedList<Integer> current) {
        boolean item;
        LinkedList<Integer> group;
        for (int i = 0; i < sum; i++) {
            item = b[i];
            if (!item) {
                b[i] = true;
                group = new LinkedList<>(current);
                group.add(i);
                if (group.size() == num) {
                    result.add(group);
                } else {
                    shouldMoveGhost(sum, num, b, result, group);
                }
                b[i] = false;
            }
        }
    }

    private void case1() {
        String[] desc = {
                "#####",
                "#A#B#",
                "# #  ",
                "#b#a#",
                "#####",
        };
        restore(5, 5, 2, desc);
    }

    private void case2() {
        String[] desc = {
                "################",
                "## ########## ##",
                "# ABCcba #      ",
                "################",
        };
        restore(16, 4, 3, desc);
    }

    private void case3() {
        String[] desc = {
                "################",
                "### ## # ##     ",
                "## # ## # c#    ",
                "# ## ########b# ",
                "# ## # # # #    ",
                "# # ## # # ##   ",
                "## a# # # # #   ",
                "### ## #### ## #",
                "## # # # #      ",
                "# ##### # ## ## ",
                "#### #B# # #    ",
                "## C# # ###     ",
                "# # # ####### # ",
                "# ###### A## #  ",
                "# # ##          ",
                "################",
        };
        restore(16, 16, 3, desc);
    }

    private void restore(int columnNum, int rowNum, int ghostNum, String[] puzzle) {
        this.columnNum = columnNum;
        this.rowNum = rowNum;
        this.ghostNum = ghostNum;
        this.puzzle = puzzle;
        this.moves = shouldMoveGhost(ghostNum);
        scan();
        Execute positive = new Execute(true);
        Execute negative = new Execute(false);
        positive.setOpposite(negative);
        negative.setOpposite(positive);
        LinkedList<Execute> executes = new LinkedList<>();
        executes.add(positive);
        executes.add(negative);
        Execute current;
        while (!executes.isEmpty()) {
            current = executes.poll();
            if (current.run()) {
                executes.add(current);
            }
        }
        System.out.println(globalMin);
        System.out.println("-----------------------------------------------------");
    }

    class Execute {
        private Execute opposite;
        private Set<Integer> blank;
        private Set<State> current = new HashSet<>();
        private Map<Integer, Integer> states = new ConcurrentHashMap<>();

        Execute(boolean isPositive) {
            this.blank = new HashSet<>(Solution.blank);
            int[] after = new int[ghostNum];
            int[] before = new int[ghostNum];
            scan(blank, before, after, isPositive);
            states.put(Arrays.toString(before).hashCode(), 0);
            current.add(new State(before));
        }

        void setOpposite(Execute opposite) {
            this.opposite = opposite;
        }

        boolean containsKey(Integer current) {
            return this.states.containsKey(current);
        }

        int getShortestSteps(Integer current) {
            return this.states.getOrDefault(current, 0);
        }

        boolean visit(Integer current, int steps) {
            if (states.containsKey(current)) {
                Integer s = states.get(current);
                if (s == null || steps < s) {
                    states.put(current, steps);
                }
                return true;
            } else {
                states.put(current, steps);
            }
            return false;
        }

        boolean run() {
            Set<State> next = new HashSet<>();
            for (State item : current) {
                move(item, next);
            }
            current.clear();
            current.addAll(next);
            return !current.isEmpty();
        }

        void move(State state, Set<State> next) {
            Integer[] nextIndexArray;
            Integer nextIndex;
            State current;
            for (LinkedList<Integer> list : moves) {//移动方案，可能会有多个同时移动
                current = state.copyOf();
                for (Integer item : list) {//移动项
                    nextIndexArray = current.next(this.blank, item);
                    for (int j = 0; j < 5; j++) {//方向
                        nextIndex = nextIndexArray[j];
                        if (nextIndex == null) {
                            continue;
                        }
                        current.move(item, nextIndex);
                        if (check(current.hashCode(), current.getStep())) {
                            next.add(current);
                        }
                    }
                }
            }
        }


        boolean check(Integer hashCode, int steps) {
            boolean visited = visit(hashCode, steps);
            boolean crossed = opposite.containsKey(hashCode);
            System.out.println("visited:" + visited + ",crossed:" + crossed);
            if (crossed) {
                steps = getShortestSteps(hashCode) + getShortestSteps(hashCode);
                System.out.println("may be " + steps + ",currentString:" + hashCode);
                if (globalMin < 0 || steps < globalMin) {
                    globalMin = steps;
                }
                return false;
            } else {
                if (visited || (globalMin > 0 && steps > globalMin)) {
                    return false;
                }
            }
            return true;
        }
    }

    private int globalMin = -1;
    private static Set<Integer> blank = new HashSet<>();
    private Map<Character, Integer> upperGhost = new HashMap<>(3);
    private Map<Character, Integer> lowerGhost = new HashMap<>(3);

    private void scan() {
        int index = 0;
        char[] row;
        Map<Character, Integer> map;
        for (String s : puzzle) {
            row = s.toCharArray();
            for (char c : row) {
                if (c == '#') {
                    index++;
                    continue;
                }
                if (c == ' ') {
                    blank.add(index);
                } else {
                    map = Util.isLowerCaseAlpha(c) ? lowerGhost : upperGhost;
                    if (map.containsKey(c)) {
                        throw new IllegalArgumentException();
                    }
                    map.put(c, index);
                }
                index++;
            }
        }
        if (upperGhost.size() != ghostNum || upperGhost.size() != lowerGhost.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void scan(Set<Integer> blank, int[] beforeIndex, int[] afterIndex, boolean isPositive) {
        if (isPositive) {
            scan(lowerGhost, beforeIndex);
            scan(upperGhost, afterIndex);
        } else {
            scan(upperGhost, beforeIndex);
            scan(lowerGhost, afterIndex);
        }
        for (int i : afterIndex) {
            blank.add(i);
        }
    }

    private void scan(Map<Character, Integer> ghost, int[] index) {
        List<Character> list = new ArrayList<>(ghost.keySet());
        Collections.sort(list);
        int i = 0;
        for (Character character : list) {
            index[i++] = ghost.get(character);
        }
    }

    class State {
        private int hashCode;
        private int[] ghostI2V;
        private int step;

        State(int[] ghostI2V) {
            this(0, ghostI2V);
        }

        State(int hashCode, int[] ghostI2V) {
            this.ghostI2V = ghostI2V;
            this.hashCode = hashCode == 0 ? Arrays.toString(this.ghostI2V).hashCode() : hashCode;
        }

        State(int hashCode, int[] ghostI2V, int step) {
            this(hashCode, ghostI2V);
            this.step = step;
        }

        State copyOf() {
            return new State(this.hashCode, Arrays.copyOf(this.ghostI2V, ghostNum), this.step);
        }

        Integer[] next(Set<Integer> blank, int ghostIndex) {
            Set<Integer> gis = new HashSet<>();
            for (int i = 0; i < ghostNum; i++) {
                gis.add(ghostI2V[i]);
            }
            return next(blank, ghostI2V[ghostIndex], gis);
        }

        Integer[] next(Set<Integer> blank, int nowIndex, Set<Integer> ghostIndex) {
            Integer[] next = new Integer[5];
            int nextIndex;
            if (nowIndex >= columnNum) {//上
                nextIndex = nowIndex - columnNum;
                if (blank.contains(nextIndex) && !ghostIndex.contains(nextIndex)) {
                    next[0] = nextIndex;
                }
            }
            if (nowIndex > (nowIndex / columnNum) * columnNum) {//左
                nextIndex = nowIndex - 1;
                if (blank.contains(nextIndex) && !ghostIndex.contains(nextIndex)) {
                    next[1] = nextIndex;
                }
            }
            if ((nowIndex / columnNum) < (rowNum - 1)) {//下
                nextIndex = nowIndex + columnNum;
                if (blank.contains(nextIndex) && !ghostIndex.contains(nextIndex)) {
                    next[2] = nextIndex;
                }
            }
            if (nowIndex < (((nowIndex / columnNum) + 1) * columnNum) - 1) {//右
                nextIndex = nowIndex + 1;
                if (blank.contains(nextIndex) && !ghostIndex.contains(nextIndex)) {
                    next[3] = nextIndex;
                }
            }
            next[4] = nowIndex;//不动
            return next;
        }

        void move(int index, int nextIndex) {
            this.ghostI2V[index] = nextIndex;
            this.step++;
            this.hashCode = Arrays.toString(this.ghostI2V).hashCode();
        }

        int getStep() {
            return step;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o instanceof State) {
                State other = (State) o;
                return this.hashCode() == other.hashCode();
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return hashCode;
        }
    }
}
