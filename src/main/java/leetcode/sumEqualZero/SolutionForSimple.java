package leetcode.sumEqualZero;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/6/22
 * Time:13:39
 * <p>
 * ��һ������������飬������ABC������������ABC�������������������㣬���ж��ٸ����ظ�����ϣ�
 */
public class SolutionForSimple {
    public static void main(String[] args) {
        int[] originArray = {0, 1, 2, 3, 4, 5, -1, -2, -3, -4, -5};
        System.out.println("input:" + Arrays.toString(originArray));
        System.out.println("-------      simple        -------");
        println(simple(originArray, 3));
    }

    private static void println(int[][] result) {
        System.out.println();
        System.out.println("-------      begin        -------");
        System.out.println("-------      count:" + result.length + "        -------");
        for (int i = 0; i < result.length; i++) {
            System.out.println((i + 1) + ":" + Arrays.toString(result[i]));
        }
        System.out.println("-------      end        -------");
    }

    /**
     * ��ʵ�֣�3��forѭ����ͬʱȥ��
     *
     * @param originArray ԭʼ����
     * @param number      ��������ӣ�
     * @return ������
     */
    private static int[][] simple(int[] originArray, int number) {
        int length = originArray.length;
        int[][] result = new int[length][number];
        int index = 0;
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                for (int k = j + 1; k < length; k++) {
                    if ((originArray[i] + originArray[j] + originArray[k]) == 0) {
                        int[] group = {originArray[i], originArray[j], originArray[k]};
                        if (index >= result.length) {
                            result = resize(result);
                        }
                        result[index] = group;
                        index++;
                    }
                }
            }
        }
        if ((result.length - index) > 0) {
            result = trim(result, index);
        }
        return result;
    }

    private static int[][] trim(int[][] result, int index) {
        int[][] newResult = new int[index][result[0].length];
        System.arraycopy(result, 0, newResult, 0, newResult.length);
        return newResult;
    }

    private static int[][] resize(int[][] result) {
        int[][] newResult = new int[result.length + result[0].length][result[0].length];
        System.arraycopy(result, 0, newResult, 0, result.length);
        return newResult;
    }
}
