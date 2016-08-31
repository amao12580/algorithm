package basic.sort;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/13
 * Time:12:15
 */
public interface Sortable{
    Comparable[] sort(Comparable [] originArray) throws Exception;

    /**
     *
     * shuffing(不是排序算法)
     *
     * 目标：Rearrange array so that result is a uniformly random permutation
     *
     * 将已经排好序的数组打乱
     *
     * @param array 已排好序的数组打乱
     * @return  乱序的数组
     */
    Comparable[] rearrange(Comparable[] array);
}
