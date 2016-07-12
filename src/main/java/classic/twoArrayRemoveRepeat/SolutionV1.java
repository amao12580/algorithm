package classic.twoArrayRemoveRepeat;

import basic.Util;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/7/12
 * Time:15:02
 * <p>
 * 在正数数组b中找到不在正数数组a中的值，返回新数组
 * <p>
 * 使用Java 8 Stream API实现
 * <p>
 * 假设b数组的长度为：N,a数组的长度为M
 * <p>
 * 时间复杂度度：N*M
 */
public class SolutionV1 {
    public static void main(String[] args) {
        Integer[] a = {1, 5, 6};
        Integer[] b = {1, 5, 7, 8};
        List<Integer> AList = Arrays.asList(a);
        Object result = Arrays.asList(b).parallelStream().filter(element -> !AList.contains(element)).toArray();
        System.out.println(Util.toJson(result));
    }
}
