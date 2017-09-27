package ACM.ZombiesTreasureChest;

import basic.Util;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/9/27
 * Time:9:28
 * <p>
 * 你有一个体积为N的箱子和两种数量无限的宝物。宝物1的体积为S1，价值为V1；宝物2
 * 的体积为S2，价值为V2。输入均为32位带符号整数。你的任务是计算最多能装多大价值的宝
 * 物。例如，n=100，S1=V1=34，S2=5，V2=3，答案为86，方案是装两个宝物1，再装6个宝物
 * 2。每种宝物都必须拿非负整数个。
 * <p>
 * Zombie's Treasure Chest, Shanghai 2011, UVa12325
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=278&page=show_problem&problem=3747
 * <p>
 * Sample Input
 * 2
 * 100 1 1 2 2
 * 100 34 34 5 3
 * Sample Output
 * Case #1: 100
 * Case #2: 86
 * <p>
 * 背包问题：动态规划
 */
public class Solution {
    private int N;
    private int V = 0;

    public static void main(String[] args) {
        new Solution().case1();
        new Solution().case2();
        new Solution().case3();
    }

    private void case1() {
        pack(100, 1, 1, 2, 2);
    }

    private void case2() {
        pack(100, 34, 34, 5, 3);
    }

    private void case3() {
        pack(Util.getRandomInteger(10, 10 * 10000), Util.getRandomInteger(10, 10 * 10000),
                Util.getRandomInteger(10, 10 * 10000), Util.getRandomInteger(10, 10 * 10000), Util.getRandomInteger(10, 10 * 10000));
    }

    private void pack(int N, int S1, int V1, int S2, int V2) {
        System.out.println("N:" + N + ",S1:" + S1 + ",V1:" + V1 + ",S2:" + S2 + ",V2:" + V2);
        long s = System.currentTimeMillis();
        this.N = N;
        Set<Gem> gems = new HashSet<>();
        buildGem(gems, S1, V1);
        buildGem(gems, S2, V2);
        LinkedList<Gem> gemList = new LinkedList<>(gems);
        Collections.sort(gemList);
        pack(gemList);
        System.out.println("N:" + this.N + ",V:" + this.V);
        System.out.println("time:" + (System.currentTimeMillis() - s));
        System.out.println("--------------------------------------------");

    }

    private void pack(LinkedList<Gem> gems) {
        Gem gem;
        int S;
        while (!gems.isEmpty()) {
            gem = gems.poll();
            S = gem.getS();
            if (S <= this.N) {
                this.V += gem.getV();
                this.N -= S;
                gems.addFirst(gem);//不限量
            }
        }
    }

    private void buildGem(Set<Gem> gems, int s, int v) {
        gems.add(new Gem(s, v));
    }

    class Gem implements Comparable<Gem> {
        int hashCode;
        int S;//体积
        int V;//价值

        Gem(int s, int v) {
            this.S = s;
            this.V = v;
            this.hashCode = Util.contactAll(',', s, v).hashCode();
        }

        int getS() {
            return S;
        }

        int getV() {
            return V;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o instanceof Gem) {
                Gem other = (Gem) o;
                return this.hashCode() == other.hashCode();
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        @Override
        public int compareTo(Gem other) {
            return (other.getV() / other.getS()) - (this.getV() / this.getS());
        }
    }
}