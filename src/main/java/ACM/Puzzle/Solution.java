package ACM.Puzzle;

import basic.Util;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/3/23
 * Time:16:16
 * <p>
 * 习题3-5　谜题（Puzzle, ACM/ICPC World Finals 1993, UVa227）
 * 有一个5*5的网格，其中恰好有一个格子是空的，其他格子各有一个字母。一共有4种指
 * 令：A, B, L, R，分别表示把空格上、下、左、右的相邻字母移到空格中。输入初始网格和指令序列（以数字0结束），输出指令执行完毕后的网格。
 * 如果有非法指令，应输出“Thispuzzle has no final configuration.”，例如，图3-5中执行ARRBBL0后，效果如图3-6所示。
 */
public class Solution {
    private static char[] seeds_puzzle = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static char[] seeds_command = {'A', 'B', 'L', 'R'};

    public static void main(String[] args) {
        Character[][][] target = {generatorPuzzle()};
        for (Character[][] item : target) {
            System.out.println("target:");
            System.out.println();
            print(item);
            System.out.println();
            System.out.println();
            System.out.println();
            String command = generatorCommand();
            System.out.println("value:" + command);
            System.out.println();
            print(Puzzle(item, command));
            System.out.println("--------------------------------");
        }
    }

    private static Element spaceElement = null;
    private static Character[][] puzzle = null;

    private static class Element {
        private int i = -1;//横坐标
        private int j = -1;//纵坐标

        public Element(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public int getI() {
            return i;
        }

        public int getJ() {
            return j;
        }

        @Override
        public String toString() {
            return "i=" + i + ",j=" + j;
        }
    }

    private static void findSpace() {
        for (int i = 0; i < 5; i++) {
            Character[] item = puzzle[i];
            for (int j = 0; j < 5; j++) {
                if (item[j] == null) {
                    spaceElement = new Element(i, j);
                    break;
                }
            }
        }
    }

    private static void move(char command) {
        switch (command) {
            case 'A':
                Element up = findUp();
//                System.out.println("space:" + spaceElement.toString() + ",up:" + (up == null ? "" : up.toString()));
                if (up != null) {
                    swap(up);
                }
                break;
            case 'B':
                Element down = findDown();
//                System.out.println("space:" + spaceElement.toString() + ",down:" + (down == null ? "" : down.toString()));
                if (down != null) {
                    swap(down);
                }
                break;
            case 'L':
                Element left = findLeft();
//                System.out.println("space:" + spaceElement.toString() + ",left:" + (left == null ? "" : left.toString()));
                if (left != null) {
                    swap(left);
                }
                break;
            case 'R':
                Element right = findRight();
//                System.out.println("space:" + spaceElement.toString() + ",right:" + (right == null ? "" : right.toString()));
                if (right != null) {
                    swap(right);
                }
                break;
        }
    }

    private static void swap(Element element) {
        Character temp = puzzle[spaceElement.getI()][spaceElement.getJ()];
        Character current = puzzle[element.getI()][element.getJ()];
        puzzle[spaceElement.getI()][spaceElement.getJ()] = current;
        puzzle[element.getI()][element.getJ()] = temp;
        spaceElement = new Element(element.getI(), element.getJ());
    }

    private static Element findRight() {
        if (spaceElement.getJ() < 4) {
            return new Element(spaceElement.getI(), spaceElement.getJ() + 1);
        }
        return null;
    }

    private static Element findLeft() {
        if (spaceElement.getJ() > 0) {
            return new Element(spaceElement.getI(), spaceElement.getJ() - 1);
        }
        return null;
    }

    private static Element findDown() {
        if (spaceElement.getI() < 4) {
            return new Element(spaceElement.getI() + 1, spaceElement.getJ());
        }
        return null;
    }

    private static Element findUp() {
        if (spaceElement.getI() > 0) {
            return new Element(spaceElement.getI() - 1, spaceElement.getJ());
        }
        return null;
    }

    private static Character[][] Puzzle(Character[][] currentPuzzle, String command) {
        if (command == null || command.isEmpty()) {
            return currentPuzzle;
        }
        int len = command.length();
        puzzle = currentPuzzle;
        findSpace();
        for (int i = 0; i < len; i++) {
            move(command.charAt(i));
        }
        return puzzle;
    }

    private static void print(Character[][] arrays) {
        for (int i = 0; i < 5; i++) {
            Character[] item = arrays[i];
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < 5; j++) {
                stringBuilder = stringBuilder.append(item[j] == null ? "*" : item[j]);
                if (j != 4) {
                    stringBuilder = stringBuilder.append(",");
                }
            }
            System.out.println(stringBuilder.toString());
        }
    }


    private static Character[][] generatorPuzzle() {
        Character[][] result = new Character[5][5];
        for (int i = 0; i < 5; i++) {
            Character[] item = result[i];
            for (int j = 0; j < 5; j++) {
                item[j] = seeds_puzzle[Util.getRandomInteger(0, seeds_puzzle.length - 1)];
            }
        }
        int i = Util.getRandomInteger(0, 4);
        int j = Util.getRandomInteger(0, 4);
        result[i][j] = null;
        return result;
    }

    private static String generatorCommand() {
        StringBuilder stringBuilder = new StringBuilder();
        int len = Util.getRandomInteger(5, 15);
        for (int i = 0; i < len; i++) {
            stringBuilder = stringBuilder.append(seeds_command[Util.getRandomInteger(0, seeds_command.length - 1)]);
        }
        return stringBuilder.toString();
    }

}
