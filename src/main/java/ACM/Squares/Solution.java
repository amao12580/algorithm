package ACM.Squares;

import basic.Util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/4/13
 * Time:15:41
 * <p>
 * 有n行n列（2≤n≤9）的小黑点，还有m条线段连接其中的一些黑点。统计这些线段连成
 * 了多少个正方形（每种边长分别统计）。
 * 行从上到下编号为1～n，列从左到右编号为1～n。边用H i j和V i j表示，分别代表边
 * (i,j)-(i,j+1)和(i,j)-(i+1,j)。如图4-5所示最左边的线段用V 1 1表示。图中包含两个边长为1的正
 * 方形和一个边长为2的正方形。
 * <p>
 * Squares, ACM/ICPC World Finals 1990, UVa201
 */
public class Solution {
    private int len;

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.generatorOne();
        System.out.println("----------------------------------------------------------------");
        solution.print();
        System.out.println("----------------------------------------------------------------");
        solution.squares();
        Map<Integer, Integer> counts = solution.counts;
        int size = counts.size();
        System.out.println("size:" + size);
        for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
            Integer item = entry.getKey();
            if (item != null) {
                System.out.println("length:" + item + ",count:" + entry.getValue());
            }
        }
    }

    private void print() {
        String nP = ".";
        String p = "#";
        String rSpace = "     ";
        int mylen = len + 1;
        for (int i = 1; i <= mylen; i++) {
            for (int j = 1; j <= mylen; j++) {
                Point c = new Point(i, j);
                if (points.contains(c)) {
                    System.out.print(p);
                } else {
                    System.out.print(nP);
                }
                System.out.print(rSpace);
            }
            System.out.println();
            System.out.println();
        }
    }

    private Map<Integer, Integer> counts = new HashMap<>();

    private void squares() {
        for (int i = 1; i <= len; i++) {
            int mayBe = len - i + 1;
            for (int j = 1; j <= len; j++) {
                Point c = new Point(i, j);
                if (points.contains(c)) {
                    int max = len - j + 1;
                    if (mayBe < max) {
                        max = mayBe;
                    }
                    while (max > 0) {
                        squares(c, max);
                        max--;
                    }
                }
            }
        }
    }

    private void squares(Point leftUp, int len) {
        int leftUpRowIndex = leftUp.getRowIndex();
        int leftUpColumnIndex = leftUp.getColumnIndex();
        int rightUpColumnIndex = leftUpRowIndex + len;
        Point rightUp = new Point(leftUpRowIndex, rightUpColumnIndex);
        if (!points.contains(rightUp) || !checkIsLine(leftUp, rightUp)) {
            return;
        }
        int rightDownRowIndex = leftUpRowIndex + len;
        Point rightDown = new Point(rightDownRowIndex, rightUpColumnIndex);
        if (!points.contains(rightDown) || !checkIsLine(rightUp, rightDown)) {
            return;
        }
        int leftDownRowIndex = leftUpRowIndex + len;
        Point leftDown = new Point(leftDownRowIndex, leftUpColumnIndex);
        if (!points.contains(leftDown) || !checkIsLine(rightDown, leftDown)) {
            return;
        }
        if (!checkIsLine(leftUp, leftDown)) {
            return;
        }
        counts.put(len, counts.getOrDefault(len, 0) + 1);
    }

    private boolean checkIsLine(Point start, Point end) {
        return lines.contains(new Line(start, end));
    }

    private class Point {
        private int rowIndex;
        private int columnIndex;
        private String uniqueCode = null;

        public Point(int rowIndex, int columnIndex) {
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;
            this.uniqueCode = rowIndex + "" + columnIndex;
        }

        public int getRowIndex() {
            return rowIndex;
        }

        public int getColumnIndex() {
            return columnIndex;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o instanceof Point) {
                Point other = (Point) o;
                return uniqueCode.equals(other.getUniqueCode());
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return uniqueCode.hashCode();
        }

        public String getUniqueCode() {
            return uniqueCode;
        }
    }

    private class Line {
        private Point start;
        private Point end;
        private String uniqueCode = null;

        public Line(Point start, Point end) {
            int s = Integer.valueOf(start.getUniqueCode());
            int e = Integer.valueOf(end.getUniqueCode());
            if (s > e) {//低位在前，高位在后
                this.start = end;
                this.end = start;
            } else {
                this.start = start;
                this.end = end;
            }
            uniqueCode = this.start.getUniqueCode() + this.end.getUniqueCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o instanceof Line) {
                Line other = (Line) o;
                return this.hashCode() == other.hashCode();
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return uniqueCode.hashCode();
        }
    }

    private Set<Line> lines = new HashSet<>();
    private Set<Point> points = new HashSet<>();

    private void generatorOne() {
//        len = Util.getRandomInteger(3, 9);
        len = 8;
        System.out.println("len:" + len);
        int lineMaxNum = ((len * (len + 1)) / 2) * ((len + 1) * 2);
        System.out.println("lineMaxNum:" + lineMaxNum);
        int lineNum = Util.getRandomInteger(lineMaxNum / 20 > 0 ? lineMaxNum / 20 : 1, lineMaxNum * 2 / 3);//二十分之一 至 三分之二的线要出现
        System.out.println("lineNum:" + lineNum);
        while (lineNum > 0) {
            Point start = findRandomPoint();
            Point end = findRandomPoint(start);//不允许斜线
            Line line = new Line(start, end);
            if (lines.add(line)) {
                points.add(start);
                points.add(end);
                lineNum--;
            }
        }
        System.out.println("lines size:" + lines.size());
        System.out.println("points size:" + points.size());
    }

    private Point findRandomPoint(Point start) {
        boolean c = Util.getRandomBoolean();
        int cIndex;
        int rIndex;
        if (c) {
            cIndex = start.getColumnIndex();
            rIndex = getRandomIndex(start.getRowIndex());
        } else {
            rIndex = start.getRowIndex();
            cIndex = getRandomIndex(start.getColumnIndex());
        }
        return new Point(rIndex, cIndex);
    }

    private int getRandomIndex(int index) {
        int c = Util.getRandomInteger(1, len);
        if (c == index) {
            return getRandomIndex(index);
        }
        return c;
    }

    private Point findRandomPoint() {
        return new Point(Util.getRandomInteger(1, len), Util.getRandomInteger(1, len));
    }
}
