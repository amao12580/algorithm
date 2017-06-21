package basic.dataStructure.tree.binary;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/19
 * Time:16:54
 */
public class Node<T> {
    public T value;
    private Node<T> left;
    private Node<T> right;
    private char[] notNullValue;

    public Node() {

    }

    public Node(T value) {
        this.value = value;
    }

    public Node(Node<T> left, Node<T> right) {
        this.left = left;
        this.right = right;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Node<T> getLeft() {
        return left;
    }

    public void setLeft(Node<T> left) {
        this.left = left;
    }

    public Node<T> getRight() {
        return right;
    }

    public void setRight(Node<T> right) {
        this.right = right;
    }

    /**
     * 是否为叶子节点
     */
    public boolean isLeafNode() {
        return this.left == null && this.right == null;
    }

}
