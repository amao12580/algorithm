package sort.bubble;

import basic.sort.SortHandlerBehavior;
import basic.sort.Sortable;

/**
 * http://blog.csdn.net/morewindows/article/details/6657829
 * <p>
 * <p>
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/12
 * Time:16:52
 */
public class BubbleHandler extends SortHandlerBehavior {

    /**
     * �����鳤��ΪN��
     * 1���Ƚ����ڵ�ǰ��������ݣ����ǰ�����ݴ��ں�������ݣ��ͽ��������ݽ�����
     * 2������������ĵ�0�����ݵ�N-1�����ݽ���һ�α���������һ�����ݾ͡������������N-1��λ�á�
     * 3��N=N-1�����N��Ϊ0���ظ�ǰ�����������������ɡ�
     *
     * O(n^2)
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
        int length = originArray.length;
        while (length > 0) {
            for (int i = 0; i < length; i++) {
                int nextIndex = i + 1;
                if (nextIndex >= length) {
                    break;
                }
                swapIfLessThan(originArray, nextIndex, i);
            }
            length--;
        }
        //System.out.println(Arrays.toString(originArray));
        return originArray;
    }

    public static void main(String[] args) throws Exception {
        Sortable sortable = new BubbleHandler();
        Integer f[] = {113, 19, 0, 5, 12, 8, 7, 4, 11, 2, 6, 21};
        //System.out.println("---:" + Arrays.toString(sortable.sort(Util.getRandomIntegerNumberArray(100))));
        //System.out.println("---:" + Arrays.toString(sortable.sort(f)));
        benchmark(sortable, 0, 10000);
    }
}
