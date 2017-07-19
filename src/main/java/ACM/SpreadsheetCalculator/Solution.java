package ACM.SpreadsheetCalculator;

import basic.Util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/7/19
 * Time:15:39
 * <p>
 * 在一个R行C列（R≤20，C≤10）的电子表格中，行编号为A～T，列编号为0～9。按照行
 * 优先顺序输入电子表格的各个单元格。每个单元格可能是整数（可能是负数）或者引用了其
 * 他单元格的表达式（只包含非负整数、单元格名称和加减号，没有括号）。表达式保证以单
 * 元格名称开头，内部不含空白字符，且最多包含75个字符。
 * 尽量计算出所有表达式的值，然后输出各个单元格的值（计算结果保证为绝对值不超过
 * 10000的整数）。如果某些单元格循环引用，在表格之后输出（仍按行优先顺序），如图6-
 * 31所示。
 * <p>
 * Spreadsheet Calculator, ACM/ICPC World Finals 1992, UVa215
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=151
 * <p>
 * Sample Input
 * 2 2
 * A1+B1
 * 5
 * 3
 * B0-A1
 * 3 2
 * A0
 * 5
 * C1
 * 7
 * A1+B1
 * B0+A1
 * 0 0
 * Sample Output
 * 0 1
 * A 3 5
 * B 3 -2
 * A0: A0
 * B0: C1
 * C1: B0+A1
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("-------------------------------------------------");
        solution.case2();
    }

    private void case1() {
        String[] desc = {"A1+B1", "5", "3", "B0-A1"};
        calculator(2, 2, desc);
    }

    private void case2() {
        String[] desc = {"A0", "5", "C1", "7", "A1+B1", "B0+A1"};
        calculator(3, 2, desc);
    }

    private void calculator(int row, int column, String[] desc) {
        String[][] array = build(desc, row, column);
        int[][] real = new int[row][column];
        boolean isContainsLoopReference = false;
        boolean isLoopReference;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                isLoopReference = parseExpress(i, j, array, real);
                if (isLoopReference && !isContainsLoopReference) {
                    isContainsLoopReference = true;
                }
            }
        }
        if (!isContainsLoopReference) {
            printReal(real, row, column);
        }
    }

    private void printReal(int[][] real, int row, int column) {
        printFirstLine(column);
        StringBuilder builder;
        for (int i = 0; i < row; i++) {
            builder = new StringBuilder(String.valueOf((char) (65 + i)));
            builder.append(" ");
            for (int j = 0; j < column; j++) {
                builder = builder.append(real[i][j]).append(" ");
            }
            System.out.println(builder.toString());
        }
    }

    private void printFirstLine(int column) {
        StringBuilder builder = new StringBuilder("  ");
        for (int i = 0; i < column; i++) {
            builder = builder.append(i).append(" ");
        }
        System.out.println(builder.toString());
    }

    private boolean parseExpress(int rowIndex, int columnIndex, String[][] array, int[][] real) {
        String express = array[rowIndex][columnIndex];
        String location = String.valueOf((char) (65 + rowIndex)) + columnIndex;
        Integer value = parseExpress0(rowIndex, columnIndex, array, real, new HashSet<>());
        if (value == null) {
            System.out.println(location + ":" + express);
            return true;
        }
        real[rowIndex][columnIndex] = value;
        array[rowIndex][columnIndex] = String.valueOf(real[rowIndex][columnIndex]);
        return false;
    }

    private Integer parseExpress0(int rowIndex, int columnIndex, String[][] array, int[][] real, Set<String> notAllowed) {
        String express = array[rowIndex][columnIndex];
        String location = String.valueOf((char) (65 + rowIndex)) + columnIndex;
        if (!Util.isUpperCaseAlpha(express.charAt(0))) {
            remove(notAllowed, location);
            real[rowIndex][columnIndex] = Integer.valueOf(express);
            return real[rowIndex][columnIndex];
        }
        if (location.equals(express)) {
            return null;
        }
        char[] chars = express.toCharArray();
        int beginIndex = 0;
        int endIndex = express.length() - 1;
        Object[] partArray = new Object[3];
        int value = 0;
        int count = 0;
        while (partArray != null) {
            partArray = parse(express, chars, beginIndex, endIndex);
            if (partArray != null) {
                String part = partArray[0].toString();
                if (!notAllowed.add(part)) {
                    return null;
                }
                Integer partValue = parseExpress0((int) part.charAt(0) - 65, Integer.valueOf(String.valueOf(part.charAt(1))), array, real, notAllowed);
                if (partValue == null) {
                    return null;
                }
                beginIndex = Integer.valueOf(partArray[1].toString());
                if (count == 0) {
                    value = partValue;
                } else {
                    if (partArray[2] != null) {
                        if (Boolean.valueOf(partArray[2].toString())) {
                            value += partValue;
                        } else {
                            value -= partValue;
                        }
                    }
                }
                count++;
            }
        }
        real[rowIndex][columnIndex] = value;
        array[rowIndex][columnIndex] = String.valueOf(real[rowIndex][columnIndex]);
        return value;
    }

    private void remove(Set<String> set, String key) {
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(key)) {
                iterator.remove();
                break;
            }
        }
    }

    private Object[] parse(String string, char[] chars, int beginIndex, int endIndex) {
        Object[] result;
        if (beginIndex > endIndex) {
            return null;
        }
        int cBeginIndex = -1;
        int cEndIndex = -1;
        char c;
        for (int i = beginIndex; i < endIndex; i++) {
            c = chars[i];
            if (c != '+' && c != '-' && cBeginIndex < 0) {
                cBeginIndex = i;
            }
            if (c == '+' || c == '-') {
                cEndIndex = i;
            }
            if (cBeginIndex >= 0 && cEndIndex >= cBeginIndex) {
                result = new Object[3];
                result[0] = string.substring(cBeginIndex, cEndIndex);
                result[1] = cEndIndex + 1;
                if (beginIndex > 0) {
                    result[2] = string.charAt(beginIndex - 1) == '+';
                }
                return result;
            }
        }
        result = new Object[3];
        result[0] = string.substring(beginIndex);
        result[1] = endIndex + 1;
        if (beginIndex > 0) {
            result[2] = string.charAt(beginIndex - 1) == '+';
        }
        result[2] = result[2] == null ? true : result[2];
        return result;
    }

    private String[][] build(String[] desc, int row, int column) {
        int r = 0, c = 0;
        String[][] result = new String[row][column];
        row--;
        column--;
        for (String s : desc) {
            if (c > column) {
                c = 0;
                r++;
                if (r > row) {
                    break;
                }
            }
            result[r][c] = s;
            c++;
        }
        return result;
    }
}
