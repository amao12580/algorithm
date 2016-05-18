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
     *  思路：
     *
     * 快速排序时基于分治模式处理的，
     * 对一个典型子数组A[p...r]排序的分治过程为三个步骤：
     * 1.分解：
     * A[p..r]被划分为俩个（可能空）的子数组A[p ..q-1]和A[q+1 ..r]，使得
     * A[p ..q-1] <= A[q] <= A[q+1 ..r]
     * 2.解决：通过递归调用快速排序，对子数组A[p ..q-1]和A[q+1 ..r]排序。
     * 3.合并。
     *
     *
     * 时间复杂度：O(n*log(n))
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

        return originArray;
    }

    public static void main(String[] args) throws Exception {

    }

}
