package ACM.Bandwidth;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/8/2
 * Time:16:19
 * <p>
 * 给出一个n（n≤8）个结点的图G和一个结点的排列，定义结点i的带宽b(i)为i和相邻结点
 * 在排列中的最远距离，而所有b(i)的最大值就是整个图的带宽。给定图G，求出让带宽最小
 * 的结点排列，如图7-7所示。
 * <p>
 * 下面两个排列的带宽分别为6和5。具体来说，图7-8（a）中各个结点的带宽分别为6, 6,
 * 1, 4, 1, 1, 6, 6，图7-8（b）中各个结点的带宽分别为5, 3, 1, 4, 3, 5, 1, 4
 * <p>
 * Bandwidth, UVa 140
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=76
 * <p>
 * Sample Input
 * A:FB;B:GC;D:GC;F:AGH;E:HD
 * #
 * Sample Output
 * A B C F G D H E -> 3
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
    }

    private void case1() {
        bandWidth("A:FB;B:GC;D:GC;F:AGH;E:HD");
    }

    private void bandWidth(String desc) {
        Set<Character> seeds = new HashSet<>();
        Character[] seedArray = new Character[seeds.size()];
        Map<Character, Set<Character>> mapping = new HashMap<>();
        parse(desc, seeds, mapping);
        seedArray = seeds.toArray(seedArray);
        boolean[] array = new boolean[seedArray.length];
        globalMin = seedArray.length - 1;
        bandWidth(array, new LinkedHashMap<>(), 0, seedArray, mapping, array.length, array.length - 1);
        print();
    }

    private void print() {
        if (globalMinSequence == null) {
            System.out.println("No solution.");
        } else {
            StringBuilder builder = new StringBuilder();
            Set<Character> characters = globalMinSequence.keySet();
            for (Character c : characters) {
                builder = builder.append(c).append(" ");
            }
            builder = builder.append("->").append(globalMin);
            System.out.println(builder.toString());
        }
    }

    private int globalMin = 0;
    private Map<Character, Integer> globalMinSequence;

    private void bandWidth(boolean[] array, Map<Character, Integer> sequence, int index, Character[] seedArray, Map<Character, Set<Character>> mapping, int len, int thisSequenceMin) {
        Map<Character, Integer> currentSequence;
        int currentMin;
        for (int i = 0; i < len; i++) {
            if (!array[i]) {
                array[i] = true;
                currentSequence = new LinkedHashMap<>(sequence);
                currentMin = min(currentSequence, index, i, seedArray, mapping);
                if (thisSequenceMin < currentMin) {
                    array[i] = false;
                    continue;
                }
                thisSequenceMin = currentMin;
                if (globalMin != 0 && thisSequenceMin > globalMin) {
                    array[i] = false;
                    continue;
                }
                currentSequence.put(seedArray[i], index + 1);
                if (currentSequence.size() == len && (globalMin == 0 || thisSequenceMin < globalMin)) {
                    globalMin = thisSequenceMin;
                    globalMinSequence = currentSequence;
                }
                bandWidth(array, currentSequence, index + 1, seedArray, mapping, len, thisSequenceMin);
                array[i] = false;
            }
        }
    }

    private int min(Map<Character, Integer> currentSequence, int index, int i, Character[] seedArray, Map<Character, Set<Character>> mapping) {
        Map<Character, Integer> sequence = new HashMap<>(currentSequence);
        sequence.put(seedArray[i], index + 1);
        return min(sequence, index + 1, mapping);
    }

    private int min(Map<Character, Integer> sequence, int index, Map<Character, Set<Character>> mapping) {
        Character key;
        int c, cd, nso;
        int d = 0;
        Integer keyIndex, valueIndex;
        Set<Character> valueSet;
        for (Map.Entry<Character, Set<Character>> entry : mapping.entrySet()) {
            key = entry.getKey();
            keyIndex = sequence.get(key);
            valueSet = entry.getValue();
            if (keyIndex == null) {
                keyIndex = index + 1;
                nso = keyIndex + 1;
            } else {
                nso = index + 1;
            }
            c = 0;
            for (Character value : valueSet) {
                valueIndex = sequence.get(value);
                if (valueIndex == null) {
                    valueIndex = nso + (c++);
                }
                cd = Math.abs(keyIndex - valueIndex);
                if (cd > d) {
                    d = cd;
                }
            }
        }
        return d;
    }

    private void parse(String desc, Set<Character> seeds, Map<Character, Set<Character>> mapping) {
        String[] array = desc.split(";");
        char key;
        char[] values;
        for (String item : array) {
            key = item.charAt(0);
            seeds.add(key);
            values = item.substring(2).toCharArray();
            for (char value : values) {
                seeds.add(value);
                add(mapping, key, value);
                add(mapping, value, key);
            }
        }
    }

    private void add(Map<Character, Set<Character>> mapping, Character key, Character value) {
        Set<Character> valueSet;
        valueSet = mapping.computeIfAbsent(key, k -> new HashSet<>());
        valueSet.add(value);
    }
}
