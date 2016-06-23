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
public class Solution {
    public static void main(String[] args) {
        int[] originArray = {0, 1, 2, 3, 4, 5, -1, -2, -3, -4, -5};
        System.out.println("-------      simple        -------");
        int[][] result = simple(originArray, 3);
        println(result);

        System.out.println("-------      recursion        -------");
        int[][] result2 = recursion(originArray, 3);
        println(result2);
    }

    private static void println(int[][] result) {
        System.out.println();
        System.out.println("-------      begin        -------");
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
                        //System.out.println(i + "," + j + "," + k + "," + Arrays.toString(group));
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

    /**
     * 3��forѭ���ĵݹ�汾
     *
     * @param originArray ԭʼ����
     * @param number      ��������ӣ�
     * @return ������
     */
    private static int[][] recursion(int[] originArray, int number) {
        topLayer = number;
        //println(originArray);
        resultArray = new int[number*2][number];
        loop(originArray, new int[topLayer], 0, 0, number);
        if ((resultArray.length - currentOffset) > 0) {
            resultArray = trim(resultArray, currentOffset);
        }
        return resultArray;
    }

    private static int[][] resultArray = null;
    private static int currentOffset = 0;
    private static int topLayer = 0;

    private static Integer loop(int[] originArray, int[] chain, int beginIndex, int expectSumValue, int currentLayer) {
        Integer currentLayerLoopResult = null;
        for (int i = beginIndex; i < originArray.length; i++) {
            int currentValue = originArray[i];
            if (topLayer == currentLayer) {//���loopʱ��������ÿ�
                chain = new int[topLayer];
            } else {
                chain = copy(chain);
            }
            if (currentLayer > 0) {
                Integer nextLayerLoopResult;
                chain[topLayer - currentLayer] = currentValue;
                if (currentLayer == 1) {//��ײ㲻��Ҫ������loop
                    nextLayerLoopResult = 0;
                } else {
                    nextLayerLoopResult = loop(originArray, chain, i + 1, expectSumValue - currentValue, currentLayer - 1);
                }
                int currentLayerSumValue = currentValue;
                if (nextLayerLoopResult == null) {
                    continue;
                } else {
                    currentLayerSumValue += nextLayerLoopResult;
                }
                if (currentLayerSumValue == expectSumValue) {
                    currentLayerLoopResult = currentValue;
                    if (currentLayer == 1) {
                        mergeArray(chain);
                        return currentLayerLoopResult;
                    }
                }
            } else {
                return currentLayerLoopResult;
            }
        }
        return currentLayerLoopResult;
    }

    private static int[] copy(int[] chain) {
        int[] result = new int[chain.length];
        System.arraycopy(chain, 0, result, 0, chain.length);
        return result;
    }

    private static void mergeArray(int[] chain) {
        if (currentOffset >= resultArray.length) {
            resultArray = resize(resultArray);
        }
        resultArray[currentOffset] = chain;
        currentOffset++;
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

    private static int[] resize(int[] result) {
        int[] newResult = new int[result.length + 1];
        System.arraycopy(result, 0, newResult, 0, result.length);
        return newResult;
    }

    private static void println(int[] group) {
        System.out.println(Arrays.toString(group));
    }
}
