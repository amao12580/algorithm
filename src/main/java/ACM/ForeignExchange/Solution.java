package ACM.ForeignExchange;

import basic.Util;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/4
 * Time:16:53
 * <p>
 * 有n（1≤n≤500000）个学生想交换到其他学校学习。为了简单起见，规定每个想从A学
 * 校换到B学校的学生必须找一个想从B换到A的“搭档”。如果每个人都能找到搭档（一个人不
 * 能当多个人的搭档），学校就会同意他们交换。每个学生用两个整数A、B表示，你的任务
 * 是判断交换是否可以进行。
 * <p>
 * Foreign Exchange, UVa 10763
 */
public class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        Student[] students = solution.generatorOne();
        boolean canExchange = solution.checkCanExchange(students);
        System.out.println("canExchange:" + canExchange);
    }

    private boolean checkCanExchange(Student[] students) {
        Set<Student> want2As = new HashSet<>();
        Set<Student> want2Bs = new HashSet<>();
        for (Student student : students) {
            if (student.isWant2B()) {
                want2Bs.add(student);
            } else {
                want2As.add(student);
            }
        }
        System.out.println("want2A number:" + want2As.size() + ",want2B umber:" + want2Bs.size());
        return want2Bs.size() == want2As.size();
    }

    private Student[] generatorOne() {
        int sum = Util.getRandomInteger(1, 500000);
        Student[] students = new Student[sum];
        for (int i = 0; i < sum; i++) {
            boolean b = Util.getRandomBoolean();
            students[i] = new Student(i + 1, b, !b);
        }
        return students;
    }

    private class Student {
        private int no;
        private boolean want2A;
        private boolean want2B;

        public Student(int no, boolean want2A, boolean want2B) {
            this.no = no;
            this.want2A = want2A;
            this.want2B = want2B;
        }


        public boolean isWant2B() {
            return want2B;
        }

        public int getNo() {
            return no;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Student student = (Student) o;
            return this.getNo() == student.getNo();

        }

        @Override
        public int hashCode() {
            return String.valueOf(this.getNo()).hashCode();
        }
    }
}
