package graph.matrix;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/7/11
 * Time:15:47
 * <p>
 * 邻接矩阵
 */
public class MatrixGraph {
    private int[][] edges = null;

    public MatrixGraph(int vertex) {//定义边的总数
        edges = new int[vertex][vertex];
    }

    public static void main(String[] args) {
        MatrixGraph edgeGraph = new MatrixGraph(20);//总共有20个顶点
        edgeGraph.addEdge(1, 5);//1号顶点和5号顶点相连
        edgeGraph.addEdge(5, 8);//5号顶点和8号顶点相连
        edgeGraph.addEdge(8, 12);//8号顶点和12号顶点相连

        edgeGraph.show();
    }

    private void show() {
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges[i].length; j++) {
                if (edges[i][j] == 1) {
                    System.out.println(i + "--------" + j);
                }
            }
        }
    }

    private void addEdge(int leftNodeNo, int rightNodeNo) {
        edges[leftNodeNo][rightNodeNo] = 1;
        edges[rightNodeNo][leftNodeNo] = 1;
    }
}
