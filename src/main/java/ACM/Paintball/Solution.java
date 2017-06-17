package ACM.Paintball;

import basic.Util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/6/16
 * Time:12:45
 * <p>
 * 有一个1000×1000的正方形战场，战场西南角的坐标为(0,0)，西北角的坐标为(0,1000)。
 * 战场上有n（0≤n≤1000）个敌人，第i个敌人的坐标为(x i ,y i )，攻击范围为r i 。为了避开敌人的
 * 攻击，在任意时刻，你与每个敌人的距离都必须严格大于它的攻击范围。你的任务是从战场
 * 的西边（x=0的某个点）进入，东边（x=1000的某个点）离开。如果有多个位置可以进/出，
 * 你应当求出最靠北的位置。输入每个敌人的x i 、y i 、r i ，输出进入战场和离开战场的坐标。
 * <p>
 * Paintball, UVa 11853
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2953
 * Sample Input
 * 3
 * 500 500 499
 * 0 0 999
 * 1000 1000 200
 * Sample Output
 * 0.00 1000.00 1000.00 800.00
 * <p>
 * <p>
 * <p>
 * 这道题不能将战场切分成1000*1000的单元格进行floodFill,因为敌人的位置与攻击范围都是实数(double)，
 * <p>
 * 会出现“缝隙”问题，1*1的单元格可能只有一部分的面积可以被攻击到，这部分到底算不算连通呢？
 * <p>
 * 进一步*2扩大为2000*2000也没用，总会有类似的问题出现。这个思路就不对了
 * <p>
 * 不应该进行等比切分，要按照圆圈之间的连通性来判断整体连通性，即是否存在一个“连通环”？
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
    }

    private void case1() {
        double[][] desc = {{500, 500, 499}, {0, 0, 999}, {1000, 1000, 200}};
        way(desc);
    }

    protected int width = 1000;
    private int high = 1000;

    private void way(double[][] desc) {
        List<Enemy> allEnemy = new ArrayList<>();
        for (double[] enemy : desc) {
            allEnemy.add(new Enemy(enemy[0], enemy[1], enemy[2]));
        }
        //找到可以攻击到最北面的那条线的所有敌人位置
        List<Enemy> crossWithNorthEdgeEnemy = getCrossWithNorthEdge(allEnemy);
        if (crossWithNorthEdgeEnemy.isEmpty()) {//北面畅通
            findNorthernmostWay(allEnemy);
        } else {
            if (isBlockNorth2South(crossWithNorthEdgeEnemy, allEnemy)) {//北面与南面完全被隔开了，不可能有路
                System.out.println("no way.");
                return;
            }
            findNorthernmostWay(allEnemy);
        }
    }

    /**
     * 是否存在一个连通环，将南北两条线完全连接，形成一条通路呢？
     */
    private boolean isBlockNorth2South(List<Enemy> crossWithNorthEdgeEnemy, List<Enemy> allEnemy) {
        Set<Enemy> hasVisit;
        for (Enemy enemy : crossWithNorthEdgeEnemy) {
            hasVisit = new HashSet<>();
            hasVisit.add(enemy);
            if (isBlockNorth2South(enemy, allEnemy, hasVisit, enemy.getSouthernmostY())) {
                return true;
            }
        }
        return false;
    }

    private boolean isBlockNorth2South(Enemy crossWithNorthEdgeEnemy, List<Enemy> allEnemy, Set<Enemy> hasVisit, double southernmostY) {
        if (southernmostY <= 0) {
            return true;
        }
        for (Enemy enemy : allEnemy) {
            if (hasVisit.contains(enemy)) {
                continue;
            }
            Double cross = crossWithNorthEdgeEnemy.crossSouthernmost(enemy);
            hasVisit.add(enemy);
            if (cross == null) {//不相交
                continue;
            }
            if (cross < southernmostY) {
                southernmostY = cross;
            }
            if (isBlockNorth2South(enemy, allEnemy, hasVisit, southernmostY)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 找到最北边的起点和终点
     */
    private void findNorthernmostWay(List<Enemy> allEnemy) {
        double westY = high;
        Double[] crossWest;
        double westUp, westDown;
        for (Enemy enemy : allEnemy) {
            crossWest = enemy.crossWithWestEdge();
            if (crossWest == null) {
                continue;
            }
            westUp = crossWest[0];
            westDown = crossWest[1];
            if (westUp >= high && westDown <= 0) {
                System.out.println("no way.");
                return;
            }
            if (westUp >= high && westDown > 0) {
                if (westDown < westY) {
                    westY = westDown;
                }
            }
        }

        double eastY = high;
        Double[] crossEast;
        double eastUp, eastDown;
        for (Enemy enemy : allEnemy) {
            crossEast = enemy.crossWithEastEdge(width);
            if (crossEast == null) {
                continue;
            }
            eastUp = crossEast[0];
            eastDown = crossEast[1];
            if (eastUp >= high && eastDown <= 0) {
                System.out.println("no way.");
                return;
            }
            if (eastUp >= high && eastDown > 0) {
                if (eastDown < westY) {
                    eastY = eastDown;
                }
            }
        }
        System.out.println("0.00 " + westY + " 1000.00 " + eastY);
    }

    /**
     * 找到所有可以攻击到最北边那条线的敌人
     */
    private List<Enemy> getCrossWithNorthEdge(List<Enemy> allEnemy) {
        return allEnemy.stream().filter(enemy -> enemy.canCrossWithNorthEdge(high)).collect(Collectors.toList());
    }
}

class Enemy {
    private String uniqueCode;
    private Location location;
    private double range;

    public Location getLocation() {
        return location;
    }

    public double getRange() {
        return range;
    }

    public Enemy(double x, double y, double range) {
        this.location = new Location(x, y);
        this.range = range;
        this.uniqueCode = Util.contactAll(this.getLocation().getX(), ".", this.getLocation().getY(), ".", this.getRange());
    }

    /**
     * 是否可以攻击到最北边的那条线
     */
    public boolean canCrossWithNorthEdge(double yMax) {
        return this.getLocation().getY() + this.getRange() >= yMax;
    }

    /**
     * 是否可以攻击到最西边的那条线，
     * <p>
     * 如果可以，返回攻击点的最北的Y轴坐标，和最南的Y轴坐标
     * <p>
     * 如果不可以，返回null
     */
    public Double[] crossWithWestEdge() {
        double x = this.getLocation().getX();
        double y = this.getLocation().getY();
        double westernmost = x - this.getRange();
        if (westernmost > 0) {//无法与最西边这条线交叉
            return null;
        }
        Double[] result = new Double[2];
        if (westernmost == 0) {//相切
            result[0] = y;
            result[1] = y;
            return result;
        }
        //交叉
        //已知斜边和一条直角边的长度，求另一条直角边的长度
        double otherLegLength = Math.sqrt(this.getRange() * this.getRange() - x * x);
        result[0] = y + otherLegLength;//北边的交汇点
        result[1] = y - otherLegLength;//南边的交汇点
        return result;
    }

    /**
     * 是否可以攻击到最东边的那条线，
     * <p>
     * 如果可以，返回攻击点的最北的Y轴坐标，和最南的Y轴坐标
     * <p>
     * 如果不可以，返回null
     */
    public Double[] crossWithEastEdge(double xMax) {
        double x = this.getLocation().getX();
        double y = this.getLocation().getY();
        double easternmost = x + this.getRange();
        if (easternmost < xMax) {//无法与最东边这条线交叉
            return null;
        }
        Double[] result = new Double[2];
        if (easternmost == xMax) {//相切
            result[0] = y;
            result[1] = y;
            return result;
        }
        //交叉
        //已知斜边和一条直角边的长度，求另一条直角边的长度
        double legLength = xMax - x;
        double otherLegLength = Math.sqrt(this.getRange() * this.getRange() - legLength * legLength);
        result[0] = y + otherLegLength;//北边的交汇点
        result[1] = y - otherLegLength;//南边的交汇点
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Enemy) {
            Enemy other = (Enemy) o;
            return this.hashCode() == other.hashCode();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return uniqueCode.hashCode();
    }

    public double getSouthernmostY() {
        return this.getLocation().getY() - this.getRange();
    }

    /**
     * 判断两个敌人的攻击范围是否存在重叠
     * <p>
     * 不存在，返回null
     * <p>
     * 存在，返回两个敌人中的最南边的Y轴坐标值
     */
    public Double crossSouthernmost(Enemy other) {
        double distance = this.getLocation().distance(other.getLocation());
        double _2range = this.getRange() + other.getRange();
        if (distance > _2range) {
            return null;
        }
        double thisY = this.getSouthernmostY();
        double otherY = other.getSouthernmostY();
        if (thisY < otherY) {
            return thisY;
        }
        return otherY;
    }
}

class Location {
    private double x;
    private double y;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 计算两点之间的距离
     */
    public double distance(Location other) {
        double x = this.getX() - other.getX();
        double y = this.getY() - other.getY();
        return Math.sqrt(x * x + y * y);
    }
}