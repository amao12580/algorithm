package basic.dataStructure.graph;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/24
 * Time:15:08
 */
public class Point {
    private int rowIndex;
    private int columnIndex;
    private String uniqueCode = null;

    public Point(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || columnIndex < 0) {
            throw new IllegalArgumentException();
        }
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.uniqueCode = rowIndex + "" + columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Point) {
            Point other = (Point) o;
            return uniqueCode.equals(other.getUniqueCode());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return uniqueCode.hashCode();
    }

    public String getUniqueCode() {
        return uniqueCode;
    }
}
