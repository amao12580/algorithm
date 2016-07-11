package graph.list;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/7/11
 * Time:15:47
 * <p>
 * 邻接表
 */
public class ListGraph {
    private Node[] vertexs = null;

    public ListGraph(int vertex) {//定义边的总数
        vertexs = new Node[vertex];
    }

    public static void main(String[] args) {
        ListGraph edgeGraph = new ListGraph(30);//总共有30个顶点
        edgeGraph.addEdge(1, 5);//1号顶点和5号顶点相连
        edgeGraph.addEdge(5, 8);//5号顶点和8号顶点相连
        edgeGraph.addEdge(8, 12);//8号顶点和12号顶点相连

        edgeGraph.show();

        edgeGraph.deepFirstSearch();

        edgeGraph.breadthFirstSearch();
    }

    /**
     * 深度优先遍历  DFS
     */
    private void deepFirstSearch() {

    }

    /**
     * 广度优先遍历  BFS
     */
    private void breadthFirstSearch() {

    }

    private void show() {
        for (int i = 0; i < vertexs.length; i++) {
            for (int j = 0; vertexs[i] != null && j < vertexs[i].getLatestIndex(); j++) {
                System.out.println(vertexs[i].getNo() + "--------" + vertexs[i].getNexts()[j]);
            }
        }
    }

    private void addEdge(int leftNodeNo, int rightNodeNo) {
        if (vertexs[leftNodeNo] == null) {
            vertexs[leftNodeNo] = new Node(leftNodeNo, rightNodeNo);
        } else {
            vertexs[leftNodeNo].append(rightNodeNo);
        }

        if (vertexs[rightNodeNo] == null) {
            vertexs[rightNodeNo] = new Node(rightNodeNo, leftNodeNo);
        } else {
            vertexs[rightNodeNo].append(leftNodeNo);
        }
    }

    private class Node {
        private int no;

        private int[] nexts = new int[10];
        private int latestIndex = 0;

        public Node(int no, int nextNo) {
            this.no = no;
            append(nextNo);
        }

        private int getNo() {
            return no;
        }

        @Override
        public boolean equals(Object obj) {
            Node node = (Node) obj;
            return this.getNo() == (node.getNo());
        }

        public void append(int nextNo) {
            nexts[latestIndex] = nextNo;
            latestIndex++;
        }

        public int[] getNexts() {
            return nexts;
        }

        public int getLatestIndex() {
            return latestIndex;
        }
    }
}
