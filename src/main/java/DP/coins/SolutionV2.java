package DP.coins;

import basic.Comparator;

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
 * V2����DP˼��������
 */
public class SolutionV2 {
    private static int[] existedSolution = null;
    private static int[] coins = {5, 3, 1};//ֻ��3��Ӳ��;

    public static void main(String[] args) {
        int sum = 11;//Ӳ����Ҫ�չ�11Ԫ
        initExistedSolution(sum + 1);
        int count = loop(sum);
        System.out.println(count);
        System.out.println("cached:" + Arrays.toString(existedSolution));
    }

    /**
     * ��ʼ��һЩ��֪�Ĵ𰸣�����չ�5Ԫ��������һö���չ�3Ԫ��������һö��
     */
    private static void initExistedSolution(int len) {
        existedSolution = new int[len];
        for (int i = 0; i < len; i++) {
            if (i == 0) {
                existedSolution[0] = 0;
            } else {
                if (i < coins.length) {
                    existedSolution[coins[i]] = 1;
                } else {
                    existedSolution[i] = -1;
                }
            }
        }
    }


    private static int loop(int amount) {
        if (amount <= 0) {
            return existedSolution[0];
        }
        //�������һ����֪�Ĵ𰸣���ȡ������
        if (existedSolution[amount] >= 0) {
            return existedSolution[amount];
        }
        //���Ϊͬ���ʵ�������
        int[] group = new int[coins.length];
        int index = 0;
        for (int coin : coins) {
            //״̬���̣�ÿһ���������ܳ���3��Ӳ��
            int part = amount - coin;
            int count;
            if (part < 0) {
                count = -1;
            } else {
                count = loop(part) + 1;
            }
            group[index] = count;
            index++;
        }
        //״̬ת�Ʒ��̣�������������Ĵ��У�ȡ����Сֵ
        //ͬʱ�����еĴ𰸻��棬��һ������Ͳ��ٻ�仯�ˡ�
        existedSolution[amount] = min(group);
        return existedSolution[amount];
    }

    /**
     * ���������е���СԪ�أ�С�����Ԫ�ز��������
     */
    public static int min(int[] array) {
        array = lessThanOrEqualZero(array);
        int min = array[0];
        for (int e : array) {
            if (Comparator.isGT(min, e)) {
                min = e;
            }
        }
        return min;
    }

    /**
     * �޳�������������С��0��Ԫ�أ���resize����
     */
    public static int[] lessThanOrEqualZero(int[] array) {
        int[] result = new int[array.length];
        int index = 0;
        for (int e : array) {
            if (e >= 0) {
                result[index] = e;
                index++;
            }
        }
        if (index < array.length) {
            int[] finalResult = new int[index];
            System.arraycopy(result, 0, finalResult, 0, index);
            result = finalResult;
        }
        return result;
    }
}
