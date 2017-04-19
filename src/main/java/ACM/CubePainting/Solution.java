package ACM.CubePainting;

import basic.Util;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/4/18
 * Time:17:31
 * <p>
 * 输入两个骰子，判断二者是否等价。每个骰子用6个字母表示，如图4-7所示。
 * <p>
 * 例如rbgggr和rggbgr分别表示如图4-8所示的两个骰子。二者是等价的，因为图4-8（a）
 * 所示的骰子沿着竖直轴旋转90°之后就可以得到图4-8（b）所示的骰子。
 * <p>
 * Cube painting, UVa 253
 * <p>
 * 我們想要為方塊塗上顏色，我們有三種顏色：藍色、紅色、綠色。方塊的每一面塗上其中一種顏色。
 * <p>
 * 把每个面旋转到第一个面，然后第一个面与它对应的面不动再进行四次旋转
 */
public class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        solution.case2();
    }

    private void case1() {
        System.out.println("----------------case1 start----------------------");
        cubeLeft = generatorOne();
        cubeRight = generatorOne();
        major();
        System.out.println("----------------case1 end----------------------");
    }

    private void case2() {
        System.out.println("----------------case2 start----------------------");
        cubeLeft = new char[]{'r', 'b', 'g', 'g', 'g', 'r'};
        cubeRight = new char[]{'r', 'g', 'g', 'b', 'g', 'r'};
        major();
        System.out.println("----------------case2 end----------------------");
        System.out.println();
    }

    private void major() {
        System.out.print("cubeLeft:");
        print(cubeLeft);
        System.out.print("cubeRight:");
        print(cubeRight);
        System.out.println("check:" + equivalence(cubeLeft, cubeRight));
    }

    private boolean equivalence(char[] left, char[] right) {
        char[] myLeft = fullCopy(left);
        if (equals(myLeft, right)) {
            System.out.println("origin equals.");
            return true;
        }
        //1
        if (equivalence(myLeft, right, 3)) {
            System.out.println("1 phase success.");
            return true;
        }
        //5
        myLeft = rotate90Degrees(myLeft, 1);
        if (equivalence(myLeft, right, 3)) {
            System.out.println("5 phase success.");
            return true;
        }
        //6
        myLeft = rotate90Degrees(myLeft, 1);
        if (equivalence(myLeft, right, 3)) {
            System.out.println("6 phase success.");
            return true;
        }
        //2
        myLeft = rotate90Degrees(myLeft, 1);
        if (equivalence(myLeft, right, 3)) {
            System.out.println("2 phase success.");
            return true;
        }
        //3
        myLeft = rotate90Degrees(myLeft, 2);
        if (equivalence(myLeft, right, 3)) {
            System.out.println("3 phase success.");
            return true;
        }
        //4
        myLeft = rotate90Degrees(myLeft, 2, 2);
        if (equivalence(myLeft, right, 3)) {
            System.out.println("4 phase success.");
            return true;
        }
        return false;
    }

    private boolean equivalence(char[] left, char[] right, int direction) {
        int count = 3;
        char[] temp = fullCopy(left);
        while (count > 0) {
            temp = rotate90Degrees(temp, direction);
            if (equals(temp, right)) {
                return true;
            } else {
                count--;
            }
        }
        return false;
    }

    private char[] fullCopy(char[] src) {
        if (src == null) {
            return null;
        }
        int len = src.length;
        char[] result = new char[len];
        System.arraycopy(src, 0, result, 0, len);
        return result;
    }

    /**
     * 旋转90度
     *
     * @param origin    原始顺序
     * @param direction 旋转方向
     * @param times     次数
     * @return 旋转完成后的顺序
     */
    private char[] rotate90Degrees(char[] origin, int direction, int times) {
        char[] result = fullCopy(origin);
        switch (direction) {
            case 1://X轴:1-->2
                swap(result, 0, 1, 5, 4, times);
                return result;
            case 2://Y轴:1-->4
                swap(result, 0, 3, 5, 2, times);
                return result;
            case 3://Z轴:2-->4
                swap(result, 1, 3, 4, 2, times);
                return result;
        }
        return result;
    }

    private char[] rotate90Degrees(char[] origin, int direction) {
        return rotate90Degrees(origin, direction, 1);
    }

    private void swap(char[] array, int a, int b, int c, int d, int times) {
        for (int i = times; i > 0; i--) {
            swap(array, a, b, c, d);
        }
    }

    private void swap(char[] array, int a, int b, int c, int d) {
        char[] temp = {array[d], array[a], array[b], array[c]};
        array[a] = temp[0];
        array[b] = temp[1];
        array[c] = temp[2];
        array[d] = temp[3];
    }

    private boolean equals(char[] thisOne, char[] otherOne) {
        int len = thisOne.length;
        if (len != otherOne.length) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (thisOne[i] != otherOne[i]) {
                return false;
            }
        }
        return true;
    }

    private void print(char[] cube) {
        System.out.println(Arrays.toString(cube));
    }

    private char[] seeds = {'r', 'g', 'b'};
    private char[] cubeLeft = null;
    private char[] cubeRight = null;

    private char[] generatorOne() {
        char[] result = new char[6];
        for (int i = 0; i < 6; i++) {
            result[i] = seeds[Util.getRandomInteger(0, seeds.length - 1)];
        }
        return result;
    }
}
