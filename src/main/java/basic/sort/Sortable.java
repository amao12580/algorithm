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
     * shuffing(���������㷨)
     *
     * Ŀ�꣺Rearrange array so that result is a uniformly random permutation
     *
     * ���Ѿ��ź�����������
     *
     * @param array ���ź�����������
     * @return  ���������
     */
    Comparable[] rearrange(Comparable[] array);
}
