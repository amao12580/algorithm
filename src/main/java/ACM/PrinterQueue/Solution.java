package ACM.PrinterQueue;

import basic.Util;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/5
 * Time:15:32
 * <p>
 * 学生会里只有一台打印机，但是有很多文件需要打印，因此打印任务不可避免地需要等
 * 待。有些打印任务比较急，有些不那么急，所以每个任务都有一个1～9间的优先级，优先级
 * 越高表示任务越急。
 * 打印机的运作方式如下：首先从打印队列里取出一个任务J，如果队列里有比J更急的任
 * 务，则直接把J放到打印队列尾部，否则打印任务J（此时不会把它放回打印队列）。
 * 输入打印队列中各个任务的优先级以及所关注的任务在队列中的位置（队首位置为
 * 0），输出该任务完成的时刻。所有任务都需要1分钟打印。例如，打印队列为{1, 1, 9, 1, 1,
 * 1}，目前处于队首的任务最终完成时刻为5。
 * <p>
 * Printer Queue, ACM/ICPC NWERC 2006, UVa12100
 */
public class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("-----------------------------------------------------------------");
        Solution solution2 = new Solution();
        solution2.case2();
    }

    private void case1() {
        int[] task = {1, 1, 9, 1, 1, 1};
        int focus = 0;
        print(task, focus);
    }

    private void case2() {
        int[] task = generatorOne();
        int focus = Util.getRandomInteger(0, task.length - 1);
        print(task, focus);
    }

    private int[] generatorOne() {
        int len = Util.getRandomInteger(5, 100);
        int[] result = new int[len];
        for (int i = 0; i < len; i++) {
            result[i] = Util.getRandomInteger(1, 9);
        }
        return result;
    }

    private int time = 0;
    private Integer nextTaskNO = null;

    private void print(int[] tasks, int focus) {
        int len = tasks.length;
        if (nextTaskNO == null) {
            nextTaskNO = len + 1;
        }
        Task[] myTasks = new Task[len];
        for (int i = 0; i < len; i++) {
            myTasks[i] = new Task(i + 1, tasks[i]);
        }
        print(myTasks, myTasks[focus]);
    }

    private void print(Task[] tasks, Task focus) {
        System.out.println("tasks:" + Arrays.toString(tasks) + ",focus:" + focus.toString());
        LinkedList<Task> position = new LinkedList<>();
        List<Task> priority = new ArrayList<>();
        for (Task task : tasks) {
            position.add(task);
            priority.add(task);
        }
        Collections.sort(priority);
        int focusPrintTime = -1;
        while (!position.isEmpty()) {
            Task current = position.pollFirst();
            Task maxPriority = priority.get(priority.size() - 1);
            if (current.compareTo(maxPriority) >= 0) {
                print(current, priority);
                if (focus.equals(current)) {
                    focusPrintTime = time;
                    System.out.println("focus print success,time:" + focusPrintTime);
                }
            } else {
                position.add(current);
            }
        }
        if (focusPrintTime < 0) {
            System.out.println("focus print failed.");
        }
    }

    private void print(Task current, List<Task> priority) {
        time++;
        System.out.println("time:" + time + ",task " + current.toString() + " has printed.");
        Iterator<Task> iterator = priority.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(current)) {
                iterator.remove();
                break;
            }
        }
    }

    private class Task implements Comparable<Task> {
        private int no;
        private int priority;

        public Task(int no, int priority) {
            this.no = no;
            this.priority = priority;
        }

        public int getNo() {
            return no;
        }

        public int getPriority() {
            return priority;
        }

        @Override
        public int compareTo(Task o) {
            if (o == null) {
                throw new IllegalArgumentException();
            }
            return this.getPriority() - o.getPriority();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                throw new IllegalArgumentException();
            }
            return obj instanceof Task && this.getNo() == ((Task) obj).getNo();
        }

        @Override
        public String toString() {
            return "(" + this.getNo() + "," + this.getPriority() + ")";
        }
    }
}
