package sort.radix;

import basic.sort.SortHandlerBehavior;
import basic.sort.Sortable;

import java.util.Arrays;

/**
 *
 * http://www.cnblogs.com/sun/archive/2008/06/26/1230095.html
 *
 *
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/12
 * Time:16:52
 */
public class RadixHandler extends SortHandlerBehavior {

    /**
     * ˼·:
     * ������ȫ��������˼��
     * <p>
     * �����д��Ƚ���ֵ(ע��,������������)ͳһΪͬ������λ����,��λ�϶̵���ǰ�油��.
     * �����λ��ʼ, ���ν���һ���ȶ�����.
     * ���������λ����һֱ�����λ��������Ժ�, ���оͱ��һ����������.
     * <p>
     * <p>
     * ʱ�临�Ӷ�:O(n)
     *
     * @param originArray ԭʼ��������
     * @return �ź��������
     * @throws Exception ������������ɽ���ʱ�׳�
     */
    @Override
    public Comparable[] sort(Comparable[] originArray) throws Exception {
        init(originArray);
        int len = originArray.length;
        if (len == 1) {
            return originArray;
        }
        if (len == 2) {
            swapIfLessThan(originArray, 1, 0);
            return originArray;
        }
        //�ȼ���Ƿ�Ϊ�������ͣ��������͸���������������ֻ��Ҫ��Ϊ�����־Ϳ����ˡ�

        //������㷨���Խ�����������ת��Ϊ�������ͣ����ұ��ִ�С�ϵĶ�Ӧ��ϵ��Ҳ����������㷨������
        Comparable element=originArray[0];
        if(element instanceof Integer){
        }else{
            throw new Exception("����֧�ֵ��������͡�"+element.getClass());
        }
        //��������е����ֵ
        int elementMaxLen=getElementMaxLen(originArray);
        //��������������Ԫ�����㲹λ
        String[] fixdArray=fixArrayElementLen(originArray,elementMaxLen);
        System.out.println("---:" + Arrays.toString(fixdArray));
        int bit=1;
        while(bit<=elementMaxLen){

            bit++;
        }



        return originArray;
    }

    private String[] fixArrayElementLen(Comparable[] originArray, int elementMaxLen) {
        String[] array=new String[originArray.length];
        for (int i = 0; i < originArray.length; i++) {
            String currentElement=originArray[i].toString();
            int currentLen=currentElement.length();
            int less=elementMaxLen-currentLen;
            if(less>0){
                currentElement=getFixValues(less)+currentElement;
            }
            array[i]=currentElement;
        }
        return array;
    }

    private String getFixValues(int length) {
        String r="";
        for (int i = 0; i < length; i++) {
            r+="0";
        }
        return r;
    }

    private int getElementMaxLen(Comparable[] originArray){
        int maxLen=originArray[0].toString().length();
        for (int i = 1; i < originArray.length; i++) {
            int currentLen=originArray[i].toString().length();
            if(currentLen>maxLen){
                maxLen=currentLen;
            }
        }
        return maxLen;
    }


    public static void main(String[] args) throws Exception {
        Sortable sortable = new RadixHandler();
        Integer f[] = {13, 19, 9, 5, 12, 8, 7, 4, 11, 2, 6, 21};
        Integer f2[] = {59, -10, 0, 6, -1, 0, 8, 58, 10, 2, 3, 7, 8, 9, 10, 12, 15, -5, 4};
        System.out.println("---:" + Arrays.toString(sortable.sort(f)));
        //System.out.println("---:" + Arrays.toString(sortable.sort(f2)));
        //benchmark(sortable);
    }

}
