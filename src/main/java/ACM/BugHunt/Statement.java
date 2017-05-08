package ACM.BugHunt;

import basic.Util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/8
 * Time:12:39
 */
class Statement {

    private static Map<String, Integer> arrayDefine;
    private static Map<String, Integer> arrayValue;

    static void parse(String[] statement) {
        arrayDefine = new HashMap<>();
        arrayValue = new HashMap<>();
        int index = 0;
        for (String string : statement) {
            System.out.println(string + " checking.");
            try {
                Type.parse(string);
            } catch (IllegalArgumentException e) {
                System.out.println("line " + (index + 1) + " is invalid." + e.getMessage());
                break;
            }
            System.out.println(string + " passed.");
            index++;
        }
    }

    private enum Type {
        DEFINE {
            @Override
            public void parsePart(String statement) {
                PartType.parse(statement, 0, statement.length() - 1, DEFINE, PartType.ARRAY, true);
            }
        }, ASSIGN {
            private int typeKeyPosition = -1;

            public void setTypeKeyPosition(int typeKeyPosition) {
                this.typeKeyPosition = typeKeyPosition;
            }

            @Override
            public void parsePart(String statement) {
                String left = statement.substring(0, typeKeyPosition);
                String value = (String) PartType.parse(left, 0, left.length() - 1, ASSIGN, PartType.ARRAY, true);
                String right = statement.substring(typeKeyPosition + 1, statement.length());
                arrayValue.put(value, Integer.valueOf(PartType.parse(right, 0, right.length() - 1, ASSIGN, null, false).toString()));
            }
        };

        public abstract void parsePart(String statement);

        public void setTypeKeyPosition(int typeKeyPosition) {

        }

        private static void parse(String statement) {
            Type.parseType(statement).parsePart(statement);
        }

        private static Type parseType(String statement) {
            ASSIGN.setTypeKeyPosition(-1);
            if (statement == null || statement.isEmpty()) {
                throw new IllegalArgumentException();
            }
            int len = statement.length();
            int p = statement.indexOf(Common.assignKey);
            if (p > 0) {
                if (p == len - 1) {//尾部
                    throw new IllegalArgumentException();
                }
                if (statement.indexOf(Common.assignKey, p + 1) > 0) {//多个
                    throw new IllegalArgumentException();
                }
                ASSIGN.setTypeKeyPosition(p);
                return ASSIGN;
            }
            if (p < 0) {
                return DEFINE;
            }
            throw new IllegalArgumentException();//首部
        }
    }

    private enum PartType {
        ARRAY, VALUE;

        private Integer parseValue(String statement, int beginIndex, int endIndex, boolean needthrow) {
            String numbStr = statement.substring(beginIndex, endIndex + 1);
            if (numbStr.isEmpty()) {
                throw new IllegalArgumentException("numbStr is empty,can not parse a int value.");
            }
            if (numbStr.charAt(0) == Common.space) {
                return parseValue(statement, beginIndex + 1, endIndex, needthrow);
            }
            if (numbStr.charAt(numbStr.length() - 1) == Common.space) {
                return parseValue(statement, beginIndex, endIndex - 1, needthrow);
            }
            Integer value;
            try {
                value = Integer.valueOf(numbStr);
            } catch (Exception e) {
                if (needthrow) {
                    throw new IllegalArgumentException("numbStr:" + numbStr + " can not parse a int value.");
                } else {
                    return null;
                }
            }
            return value;
        }

        private static Object parse(String statement, int beginIndex, int endIndex, Type type, PartType expect, Boolean isLeft) {
            if (beginIndex > endIndex) {
                return null;
            }
            if (statement.charAt(beginIndex) == Common.space) {
                parse(statement, beginIndex + 1, endIndex, type, expect, isLeft);
                return null;
            }
            if (statement.charAt(endIndex) == Common.space) {
                parse(statement, beginIndex, endIndex - 1, type, expect, isLeft);
                return null;
            }
            if (expect == null) {
                Integer value = VALUE.parseValue(statement, beginIndex, endIndex, false);
                if (value == null) {
                    LinkedList<Integer> arrayBeginIndexs = parseKeys(statement, Common.arrayBeginKey);
                    LinkedList<Integer> arrayEndIndexs = parseKeys(statement, Common.arrayEndKey);
                    if (arrayBeginIndexs.size() != arrayEndIndexs.size()) {
                        throw new IllegalArgumentException("array is not valid.");
                    }
                    return parseArray(statement, beginIndex, arrayBeginIndexs, endIndex, arrayEndIndexs, type, isLeft);
                } else {
                    return value;
                }
            } else if (expect.equals(VALUE)) {
                return VALUE.parseValue(statement, beginIndex, endIndex, true);
            } else {
                LinkedList<Integer> arrayBeginIndexs = parseKeys(statement, Common.arrayBeginKey);
                LinkedList<Integer> arrayEndIndexs = parseKeys(statement, Common.arrayEndKey);
                if (arrayBeginIndexs.size() != arrayEndIndexs.size()) {
                    throw new IllegalArgumentException("array is not valid.");
                }
                return parseArray(statement, beginIndex, arrayBeginIndexs, endIndex, arrayEndIndexs, type, isLeft);
            }
        }

        private static LinkedList<Integer> parseKeys(String string, char key) {
            LinkedList<Integer> result = new LinkedList<>();
            int len = string.length();
            char[] chars = string.toCharArray();
            for (int i = 0; i < len; i++) {
                if (chars[i] == key) {
                    result.add(i);
                }
            }
            return result;
        }

        private static String parseArray(String statement, int beginIndex, LinkedList<Integer> arrayBeginIndexs, int endIndex, LinkedList<Integer> arrayEndIndexs, Type type, Boolean isLeft) {
            if (beginIndex > endIndex) {
                return null;
            }
            if (statement.charAt(beginIndex) == Common.space) {
                return parseArray(statement, beginIndex + 1, arrayBeginIndexs, endIndex, arrayEndIndexs, type, isLeft);
            }
            if (statement.charAt(endIndex) == Common.space) {
                return parseArray(statement, beginIndex, arrayBeginIndexs, endIndex - 1, arrayEndIndexs, type, isLeft);
            }
            Integer bp = arrayBeginIndexs.pollLast();
            Integer ep = arrayEndIndexs.pollFirst();
            if (bp == null || ep == null) {
                return statement;
            }
            Integer index = VALUE.parseValue(statement, bp + 1, ep - 1, false);
            Integer preBp = arrayBeginIndexs.peekLast();
            boolean isOut = false;
            if (preBp == null) {
                preBp = -1;
                isOut = true;
            }

            String name = Util.skipBeginKey(statement, Common.space, preBp + 1, bp - 1);
            name = Util.skipEndKey(name, Common.space, 0, name.length() - 1);
            if (type.equals(Type.DEFINE)) {
                arrayDefine.put(name, index);
                return null;
            }
            if (isOut && isLeft) {
                return statement;
            }
            Integer dl = arrayDefine.get(name);
            if (dl == null) {
                throw new IllegalArgumentException("array " + name + " not define.");
            }
            if (index < 0 || index > dl - 1) {
                throw new IllegalArgumentException("index out of bound:" + index + ",max:" + (dl - 1));
            }
            String array = name + Common.arrayBeginKey + index + Common.arrayEndKey;
            Integer value = arrayValue.get(array);
            if (value == null) {
                throw new IllegalArgumentException(array + " not assigned.");
            }
            String b = statement;
            String e = statement;
            statement = b.substring(beginIndex, preBp + 1) + value + e.substring(ep + 1, endIndex + 1);
            arrayBeginIndexs = parseKeys(statement, Common.arrayBeginKey);
            arrayEndIndexs = parseKeys(statement, Common.arrayEndKey);
            return parseArray(statement, 0, arrayBeginIndexs, statement.length() - 1, arrayEndIndexs, type, isLeft);
        }
    }
}
