package sort.insertion;

import basic.Comparator;
import basic.sort.SortHandlerBehavior;
import basic.sort.Sortable;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/12
 * Time:16:52
 * <p>
 * ���ԶԲ���������иĽ�
 * <p>
 * <p>
 * �Ľ�Ҫ�㣺ÿ����ǰ�����������Σ�Ѱ�Һ���λ�ý���swapʱ������Ҫ�������������
 * <p>
 * ʹ�ö��ֲ��ҽ���ʱ�临�ӶȽ��ͣ�O(n)  -->   O(log n)
 */
public class InsertionImprovementHandler extends SortHandlerBehavior {

    /**
     * ˼·��
     * �ڵ�i�ε����У�����i��Ԫ����ÿһ��������ұ�����ĵ�Ԫ�ؽ���λ��
     * <p>
     * <p>
     * ʱ�临�Ӷȣ�O(n^2)
     *
     * @param originArray ԭʼ��������
     * @return �ź��������
     * @throws Exception ������������ɽ���ʱ�׳�
     */
    @Override
    public Comparable[] sort(Comparable[] originArray) throws Exception {
        init(originArray);
        int len = originArray.length;
        if (len == 1) {
            return originArray;
        }
        if (len == 2) {
            swapIfLessThan(originArray, 1, 0);
            return originArray;
        }
        for (int i = 1; i < len; i++) {
            int endIndex = i - 1;
            if (endIndex < 0) {
                continue;
            }
            int possibleIndex = binarySearch(originArray, 0, i - 1, originArray[i]);
            if (possibleIndex < i - 1) {
                swapAlways(originArray, i, possibleIndex);
                move(originArray, possibleIndex + 1, endIndex);
            }
            if (possibleIndex == i - 1) {
                swapAlways(originArray, i, i - 1);
            }
        }
        return originArray;
    }

    /**
     * �ڸ��������begin��end�±�ֵ��Χ�ڣ���ÿһ��Ԫ������ƶ�1��
     */
    private void move(Comparable[] originArray, int beginIndex, int endIndex) {
        while (beginIndex <= endIndex) {
            swapAlways(originArray, endIndex, endIndex);
            endIndex--;
        }
    }

    /**
     * �ڸ��������begin��end�±�ֵ��Χ�ڣ��ҵ���һ����key���Ԫ���±�ֵ
     */
    private int binarySearch(Comparable[] originArray, int beginIndex, int endIndex, Comparable key) {
        if (beginIndex > endIndex) {
            return endIndex + 1;
        }
        if (Comparator.isLT(originArray[endIndex], key)) {
            return endIndex + 1;
        }
        int middleIndex = beginIndex + ((endIndex - beginIndex) / 2);
        if (Comparator.isGT(originArray[middleIndex], key)) {
            int previousIndex = middleIndex - 1;
            if (previousIndex < beginIndex) {
                return middleIndex;
            }
            //�ж���һ��ֵ�Ƿ����Ҫ��
            if (Comparator.isGT(originArray[previousIndex], key)) {
                return binarySearch(originArray, beginIndex, previousIndex, key);
            } else {
                return middleIndex;
            }
        } else {
            return binarySearch(originArray, middleIndex + 1, endIndex, key);
        }
    }

    public static void main(String[] args) throws Exception {
        Sortable sortable = new InsertionImprovementHandler();
        Integer f[] = {5, 0, 8, 58, 10, 2, 3, 7, 8, 9, 10, 12, 15, -5, 4, 152};
        System.out.println("---:" + Arrays.toString(f));
        System.out.println("---:" + Arrays.toString(sortable.sort(f)));
        benchmark(sortable);
    }
}
