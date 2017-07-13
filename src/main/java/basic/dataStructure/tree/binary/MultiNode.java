package basic.dataStructure.tree.binary;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/7/13
 * Time:18:07
 */
public class MultiNode<T> {
    public T value;
    private LinkedList<MultiNode<T>> nexts = new LinkedList<>();

    public MultiNode() {

    }

    public MultiNode(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void appendNextNode(MultiNode<T> next) {
        nexts.add(next);
    }

    /**
     * 是否为叶子节点
     */
    public boolean isLeafNode() {
        return this.nexts.isEmpty();
    }
}
