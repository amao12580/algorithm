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
     *  思路：
     * 在第i次迭代中，在剩下的(即未排序的)元素中找到最小的元素
     * 将第i个元素与最小的元素交换位置
     *
     *
     * 时间复杂度：O(n^2)
     *
     *
     *
     * @param originArray  原始输入数组
     * @return 排好序的数组
     * @throws Exception  在输入参数不可解析时抛出
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
            Comparable current=originArray[i];//将当前元素记为X元素，将X以及X之前的元素集合分为一组，记为A组。当前元素之后的所有元素分为一组，记为B组
            int lessIndex=i+1;//假设B组的第一个元素为B组内的最小元素，记这个元素为Y元素。记录这个元素的下标值
            if(lessIndex==len){
                break;
            }
            Comparable lesser=originArray[lessIndex];
            //寻找B组内的最小元素的元素值和下标值
            for (int j = i+2; j < len; j++) {
                Comparable index=originArray[j];//记这个元素为Z
                if(Comparator.isLT(index, lesser)){//如果Z比Y还要小，更新B组内的最小元素的元素值和下标值
                    lesser=index;
                    lessIndex=j;
                }
                System.out.println("***:" + Arrays.toString(originArray));
            }
            swapIfLessThan(originArray,lessIndex,lesser,i,current);//如果Y元素比X元素还要小，交换两个元素的位置
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
