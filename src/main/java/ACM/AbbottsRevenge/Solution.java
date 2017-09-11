package ACM.AbbottsRevenge;

import basic.dataStructure.graph.Point;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/25
 * Time:15:14
 * <p>
 * Abbott's Revenge, ACM/ICPC World Finals 2000, UVa 816
 * <p>
 * 有一个最多包含9*9个交叉点的迷宫。输入起点、离开起点时的朝向和终点，求一条最
 * 短路（多解时任意输出一个即可）。
 * 图6-14　迷宫及走向
 * 这个迷宫的特殊之处在于：进入一个交叉点
 * 的方向（用NEWS这4个字母分别表示北东西
 * 南，即上右左下）不同，允许出去的方向也不
 * 同。例如，1 2 WLF NR ER *表示交叉点(1,2)
 * （上数第1行，左数第2列）有3个路标（字
 * 符“*”只是结束标志），如果进入该交叉点时的
 * 朝向为W（即朝左），则可以左转（L）或者直
 * 行（F）；如果进入时朝向为N或者E则只能右转
 * （R），如图6-14所示。
 * 注意：初始状态是“刚刚离开入口”，所以即
 * 使出口和入口重合，最短路也不为空。例如，图
 * 6-14中的一条最短路为(3,1) (2,1) (1,1) (1,2)
 * (2,2) (2,3) (1,3) (1,2) (1,1) (2,1) (2,2) (1,2) (1,3) (2,3) (3,3)。
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=757
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("--------------------------------------------------------------------------------");
        solution.case2();
    }

    /**
     * SAMPLE
     * 3 1 N 3 3
     * 1 1 WL NR *
     * 1 2 WLF NR ER *
     * 1 3 NL ER *
     * 2 1 SL WR NF *
     * 2 2 SL WF ELF *
     * 2 3 SFR EL *
     */
    private void case1() {
        Crossroad[] crossroads = new Crossroad[6];
        crossroads[0] = new Crossroad(1, 1, "WL", "NR");
        crossroads[1] = new Crossroad(1, 2, "WLF", "NR", "ER");
        crossroads[2] = new Crossroad(1, 3, "NL", "ER");
        crossroads[3] = new Crossroad(2, 1, "SL", "WR", "NF");
        crossroads[4] = new Crossroad(2, 2, "SL", "WF", "ELF");
        crossroads[5] = new Crossroad(2, 3, "SFR", "EL");
        run(new Maze("SAMPLE"), crossroads, new Rats(3, 1, 'N'), new Point(3, 3));
    }

    /**
     * NOSOLUTION
     * 3 1 N 3 2
     * 1 1 WL NR *
     * 1 2 NL ER *
     * 2 1 SL WR NFR *
     * 2 2 SR EL *
     * 0
     * END
     */
    private void case2() {
        Crossroad[] crossroads = new Crossroad[5];
        crossroads[0] = new Crossroad(1, 1, "WL", "NR");
        crossroads[1] = new Crossroad(1, 2, "NR", "ER");
        crossroads[2] = new Crossroad(2, 1, "NL", "ER");
        crossroads[3] = new Crossroad(2, 1, "SL", "WR", "NFR");
        crossroads[4] = new Crossroad(2, 2, "SR", "EL");
        run(new Maze("NOSOLUTION"), crossroads, new Rats(3, 1, 'N'), new Point(3, 2));
    }

    private void run(Maze maze, Crossroad[] crossroads, Rats rats, Point target) {
        System.out.println(maze.getName());
        maze.setCrossroads(crossroads);
        maze.appendCrossroad(new Crossroad(rats));
        List<Path> paths = new ArrayList<>();
        Path path = new Path(target);
        path.append(rats.getMovePoint());
        paths.add(path);
        move(maze, rats, path, paths);
        paths = filter(paths);
        if (paths.isEmpty()) {
            System.out.println("No SolutionV1 Possible");
            return;
        }
        if (paths.size() > 1) {
            Collections.sort(paths);
        }
        Path best = paths.get(0);
        LinkedHashSet<MovePoint> bestChain = best.getTrace();
        StringBuilder builder = new StringBuilder();
        for (MovePoint point : bestChain) {
            builder = builder.append(point.getLocation().toString());
            builder = builder.append(" ");
        }
        System.out.println(builder.toString());
    }

    private void move(Maze maze, Rats rats, Path currentPath, List<Path> paths) {
        char[] directions = maze.getCanMoveDirections(rats);
        if (directions == null) {
            return;
        }
        int length = directions.length;
        if (directions.length == 0) {
            return;
        }
        Path path = currentPath;
        for (int i = 0; i < length; i++) {
            if (i > 0) {//出现多个方向
                path = new Path(currentPath);
                paths.add(path);
            }
            move(maze, rats, path, paths, directions[i]);
        }
    }

    private void move(Maze maze, Rats rats, Path path, List<Path> paths, char direction) {
        Boolean moved = move(direction, rats, path);
        if (moved == null || moved) {//出现死循环路线或者已经到达目的地，终止寻路
            return;
        }
        move(maze, rats, path, paths);
    }

    private Boolean move(char direction, Rats rats, Path path) {
        rats.move(direction);
        Boolean result = path.append(rats.getMovePoint());
        if (result == null) {//因出现死循环路线，回退一步
            rats.rollback(path.getLast());
        }
        return result;
    }

    /**
     * 过滤掉未能到达的路线
     */
    private List<Path> filter(List<Path> paths) {
        List<Path> result = paths.stream().filter(Path::hasArrived).collect(Collectors.toCollection(ArrayList::new));
        paths.clear();
        return result;
    }


    private class Path implements Comparable<Path> {
        private boolean hasArrived = false;
        private Point target;
        private MovePoint last;
        private LinkedHashSet<MovePoint> trace = new LinkedHashSet<>();

        public Path(Point target) {
            this.target = target;
        }

        public Path(Path copyOf) {
            this.target = copyOf.getTarget();
            this.trace.addAll(copyOf.getTrace());
        }

        public Boolean append(MovePoint movePoint) {
            if (!trace.add(movePoint)) {//出现死循环路线
                return null;
            }
            last = movePoint;
            hasArrived = movePoint.getLocation().equals(target);
            return hasArrived;
        }

        public LinkedHashSet<MovePoint> getTrace() {
            return trace;
        }

        public Point getTarget() {
            return target;
        }

        public MovePoint getLast() {
            return last;
        }

        public boolean hasArrived() {
            return hasArrived;
        }

        @Override
        public int compareTo(Path other) {
            return this.getTrace().size() - other.getTrace().size();
        }
    }


    private class Maze {
        private String name;
        private Map<String, Crossroad> crossroads;

        public Maze(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setCrossroads(Crossroad[] crossroads) {
            this.crossroads = new HashMap<>();
            for (Crossroad crossroad : crossroads) {
                this.crossroads.put(crossroad.getUniqueCode(), crossroad);
            }
        }

        public void appendCrossroad(Crossroad crossroad) {
            this.crossroads.put(crossroad.getUniqueCode(), crossroad);
        }

        public char[] getCanMoveDirections(Rats rats) {
            String key = rats.getMovePoint().getLocation().getUniqueCode();
            return crossroads.containsKey(key) ? crossroads.get(key).getDirections(rats.getDirection()) : null;
        }
    }


    private class MovePoint {
        private String uniqueCode;
        private Point location;
        private char direction;

        public MovePoint(Point location, char direction) {
            this.location = location;
            this.direction = direction;
            this.uniqueCode = location.getUniqueCode().concat(String.valueOf(direction));
        }

        public Point getLocation() {
            return location;
        }

        public char getDirection() {
            return direction;
        }

        @Override
        public int hashCode() {
            return getUniqueCode().hashCode();
        }

        public String getUniqueCode() {
            return this.uniqueCode;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o instanceof MovePoint) {
                MovePoint other = (MovePoint) o;
                return this.getUniqueCode().equals(other.getUniqueCode());
            } else {
                return false;
            }
        }
    }


    private class Rats {
        private MovePoint movePoint;

        public Rats(int x, int y, char direction) {
            checkInRange(x, y);
            this.movePoint = new MovePoint(new Point(x, y), direction);
        }

        private void checkInRange(int x, int y) {
            if (!(x >= 1 && x <= 9 && y >= 1 && y <= 9)) {
                throw new IllegalArgumentException(x + "," + y);
            }
        }

        public MovePoint getMovePoint() {
            return this.movePoint;
        }

        public char getDirection() {
            return this.movePoint.getDirection();
        }

        public void move(char newDirection) {
            Point point = this.movePoint.getLocation();
            int x = point.getRowIndex();
            int y = point.getColumnIndex();
            char oldDirection = this.movePoint.getDirection();
            char direction;
            switch (oldDirection) {
                case 'N':
                    switch (newDirection) {
                        case 'F':
                            x--;
                            direction = 'N';
                            break;
                        case 'L':
                            y--;
                            direction = 'W';
                            break;
                        case 'R':
                            y++;
                            direction = 'E';
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                    break;
                case 'E':
                    switch (newDirection) {
                        case 'F':
                            y++;
                            direction = 'E';
                            break;
                        case 'L':
                            x--;
                            direction = 'N';
                            break;
                        case 'R':
                            x++;
                            direction = 'S';
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                    break;
                case 'W':
                    switch (newDirection) {
                        case 'F':
                            y--;
                            direction = 'W';
                            break;
                        case 'L':
                            x++;
                            direction = 'S';
                            break;
                        case 'R':
                            x--;
                            direction = 'N';
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                    break;
                case 'S':
                    switch (newDirection) {
                        case 'F':
                            x++;
                            direction = 'S';
                            break;
                        case 'L':
                            y++;
                            direction = 'E';
                            break;
                        case 'R':
                            y--;
                            direction = 'W';
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                    break;
                default:
                    throw new IllegalArgumentException();
            }

            checkInRange(x, y);
            this.movePoint = new MovePoint(new Point(x, y), direction);
        }

        public void rollback(MovePoint pre) {
            this.movePoint = pre;
        }
    }

    private class Crossroad {
        private Point location;
        private Map<Character, char[]> directions = new HashMap<>();

        public Crossroad(int x, int y, String... directions) {
            this.location = new Point(x, y);
            char[] chars;
            int len;
            for (String item : directions) {
                if (item == null || item.length() == 1) {
                    throw new IllegalArgumentException();
                }
                chars = item.toCharArray();
                len = chars.length;
                char key = chars[0];
                char[] array = this.directions.get(key);
                if (array == null) {
                    array = new char[item.length() - 1];
                }
                System.arraycopy(chars, 1, array, 0, len - 1);
                this.directions.put(key, array);
            }
        }

        public Crossroad(Rats rats) {
            Point point = rats.getMovePoint().getLocation();
            this.location = new Point(point.getRowIndex(), point.getColumnIndex());
            char[] array = {'F'};
            this.directions.put(rats.getDirection(), array);
        }

        @Override
        public int hashCode() {
            return getUniqueCode().hashCode();
        }

        public String getUniqueCode() {
            return location.getUniqueCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o instanceof Crossroad) {
                Crossroad other = (Crossroad) o;
                return this.getUniqueCode().equals(other.getUniqueCode());
            } else {
                return false;
            }
        }

        public char[] getDirections(char direction) {
            return directions.get(direction);
        }
    }
}
