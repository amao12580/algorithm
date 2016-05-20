package sort.quick;

import basic.Comparator;
import basic.sort.SortHandlerBehavior;
import basic.sort.Sortable;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/12
 * Time:16:52
 */
public class QuickHandler extends SortHandlerBehavior {

    /**
     *  ˼·:
     *
     * ��������ʱ���ڷ���ģʽ�����,
     * ��һ������������A[p...r]����ķ��ι���Ϊ��������:
     * 1.�ֽ�:
     * A[p..r]������Ϊ���������ܿգ���������A[p ..q-1]��A[q+1 ..r],ʹ��
     * A[p ..q-1] <= A[q] <= A[q+1 ..r]
     * 2.���:ͨ���ݹ���ÿ�������,��������A[p ..q-1]��A[q+1 ..r]����
     * 3.�ϲ���
     *
     *
     * ʱ�临�Ӷ�:O(n*log(n))
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
            swapIfLessThan(originArray, 1, 0);
            return originArray;
        }
        sort(originArray,0,originArray.length-1);
        return originArray;
    }
    public void sort(Comparable[] originArray,final int beginIndex,final int endIndex) throws Exception {
        if((endIndex-beginIndex)<=1){
            return;
        }

        int N=partition(originArray,beginIndex,endIndex);
        //System.out.println("N:" + N);
        if((N-1)>=beginIndex){
            //System.out.println("N2:" + (N-1));
            sort(originArray, beginIndex, N - 1);
        }
        if((N+1)<=endIndex){
            //System.out.println("N3:" + (N+1));
            sort(originArray,N+1,endIndex);
        }
    }

    /**
     *
     * ������Ԫ�ؽ����ƶ����з�����Ϊ�����֣�ʹ��벿��<=�Ұ벿�֡�
     *
     * ���������ֵķָ�Ԫ�أ��±�ֵ��Ϊ��N
     *
     * @param originArray ԭʼ����
     * @param beginIndex �з��������ʼԪ���±�
     * @param endIndex �з�����Ľ���Ԫ���±�
     * @return N
     * @throws Exception
     */

    public int partition (Comparable[] originArray ,final int beginIndex,final int endIndex) throws Exception {
        //System.out.println("begin:"+beginIndex+",end:"+endIndex);
        int basicIndex=beginIndex;
        int leftIndex=basicIndex+1;
        int rightIndex=endIndex;
        while(rightIndex>=leftIndex){
            while(leftIndex<=endIndex && rightIndex>=leftIndex){
                if(Comparator.isLT(originArray[leftIndex], originArray[basicIndex])){
                    swapAlways(originArray, leftIndex, originArray[leftIndex], basicIndex, originArray[basicIndex]);
                    basicIndex=leftIndex;
                    leftIndex=basicIndex+1;
                }else{
                    break;
                }
            }
            while(beginIndex<=rightIndex && rightIndex>=leftIndex){
                if(Comparator.isLT(originArray[rightIndex], originArray[basicIndex])){
                    swapAlways(originArray, rightIndex, originArray[rightIndex], leftIndex, originArray[leftIndex]);
                    break;
                }else{
                    rightIndex--;
                }
            }
        }
        //System.out.println("---:"+ Arrays.toString(originArray));
        return basicIndex;
    }




    public static void main(String[] args) throws Exception {
        Sortable sortable=new QuickHandler();
        Integer f[]={13,19,9,5,12,8,7,4,11,2,6,21};
        Integer f2[]={59,-10,0,6,-1,0,8,58,10,2,3,7,8,9,10,12,15,-5,4};
        System.out.println("---:"+ Arrays.toString(sortable.sort(f)));
        System.out.println("---:" + Arrays.toString(sortable.sort(f2)));
        benchmark(sortable);
    }

}
