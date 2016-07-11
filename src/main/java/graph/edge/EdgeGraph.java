package graph.edge;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/7/11
 * Time:15:47
 *
 * 边的集合
 */
public class EdgeGraph {
    private Node[][] edges = null;
    private int latestIndex = 0;

    public EdgeGraph(int edgeSum) {//定义边的总数
        edges = new Node[edgeSum][2];
    }

    public static void main(String[] args) {
        EdgeGraph edgeGraph = new EdgeGraph(10);//总共有10条边
        edgeGraph.addEdge(1, 5);//1号顶点和5号顶点相连
        edgeGraph.addEdge(5, 8);//5号顶点和8号顶点相连
        edgeGraph.addEdge(8, 12);//8号顶点和12号顶点相连

        edgeGraph.show();
    }

    private void show() {
        for (int i = 0; i < latestIndex; i++) {
            Node[] nodes = edges[i];
            System.out.println(nodes[0].toString() + "--------" + nodes[1].toString());
        }
    }

    private void addEdge(int leftNodeNo, int rightNodeNo) {
        Node left = new Node(leftNodeNo);
        Node right = new Node(rightNodeNo);
        edges[latestIndex][0] = left;
        edges[latestIndex][1] = right;
        latestIndex++;
    }

    private class Node {
        private int no;

        public Node(int no) {
            this.no = no;
        }

        private int getNo() {
            return no;
        }

        @Override
        public boolean equals(Object obj) {
            Node node = (Node) obj;
            return this.getNo() == (node.getNo());
        }

        @Override
        public String toString() {
            return this.getNo() + "";
        }
    }
}
