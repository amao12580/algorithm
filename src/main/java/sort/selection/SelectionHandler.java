package sort.selection;

import basic.Comparator;
import basic.sort.Sortable;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/12
 * Time:16:52
 */
public class SelectionHandler<E extends Comparable> implements Sortable<E>{

    @Override
    public E[] sort(E[]... originArray) throws Exception {

        return null;
    }

    /**
     *  ˼·��
     * �ڵ�i�ε����У���ʣ�µ�(��δ�����)Ԫ�����ҵ���С��Ԫ��
     * ����i��Ԫ������С��Ԫ�ؽ���λ��
     *
     *
     * ʱ�临�Ӷȣ�O(n^2)
     *
     *
     *
     * @param originArray  ԭʼ��������
     * @return �ź��������
     * @throws Exception  ������������ɽ���ʱ�׳�
     */
    @Override
    public E[] sort(E[] originArray) throws Exception {
        if(originArray==null){
            throw new IllegalAccessException("�����Խ���null����");
        }
        int len=originArray.length;
        if(len==1){
            return originArray;
        }
        if(len==2){
            E first=originArray[0];
            E second=originArray[1];
            if(Comparator.isLT(second, first)){
                originArray[0]=second;
                originArray[1]=first;
            }
            return originArray;
        }
        for (int i = 0; i < len; i++) {
            E current=originArray[i];//����ǰԪ�ؼ�ΪXԪ�أ���X�Լ�X֮ǰ��Ԫ�ؼ��Ϸ�Ϊһ�飬��ΪA�顣��ǰԪ��֮�������Ԫ�ط�Ϊһ�飬��ΪB��
            int lessIndex=i+1;//����B��ĵ�һ��Ԫ��ΪB���ڵ���СԪ�أ������Ԫ��ΪYԪ�ء���¼���Ԫ�ص��±�ֵ
            if(lessIndex==len){
                break;
            }
            E lesser=originArray[lessIndex];
            //Ѱ��B���ڵ���СԪ�ص�Ԫ��ֵ���±�ֵ
            for (int j = i+2; j < len; j++) {
                E index=originArray[j];//�����Ԫ��ΪZ
                if(Comparator.isLT(index, lesser)){//���Z��Y��ҪС������B���ڵ���СԪ�ص�Ԫ��ֵ���±�ֵ
                    lesser=index;
                    lessIndex=j;
                }
                System.out.println("***:" + Arrays.toString(originArray));
            }
            if(Comparator.isLT(lesser, current)){//���YԪ�ر�XԪ�ػ�ҪС����������Ԫ�ص�λ��
                originArray[i]=lesser;
                originArray[lessIndex]=current;
            }
            System.out.println("###:"+Arrays.toString(originArray));
        }
        return originArray;
    }

    public static void main(String[] args) throws Exception {
        Sortable<Integer> sortable=new SelectionHandler<>();
        Integer f[]={-1,0,8,58,10,2,3,7,8,9,10,12,15,-5,4};
        System.out.println("---:"+Arrays.toString(sortable.sort(f)));
    }

}
