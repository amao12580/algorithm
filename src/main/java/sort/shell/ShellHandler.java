package sort.shell;

import basic.Util;
import basic.sort.SortHandlerBehavior;
import basic.sort.Sortable;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/12
 * Time:16:52
 */
public class ShellHandler extends SortHandlerBehavior {
    /**
     * ˼·��
     * ϣ�������ǻ��ڲ��������
     * ����h�Ĳ������в�������
     *
     * ��������һ�����3x+1��1,4,13,40,121,364.....��ʹ�����ֵ������е�ϣ����������ıȽϴ������ᳬ��N�����ɱ����Ե������еĳ��ȡ�
     *
     * �����£�ʹ��3x+1�������е�ϣ������ıȽϴ�����O(N^(3/2))
     *
     * 3x+1���е�ͨ�ʽ�ǣ�f(x)=((3^(x+1))-1)/2
     *
     *
     * ʱ�临�Ӷȣ�O(n*logn)
     *
     * @param originArray ԭʼ��������
     * @return �ź��������
     * @throws Exception ������������ɽ���ʱ�׳�
     */
    @Override
    public Comparable[] sort(Comparable[] originArray) throws Exception {
        init(originArray);
        int N = originArray.length;
        //System.out.println("N:" + N);
        // 3x+1 increment sequence:  1, 4, 13, 40, 121, 364, 1093, ...
        int h = 1;
        while (h < N / 3) {
            h = 3 * h + 1;
        }
        //System.out.println("h:" + h);
        while (h >= 1) {
            // h-sort the array
            for (int i = h; i < N; i++) {
                for (int j = i; j >= h; j -= h) {
                    swapIfLessThan(originArray, j, j - h);
                }
            }
            h /= 3;
        }
        return originArray;
    }

    public static void main(String[] args) throws Exception {
        Sortable sortable = new ShellHandler();
        //Integer f[] = {5, 0, 8, 58, 10, 2, 3, 7, 8, 9, 10, 12, 15, -5, 4, 152,5, 0, 8, 58, 10, 2, 3, 7, 8, 9, 10, 12, 15, -5, 4, 152,5, 0, 8, 58, 10, 2, 3, 7, 8, 9, 10, 12, 15, -5, 4, 152,5, 0, 8, 58, 10, 2, 3, 7, 8, 9, 10, 12, 15, -5, 4, 152,5, 0, 8, 58, 10, 2, 3, 7, 8, 9, 10, 12, 15, -5, 4, 152,5, 0, 8, 58, 10, 2, 3, 7, 8, 9, 10, 12, 15, -5, 4, 152};
        Integer[] originArray=Util.getRandomIntegerNumberArray(100);
        System.out.println("originArray.length:" + originArray.length);
        Comparable[] sortedArray=sortable.sort(originArray);
        System.out.println("---:" + Arrays.toString(sortedArray));
        System.out.println("sortedArray.length:" + sortedArray.length);
        Comparable[] rearrangeArray=sortable.rearrange(sortedArray);
        System.out.println("---:" + Arrays.toString(rearrangeArray));
        System.out.println("rearrangeArray.length:" + rearrangeArray.length);
    }
}
