package sort.radix;

import basic.sort.SortHandlerBehavior;
import basic.sort.Sortable;
import sort.merge.MergeHandler;

/**
 * http://www.cnblogs.com/sun/archive/2008/06/26/1230095.html
 * <p>
 * <p>
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/12
 * Time:16:52
 */
public class RadixHandler extends SortHandlerBehavior {

    /**
     * ˼·:
     * һ�������Ͱ���򣬰���λ��ֵ���з�Ͱ
     * <p>
     * �����д��Ƚ���ֵ(ע��,������������)ͳһΪͬ������λ����,��λ�϶̵���ǰ�油��.
     * �����λ��ʼ, ���ν���һ���ȶ�����.
     * ���������λ����һֱ�����λ��������Ժ�, ���оͱ��һ����������.
     * <p>
     * <p>
     *  m�������ֵ�ĳ��ȣ�һ��n >> m
     *
     * ʱ�临�Ӷ�:O(n+m)
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
        Comparable element = originArray[0];
        if (element instanceof Integer) {
        } else {
            throw new Exception("����֧�ֵ��������͡�" + element.getClass());
        }
        //��������е����ֵ
        int elementMaxLen = getElementMaxLen(originArray);
        if (elementMaxLen == 0) {
            return originArray;
        }
        //��������������Ԫ�����㲹λ
        String[] fixedArray = fixArrayElementLen(originArray, elementMaxLen);
        //�ӵ͵��߰�λ��������
        int bit = 1;
        //System.out.println("---:" + Arrays.toString(fixedArray));
        //System.out.println("---elementMaxLen:" + elementMaxLen);

        while (bit <= elementMaxLen) {
            //System.out.println("---bit:" + bit);
            fixedArray = (String[]) sortByBit(elementMaxLen - bit, fixedArray);
            //System.out.println("---:" + Arrays.toString(fixedArray));
            bit++;
        }
        return freeArray(fixedArray);
    }

    private Comparable[] freeArray(String[] fixedArray) {
        Comparable[] result = new Comparable[fixedArray.length];
        for (int i = 0; i < fixedArray.length; i++) {
            result[i] = Integer.valueOf(fixedArray[i]);
        }
        return result;
    }

    private Comparable[] sortByBit(int bit, String[] originArray) throws Exception {//�����ȶ����򣺲�������
        Sortable sortable = new MergeHandler(new RadixSortHandler(bit));
        return sortable.sort(originArray);
    }

    class RadixSortHandler extends SortHandlerBehavior {
        private int bit = 1;

        public RadixSortHandler(int bit) {
            this.bit = bit;
        }

        @Override
        public Comparable[] sort(Comparable[] originArray) throws Exception {
            int len = originArray.length;
            if (len == 1) {
                return originArray;
            }
            if (len == 2) {
                swapIfLessThan(originArray, 1, 0);
                return originArray;
            }
            for (int i = 0; i < len; i++) {
                int endIndex = i - 1;
                if (endIndex <= 0) {
                    continue;
                }
                for (int j = 0; j <= endIndex; j++) {
                    swapIfTrue(originArray, i, j, getBitValueByBitIndex(originArray, i, bit) < getBitValueByBitIndex(originArray, j, bit));
                }
            }
            return originArray;
        }
    }

    private Integer getBitValueByBitIndex(Comparable[] originArray, int index, int bit) {
        String element = originArray[index].toString();
        String valueStr = element.substring(bit, bit + 1);
        //System.out.println("---:" + element+","+bit+","+valueStr);
        return Integer.valueOf(valueStr);
    }


    private String[] fixArrayElementLen(Comparable[] originArray, int elementMaxLen) {
        String[] array = new String[originArray.length];
        for (int i = 0; i < originArray.length; i++) {
            String currentElement = originArray[i].toString();
            int currentLen = currentElement.length();
            int less = elementMaxLen - currentLen;
            if (less > 0) {
                currentElement = getFixValues(less) + currentElement;
            }
            array[i] = currentElement;
        }
        return array;
    }

    private String getFixValues(int length) {
        String r = "";
        for (int i = 0; i < length; i++) {
            r += "0";//���㲹λ�ĺô���1.��ȥ�Ƚϣ�0��Ϊÿλ����Сֵ��������Ƚϣ��Զ��������2.��ת�����ź���󣬷�תΪԭʼֵ����Integer.valueOf���ɻ�ԭ
        }
        return r;
    }

    private int getElementMaxLen(Comparable[] originArray) throws Exception {
        int maxLen = originArray[0].toString().length();
        boolean hasPositive = false;//��������
        boolean hasNegative = false;//���ڸ���
        for (int i = 1; i < originArray.length; i++) {
            Comparable current = originArray[i];
            if (current == null) {
                throw new Exception("������nullֵ");
            }
            Integer element = Integer.valueOf(current.toString());
            if (element > 0) {
                if (!hasPositive) {
                    hasPositive = true;
                }
            } else if (element < 0) {
                if (!hasNegative) {
                    hasNegative = true;
                }
            } else {
                continue;
            }
            int currentLen = originArray[i].toString().length();
            if (currentLen > maxLen) {
                maxLen = currentLen;
            }
        }
        if (hasPositive && hasNegative) {
            throw new Exception("������ͬʱ���������͸���");
        }
        if (!hasNegative && !hasPositive) {
            //����ֵ��Ϊ0
            return 0;
        }
        return maxLen;
    }


    public static void main(String[] args) throws Exception {
        Sortable sortable = new RadixHandler();
        Integer f[] = {113, 19, 0, 5, 12, 8, 7, 4, 11, 2, 6, 21};
        //System.out.println("---:" + Arrays.toString(sortable.sort(Util.getRandomIntegerNumberArray(100))));
        //System.out.println("---:" + Arrays.toString(sortable.sort(f)));

        benchmark(sortable,0,100);
    }
}
