package ACM.EightNums;

import basic.Util;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/9/9
 * Time:14:37
 * <p>
 * 八数码问题。
 * <p>
 * 编号为1～8的8个正方形滑块被摆成3行3列（有一个格子留空），如图7-
 * 14所示。每次可以把与空格相邻的滑块（有公共边才算相邻）移到空格中，而它原来的位置
 * 就成为了新的空格。给定初始局面和目标局面（用0表示空格），你的任务是计算出最少的
 * 移动步数。如果无法到达目标局面，则输出-1。
 * <p>
 * 样例输入：
 * 2 6 4 1 3 7 0 5 8
 * 8 1 5 7 3 6 4 0 2
 * 样例输出：
 * 31
 */
public class SolutionV2 {
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
//        PrintStream print = new PrintStream("E:\\test.txt");  //写好输出位置文件；
//        System.setOut(print);
        new SolutionV2().case1();
        new SolutionV2().case2();
//        new SolutionV2().case3();
    }


    private void case1() throws FileNotFoundException, InterruptedException {
        int[] before = {2, 6, 4, 1, 3, 7, 0, 5, 8};
        int[] after = {8, 1, 5, 7, 3, 6, 4, 0, 2};
        slide((byte) 3, (byte) 3, before, after);
    }

    private void case2() throws FileNotFoundException, InterruptedException {
        Integer[] seed = {6, 2, 4, 1, 3, 7, 0, 5, 8};
        Util.shuffleArray(seed);
        int[] before = Util.cover(seed);
        Util.shuffleArray(seed);
        int[] after = Util.cover(seed);
        slide((byte) 3, (byte) 3, before, after);
    }

    private void case3() throws FileNotFoundException, InterruptedException {//4*4 timeout or OOM
        Integer[] seed = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        Util.shuffleArray(seed);
        int[] before = Util.cover(seed);
        Util.shuffleArray(seed);
        int[] after = Util.cover(seed);
        slide((byte) 4, (byte) 4, before, after);
    }

    /**
     * 有rows行，每行有columns个
     */
    private void slide(byte rows, byte columns, int[] before, int[] after) throws FileNotFoundException, InterruptedException {
        long s = System.currentTimeMillis();
        System.out.println(rows + "," + columns + "    " + Arrays.toString(before) + "     " + Arrays.toString(after));
        this.rows = rows;
        this.columns = columns;
        this.sum = rows * columns;
        String beforeString = contactAll(before);
        String afterString = contactAll(after);
        List<Current> moves = new ArrayList<>();
        moves.add(new Current(indexOfZero(before), (byte) -1, beforeString.toCharArray()));
        positiveCurrents.put(beforeString, 0);
        negativeCurrents.put(afterString, 0);
        positiveThread = new SearchHandler(moves, true);
        moves = new ArrayList<>();
        moves.add(new Current(indexOfZero(after), (byte) -1, afterString.toCharArray()));
        negativeThread = new SearchHandler(moves, false);
        positiveThread.start();
        negativeThread.start();
        print();
        System.out.println("time:" + (System.currentTimeMillis() - s));
    }

    private byte rows;
    private byte columns;
    private int sum;
    private Thread positiveThread;
    private Thread negativeThread;

    private byte indexOfZero(int[] array) {
        for (int i = 0; i < sum; i++) {
            if (array[i] == 0) {
                return (byte) i;
            }
        }
        return -1;
    }

    private void print() throws InterruptedException {
        if (!positiveThread.isAlive() && !negativeThread.isAlive()) {
            positiveCurrents.clear();
            negativeCurrents.clear();
            System.out.println("shortest path length is " + globalMin);
            System.out.println("--------------------------------------------");
        } else {
            Thread.sleep(100);
            print();
        }
    }

    private Map<String, Integer> positiveCurrents = new ConcurrentHashMap<>();//正向搜索
    private Map<String, Integer> negativeCurrents = new ConcurrentHashMap<>();//反向搜索

    private boolean cross(boolean isPositive, String current) {
        return (isPositive ? negativeCurrents : positiveCurrents).containsKey(current);
    }

    private boolean visit(boolean isPositive, String current, int steps) {
        Map<String, Integer> target = isPositive ? positiveCurrents : negativeCurrents;
        if (target.containsKey(current)) {
            Integer s = target.get(current);
            if (s == null || steps < s) {
                target.put(current, steps);
            }
            return true;
        } else {
            target.put(current, steps);
        }
        return false;
    }

    private int getShortestSteps(boolean isPositive, String current) {
        return (isPositive ? positiveCurrents : negativeCurrents).getOrDefault(current, 0);
    }

    class SearchHandler extends Thread {
        private boolean isPositive;
        private List<Current> moves;

        SearchHandler(List<Current> moves, boolean isPositive) {
            this.moves = moves;
            this.isPositive = isPositive;
        }

        @Override
        public void run() {
            slide(moves);
        }

        private void slide(List<Current> moves) {
            Current current;
            char[] currentDesc;
            List<Byte> canMove;
            List<Current> next = new ArrayList<>();
            for (Current item : moves) {
//                System.out.println(isPositive + "," + String.valueOf(item.getDesc()));
                canMove = canMove(item.getWhiteIndex(), item.getPreWhiteIndex());
                for (Byte move : canMove) {
                    current = item.copyOf();
                    currentDesc = Arrays.copyOf(current.getDesc(), sum);
                    current.plusStep(currentDesc, move(currentDesc, current.getWhiteIndex(), move));
                    if (check(String.valueOf(currentDesc), current.getStep())) {
                        next.add(current);
                    } else {
                        current.clear();
                    }
                }
                canMove.clear();
            }
            moves.clear();
            if (next.isEmpty()) {
                return;
            }
            slide(next);
        }

        synchronized boolean check(String currentString, int steps) {
            boolean visited = visit(isPositive, currentString, steps);
            boolean crossed = cross(isPositive, currentString);
            if (crossed) {
                steps = getShortestSteps(isPositive, currentString) + getShortestSteps(!isPositive, currentString);
//                System.out.println("may be " + steps + ",currentString:" + currentString);
                if (globalMin < 0 || steps < globalMin) {
                    globalMin = steps;
                }
                return false;
            } else {
                if (visited || (globalMin > 0 && steps > globalMin)) {
                    return false;
                }
            }
            return true;
        }
    }


    private int globalMin = -1;

    private String contactAll(int[] array) {
        StringBuilder builder = new StringBuilder(sum);
        for (int item : array) {
            builder = builder.append(item);
        }
        return builder.toString();
    }

    private List<Byte> canMove(byte whiteIndex, byte block) {
        List<Byte> result = new ArrayList<>(4);
        //上
        if (whiteIndex >= columns) {
            if (block < 0 || whiteIndex - columns != block) {
                result.add((byte) 0);
            }
        }
        //左
        if (whiteIndex >= 1 && (whiteIndex - 1) / columns == whiteIndex / columns) {
            if (block < 0 || whiteIndex - 1 != block) {
                result.add((byte) 1);
            }
        }
        //下
        if (whiteIndex <= (columns * (rows - 1)) - 1) {
            if (block < 0 || whiteIndex + columns != block) {
                result.add((byte) 2);
            }
        }
        //右
        if (whiteIndex <= (columns * rows) - 2 && (whiteIndex + 1) / columns == whiteIndex / columns) {
            if (block < 0 || whiteIndex + 1 != block) {
                result.add((byte) 3);
            }
        }
        return result;
    }

    private byte move(char[] current, byte whiteIndex, byte direction) {
        switch (direction) {
            case 0:
                swapArray(current, whiteIndex, whiteIndex - columns);
                return (byte) (whiteIndex - columns);
            case 1:
                swapArray(current, whiteIndex, whiteIndex - 1);
                return (byte) (whiteIndex - 1);
            case 2:
                swapArray(current, whiteIndex, whiteIndex + columns);
                return (byte) (whiteIndex + columns);
            case 3:
                swapArray(current, whiteIndex, whiteIndex + 1);
                return (byte) (whiteIndex + 1);
            default:
                throw new IllegalArgumentException();
        }
    }

    private void swapArray(char[] arrays, int thisIndex, int otherIndex) {
        char temp = arrays[thisIndex];
        arrays[thisIndex] = arrays[otherIndex];
        arrays[otherIndex] = temp;
    }

    class Current {
        private byte whiteIndex;
        private byte preWhiteIndex = -1;
        private char[] desc;
        private int steps;


        Current(byte whiteIndex, byte preWhiteIndex, char[] desc) {
            if (whiteIndex < 0) {
                throw new IllegalArgumentException();
            }
            this.whiteIndex = whiteIndex;
            this.preWhiteIndex = preWhiteIndex;
            this.desc = desc;
        }

        Current(byte whiteIndex, byte preWhiteIndex, char[] desc, int steps) {
            this(whiteIndex, preWhiteIndex, desc);
            this.steps = steps;
        }

        byte getWhiteIndex() {
            return whiteIndex;
        }

        byte getPreWhiteIndex() {
            return preWhiteIndex;
        }

        int getStep() {
            return steps;
        }

        public char[] getDesc() {
            return desc;
        }

        Current copyOf() {
            return new Current(this.whiteIndex, this.preWhiteIndex, this.desc, this.steps);
        }

        void plusStep(char[] desc, byte newIndex) {
            this.preWhiteIndex = whiteIndex;
            this.whiteIndex = newIndex;
            this.desc = desc;
            this.steps++;
        }

        void clear() {
            this.desc = null;
        }
    }
}
