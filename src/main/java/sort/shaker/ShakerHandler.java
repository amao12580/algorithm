package sort.shaker;

import basic.sort.SortHandlerBehavior;
import basic.sort.Sortable;

/**
 * http://bubkoo.com/2014/01/15/sort-algorithm/shaker-sort/
 * <p>
 * <p>
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/12
 * Time:16:52
 */
public class ShakerHandler extends SortHandlerBehavior {

    /**
     * ��β��������ð���������΢���Ρ���ͬ�ĵط����ڣ���β�������Ǵӵ͵���Ȼ��Ӹߵ����������򣬶�ð����������ӵ͵���ȥ�Ƚ��������ÿ��Ԫ�ء�
     * ���ɱ�ð�������Ч����΢��һ�㣬ԭ����ð������ֻ��һ��������бȶ�(�ɵ͵���)��ÿ��ѭ��ֻ�ƶ�һ����Ŀ��
     * <p>
     * <p>
     * ������̣�
     * �ȶ���������ҽ���ð���������򣩣�������Ԫ��ȥ�����Ҷ�
     * �ٶ�������ҵ������ð�����򣨽��򣩣�����С��Ԫ��ȥ�������
     * �Դ����ƣ����θı�ð�ݵķ��򣬲�������Сδ����Ԫ�صķ�Χ��ֱ�����һ��Ԫ�ؽ���
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
        int endIndex = originArray.length - 1;
        int beginIndex = 0;
        while (beginIndex <= endIndex) {
            endIndex = ascending(originArray, beginIndex, endIndex);
            beginIndex = descending(originArray, beginIndex, endIndex);
        }
        return originArray;
    }

    private int ascending(Comparable[] originArray, int beginIndex, int endIndex) {
        for (int i = beginIndex; i <= endIndex; i++) {
            int nextIndex = i + 1;
            if (nextIndex > endIndex) {
                break;
            }
            swapIfLessThan(originArray, nextIndex, i);
        }
        endIndex--;
        return endIndex;
    }

    private int descending(Comparable[] originArray, int beginIndex, int endIndex) {
        for (int i = endIndex; i >= beginIndex; i--) {
            int previousIndex = i - 1;
            if (previousIndex < beginIndex) {
                break;
            }
            swapIfLessThan(originArray, i, previousIndex);
        }
        beginIndex++;
        return beginIndex;
    }

    public static void main(String[] args) throws Exception {
        Sortable sortable = new ShakerHandler();
        Integer f[] = {113, 19, 0, 5, 12, 8, 7, 4, 11, 2, 6, 21};
        //System.out.println("---:" + Arrays.toString(sortable.sort(Util.getRandomIntegerNumberArray(1000000, 0, 100))));
        //System.out.println("---:" + Arrays.toString(sortable.sort(f)));
        benchmark(sortable, 0, 10000);
    }
}
