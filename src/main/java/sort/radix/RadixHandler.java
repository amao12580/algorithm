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
     * 思路:
     * 一种特殊的桶排序，按照位数值进行分桶
     * <p>
     * 将所有待比较数值(注意,必须是正整数)统一为同样的数位长度,数位较短的数前面补零.
     * 从最低位开始, 依次进行一次稳定排序.
     * 这样从最低位排序一直到最高位排序完成以后, 数列就变成一个有序序列.
     * <p>
     * <p>
     *  m是最大数值的长度，一般n >> m
     *
     * 时间复杂度:O(n+m)
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
        Comparable element = originArray[0];
        if (element instanceof Integer) {
        } else {
            throw new Exception("不受支持的排序类型。" + element.getClass());
        }
        //求出数组中的最长数值
        int elementMaxLen = getElementMaxLen(originArray);
        if (elementMaxLen == 0) {
            return originArray;
        }
        //将不够长的数组元素用零补位
        String[] fixedArray = fixArrayElementLen(originArray, elementMaxLen);
        //从低到高按位进行排序
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

    private Comparable[] sortByBit(int bit, String[] originArray) throws Exception {//采用稳定排序：插入排序
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
            r += "0";//用零补位的好处：1.免去比较，0作为每位的最小值，不参与比较，自动排在最后。2.易转换，排好序后，反转为原始值，用Integer.valueOf即可还原
        }
        return r;
    }

    private int getElementMaxLen(Comparable[] originArray) throws Exception {
        int maxLen = originArray[0].toString().length();
        boolean hasPositive = false;//存在正数
        boolean hasNegative = false;//存在负数
        for (int i = 1; i < originArray.length; i++) {
            Comparable current = originArray[i];
            if (current == null) {
                throw new Exception("不允许null值");
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
            throw new Exception("不允许同时包含正数和负数");
        }
        if (!hasNegative && !hasPositive) {
            //所有值都为0
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
