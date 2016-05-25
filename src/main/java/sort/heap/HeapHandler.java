package sort.heap;

import basic.Util;
import basic.sort.SortHandlerBehavior;
import basic.sort.Sortable;

import java.util.Arrays;

/**
 *
 * http://www.cnblogs.com/kkun/archive/2011/11/23/2260286.html
 *
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/12
 * Time:16:52
 */
public class HeapHandler extends SortHandlerBehavior {

    int heapSize = 0;

    /**
     * ˼·:
     * ������ȫ��������˼��
     *
     * �趨�����ʼ����ΪN�����������Ϊ���ѣ����ѵĸ��ڵ������ֵ��
     * ���ڵ�λ�������ײ���O(0)
     * ��������ֵ��O(N-1)����
     * ������ı�����Χ��һ����:N--
     * ��0��N֮���ظ���������
     *
     *
     * ʱ�临�Ӷ�:O(n*log(n))
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
        heapSize = len;
        while (heapSize > 0) {
//            System.out.println("^^^:" + Arrays.toString(originArray));
            rebuild(originArray, heapSize);
//            System.out.println("$$$:" + Arrays.toString(originArray));
            swapAlways(originArray, 0, originArray[0], heapSize - 1, originArray[heapSize - 1]);
//            System.out.println("***:" + Arrays.toString(originArray));
            heapSize--;
        }
        return originArray;
    }

    /**
     * �ؽ�����
     *
     * @param originArray ����
     */
    private void rebuild(Comparable[] originArray, int endIndex) {
        double high = Util.log(endIndex, 2);
        int highlen = (int) high;
        if (high > highlen) {
            highlen++;//������ĸ߶�ֵ
        }
        while (highlen >= 1) {
            int floorStartIndex = (int) Util.pow(2, highlen - 2);//ÿ�δ����ĵ����ڶ���ĵ�һ��������
            if (floorStartIndex <= 0) {
                return;
            }
            int floorEndIndex = (int) Util.pow(2, highlen - 1) - 1;////�����ĵ����ڶ�������һ������������
            //System.out.println("floorStartIndex:" + floorStartIndex + ",floorEndIndex:" + floorEndIndex + ",highlen:" + highlen);
            for (int currentIndex = floorStartIndex; currentIndex <= floorEndIndex; currentIndex++) {
                Comparable leftChildNode = leftChildNode(originArray, currentIndex);
                //System.out.println("currentIndex:" + currentIndex + ",endIndex:" + endIndex + ",heapSize:" + heapSize);
                if (leftChildNode != null) {
                    int leftChildIndex = 2 * currentIndex - 1;
                    //System.out.println("leftChildIndex:" + leftChildIndex + ",leftChildNode:" + leftChildNode);
                    swapIfLessThan(originArray, currentIndex - 1, leftChildIndex);//�ڵ�ǰ�����ý�������֮��ѡ�����ֵ�������ֵ��������ǰ���
                }
//                System.out.println("///:");
//                printHeapTree(originArray);
                Comparable rightChildNode = rightChildNode(originArray, currentIndex);
                if (rightChildNode != null) {
                    int rightChildIndex = 2 * currentIndex;
                    //System.out.println("rightChildIndex:" + rightChildIndex + ",rightChildNode:" + rightChildNode);
                    swapIfLessThan(originArray, currentIndex - 1, rightChildIndex);//�ڵ�ǰ�����ý����ҽ��֮��ѡ�����ֵ�������ֵ��������ǰ���
                }
//                System.out.println("+++:");
//                printHeapTree(originArray);
            }
            highlen--;
        }
    }

    /**
     * ���ӽڵ�
     *
     * @param i ��ǰ�ڵ��λ��
     * @return ���ӽڵ�
     */
    private Comparable leftChildNode(Comparable[] originArray, int i) {
        int expectIndex = 2 * i - 1;
        if (expectIndex > (heapSize - 1) || expectIndex < 0) {
            return null;
        }
        return originArray[expectIndex];
    }

    /**
     * ���ӽڵ�
     *
     * @param i ��ǰ�ڵ��λ��
     * @return ���ӽڵ�
     */
    private Comparable rightChildNode(Comparable[] originArray, int i) {
        int expectIndex = 2 * i;
        if (expectIndex > (heapSize - 1) || expectIndex < 0) {
            return null;
        }
        return originArray[expectIndex];
    }

    private void printHeapTree(Comparable[] array) {
        for (int i = 1; i < heapSize; i = i * 2) {
            for (int k = i - 1; k < 2 * (i) - 1 && k < heapSize; k++) {
                System.out.print(array[k] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws Exception {
        Sortable sortable = new HeapHandler();
        Integer f[] = {13, 19, 9, 5, 12, 8, 7, 4, 11, 2, 6, 21};
        Integer f2[] = {59, -10, 0, 6, -1, 0, 8, 58, 10, 2, 3, 7, 8, 9, 10, 12, 15, -5, 4};
        System.out.println("---:" + Arrays.toString(sortable.sort(f)));
        //System.out.println("---:" + Arrays.toString(sortable.sort(f2)));
        benchmark(sortable);
    }

}
