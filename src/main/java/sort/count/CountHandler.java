package sort.count;

import basic.sort.SortHandlerBehavior;
import basic.sort.Sortable;

/**
 * https://segmentfault.com/a/1190000003054515
 * <p>
 * <p>
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/12
 * Time:16:52
 */
public class CountHandler extends SortHandlerBehavior {

    /**
     * ˼·:
     * һ�������Ͱ���򣬰���ÿ�����������ֵ���з�Ͱ
     * <p>
     * �ҳ��������������������С��Ԫ��
     * ͳ��������ÿ��ֵΪi��Ԫ�س��ֵĴ�������������C�ĵ�i��
     * �����еļ����ۼӣ���C�еĵ�һ��Ԫ�ؿ�ʼ��ÿһ���ǰһ����ӣ�
     * �������Ŀ�����飺��ÿ��Ԫ��i����������ĵ�C(i)�ÿ��һ��Ԫ�ؾͽ�C(i)��ȥ1
     * <p>
     * <p>
     * k�����ֵ����Сֵ�Ĳ�ֵ���ھ��ȷֲ��������У�k��nû�����ԵĲ��
     * <p>
     * ʱ�临�Ӷ�:O(n+k)
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
        Comparable element = originArray[0];
        if (element instanceof Integer) {
        } else {
            throw new Exception("����֧�ֵ��������͡�" + element.getClass());
        }
        //��������е����ֵ����Сֵ
        Comparable[] maxAndMin = maxAndMin(originArray);
        int maxValue = (Integer) maxAndMin[0];
        int minValue = (Integer) maxAndMin[1];
        if (maxValue == minValue) {
            return originArray;
        }
        //������������
        int bucket = maxValue - minValue + 1;
        //System.out.println("bucket��"+bucket);
        int[] countArray = new int[bucket];
        //����������
        for (int i = 0; i < originArray.length; i++) {
            countArray[(Integer) originArray[i] - minValue] += 1;
        }
        //�����������˳�����
        for (int i = 1; i < countArray.length; i++) {
            countArray[i] += countArray[i - 1];
        }
        //�����������
        int[] result = new int[originArray.length];
        for (int i = 0; i < originArray.length; i++) {
            int value = (Integer) originArray[i];
            int index = countArray[value - minValue] - 1;
            result[index] = value;
            countArray[value - minValue] -= 1;
        }
        return freeArray(result);
    }

    private Comparable[] freeArray(int[] sortedArray) {
        Comparable[] result = new Comparable[sortedArray.length];
        for (int i = 0; i < sortedArray.length; i++) {
            result[i] = sortedArray[i];
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        Sortable sortable = new CountHandler();
        Integer f[] = {113, 19, 0, 5, 12, 8, 7, 4, 11, 2, 6, 21};
        //System.out.println("---:" + Arrays.toString(sortable.sort(Util.getRandomIntegerNumberArray(100))));
        //System.out.println("---:" + Arrays.toString(sortable.sort(f)));

        benchmark(sortable, 0, 100000000);//���ʺ϶��޹��ɵ�����������������������У��������ֵ����Сֵ�Ĳ��һ���ڣ���ζ��bucke�ĳ��ȹ��󣩣��Ӷ��ľ��ڴ�
    }
}
