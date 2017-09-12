package ACM.EightNums;

import basic.Util;

import java.util.*;

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
public class SolutionV1 {
    public static void main(String[] args) {
        new SolutionV1().case1();
//        new SolutionV1().case2();
//        new SolutionV1().case3();
    }

    private void case1() {
        int[] before = {0, 1, 2, 3};
        int[] after = {0, 3, 1, 2};//5
//        int[] after = {1, 2, 3, 0};//无解
        slide((byte) 2, (byte) 2, before, after);
    }

    /**
     * 有rows行，每行有columns个
     */
    private void slide(byte rows, byte columns, int[] before, int[] after) {
        long s = System.currentTimeMillis();
        System.out.println(rows + "," + columns + "    " + Arrays.toString(before) + "     " + Arrays.toString(after));
        this.rows = rows;
        this.columns = columns;
        this.sum = rows * columns;
        String beforeString = contactAll(before);
        String afterString = contactAll(after);
        List<Current> moves = new ArrayList<>();
        moves.add(new Current((byte) beforeString.indexOf("0"), (byte) -1, beforeString.toCharArray()));
        slide(moves, afterString);
        print();
        System.out.println("time:" + (System.currentTimeMillis() - s));
    }

    private byte rows;
    private byte columns;
    private int sum;

    private void print() {
        System.out.println(globalMin);
        System.out.println("--------------------------------------------");
    }

    private void slide(List<Current> moves, String after) {
        Current current;
        char[] currentDesc;
        byte currentWhiteIndex;
        int currentStep;
        List<Byte> canMove;
        List<Current> next = new ArrayList<>();
        for (Current item : moves) {
            canMove = canMove(item.getWhiteIndex(), item.getPreWhiteIndex());
            for (Byte move : canMove) {
                current = item.copyOf();
                currentDesc = Arrays.copyOf(current.getDesc(), sum);
                currentWhiteIndex = current.getWhiteIndex();
                currentWhiteIndex = move(currentDesc, currentWhiteIndex, move);
                if (!current.plusStep(currentDesc, currentWhiteIndex)) {
                    current.clear();
                    continue;
                }
                currentStep = current.getStep();
                if (String.valueOf(currentDesc).equals(after)) {
                    System.out.println("may be " + currentStep + "   " + current.getSteps().toString());
                    if (globalMin < 0 || currentStep < globalMin) {
                        globalMin = currentStep;
                    }
                    current.clear();
                    continue;
                } else {
                    if (globalMin > 0 && currentStep > globalMin) {
                        current.clear();
                        continue;
                    }
                }
                next.add(current);
            }
            canMove.clear();
        }
        moves.clear();
        if (next.isEmpty()) {
            return;
        }
        slide(next, after);
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
        if (!result.isEmpty()) {
            Collections.shuffle(result);
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
        private Set<String> steps = new LinkedHashSet<>();


        Current(byte whiteIndex, byte preWhiteIndex, char[] desc) {
            this.whiteIndex = whiteIndex;
            this.preWhiteIndex = preWhiteIndex;
            this.desc = desc;
            this.steps.add(String.valueOf(desc));
        }

        Current(byte whiteIndex, byte preWhiteIndex, char[] desc, Set<String> steps) {
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
            return steps.size();
        }

        public char[] getDesc() {
            return desc;
        }

        Set<String> getSteps() {
            return steps;
        }

        Current copyOf() {
            return new Current(this.whiteIndex, this.preWhiteIndex, this.desc, new LinkedHashSet<>(this.steps));
        }

        boolean plusStep(char[] desc, byte newIndex) {
            if (this.getSteps().size() > 10 * 100) {
                this.getSteps().clear();
                return false;
            }
            String step = String.valueOf(desc);
            if (!this.steps.add(step)) {
                return false;
            }
            this.preWhiteIndex = whiteIndex;
            this.whiteIndex = newIndex;
            this.desc = desc;
            return true;
        }

        void clear() {
            this.desc = null;
            this.steps.clear();
            this.steps = null;
        }
    }
}
