package basic.dataStructure.graph;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/24
 * Time:15:11
 */
public class Line {
    private Point start;
    private Point end;
    private String uniqueCode = null;

    public Line(Point start, Point end) {
        int s = Integer.valueOf(start.getUniqueCode());
        int e = Integer.valueOf(end.getUniqueCode());
        if (s > e) {//低位在前，高位在后
            this.start = end;
            this.end = start;
        } else {
            this.start = start;
            this.end = end;
        }
        uniqueCode = this.start.getUniqueCode() + this.end.getUniqueCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Line) {
            Line other = (Line) o;
            return this.hashCode() == other.hashCode();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return uniqueCode.hashCode();
    }
}
