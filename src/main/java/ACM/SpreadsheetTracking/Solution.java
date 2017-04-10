package ACM.SpreadsheetTracking;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/4/8
 * Time:18:06
 * <p>
 * 有一个r行c列（1≤r，c≤50）的电子表格，行从上到下编号为1～r，列从左到右编号为1～c。
 * 如图4-2（a）所示，如果先删除第1、5行，然后删除第3, 6, 7, 9列，结果如图4-2（b）所示。
 * <p>
 * 接下来在第2、3、5行前各插入一个空行，然后在第3列前插入一个空列，会得到如图4-3所示结果。
 * <p>
 * 你的任务是模拟这样的n个操作。具体来说一共有5种操作：EX r1 c1 r2 c2交换单元格(r1,c1),(r2,c2)。
 * <command> A x 1 x 2 … x A 插入或删除A行或列（DC-删除列，DR-删除行，IC-插入列，IR-插入行，1≤A≤10）。
 * 在插入／删除指令后，各个x值不同，且顺序任意。
 * 接下来是q个查询，每个查询格式为“r c”，表示查询原始表格的单元格(r,c)。
 * 对于每个查询，输出操作执行完后该单元格的新位置。输入保证在任意时刻行列数均不超过50。
 * <p>
 * Spreadsheet Tracking, ACM/ICPC World Finals 1997, UVa512
 */
public class Solution {
    public static void main(String[] args) {
        Solution case1 = new Solution();
        case1.case1();
    }

    private void case1() {
        String[][] grid = {
                {"22", "55", "66", "77", "88", "99", "10", "12", "14"},
                {"2", "24", "6", "8", "22", "12", "14", "16", "18"},
                {"18", "19", "20", "21", "22", "23", "24", "25", "26"},
                {"24", "25", "26", "67", "22", "69", "70", "71", "77"},
                {"68", "78", "79", "80", "22", "25", "28", "29", "30"},
                {"16", "12", "11", "10", "22", "56", "57", "58", "59"},
                {"33", "34", "35", "36", "22", "38", "39", "40", "41"}
        };
        setGrid(grid);
        printGrid();
        deleteRow(1);
        deleteRow(5);
        deleteColumn(3);
        deleteColumn(6);
        deleteColumn(7);
        deleteColumn(9);
        resize();
        printGrid();
        addEmptyRow(2, 3, 5);
        addEmptyColumn(3, 5);
        resize();
        printGrid();
        swapCell(3, 2, 6, 5);
        printGrid();
    }

    private String[][] myGrid = null;
    private int rows = 0;
    private int columns = 0;
    private boolean needResize = false;

    private void setGrid(String[][] grid) {
        myGrid = grid;
        rows = myGrid == null ? 0 : myGrid.length;
        columns = myGrid != null && myGrid[0] != null ? myGrid[0].length : 0;
    }

    private boolean checkInRange(Integer rowNum, Integer columnNum) {
        if (myGrid == null) {
            return false;
        }
        if (rowNum != null) {
            if (rowNum < 1) {
                return false;
            }
            int max = myGrid.length;
            if (rowNum > max) {
                return false;
            }
        }
        if (columnNum != null) {
            if (columnNum < 1) {
                return false;
            }
            int max = myGrid[0].length;
            if (columnNum > max) {
                return false;
            }
        }
        return true;
    }

    private void deleteRow(int rowNum) {
        if (!checkInRange(rowNum, null)) {
            return;
        }
        myGrid[rowNum - 1] = new String[myGrid[rowNum - 1].length];
        rows--;
        needResize = true;
    }

    private void deleteColumn(int columnNum) {
        if (!checkInRange(null, columnNum)) {
            return;
        }
        int len = myGrid.length;
        for (int i = 0; i < len; i++) {
            String[] item = myGrid[i];
            int jLen = item.length;
            for (int j = 0; j < jLen; j++) {
                if (j == columnNum - 1) {
                    myGrid[i][j] = null;
                }
            }
        }
        columns--;
        needResize = true;
    }

    private void resize() {
        if (!needResize) {
            return;
        }
        if (rows <= 0 || columns <= 0) {
            setGrid(null);
        }
        String[][] grid = new String[rows][columns];
        int i1 = 0;
        for (String[] item : myGrid) {
            int jLen = item.length;
            int realLen = jLen;
            String[] temp = new String[jLen];
            int index = 0;
            for (String c : item) {
                if (c != null) {
                    temp[index] = c;
                    index++;
                } else {
                    realLen--;
                }
            }
            if (realLen <= 0) {
                continue;
            }
            String[] news = new String[realLen];
            int index2 = 0;
            for (int j = 0; j < jLen; j++) {
                String c = temp[j];
                if (c == null) {
                    break;
                }
                news[index2] = temp[j];
                index2++;
            }
            grid[i1] = news;
            i1++;
        }
        setGrid(grid);
        needResize = false;
    }

    private void addEmptyRow(int... rowNumbers) {
        String[] empty = new String[50];
        for (int i = 0; i < 50; i++) {
            empty[i] = "";
        }
        int numbers = rowNumbers.length;
        if (myGrid == null) {
            String[][] grid = new String[numbers][empty.length];
            for (int i = 0; i < numbers; i++) {
                grid[i] = empty;
            }
            setGrid(grid);
            return;
        }
        empty = new String[columns];
        for (int i = 0; i < columns; i++) {
            empty[i] = "";
        }
        int[] temp2 = new int[numbers];
        int index2 = 0;
        for (int c : rowNumbers) {
            if (checkInRange(c, null)) {
                temp2[index2] = c;
                index2++;
            }
        }
        if (index2 == 0) {
            return;
        }
        if (index2 < numbers) {
            int[] resize = new int[index2];
            System.arraycopy(temp2, 0, resize, 0, index2);
            rowNumbers = resize;
            numbers = rowNumbers.length;
        }
        Arrays.sort(rowNumbers);
        String[][] temp = new String[rows + numbers][myGrid[0].length];
        int addIndex = 0;
        for (int i = 0; i < rows; i++) {
            int index = Arrays.binarySearch(rowNumbers, i + 1);
            if (index >= 0) {
                rowNumbers[index] = -1;
                temp[addIndex] = empty;
                addIndex++;
            }
            temp[addIndex] = myGrid[i];
            addIndex++;
        }
        myGrid = temp;
        rows += rowNumbers.length;
    }

    private void addEmptyColumn(int... columnNumbers) {
        int numbers = columnNumbers.length;
        int[] temp2 = new int[numbers];
        int index2 = 0;
        for (int c : columnNumbers) {
            if (checkInRange(null, c)) {
                temp2[index2] = c;
                index2++;
            }
        }
        if (index2 == 0) {
            return;
        }
        if (index2 < numbers) {
            int[] resize = new int[index2];
            System.arraycopy(temp2, 0, resize, 0, index2);
            columnNumbers = resize;
            numbers = columnNumbers.length;
        }
        Arrays.sort(columnNumbers);
        int len = myGrid.length;
        for (int i = 0; i < len; i++) {
            String[] item = myGrid[i];
            int jLen = item.length;
            String[] a = new String[jLen + numbers];
            int addIndex = 0;
            int[] temp = new int[numbers];
            System.arraycopy(columnNumbers, 0, temp, 0, numbers);
            for (int j = 0; j < jLen; j++) {
                int index = Arrays.binarySearch(temp, j + 1);
                if (index >= 0) {
                    temp[index] = -1;
                    a[addIndex] = "";
                    addIndex++;
                }
                a[addIndex] = item[j];
                addIndex++;
            }
            myGrid[i] = a;
        }
        columns += numbers;
    }

    private void swapCell(int thisRowNum, int thisColumnNum, int otherRowNum, int otherColumnNum) {
        if (!checkInRange(thisRowNum, thisColumnNum)) {
            return;
        }
        if (!checkInRange(otherRowNum, otherColumnNum)) {
            return;
        }
        thisRowNum--;
        thisColumnNum--;
        otherRowNum--;
        otherColumnNum--;
        String temp = myGrid[thisRowNum][thisColumnNum];
        myGrid[thisRowNum][thisColumnNum] = myGrid[otherRowNum][otherColumnNum];
        myGrid[otherRowNum][otherColumnNum] = temp;
    }

    private void printGrid() {
        if (myGrid == null) {
            System.out.println("*");
        }
        for (String[] item : myGrid) {
            StringBuilder stringBuilder = new StringBuilder();
            int jLen = item.length;
            for (int j = 0; j < jLen; j++) {
                String c = item[j];
                if (c == null) {
                    c = "*";
                }
                if ("".equals(c)) {
                    c = "#";
                }
                if (c.length() == 1) {
                    c = " " + c;
                }
                stringBuilder = stringBuilder.append(c);
                if (j != jLen - 1) {
                    stringBuilder = stringBuilder.append(",");
                }
            }
            System.out.println(stringBuilder.toString());
        }
        System.out.println("-------------------------------------------------------");
    }
}
