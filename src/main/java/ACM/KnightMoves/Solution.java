package ACM.KnightMoves;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/7/10
 * Time:15:12
 * <p>
 * 输入标准8*8国际象棋棋盘上的两个格子（列用a～h表示，行用1～8表示），求马最少需要多少步从起点跳到终点。例如从a1到b2需要4步。马的移动方式如图6-21所示。
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=380
 * <p>
 * Knight Moves, UVa 439
 * <p>
 * <p>
 * Sample Input
 * e2 e4
 * a1 b2
 * b2 c3
 * a1 h8
 * a1 h7
 * h8 a1
 * b1 c3
 * f6 f6
 * Sample Output
 * To get from e2 to e4 takes 2 knight moves.
 * To get from a1 to b2 takes 4 knight moves.
 * To get from b2 to c3 takes 2 knight moves.
 * To get from a1 to h8 takes 6 knight moves.
 * To get from a1 to h7 takes 5 knight moves.
 * To get from h8 to a1 takes 6 knight moves.
 * To get from b1 to c3 takes 1 knight moves.
 * To get from f6 to f6 takes 0 knight moves.
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
    }

    private void case1() {
        step("e2", "e4");
        step("a1", "b2");
        step("b2", "c3");
        step("a1", "h8");
        step("a1", "h7");
        step("h8", "a1");
        step("b1", "c3");
        step("f6", "f6");
    }

    private void step(String start, String end) {
        if (start.equals(end)) {
            System.out.println("To get from " + start + " to " + end + " takes 0 knight moves.");
            return;
        }
        List<Integer> counts = new ArrayList<>();
        Set<String> starts = new HashSet<>();
        starts.add("root," + start);
        step0(0, counts, starts, end);
        if (counts.size() > 1) {
            Collections.sort(counts);
        }
        System.out.println("To get from " + start + " to " + end + " takes " + counts.get(0) + " knight moves.");
    }

    private void step0(int steps, List<Integer> counts, Set<String> starts, String end) {
        if (starts.isEmpty() || steps > 10000 || counts.size() > 100) {
            return;
        }
        String start, previous;
        String[] array;
        LinkedList<String> nexts;
        steps++;
        Set<String> newStarts = new HashSet<>();
        for (String key : starts) {
            array = key.split(",");
            previous = array[0];
            start = array[1];
            if (start.equals(end)) {
                counts.add(steps);
                return;
            }
            nexts = nextStep(previous, start);
            if (nexts.contains(end)) {
                counts.add(steps);
                continue;
            }
            while (!nexts.isEmpty()) {
                newStarts.add(start + "," + nexts.poll());
            }
        }
        step0(steps, counts, newStarts, end);
    }

    private LinkedList<String> nextStep(String previous, String point) {
        LinkedList<String> result = new LinkedList<>();
        char column = point.charAt(0);
        int row = Integer.valueOf(String.valueOf(point.charAt(1)));
        ifInRange(column + 2, row + 1, result, previous);
        ifInRange(column + 1, row + 2, result, previous);
        ifInRange(column - 1, row + 2, result, previous);
        ifInRange(column - 2, row + 1, result, previous);
        ifInRange(column - 2, row - 1, result, previous);
        ifInRange(column - 1, row - 2, result, previous);
        ifInRange(column + 1, row - 2, result, previous);
        ifInRange(column + 2, row - 1, result, previous);
        return result;
    }

    private void ifInRange(int column, int row, LinkedList<String> result, String previous) {
        if (column >= 97 && column <= 104 && row >= 1 && row <= 8) {
            String key = String.valueOf((char) (column)).concat(String.valueOf(row));
            if ("root".equals(previous) || !key.equals(previous)) {
                result.add(key);
            }
        }
    }
}
