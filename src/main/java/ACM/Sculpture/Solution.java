package ACM.Sculpture;

import basic.Util;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/6/3
 * Time:14:07
 * <p>
 * 某雕塑由n（n≤50）个边平行于坐标轴的长方体组成。每个长方体用6个整
 * 数x 0 ，y 0 ，z 0 ，x，y，z表示（均为1～500的整数），其中x 0 为长方体的顶点中x坐标的最小
 * 值，x表示长方体在x方向的总长度。其他4个值类似定义。你的任务是统计这个雕像的体积
 * 和表面积。注意，雕塑内部可能会有密闭的空间，其体积应计算在总体积中，但从“外部”看
 * 不见的面不应计入表面积。雕塑可能会由多个连通块组成。
 * <p>
 * Sculpture, ACM/ICPC NWERC 2008, UVa12171
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3323
 * <p>
 * Sample Input
 * 2
 * 2
 * 1 2 3 3 4 5
 * 6 2 3 3 4 5
 * 7
 * 1 1 1 5 5 1
 * 1 1 10 5 5 1
 * 1 1 2 1 4 8
 * 2 1 2 4 1 8
 * 5 2 2 1 4 8
 * 1 5 2 4 1 8
 * 3 3 4 1 1 1
 * Sample Output
 * 188 120
 * 250 250
 * <p>
 * 本题的关键理解在于坐标系的离散化处理，离散化前与离散化后，形成两个坐标系，存在对照关系。原始坐标系中的两个点，在离散化坐标系中，
 * <p>
 * 不仅要满足在原始坐标系中的大小关系，还需要能根据离散坐标系中的两点，计算出原始坐标系的原始点。
 * <p>
 * reference：
 * <p>
 * 1.http://baike.baidu.com/item/%E7%A6%BB%E6%95%A3%E5%8C%96
 * <p>
 * 2.https://baike.baidu.com/pic/%E7%A6%BB%E6%95%A3%E5%8C%96/10501557/0/b21c8701a18b87d6c762ab59070828381f30fd50?fr=lemma&ct=single#aid=0&pic=b21c8701a18b87d6c762ab59070828381f30fd50
 * <p>
 * 图2是个关键，必需要弄明白三组数据的对照关系
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("-----------------------------------------");
        solution.case2();
    }

    private void case1() {
        int[][] desc = {{1, 2, 3, 3, 4, 5}, {6, 2, 3, 3, 4, 5}};
        sculpture(desc);
    }

    private void case2() {
        int[][] desc = {
                {1, 1, 1, 5, 5, 1},
                {1, 1, 10, 5, 5, 1},
                {1, 1, 2, 1, 4, 8},
                {2, 1, 2, 4, 1, 8},
                {5, 2, 2, 1, 4, 8},
                {1, 5, 2, 4, 1, 8},
                {3, 3, 4, 1, 1, 1}
        };
        sculpture(desc);
    }

    private void sculpture(int[][] desc) {
        System.out.println(Arrays.deepToString(desc));
        List<Cube> realCubes = new LinkedList<>();
        Cube cube;
        LinkedList<Integer> X = new LinkedList<>();
        LinkedList<Integer> Y = new LinkedList<>();
        LinkedList<Integer> Z = new LinkedList<>();
        for (int[] item : desc) {
            cube = new Cube(item[0], item[1], item[2], item[3], item[4], item[5]);
            X.add(cube.getX().getStart());
            X.add(cube.getX().getEnd());
            Y.add(cube.getY().getStart());
            Y.add(cube.getY().getEnd());
            Z.add(cube.getZ().getStart());
            Z.add(cube.getZ().getEnd());
            realCubes.add(cube);
        }
        LinkedList<Integer> DX = discrete(X);
        LinkedList<Integer> DY = discrete(Y);
        LinkedList<Integer> DZ = discrete(Z);
        int len = Util.max(DX.size(), DY.size(), DZ.size()) + 2;
        List<Cube> DCubes = new LinkedList<>();
        Cube DCube;
        Area[][][] areas = new Area[len][len][len];
        //构建离散化坐标系
        for (Cube c : realCubes) {
            DCube = new Cube(findDI(DX, c.getX().getStart()), findDI(DX, c.getX().getEnd()),
                    findDI(DY, c.getY().getStart()), findDI(DY, c.getY().getEnd()),
                    findDI(DZ, c.getZ().getStart()), findDI(DZ, c.getZ().getEnd()), c);
            paintCube(DCube, areas);
            DCubes.add(DCube);
        }
        long volume = 0;
        floodFill(areas, new Cube(0, 0, 0, 1, 1, 1));
    }

    /**
     * 针对flood fill过程中，处于离散坐标系。
     * <p>
     * 计算表面积的难点在于，将与空气接触的立方体，每一个维度，取单位为1的二维正方形方块，计算在这个方块在原始坐标系的对应位置，从而计算出面积。
     * <p>
     * 注意每个维度，每个立方体均有两个面。
     * <p>
     * 计算体积的思路也是类似，空气没有接触到立方体，则需要将该单位为1的立方体，计算出原始坐标系对应的体积，累加到空气体积中。
     * <p>
     * <p>
     * 可见，难点就在于两个坐标系的转换，如何根据离散坐标系的相对位置，计算出原始坐标系的面积值和体积值，成为一个谜题。
     * <p>
     * 2017.6.8 这道题先暂时搁置了，等回头二刷的时候再来solved.
     */
    private void floodFill(Area[][][] space, Cube cube) {

    }

    private class Area {
        private boolean visit;
        private long area;

        public Area() {
        }

        public Area(long area) {
            this.area = area;
        }

        public void setVisit() {
            this.visit = true;
        }

        public void setArea(long area) {
            this.area = area;
        }
    }

    long area = 0;

    long airValume = 0;

    /**
     * 长方体有六个面，每个面，都需要染色
     */
    private void paintCube(Cube cube, Area[][][] space) {
        Line X = cube.getX();
        Line Y = cube.getY();
        Line Z = cube.getZ();

        int xs = X.getStart();
        int xe = X.getEnd();
        int ys = Y.getStart();
        int ye = Y.getEnd();
        int zs = Z.getStart();
        int ze = Z.getEnd();

        int x = xs;
        int y;
        while (x <= xe) {
            y = ys;
            while (y <= ye) {
                paintCube(x, y, zs, space);
                paintCube(x, y, ze, space);
                y++;
            }
            x++;
        }
        if (ye - ys > 1) {
            int z = zs;
            while (z <= ze) {
                y = ys;
                while (y <= ye) {
                    paintCube(xs, y, z, space);
                    paintCube(xe, y, z, space);
                    y++;
                }
                z++;
            }

            z = zs;
            while (z <= ze) {
                x = xs;
                while (x <= xe) {
                    paintCube(x, ys, z, space);
                    paintCube(x, ye, z, space);
                    x++;
                }
                z++;
            }
        }
    }

    private void paintCube(int x, int y, int z, Area[][][] space) {
        space[x][y][z] = new Area();
    }

    /**
     * 用真实坐标，找到离散化坐标值
     */
    private int findDI(LinkedList<Integer> D, int real) {
        Integer[] array = new Integer[D.size()];
        return Arrays.binarySearch(D.toArray(array), real) + 1;
    }

    /**
     * 将多个真实坐标，离散化
     */
    private LinkedList<Integer> discrete(LinkedList<Integer> real) {
        Collections.sort(real);
        LinkedHashSet<Integer> set = new LinkedHashSet<>();
        set.addAll(real);
        LinkedList<Integer> result = new LinkedList<>();
        result.addAll(set);
        return result;
    }

    private class Cube {
        private String code;
        private Line x;
        private Line y;
        private Line z;

        private Cube real;

        public Cube(int x_start, int y_start, int z_start, int x_offset, int y_offset, int z_offset) {
            this.x = new Line(x_start, x_offset);
            this.y = new Line(y_start, y_offset);
            this.z = new Line(z_start, z_offset);
            this.code = x.getNo().concat(".").concat(y.getNo()).concat(".").concat(z.getNo());
        }

        public Cube(int x_start, int x_end, int y_start, int y_end, int z_start, int z_end, Cube real) {
            this.x = new Line(x_start, x_end - x_start);
            this.y = new Line(y_start, y_end - y_start);
            this.z = new Line(z_start, z_end - z_start);
            this.real = real;
            this.code = x.getNo().concat(".").concat(y.getNo()).concat(".").concat(z.getNo());
        }


        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o instanceof Line) {
                Line other = (Line) o;
                return this.hashCode() == other.hashCode();
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return code.hashCode();
        }

        public Line getY() {
            return y;
        }

        public Line getX() {
            return x;
        }

        public Line getZ() {
            return z;
        }

        @Override
        public String toString() {
            return String.valueOf(code);
        }
    }

    private class Line {
        private String no;
        private int start;
        private int end;

        public Line(int start, int offset) {
            this.start = start;
            this.end = start + offset;
            this.no = String.valueOf(start).concat(".").concat(String.valueOf(end));
        }

        public String getNo() {
            return no;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o instanceof Line) {
                Line other = (Line) o;
                return this.hashCode() == other.hashCode();
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return no.hashCode();
        }
    }
}
