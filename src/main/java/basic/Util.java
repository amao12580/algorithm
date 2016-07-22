package basic;

import com.google.gson.Gson;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/17
 * Time:14:14
 */
public class Util {

    public static final Integer DEFAULT_ARRAY_LENGTH_MIN = 1;
    public static Integer DEFAULT_ARRAY_LENGTH_MAX;

    private static final Gson json = new Gson();

    public static String toJson(Object obj) {
        return json.toJson(obj);
    }

    public static <T> T fromJson(String jsonString, Class<T> classOfT) {
        return json.fromJson(jsonString, classOfT);
    }

    private static void changeDefaultArrayLengthMax() {
        DEFAULT_ARRAY_LENGTH_MAX = (int) Math.pow(10d, 6d);
    }

    public static Double[] getRandomRealNumberArray() {
        changeDefaultArrayLengthMax();
        return getRandomRealNumberArray(getRandomInteger(DEFAULT_ARRAY_LENGTH_MIN, DEFAULT_ARRAY_LENGTH_MAX));
    }

    public static Double[] getRandomRealNumberArray(int len) {
        Double[] array = new Double[len];
        for (int i = 0; i < array.length; i++) {
            array[i] = getRandomDouble();
        }
        return array;
    }

    public static Integer[] getRandomIntegerNumberArray() {
        changeDefaultArrayLengthMax();
        return getRandomIntegerNumberArray(getRandomInteger(DEFAULT_ARRAY_LENGTH_MIN, DEFAULT_ARRAY_LENGTH_MAX));
    }

    public static Integer[] getRandomIntegerNumberArray(int len) {
        Integer[] array = new Integer[len];
        for (int i = 0; i < array.length; i++) {
            array[i] = getRandomInteger(1, Integer.MAX_VALUE);
        }
        return array;
    }


    public static Integer[] getRandomIntegerNumberArray(int len, int min, int max) {
        Integer[] array = new Integer[len];
        for (int i = 0; i < array.length; i++) {
            array[i] = getRandomInteger(min, max);
        }
        return array;
    }

    public static Double getRandomDouble() {
        return Math.random() * Double.MAX_VALUE;
    }

    public static int getRandomInteger(int min, int max) {
        return (int) (Math.random() * max) + min;
    }

    public static double log(double value, double base) {
        return Math.log(value) / Math.log(base);
    }

    public static double pow(double value, double base) {
        return Math.pow(value, base);
    }


    private static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LETTERCHAR = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERCHAR = "0123456789";

    public static String generateMixedString(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return stringBuffer.toString();
    }

    public static String generateLetterString(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(LETTERCHAR.charAt(random.nextInt(LETTERCHAR.length())));
        }
        return stringBuffer.toString();
    }

    public static String generateNumberString(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(NUMBERCHAR.charAt(random.nextInt(NUMBERCHAR.length())));
        }
        return stringBuffer.toString();
    }


    /**
     * 将字符串转换成二进制字符串
     */
    public static String toBinary(String str) {
        if (str == null) {
            return null;
        }
        byte[] buff = str.getBytes();
        StringBuilder stringBuilder = new StringBuilder();
        for (byte i : buff) {
            stringBuilder.append(byte2bits(i));
        }
        return stringBuilder.toString();
    }

    public static String byte2bits(byte b) {
        int z = b;
        z |= 256;
        String str = Integer.toBinaryString(z);
        int len = str.length();
        return str.substring(len - 8, len);
    }
}
