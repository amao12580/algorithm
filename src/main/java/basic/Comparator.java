package basic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/5
 * Time:11:45
 */
public enum Comparator {
    GT(1, "greater than"),//大于
    LT(-1, "less than"),//小于
    EQUAL(0, "equal");//相等

    Integer code;
    String desc;

    static Map<Integer, Comparator> meta;

    Comparator(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    static {
        init();
    }

    private static void init() {
        if (meta == null) {
            meta = new HashMap<>();
            Comparator[] arr = Comparator.values();
            for (int i = 0; i < arr.length; i++) {
                Comparator obj = arr[i];
                meta.put(obj.getCode(), obj);
            }
            //System.out.println("meta:"+meta.toString());
        }
    }

    private static Comparator get(Integer code) {
        init();
        return meta.get(code);
    }

    private Integer getCode() {
        return this.code;
    }

    /**
     * 是否是小于等于？
     *
     * @param result 比较完成以后的结果
     * @return 检查结果
     */
    public static boolean isLTE(Comparator result) {
        if (result.code == Comparator.LT.code || result.code == Comparator.EQUAL.code) {
            return true;
        }
        return false;
    }

    public static boolean isLTE(Comparable one, Comparable other) {
        return isLTE(compare(one, other));
    }

    public static boolean isGT(Comparator result) {
        if (result.code == Comparator.GT.code) {
            return true;
        }
        return false;
    }

    public static boolean isGT(Comparable one, Comparable other) {
        return isGT(compare(one, other));
    }

    public static boolean isLT(Comparator result) {
        if (result.code == Comparator.LT.code) {
            return true;
        }
        return false;
    }

    public static boolean isLT(Comparable one, Comparable other) {
        return isLT(compare(one, other));
    }

    public static boolean isEQUAL(Comparable one, Comparable other) {
        return isEQUAL(compare(one, other));
    }

    public static boolean isEQUAL(Comparator result) {
        if (result.code == Comparator.EQUAL.code) {
            return true;
        }
        return false;
    }

    public static Comparator compare(Comparable one, Comparable other) {
        if(one instanceof String && other instanceof String){
            one=Integer.valueOf(one.toString());
            other=Integer.valueOf(other.toString());
        }
        return get(one.compareTo(other));
    }
}
