package ACM.AlignmentOfCode;

import basic.Util;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/3
 * Time:17:11
 * <p>
 * 输入若干行代码，要求各列单词的左边界对齐且尽量靠左。单词之间至少要空一格。每
 * 个单词不超过80个字符，每行不超过180个字符，一共最多1000行，样例输入与输出如图5-5
 * 所示。
 * <p>
 * Alignment of Code, ACM/ICPC NEERC 2010, UVa1593
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        String[] seeds = solution.generatorOne();
        solution.print(seeds);
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        solution.printPretty(seeds);
    }

    private String[] generatorOne() {
//        int lineSum = Util.getRandomInteger(3, 1000);
        int lineSum = Util.getRandomInteger(3, 100);
        String[] strings = new String[lineSum];
        for (int i = 0; i < lineSum; i++) {
            strings[i] = generatorOneLine();
        }
        return strings;
    }

    private String generatorOneLine() {
//        int lineLen = Util.getRandomInteger(10, 180);
        int lineLen = Util.getRandomInteger(10, 150);
        StringBuilder builder = new StringBuilder();
        int len = 0;
        while (len <= lineLen) {
            String word = generatorOneWord();
            builder = builder.append(word);
            len += word.length();
        }
        return builder.toString();
    }

    private char[] seeds = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private int seedEndIndex = seeds.length - 1;

    private String generatorOneWord() {
//        int len = Util.getRandomInteger(3, 80);
        int len = Util.getRandomInteger(3, 50);
        StringBuilder builder = new StringBuilder(len);
        builder = builder.append(" ");
        if (len % 2 == 0) {
            builder = builder.append(" ");
        }
        for (int i = 0; i < len; i++) {
            builder = builder.append(seeds[Util.getRandomInteger(0, seedEndIndex)]);
        }
        return builder.toString();
    }

    private class Node {
        private Node next;
        private char value;

        public Node(Node next, char value) {
            this.next = next;
            this.value = value;
        }

        public boolean hasNext() {
            return this.next != null;
        }

        public boolean valueIsEmpty() {
            return this.value == empty;
        }

        public Node next() {
            return this.next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            Node node = this;
            stringBuilder = stringBuilder.append(node.value);
            while (node.hasNext()) {
                stringBuilder = stringBuilder.append(node.next().value);
                node = node.next();
            }
            return stringBuilder.toString();
        }
    }

    private char empty = ' ';

    private void printPretty(String[] strings) {
        Node[] nodes = transferToSingleDirectionLinkList(strings);
        format(nodes);
        print(nodes);
    }

    private void print(Object[] objects) {
        for (Object obj : objects) {
            System.out.println(obj.toString());
        }
    }

    private void format(Node[] nodes) {
        if (nodes == null) {
            return;
        }
        int len = nodes.length;
        //处理头部
        for (int i = 0; i < len; i++) {
            nodes[i] = formatHead(nodes[i]);
        }
        //处理中间部分
        Node[] temps = nextNodes(nodes);
        if (temps == null) {
            return;
        }
        while (!isAllNextNodeIsEmpty(temps)) {
            temps = nextNodes(temps);
            if (temps == null) {
                return;
            }
        }
        temps = nextNodes(temps);
        if (temps == null) {
            return;
        }
        format(temps);
    }

    private Node[] nextNodes(Node[] nodes) {
        int len = nodes.length;
        Node[] result = new Node[len];
        int index = -1;
        for (Node node : nodes) {
            if (node != null) {
                index++;
                result[index] = node.next();
            }
        }
        if (index < 0) {
            return null;
        }
        if (index < len - 1) {
            return resizeArrayByEndNull(result, index);
        }
        return result;
    }

    public Node[] resizeArrayByEndNull(Node[] nodes, int endIndex) {
        Node[] result = new Node[endIndex + 1];
        System.arraycopy(nodes, 0, result, 0, endIndex + 1);
        return result;
    }

    private boolean isAllNextNodeIsEmpty(Node[] nodes) {
        boolean flag = true;
        if (nodes == null) {
            return true;
        }
        int len = nodes.length;
        Node[] nextNodeValueIsEmpty = new Node[len];
        int index = 0;
        for (Node node : nodes) {
            if (node != null && node.hasNext()) {
                if (node.next().valueIsEmpty()) {
                    nextNodeValueIsEmpty[index] = node;
                    index++;
                } else {
                    flag = false;
                }
            }
        }
        if (!flag) {
            for (int i = 0; i < index; i++) {
                Node node = nextNodeValueIsEmpty[i];
                node.setNext(new Node(node.next(), empty));
            }
        }
        return flag;
    }

    private Node formatHead(Node node) {
        if (node == null) {
            return null;
        }
        if (node.valueIsEmpty()) {
            if (node.hasNext()) {
                if (!node.next().valueIsEmpty()) {
                    return node;
                } else {
                    node.setNext(node.next().next());
                    return formatHead(node);
                }
            } else {
                return node;
            }
        } else {
            return new Node(node, empty);
        }
    }

    private Node[] transferToSingleDirectionLinkList(String[] strings) {
        Node[] nodes = new Node[strings.length];
        int index = 0;
        for (String string : strings) {
            if (string == null || string.isEmpty()) {
                continue;
            }
            char[] chars = string.toCharArray();
            int len = chars.length;
            final Node end = new Node(null, chars[len - 1]);
            Node next = end;
            Node current = null;
            for (int i = len - 2; i >= 0; i--) {
                current = new Node(next, chars[i]);
                next = current;
            }
            nodes[index] = current == null ? end : current;
            index++;
        }
        //TODO:Resize nodes array length by final index.
        return nodes;
    }
}
