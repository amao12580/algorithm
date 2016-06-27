package DP.coins;

import java.util.Arrays;

/**
 * http://www.hawstein.com/posts/dp-novice-to-advanced.html
 * <p>
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/6/23
 * Time:17:04
 * <p>
 * ����ֵΪ1Ԫ��3Ԫ��5Ԫ��Ӳ������ö����������ٵ�Ӳ�Ҵչ�11Ԫ��
 * <p>
 *
 * V1 û��ʹ��DP˼����⣬ֱ�Ӳ��õݹ鱩���ֽ�
 */
public class SolutionV1 {
    private static int maxValue = 0;
    private static int minValue = 0;
    private static int index = 0;
    private static int[] subSolution = null;
    private static int[] coins = null;
    private static int[] result = new int[maxValue];

    public static void main(String[] args) {
        //��ʼ��Ӳ�ҵ�����͸���
        coins = new int[5 + 1];//Ӳ�ҵ������ֵΪ5
        coins[1] = 10;//������ֵΪ1��Ӳ����10��
        coins[3] = 10;//������ֵΪ3��Ӳ����10��
        coins[5] = 10;//������ֵΪ5��Ӳ����10��
        //����û�г�ʼ����Ӳ����ֵ������˵��ֵΪ4������Ϊ0.
        int sum = 11;//Ӳ����Ҫ�չ�11Ԫ
        maxValue = 5;
        initSubSolution(sum + 1);
        minValue = firstElement(coins);
        int s = loop(sum);

        System.out.println(s);
        System.out.println("����Ӳ������" + result.length + "ö");
        System.out.println("result:" + Arrays.toString(result));
        System.out.println("subSolution:" + Arrays.toString(subSolution));
    }

    private static void initSubSolution(int len) {
        subSolution = new int[len];
        for (int i = 2; i < len; i++) {
            subSolution[i] = -1;
        }
        subSolution[0] = 0;
        subSolution[1] = 1;
        subSolution[3] = 1;
        subSolution[5] = 1;
    }

    private static int loop(int sum) {
        if (sum <= 0) {
            return 0;
        }
        int part = sum - maxValue;
        int here = subSolution[sum];
        if (here > 0) {
            append(sum);
            return here;
        } else {
            if (part < 0) {
                maxValue = previousElement(coins);
                part = sum;
            }
        }
        append(maxValue);
        return loop(part);
    }

    private static void append(int sum) {
        if (index > result.length - 1) {
            int[] newResult = new int[result.length + 1];
            System.arraycopy(result, 0, newResult, 0, result.length);
            result = newResult;
        }
        result[index] = sum;
        index++;
    }

    private static Integer previousElement(int[] coins) {
        if (maxValue < 1) {
            return minValue;
        }
        for (int i = maxValue - 1; i >= 0; i--) {
            if (coins[i] > 0) {
                return i;
            }
        }
        return minValue;
    }

    private static Integer firstElement(int[] coins) {
        for (int i = 0; i < coins.length; i++) {
            if (coins[i] > 0) {
                return i;
            }
        }
        return null;
    }
}
