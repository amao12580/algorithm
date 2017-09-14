package ACM.EightNums;

import basic.Util;
import basic.dataStructure.graph.Line;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;
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
public class SolutionV3 {
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        PrintStream print = new PrintStream("E:\\test.txt");  //写好输出位置文件；
        System.setOut(print);
        new SolutionV3().case1();
//        new SolutionV3().case2();
//        new SolutionV3().case3();
//        new SolutionV3().case4();
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

    private void case4() throws FileNotFoundException, InterruptedException {
        int[] before = {8, 7, 1, 5, 2, 6, 3, 4, 0};
        int[] after = {8, 7, 1, 6, 2, 5, 3, 4, 0};
        slide((byte) 3, (byte) 3, before, after);
    }

    private void init() {
        //初始化最短距离二维表
        distanceArray = new int[sum][sum];
        for (int i = 0; i < sum; i++) {
            for (int j = 0; j < sum; j++) {
                distanceArray[i][j] = calculateShortestDistance0(i, j);
            }
        }
    }

    private int calculateShortestDistance0(int small, int big) {
        if (big == small) {
            return 0;
        }
        int lr = small / columns;
        int rr = big / columns;
        if (lr == rr) {
            return big - small;
        }
        return Math.abs(big - rr * columns - (small - lr * columns)) + (rr - lr);
    }

    private int calculateShortestDistance(int small, int big) {
        return distanceArray[small][big];
    }

    private int calculateShortestDistance(int[] descV2I, int[] afterV2I) {
        int d = 0;
        for (int i = 0; i < sum; i++) {
            d += calculateShortestDistance(descV2I[i], afterV2I[i]);
        }
        return d;
    }

    private int calculateShortestDistance(int[] current, int lastDistance, int lastIndex, int currentIndex, int[] target) {
        int tbi = target[current[lastIndex]];
        int tai = target[current[currentIndex]];
        return lastDistance
                - (calculateShortestDistance(tbi, currentIndex))
                - (calculateShortestDistance(tai, lastIndex))
                + (calculateShortestDistance(tbi, lastIndex))
                + (calculateShortestDistance(tai, currentIndex));
    }

    private int[][] distanceArray;

    /**
     * 有rows行，每行有columns个
     */
    private void slide(byte rows, byte columns, int[] before, int[] after) throws FileNotFoundException, InterruptedException {
        long s = System.currentTimeMillis();
        System.out.println(rows + "," + columns + "    " + Arrays.toString(before) + "     " + Arrays.toString(after));
        this.rows = rows;
        this.columns = columns;
        this.sum = rows * columns;
        init();
        Set<Current> moves = new HashSet<>();
        int[] afterV2I = V2I(after);
        int[] beforeV2I = V2I(before);
        moves.add(new Current(indexOfZero(before), -1, before, beforeV2I, calculateShortestDistance(beforeV2I, afterV2I)));
        positiveCurrents.put(Arrays.toString(before), 0);
        negativeCurrents.put(Arrays.toString(before), 0);
        positiveThread = new SearchHandler(moves, true, after);
        moves = new HashSet<>();
        moves.add(new Current(indexOfZero(after), -1, after, afterV2I, calculateShortestDistance(afterV2I, beforeV2I)));
        negativeThread = new SearchHandler(moves, false, before);
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

    private int indexOfZero(int[] array) {
        for (int i = 0; i < sum; i++) {
            if (array[i] == 0) {
                return i;
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
            Thread.sleep(20);
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

    private int[] V2I(int[] array) {
        int[] result = new int[sum];
        for (int i = 0; i < sum; i++) {
            result[array[i]] = i;
        }
        return result;
    }

    private int getShortestSteps(boolean isPositive, String current) {
        return (isPositive ? positiveCurrents : negativeCurrents).getOrDefault(current, 0);
    }

    class SearchHandler extends Thread {
        private boolean isPositive;
        private List<Current> moves;
        private int[] targetV2I;

        SearchHandler(Set<Current> moves, boolean isPositive, int[] targetI2V) {
            this.moves = new ArrayList<>(moves);
            this.isPositive = isPositive;
            this.targetV2I = V2I(targetI2V);
        }

        @Override
        public void run() {
            slide(moves);
        }

        private void slide(List<Current> moves) {
            Current current;
            int[] currentDescI2V, currentDescV2I;
            List<Byte> canMove;
            Set<Current> next = new HashSet<>();
            for (Current item : moves) {
                System.out.println(isPositive + "," + Arrays.toString(item.getDescI2V()));
                canMove = canMove(item.getWhiteIndex(), item.getPreWhiteIndex());
                for (Byte move : canMove) {
                    current = item.copyOf();
                    currentDescI2V = Arrays.copyOf(current.getDescI2V(), sum);
                    currentDescV2I = Arrays.copyOf(current.getDescV2I(), sum);
                    current.plusStep(currentDescI2V, currentDescV2I, move(currentDescI2V,
                            currentDescV2I, current.getWhiteIndex(), move), targetV2I);
                    if (check(Arrays.toString(currentDescI2V), current.getStep())) {
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
            List<Current> temp = new ArrayList<>(next);
            next.clear();
            if (next.size() > 1) {
                Collections.sort(temp);
            }
            slide(temp);
        }

        synchronized boolean check(String currentString, int steps) {
            boolean visited = visit(isPositive, currentString, steps);
            boolean crossed = cross(isPositive, currentString);
            if (crossed) {
                steps = getShortestSteps(isPositive, currentString) + getShortestSteps(!isPositive, currentString);
                System.out.println("may be " + steps + ",currentString:" + currentString);
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

    private List<Byte> canMove(int whiteIndex, int block) {
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

    private int move(int[] I2V, int[] V2I, int whiteIndex, int direction) {
        switch (direction) {
            case 0:
                swapArray(I2V, V2I, whiteIndex, whiteIndex - columns);
                return whiteIndex - columns;
            case 1:
                swapArray(I2V, V2I, whiteIndex, whiteIndex - 1);
                return whiteIndex - 1;
            case 2:
                swapArray(I2V, V2I, whiteIndex, whiteIndex + columns);
                return whiteIndex + columns;
            case 3:
                swapArray(I2V, V2I, whiteIndex, whiteIndex + 1);
                return whiteIndex + 1;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void swapArray(int[] I2V, int[] V2I, int thisIndex, int otherIndex) {
        swapArray(V2I, I2V[thisIndex], I2V[otherIndex]);
        swapArray(I2V, thisIndex, otherIndex);
    }

    private void swapArray(int[] arrays, int thisIndex, int otherIndex) {
        int temp = arrays[thisIndex];
        arrays[thisIndex] = arrays[otherIndex];
        arrays[otherIndex] = temp;
    }

    class Current implements Comparable<Current> {
        private int whiteIndex;
        private int preWhiteIndex = -1;
        private int hashCode;
        private int[] descI2V;
        private int[] descV2I;
        private int steps;
        private int distance;


        Current(int whiteIndex, int preWhiteIndex, int[] descI2V, int[] descV2I, int distance) {
            if (whiteIndex < 0) {
                throw new IllegalArgumentException();
            }
            this.whiteIndex = whiteIndex;
            this.preWhiteIndex = preWhiteIndex;
            this.descI2V = descI2V;
            this.hashCode = Arrays.toString(descI2V).hashCode();
            this.descV2I = descV2I;
            this.distance = distance;
        }

        Current(int whiteIndex, int preWhiteIndex, int[] descI2V, int[] descV2I, int distance, int steps) {
            this(whiteIndex, preWhiteIndex, descI2V, descV2I, distance);
            this.steps = steps;
        }

        int getWhiteIndex() {
            return whiteIndex;
        }

        int getPreWhiteIndex() {
            return preWhiteIndex;
        }

        int getStep() {
            return steps;
        }

        int[] getDescI2V() {
            return descI2V;
        }

        int[] getDescV2I() {
            return descV2I;
        }

        Current copyOf() {
            return new Current(this.whiteIndex, this.preWhiteIndex, this.descI2V, this.descV2I, this.distance, this.steps);
        }

        void plusStep(int[] descI2V, int[] descV2I, int newIndex, int[] targetV2I) {
            this.preWhiteIndex = whiteIndex;
            this.distance = calculateShortestDistance(descI2V, this.distance, this.preWhiteIndex, newIndex, targetV2I);
            this.whiteIndex = newIndex;
            this.descI2V = descI2V;
            this.hashCode = Arrays.toString(descI2V).hashCode();
            this.descV2I = descV2I;
            this.steps++;
        }

        void clear() {
            this.descI2V = null;
            this.descV2I = null;
        }

        @Override
        public int compareTo(Current other) {
            return this.distance - other.distance;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o instanceof Current) {
                Current other = (Current) o;
                return this.hashCode() == other.hashCode();
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return hashCode;
        }
    }
}
