package sort.quick;

import basic.sort.SortHandlerBehavior;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/12
 * Time:16:52
 */
public class QuickHandler extends SortHandlerBehavior {

    /**
     *  ˼·��
     *
     * ��������ʱ���ڷ���ģʽ����ģ�
     * ��һ������������A[p...r]����ķ��ι���Ϊ�������裺
     * 1.�ֽ⣺
     * A[p..r]������Ϊ���������ܿգ���������A[p ..q-1]��A[q+1 ..r]��ʹ��
     * A[p ..q-1] <= A[q] <= A[q+1 ..r]
     * 2.�����ͨ���ݹ���ÿ������򣬶�������A[p ..q-1]��A[q+1 ..r]����
     * 3.�ϲ���
     *
     *
     * ʱ�临�Ӷȣ�O(n*log(n))
     *
     *
     *
     * @param originArray  ԭʼ��������
     * @return �ź��������
     * @throws Exception  ������������ɽ���ʱ�׳�
     */
    @Override
    public Comparable[] sort(Comparable[] originArray) throws Exception {
        init(originArray);
        int len=originArray.length;
        if(len==1){
            return originArray;
        }
        if(len==2){
            swapIfLessThan(originArray,1,0);
            return originArray;
        }

        return originArray;
    }

    public static void main(String[] args) throws Exception {

    }

}
