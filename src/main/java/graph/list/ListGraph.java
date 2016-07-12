package graph.list;

import java.util.Stack;

/**
 * https://segmentfault.com/a/1190000002685939
 * <p>
 * <p>
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/7/11
 * Time:15:47
 * <p>
 * 邻接表
 */
public class ListGraph {
    private Node[] vertexs = null;
    private int numOfVertex = 0;

    public ListGraph(int numOfVertex) {//定义顶点的总数
        this.numOfVertex = numOfVertex;
        this.vertexs = new Node[this.numOfVertex];
    }

    public static void main(String[] args) {
        ListGraph edgeGraph = new ListGraph(7);

//        edgeGraph.addEdge(1, 3);
//        edgeGraph.addEdge(2, 3);
//        edgeGraph.addEdge(1, 4);
//        edgeGraph.addEdge(3, 4);
//        edgeGraph.addEdge(5, 7);
//        edgeGraph.addEdge(1, 6);
//        edgeGraph.addEdge(6, 7);

        edgeGraph.show();

        edgeGraph.deepFirstSearch();
        edgeGraph.breadthFirstSearch();
    }

    public void showVertex(int index) {
        System.out.print(vertexs[index].getNo());
    }

    /**
     * 深度优先遍历  DFS
     * <p>
     * <p>
     * 深度优先遍历，从初始访问结点出发，我们知道初始访问结点可能有多个邻接结点，深度优先遍历的策略就是首先访问第一个邻接结点，然后再以这个被访问的邻接结点作为初始结点，访问它的第一个邻接结点。
     * 总结起来可以这样说：每次都在访问完当前结点后首先访问当前结点的第一个邻接结点。
     * <p>
     * 我们从这里可以看到，这样的访问策略是优先往纵向挖掘深入，而不是对一个结点的所有邻接结点进行横向访问。
     * <p>
     * 具体算法表述如下：
     * <p>
     * 访问初始结点v，并标记结点v为已访问。
     * 查找结点v的第一个邻接结点w。
     * 若w存在，则继续执行4，否则算法结束。
     * 若w未被访问，对w进行深度优先遍历递归（即把w当做另一个v，然后进行步骤123）。
     * 查找结点v的w邻接结点的下一个邻接结点，转到步骤3。
     */
    private void deepFirstSearch() {
        vertexs[0].wasVisited = true;// 从头开始访问
        showVertex(0);
        Stack stack = new Stack();
        stack.push(0);
        /**
         * 1.用peek()方法获取栈顶的顶点 2.试图找到这个顶点的未访问过的邻接点 3.如果没有找到这样的顶点，出栈 4.如果找到，访问之，入栈
         */
        while (!stack.isEmpty()) {
            int index = getUnVisitedVertex(stack.peek());
            if (index == -1)
                stack.pop();
            else {
                vertexs[index].wasVisited = true;
                showVertex(index);
                stack.push(index);
            }
        }
        // 栈为空，遍历结束，标记位重新初始化
        for (int i = 0; i < numOfVertex; i++)
            vertexs[i].wasVisited = false;

    }

    private int getUnVisitedVertex(Object peek) {
        return getUnVisitedVertex(Integer.valueOf(peek.toString()).intValue());
    }

    private int getUnVisitedVertex(int index) {
        Node[] childs = vertexs[index].getChilds();
        for (int i = 0, length = childs.length; i < length; i++)
            if (!childs[i].wasVisited)
                return i;
        return -1;
    }

    /**
     * 广度优先遍历  BFS
     * <p>
     * 类似于一个分层搜索的过程，广度优先遍历需要使用一个队列以保持访问过的结点的顺序，以便按这个顺序来访问这些结点的邻接结点。
     * <p>
     * 具体算法表述如下：
     * <p>
     * 访问初始结点v并标记结点v为已访问。
     * 结点v入队列
     * 当队列非空时，继续执行，否则算法结束。
     * 出队列，取得队头结点u。
     * 查找结点u的第一个邻接结点w。
     * 若结点u的邻接结点w不存在，则转到步骤3；否则循环执行以下三个步骤：
     * 1). 若结点w尚未被访问，则访问结点w并标记为已访问。
     * 2). 结点w入队列
     * 3). 查找结点u的继w邻接结点后的下一个邻接结点w，转到步骤6。
     */
    private void breadthFirstSearch() {

    }

    private void show() {
        int i = 0, vertexsLength = vertexs.length;
        while (i < vertexsLength) {
            Node vertex = vertexs[i];
            for (int j = 0; vertex != null && j < vertex.getLatestIndex(); j++) {
                System.out.println(vertex.getNo() + "--------" + vertex.getChilds()[j]);
            }
            i++;
        }
    }

    private void addEdge(char leftNodeName, char rightNodeName) {

    }

    private class Node {
        private char name;

        private Node[] childs = null;
        private int latestIndex = 0;
        public boolean wasVisited = false;

        public Node(char name, char childName) {
            this.name = name;
            append(childName);
        }

        public Node(char name) {
            this.name = name;
        }

        private char getNo() {
            return name;
        }

        @Override
        public boolean equals(Object obj) {
            Node node = (Node) obj;
            return this.getNo() == (node.getNo());
        }

        public void append(char childNo) {
            if (childs == null) {
                childs = new Node[10];
            }
            childs[latestIndex] = new Node(childNo);
            latestIndex++;
        }

        public Node[] getChilds() {
            return childs;
        }

        public int getLatestIndex() {
            return latestIndex;
        }
    }
}
