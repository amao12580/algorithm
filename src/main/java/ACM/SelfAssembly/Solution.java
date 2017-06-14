package ACM.SelfAssembly;

import basic.Util;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/6/8
 * Time:10:12
 * <p>
 * 有n（n≤40000）种边上带标号的正方形。每条边上的标号要么为一个大写字母后面跟着
 * 一个加号或减号，要么为数字00。当且仅当两条边的字母相同且符号相反时，两条边能拼在
 * 一起（00不能和任何边拼在一起，包括另一条标号为00的边）。
 * 假设输入的每种正方形都有无穷多种，而且可以旋转和翻转，你的任务是判断能否组成
 * 一个无限大的结构。每条边要么悬空（不和任何边相邻），要么和一个上述可拼接的边相
 * 邻。如图6-17（a）所示是3个正方形，图6-17（b）所示边是它们组成的一个合法结构（但大
 * 小有限）。
 * <p>
 * Self-Assembly, ACM/ICPC World Finals 2013, UVa 1572
 * <p>
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=onlinejudge&page=show_problem&problem=4364
 * <p>
 * Sample Input
 * 3
 * A+00A+A+ 00B+D+A- B-C+00C+
 * 1
 * K+K-Q+Q-
 * <p>
 * Sample Output
 * bounded
 * unbounded
 */
public class Solution {
    public static void main(String[] args) {
        new Solution().case1();
        System.out.println("---------------------------------------");
        new Solution().case2();
        System.out.println("---------------------------------------");
        new Solution().case3();
    }

    private void case1() {
        int len = Util.getRandomInteger(10, 20);
//        int len = Util.getRandomInteger(10, 40000);
        String[][] desc = new String[len][];
        for (int i = 0; i < len; i++) {
            desc[i] = generateOne();
        }
        check(desc);
    }

    private String[] generateOne() {
        String[] piece = new String[4];
        for (int i = 0; i < 4; i++) {
            piece[i] = generateOnePiece();
        }
        return piece;
    }

    private String generateOnePiece() {
        return Util.getRandomBoolean() ? Util.UPLETTERCHAR.charAt(Util.getRandomInteger(0, 25)) + (Util.getRandomBoolean() ? "+" : "-") : "00";
    }

    private void case2() {
        String[][] desc = {{"A+", "00", "A+", "A+"},
                {"00", "B+", "D+", "A-"},
                {"B-", "C+", "00", "C+"}};
        check(desc);
    }

    private void case3() {
        String[][] desc = {{"K+", "K-", "Q+", "Q-"}};
        check(desc);
    }

    Map<String, Integer> key2Index = new HashMap<>();

    Map<String, Integer> existKey2Index = new HashMap<>();

    Map<String, Set<Block>> existKey2Block = new HashMap<>();

    LinkedList<String> keys = getKeys();

    private void check(String[][] desc) {
        System.out.println(Arrays.deepToString(desc));
        int p;
        for (String[] array : desc) {
            for (String key : array) {
                p = keys.indexOf(key);
                if (p >= 0) {
                    existKey2Index.put(key, keys.indexOf(key));
                }
            }
        }
        List<Block> blocks = new ArrayList<>();
        for (String[] array : desc) {
            blocks.add(new Block(array));
        }
        String value;
        Set<Block> set;
        for (Block block : blocks) {
            List<Face> faces = block.getFaces();
            for (Face face : faces) {
                value = face.getMyself();
                if ("00".equals(value)) {
                    continue;
                }
                set = existKey2Block.get(value);
                if (set == null) {
                    set = new HashSet<>();
                    set.add(block);
                    existKey2Block.put(value, set);
                } else {
                    set.add(block);
                }
            }
        }
        if (check(blocks)) {//DAG存在环
            System.out.println("unbounded");
        } else {
            System.out.println("bounded");
        }
    }

    private boolean check(List<Block> blocks) {
        for (Block block : blocks) {
            if (check(block)) {
                return true;
            }
        }
        return false;
    }


    private boolean check(Block block) {
        Set<Block> blocks = new HashSet<>();
        blocks.add(block);
        List<Face> faces = block.getFaces();
        //翻转三次
        List<Face> newFaces = new LinkedList<>();
        newFaces.add(faces.get(0));
        newFaces.add(faces.get(3));
        newFaces.add(faces.get(2));
        newFaces.add(faces.get(1));
        blocks.add(new Block(newFaces));

        newFaces.add(faces.get(2));
        newFaces.add(faces.get(1));
        newFaces.add(faces.get(0));
        newFaces.add(faces.get(3));
        blocks.add(new Block(newFaces));

        newFaces.add(faces.get(2));
        newFaces.add(faces.get(3));
        newFaces.add(faces.get(0));
        newFaces.add(faces.get(1));
        blocks.add(new Block(newFaces));

        for (Block current : blocks) {
            if (check(current, current)) {
                return true;
            }
        }
        return false;
    }

    private boolean check(Block origin, Block current) {
        for (int i = 0; i < 4; i++) {
            if (check(origin, current, i, new Point(0, 0), new HashMap<>())) {
                return true;
            }
        }
        return false;
    }

    private boolean check(Block origin, Block current, int faceIndex, Point point, HashMap<String, Block> map) {
//        System.out.println(origin.toString() + "," + current.toString() + "," + faceIndex + "," + point.toString());
        String other = current.getFaces().get(faceIndex).getOther();
        if (other == null) {
            return false;
        }
        Point nextPoint = getNextPoint(point, faceIndex);
        if (map.containsKey(nextPoint.toString())) {
            return false;
        }
        Set<Block> matchs = existKey2Block.get(other);
        if (matchs == null) {
            return false;
        }
        int oppositeIndex = faceIndex + 2 >= 4 ? faceIndex - 2 : faceIndex + 2;
        HashMap<String, Block> myMap;
        for (Block match : matchs) {
            Set<Block> nexts = getNextBlock(match, other, faceIndex);
            if (!nexts.isEmpty()) {
                for (Block next : nexts) {
                    if (!lookAround(nextPoint, next, map, faceIndex)) {
                        continue;
                    }
                    if (next.equals(origin)) {
                        return true;
                    } else {
                        myMap = copyOf(map);
                        myMap.put(nextPoint.toString(), next);
                        for (int i = 0; i < 4; i++) {
                            if (i == oppositeIndex) {
                                continue;
                            }
                            if (check(origin, next, i, nextPoint, myMap)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean lookAround(Point point, Block block, HashMap<String, Block> map, int faceIndex) {
        int skipIndex = faceIndex + 2 >= 4 ? faceIndex - 2 : faceIndex + 2;
        Block next;
        for (int i = 0; i < 4; i++) {
            if (i == skipIndex) {
                continue;
            }
            next = map.get(getNextPoint(point, i).toString());
            if (next == null) {
                continue;
            }
            if (!match(next, block, i)) {
                return false;
            }
        }
        return true;
    }

    private boolean match(Block next, Block block, int faceIndex) {
        String thisValue = block.getFaces().get(faceIndex).getMyself();
        int otherIndex = faceIndex + 2 >= 4 ? faceIndex - 2 : faceIndex + 2;
        String otherValue = next.getFaces().get(otherIndex).getMyself();
        if (thisValue == null || otherValue == null) {
            return false;
        }
        if ("00".equals(thisValue) || "00".equals(otherValue)) {
            return false;
        }
        if (thisValue.charAt(0) == otherValue.charAt(0)) {
            if (thisValue.charAt(1) == '+' && otherValue.charAt(1) == '-') {
                return true;
            }
            if (thisValue.charAt(1) == '-' && otherValue.charAt(1) == '+') {
                return true;
            }
        }
        return false;
    }

    private HashMap<String, Block> copyOf(HashMap<String, Block> map) {
        HashMap<String, Block> result = new HashMap<>(map.size());
        for (Map.Entry<String, Block> entry : map.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    private Point getNextPoint(Point point, int faceIndex) {
        int row = point.getRowIndex();
        int column = point.getColumnIndex();
        switch (faceIndex) {
            case 0:
                return new Point(row - 1, column);
            case 1:
                return new Point(row, column + 1);
            case 2:
                return new Point(row + 1, column);
            case 3:
                return new Point(row, column - 1);
            default:
                throw new IllegalArgumentException();
        }
    }

    private Set<Block> getNextBlock(Block match, String other, int faceIndex) {
        Set<Block> blocks = new HashSet<>();
        Face otherFace = new Face(other);
        List<Face> matchFaces = match.getFaces();
        Face[] newFaces = new Face[4];//按原始相对顺序
        Face[] copyNewFaces = new Face[4];//翻转
        int index = 0;
        int otherIndex = faceIndex + 2 >= 4 ? faceIndex - 2 : faceIndex + 2;
        int oppositeIndex, newOppositeIndex;
        for (Face matchFace : matchFaces) {
            if (matchFace.equals(otherFace)) {
                newFaces[otherIndex] = otherFace;
                copyNewFaces[otherIndex] = otherFace;
                oppositeIndex = index + 2 >= 4 ? index - 2 : index + 2;
                newOppositeIndex = otherIndex + 2 >= 4 ? otherIndex - 2 : otherIndex + 2;
                newFaces[newOppositeIndex] = matchFaces.get(oppositeIndex);
                copyNewFaces[newOppositeIndex] = newFaces[newOppositeIndex];

                newFaces[newOppositeIndex + 1 == 4 ? 0 : newOppositeIndex + 1] =
                        matchFaces.get(oppositeIndex + 1 == 4 ? 0 : oppositeIndex + 1);

                newFaces[otherIndex + 1 == 4 ? 0 : otherIndex + 1] =
                        matchFaces.get(index + 1 == 4 ? 0 : index + 1);

                copyNewFaces[otherIndex + 1 == 4 ? 0 : otherIndex + 1] =
                        matchFaces.get(oppositeIndex + 1 == 4 ? 0 : oppositeIndex + 1);

                copyNewFaces[newOppositeIndex + 1 == 4 ? 0 : newOppositeIndex + 1] =
                        matchFaces.get(index + 1 == 4 ? 0 : index + 1);

                blocks.add(new Block(newFaces));
                blocks.add(new Block(copyNewFaces));
            }
            index++;
        }
        return blocks;
    }


    private class Block {
        private Face up;
        private Face right;
        private Face down;
        private Face left;

        private List<Face> faces;

        private String uniqueCode;

        public Block(String... values) {
            this.up = new Face(values[0]);
            this.right = new Face(values[1]);
            this.down = new Face(values[2]);
            this.left = new Face(values[3]);
            build();
        }

        public Block(Face[] faces) {
            this.up = faces[0];
            this.right = faces[1];
            this.down = faces[2];
            this.left = faces[3];
            build();
        }

        public Block(List<Face> faces) {
            this.up = faces.get(0);
            this.right = faces.get(1);
            this.down = faces.get(2);
            this.left = faces.get(3);
            faces.clear();
            build();
        }

        private void build() {
            this.faces = new LinkedList<>();
            this.faces.add(this.up);
            this.faces.add(this.right);
            this.faces.add(this.down);
            this.faces.add(this.left);

            this.uniqueCode = this.up.getMyself().concat(".")
                    .concat(this.right.getMyself()).concat(".")
                    .concat(this.down.getMyself()).concat(".")
                    .concat(this.left.getMyself()).concat(".");
        }

        public List<Face> getFaces() {
            return faces;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o instanceof Block) {
                Block other = (Block) o;
                return this.uniqueCode.equals(other.uniqueCode);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return uniqueCode.hashCode();
        }

        @Override
        public String toString() {
            return uniqueCode;
        }
    }

    private Map<String, String> key2key = new HashMap<>();

    private class Face {
        private String myself;
        private String other;

        public Face(String myself) {
            this.myself = myself;
            if (key2key.containsKey(myself)) {
                this.other = key2key.get(myself);
            } else {
                String other = existKey2Index.containsKey(myself) ? keys.get(existKey2Index.get(myself) + (myself.endsWith("+") ? 26 : -26)) : null;
                this.other = existKey2Index.containsKey(other) ? other : null;
                key2key.put(myself, this.other);
            }
        }

        public String getMyself() {
            return myself;
        }

        public String getOther() {
            return other;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o instanceof Face) {
                Face other = (Face) o;
                return this.hashCode() == other.hashCode();
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return myself.hashCode();
        }
    }

    public class Point {
        private int rowIndex;
        private int columnIndex;

        public Point(int rowIndex, int columnIndex) {
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;
        }

        public int getRowIndex() {
            return rowIndex;
        }

        public int getColumnIndex() {
            return columnIndex;
        }

        @Override
        public String toString() {
            return "(" + rowIndex +
                    ", " + columnIndex +
                    ')';
        }
    }

    private LinkedList<String> getKeys() {
        LinkedList<String> result = new LinkedList<>();
        result.addAll(getKeys(result.size(), "+"));
        result.addAll(getKeys(result.size(), "-"));
        return result;
    }

    private LinkedList<String> getKeys(int offset, String suffix) {
        LinkedList<String> result = new LinkedList<>();
        String key;
        int index = 0;
        for (int i = 65; i <= 90; i++) {
            key = String.valueOf((char) i).concat(suffix);
            result.add(key);
            key2Index.put(key, offset + index++);
        }
        return result;
    }
}
