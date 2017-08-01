package ACM.PrimeRingProblem;

import basic.Util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/7/31
 * Time:17:29
 * <p>
 * 输入正整数n，把整数1, 2, 3,…, n组成一个环，使得相邻两个整数之和均为素数。输出
 * 时从整数1开始逆时针排列。同一个环应恰好输出一次。n≤16。
 * 样例输入：
 * 6
 * 样例输出：
 * 1 4 3 2 5 6
 * 1 6 5 2 3 4
 * <p>
 * Prime Ring Problem, UVa 524
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("------------------------------------------------");
        solution.case2();
        System.out.println("------------------------------------------------");
        solution.case3();
    }

    private void case1() {
        ring(6);
    }

    private void case2() {
        ring(Util.getRandomInteger(1, 16));
    }

    private void case3() {
        ring(16);
    }

    private void ring(int n) {
        System.out.println("n:" + n);
        boolean[] array = new boolean[n];
        LinkedList<Integer> numbers = new LinkedList<>();
        ring(array, numbers, n);
        print();
        result.clear();
    }

    private void print() {
        result.forEach(System.out::println);
    }

    private void ring(boolean[] array, LinkedList<Integer> numbers, int n) {
        if (numbers.size() == n) {
            if (Util.isPrime(numbers.peekFirst() + numbers.peekLast())) {
                addResult(numbers);
            }
            numbers.clear();
            return;
        }
        LinkedList<Integer> currentNumbers;
        for (int i = 0; i < n; i++) {
            if (!array[i]) {
                array[i] = true;
                currentNumbers = new LinkedList<>(numbers);
                if (isPrime(currentNumbers, i + 1)) {
                    currentNumbers.add(i + 1);
                    ring(array, currentNumbers, n);
                }
                array[i] = false;
            }
        }
    }

    private boolean isPrime(LinkedList<Integer> currentNumbers, int number) {
        if (currentNumbers.isEmpty()) {
            return Util.isPrime(number);
        }
        return Util.isPrime(number + currentNumbers.peekLast());
    }

    Set<String> result = new HashSet<>();

    private void addResult(LinkedList<Integer> numbers) {
        int firstIndex = numbers.indexOf(1);
        if (firstIndex < 0) {
            throw new IllegalArgumentException();
        }
        if (firstIndex != 0) {
            int last = numbers.size() - 1;
            for (int i = last; i >= firstIndex; i--) {
                numbers.addFirst(numbers.pollLast());
            }
        }
        result.add(Util.contactAll(' ', numbers.toArray()));
    }
}
