package basic.dataStructure.tree.binary;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/19
 * Time:16:28
 * <p>
 * 二叉树
 */
public final class BinaryTree<T> {
    private Node<T> root;

    public BinaryTree() {

    }

    public Node<T> getRoot() {
        return root;
    }

    public void setRoot(Node<T> root) {
        this.root = root;
    }
}
