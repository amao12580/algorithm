package basic.sort;

import basic.Comparator;
import basic.Util;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/13
 * Time:14:45
 */
public abstract class SortHandlerBehavior implements Sortable {

    public void swap(Comparable[] array, int firstIndex, int secondIndex) {
        swapIfNotEqual(array, firstIndex, array[firstIndex], secondIndex, array[secondIndex]);
    }

    public void swap(Comparable[] array, int firstIndex, Comparable firstValue, int secondIndex) {
        swapIfNotEqual(array, firstIndex, firstValue, secondIndex, array[secondIndex]);
    }

    public void swap(Comparable[] array, int firstIndex, int secondIndex, Comparable secondValue) {
        swapIfNotEqual(array, firstIndex, array[firstIndex], secondIndex, secondValue);
    }

    public void swap(Comparable[] array, int firstIndex, Comparable firstValue, int secondIndex, Comparable secondValue) {
        swapIfNotEqual(array, firstIndex, firstValue, secondIndex, secondValue);
    }

    public void swapIfNotEqual(Comparable[] array, int firstIndex, Comparable firstValue, int secondIndex, Comparable secondValue) {
        if (Comparator.isEQUAL(firstValue, secondValue)) {
            swapAlways(array, firstIndex, firstValue, secondIndex, secondValue);
        }
    }

    public void swapIfLessThan(Comparable[] array, int firstIndex, Comparable firstValue, int secondIndex, Comparable secondValue) {
        if (Comparator.isLT(firstValue, secondValue)) {
            swapAlways(array, firstIndex, firstValue, secondIndex, secondValue);
        }
    }

    public void swapIfTrue(Comparable[] array, int firstIndex, int secondIndex, boolean condition) {
        if (condition) {
            swapAlways(array, firstIndex, array[firstIndex], secondIndex, array[secondIndex]);
        }
    }

    public void swapIfLessThan(Comparable[] array, int firstIndex, Comparable firstValue, int secondIndex) {
        swapIfLessThan(array, firstIndex, firstValue, secondIndex, array[secondIndex]);
    }

    public void swapAlways(Comparable[] array, int firstIndex, Comparable firstValue, int secondIndex, Comparable secondValue) {
        array[firstIndex] = secondValue;
        array[secondIndex] = firstValue;
    }

    public void swapIfLessThan(Comparable[] array, int firstIndex, int secondIndex) {
        swapIfLessThan(array, firstIndex, array[firstIndex], secondIndex, array[secondIndex]);
    }

    public void init(Comparable[] originArray) throws IllegalAccessException {
        if (originArray == null) {
            throw new IllegalAccessException("�����Խ���null����");
        }
    }

    public void init(Comparable[]... originArray) throws IllegalAccessException {
        for (int i = 0; i < originArray.length; i++) {
            init(originArray[i]);
        }
    }

    /**
     * �������ƽ��ֵ
     *
     * @param array ����
     * @return ƽ��ֵ
     */
    public static double avg(double[] array) {
        double sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        return sum / array.length;
    }

    /**
     * ���������Сֵ
     *
     * @param array ����
     * @return ��Сֵ
     */
    public static double min(double[] array) {
        double min = 0;
        if (array != null && array.length > 0) {
            min = array[0];
        }
        for (int i = 0; i < array.length; i++) {
            double e = array[i];
            if (min > e) {
                min = e;
            }
        }
        return min;
    }

    public static Comparable min(Comparable[] array) {
        Comparable min = null;
        if (array != null && array.length > 0) {
            min = array[0];
        }
        for (int i = 0; i < array.length; i++) {
            Comparable e = array[i];
            if (Comparator.isGT(min, e)) {
                min = e;
            }
        }
        return min;
    }

    /**
     * ����������ֵ
     *
     * @param array ����
     * @return ���ֵ
     */
    public static double max(double[] array) {
        double max = array[0];
        for (int i = 1; i < array.length; i++) {
            double e = array[i];
            if (max < e) {
                max = e;
            }
        }
        return max;
    }

    public static Comparable max(Comparable[] array) {
        Comparable max = array[0];
        for (int i = 1; i < array.length; i++) {
            Comparable e = array[i];
            if (Comparator.isLT(max, e)) {
                max = e;
            }
        }
        return max;
    }

    public static Comparable[] maxAndMin(Comparable[] array) {
        Comparable max = array[0];
        Comparable min = array[0];
        for (int i = 1; i < array.length; i++) {
            Comparable e = array[i];
            if (Comparator.isLT(max, e)) {
                max = e;
            }
            if (Comparator.isGT(min, e)) {
                min = e;
            }
        }
        Comparable[] result=new Comparable[2];
        result[0]=max;
        result[1]=min;
        return result;
    }

    /**
     * shuffing(���������㷨)
     * <p>
     * Ŀ�꣺RearrangComparable array so that result is a uniformly random permutation
     * <p>
     * ���Ѿ��ź�����������
     *
     * @param array ���ź�����������
     * @return ���������
     */
    public Comparable[] rearrange(Comparable[] array) {
        for (int i = 0; i < array.length; i++) {
            int randomIndex = Util.getRandomInteger(0, array.length - 1);
            swapAlways(array, i, array[i], randomIndex, array[randomIndex]);
        }
        return array;
    }

    public static void benchmark(Sortable sortable) throws Exception {
        benchmark(sortable, null, null);
    }

    /**
     * @param sortable
     * @param min      ��������Ԫ����Сֵ
     * @param max      ��������Ԫ����d��ֵ
     */
    public static void benchmark(Sortable sortable, Integer min, Integer max) throws Exception {
        int count = 50;
        double[] times = new double[count];
        for (int i = 0; i < count; i++) {
            Integer[] array;
            if (min != null && max != null) {
                array = Util.getRandomIntegerNumberArray(1000000, min, max);
            } else {
                array = Util.getRandomIntegerNumberArray(1000000);
            }
            long st = System.currentTimeMillis();
            sortable.sort(array);
            if (count % (i + 1) == 0) {
                System.out.println(((i * 100) / count) + "%");
            }
            times[i] = System.currentTimeMillis() - st;
        }
        System.out.println("avg:" + avg(times) + ",max:" + max(times) + ",min:" + min(times));
    }
}
