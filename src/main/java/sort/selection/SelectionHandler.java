package sort.selection;

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
public class SelectionHandler extends SortHandlerBehavior {
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
        for (int i = 0; i < len; i++) {
            Comparable current=originArray[i];//����ǰԪ�ؼ�ΪXԪ�أ���X�Լ�X֮ǰ��Ԫ�ؼ��Ϸ�Ϊһ�飬��ΪA�顣��ǰԪ��֮�������Ԫ�ط�Ϊһ�飬��ΪB��
            int lessIndex=i+1;//����B��ĵ�һ��Ԫ��ΪB���ڵ���СԪ�أ������Ԫ��ΪYԪ�ء���¼���Ԫ�ص��±�ֵ
            if(lessIndex==len){
                break;
            }
            Comparable lesser=originArray[lessIndex];
            //Ѱ��B���ڵ���СԪ�ص�Ԫ��ֵ���±�ֵ
            for (int j = i+2; j < len; j++) {
                Comparable index=originArray[j];//�����Ԫ��ΪZ
                if(Comparator.isLT(index, lesser)){//���Z��Y��ҪС������B���ڵ���СԪ�ص�Ԫ��ֵ���±�ֵ
                    lesser=index;
                    lessIndex=j;
                }
                System.out.println("***:" + Arrays.toString(originArray));
            }
            swapIfLessThan(originArray,lessIndex,lesser,i,current);//���YԪ�ر�XԪ�ػ�ҪС����������Ԫ�ص�λ��
            System.out.println("###:"+Arrays.toString(originArray));
        }
        return originArray;
    }

    public static void main(String[] args) throws Exception {
        Sortable sortable=new SelectionHandler();
        Integer f[]={-1,0,8,58,10,2,3,7,8,9,10,12,15,-5,4};
        System.out.println("---:"+Arrays.toString(sortable.sort(f)));
    }

}
