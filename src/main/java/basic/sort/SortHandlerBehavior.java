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

    public void swap(Comparable[] array,int firstIndex,int secondIndex){
        swapIfNotEqual(array,firstIndex,array[firstIndex],secondIndex,array[secondIndex]);
    }

    public void swap(Comparable[] array,int firstIndex,Comparable firstValue,int secondIndex){
        swapIfNotEqual(array,firstIndex,firstValue,secondIndex,array[secondIndex]);
    }

    public void swap(Comparable[] array,int firstIndex,int secondIndex,Comparable secondValue){
        swapIfNotEqual(array,firstIndex,array[firstIndex],secondIndex,secondValue);
    }

    public void swap(Comparable[] array,int firstIndex,Comparable firstValue,int secondIndex,Comparable secondValue){
        swapIfNotEqual(array,firstIndex,firstValue,secondIndex,secondValue);
    }

    public void swapIfNotEqual(Comparable[] array,int firstIndex,Comparable firstValue,int secondIndex,Comparable secondValue){
        if(Comparator.isEQUAL(firstValue, secondValue)){
            swapAlways(array, firstIndex, firstValue, secondIndex, secondValue);
        }
    }

    public void swapIfLessThan(Comparable[] array,int firstIndex,Comparable firstValue,int secondIndex,Comparable secondValue){
        if(Comparator.isLT(firstValue, secondValue)){
            swapAlways(array, firstIndex, firstValue, secondIndex, secondValue);
        }
    }

    public void swapIfLessThan(Comparable[] array, int firstIndex, Comparable firstValue, int secondIndex) {
        swapIfLessThan(array, firstIndex, firstValue, secondIndex, array[secondIndex]);
    }

    public void swapAlways(Comparable[] array, int firstIndex, Comparable firstValue, int secondIndex, Comparable secondValue){
        array[firstIndex]=secondValue;
        array[secondIndex]=firstValue;
    }

    public void swapIfLessThan(Comparable[] array,int firstIndex,int secondIndex){
        swapIfLessThan(array, firstIndex, array[firstIndex], secondIndex, array[secondIndex]);
    }

    public void init(Comparable[] originArray) throws IllegalAccessException {
        if(originArray==null){
            throw new IllegalAccessException("不可以接受null参数");
        }
    }

    public void init(Comparable[]... originArray) throws IllegalAccessException {
        for (int i = 0; i < originArray.length; i++) {
            init(originArray[i]);
        }
    }

    /**
     * 求数组的平均值
     *
     * @param array 数组
     * @return 平均值
     */
    public static double avg(double[] array){
        double sum=0;
        for (int i = 0; i < array.length; i++) {
            sum+=array[i];
        }
        return sum/array.length;
    }

    /**
     * 求数组的最小值
     *
     * @param array 数组
     * @return 最小值
     */
    public static double min(double[] array){
        double min=0;
        if(array!=null&&array.length>0){
            min=array[0];
        }
        for (int i = 0; i < array.length; i++) {
            double e=array[i];
            if(min>e){
                min=e;
            }
        }
        return min;
    }

    /**
     * 求数组的最大值
     *
     * @param array 数组
     * @return 最大值
     */
    public static double max(double[] array){
        double max=0;
        for (int i = 0; i < array.length; i++) {
            double e=array[i];
            if(max<e){
                max=e;
            }
        }
        return max;
    }

    /**
     *
     * shuffing(不是排序算法)
     *
     * 目标：RearrangComparable array so that result is a uniformly random permutation
     *
     * 将已经排好序的数组打乱
     *
     * @param array 已排好序的数组打乱
     * @return  乱序的数组
     */
    public Comparable[] rearrange(Comparable[] array){
        for (int i = 0; i < array.length; i++) {
            int randomIndex= Util.getRandomInteger(0, array.length - 1);
            swapAlways(array,i,array[i], randomIndex,array[randomIndex]);
        }
        return array;
    }

    public static void benchmark(Sortable sortable) throws Exception {
        int count=200;
        double[] times=new double[count];
        for (int i = 0; i < count; i++) {
            Integer[] array= Util.getRandomIntegerNumberArray(1000000);
            long st=System.currentTimeMillis();
            sortable.sort(array);
            times[i]=System.currentTimeMillis()-st;
        }
        System.out.println("avg:" + avg(times) + ",max:" + max(times) + ",min:" + min(times));
    }
}
