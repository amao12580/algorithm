package sort.insertion;

import basic.sort.SortHandlerBehavior;
import basic.sort.Sortable;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/12
 * Time:16:52
 */
public class InsertionHandler<E extends Comparable> extends SortHandlerBehavior<E> {

    @Override
    public E[] sort(E[]... originArray) throws Exception {

        return null;
    }

    /**
     *  ˼·��
     * �ڵ�i�ε����У�����i��Ԫ����ÿһ��������ұ�����ĵ�Ԫ�ؽ���λ��
     *
     *
     * ʱ�临�Ӷȣ�O
     *
     *
     *
     * @param originArray  ԭʼ��������
     * @return �ź��������
     * @throws Exception  ������������ɽ���ʱ�׳�
     */
    @Override
    public E[] sort(E[] originArray) throws Exception {
        init(originArray);
        int len=originArray.length;
        if(len==1){
            return originArray;
        }
        if(len==2){
            swapIfLessThan(originArray,1,0);
            return originArray;
        }
        for (int i = 0; i < len; i++) {
            int endIndex=i-1;
            if(endIndex<0){
                continue;
            }
            for (int j = 0; j <= endIndex; j++) {
                swapIfLessThan(originArray,i,j);
                System.out.println("***:" + Arrays.toString(originArray));
            }
            System.out.println("###:"+Arrays.toString(originArray));
        }
        return originArray;
    }

    public static void main(String[] args) throws Exception {
        Sortable<Integer> sortable=new InsertionHandler<>();
        Integer f[]={5,0,8,58,10,2,3,7,8,9,10,12,15,-5,4,152};
        System.out.println("---:"+Arrays.toString(sortable.sort(f)));
    }

}
