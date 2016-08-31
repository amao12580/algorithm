package leetcode.sumEqualZero;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/6/22
 * Time:13:39
 * <p>
 * 有一个随机整数数组，从中挑ABC三个整数，让ABC三个整数加起来等于零，看有多少个不重复的组合？
 */
public class SolutionForDynamicPrograming {
    public static void main(String[] args) {
        //int[] originArray = {-1, 0, 1, 2, -1, -4};
        int[] originArray = {-5, 14, 1, -2, 11, 11, -10, 3, -6, 0, 3, -4, -9, -13, -8, -7, 9, 8, -7, 11, 12, -7, 4, -7, -1, -5, 13, 1, -2, 8, -13, 0, -1, 3, 13, -13, -1, 10, 5, 1, -13, -15, 12, -7, -13, -11, -7, 3, 13, 1, 0, 2, 1, 11, 10, 8, -8, 1, -14, -3, -6, -12, 12, 0, 6, 2, 2, -9, -3, 14, -1, -9, 14, -4, -1, 8, -8, 7, -4, 12, -14, 3, -9, 2, 0, -13, -13, -1, 3, -12, 11, 4, -9, 8, 11, 5, -5, -10, 3, -1, -11, -13, 5, -12, -10, 11, 11, -3, -5, 14, -13, -4, -5, -7, 6, 2, -13, 0, 8, -3, 4, 4, -14, 2};
        System.out.println("input:" + Arrays.toString(originArray));
        System.out.println("-------      dynamicPrograming        -------");
        SolutionForDynamicPrograming solution = new SolutionForDynamicPrograming();
        long s = System.currentTimeMillis();
        List<List<Integer>> result = solution.dynamicPrograming(originArray, 3, 0);
        long e = System.currentTimeMillis();
        System.out.println("time:" + (e - s));
        //solution.println(solution.toArray(result));
    }

    private Set<String> uniques = new HashSet<>();
    private Map<Integer, Map<Integer, List<List<Integer>>>> existedSolution = new HashMap<>();
    private List<List<Integer>> empty = new LinkedList<>();

    private int[][] toArray(List<List<Integer>> solution) {
        int[][] result = new int[solution.size()][3];
        int index = 0;
        for (List<Integer> list : solution) {
            result[index] = fromIntegerList(list);
            index++;
        }
        return result;
    }

    private int[] fromIntegerList(List<Integer> list) {
        Integer[] part = new Integer[3];
        part = list.toArray(part);
        int[] result = new int[3];
        for (int i = 0; i < part.length; i++) {
            result[i] = part[i];
        }
        return result;
    }

    private void println(int[][] result) {
        System.out.println();
        System.out.println("-------      begin        -------");
        System.out.println("-------      count:" + result.length + "        -------");
        for (int i = 0; i < result.length; i++) {
            System.out.println((i + 1) + ":" + Arrays.toString(result[i]));
        }
        System.out.println("-------      end        -------");
    }

    private List<List<Integer>> dynamicPrograming(int[] array, int number, int expectValue) {
        Arrays.sort(array);
        return loop(array, 0, number, 0);
    }

    /**
     * 运用DP思想来降低递归次数
     */
    private List<List<Integer>> loop(int[] array, int beginIndex, int number, int expectValue) {
        //System.out.println("number:" + number + ",expectValue:" + expectValue);
        if (number == 0 || beginIndex > array.length - 1) {
            return empty;
        }
        //尝试从已知的答案中获取
        List<List<Integer>> existed = getExistedSolution(number, expectValue);
        if (existed != null) {
            return existed;
        }
        List<List<Integer>> thisPartGroup = new LinkedList<>();
        if (number == 1) {//最底层不再需要向下分解
            if (array[beginIndex] > expectValue || array[array.length - 1] < expectValue) {

            } else {
                int expectIndex = Arrays.binarySearch(array, beginIndex, array.length, expectValue);
                if (expectIndex >= beginIndex) {
                    List<Integer> underlying = new LinkedList<>();
                    underlying.add(expectValue);
                    thisPartGroup.add(underlying);
                }
            }
        } else {
            for (int index = beginIndex; index < array.length; index++) {
                int currentValue = array[index];
                int nextIndex = index + 1;
                //将问题分解为同性质的子问题
                List<List<Integer>> nextPartGroup = loop(array, nextIndex, number - 1, expectValue - currentValue);
                //System.out.println("currentValue:" + currentValue + ",number:" + number + ",expectValue:" + expectValue + ",groups:" + nextPartGroup.toString());
                if (!nextPartGroup.isEmpty()) {
                    buildFullGroups(number, thisPartGroup, nextPartGroup, currentValue);
                }
            }
        }
        putExistedSolution(number, expectValue, thisPartGroup);
        return thisPartGroup;
    }

    private void buildFullGroups(int number, List<List<Integer>> thisPartGroup, List<List<Integer>> nextPartGroup, int currentValue) {
        for (List<Integer> list : nextPartGroup) {
            List<Integer> resultList = new LinkedList<>(list);
            resultList.add(currentValue);
            //Collections.sort(resultList);
            if (number == 3) {
                //System.out.println(resultList.get(0) + "," + currentValue + "." + resultList.toString());
                if (uniques.add(resultList.get(0) + "," + currentValue)) {
                    thisPartGroup.add(resultList);
                }
            } else {
                thisPartGroup.add(resultList);
            }
        }
    }

    private void putExistedSolution(int number, int expectValue, List<List<Integer>> result) {
        Map<Integer, List<List<Integer>>> expects = new HashMap<>();
        if (existedSolution.containsKey(number)) {
            expects = existedSolution.get(number);
        }
        expects.put(expectValue, result);
        existedSolution.put(number, expects);
    }

    private List<List<Integer>> getExistedSolution(int number, int expectValue) {
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
