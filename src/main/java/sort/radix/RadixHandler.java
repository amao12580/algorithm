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
     * 思路:
     * 借用完全二叉树的思想
     * <p>
     * 将所有待比较数值(注意,必须是正整数)统一为同样的数位长度,数位较短的数前面补零.
     * 从最低位开始, 依次进行一次稳定排序.
     * 这样从最低位排序一直到最高位排序完成以后, 数列就变成一个有序序列.
     * <p>
     * <p>
     * 时间复杂度:O(n)
     *
     * @param originArray 原始输入数组
     * @return 排好序的数组
     * @throws Exception 在输入参数不可解析时抛出
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
        //先检查是否为整数类型，正整数和负整数都可以排序，只需要分为两部分就可以了。

        //如果有算法可以将非整数类型转化为整数类型，并且保持大小上的对应关系，也可以用这个算法来排序
        Comparable element=originArray[0];
        if(element instanceof Integer){
        }else{
            throw new Exception("不受支持的排序类型。"+element.getClass());
        }
        //求出数组中的最长数值
        int elementMaxLen=getElementMaxLen(originArray);
        //将不够长的数组元素用零补位
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
