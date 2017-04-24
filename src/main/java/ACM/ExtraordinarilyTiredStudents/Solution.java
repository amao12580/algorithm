package ACM.ExtraordinarilyTiredStudents;

import basic.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/4/21
 * Time:16:52
 * <p>
 * 课堂上有n个学生（n≤10）。每个学生都有一个“睡眠-清醒”周期，其中第i个学生醒A i 分
 * 钟后睡B i 分钟，然后重复（1≤A i ，B i ≤5），初始时第i个学生处在他的周期的第C i 分钟。每个
 * 学生在临睡前会察看全班睡觉人数是否严格大于清醒人数，只有这个条件满足时才睡觉，否
 * 则就坚持听课A i 分钟后再次检查这个条件。问经过多长时间后全班都清醒。如果用(A,B,C)描
 * 述一些学生，则图4-11中描述了3个学生(2,4,1)、(1,5,2)和(1,4,3)在每个时刻的行为。
 * <p>
 * 注意：有可能并不存在“全部都清醒”的时刻，此时应输出-1。
 * <p>
 * Extraordinarily Tired Students, ACM/ICPC Xi'an 2006, UVa12108
 */
public class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("----------------------------------------------------------------------");
        solution.case2();
    }

    private class Student {
        private int wake;
        private int sleep;
        private int currentClock;
        private boolean isCurrentSleep;
        private int nextWakeClock = 0;
        private int nextSleepClock = 0;
        private StringBuilder printMe = new StringBuilder();

        public Student(int wake, int sleep, int startClock) {
            this.wake = wake;
            this.sleep = sleep;
            this.currentClock = 1;
            this.isCurrentSleep = startClock > wake;
            if (isCurrentSleep) {
                this.nextWakeClock = wake + sleep + 2 - startClock;
            } else {
                this.nextSleepClock = wake + 2 - startClock;
            }
            appendLog();
        }

        public boolean isCurrentSleep() {
            return this.isCurrentSleep;
        }

        public void nextClock(int wakeSum, int sleepSum) {
            this.currentClock++;
            if (this.isCurrentSleep) {
                if (this.nextWakeClock == this.currentClock) {//醒过来
                    this.isCurrentSleep = false;
                    this.nextSleepClock = this.currentClock + this.wake;
                }
            } else {
                if (this.nextSleepClock == this.currentClock) {//可以睡了吗？
                    if (sleepSum > wakeSum) {//睡觉人数大于清醒人数，开始睡觉
                        this.isCurrentSleep = true;
                        this.nextWakeClock = this.currentClock + this.sleep;
                    } else { //保持清醒
                        this.nextSleepClock = this.currentClock + this.wake;
                    }
                }
            }
            appendLog();
        }

        private void appendLog() {
            printMe = printMe.append(this.isCurrentSleep ? "@" : "#");
            printMe = printMe.append("  ");
            if ((this.currentClock + "").length() == 2) {
                printMe = printMe.append(" ");
            } else if ((this.currentClock + "").length() == 3) {
                printMe = printMe.append("  ");
            }
        }

        public boolean keepSleepInNextClock() {//下一个计时还在睡眠周期内吗？
            return this.nextWakeClock >= this.currentClock + 1;
        }

        public StringBuilder getPrintMe() {
            return printMe;
        }
    }

    private void case1() {
        int[][] s = generatorOne();
        print(s);
        int clock = findAllWake(s);
        System.out.println("case1 clock:" + clock);
    }

    private void case2() {
        int[][] s = {{2, 4, 1}, {1, 5, 2}, {1, 4, 3}};
        print(s);
        int clock = findAllWake(s);
        System.out.println("case2 clock:" + clock);
    }

    private int findAllWake(int[][] s) {
        List<Student> students = new ArrayList<>();
        List<Student> studentsForPrint = new ArrayList<>();
        for (int[] item : s) {
            students.add(new Student(item[0], item[1], item[2]));
            studentsForPrint.add(new Student(item[0], item[1], item[2]));
        }
        int sum = students.size();
        int lessonTime = 60;//一节课至多60分钟
        printWithClock(studentsForPrint, lessonTime);
        int clock = 2;
        while (clock <= lessonTime) {
            if (isAllStudentWake(students)) {
                return clock - 1;
            }
            nextClock(students, sum);
            clock++;
        }
        return -1;
    }

    private void nextClock(List<Student> students, int sum) {
        int sleepSum = keepSleepInNextClockSum(students);
        int wakeSum = sum - sleepSum;
        for (Student student : students) {
            student.nextClock(wakeSum, sleepSum);
        }
    }

    private int keepSleepInNextClockSum(List<Student> students) {
        int sum = 0;
        for (Student student : students) {
            if (student.keepSleepInNextClock()) {
                sum++;
            }
        }
        return sum;
    }

    private boolean isAllStudentWake(List<Student> students) {
        for (Student student : students) {
            if (student.isCurrentSleep()) {
                return false;
            }
        }
        return true;
    }


    private void printWithClock(List<Student> students, int lessonTime) {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= lessonTime; i++) {
            builder = builder.append(i);
            builder = builder.append("  ");
        }
        System.out.println(builder.toString());
        int sum = students.size();
        int s = 1;
        while (s <= lessonTime) {
            nextClock(students, sum);
            s++;
        }
        for (Student student : students) {
            System.out.println(student.getPrintMe().toString());
        }
    }

    private void print(int[][] s) {
        for (int[] item : s) {
            int iLen = item.length;
            System.out.print("(");
            for (int j = 0; j < iLen; j++) {
                System.out.print(item[j]);
                if (j != iLen - 1) {
                    System.out.print(",");
                }
            }
            System.out.println(")");
        }
    }

    private int[][] generatorOne() {
        int sum = Util.getRandomInteger(3, 10);//学生个数
        int[][] result = new int[sum][3];
        for (int i = 0; i < sum; i++) {
            result[i][0] = Util.getRandomInteger(1, 5);
            result[i][1] = Util.getRandomInteger(1, 5);
            result[i][2] = Util.getRandomInteger(1, result[i][0] + result[i][1]);
        }
        return result;
    }
}
