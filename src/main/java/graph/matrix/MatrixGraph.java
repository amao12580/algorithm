package graph.matrix;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/7/11
 * Time:15:47
 * <p>
 * 邻接矩阵
 */
public class MatrixGraph {
    private List<String> vertexList = null;//存储点的链表
    private int[][] edges;//邻接矩阵，用来存储边
    private int numOfEdges;//边的数目

    /**
     * @param n 顶点的个数
     */
    public MatrixGraph(int n) {
        //初始化矩阵，一维数组，和边的数目
        edges = new int[n][n];
        vertexList = new ArrayList(n);
        numOfEdges = 0;
    }

    //得到结点的个数
    public int getNumOfVertex() {
        return vertexList.size();
    }

    //得到边的数目
    public int getNumOfEdges() {
        return numOfEdges;
    }

    //返回结点i的数据
    public Object getValueByIndex(int i) {
        return vertexList.get(i);
    }

    //返回边的权值
    public int getWeight(int v1, int v2) {
        return edges[v1][v2];
    }

    //插入结点
    public void insertVertex(String vertex) {
        vertexList.add(vertexList.size(), vertex);
    }

    //插入边
    public void insertEdge(int v1, int v2, int weight) {
        edges[v1][v2] = weight;
        numOfEdges++;
    }

    //删除边
    public void deleteEdge(int v1, int v2) {
        edges[v1][v2] = 0;
        numOfEdges--;
    }

    //得到第一个邻接结点的下标
    public int getFirstNeighbor(int index) {
        for (int j = 0; j < vertexList.size(); j++) {
            if (edges[index][j] > 0) {
                return j;
            }
        }
        return -1;
    }

    //根据前一个邻接结点的下标来取得下一个邻接结点
    public int getNextNeighbor(int v1, int v2) {
        for (int j = v2 + 1; j < vertexList.size(); j++) {
            if (edges[v1][j] > 0) {
                return j;
            }
        }
        return -1;
    }

    private void depthFirstSearch(boolean[] isVisited, int i) {
        //首先访问该结点，在控制台打印出来
        System.out.print(getValueByIndex(i) + "  ");
        //置该结点为已访问
        isVisited[i] = true;

        int w = getFirstNeighbor(i);
        while (w != -1) {
            if (!isVisited[w]) {
                depthFirstSearch(isVisited, w);
            }
            w = getNextNeighbor(i, w);
        }
    }

    public void depthFirstSearch() {
        boolean[] isVisited = new boolean[getNumOfVertex()];
        for (int i = 0; i < getNumOfVertex(); i++) {
            //因为对于非连通图来说，并不是通过一个结点就一定可以遍历所有结点的。
            if (!isVisited[i]) {
                depthFirstSearch(isVisited, i);
            }
        }
    }

    private void breadthFirstSearch(boolean[] isVisited, int i) {
        int u, w = 0;
        LinkedList<Integer> queue = new LinkedList<>();

        //访问结点i
        System.out.print(getValueByIndex(i) + "  ");
        isVisited[i] = true;
        //结点入队列
        queue.addLast(w);
        while (!queue.isEmpty()) {
            u = queue.removeFirst();
            w = getFirstNeighbor(u);
            while (w != -1) {
                if (!isVisited[w]) {
                    //访问该结点
                    System.out.print(getValueByIndex(w) + "  ");
                    //标记已被访问
                    isVisited[w] = true;
                    //入队列
                    queue.addLast(w);
                }
                //寻找下一个邻接结点
                w = getNextNeighbor(u, w);
            }
        }
    }

    public void breadthFirstSearch() {
        boolean[] isVisited = new boolean[getNumOfVertex()];
        for (int i = 0; i < getNumOfVertex(); i++) {
            if (!isVisited[i]) {
                breadthFirstSearch(isVisited, i);
            }
        }
    }

    public static void main(String[] args) {
//        System.out.println(Thread.currentThread().getId());
//        List list = new ArrayList<>();
//        list.add(1);
//        list.add("1");
//        list.add(false);
//        list.add('A');
//        Set set = new HashSet<>();
//        set.add(1);
//        set.add(1);
//        set.add("2");
//        set.add(true);
//        set.add('A');
//        set.add('A');
//        set.add(1l);
//        System.out.println(list.toString());
//        System.out.println(set.toString());
        int n = 8;//分别代表结点个数和边的数目
        String labels[] = {"1", "2", "3", "4", "5", "6", "7", "8"};//结点的标识
        MatrixGraph graph = new MatrixGraph(n);
        for (String label : labels) {
            graph.insertVertex(label);//插入结点
        }
        graph.insertEdge(0, 1, 1);
        graph.insertEdge(0, 2, 1);
        graph.insertEdge(1, 3, 1);
        graph.insertEdge(1, 4, 1);
        graph.insertEdge(3, 7, 1);
        graph.insertEdge(4, 7, 1);
        graph.insertEdge(2, 5, 1);
        graph.insertEdge(2, 6, 1);
        graph.insertEdge(5, 6, 1);
        graph.insertEdge(1, 0, 1);
        graph.insertEdge(2, 0, 1);
        graph.insertEdge(3, 1, 1);
        graph.insertEdge(4, 1, 1);
        graph.insertEdge(7, 3, 1);
        graph.insertEdge(7, 4, 1);
        graph.insertEdge(4, 2, 1);
        graph.insertEdge(5, 2, 1);
        graph.insertEdge(6, 5, 1);

        System.out.println("depthFirstSearch");
        graph.depthFirstSearch();
        System.out.println();
        System.out.println("breadthFirstSearch");
        graph.breadthFirstSearch();
    }
}
