package ACM.ADiceyProblem;

import basic.Util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/7/18
 * Time:15:13
 * <p>
 * 图6-30（a）是一个迷宫，图6-30（b）是一个筛子。你的任务是把筛子放在起点（筛子
 * 顶面和正面的数字由输入给定），经过若干次滚动以后回到起点。
 * 每次到达一个新格子时，格子上的数字必须和与它接触的筛子上的数字相同，除非到达
 * 的格子上画着五星（此时，与它接触的筛子上的数字可以任意）。输入一个R和C行
 * （1≤R，C≤10）的迷宫、起点坐标以及顶面、正面的数字，输出一条可行的路径。
 * <p>
 * A Dicey Problem, ACM/ICPC World Finals 1999, UVa810
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=onlinejudge&page=show_problem&problem=751
 * <p>
 * Sample Input
 * DICEMAZE1
 * 3 3 1 2 5 1
 * -1 2 4
 * 5 5 6
 * 6 -1 -1
 * DICEMAZE2
 * 4 7 2 6 3 6
 * 6 4 6 0 2 6 4
 * 1 2 -1 5 3 6 1
 * 5 3 4 5 6 4 2
 * 4 1 2 0 3 -1 6
 * DICEMAZE3
 * 3 3 1 1 2 4
 * 2 2 3
 * 4 5 6
 * -1 -1 -1
 * END
 * Sample Output
 * DICEMAZE1
 * (1,2),(2,2),(2,3),(3,3),(3,2),(3,1),(2,1),(1,1),(1,2)
 * DICEMAZE2
 * (2,6),(2,5),(2,4),(2,3),(2,2),(3,2),(4,2),(4,1),(3,1),
 * (2,1),(2,2),(2,3),(2,4),(2,5),(1,5),(1,6),(1,7),(2,7),
 * (3,7),(4,7),(4,6),(3,6),(2,6)
 * DICEMAZE3
 * No Solution Possible
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        solution.case2();
        solution.case3();
    }

    private void case1() {
        String config = "3 3 1 2 5 1 ";
        String[] desc = {"-1 2 4 ", "5 5 6 ", "6 -1 -1 "};
        maze("DICEMAZE1", config, desc);
    }

    private void case2() {
        String config = "4 7 2 6 3 6 ";
        String[] desc = {"6 4 6 0 2 6 4 ", "1 2 -1 5 3 6 1 ", "5 3 4 5 6 4 2 ", "4 1 2 0 3 -1 6 "};
        maze("DICEMAZE2", config, desc);
    }

    private void case3() {
        String config = "3 3 1 1 2 4 ";
        String[] desc = {"2 2 3 ", "4 5 6 ", "-1 -1 -1 "};
        maze("DICEMAZE3", config, desc);
    }

    private void maze(String name, String config, String[] desc) {
        int[] configArray = parse(config, 6);
        Dice dice = new Dice(configArray[2] - 1, configArray[3] - 1, configArray[5], configArray[4]);
        int[][] maze = buildMaze(desc, configArray[0], configArray[1]);
        System.out.println(name);
        LinkedHashSet<Dice> path = new LinkedHashSet<>();
        path.add(dice);
        if (move(dice, maze, configArray, path)) {
            printPath(path);
        } else {
            System.out.println("No Solution Possible");
        }
    }

    private void printPath(LinkedHashSet<Dice> path) {
        Iterator<Dice> iterator = path.iterator();
        StringBuilder builder = new StringBuilder();
        while (iterator.hasNext()) {
            builder = builder.append(iterator.next().toIndexString()).append(", ");
        }
        String result = builder.toString();
        result = result.substring(0, result.length() - 2);
        System.out.println("  " + result);
    }

    private boolean move(Dice dice, int[][] maze, int[] configArray, LinkedHashSet<Dice> path) {
        LinkedList<Integer> directions = dice.canMove(maze, configArray[0], configArray[1]);
        if (directions.isEmpty()) {
            return false;
        }
        LinkedHashSet<Dice> currentPath;
        Dice currentDice;
        for (Integer direction : directions) {
            currentPath = new LinkedHashSet<>();
            currentPath.addAll(path);
            currentDice = dice.copy();
            if (currentDice.move(direction, configArray[2] - 1, configArray[3] - 1)) {
                currentPath.add(currentDice);
                path.clear();
                path.addAll(currentPath);
                return true;
            } else {
                if (currentPath.add(currentDice) && move(currentDice, maze, configArray, currentPath)) {
                    path.clear();
                    path.addAll(currentPath);
                    return true;
                }
            }
        }
        return false;
    }

    private int[][] buildMaze(String[] desc, int rl, int cl) {
        int[][] result = new int[rl][cl];
        for (int i = 0; i < rl; i++) {
            result[i] = parse(desc[i], cl);
        }
        return result;
    }

    private int[] parse(String desc, int len) {
        int[] result = new int[len];
        int index = 0;
        char[] chars = desc.toCharArray();
        int beginIndex = -1;
        int endIndex = -1;
        int cLen = chars.length;
        char c;
        for (int i = 0; i < cLen; i++) {
            if (index > len - 1) {
                break;
            }
            c = chars[i];
            if (c != ' ' && beginIndex < 0) {
                beginIndex = i;
            }
            if (c == ' ') {
                endIndex = i;
            }
            if (beginIndex >= 0 && endIndex >= beginIndex) {
                result[index++] = Integer.valueOf(desc.substring(beginIndex, endIndex));
                beginIndex = -1;
                endIndex = -1;
            }
        }
        return result;
    }
}


class Dice {
    private int row;
    private int column;
    private static final int[] init = {1, 2, 6, 5, 3, 4};//上，右，底，左，后，前
    private int[] now;
    private int face;
    private int top;

    public Dice(int row, int column, int face, int top) {
        this.row = row;
        this.column = column;
        this.face = face;
        this.top = top;
        this.now = rotate(init, face, top);
    }

    public Dice(int row, int column, int face, int top, int[] now) {
        this.row = row;
        this.column = column;
        this.face = face;
        this.top = top;
        this.now = Arrays.copyOf(now, 6);
    }

    public LinkedList<Integer> canMove(int[][] maze, int rowMax, int columnMax) {
        rowMax--;
        columnMax--;
        LinkedList<Integer> direction = new LinkedList<>();
        int[] moveIndex;
        int row, column;
        int top = this.top;
        for (int i = 0; i < 4; i++) {
            moveIndex = moveIndex(i);
            row = moveIndex[0];
            column = moveIndex[1];
            if (Util.checkInRange(moveIndex[0], 0, rowMax) && Util.checkInRange(moveIndex[1], 0, columnMax)
                    && match(maze, row, column, top)) {
                direction.add(i);
            }
        }
        return direction;
    }

    public boolean match(int[][] maze, int row, int column, int top) {
        int t = maze[row][column];
        return t != 0 && (t == -1 || t == top);
    }

    public boolean move(int direction, int rowTarget, int columnTarget) {
        move(direction);
        return hasReached(rowTarget, columnTarget);
    }

    public boolean hasReached(int row, int column) {
        return this.row == row && this.column == column;
    }

    private int[] moveIndex(int direction) {
        int[] result = new int[2];
        int row = this.row;
        int column = this.column;
        switch (direction) {
            case 0://向上
                row--;
                break;
            case 1://向左
                column--;
                break;
            case 2://向下
                row++;
                break;
            case 3://向右
                column++;
                break;
        }
        result[0] = row;
        result[1] = column;
        return result;
    }

    private int[] rotate(int[] init, int face, int top) {
        int[] now = Arrays.copyOf(init, 6);
        while (now[5] != face) {
            now = moveValue(now, 2);
        }
        while (now[0] != top) {
            now = moveValue(now, 3);
        }
        return now;
    }

    private int[] moveValue(int[] desc, int direction) {
        int[] result = Arrays.copyOf(desc, 6);
        switch (direction) {
            case 0://向上
                Util.swapArray(result, 0, 4);
                Util.swapArray(result, 2, 5);
                Util.swapArray(result, 0, 2);
                break;
            case 1://向左
                Util.swapArray(result, 0, 3);
                Util.swapArray(result, 0, 2);
                Util.swapArray(result, 0, 1);
                break;
            case 2://向下
                Util.swapArray(result, 0, 5);
                Util.swapArray(result, 0, 2);
                Util.swapArray(result, 0, 4);
                break;
            case 3://向右
                Util.swapArray(result, 0, 1);
                Util.swapArray(result, 0, 2);
                Util.swapArray(result, 0, 3);
                break;
        }
        return result;
    }

    private void move(int direction) {
        int[] moveIndex = moveIndex(direction);
        int[] moveValue = moveValue(now, direction);
        this.row = moveIndex[0];
        this.column = moveIndex[1];
        this.face = moveValue[5];
        this.top = moveValue[0];
        this.now = moveValue;
    }

    protected Dice copy() {
        return new Dice(this.row, this.column, this.face, this.top, this.now);
    }

    @Override
    public String toString() {
        return this.row + "." + this.column + "." + this.face + "." + this.top;
    }

    public String toIndexString() {
        return "(" + (this.row + 1) + "," + (this.column + 1) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Dice) {
            Dice other = (Dice) o;
            return this.hashCode() == other.hashCode();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}