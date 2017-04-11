package ACM.ATypicalHomework;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/4/11
 * Time:18:09
 */
public class Command {
    private int code;
    private Object msg;

    public Command(int code, Object msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public Object getMsg() {
        return msg;
    }
}
