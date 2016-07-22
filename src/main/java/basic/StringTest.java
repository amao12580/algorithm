package basic;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/7/22
 * Time:11:31
 */
public class StringTest {
    public static void main(String[] args) throws IOException {
        String str = "beep boop beer!";
        byte[] buff = str.getBytes();
        for (byte i : buff) {
            String s = byte2bits(i);
            System.out.print(s);
        }
    }

    public static String byte2bits(byte b) {
        int z = b;
        z |= 256;
        String str = Integer.toBinaryString(z);
        int len = str.length();
        return str.substring(len - 8, len);
    }
}
