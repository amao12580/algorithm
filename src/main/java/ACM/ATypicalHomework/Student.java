package ACM.ATypicalHomework;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/4/11
 * Time:18:10
 */
public class Student implements Comparable<Student> {
    private int SID;
    private String CID;
    private String name;
    private int sumScores;
    private float avgScores;
    private List<Integer> scores;

    public Student(int SID, String CID, String name, List<Integer> scores) {
        this.SID = SID;
        this.CID = CID;
        this.name = name;
        this.scores = scores;
        int sum = 0;
        for (Integer s : scores) {
            sum += s;
        }
        sumScores = sum;
        avgScores = new BigDecimal(sum).divide(new BigDecimal(scores.size()), 1, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public int getSID() {
        return SID;
    }

    public String getName() {
        return name;
    }

    public int getSumScores() {
        return sumScores;
    }

    public List<Integer> getScores() {
        return scores;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Student) {
            Student other = (Student) obj;
            return this.getSID() == other.getSID();
        } else {
            return false;
        }
    }

    public boolean search(String key) {
        return (getSID() + "").equals(key) || getName().equals(key);
    }

    public String toString(Set<Student> studentRanks) {
        Integer[] scoreArray = new Integer[4];
        return "rank:" + getRankIndex(studentRanks) + ",SID:" + SID + ", CID:" + CID + ", name:" + name + ", scores: " + Arrays.toString(scores.toArray(scoreArray)) + ", sumScores:" + sumScores + ", avgScores:" + avgScores;
    }

    @Override
    public int compareTo(Student other) {
        if (other == null) {
            throw new IllegalArgumentException("param is null or invalid type..");
        }
        if (other.getSumScores() > this.getSumScores()) {
            return 1;
        } else if (other.getSumScores() < this.getSumScores()) {
            return -1;
        } else {
            return 0;
        }
    }

    public int getRankIndex(Set<Student> studentRanks) {
        Iterator<Student> iterator = studentRanks.iterator();
        int rankIndex = 0;
        int rankSum = -1;
        while (iterator.hasNext()) {
            Student student = iterator.next();
            if (rankIndex == 0) {
                rankIndex++;
                rankSum = student.getSumScores();
            } else {
                if (student.getSumScores() < rankSum) {
                    rankIndex++;
                    rankSum = student.getSumScores();
                }
            }
            if (student.equals(this)) {
                return rankIndex;
            }
        }
        return rankIndex;
    }
}
