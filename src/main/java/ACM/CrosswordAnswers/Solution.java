package ACM.CrosswordAnswers;

import basic.Util;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/3/23
 * Time:17:35
 * <p>
 * 输入一个r行c列（1≤r，c≤10）的网格，黑格用“*”表示，每个白格都填有一个字母。
 * 如果一个白格的左边相邻位置或者上边相邻位置没有白格（可能是黑格，也可能出了网格边界），
 * 则称这个白格是一个起始格。
 * <p>
 * 首先把所有起始格按照从上到下、从左到右的顺序编号为1, 2, 3,…，如图3-7所示。
 * <p>
 * 接下来要找出所有横向单词（Across）。这些单词必须从一个起始格开始，
 * 向右延伸到一个黑格的左边或者整个网格的最右列。最后找出所有竖向单词（Down）。
 * 这些单词必须从一个起始格开始，向下延伸到一个黑格的上边或者整个网格的最下行。
 * 输入输出格式和样例请参考原题。
 * <p>
 * <p>
 * 习题3-6　纵横字谜的答案（Crossword Answers, ACM/ICPC World Finals 1994,UVa232）
 * <p>
 * <p>
 * <p>
 * 样例：
 * 输入网格：
 * <p>
 * AIM*DEN
 * ME*ONE
 * UPON*TO
 * SO*ERIN
 * SA*OR*
 * IES*DEA
 * <p>
 * <p>
 * 输出横向和纵向结果
 * <p>
 * Across
 * <p>
 * 1.AIM
 * 4.DEN
 * 7.ME
 * 8.ONE
 * 9.UPON
 * 11.TO
 * 12.SO
 * 13.ERIN
 * 15.SA
 * 17.OR
 * 18.IES
 * 19.DEA
 * <p>
 * <p>
 * Down
 * <p>
 * 1.A
 * 2.IMPOSE
 * 3.MEO
 * 4.DO
 * 5.ENTIRE
 * 6.NEON
 * 9.US
 * 10.NE
 * 14.ROD
 * 16.AS
 * 18.I
 * 20.A
 */
public class Solution {
    private static char[] seeds_puzzle = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static char blackValue = '*';

    public static void main(String[] args) {
        Character[][][] target = {generatorPuzzle()};
        for (Character[][] item : target) {
            System.out.println("target:");
            System.out.println();
            print(item);
            System.out.println();
            System.out.println();
            System.out.println("value:");
            System.out.println();
            print(CrosswordAnswers(item));
            System.out.println("--------------------------------");
        }
    }

    private static void print(String[][] result) {
        for (String[] item : result) {
            StringBuilder stringBuilder = new StringBuilder();
            int len2 = item.length;
            for (int j = 0; j < len2; j++) {
                if (item[j] == null) {
                    break;
                }
                stringBuilder = stringBuilder.append(item[j]);
                if (j != len2 - 1) {
                    stringBuilder = stringBuilder.append(",");
                }
            }
            System.out.println(stringBuilder.toString());
        }
    }

    private static Character[][] puzzle = null;

    private static Element[][] puzzle_parsed = null;


    private static class Element {
        private int i = -1;//横坐标
        private int j = -1;//纵坐标
        private Character value = null;//值
        private boolean isStart = false;//是否为起始格？true为是起始格

        public Element(int i, int j, char value) {
            this.i = i;
            this.j = j;
            this.value = value;
        }

        public int getI() {
            return i;
        }

        public int getJ() {
            return j;
        }

        public Character getValue() {
            return value;
        }

        public boolean isStart() {
            return isStart;
        }

        public void setIsStart(boolean isStart) {
            this.isStart = isStart;
        }

        public boolean isWhite() {
            return value != null && value != blackValue;
        }

        public boolean isBlack() {
            return value != null && value == blackValue;
        }

        @Override
        public String toString() {
            return "i=" + i + ",j=" + j + ",value:" + value + ",isStart:" + isStart;
        }
    }

    private static void checkStart() {
        int len = puzzle_parsed.length;
        for (int i = 0; i < len; i++) {
            Element[] item = puzzle_parsed[i];
            int jLen = item.length;
            for (int j = 0; j < jLen; j++) {
                puzzle_parsed[i][j].setIsStart(puzzle_parsed[i][j].isWhite() && checkIsStart(i, j));
            }
        }
    }

    private static boolean checkIsStart(int i, int j) {
        if (puzzle_parsed[i][j].getValue() == blackValue) {
            return false;
        }
        Element left = findLeft(i, j);
        Element up = findUp(i, j);
        return left == null || left.isBlack() || up == null || up.isBlack();
    }

    private static Element findUp(int i, int j) {
        if (i <= 0) {
            return null;
        }
        return puzzle_parsed[i - 1][j];
    }

    private static Element findLeft(int i, int j) {
        if (j <= 0) {
            return null;
        }
        return puzzle_parsed[i][j - 1];
    }

    private static void parsePuzzle() {
        int len = puzzle.length;
        puzzle_parsed = new Element[len][puzzle[0].length];
        for (int i = 0; i < len; i++) {
            Character[] item = puzzle[i];
            int jLen = item.length;
            for (int j = 0; j < jLen; j++) {
                puzzle_parsed[i][j] = new Element(i, j, puzzle[i][j]);
            }
        }
    }

    private static String[][] CrosswordAnswers(Character[][] currentPuzzle) {
        puzzle = currentPuzzle;
        parsePuzzle();
        checkStart();
        int len = puzzle_parsed.length;
        String[][] result = new String[2][len * puzzle_parsed[0].length];
        //横向扫描
        findWord(puzzle_parsed, result[0]);
        //纵向扫描
        findWord(reverse(puzzle_parsed), result[1]);
        return result;
    }

    private static String[] findWord(Element[][] array, String[] result) {
        int index = 0;
        for (Element[] item : array) {
            int jLen = item.length;
            String target = "";
            for (int j = 0; j <= jLen; j++) {
                if (j == jLen) {
                    if (!target.isEmpty()) {
                        result[index++] = target;
                    }
                    break;
                }
                Element current = item[j];
                if (current.isWhite() && !(target.isEmpty() && !current.isStart())) {
                    target += current.getValue();
                } else {
                    if (!target.isEmpty()) {
                        result[index++] = target;
                        target = "";
                    }
                }
            }
        }
        return result;
    }

    private static Element[][] reverse(Element[][] array) {
        int len = array.length;
        Element[][] result = new Element[array[0].length][len];
        int rLen = result.length;
        for (int i = 0; i < rLen; i++) {
            Element[] item = result[i];
            int jLen = item.length;
            for (int j = 0; j < jLen; j++) {
                item[j] = array[j][i];
            }
        }
        return result;
    }


    private static void print(Character[][] arrays) {
        for (Character[] item : arrays) {
            StringBuilder stringBuilder = new StringBuilder();
            int len2 = item.length;
            for (int j = 0; j < len2; j++) {
                stringBuilder = stringBuilder.append(item[j]);
                if (j != len2 - 1) {
                    stringBuilder = stringBuilder.append(",");
                }
            }
            System.out.println(stringBuilder.toString());
        }
    }


    private static Character[][] generatorPuzzle() {
        int r = Util.getRandomInteger(4, 20);
        int c = Util.getRandomInteger(4, 10);
        Character[][] result = new Character[r][c];
        for (int i = 0; i < r; i++) {
            Character[] item = result[i];
            for (int j = 0; j < c; j++) {
                item[j] = needOneBlack() ? blackValue : seeds_puzzle[Util.getRandomInteger(0, seeds_puzzle.length - 1)];
            }
        }
        return result;
    }

    private static boolean needOneBlack() {//设置黑格子的出现几率：2/7
        return Util.getRandomInteger(1, 7) > 5;
    }
}
