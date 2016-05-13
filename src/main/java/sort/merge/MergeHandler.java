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
     * 对多个已排序的数组进行有序合并
     *
     * @param sortedArrays 多个数组
     * @return 有序的合集数组
     * @throws IllegalAccessException
     */
    @Override
    public Integer[] sort(Integer[] ... sortedArrays) throws IllegalAccessException {
        if(sortedArrays==null){
            throw new IllegalAccessException("不可以接受null参数");
        }
        if(sortedArrays.length<2){
            return sortedArrays[0];
        }
        //考虑切分为每两个为一组，每组使用callable并行计算
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
     * 将两个已经排好序的数组合并成一个有序数组。
     *
     * 排好序是指数值小的元素总是下标值较小
     *
     * 时间复杂度为 On n为l两个数组的元素总个数
     *
     * @param leftArray  第一个排好序的数组
     * @param rightArray  第二个排好序的数组
     * @return 合并完成的有序数组
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
