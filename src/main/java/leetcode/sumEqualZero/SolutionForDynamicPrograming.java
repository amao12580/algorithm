package leetcode.sumEqualZero;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/6/22
 * Time:13:39
 * <p>
 * ��һ������������飬������ABC������������ABC�������������������㣬���ж��ٸ����ظ�����ϣ�
 */
public class SolutionForDynamicPrograming {
    public static void main(String[] args) {
        int[] originArray = {0, 1, 2, 3, 4, 5, -1, -2, -3, -4, -5};
        System.out.println("input:" + Arrays.toString(originArray));
        System.out.println("-------      dynamicPrograming        -------");
        count = 3;//3�������
        int expectSumValue = 0;//�����ۼ�ֵΪ��
        dynamicPrograming(originArray, 0, count, expectSumValue);
        println(toArray(solution));
    }

    private static int[][] toArray(List<List<Integer>> solution) {
        int[][] result = new int[solution.size()][count];
        int index = 0;
        for (List<Integer> list : solution) {
            result[index] = fromIntegerList(list);
            index++;
        }
        return result;
    }

    private static int[] fromIntegerList(List<Integer> list) {
        Integer[] part = new Integer[count];
        part = list.toArray(part);
        int[] result = new int[count];
        for (int i = 0; i < part.length; i++) {
            result[i] = part[i];
        }
        return result;
    }

    private static void println(int[][] result) {
        System.out.println();
        System.out.println("-------      begin        -------");
        System.out.println("-------      count:" + result.length + "        -------");
        for (int i = 0; i < result.length; i++) {
            System.out.println((i + 1) + ":" + Arrays.toString(result[i]));
        }
        System.out.println("-------      end        -------");
    }

    private static int count;
    private static Map<Integer, Map<Integer, List<List<Integer>>>> existedSolution = new HashMap<>();
    private static List<List<Integer>> solution = new ArrayList<>();

    /**
     * ����DP˼�������͵ݹ����
     */
    private static List<List<Integer>> dynamicPrograming(int[] array, int beginIndex, int number, int expectValue) {
        if (number == 0 || beginIndex == array.length) {
            return null;
        }
        //���Դ���֪�Ĵ��л�ȡ
        List<List<Integer>> existed = getExistedSolution(number, expectValue);
        if (existed != null && !existed.isEmpty()) {
            //�����ˣ����п��ܲ�����beginIndexҪ��
            //У��������Ƿ����Ҫ��
            List<List<Integer>> vailds = filterVailds(existed, array, beginIndex);
            if (vailds != null && !vailds.isEmpty()) {
                return vailds;
            }
        }
        List<List<Integer>> thisPartGroup = new ArrayList<>();
        for (int i = beginIndex; i < array.length; i++) {
            int currentValue = array[i];
            if (number == 1) {//��ײ㲻����Ҫ���·ֽ�
                if (currentValue == expectValue) {
                    List<Integer> underlying = new ArrayList<>();
                    underlying.add(currentValue);
                    thisPartGroup.add(underlying);
                    break;
                }
            } else {
                //������ֽ�Ϊͬ���ʵ�������
                List<List<Integer>> nextPartGroup = dynamicPrograming(array, i + 1, number - 1, expectValue - currentValue);
                if (nextPartGroup != null) {
                    buildFullGroups(thisPartGroup, nextPartGroup, currentValue);
                    if (number == count) {
                        //�ҵ��˶�����еĽⷨ
                        solution.addAll(thisPartGroup);
                        thisPartGroup.clear();
                    }
                }
            }
        }
        if (number != count) {
            //���������㣬�ͽ��𰸻�������
            putExistedSolution(number, expectValue, thisPartGroup);
        }
        if (thisPartGroup.isEmpty()) {
            thisPartGroup = null;
        }
        return thisPartGroup;
    }

    private static List<List<Integer>> filterVailds(List<List<Integer>> existed, int[] array, int beginIndex) {
        List<List<Integer>> noRepeats = new ArrayList<>();
        for (List<Integer> list : existed) {
            boolean isOutOfRange = false;
            for (Integer key : list) {
                int index = Arrays.binarySearch(array, beginIndex, array.length - 1, key);
                if (index < beginIndex) {
                    isOutOfRange = true;
                    break;
                }
            }
            if (!isOutOfRange) {
                noRepeats.add(list);
            }
        }
        return noRepeats;
    }

    private static void buildFullGroups(List<List<Integer>> thisPartGroup, List<List<Integer>> nextPartGroup, int currentValue) {
        for (List<Integer> list : nextPartGroup) {
            List<Integer> resultList = new ArrayList<>(list);
            resultList.add(currentValue);
            thisPartGroup.add(resultList);
        }
    }

    private static void putExistedSolution(int number, int expectValue, List<List<Integer>> result) {
        Map<Integer, List<List<Integer>>> expects = new HashMap<>();
        if (existedSolution.containsKey(number)) {
            expects = existedSolution.get(number);
        }
        expects.put(expectValue, result);
        existedSolution.put(number, expects);
    }

    private static List<List<Integer>> getExistedSolution(int number, int expectValue) {
        if (existedSolution.containsKey(number)) {
            Map<Integer, List<List<Integer>>> expects = existedSolution.get(number);
            if (expects.containsKey(expectValue)) {
                return expects.get(expectValue);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
