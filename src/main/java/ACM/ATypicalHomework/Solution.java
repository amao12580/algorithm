package ACM.ATypicalHomework;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/4/10
 * Time:17:15
 * <p>
 * 编写一个成绩管理系统（SPMS）。最多有100个学生，每个学生有如下属性。
 * SID：学生编号，包含10位数字。
 * CID：班级编号，为不超过20的正整数。
 * 姓名：不超过10的字母和数字组成，第一个字符为大写字母。名字中不能有空白字符。
 * 4门课程（语文、数学、英语、编程）成绩，均为不超过100的非负整数。
 * 进入SPMS后，应显示主菜单：
 * Welcome to Student Performance Management System (SPMS).
 * 1 - Add
 * 2 - Remove
 * 3 - Query
 * 4 - Show ranking
 * 5 - Show Statistics
 * 0 - Exit
 * 选择1之后，会出现添加学生记录的提示信息：
 * Please enter the SID, CID, name and four scores. Enter 0 to finish.
 * 然后等待输入。本题保证输入总是合法的（不会有非法的SID、CID，并且恰好有4个分
 * 数等），但可能会输入重复SID。在这种情况下，需要输出一行提示：
 * Duplicated SID.
 * 不过名字是可以重复的。你的程序应当不停地打印前述提示信息，直到用户输入单个
 * 0。然后应当再次打印主菜单。
 * 选择2之后，会出现如下提示信息：
 * Please enter SID or name. Enter 0 to finish.
 * 然后等待输入，在数据库中删除能匹配上述SID或者名字的所有学生，并且打印如下信
 * 息（xx可以等于0）：
 * xx student(s) removed.
 * 你的程序应当不停地打印前述提示信息，直到用户输入单个0，然后再次打印主菜单。
 * 选择3之后，会出现如下提示信息：
 * Please enter SID or name. Enter 0 to finish.
 * 然后等待输入。如果数据库中没有能匹配上述SID或者名字的学生，什么都不要做；否
 * 则输出所有满足条件的学生，按照进入数据库的顺序排列。输出格式和添加的格式相同，但
 * 增加3列：年级排名（第一列）、总分和平均分（最后两列）。所有班级中总分最高的学生
 * 获得第1名，如果有两个学生并列第2名，则下一个学生的排名为4（而非3）。你的程序应当
 * 不停地打印前述提示信息，直到用户输入单个0。然后应当再次打印主菜单。
 * 选择4之后，会出现如下提示信息：
 * Showing the ranklist hurts students' self-esteem. Don't do that.
 * 然后自动返回主菜单。
 * 选择5之后，会出现如下提示信息：
 * Chinese
 * Average Score: xx.xx
 * Number of passed students: xx
 * Number of failed students: xx
 * …（为了节约篇幅，此处省略了Mathematics、English和Programming的统计信息）
 * Overall:
 * Number of students who passed all subjects: xx
 * Number of students who passed 3 or more subjects: xx
 * Number of students who passed 2 or more subjects: xx
 * Number of students who passed 1 or more subjects: xx
 * Number of students who failed all subjects: xx
 * 然后自动回到主菜单。
 * 选择0之后，程序终止。注意，单科成绩和总分都应格式化为整数，但平均分应恰好保
 * 留两位小数。
 * 提示：这个程序适合直接运行，用键盘与之交互，然后从屏幕中看到输出信息。但正因
 * 为如此，作为一道算法竞赛的题目，其输出看上去会比较乱。
 */
public class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        List<Integer> score = new ArrayList<>();
        score.add(12);
        score.add(24);
        score.add(23);
        score.add(79);
        Student student = new Student(100, "200", "zs", score);
        score = new ArrayList<>();
        score.add(99);
        score.add(88);
        score.add(77);
        score.add(66);
        Student student2 = new Student(200, "200", "zs", score);
        score = new ArrayList<>();
        score.add(77);
        score.add(45);
        score.add(69);
        score.add(89);
        Student student3 = new Student(300, "201", "zs", score);
        solution.SPMS(new Command(1, student));
        solution.SPMS(new Command(1, student2));
        solution.SPMS(new Command(1, student3));
        String[] keys = {"zs"};
        solution.SPMS(new Command(3, keys));
        String[] keys2 = {"100"};
        solution.SPMS(new Command(2, keys2));
        solution.SPMS(new Command(3, keys));
        score = new ArrayList<>();
        score.add(45);
        score.add(66);
        score.add(73);
        score.add(23);
        Student student4 = new Student(400, "201", "zs", score);
        solution.SPMS(new Command(1, student4));
        solution.SPMS(new Command(3, keys));
        solution.SPMS(new Command(4, null));
        solution.SPMS(new Command(5, null));
    }

    private static Set<Student> students = new LinkedHashSet<>();
    private static Set<Student> studentRanks = new TreeSet<>();

    private void SPMS(Command... commands) {
        for (Command command : commands) {
            switch (command.getCode()) {
                case 1:
                    Add(command.getMsg());
                    break;
                case 2:
                    Remove(command.getMsg());
                    System.out.println("------------------------------------------------------------------");
                    break;
                case 3:
                    Query(command.getMsg());
                    System.out.println("------------------------------------------------------------------");
                    break;
                case 4:
                    ShowRanking(command.getMsg());
                    System.out.println("------------------------------------------------------------------");
                    break;
                case 5:
                    ShowStatistics(command.getMsg());
                    System.out.println("------------------------------------------------------------------");
                    break;
                case 6:
                    Exit();
            }
        }
    }

    private void Exit() {

    }

    private void ShowStatistics(Object msg) {
        ShowStatistics(0, "语文");
        ShowStatistics(1, "数学");
        ShowStatistics(2, "英语");
        ShowStatistics(3, "编程");
    }

    private void ShowStatistics(int index, String name) {
        int sum = 0;
        int pass = 0;
        int fail = 0;
        for (Student student : students) {
            List<Integer> scores = student.getScores();
            sum += scores.get(index);
            if (scores.get(index) >= 60) {
                pass++;
            } else {
                fail++;
            }
        }
        System.out.println(name);
        System.out.println("Average Score:" + new BigDecimal(sum).divide(new BigDecimal(students.size()), 1, BigDecimal.ROUND_HALF_UP).floatValue());
        System.out.println("Number of passed students:" + pass);
        System.out.println("Number of failed students:" + fail);
    }

    private void ShowRanking(Object msg) {
        System.out.println("Showing the ranklist hurts students' self-esteem. Don't do that.");
    }

    private void Query(Object msg) {
        if (msg instanceof String[]) {
            String[] keys = (String[]) msg;
            for (String key : keys) {
                students.stream().filter(student -> student.search(key)).forEach(student -> System.out.println(student.toString(studentRanks)));
            }
        }
    }

    private void Remove(Object msg) {
        if (msg instanceof String[]) {
            String[] keys = (String[]) msg;
            int removed = 0;
            for (String key : keys) {
                Iterator<Student> iterator = students.iterator();
                while (iterator.hasNext()) {
                    Student student = iterator.next();
                    if (student.search(key)) {
                        iterator.remove();
                        Iterator<Student> iterator2 = studentRanks.iterator();
                        while (iterator2.hasNext() && iterator2.next().equals(student)) {
                            iterator2.remove();
                            break;
                        }
                        removed++;
                    }
                }
            }
            System.out.println(removed + " student" + (removed > 1 ? "s" : "") + " removed.");
        }
    }

    private void Add(Object msg) {
        if (msg instanceof Student) {
            Student student = (Student) msg;
            if (!students.add(student) || !studentRanks.add(student)) {
                System.out.println("Duplicated SID.");
            }
        }
    }
}
