package classic.middleElement;

import basic.sort.Sortable;
import sort.quick.QuickHandler;

import java.util.Arrays;

/**
 * ���ٴ������������ҵ���λ��
 * <p>
 * ��λ���Ķ��壺��λ����ֵ��������������м�λ��
 * <p>
 * ˼·��
 * <p>
 * 1.�����ȡ�м�λ��
 */
public class SolutionV1 {
    public static void main(String[] args) throws Exception {
        Comparable originArray[] = {113, 19, 0, 5, 12, 8, 7, 4, 11, 2, 6, 21, 9};
        Sortable sortable = new QuickHandler();
        originArray = sortable.sort(originArray);
        System.out.println("sorted:" + Arrays.toString(originArray));
        System.out.println("middle:" + originArray[originArray.length / 2].toString());
    }

}
