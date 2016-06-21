package sort.shaker;

import basic.Comparator;
import basic.sort.SortHandlerBehavior;
import basic.sort.Sortable;

import java.util.Arrays;

/**
 * http://bubkoo.com/2014/01/15/sort-algorithm/shaker-sort/
 * <p>
 * <p>
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/12
 * Time:16:52
 */
public class ShakerHandler extends SortHandlerBehavior {

    /**
     * ��β��������ð���������΢���Ρ���ͬ�ĵط����ڣ���β�������Ǵӵ͵���Ȼ��Ӹߵ����������򣬶�ð����������ӵ͵���ȥ�Ƚ��������ÿ��Ԫ�ء�
     * ���ɱ�ð�������Ч����΢��һ�㣬ԭ����ð������ֻ��һ��������бȶ�(�ɵ͵���)��ÿ��ѭ��ֻ�ƶ�һ����Ŀ��
     * <p>
     * <p>
     * ������̣�
     * �ȶ���������ҽ���ð���������򣩣�������Ԫ��ȥ�����Ҷ�
     * �ٶ�������ҵ������ð�����򣨽��򣩣�����С��Ԫ��ȥ�������
     * �Դ����ƣ����θı�ð�ݵķ��򣬲�������Сδ����Ԫ�صķ�Χ��ֱ�����һ��Ԫ�ؽ���
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
        int endIndex = originArray.length - 1;
        int beginIndex = 0;
        while (beginIndex <= endIndex) {
            endIndex = ascending(originArray, beginIndex, endIndex);
            beginIndex = descending(originArray, beginIndex, endIndex);
        }
        return originArray;
    }

    private int ascending(Comparable[] originArray, int beginIndex, int endIndex) {
        for (int i = beginIndex; i <= endIndex; i++) {
            int nextIndex = i + 1;
            if (nextIndex > endIndex) {
                break;
            }
            swapIfLessThan(originArray, nextIndex, i);
        }
        endIndex--;
        return endIndex;
    }

    private int descending(Comparable[] originArray, int beginIndex, int endIndex) {
        for (int i = endIndex; i >= beginIndex; i--) {
            int previousIndex = i - 1;
            if (previousIndex < beginIndex) {
                break;
            }
            swapIfLessThan(originArray, i, previousIndex);
        }
        beginIndex++;
        return beginIndex;
    }

    /**
     * ���ԶԼ�β�������ԭʼʵ�ֽ��и���
     * <p>
     * ��һ�α����У�ͬʱ�ҵ����ֵ����Сֵ�������ֵ�ŵ�β������Сֵ�ŵ��ײ�
     * <p>
     * ��ʵ�����Ѿ���ѡ�������е�����
     * <p>
     * ����һ��ʱ�临�Ӷȣ�������O(n^2)������ԭʼ�㷨û��̫�������
     *
     * @param originArray
     * @return
     * @throws Exception
     */
    public Comparable[] sortImprovement(Comparable[] originArray) throws Exception {
        init(originArray);
        int len = originArray.length;
        if (len == 1) {
            return originArray;
        }
        if (len == 2) {
            swapIfLessThan(originArray, 1, 0);
            return originArray;
        }
        int endIndex = originArray.length - 1;
        int beginIndex = 0;
        while (beginIndex <= endIndex) {
            int[] result = doubleWay(originArray, beginIndex, endIndex);
            beginIndex = result[0];
            endIndex = result[1];
        }
        return originArray;
    }

    private int[] doubleWay(Comparable[] originArray, int beginIndex, int endIndex) {
        int[] result = new int[2];
        int minIndex = beginIndex;
        Comparable minvalue = originArray[minIndex];
        for (int i = beginIndex; i <= endIndex; i++) {
            int nextIndex = i + 1;
            if (nextIndex > endIndex) {
                break;
            }
            Comparable nextValue = originArray[nextIndex];
            if (Comparator.isLT(nextValue, minvalue)) {
                minIndex = nextIndex;
                minvalue = originArray[minIndex];

            }
            if (Comparator.isLT(nextValue, originArray[i])) {
                swapAlways(originArray, nextIndex, i);
                if (Comparator.isEQUAL(nextValue, minvalue)) {
                    minIndex = i;
                    minvalue = originArray[minIndex];
                }
            }
        }
        if (minIndex != beginIndex) {
            swapAlways(originArray, beginIndex, minIndex);
        }
        beginIndex++;
        endIndex--;
        result[0] = beginIndex;
        result[1] = endIndex;
        return result;
    }

    public static void main(String[] args) throws Exception {
        Sortable sortable = new ShakerHandler();
        Integer f[] = {113, 19, 0, 5, 12, 8, 7, 4, 11, 2, 6, 21};
        //System.out.println("---:" + Arrays.toString(sortable.sort(Util.getRandomIntegerNumberArray(1000000, 0, 100))));
        System.out.println("---:" + Arrays.toString(sortable.sort(f)));
        //benchmark(sortable, 0, 10000);
    }
}
