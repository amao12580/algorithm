package sort.merge;

import basic.Comparator;
import basic.sort.Sortable;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/5
 * Time:11:10
 */
public class MergeHandler implements Sortable<Integer> {

    /**
     * �Զ��������������������ϲ�
     *
     * @param sortedArrays �������
     * @return ����ĺϼ�����
     * @throws IllegalAccessException
     */
    @Override
    public Integer[] sort(Integer[] ... sortedArrays) throws IllegalAccessException {
        if(sortedArrays==null){
            throw new IllegalAccessException("�����Խ���null����");
        }
        if(sortedArrays.length<2){
            return sortedArrays[0];
        }
        //�����з�Ϊÿ����Ϊһ�飬ÿ��ʹ��callable���м���
        for (int i = 0; i < sortedArrays.length; i++) {
            if((i+1)<sortedArrays.length){

            }
        }
        return null;
    }

    @Override
    public Integer[] sort(Integer[] originArray) throws Exception {
        return new Integer[0];
    }

    /**
     *
     * �������Ѿ��ź��������ϲ���һ���������顣
     *
     * �ź�����ָ��ֵС��Ԫ�������±�ֵ��С
     *
     * ʱ�临�Ӷ�Ϊ On nΪl���������Ԫ���ܸ���
     *
     * @param leftArray  ��һ���ź��������
     * @param rightArray  �ڶ����ź��������
     * @return �ϲ���ɵ���������
     */
    private static int[] merge(int[] leftArray,int[] rightArray){
        int leftLen=leftArray.length;
        int rightLen=rightArray.length;
        int total=leftLen+rightLen;
        int[] result= new int[total];
        int fi=0,si=0,ri=0;
        while (ri<total){
            //System.out.println("ri"+ri+",si:"+si+",fi:"+fi);
            if(fi>leftLen-1){
                result[ri]=rightArray[si];
                si++;
                ri++;
                continue;
            }
            if(si>rightLen-1){
                result[ri]=leftArray[fi];
                fi++;
                ri++;
                continue;
            }
            int f=leftArray[fi];
            int s=rightArray[si];
            if(Comparator.isLTE(f, s)){
                result[ri]=f;
                fi++;
            }else{
                result[ri]=s;
                si++;
            }
            ri++;
        }
        return result;
    }

    public static void main(String[] args) {
        int f[]={-1,0,2,3,7,8,9,10,12,15};
        int s[]={0,0,1,4,5,6,7,8,11,18};
        System.out.println(Arrays.toString(merge(f,s)));
    }
}
