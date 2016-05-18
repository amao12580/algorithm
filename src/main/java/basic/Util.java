package basic;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/17
 * Time:14:14
 */
public class Util {

    public static final Integer DEFAULT_ARRAY_LENGTH_MIN=1;
    public static Integer DEFAULT_ARRAY_LENGTH_MAX;

    private static void changeDefaultArrayLengthMax() {
        DEFAULT_ARRAY_LENGTH_MAX=(int)Math.pow(10d,6d);
    }

    /**
     * 获取随机实数构成的数组.
     *
     * 数组元素的大小在0到Double最大值之间的任意数值
     *
     * 数组长度在 DEFAULT_ARRAY_LENGTH_MIN  至  DEFAULT_ARRAY_LENGTH_MAX 之间随机
     */
    public static Double[] getRandomRealNumberArray(){
        changeDefaultArrayLengthMax();
        return getRandomRealNumberArray(getRandomInteger(DEFAULT_ARRAY_LENGTH_MIN,DEFAULT_ARRAY_LENGTH_MAX));
    }
    public static Double[] getRandomRealNumberArray(int len){
        Double[] array=new Double[len];
        for (int i = 0; i <array.length; i++) {
            array[i]=getRandomDouble();
        }
        return array;
    }

    /**
     * 获取随机正整数构成的数组.
     *
     * 数组元素的大小在0到Integer最大值之间的任意数值
     *
     * 数组长度在 DEFAULT_ARRAY_LENGTH_MIN  至  DEFAULT_ARRAY_LENGTH_MAX 之间随机
     */
    public static Integer[] getRandomIntegerNumberArray(){
        changeDefaultArrayLengthMax();
        return getRandomIntegerNumberArray(getRandomInteger(DEFAULT_ARRAY_LENGTH_MIN,DEFAULT_ARRAY_LENGTH_MAX));
    }

    public static Integer[] getRandomIntegerNumberArray(int len){
        Integer[] array=new Integer[len];
        for (int i = 0; i <array.length; i++) {
            array[i]=getRandomInteger(1, Integer.MAX_VALUE);
        }
        return array;
    }

    public static Double getRandomDouble() {
        return Math.random()*Double.MAX_VALUE;
    }

    public static int getRandomInteger(int min,int max) {
        return (int)(Math.random()*max) + min;
    }

    //求对数
    public static double log(double value, double base) {
        return Math.log(value) / Math.log(base);
    }

    //求指数
    public static double pow(double value, double base) {
        return Math.pow(value,base);
    }



    public static void main(String[] args) {
        System.out.println((int)Math.pow(10d,6d));
        System.out.println(Arrays.toString(Util.getRandomRealNumberArray(1000)));
        System.out.println(Arrays.toString(Util.getRandomIntegerNumberArray(1000)));
    }
}
