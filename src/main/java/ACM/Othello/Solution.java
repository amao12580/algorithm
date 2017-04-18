package ACM.Othello;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/4/18
 * Time:9:37
 * <p>
 * 你的任务是模拟黑白棋游戏的进程。黑白棋的规则为：黑白双方轮流放棋子，每次必须
 * 让新放的棋子“夹住”至少一枚对方棋子，然后把所有被新放棋子“夹住”的对方棋子替换成己
 * 方棋子。一段连续（横、竖或者斜向）的同色棋子被“夹住”的条件是两端都是对方棋子（不
 * 能是空位）。如图4-6（a）所示，白棋有6个合法操作，分别为(2,3),(3,3),(3,5),(3,6),(6,2),(7,3),
 * (7,4),(7,5)。选择在(7,3)放白棋后变成如图4-6（b）所示效果（注意有竖向和斜向的共两枚黑棋变
 * 白）。注意(4,6)的黑色棋子虽然被夹住，但不是被新放的棋子夹住，因此不变白。
 * <p>
 * 输入一个8*8的棋盘以及当前下一次操作的游戏者，处理3种指令：
 * L指令打印所有合法操作，按照从上到下，从左到右的顺序排列（没有合法操作时输出
 * No legal move）。
 * Mrc指令放一枚棋子在(r,c)。如果当前游戏者没有合法操作，则是先切换游戏者再操
 * 作。输入保证这个操作是合法的。输出操作完毕后黑白方的棋子总数。
 * Q指令退出游戏，并打印当前棋盘（格式同输入）。
 * <p>
 * Othello, ACM/ICPC World Finals 1992, UVa220
 * <p>
 * 辅助理解资料：http://baike.baidu.com/item/%E9%BB%91%E7%99%BD%E6%A3%8B/80689#4
 */
public class Solution {
    private static final int DEFAULT_CHESS_LENGTH = 8;
    private static final int DEFAULT_CHESSMEN_SUM_MAX = DEFAULT_CHESS_LENGTH * DEFAULT_CHESS_LENGTH;
    private static final boolean DEFAULT_MOVE_ROLE = true;//true=黑子；false=白子
    private boolean currentCanMoveRole = DEFAULT_MOVE_ROLE;//true=黑子；false=白子

    //白子
    private Set<Point> whites = new HashSet<>();

    //黑子
    private Set<Point> blacks = new HashSet<>();

    public void setCurrentCanMoveRole(boolean currentCanMoveRole) {
        this.currentCanMoveRole = currentCanMoveRole;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.init();
        System.out.println("------------------------------------------");
        solution.printChessBoard();
        solution.setCurrentCanMoveRole(false);//设置为白子可走
        System.out.println("------------------------------------------");
        solution.printCanMove(false);
        System.out.println("------------------------------------------");
        solution.move(false, 7, 3);
        System.out.println("------------------------------------------");
        solution.printChessBoard();
        System.out.println("------------------------------------------");
        solution.printCanMove(true);
        System.out.println("------------------------------------------");
        solution.move(true, 2, 8);
        System.out.println("------------------------------------------");
        solution.printChessBoard();
        System.out.println("------------------------------------------");
        solution.printCanMove(false);
        System.out.println("------------------------------------------");
        solution.move(false, 3, 6);
        System.out.println("------------------------------------------");
        solution.printChessBoard();
    }

    private void move(boolean moveRole, int rowIndex, int columnIndex) {
        if (blacks.size() + whites.size() == DEFAULT_CHESSMEN_SUM_MAX) {
            System.out.print("Game over,winner is " + (blacks.size() > whites.size() ? "Black" : "White"));
            return;
        }
        if (moveRole != currentCanMoveRole) {
            System.out.println("not this time.");
            return;
        }
        Set<Point> mayBes = getCanMove(moveRole);
        Point point = new Point(rowIndex, columnIndex);
        if (!mayBes.contains(point)) {
            System.out.println("illegal move");
            return;
        }
        System.out.print((moveRole ? "Black" : "White") + " moved (" + rowIndex + "," + columnIndex + ")");
        Set<Point> friends = moveRole ? blacks : whites;
        Set<Point> enemys = moveRole ? whites : blacks;
        friends.add(point);
        replace(rowIndex, columnIndex, friends, enemys);
        currentCanMoveRole = !moveRole;
        System.out.println(".white left " + whites.size() + ",black left " + blacks.size());
    }


    private void printCanMove(boolean moveRole) {
        String NL = "No legal move";
        Set<Point> mayBe = getCanMove(moveRole);
        if (mayBe.isEmpty()) {
            System.out.println(NL);
        } else {
            System.out.println((moveRole ? "Black" : "White") + " can move options:");
            for (Point point : mayBe) {
                System.out.println(point.toString());
            }
        }
    }

    private Set<Point> getCanMove(boolean moveRole) {
        Set<Point> mayBes = new HashSet<>();
        if (moveRole) {//走黑子
            for (Point point : blacks) {
                findByOnePoint(point, blacks, whites, mayBes);
            }
        } else {//走白子
            for (Point point : whites) {
                findByOnePoint(point, whites, blacks, mayBes);
            }
        }
        return mayBes;
    }

    private void replaceMen(Set<Point> replaces, Set<Point> friends, Set<Point> enemys) {
        if (replaces.isEmpty()) {
            return;
        }
        Iterator<Point> iterator = enemys.iterator();
        while (iterator.hasNext()) {
            Point next = iterator.next();
            if (replaces.contains(next)) {
                iterator.remove();
            }
        }
        friends.addAll(replaces);
    }


    private void findByOnePoint(Set<Point> friends, Set<Point> enemys, Set<Point> mayBes,
                                Integer rowIndex, Integer rowIndexMin, Integer rowIndexMax, Boolean rowIsPlus,
                                Integer columnIndex, Integer columnIndexMin, Integer columnIndexMax, Boolean columnIsPlus) {
        boolean foundEnemy = false;
        int myRowIndex = rowIndex;
        int myColumnIndex = columnIndex;
        while (checkInRange(myRowIndex, rowIndexMin, rowIndexMax, myColumnIndex, columnIndexMin, columnIndexMax)) {
            Point point = new Point(myRowIndex, myColumnIndex);
            Boolean isSpace = isSpace(point, enemys, friends);
            if (isSpace == null) {
                if (foundEnemy) {
                    mayBes.add(point);
                }
                break;
            }
            if (!isSpace) {
                foundEnemy = true;
            }
            if (isSpace) {
                break;
            }
            if (rowIsPlus != null) {
                if (rowIsPlus) {
                    myRowIndex++;
                } else {
                    myRowIndex--;
                }
            }
            if (columnIsPlus != null) {
                if (columnIsPlus) {
                    myColumnIndex++;
                } else {
                    myColumnIndex--;
                }
            }
        }
    }

    private void replace(Set<Point> friends, Set<Point> enemys, Set<Point> replaces,
                         Integer rowIndex, Integer rowIndexMin, Integer rowIndexMax, Boolean rowIsPlus,
                         Integer columnIndex, Integer columnIndexMin, Integer columnIndexMax, Boolean columnIsPlus) {
        Set<Point> replace = new HashSet<>();
        int myRowIndex = rowIndex;
        int myColumnIndex = columnIndex;
        while (checkInRange(myRowIndex, rowIndexMin, rowIndexMax, myColumnIndex, columnIndexMin, columnIndexMax)) {
            Point point = new Point(myRowIndex, myColumnIndex);
            Boolean isSpace = isSpace(point, enemys, friends);
            if (isSpace == null) {
                break;
            }
            if (isSpace) {
                break;
            } else {
                replace.add(point);
            }
            if (rowIsPlus != null) {
                if (rowIsPlus) {
                    myRowIndex++;
                } else {
                    myRowIndex--;
                }
            }
            if (columnIsPlus != null) {
                if (columnIsPlus) {
                    myColumnIndex++;
                } else {
                    myColumnIndex--;
                }
            }
        }
        if (!replace.isEmpty()) {
            replaces.addAll(replace);
        }
    }

    private boolean checkInRange(Integer rowIndex, Integer rowIndexMin, Integer rowIndexMax,
                                 Integer columnIndex, Integer columnIndexMin, Integer columnIndexMax) {
        boolean rowInRange = (rowIndexMin == null || rowIndex >= rowIndexMin) && (rowIndexMax == null || rowIndex <= rowIndexMax);
        boolean columnInRange = (columnIndexMin == null || columnIndex >= columnIndexMin) && (columnIndexMax == null || columnIndex <= columnIndexMax);
        return rowInRange && columnInRange;
    }

    /**
     * 用己方的一个棋子，找出所有可能的新落子点，添加到maybe中
     *
     * @param friend  己方棋子
     * @param friends 己方棋子集合
     * @param enemys  对方棋子集合
     * @param mayBes  己方所有可能的落子点
     */
    private void findByOnePoint(Point friend, Set<Point> friends, Set<Point> enemys, Set<Point> mayBes) {
        //正上方
        findByOnePoint(friends, enemys, mayBes, friend.getRowIndex() - 1, 1, null, false, friend.getColumnIndex(), null, null, null);
        //左上方
        findByOnePoint(friends, enemys, mayBes, friend.getRowIndex() - 1, 1, null, false, friend.getColumnIndex() - 1, 1, null, false);
        //正下方
        findByOnePoint(friends, enemys, mayBes, friend.getRowIndex() + 1, null, DEFAULT_CHESS_LENGTH, true, friend.getColumnIndex(), null, null, null);
        //左下方
        findByOnePoint(friends, enemys, mayBes, friend.getRowIndex() + 1, null, DEFAULT_CHESS_LENGTH, true, friend.getColumnIndex() - 1, 1, null, false);
        //正左方
        findByOnePoint(friends, enemys, mayBes, friend.getRowIndex(), null, null, null, friend.getColumnIndex() - 1, 1, null, false);
        //正右方
        findByOnePoint(friends, enemys, mayBes, friend.getRowIndex(), null, null, null, friend.getColumnIndex() + 1, null, DEFAULT_CHESS_LENGTH, true);
        //右上方
        findByOnePoint(friends, enemys, mayBes, friend.getRowIndex() - 1, 1, null, false, friend.getColumnIndex() + 1, null, DEFAULT_CHESS_LENGTH, true);
        //右下方
        findByOnePoint(friends, enemys, mayBes, friend.getRowIndex() + 1, null, DEFAULT_CHESS_LENGTH, true, friend.getColumnIndex() + 1, null, DEFAULT_CHESS_LENGTH, true);
    }


    private void replace(int rowIndex, int columnIndex, Set<Point> friends, Set<Point> enemys) {
        Set<Point> replaces = new HashSet<>();
        //正上方
        replace(friends, enemys, replaces, rowIndex - 1, 1, null, false, columnIndex, null, null, null);
        //左上方
        replace(friends, enemys, replaces, rowIndex - 1, 1, null, false, columnIndex - 1, 1, null, false);
        //正下方
        replace(friends, enemys, replaces, rowIndex + 1, null, DEFAULT_CHESS_LENGTH, true, columnIndex, null, null, null);
        //左下方
        replace(friends, enemys, replaces, rowIndex + 1, null, DEFAULT_CHESS_LENGTH, true, columnIndex - 1, 1, null, false);
        //正左方
        replace(friends, enemys, replaces, rowIndex, null, null, null, columnIndex - 1, 1, null, false);
        //正右方
        replace(friends, enemys, replaces, rowIndex, null, null, null, columnIndex + 1, null, DEFAULT_CHESS_LENGTH, true);
        //右上方
        replace(friends, enemys, replaces, rowIndex - 1, 1, null, false, columnIndex + 1, null, DEFAULT_CHESS_LENGTH, true);
        //右下方
        replace(friends, enemys, replaces, rowIndex + 1, null, DEFAULT_CHESS_LENGTH, true, columnIndex + 1, null, DEFAULT_CHESS_LENGTH, true);

        replaceMen(replaces, friends, enemys);
    }

    private void printChessBoard() {
        for (int i = 1; i <= DEFAULT_CHESS_LENGTH; i++) {
            for (int j = 1; j <= DEFAULT_CHESS_LENGTH; j++) {
                Boolean isSpace = isSpace(i, j);
                if (isSpace == null) {
                    System.out.print(".");
                } else {
                    if (isSpace) {
                        System.out.print("B");
                    } else {
                        System.out.print("W");
                    }
                }
                System.out.print("  ");
            }
            System.out.println();
        }
    }

    /**
     * 该位置是否为空置
     */
    private Boolean isSpace(int rowIndex, int columnIndex) {
        return isSpace(new Point(rowIndex, columnIndex), whites, blacks);
    }

    private Boolean isSpace(Point point, Set<Point> whites, Set<Point> blacks) {
        if (whites.contains(point)) {
            return false;
        }
        if (blacks.contains(point)) {
            return true;
        }
        return null;
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


        @Override
        public String toString() {
            return "(" + rowIndex + "," + columnIndex + ")";
        }
    }

    private void init() {
        //standard
//        whites.add(new Point(4, 4));
//        whites.add(new Point(5, 5));
//        blacks.add(new Point(4, 5));
//        blacks.add(new Point(5, 4));
        whites.add(new Point(3, 7));
        blacks.add(new Point(3, 4));

        blacks.add(new Point(4, 4));
        blacks.add(new Point(4, 5));
        blacks.add(new Point(4, 6));

        whites.add(new Point(5, 3));
        blacks.add(new Point(5, 4));
        whites.add(new Point(5, 5));
        whites.add(new Point(5, 6));

        blacks.add(new Point(6, 3));
        blacks.add(new Point(6, 4));
        blacks.add(new Point(6, 5));
        whites.add(new Point(6, 6));
    }
}
