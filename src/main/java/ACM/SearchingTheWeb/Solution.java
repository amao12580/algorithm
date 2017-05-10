package ACM.SearchingTheWeb;

import basic.Util;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/8
 * Time:18:51
 * <p>
 * 输入n篇文章和m个请求（n<100，m≤50000），每个请求都是以下4种格式之一。
 * A：查找包含关键字A的文章。
 * A AND B：查找同时包含关键字A和B的文章。
 * A OR B：查找包含关键字A或B的文章。
 * NOT A：查找不包含关键字A的文章。
 * 处理询问时，需要对于每篇文章输出证据。前3种询问输出所有至少包含一个关键字的
 * 行，第4种询问输出整篇文章。关键字只由小写字母组成，查找时忽略大小写。每行不超过
 * 80个字符，一共不超过1500行。
 * <p>
 * Searching the Web, ACM/ICPC Beijing 2004, UVa1597
 * <p>
 * https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=4472
 */
public class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
    }

    private static final int keywordMax = 80;

    private void case1() {
        int len = Util.getRandomInteger(1, 99);
        int aSum = 0;
        int lSum = 0;
        String[][] articles = new String[len][];
        for (int i = 0; i < len; i++) {
            String[] article = generatorOneArticle();
            articles[i] = article;
            aSum++;
            lSum += article.length;
            if (lSum > 1500) {//所有文章的总行数不超过1500
                break;
            }
        }
        long s = System.currentTimeMillis();
        for (int i = 0; i < len; i++) {
            String[] article = articles[i];
            if (article == null) {
                break;
            }
            indexOne(article);
        }
        long e = System.currentTimeMillis();
        System.out.println(this.articles.size() + "," + this.indexes.size());
        System.out.println("article size:" + aSum + ",line size:" + lSum + ",time:" + (e - s) + "ms");
        case2();
    }

    private void case2() {
//        int len = Util.getRandomInteger(1, 50000);
        int len = Util.getRandomInteger(1, 50);
        for (int i = 0; i < len; i++) {
            search(generatorOneQuery());
            System.out.println("---------------------------------------------------------------------------");
        }
    }

    private void search(String query) {
        String[] params = Command.parse(query);
        Command command = Command.make(params[0]);
        if (command == null) {
            System.out.println("Inner Exception.");
            return;
        }
        String keyword;
        String keyword1;
        String keyword2;
        Set<Position> positions;
        switch (command) {
            case NONE:
                keyword = params[1];
                System.out.println("command :" + Command.NONE.toString() + ",keyword:" + keyword);
                positions = findPositions(keyword);
                if (positions != null) {
                    printLine(positions);
                } else {
                    printNotFound();
                }
                break;
            case NOT:
                keyword = params[2];
                System.out.println("command :" + Command.NOT.toString() + ",keyword:" + keyword);
                List<Article> articles = notIn(findPositions(keyword));
                if (articles != null) {
                    printArticle(articles);
                } else {
                    printNotFound();
                }
                break;
            case OR:
                keyword1 = params[1];
                keyword2 = params[2];
                System.out.println("command :" + Command.OR.toString() + ",keyword:" + keyword1 + "," + keyword2);
                positions = or(findPositions(keyword1), findPositions(keyword2));
                if (positions != null) {
                    printLine(positions);
                } else {
                    printNotFound();
                }
                break;
            case AND:
                keyword1 = params[1];
                keyword2 = params[2];
                System.out.println("command :" + Command.AND.toString() + ",keyword:" + keyword1 + "," + keyword2);
                Set<Position> positions1 = findPositions(keyword1);
                if (positions1 == null || positions1.isEmpty()) {
                    printNotFound();
                    break;
                }
                positions = and(positions1, findPositions(keyword2));
                if (positions != null) {
                    printLine(positions);
                } else {
                    printNotFound();
                }
                break;
        }
    }

    private Set<Position> and(Set<Position> left, Set<Position> right) {
        return or2And(left, right, false);
    }

    private Set<Position> or2And(Set<Position> left, Set<Position> right, boolean isOr) {
        if ((left == null || left.isEmpty()) && (right == null || right.isEmpty())) {
            return null;
        }
        if (left == null || left.isEmpty()) {
            if (!isOr) {
                return null;
            }
            return right;
        }
        if (right == null || right.isEmpty()) {
            if (!isOr) {
                return null;
            }
            return left;
        }
        Set<Position> result = new HashSet<>();
        if (isOr) {
            result.addAll(left);
            result.addAll(right);
        } else {
            result.addAll(left.stream().filter(right::contains).collect(Collectors.toList()));
        }
        return result;
    }

    private Set<Position> or(Set<Position> left, Set<Position> right) {
        return or2And(left, right, true);
    }

    private void printArticle(List<Article> articles) {
        int maxPrint = 10;
        for (Article article : articles) {
            if (maxPrint <= 0) {
                System.out.println("Too much print,will skip after.");
                break;
            }
            System.out.println(article.toString());
            maxPrint -= article.getLines().size();
        }
    }

    private List<Article> notIn(Set<Position> positions) {
        if (positions == null || positions.isEmpty()) {
            positions = findAll();
        }
        List<Article> articles = new ArrayList<>();
        for (Position position : positions) {
            int articleId = position.getArticleId();
            Article article = this.articles.get(articleId);
            if (article == null) {
                continue;
            }
            articles.add(article);
        }
        return articles;
    }

    private Set<Position> allPositions = null;

    private Set<Position> findAll() {
        if (allPositions == null) {
            allPositions = new HashSet<>();
            for (Map.Entry<String, Set<Position>> entry : indexes.entrySet()) {
                allPositions.addAll(entry.getValue());
            }
        }
        return allPositions;
    }

    private void printNotFound() {
        System.out.println("Sorry, I found nothing.");
    }

    private void printLine(Set<Position> positions) {
        for (Position position : positions) {
            int articleId = position.getArticleId();
            Article article = articles.get(articleId);
            if (article == null) {
                continue;
            }
            Line line = article.getLines().get(position.getLineId());
            if (line == null) {
                continue;
            }
            System.out.println("line:" + line.getContent() + ",offset:" + position.getOffset());
        }
    }

    private Set<Position> findPositions(String param) {
        param = validKeyWord(param);
        return findOne(param);
    }

    private String validKeyWord(String keyword) {
        char key = ' ';
        int beginIndex = -1;
        int endIndex = -1;
        char[] chars = keyword.toCharArray();
        int len = chars.length - 1;
        for (int i = 0; i <= len; i++) {
            if (chars[i] != key) {
                beginIndex = i;
                break;
            }
        }
        for (int i = len; i >= 0; i--) {
            if (chars[i] != key) {
                endIndex = i;
                break;
            }
        }
        if (beginIndex >= 0 && endIndex >= beginIndex) {
            return keyword.substring(beginIndex, endIndex + 1);
        }
        return null;
    }

    private int nextArticleId = 0;
    private int nextLineId = 0;

    public int getNextArticleId() {
        nextArticleId++;
        return nextArticleId;
    }

    public int getNextLineId() {
        nextLineId++;
        return nextLineId;
    }

    private class Article {
        private int id;
        private Map<Integer, Line> lines;

        public int getId() {
            return id;
        }

        public Map<Integer, Line> getLines() {
            return lines;
        }

        public Article(String[] strings) {
            this.id = getNextArticleId();
            this.lines = new HashMap<>();
            for (String string : strings) {
                if (string == null || string.isEmpty()) {
                    continue;
                }
                Line line = new Line(string);
                this.lines.put(line.getId(), line);
            }
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Line other = (Line) o;
            return id == other.id;
        }

        @Override
        public int hashCode() {
            return (id + "").hashCode();
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            Map<Integer, Line> lines = getLines();
            for (Map.Entry<Integer, Line> entry : lines.entrySet()) {
                stringBuilder = stringBuilder.append(entry.getValue());
                stringBuilder = stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        }
    }

    private class Line {
        private int id;
        private String content;

        public int getId() {
            return id;
        }

        public String getContent() {
            return content;
        }

        public Line(String string) {
            this.id = getNextLineId();
            this.content = string;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Line other = (Line) o;
            return id == other.id;
        }

        @Override
        public int hashCode() {
            return (id + "").hashCode();
        }

        @Override
        public String toString() {
            return getContent();
        }
    }

    Map<Integer, Article> articles = new HashMap<>();

    private void indexOne(String[] strings) {
        Article article = new Article(strings);
        indexOne(article);
        this.articles.put(article.getId(), article);
    }

    private void indexOne(Article article) {
        int articleId = article.getId();
        Map<Integer, Line> lines = article.getLines();
        for (Map.Entry<Integer, Line> entry : lines.entrySet()) {
            indexOne(entry.getValue(), articleId);
        }
    }

    private void indexOne(Line line, int articleId) {
        int lineId = line.getId();
        String content = line.getContent();
        int len = content.length();
        int beginIndex = -1;
        int endIndex = -1;
        for (int i = 0; i < len; i++) {
            char c = content.charAt(i);
            if (isAlpha(c)) {
                if (beginIndex < 0 && endIndex < 0) {
                    beginIndex = i;
                    endIndex = i;
                } else {
                    if (beginIndex >= 0 && endIndex >= beginIndex) {
                        endIndex = i;
                    }
                }
            } else {
                if (beginIndex >= 0 && endIndex >= beginIndex) {
                    indexOne(content.substring(beginIndex, endIndex + 1), articleId, lineId, beginIndex);
                    beginIndex = -1;
                    endIndex = -1;
                }
            }
        }
        if (beginIndex >= 0 && endIndex >= beginIndex) {
            indexOne(content.substring(beginIndex, endIndex + 1), articleId, lineId, beginIndex);
        }
    }

    private void indexOne(String string, int articleId, int lineId, int offset) {
        string = string.toLowerCase();
        int len = string.length();
        int max = len > keywordMax ? keywordMax : len;
        int step = max;
        while (step >= 0) {
            for (int i = 0; i + step < max; i++) {
                String term = string.substring(i, i + step + 1);
                Set<Position> positions = findOne(term, false);
                if (positions == null) {
                    positions = new HashSet<>();
                    positions.add(new Position(articleId, lineId, offset + i));
                    indexes.put(term, positions);
                } else {
                    positions.add(new Position(articleId, lineId, offset + i));
                }
            }
            step--;
        }
        if (max > currentMaxLen) {
            currentMaxLen = max;
        }
    }

    private boolean isAlpha(char c) {
        return (c >= 65 && c <= 90) || (c >= 97 && c <= 122);
    }

    private int currentMaxLen = 0;

    Map<String, Set<Position>> cacheQuery = new HashMap<>();

    private Set<Position> findOne(String term) {
        return findOne(term, true);
    }

    private Set<Position> findOne(String term, boolean useCache) {
        if (!useCache) {
            return indexes.get(term);
        }
        Set<Position> cache = cacheQuery.get(term);
        if (cache != null) {
            return cache;
        }
        cache = indexes.get(term);
        if (cache != null) {
            cacheQuery.put(term, cache);
        }
        return cache;
    }

    Map<String, Set<Position>> indexes = new HashMap<>();

    private class Position {
        private String no;
        private int articleId;
        private int lineId;
        private int offset;

        public Position(int articleId, int lineId, int offset) {
            this.articleId = articleId;
            this.lineId = lineId;
            this.offset = offset;
            this.no = articleId + "." + lineId + "." + offset;
        }

        public int getArticleId() {
            return articleId;
        }

        public int getLineId() {
            return lineId;
        }

        public int getOffset() {
            return offset;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Position other = (Position) o;
            return articleId == other.getArticleId() && lineId == other.getLineId() && offset == other.getOffset();
        }

        @Override
        public int hashCode() {
            return no.hashCode();
        }
    }


    enum Command {
        NONE {
            @Override
            public String getRandomQuery() {
                return getRandomKeyword();
            }
        },
        AND {
            @Override
            public String getRandomQuery() {
                return getRandomKeyword() + " " + this.toString() + " " + getRandomKeyword();
            }
        }, OR {
            @Override
            public String getRandomQuery() {
                return getRandomKeyword() + " " + this.toString() + " " + getRandomKeyword();
            }
        }, NOT {
            @Override
            public String getRandomQuery() {
                return this.toString() + " " + getRandomKeyword();
            }
        };

        public abstract String getRandomQuery();

        public String getRandomKeyword() {
            int len = Util.getRandomInteger(3, keywordMax / 8);
            return Util.generateLetterString(len);
        }

        public static String getRandomOne() {
            Command[] commands = Command.values();
            return commands[Util.getRandomInteger(0, commands.length - 1)].getRandomQuery();
        }

        private static String[] parse(String query, String key) {
            int p = query.indexOf(key);
            if (p >= 0) {
                String[] result = new String[3];
                result[0] = key;
                result[1] = query.substring(0, p).toLowerCase();
                result[2] = query.substring(p + key.length()).toLowerCase();
                return result;
            }
            return null;
        }


        public static Command make(String command) {
            Command[] commands = Command.values();
            for (Command c : commands) {
                if (command.equals(c.toString())) {
                    return c;
                }
            }
            return null;
        }

        public static String[] parse(String query) {
            String[] result;
            String and = AND.toString();
            result = parse(query, and);
            if (result != null) {
                return result;
            }
            String or = OR.toString();
            result = parse(query, or);
            if (result != null) {
                return result;
            }
            String not = NOT.toString();
            result = parse(query, not);
            if (result != null) {
                return result;
            }
            result = new String[3];
            result[0] = NONE.toString();
            result[1] = query.toLowerCase();
            return result;
        }
    }

    private String generatorOneQuery() {
        return Command.getRandomOne();
    }

    private String[] generatorOneArticle() {//一篇文章
        int len = Util.getRandomInteger(1, 1500 / 2);
        String[] result = new String[len];
        for (int i = 0; i < len; i++) {
            result[i] = generatorOneLine();
        }
        return result;
    }

    private String generatorOneLine() {
        int len = Util.getRandomInteger(1, keywordMax);
        return Util.generateMixedString(len);
    }
}
