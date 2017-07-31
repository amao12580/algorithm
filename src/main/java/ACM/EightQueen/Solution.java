package ACM.EightQueen;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/7/29
 * Time:11:17
 * <p>
 * spec link:https://zh.wikipedia.org/wiki/%E5%85%AB%E7%9A%87%E5%90%8E%E9%97%AE%E9%A2%98
 * <p>
 * 八皇后问题
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("----------------------------------------------------");
        solution.case2();
    }

    private void case1() {//92
        queen(8);
    }

    private void case2() {//14200
        queen(12);
    }

    private void queen(int num) {
        boolean[] row = new boolean[num];//横向
        boolean[] column = new boolean[num];//纵向
        List<Point> points = new ArrayList<>();
        queen(row, column, points, num);
        System.out.println(num + " queen question has " + resultSet.size() + " solution.");
    }


    private void queen(boolean[] rows, boolean[] columns, List<Point> points, int num) {
        if (points.size() == num) {
            addChess(points);
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
                        currentPoints.add(createPoint(i, j));
                        if (checkAllPeace(currentPoints)) {
                            queen(rows, columns, currentPoints, num);
                        }
                        columns[j] = false;
                    }
                }
                rows[i] = false;
            }
        }
    }

    Map<String, Point> pointMap = new HashMap<>();

    private Point createPoint(int i, int j) {
        String key = i + "," + j;
        Point cache = pointMap.get(key);
        if (cache == null) {
            cache = new Point(i, j);
            pointMap.put(key, cache);
        }
        return cache;
    }

    private void addChess(List<Point> points) {
        Collections.sort(points);
        StringBuilder builder = new StringBuilder();
        for (Point point : points) {
            builder = builder.append(point.toString()).append(" ");
        }
        if (resultSet.add(builder.toString())) {
            System.out.println(builder.toString());
        }
    }

    Set<String> resultSet = new HashSet<>();

    private boolean checkAllPeace(List<Point> points) {
        return points.size() == 1 || checkAllPeace(points, points.size(), 0);
    }

    private boolean checkAllPeace(List<Point> points, int size, int leftIndex) {
        if (leftIndex == size) {
            return true;
        }
        Point current = points.get(leftIndex);
        for (int i = leftIndex + 1; i < size; i++) {
            if (!checkAllPeace(current, points.get(i))) {
                return false;
            }
        }
        return checkAllPeace(points, size, leftIndex + 1);
    }

    private boolean checkAllPeace(Point left, Point right) {
        if (left.getRowIndex() > right.getRowIndex() && left.getColumnIndex() > right.getColumnIndex()) {
            return left.getRowIndex() - right.getRowIndex() != left.getColumnIndex() - right.getColumnIndex();
        }

        if (left.getRowIndex() < right.getRowIndex() && left.getColumnIndex() < right.getColumnIndex()) {
            return left.getRowIndex() - right.getRowIndex() != left.getColumnIndex() - right.getColumnIndex();
        }

        return left.getRowIndex() - right.getRowIndex() + left.getColumnIndex() - right.getColumnIndex() != 0;
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
        return "(" + (rowIndex + 1) + "," + (columnIndex + 1) + ")";
    }

    @Override
    public int compareTo(Point other) {
        if (this.getRowIndex() != other.getRowIndex()) {
            return this.getRowIndex() - other.getRowIndex();
        }
        return this.getColumnIndex() - other.getColumnIndex();
    }
}
