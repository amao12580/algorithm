package ACM.Xiangqi;

import basic.Util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/4/13
 * Time:15:42
 * <p>
 * 考虑一个象棋残局，其中红方有n（2≤n≤7）个棋子，黑方只有一个将。红方除了有一个
 * 帅（G）之外还有3种可能的棋子：车（R），马（H），炮（C），并且需要考虑“蹩马
 * 腿”（如图4-4所示）与将和帅不能照面（将、帅如果同在一条直线上，中间又不隔着任何棋
 * 子的情况下，走子的一方获胜）的规则。
 * 输入所有棋子的位置，保证局面合法并且红方已经将军。你的任务是判断红方是否已经
 * 把黑方将死。关于中国象棋的相关规则请参见原题。
 * <p>
 * Xiangqi, ACM/ICPC Fuzhou 2011, UVa1589
 */
public class Solution {
    private int rowLen = 0;
    private int columnLen = 0;
    private Role[][] chess = null;
    private Point GPoint = null;
    private Point JPoint = null;
    private List<Point> RPoints = new LinkedList<>();
    private List<Point> HPoints = new LinkedList<>();
    private List<Point> CPoints = new LinkedList<>();

    public void setRowLen(int rowLen) {
        this.rowLen = rowLen;
    }

    public void setColumnLen(int columnLen) {
        this.columnLen = columnLen;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.setRowLen(Util.getRandomInteger(10, 15));
        solution.setColumnLen(Util.getRandomInteger(5, 10));
        System.out.println("rowLen:" + solution.rowLen + ",columnLen:" + solution.columnLen);
        solution.generatorChess();
        System.out.println("------------------------------------------------------");
        solution.print();
        System.out.println("------------------------------------------------------");
        System.out.println("result:" + solution.judge());
    }

    private String judge() {
        String RW = "Red win.";//黑方死棋了
        String BW = "Black win.";//红方丢掉帅，将与帅在一条线上，中间无隔子
        String FW = "Fake waring.";//红方没有对黑方将军
        String BL = "Black can live.";//黑方没有死棋
        if (checkBlackWin()) {
            return BW;
        }
        if (checkFakeWaring()) {
            return FW;
        }
        int rIndex = JPoint.getRowIndex();
        int cIndex = JPoint.getColumnIndex();
        //上
        if (rIndex - 1 >= 0) {
            swapJPoint(rIndex - 1, cIndex);
            if (checkFakeWaring() && !checkBlackWin()) {
                return BL;
            } else {
                swapJPoint(rIndex, cIndex);
            }
        }
        //下
        if (rIndex + 1 <= rowLen - 1) {
            swapJPoint(rIndex + 1, cIndex);
            if (checkFakeWaring() && !checkBlackWin()) {
                return BL;
            } else {
                swapJPoint(rIndex, cIndex);
            }
        }
        //左
        if (cIndex - 1 >= 0) {
            swapJPoint(rIndex, cIndex - 1);
            if (checkFakeWaring() && !checkBlackWin()) {
                return BL;
            } else {
                swapJPoint(rIndex, cIndex);
            }
        }
        //右
        if (cIndex + 1 <= columnLen - 1) {
            swapJPoint(rIndex, cIndex + 1);
            if (checkFakeWaring() && !checkBlackWin()) {
                return BL;
            } else {
                swapJPoint(rIndex, cIndex);
            }
        }
        return RW;
    }

    private void swapJPoint(int rIndex, int cIndex) {
        Role thisP = JPoint.getRole();
        Role other = chess[rIndex][cIndex];
        chess[rIndex][cIndex] = thisP;
        chess[JPoint.getRowIndex()][JPoint.getColumnIndex()] = null;//吃掉了
        JPoint = new Point(thisP, rIndex, cIndex);
        remove(other, rIndex, cIndex);
    }

    private void remove(Role c, int rIndex, int cIndex) {
        if (c == null) {
            return;
        }
        if (c.compareTo(Role.R) == 0) {
            remove(c, rIndex, cIndex, RPoints);
            return;
        }
        if (c.compareTo(Role.H) == 0) {
            remove(c, rIndex, cIndex, HPoints);
            return;
        }
        if (c.compareTo(Role.C) == 0) {
            remove(c, rIndex, cIndex, CPoints);
        }
    }

    private void remove(Role c, int rIndex, int cIndex, List<Point> points) {
        Iterator<Point> iterator = points.iterator();
        while (iterator.hasNext()) {
            Point point = iterator.next();
            if (point.getColumnIndex() == cIndex && point.getRowIndex() == rIndex && point.getRole().compareTo(c) == 0) {
                iterator.remove();
                break;
            }
        }
    }

    private boolean checkFakeWaring() {
        int cIndex = JPoint.getColumnIndex();
        int rIndex = JPoint.getRowIndex();
        for (Point point : RPoints) {
            if (point.canAttack(cIndex, rIndex)) {
                return false;
            }
        }
        for (Point point : HPoints) {
            if (point.canAttack(cIndex, rIndex)) {
                return false;
            }
        }
        for (Point point : CPoints) {
            if (point.canAttack(cIndex, rIndex)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkBlackWin() {
        return GPoint.canAttack(JPoint.getColumnIndex(), JPoint.getRowIndex());
    }

    private class Point {
        private Role role;
        private int rowIndex;
        private int columnIndex;

        public Point(Role role, int rowIndex, int columnIndex) {
            this.role = role;
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;
        }

        public int getRowIndex() {
            return rowIndex;
        }

        public int getColumnIndex() {
            return columnIndex;
        }

        public Role getRole() {
            return role;
        }

        @Override
        public String toString() {
            return "{role:" + role.toString() + ",rowIndex:" + rowIndex + ",columnIndex:" + columnIndex + "}";
        }

        public boolean canAttack(int otherColumnIndex, int otherRowIndex) {
            if (role.compareTo(Role.H) == 0) {
                return role.canAttack(chess, columnIndex, rowIndex, otherColumnIndex, otherRowIndex);
            }
            return !(rowIndex != otherRowIndex && columnIndex != otherColumnIndex) && role.canAttack(chess, columnIndex, rowIndex, otherColumnIndex, otherRowIndex);
        }

    }

    private void generatorChess() {
        chess = new Role[rowLen][columnLen];
        boolean JShow = false;
        boolean GShow = false;
        int left = 8;
        RPoints = new LinkedList<>();
        HPoints = new LinkedList<>();
        CPoints = new LinkedList<>();
        for (int i = 0; i < rowLen; i++) {
            for (int j = 0; j < columnLen; ) {
                Role c = Role.getRandomOne();
                if ((JShow && c.compareTo(Role.J) == 0) || (GShow && c.compareTo(Role.G) == 0)) {
                    continue;
                }
                if (c.compareTo(Role.NULL) > 0 && left > 0) {
                    if (!JShow && c.compareTo(Role.J) == 0) {
                        JPoint = new Point(c, i, j);
//                        System.out.println("JPoint:" + JPoint.toString());
                        JShow = true;
                    }
                    if (!GShow && c.compareTo(Role.G) == 0) {
                        GPoint = new Point(c, i, j);
//                        System.out.println("GPoint:" + GPoint.toString());
                        GShow = true;
                    }
                    if (c.compareTo(Role.R) == 0) {
                        Point point = new Point(c, i, j);
                        RPoints.add(point);
//                        System.out.println("RPoints:" + point.toString());
                    }
                    if (c.compareTo(Role.H) == 0) {
                        Point point = new Point(c, i, j);
                        HPoints.add(point);
//                        System.out.println("HPoints:" + point.toString());
                    }
                    if (c.compareTo(Role.C) == 0) {
                        Point point = new Point(c, i, j);
                        CPoints.add(point);
//                        System.out.println("CPoints:" + point.toString());
                    }
                    chess[i][j] = c;
                    left--;
                }
                j++;
            }
        }
        if (!JShow || !GShow) {
            generatorChess();
        }
    }

    private void print() {
        if (chess == null) {
            System.out.println("*");
        }
        for (Role[] item : chess) {
            StringBuilder stringBuilder = new StringBuilder();
            int jLen = item.length;
            for (int j = 0; j < columnLen; j++) {
                String c;
                Role a = item[j];
                if (a == null) {
                    c = "*";
                } else {
                    c = a.toString();
                }
                stringBuilder = stringBuilder.append(c);
                if (j != jLen - 1) {
                    stringBuilder = stringBuilder.append(",");
                }
            }
            System.out.println(stringBuilder.toString());
        }
    }
}
