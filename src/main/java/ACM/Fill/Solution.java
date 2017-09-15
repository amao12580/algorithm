package ACM.Fill;

import basic.Util;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/9/14
 * Time:12:37
 * <p>
 * 有装满水的6升的杯子、空的3升杯子和1升杯子，3个杯子中都没有刻度。在不使用其他
 * 道具的情况下，是否可以量出4升的水呢？
 * 方法如图7-15所示。
 * <p>
 * 注意：由于没有刻度，用杯子x给杯子y倒水时必须一直持续到把杯子y倒满或者把杯
 * 子x倒空，而不能中途停止。
 * 你的任务是解决一般性的问题：设3个杯子的容量分别为a, b, c，最初只有第3个杯子装
 * 满了c升水，其他两个杯子为空。最少需要倒多少升水才能让某一个杯子中的水有d升呢？如
 * 果无法做到恰好d升，就让某一个杯子里的水是d'升，其中d'<d并且尽量接近d。
 * （1≤a,b,c,d≤200）。要求输出最少的倒水量和目标水量（d或者d'）。
 * <p>
 * <p>
 * Fill, UVa 10603
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=onlinejudge&page=show_problem&problem=1544
 * <p>
 * Sample Input
 * 2
 * 2 3 4 2
 * 96 97 199 62
 * Sample Output
 * 2 2
 * 9859 62
 */
public class Solution {
    public static void main(String[] args) {
        new Solution().case1();
        new Solution().case2();
        new Solution().case3();
        new Solution().case4();
    }

    private void case1() {
        fill(1, 3, 6, 4);
    }

    private void case2() {
        fill(2, 3, 4, 2);
    }

    private void case3() {
        fill(96, 97, 199, 62);
    }


    private void case4() {
        fill(Util.getRandomInteger(1, 200), Util.getRandomInteger(1, 200), Util.getRandomInteger(1, 200), Util.getRandomInteger(1, 200));
    }

    /**
     * 杯子的容量分别为a, b, c，最初只有第3个杯子装满了c升水
     * <p>
     * 最少需要倒多少升水才能让某一个杯子中的水有d升呢？如果无法做到恰好d升，就让某一个杯子里的水是d'升，其中d'<d并且尽量接近d。
     */
    private void fill(int a, int b, int c, int d) {
        long s = System.currentTimeMillis();
        System.out.println("a:" + a + ",b:" + b + ",c:" + c + ",d:" + d);
        capacity[0] = a;
        capacity[1] = b;
        capacity[2] = c;
        this.d = d;
        int[] stock = new int[3];
        stock[2] = c;
        List<State> states = new ArrayList<>();
        State state = new State(stock);
        states.add(state);
        allStates.add(state.hashCode);
        fill(states);
        System.out.println(minJug + " " + (minGap == null ? -1 : (d - minGap)));
        System.out.println("time:" + (System.currentTimeMillis() - s));
        System.out.println("----------------------------------------------");
    }

    private void fill(List<State> states) {
        State current;
        int[][] maybe;
        int[] es;
        int currentGap;
        int currentJug;
        Set<State> next = new HashSet<>();
        for (State state : states) {
            maybe = state.canJug();
            for (int i = 0; i < 3; i++) {
                es = maybe[i];
                if (es == null) {
                    continue;
                }
                for (int endIndex : es) {
                    current = state.copyOf();
                    current.jug(i, endIndex);
                    if (!allStates.add(current.hashCode) || next.contains(current)) {
                        continue;
                    }
                    currentGap = current.getGap();
                    currentJug = current.getJug();
                    if (minGap == null) {
                        minGap = currentGap;
                        minJug = currentJug;
                    } else {
                        if (Math.abs(currentGap) < Math.abs(minGap)) {
                            minGap = currentGap;
                            minJug = currentJug;
                        }
                        if (Math.abs(currentGap) == Math.abs(minGap) && currentJug < minJug) {
                            minJug = currentJug;
                        }
                    }
                    next.add(current);
                }
            }
        }
        states.clear();
        if (next.isEmpty()) {
            return;
        }
        fill(new ArrayList<>(next));
    }

    private Set<Integer> allStates = new HashSet<>();

    private int minJug = -1;
    private Integer minGap = null;
    private int d = -1;

    private int[] capacity = new int[3];//每个杯子的容量

    class State {
        int hashCode;
        int[] stock;//每个杯子已经有的水量
        int jug = 0;//倒水量

        State(int[] stock) {
            this(stock, 0);
        }

        State(int[] stock, int hashCode) {
            this.stock = stock;
            this.hashCode = hashCode == 0 ? Arrays.toString(stock).hashCode() : hashCode;
        }

        State(int[] stock, int jug, int hashCode) {
            this(stock, hashCode);
            this.jug = jug;
        }

        State copyOf() {
            return new State(Arrays.copyOf(this.stock, 3), this.jug, this.hashCode);
        }

        void jug(int beginIndex, int endIndex) {
            this.jug += jug(beginIndex, endIndex, this.stock);
            this.hashCode = Arrays.toString(stock).hashCode();
        }

        private int jug(int beginIndex, int endIndex, int[] stock) {
            int residual = capacity[endIndex] - stock[endIndex];
            if (residual <= 0) {
                throw new IllegalArgumentException();
            }
            int sum = stock[beginIndex];
            if (sum >= residual) {
                stock[beginIndex] = sum - residual;
                stock[endIndex] = capacity[endIndex];
                return residual;
            } else {
                stock[beginIndex] = 0;
                stock[endIndex] += sum;
                return sum;
            }
        }

        int[][] canJug() {
            return canJug(this.stock);
        }

        private int[][] canJug(int[] stock) {
            int[][] result = new int[3][];
            for (int i = 0; i < 3; i++) {
                result[i] = canJug(i, stock);
            }
            return result;
        }

        private int[] canJug(int index, int[] stock) {
            int s = stock[index];
            if (s == 0) {
                return null;
            }
            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                if (i == index) {
                    continue;
                }
                if (capacity[i] - stock[i] > 0) {
                    result.add(i);
                }
            }
            if (result.isEmpty()) {
                return null;
            }
            int len = result.size();
            int[] array = new int[len];
            for (int i = 0; i < len; i++) {
                array[i] = result.get(i);
            }
            return array;
        }

        int getGap() {
            return d - minGap(this.stock);
        }

        int getJug() {
            return jug;
        }

        private int minGap(int[] stock) {
            int s;
            int closest = -1;
            int gap;
            int r = -1;
            for (int i = 0; i < 3; i++) {
                s = stock[i];
                if (s == d) {
                    return d;
                } else {
                    gap = Math.abs(s - d);
                    if (closest == -1 || gap < closest) {
                        closest = gap;
                        r = s;
                    }
                }
            }
            return r;
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
            return this.hashCode;
        }
    }
}
