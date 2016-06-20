package sort.bucket;

import basic.Util;
import basic.sort.SortHandlerBehavior;
import basic.sort.Sortable;
import sort.count.CountHandler;

import java.util.Arrays;

/**
 * https://segmentfault.com/a/1190000003054515
 * <p>
 * <p>
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/12
 * Time:16:52
 */
public class BucketHandler extends SortHandlerBehavior {

    //�������壺ÿ��Ͱ����Сsize
    private static final int MIN_BUCKET_SIZE = 64;

    /**
     * Ͱ�����Ǽ�������ı��֣��Ѽ������������ڵ�m��"СͰ"�ŵ�һ��"��Ͱ"�У��ڷ���Ͱ�󣬶�ÿ��Ͱ��������һ���ÿ��ţ���Ȼ��ϲ������Ľ����
     * <p>
     * ����˼�룺
     * <p>
     * Ͱ�������������һ��������̲������ù��̽�Ԫ�ؾ��ȶ������طֲ�������[0,1)�ϡ����ǰ�����[0,1)���ֳ�n����ͬ��С�������䣬��ΪͰ����n����¼�ֲ�������Ͱ��ȥ��
     * ����ж���һ����¼�ֵ�ͬһ��Ͱ�У���Ҫ����Ͱ������������ΰѸ���Ͱ�еļ�¼�г����ǵõ��������С�
     * <p>
     * <p>
     * Ч�ʷ�����
     * <p>
     * Ͱ�����ƽ��ʱ�临�Ӷ�Ϊ���Ե�O(N+C)������CΪͰ�ڿ��ŵ�ʱ�临�Ӷȡ���������ͬ����N��Ͱ����MԽ����Ч��Խ�ߣ���õ�ʱ�临�ӶȴﵽO(N)��
     * ��ȻͰ����Ŀռ临�Ӷ� ΪO(N+M)������������ݷǳ��Ӵ󣬶�Ͱ������Ҳ�ǳ��࣬��ռ���������ǰ���ġ����⣬Ͱ�������ȶ��ġ�
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
        int length = maxValue - minValue;
        if (length == 0) {
            return originArray;
        }

        //����Ͱ�ĸ���
        int bucketCount = length / MIN_BUCKET_SIZE;
        if (bucketCount < (length * MIN_BUCKET_SIZE)) {
            bucketCount++;
        }
        //System.out.println("bucketCount��" + bucketCount);
        int[][] bucketArray = new int[bucketCount][MIN_BUCKET_SIZE];

        //���������飬��������ÿ��Ͱ�����Ԫ�ص��±�ֵ
        int[] buckeCountArray = new int[bucketCount];
        //���Ͱ
        for (Comparable anOriginArray : originArray) {
            int value = (Integer) anOriginArray;
            int bucketIndex = getBucketIndex(value, minValue, bucketCount);
            int subBucketIndex = buckeCountArray[bucketIndex];
            if (subBucketIndex > MIN_BUCKET_SIZE - 1) {
                resize(bucketArray, bucketIndex);//Ԥ�����СͰ��������������һ��resize������+=MIN_BUCKET_SIZE
            }
            bucketArray[bucketIndex][buckeCountArray[bucketIndex]] = value;
            buckeCountArray[bucketIndex] += 1;
        }
        //��СͰ�������������

        Sortable sortable = new CountHandler();
        for (int i = 0; i < bucketArray.length; i++) {
            int count = buckeCountArray[i];
            if (count - 1 > 0) {
                bucketArray[i] = freeArray(sortable.sort(getPartArray(bucketArray[i], count)));
            }
        }
        //���Թ����������
        int index = 0;
        for (int i = 0; i < bucketArray.length; i++) {
            for (int j = 0; j < buckeCountArray[i]; j++) {
                originArray[index] = bucketArray[i][j];
                index++;
            }
        }
        return originArray;
    }

    private void resize(int[][] bucketArray, int bucketIndex) {
        int[] originBucketArrays = bucketArray[bucketIndex];
        int[] newBucketArrays = new int[originBucketArrays.length + MIN_BUCKET_SIZE];
        for (int i = 0; i < originBucketArrays.length; i++) {
            int value = originBucketArrays[i];
            if (value != 0) {
                newBucketArrays[i] = value;
            }
        }
        bucketArray[bucketIndex] = newBucketArrays;
    }

    private int[] freeArray(Comparable[] array) {
        int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = Integer.valueOf(array[i].toString());
        }
        return result;
    }

    private Comparable[] getPartArray(int[] array, int endIndex) {
        Comparable[] result = new Comparable[endIndex];
        for (int i = 0; i < endIndex; i++) {
            result[i] = array[i];
        }
        return result;
    }

    private int getBucketIndex(int value, int minValue, int bucketCount) throws Exception {
        int index = 0;
        while (index < bucketCount) {
            int st = index * MIN_BUCKET_SIZE + minValue;
            if (st <= value && value < (st + MIN_BUCKET_SIZE)) {
                return index;
            } else {
                index++;
            }
        }
        throw new Exception("Ԫ��ֵ��" + value + "�޷����䵽Ͱ");
    }

    public static void main(String[] args) throws Exception {
        Sortable sortable = new BucketHandler();
        Integer f[] = {113, 19, 0, 5, 12, 8, 7, 4, 11, 2, 6, 21};
        System.out.println("---:" + Arrays.toString(sortable.sort(Util.getRandomIntegerNumberArray(1000000, 0, 100))));
        //System.out.println("---:" + Arrays.toString(sortable.sort(f)));
        //benchmark(sortable, 0, 10000);
    }
}
