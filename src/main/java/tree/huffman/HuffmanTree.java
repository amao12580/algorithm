package tree.huffman;

import basic.Util;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/7/20
 * Time:17:33
 * <p>
 * http://coolshell.cn/articles/7459.html
 * <p>
 * http://www.cnblogs.com/mcgrady/p/3329825.html
 * <p>
 * 定义：
 * <p>
 * 哈夫曼树是一种带权路径长度最短的二叉树，也称为最优二叉树。
 * <p>
 * 我们可以看到出现频率越多的会越在上层，编码也越短，出现频率越少的就越在下层，编码也越长。
 * <p>
 * 通过这个二叉树建立我们Huffman编码和解码的字典表。
 */
public class HuffmanTree {
    private Map<Character, Integer> rateAnalyze;
    private Map<Character, String> dictionary;
    private String plainText;

    /**
     * 解码
     *
     * @param ciphertext 密文
     * @return 明文
     */
    private String decoding(String ciphertext) {
        String temp = ciphertext;
        StringBuilder result = new StringBuilder();
        while (!temp.isEmpty()) {
            for (Map.Entry<Character, String> entry : dictionary.entrySet()) {
                if (temp.startsWith(String.valueOf(entry.getValue()))) {
                    result.append(entry.getKey());
                    temp = temp.replaceFirst(entry.getValue(), "");
                }
            }
        }
        return result.toString();
    }

    /**
     * 编码
     *
     * @param plainText 明文
     * @return 密文
     */
    private String encoding(String plainText) {
        this.plainText = plainText;
        analyzeCharacterRate();
        TreeNode top = buildHuffmanTree();
        buildHuffmanDictionary(top);
        return buildHuffmanEncoding();
    }

    private void buildHuffmanDictionary(TreeNode top) {
        dictionary = new HashMap<>(rateAnalyze.keySet().size());
        scanBinaryTree(top, "");
//        System.out.println("dictionary:" + Util.toJson(dictionary));
    }


    private void scanBinaryTree(TreeNode parent, String code) {
        if (parent.getLeft() != null) {
            scanBinaryTree(parent.getLeft(), code + "0");
        }
        if (parent.getRight() != null) {
            scanBinaryTree(parent.getRight(), code + "1");
        }
        if (parent.getValue() != null) {
            dictionary.put(parent.getValue(), code);
        }
    }

    private String buildHuffmanEncoding() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0, len = plainText.length(); i < len; i++) {
            stringBuilder.append(dictionary.get(plainText.charAt(i)));
        }
        return stringBuilder.toString();
    }

    /**
     * 分析字符串中，每个字符出现的次数
     */
    private void analyzeCharacterRate() {
        rateAnalyze = new LinkedHashMap<>();
        for (int i = 0, len = plainText.length(); i < len; i++) {
            Character character = plainText.charAt(i);
            Integer offset = rateAnalyze.get(character);
            offset = offset == null ? 0 : offset;
            rateAnalyze.put(character, offset + 1);
        }
//        System.out.println("rateAnalyze:" + rateAnalyze.toString());
    }

    private TreeNode buildHuffmanTree() {
        //构建优先队列
        //出现次数多的，排在后面。
        //相同次数的，按照原始出现的顺序排列
        Queue<TreeNode> queue = new PriorityQueue<>(rateAnalyze.size());
        queue.addAll(rateAnalyze.entrySet().stream().map(entry -> new TreeNode(entry.getKey(), entry.getValue())).collect(Collectors.toList()));
//        printQueue(queue);
        TreeNode top = null;
        while (!queue.isEmpty()) {
            TreeNode parent;
            TreeNode left = queue.poll();
            TreeNode right = queue.poll();
            if (right == null) {
                top = left;
                break;
            }
            if (left.getScore() < right.getScore()) {//保持得分少的节点在树的左侧
                parent = new TreeNode(left, right);
            } else {
                parent = new TreeNode(right, left);
            }
            queue.add(parent);
        }
        return top;
    }

    private void printQueue(Queue<TreeNode> queue) {
        System.out.println("queue:" + Util.toJson(queue));
        while (!queue.isEmpty()) {
            System.out.println("node:" + Util.toJson(queue.poll()));
        }
    }

    public int getFirstIndex(Character value) {
        if (value != null) {
            Set<Character> set = rateAnalyze.keySet();
            Iterator iterator = set.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                if (value == iterator.next()) {
                    return index;
                }
                index++;
            }
        }
        return -1;
    }

    public Map<Character, String> getDictionary() {
        return dictionary;
    }

    class TreeNode implements Comparable<TreeNode> {
        private TreeNode left;
        private TreeNode right;
        private Character value;//字符值
        private int score;//字符出现的频率

        public TreeNode(char value, int score) {
            if (score <= 0) {
                throw new IllegalArgumentException("score 需要大于零.");
            }
            this.value = value;
            this.score = score;
        }

        public TreeNode(TreeNode left, TreeNode right) {
            this.left = left;
            this.right = right;
            this.score = getScore(left, right);
        }

        private int getScore(TreeNode left, TreeNode right) {
            int score = 0;
            if (left != null) {
                score += left.getScore();
            }
            if (right != null) {
                score += right.getScore();
            }
            return score;
        }

        public int getScore() {
            return score;
        }

        public Character getValue() {
            return value;
        }

        public TreeNode getLeft() {
            return left;
        }

        public TreeNode getRight() {
            return right;
        }

        @Override
        public int compareTo(TreeNode other) {
            if (other == null) {
                throw new IllegalArgumentException("不接受Null参数比较");
            }
            if (this.getScore() == other.getScore()) {
                int thisIndex = getFirstIndex(this.getValue());
                int otherIndex = getFirstIndex(other.getValue());
                if (thisIndex > otherIndex) {
                    return 1;
                }
                if (thisIndex == otherIndex) {
                    return 0;
                }
                if (thisIndex < otherIndex) {
                    return -1;
                }
            }
            if (this.getScore() > other.getScore()) {
                return 1;
            }
            return -1;
        }
    }


    public static void main(String[] args) {
        String plainText = Util.generateMixedString(20);
        //String plainText = "beep boop beer!";
        System.out.println("plainText:" + plainText);
        String originEncodingValue = Util.toBinary(plainText);
        System.out.println("originEncodingValue:" + originEncodingValue + ",length:" + originEncodingValue.length());
        HuffmanTree huffmanTree = new HuffmanTree();
        String encodingValue = huffmanTree.encoding(plainText);
        System.out.println("huffmanEncodingValue:" + encodingValue + ",length:" + encodingValue.length());
        System.out.println("huffmanDictionary:" + huffmanTree.getDictionary().toString());
        //压缩比率
        System.out.println("compression ratio:" + ((originEncodingValue.length() - encodingValue.length()) * 100 / originEncodingValue.length()) + "%");
        String decodingPlainText = huffmanTree.decoding(encodingValue);
        System.out.println("decodingPlainText:" + decodingPlainText);
        // JDK 默认不开启assert，如何打开，VM加上'-ea'。详情：http://www.hankcs.com/program/java-assert.html
        boolean isOpen = false;
        assert isOpen = true;             //如果开启了断言，会将isOpen的值改为true
        System.out.println("是否开启了断言" + isOpen);  //打印是否开启了断言
        assert decodingPlainText.equals(plainText) : "decoding was failed";
    }
}
