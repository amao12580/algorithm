package sort.merge;

import basic.Comparator;
import basic.Util;
import basic.sort.SortHandlerBehavior;
import basic.sort.Sortable;
import sort.insertion.InsertionHandler;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/5
 * Time:11:10
 */
public class MergeHandler extends SortHandlerBehavior {

    private static final int DEFAULT_SPLIT_HALVE_SIZE = 2;

    private static final int DEFAULT_SPLIT_PIECE_ARRAY_LENGTH_MIN = (int) Util.pow(DEFAULT_SPLIT_HALVE_SIZE, 6);

    /**
     * merge sort(�鲢����)
     * <p>
     * ˼·��
     * Divide array into two halves.
     * Recursively sort each half.
     * Merge two halves.
     * <p>
     * <p>
     * Abstract in-place merge(ԭ�ع鲢�ĳ��󷽷�)
     * Given two sorted subarrays a[lo] to a[mid] and a[mid+1] to a[hi],replace with sorted subarray a[lo] to a[hi]
     * <p>
     * <p>
     * ���裺
     * �Ƚ�����Ԫ�ظ��Ƶ�aux[]�У��ٹ鲢��a[]�С�
     * �鲢ʱ���ĸ��жϣ�
     * �����þ�(ȡ�Ұ��Ԫ��)
     * �Ұ���þ�(ȡ����Ԫ��)
     * �Ұ�ߵĵ�ǰԪ��С�����ߵĵ�ǰԪ��(ȡ�Ұ�ߵ�Ԫ��)
     * �Ұ�ߵĵ�ǰԪ�ش���/�������ߵĵ�ǰԪ��(ȡ���ߵ�Ԫ��)
     *
     * @param originArray ���������
     * @return ���ź��������
     * @throws Exception
     */
    @Override
    public Comparable[] sort(Comparable[] originArray) throws Exception {
        init(originArray);
        if (originArray.length < DEFAULT_SPLIT_PIECE_ARRAY_LENGTH_MIN) {
            return sortable.sort(originArray);
        }
        //1.���ȷݲ��Ϊ����ȳ�����
        Comparable[][] splitedArray = split(originArray);
        //2.ÿ�ȷ����飬ʹ�ò��������㷨��������
        for (int i = 0; i < splitedArray.length; i++) {
            splitedArray[i] = sortable.sort(splitedArray[i]);
        }
        //3.�ϲ�������
        int index = 0;
        for (int i = 0; i < splitedArray.length; i += DEFAULT_SPLIT_HALVE_SIZE) {
            Comparable[] mergredArray;
            if (i + 1 < splitedArray.length) {
                mergredArray = merge(splitedArray[i], splitedArray[i + 1]);
            } else {
                mergredArray = splitedArray[i];
            }
            addAll(originArray, index, mergredArray);
            index += mergredArray.length;
        }
        return originArray;
    }

    //2.ÿ�ȷ����飬ʹ�ò��������㷨��������
    private SortHandlerBehavior sortable = null;

    public MergeHandler(SortHandlerBehavior sortable) {
        this.sortable = sortable;
    }

    private void addAll(Comparable[] originArray, int index, Comparable[] pieceArray) {
        if (originArray == null || pieceArray == null) {
            return;
        }
        int j = 0;
        for (int i = index; i < (index + pieceArray.length); i++) {
            originArray[i] = pieceArray[j];
            j++;
        }
    }


    public Comparable[][] split(Comparable[] originArray) throws Exception {
        int originArrayLength = originArray.length;
        int pieceNum = originArrayLength / DEFAULT_SPLIT_PIECE_ARRAY_LENGTH_MIN;
        if (pieceNum * DEFAULT_SPLIT_PIECE_ARRAY_LENGTH_MIN < originArrayLength) {
            pieceNum++;
        }

        Comparable[][] result = new Comparable[pieceNum][];
        for (int i = 0; i < originArrayLength; i++) {
            int slot = i / DEFAULT_SPLIT_PIECE_ARRAY_LENGTH_MIN;
            int position = i % DEFAULT_SPLIT_PIECE_ARRAY_LENGTH_MIN;
            if (result[slot] == null) {
                if ((slot + 1) < pieceNum) {
                    result[slot] = new Comparable[DEFAULT_SPLIT_PIECE_ARRAY_LENGTH_MIN];
                } else {
                    result[slot] = new Comparable[originArrayLength - ((pieceNum - 1) * DEFAULT_SPLIT_PIECE_ARRAY_LENGTH_MIN)];
                }
            }
            //System.out.println("index:"+index+",slot:"+slot+",position:"+position+",result[slot].length:"+result[slot].length);
            result[slot][position] = originArray[i];
        }
        return result;
    }

    /**
     * http://blog.jobbole.com/100349/
     * <p>
     * �������Ѿ��ź��������ϲ���һ���������顣
     * <p>
     * �ź�����ָ��ֵС��Ԫ�������±�ֵ��С
     * <p>
     * ʱ�临�Ӷ�Ϊ On nΪl���������Ԫ���ܸ���
     *
     * @param leftArray  ��һ���ź��������
     * @param rightArray �ڶ����ź��������
     * @return �ϲ���ɵ���������
     */
    private Comparable[] merge(Comparable[] leftArray, Comparable[] rightArray) {
        int leftLen = leftArray.length;
        int rightLen = rightArray.length;
        int total = leftLen + rightLen;
        Comparable[] result = new Comparable[total];
        int fi = 0, si = 0, ri = 0;
        while (ri < total) {
            if (fi > leftLen - 1) {
                result[ri] = rightArray[si];
                si++;
                ri++;
                continue;
            }
            if (si > rightLen - 1) {
                result[ri] = leftArray[fi];
                fi++;
                ri++;
                continue;
            }
            Comparable f = leftArray[fi];
            Comparable s = rightArray[si];
            if (Comparator.isLTE(f, s)) {
                result[ri] = f;
                fi++;
            } else {
                result[ri] = s;
                si++;
            }
            ri++;
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        Sortable sortable = new MergeHandler(new InsertionHandler());
        benchmark(sortable);
    }
}
