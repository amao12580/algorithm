package basic.sort;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/13
 * Time:12:15
 */
public interface Sortable <E extends Comparable>{
    E[] sort(E []... originArray) throws Exception;
    E[] sort(E [] originArray) throws Exception;
}
