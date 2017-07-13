package basic.dataStructure.tree.binary;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/7/13
 * Time:18:04
 * <p>
 * 多叉树
 */
public final class MultiTree<T> {
    private MultiNode<T> root;

    public MultiTree() {

    }

    public MultiNode<T> getRoot() {
        return root;
    }

    public void setRoot(MultiNode<T> root) {
        this.root = root;
    }
}
