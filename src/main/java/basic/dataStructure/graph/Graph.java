package basic.dataStructure.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/24
 * Time:15:09
 */
public class Graph {
    private Set<Point> points = new HashSet<>();
    private Set<Line> lines = new HashSet<>();

    public Set<Point> getPoints() {
        return points;
    }

    public Set<Line> getLines() {
        return lines;
    }

    public boolean isEmptyPoints() {
        return this.getPoints().isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Graph graph = (Graph) o;
        return !(points != null ? !points.equals(graph.points) : graph.points != null) && !(lines != null ? !lines.equals(graph.lines) : graph.lines != null);
    }

    /**
     * 判断两个图之间是否存在重叠
     * <p>
     * 计算两个图的顶点集合是否存在交集即可
     */
    public boolean isOverlap(Graph other) {
        if (!(!this.isEmptyPoints() && !other.isEmptyPoints())) {
            return false;
        }
        Set<Point> thisPoints = new HashSet<>();
        thisPoints.addAll(this.getPoints());
        thisPoints.retainAll(other.getPoints());
        boolean result = !thisPoints.isEmpty();
        thisPoints.clear();
        return result;
    }

    public void addPoint(Point point) {
        this.points.add(point);
    }
}
