package basic;

import com.google.gson.Gson;

import java.util.Arrays;
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


    public static long getRandomLong() {
        return (long) (Math.random() * Long.MAX_VALUE);
    }

    /**
     * 返回min与max之间的随机整数值，该值可能等于min，也有可能等于max
     *
     * @param min lower
     * @param max upper
     * @return random
     */
    public static int getRandomInteger(int min, int max) {
        if (max < min) {
            return 0;
        }
        if (max == min) {
            return max;
        }
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    /**
     * 随机返回一个true或false
     */
    public static boolean getRandomBoolean() {
        return getRandomInteger(1, 1000) > 500;
    }

    public static double log(double value, double base) {
        return Math.log(value) / Math.log(base);
    }

    public static double pow(double value, double base) {
        return Math.pow(value, base);
    }


    private static final String LETTERCHAR = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERCHAR = "0123456789";
    private static final String ALLCHAR = LETTERCHAR + NUMBERCHAR;

    public static String generateMixedString() {
        return generateMixedString(10);
    }

    public static String generateMixedString(int length) {
        StringBuilder stringBuffer = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return stringBuffer.toString();
    }

    public static String generateLetterString() {
        return generateLetterString(10);
    }

    public static String generateLetterString(int length) {
        StringBuilder stringBuffer = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(LETTERCHAR.charAt(random.nextInt(LETTERCHAR.length())));
        }
        return stringBuffer.toString();
    }

    public static String generateNumberString(int length) {
        StringBuilder stringBuffer = new StringBuilder();
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


    /**
     * 在给定数组的begin、end下标值范围内(包含两个临界值)，找到最后一个比key小的元素下标值
     */
    public static int binarySearchLatestLessThan(int[] originArray, int beginIndex, int endIndex, int key) {
        if (beginIndex > endIndex) {
            return endIndex + 1;
        }
        if (originArray[endIndex] < key) {
            return endIndex;
        }
        int middleIndex = beginIndex + ((endIndex - beginIndex) / 2);
        if (originArray[middleIndex] < key) {
            int previousIndex = middleIndex + 1;
            if (previousIndex > endIndex) {
                return middleIndex;
            }
            //判断下一个值是否符合要求
            if (originArray[previousIndex] >= key) {
                return middleIndex;
            } else {
                return binarySearchLatestLessThan(originArray, previousIndex, endIndex, key);
            }
        } else {
            if (middleIndex == 0) {
                return -1;
            }
            return binarySearchLatestLessThan(originArray, beginIndex, middleIndex, key);
        }
    }

    /**
     * 打乱数组
     */
    public static void shuffleArray(Object[] arrays) {
        int endIndex = arrays.length - 1;
        for (int i = 0; i <= endIndex; i++) {
            if (swapArray(arrays, i, getRandomInteger(0, endIndex))) {
                swapArray(arrays, i, getRandomInteger(0, endIndex));
            }
        }
    }

    /**
     * 数组指定两个下标值进行交换，交换成功后返回true
     */
    public static boolean swapArray(Object[] arrays, int thisIndex, int otherIndex) {
        int endIndex = arrays.length - 1;
        if (thisIndex >= 0 && thisIndex <= endIndex && otherIndex >= 0 && otherIndex <= endIndex && thisIndex != otherIndex) {
            Object temp = arrays[thisIndex];
            arrays[thisIndex] = arrays[otherIndex];
            arrays[otherIndex] = temp;
            return true;
        }
        return false;
    }

    /**
     * 按照字典序，升序排列
     * <p>
     * 具体字符比较大小，请参见ASCII码对照表，取10进制整数值
     * <p>
     * https://zh.wikipedia.org/wiki/ASCII
     */
    public static String[] orderByDictionaryASC(String[] strings) {
        if (strings == null) {
            return null;
        }
        int len = strings.length;
        if (len == 1) {
            return strings;
        }
        DictionaryString[] dictionaryStrings = new DictionaryString[len];
        for (int i = 0; i < len; i++) {
            dictionaryStrings[i] = new DictionaryString(strings[i]);
        }
        Arrays.sort(dictionaryStrings);
        String[] result = new String[len];
        for (int i = 0; i < len; i++) {
            result[i] = dictionaryStrings[i].getString();
        }
        return result;
    }

    private static class DictionaryString implements Comparable<DictionaryString> {
        private String string;

        public DictionaryString(String string) {
            this.string = string;
        }

        public String getString() {
            return string;
        }

        @Override
        public int compareTo(DictionaryString other) {
            if (other == null) {
                throw new IllegalArgumentException();
            }
            String thisString = this.getString();
            String otherString = other.getString();
            int myLen = thisString.length();
            int otherLen = otherString.length();
            int len = myLen > otherLen ? otherLen : myLen;
            for (int i = 0; i < len; i++) {
                int c = thisString.charAt(i) - otherString.charAt(i);
                if (c > 0) {
                    return 1;
                }
                if (c < 0) {
                    return -1;
                }
            }
            if (myLen > otherLen) {
                return 1;
            }
            if (otherLen > myLen) {
                return -1;
            }
            return 0;
        }
    }

    public static final char[] seeds_big_chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    public static final int seedsBigCharsEndIndex = seeds_big_chars.length - 1;
}
