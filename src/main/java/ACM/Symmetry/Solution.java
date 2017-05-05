package ACM.Symmetry;

import basic.Util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/5
 * Time:13:23
 * <p>
 * 给出平面上N（N≤1000）个点，问是否可以找到一条竖线，使得所有点左右对称。例如
 * 图5-6中，左边的图形有对称轴，右边没有。
 * <p>
 * Symmetry, ACM/ICPC Seoul 2004, UVa1595
 */
public class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("------------------------------------------------------------------------------------------------------");
        solution.case2();
        System.out.println("------------------------------------------------------------------------------------------------------");
        solution.case3();
    }

    private void case1() {
        Point[] points = {new Point(-2, 5), new Point(6, 5), new Point(2, 3), new Point(0, 0), new Point(4, 0)};
        find(points);
    }

    private void case2() {
        Point[] points = {new Point(0, 4), new Point(2, 3), new Point(0, 0), new Point(4, 0)};
        find(points);
    }

    private void case3() {
        Point[] points = generatorOne();
        find(points);
    }

    private Point[] generatorOne() {
        int len = Util.getRandomInteger(1, 1000);
        Point[] points = new Point[len];
        for (int i = 0; i < len; i++) {
            points[i] = generatorOnePoint();
        }
        return points;
    }

    private Point generatorOnePoint() {
        int x = Util.getRandomInteger(-100, 100);
        int y = Util.getRandomInteger(-100, 100);
        return new Point(x, y);
    }

    private void find(Point[] points) {
        System.out.println("points:" + Arrays.toString(points));
        Set<Float> maybes = new HashSet<>();
        Set<Point> pointSet = new HashSet<>();
        int len = points.length;
        for (int i = 0; i < len; i++) {
            maybes.add(points[i].getX());
            pointSet.add(points[i]);
            for (int j = i + 1; j < len; j++) {
                maybes.add(points[j].getX());
                if (points[i].withLine(points[j])) {
                    maybes.add(points[i].middleX(points[j]));
                }
            }
        }
        for (Float X : maybes) {
            if (match(X, pointSet)) {
                System.out.println("line x :" + X);
                return;
            }
        }
        System.out.println("line not found.");
    }

    private boolean match(Float X, Set<Point> points) {
        for (Point point : points) {
            float cX = point.getX();
            if (cX != X) {
                Point towardPoint = new Point(2 * X - cX, point.getY());
                if (!points.contains(towardPoint)) {
                    return false;
                }
            }
        }
        return true;
    }

    private class Point {
        private String no;
        private float x;
        private float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
            this.no = x + "" + y;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public String getNo() {
            return no;
        }

        public boolean withLine(Point other) {
            return this.getY() == other.getY();
        }

        public float middleX(Point other) {
            return (this.getX() + other.getX()) / 2;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj instanceof Point) {
                Point other = (Point) obj;
                return this.getNo().equals(other.getNo());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return this.getNo().hashCode();
        }

        @Override
        public String toString() {
            return "(" + this.getX() + "," + this.getY() + ")";
        }
    }
}
