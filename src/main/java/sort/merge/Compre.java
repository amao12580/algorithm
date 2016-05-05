package sort.merge;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/5
 * Time:11:45
 */
public enum Compre {
    GT(1,"greater than"),//����
    LT(2,"less than"),//С��
    EQUAL(0,"equal");//���

    int code;
    String desc;
    Compre(int code, String desc){
        this.code=code;
        this.desc=desc;
    }

    /**
     * �Ƿ���С�ڵ��ڣ�
     *
     * @param result �Ƚ�����Ժ�Ľ��
     * @return �����
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
