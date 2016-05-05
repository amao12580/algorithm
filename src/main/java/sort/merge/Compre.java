package sort.merge;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/5
 * Time:11:45
 */
public enum Compre {
    GT(1,"greater than"),//大于
    LT(2,"less than"),//小于
    EQUAL(0,"equal");//相等

    int code;
    String desc;
    Compre(int code, String desc){
        this.code=code;
        this.desc=desc;
    }

    /**
     * 是否是小于等于？
     *
     * @param result 比较完成以后的结果
     * @return 检查结果
     */
    public static boolean isLTE(Compre result){
        if(result.code== Compre.LT.code||result.code== Compre.EQUAL.code){
            return true;
        }
        return false;
    }

    public static Compre compare(int one,int other){
        if(one>other){
            return Compre.GT;
        }
        if(one<other){
            return Compre.LT;
        }
        return Compre.EQUAL;
    }
}
