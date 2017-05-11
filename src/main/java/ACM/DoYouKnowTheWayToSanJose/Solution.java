package ACM.DoYouKnowTheWayToSanJose;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/10
 * Time:11:21
 * <p>
 * <p>
 * 有n张地图（已知名称和某两个对角线端点的坐标）和m个地名（已知名称和坐标），
 * 还有q个查询。每张地图都是边平行于坐标轴的矩形，比例定义为高度除以宽度的值。每个
 * 查询包含一个地名和详细等级i。面积相同的地图总是属于同一个详细等级。假定包含此地
 * 名的地图中一共有k种不同的面积，则合法的详细等级为1～k（其中1最不详细，k最详细，
 * 面积越小越详细）。如果详细等级i的地图不止一张，则输出地图中心和查询地名最接近的
 * 一张；如果还有并列的，地图长宽比应尽量接近0.75（这是Web浏览器的比例）；如果还有
 * 并列，查询地名和地图右下角的坐标应最远（对应最少的滚动条移动）；如果还有并列，则
 * 输出x坐标最小的一个。如果查询的地名不存在或者没有地图包含它，或者包含它的地图总
 * 数超过i，应报告查询非法（并输出包含它的最详细地图名称，如果存在）。
 * <p>
 * Do You Know The Way to San Jose?, ACM/ICPC World Finals 1997, UVa511
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=452
 * <p>
 * 这道题的中文描述与原题出入较大，建议直接看原题
 */
public class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
    }

    private void case1() {
        Map[] maps = new Map[6];
        maps[0] = new Map("BayArea", new Point(-6.0, 12.0), new Point(-11.0, 5.0));
        maps[1] = new Map("SantaClara", new Point(4.0, 9.0), new Point(-3.5, 2.5));
        maps[2] = new Map("SanJoseRegion", new Point(-3.0, 10.0), new Point(11.0, 3.0));
        maps[3] = new Map("CenterCoast", new Point(-5.0, 11.0), new Point(1.0, -8.0));
        maps[4] = new Map("SanMateo", new Point(-5.5, 4.0), new Point(-12.5, 9.0));
        maps[5] = new Map("NCalif", new Point(-13.0, -7.0), new Point(13.0, 15.0));

        Location[] locations = new Location[6];
        locations[0] = new Location("Monterey", new Point(-4.0, 2.0));
        locations[1] = new Location("SanJose", new Point(-1.0, 7.5));
        locations[2] = new Location("Fresno", new Point(7.0, 0.1));
        locations[3] = new Location("SanFrancisco", new Point(-10.0, 8.6));
        locations[4] = new Location("SantaCruz", new Point(-4.0, 2.0));
        locations[5] = new Location("SanDiego", new Point(13.8, -19.3));

        Request[] requests = new Request[7];
        requests[0] = new Request("SanJose", 3);
        requests[1] = new Request("SanFrancisco", 2);
        requests[2] = new Request("Fresno", 2);
        requests[3] = new Request("Stockton", 1);
        requests[4] = new Request("SanDiego", 2);
        requests[5] = new Request("SanJose", 4);
        requests[6] = new Request("SantaCruz", 3);

        find(maps, locations, requests);
    }

    private void find(Map[] mapArray, Location[] locationArray, Request[] requestArray) {
        Set<Map> maps = new HashSet<>();
        Collections.addAll(maps, mapArray);
        calculateLevel(maps);
        java.util.Map<String, Location> locations = new HashMap<>();
        for (Location location : locationArray) {
            locations.put(location.getName(), location);
            maps.stream().filter(map -> map.containsLocation(location)).forEach(location::addMap);
        }
        for (Request request : requestArray) {
            String name = request.getName();
            int level = request.getLevel();
            Location location = locations.get(name);
            if (location == null) {
                System.out.println(name + " at detail level " + level + " unknown location");
                continue;
            }
            Set<Map> containMaps = location.getMaps();
            if (containMaps.isEmpty()) {
                System.out.println(name + " at detail level " + level + " no map contains that location");
                continue;
            }
            List<Map> result = new ArrayList<>();
            result.addAll(location.getMaps(level));
            if (result.size() > 1) {
                currentLocation = location;
                Collections.sort(result);
            }
            printFound(name, level, result.get(0), location.isNotThisLevel());
        }
    }

    private Location currentLocation = null;

    private void printFound(String name, int level, Map map, boolean notThisLevel) {
        if (!notThisLevel) {
            System.out.println(name + " at detail level " + level + " using " + map.getName() + " level " + map.getLevel());
        } else {
            System.out.println(name + " at detail level " + level + " no map at that detail level;" + " using " + map.getName() + " level " + map.getLevel());
        }
    }

    private void calculateLevel(Set<Map> maps) {
        java.util.Map<Double, List<Map>> areaMaps = new HashMap<>();
        for (Map map : maps) {
            double area = map.getArea();
            List<Map> list = areaMaps.get(area);
            if (list == null) {
                list = new ArrayList<>();
                list.add(map);
                areaMaps.put(area, list);
            } else {
                list.add(map);
            }
        }
        Double[] areas = new Double[areaMaps.size()];
        areas = areaMaps.keySet().toArray(areas);
        Arrays.sort(areas);
        int level = areas.length;
        for (Double area : areas) {
            setLevel(areaMaps.get(area), level);
            level--;
        }
    }

    private void setLevel(List<Map> maps, int level) {
        for (Map map : maps) {
            map.setLevel(level);
        }
    }

    private class Request {
        private String name;
        private int level;

        public Request(String name, int level) {
            this.name = name;
            this.level = level;
        }

        public String getName() {
            return name;
        }

        public int getLevel() {
            return level;
        }
    }

    private class Location {
        private String name;
        private Point position;
        private Set<Map> maps;

        public Location(String name, Point position) {
            this.name = name;
            this.position = position;
        }

        public String getName() {
            return name;
        }

        public Point getPosition() {
            return position;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Location other = (Location) o;
            return this.getName().equals(other.getName());

        }

        @Override
        public int hashCode() {
            return this.getName().hashCode();
        }

        public void addMap(Map map) {
            if (this.maps == null) {
                this.maps = new HashSet<>();
            }
            this.maps.add(map);
        }

        public Set<Map> getMaps() {
            if (this.maps == null) {
                return new HashSet<>();
            }
            return this.maps;
        }

        public boolean isNotThisLevel = false;

        public boolean isNotThisLevel() {
            boolean result = this.isNotThisLevel;
            this.isNotThisLevel = false;
            return result;
        }

        public Set<Map> getMaps(int level) {
            if (this.maps == null || level == 0) {
                return new HashSet<>();
            }
            Set<Map> result = this.maps.stream().filter(map -> map.getLevel() >= level).collect(Collectors.toSet());
            if (result.isEmpty()) {
                this.isNotThisLevel = true;
                return this.getMaps(level - 1);
            } else {
                return result;
            }
        }
    }

    private class Map implements Comparable<Map> {
        private String name;
        private Point left;
        private Point right;
        private Point rightDown;
        private Point center;
        private boolean isLeftUp;
        private double area;//面积
        private double proportion;//比例
        private int level;

        public Map(String name, Point left, Point right) {
            if (left.equals(right) || left.getX() == right.getX() || left.getY() == right.getY()) {
                throw new IllegalArgumentException();
            }
            this.name = name;
            if (right.getX() < left.getX()) {
                this.left = right;
                this.right = left;
            } else {
                this.left = left;
                this.right = right;
            }
            calculate();
        }

        private void calculate() {
            double high;
            double middleY;
            if (this.left.getY() > this.right.getY()) {
                high = this.left.getY() - this.right.getY();
                middleY = this.right.getY() + high / 2;
                this.isLeftUp = true;
            } else {
                high = this.right.getY() - this.left.getY();
                middleY = this.left.getY() + high / 2;
                this.isLeftUp = false;
            }
            double length = this.right.getX() - this.left.getX();
            this.area = high * length;
            this.proportion = high / length;
            this.center = new Point(this.left.getX() + length / 2, middleY);
            if (this.isLeftUp) {
                this.rightDown = this.right;
            } else {
                this.rightDown = new Point(this.right.getX(), this.left.getY());
            }
        }

        public String getName() {
            return name;
        }

        public double getArea() {
            return area;
        }

        public double getProportion() {
            return proportion;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public Point getCenter() {
            return center;
        }

        public Point getRightDown() {
            return rightDown;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Location other = (Location) o;
            return this.getName().equals(other.getName());

        }

        @Override
        public int hashCode() {
            return this.getName().hashCode();
        }

        public boolean containsLocation(Location location) {
            Point point = location.getPosition();
            if (point.getX() < this.left.getX() || point.getX() > this.right.getX()) {
                return false;
            }
            if (this.isLeftUp) {
                if (point.getY() < this.right.getY() || point.getY() > this.left.getY()) {
                    return false;
                }
            } else {
                if (point.getY() < this.left.getY() || point.getY() > this.right.getY()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int compareTo(Map other) {
            int tl = this.getLevel();
            int ol = other.getLevel();
            if (tl != ol) {
                return tl - ol > 0 ? 1 : -1;
            }
            Point current = currentLocation.getPosition();
            double tcd = this.getCenter().distance(current);
            double ocd = other.getCenter().distance(current);
            if (tcd != ocd) {
                return tcd - ocd > 0 ? 1 : -1;
            }
            double tp = Math.abs(this.getProportion() - 0.75);
            double op = Math.abs(other.getProportion() - 0.75);
            if (tp != op) {
                return tp - op > 0 ? 1 : -1;
            }
            double trdd = this.getRightDown().distance(current);
            double ordd = other.getRightDown().distance(current);
            if (trdd != ordd) {
                return ordd - trdd > 0 ? 1 : -1;
            }
            double tmx = this.left.getX();
            double omx = other.left.getX();
            if (tmx != omx) {
                return tmx - omx > 0 ? 1 : -1;
            }
            return 0;
        }
    }

    private class Point {
        private String no;
        private double x;
        private double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
            this.no = x + "." + y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public String getNo() {
            return no;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj instanceof Point) {
                Point other = (Point) obj;
                return this.getNo().equals(other.getNo());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return this.getNo().hashCode();
        }

        @Override
        public String toString() {
            return "(" + this.getX() + "," + this.getY() + ")";
        }

        public double distance(Point other) {
            double x = other.getX() - this.getX();
            double y = other.getY() - this.getY();
            return Math.sqrt(x * x + y * y);
        }
    }
}
