package ACM.MatrixChainMultiplication;

import basic.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/17
 * Time:9:48
 * <p>
 * 输入n个矩阵的维度和一些矩阵链乘表达式，输出乘法的次数。如果乘法无法进行，输
 * 出error。假定A是m * n矩阵，B是n * p矩阵，那么AB是m * p矩阵，乘法次数为m * n * p。如果A的
 * 列数不等于B的行数，则乘法无法进行。
 * 例如，A是50 * 10的，B是10 * 20的，C是20 * 5的，则(A(BC))的乘法次数为10 * 20 * 5（BC的
 * 乘法次数）+ 50 * 10 * 5（(A(BC))的乘法次数）= 3500。
 * <p>
 * Matrix Chain Multiplication, UVa 442
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=6&page=show_problem&problem=383
 */
public class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
    }

    private void case1() {
        String[] expressions = {
                "A", "B", "C", "(AA)", "(AB)", "(AC)", "(A(BC))", "((AB)C)", "(((((DE)F)G)H)I)", "(D(E(F(G(HI)))))", "((D(EF))((GH)I))"
        };
        for (String expression : expressions) {
            Map<String, Matrix> matrixMap = new HashMap<>();
            matrixMap.put("A", new Matrix('A', 50, 10));
            matrixMap.put("B", new Matrix('B', 10, 20));
            matrixMap.put("C", new Matrix('C', 20, 5));
            matrixMap.put("D", new Matrix('D', 30, 35));
            matrixMap.put("E", new Matrix('E', 35, 15));
            matrixMap.put("F", new Matrix('F', 15, 5));
            matrixMap.put("G", new Matrix('G', 5, 10));
            matrixMap.put("H", new Matrix('H', 10, 20));
            matrixMap.put("I", new Matrix('I', 20, 25));
            calculate(matrixMap, expression);
        }
    }

    private void calculate(Map<String, Matrix> matrixs, String expressionStr) {
        System.out.print("expression:" + expressionStr);
        int endIndex = expressionStr.length() - 1;
        char right = ')';
        Stack<Matrix> stack = new Stack<>();
        for (int i = 0; i <= endIndex; i++) {
            char c = expressionStr.charAt(i);
            if (Util.isAlpha(c)) {
                stack.push(matrixs.get(String.valueOf(c)));
            } else if (c == right) {
                while (stack.size() > 1) {
                    Matrix rightMatrix = stack.pop();
                    if (stack.isEmpty()) {
                        stack.push(rightMatrix);
                        break;
                    }
                    Matrix leftMatrix = stack.pop();
                    if (leftMatrix != null && rightMatrix != null) {
                        if (leftMatrix.getN() == rightMatrix.getM()) {
                            Matrix matrix = new Matrix();
                            matrix.setName(leftMatrix.getName() + rightMatrix.getName());
                            matrix.setM(leftMatrix.getM());
                            matrix.setN(rightMatrix.getN());
                            matrix.setNum(rightMatrix.getNum() + leftMatrix.getNum() + leftMatrix.getM() * leftMatrix.getN() * rightMatrix.getN());
                            stack.push(matrix);
                        } else {
                            System.out.println("     error");
                            return;
                        }
                    }
                }
            }
        }
        System.out.println("     " + (stack.isEmpty() ? 0 : stack.peek() == null ? 0 : stack.pop().getNum()));
    }

    private class Matrix {
        private String name;
        private int m;
        private int n;
        private long num = 0;

        public Matrix() {
        }

        public Matrix(char name, int m, int n) {
            this.name = String.valueOf(name);
            this.m = m;
            this.n = n;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setM(int m) {
            this.m = m;
        }

        public void setN(int n) {
            this.n = n;
        }

        public void setNum(long num) {
            this.num = num;
        }

        public long getNum() {
            return num;
        }

        public String getName() {
            return name;
        }

        public int getM() {
            return m;
        }

        public int getN() {
            return n;
        }
    }
}
