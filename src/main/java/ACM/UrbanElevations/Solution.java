package ACM.UrbanElevations;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/6/3
 * Time:15:57
 * <p>
 * 如图5-4所示，有n（n≤100）个建筑物。左侧是俯视图（左上角为建筑物编号，右下角
 * 为高度），右侧是从南向北看的正视图。
 * <p>
 * 输入每个建筑物左下角坐标（即x、y坐标的最小值）、宽度（即x方向的长度）、深度
 * （即y方向的长度）和高度（以上数据均为实数），输出正视图中能看到的所有建筑物，按
 * 照左下角x坐标从小到大进行排序。左下角x坐标相同时，按y坐标从小到大排序。
 * 输入保证不同的x坐标不会很接近（即任意两个x坐标要么完全相同，要么差别足够大，
 * 不会引起精度问题）。
 * <p>
 * Urban Elevations, ACM/ICPC World Finals 1992, UVa221
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=157
 * <p>
 * Sample Input
 * 14
 * 160 0 30 60 30
 * 125 0 32 28 60
 * 95 0 27 28 40
 * 70 35 19 55 90
 * 0 0 60 35 80
 * 0 40 29 20 60
 * 35 40 25 45 80
 * 0 67 25 20 50
 * 0 92 90 20 80
 * 95 38 55 12 50
 * 95 60 60 13 30
 * 95 80 45 25 50
 * 165 65 15 15 25
 * 165 85 10 15 35
 * 0
 * Sample Output
 * For map #1, the visible buildings are numbered as follows:
 * 5 9 4 3 10 2 1 14
 * <p>
 * 重要笔记：
 * <p>
 * 针对某建筑物C，在从南向北的视图时，这个截面在横向X轴区间值为（x1,x2）z轴值为h,,其前面可能会有N个建筑物对其遮盖,造成区间值分隔为M个子区间（M<=2N+1）.
 * <p>
 * 则建筑物C是否可见的条件是：M个子区间，是否至少存在一个子区间形成的截面是可见的，遍历M个子区间，每个取中间值x3，高度取C的高度h，形成一个点P(x3,h)，
 * <p>
 * 则原始是否可见的条件转化为，是否至少存在一个点P不被前面的所有建筑物遮盖。
 * <p>
 * 这就将原来建筑物C的可见性，从一个南墙截面可见性，转化为至多M个点的可见性。截面的可见性，由于X轴与Z轴的无穷性，是不可能通过穷举来验证的。
 * <p>
 * 转化的过程中，将无穷的问题转化为有限的问题，是一个去伪存真抓本质的过程，也就是离散化的中心思想。
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
    }

    private void case1() {
        double[][] desc = {
                {160, 0, 30, 60, 30},
                {125, 0, 32, 28, 60},
                {95, 0, 27, 28, 40},
                {70, 35, 19, 55, 90},
                {0, 0, 60, 35, 80},
                {0, 40, 29, 20, 60},
                {35, 40, 25, 45, 80},
                {0, 67, 25, 20, 50},
                {0, 92, 90, 20, 80},
                {95, 38, 55, 12, 50},
                {95, 60, 60, 13, 30},
                {95, 80, 45, 25, 50},
                {165, 65, 15, 15, 25},
                {165, 85, 10, 15, 35}
        };
        elevation(desc);
    }

    private void elevation(double[][] desc) {
        int dLen = desc.length;
        List<Cube> cubes = new ArrayList<>();
        for (int i = 0; i < dLen; i++) {
            cubes.add(new Cube(i + 1, desc[i][0], desc[i][1], desc[i][2], desc[i][3], desc[i][4]));
        }
        if (cubes.size() == 1) {
            System.out.println(cubes.toString());
            return;
        }
        LinkedList<Cube> orderByY = new LinkedList<>();
        orderByY.addAll(cubes);
        orderBy = 0;
        Collections.sort(orderByY);
        LinkedList<Cube> orderByXY = new LinkedList<>();
        orderByXY.addAll(cubes);
        orderBy = 2;
        Collections.sort(orderByXY);
        LinkedList<Cube> result = new LinkedList<>();
        elevation(orderByY, orderByXY, result, orderByXY.poll());
        System.out.println(result.toString());
    }

    private void elevation(LinkedList<Cube> orderByY, LinkedList<Cube> orderByXY, LinkedList<Cube> result, Cube current) {
        if (current == null) {
            return;
        }
        if (!cover(current, orderByY)) {
            result.add(current);
        }
        elevation(orderByY, orderByXY, result, orderByXY.poll());
    }

    private boolean cover(Cube current, LinkedList<Cube> orderByY) {
        LinkedList<Cube> before = getBefore(current, orderByY);
        if (before.isEmpty()) {
            return false;
        }
        LinkedList<Double> ranges = new LinkedList<>();
        double min = current.getX().getStart();
        double max = current.getX().getEnd();
        ranges.add(min);
        ranges.add(max);
        for (Cube cube : before) {
            if (cube.getX().getStart() > min) {
                ranges.add(cube.getX().getStart());
            }
            if (cube.getX().getEnd() < max) {
                ranges.add(cube.getX().getEnd());
            }
        }
        Collections.sort(ranges);
        LinkedHashSet<Double> rangeSet = new LinkedHashSet<>();
        rangeSet.addAll(ranges);
        ranges.clear();
        ranges.addAll(rangeSet);
        return cover(ranges.poll(), ranges.poll(), current.getZ(), ranges, before);
    }

    private boolean cover(Double s, Double e, double high, LinkedList<Double> ranges, LinkedList<Cube> before) {
        boolean isCover = false;
        double middle = (s + e) / 2;
        for (Cube cube : before) {
            if (cube.getX().getStart() <= middle && cube.getX().getEnd() >= middle && cube.getZ() >= high) {
                isCover = true;
                break;
            }
        }
        return isCover && (ranges.peek() == null || cover(e, ranges.poll(), high, ranges, before));
    }

    private LinkedList<Cube> getBefore(Cube current, LinkedList<Cube> orderByY) {
        LinkedList<Cube> result = new LinkedList<>();
        orderBy = 0;
        for (Cube cube : orderByY) {
            if (cube.equals(current)) {
                return result;
            }
            if (cube.compareTo(current) < 0 && !(cube.getX().getEnd() < current.getX().getStart() //在current之前，并且两个x区间存在交叉
                    || cube.getX().getStart() > current.getX().getEnd())) {
                result.add(cube);
            }
        }
        return result;
    }

    private Byte orderBy;

    private class Cube implements Comparable<Cube> {
        private int no;
        private String code;
        private Line x;
        private Line y;
        private double z;

        public Cube(int no, double x_start, double y_start, double x_offset, double y_offset, double z) {
            this.x = new Line(x_start, x_offset);
            this.y = new Line(y_start, y_offset);
            this.z = z;
            this.no = no;
            this.code = x.getNo().concat(".").concat(y.getNo()).concat(".").concat(String.valueOf(z));
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
            return code.hashCode();
        }

        public Line getY() {
            return y;
        }

        public Line getX() {
            return x;
        }

        public double getZ() {
            return z;
        }

        @Override
        public int compareTo(Cube other) {
            if (orderBy != null && orderBy == 0) {
                double key = this.getY().getStart() - other.getY().getStart();
                return key > 0 ? 1 : key == 0 ? 0 : -1;
            } else if (orderBy != null && orderBy == 1) {
                double key = this.getX().getStart() - other.getX().getStart();
                return key > 0 ? 1 : key == 0 ? 0 : -1;
            } else if (orderBy != null && orderBy == 2) {
                double key = this.getX().getStart() - other.getX().getStart();
                if (key == 0) {
                    key = this.getY().getStart() - other.getY().getStart();
                    return key > 0 ? 1 : key == 0 ? 0 : -1;
                } else {
                    return key > 0 ? 1 : -1;
                }
            } else {
                throw new IllegalArgumentException();
            }
        }

        @Override
        public String toString() {
            return String.valueOf(no);
        }
    }

    private class Line {
        private String no;
        private double start;
        private double end;

        public Line(double start, double offset) {
            this.start = start;
            this.end = start + offset;
            this.no = String.valueOf(start).concat(".").concat(String.valueOf(end));
        }

        public String getNo() {
            return no;
        }

        public double getStart() {
            return start;
        }

        public double getEnd() {
            return end;
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
            return no.hashCode();
        }
    }
}
