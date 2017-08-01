package ACM.EightQueen;

import basic.Util;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/7/29
 * Time:11:17
 * <p>
 * spec link:    https://zh.wikipedia.org/wiki/%E5%85%AB%E7%9A%87%E5%90%8E%E9%97%AE%E9%A2%98
 * <p>
 * 八皇后问题
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("----------------------------------------------------");
        solution.case2();
        System.out.println("----------------------------------------------------");
        solution.case3();
    }

    private void case1() {//92
        queen(8);
    }

    private void case2() {//14200
        queen(12);
    }

    private void case3() {//2
        queen(4);
    }

    private void queen(int num) {
        boolean[] row = new boolean[num];//横向
        boolean[] column = new boolean[num];//纵向
        List<Point> points = new ArrayList<>();
        queen(row, column, points, num);
        System.out.println(Util.contactAll(num, " queen question has ", results.size(), " solution."));
        results.clear();
    }


    private void queen(boolean[] rows, boolean[] columns, List<Point> points, int num) {
        if (points.size() == num) {
            addResult(points);
            points.clear();
            return;
        }
        List<Point> currentPoints;
        for (int i = 0; i < num; i++) {
            if (!rows[i]) {
                rows[i] = true;
                for (int j = 0; j < num; j++) {
                    if (!columns[j]) {
                        columns[j] = true;
                        currentPoints = new ArrayList<>(points);
                        if (checkAllPeace(currentPoints, createPoint(i, j))) {
                            queen(rows, columns, currentPoints, num);
                        }
                        columns[j] = false;
                    }
                }
                rows[i] = false;
            }
        }
    }

    Map<String, Point> pointCache = new HashMap<>();

    private Point createPoint(int i, int j) {
        String key = Util.contactAll(i, ",", j);
        Point cache = pointCache.get(key);
        if (cache == null) {
            cache = new Point(i, j);
            pointCache.put(key, cache);
        }
        return cache;
    }

    private void addResult(List<Point> points) {
        Collections.sort(points);
        StringBuilder builder = new StringBuilder();
        for (Point point : points) {
            builder = builder.append(point.toString()).append(" ");
        }
        if (results.add(builder.toString())) {
            System.out.println(builder.toString());
        }
    }

    Set<String> results = new HashSet<>();

    private boolean checkAllPeace(List<Point> points, Point last) {
        for (Point point : points) {
            if (!checkAllPeace(point, last)) {
                return false;
            }
        }
        points.add(last);
        return true;
    }

    private boolean checkAllPeace(Point left, Point right) {
        int lr2rr = left.getRowIndex() - right.getRowIndex();
        int lc2rc = left.getColumnIndex() - right.getColumnIndex();
        if (lr2rr > 0 && lc2rc > 0) {
            return lr2rr != lc2rc;
        }
        if (lr2rr < 0 && lc2rc < 0) {
            return lr2rr != lc2rc;
        }
        return lr2rr + lc2rc != 0;
    }
}

class Point implements Comparable<Point> {
    private int rowIndex;
    private int columnIndex;

    public Point(int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    @Override
    public String toString() {
        return Util.contactAll("(", rowIndex + 1, ",", columnIndex + 1, ")");
    }

    @Override
    public int compareTo(Point other) {
        if (this.getRowIndex() != other.getRowIndex()) {
            return this.getRowIndex() - other.getRowIndex();
        }
        return this.getColumnIndex() - other.getColumnIndex();
    }
}
