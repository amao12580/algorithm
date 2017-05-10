package ACM.UpdatingADictionary;

import basic.Util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/9
 * Time:17:42
 * <p>
 * 在本题中，字典是若干键值对，其中键为小写字母组成的字符串，值为没有前导零或正
 * 号的非负整数（-4，03和+77都是非法的，注意该整数可以很大）。输入一个旧字典和一个
 * 新字典，计算二者的变化。输入的两个字典中键都是唯一的，但是排列顺序任意。具体格式
 * 为（注意字典格式中不含任何空白字符）：
 * {key:value,key:value,…,key:value}
 * 输入包含两行，各包含不超过100个字符，即旧字典和新字典。输出格式如下：
 * 如果至少有一个新增键，打印一个“+”号，然后是所有新增键，按字典序从小到大排
 * 列。
 * 如果至少有一个删除键，打印一个“-”号，然后是所有删除键，按字典序从小到大排
 * 列。
 * 如果至少有一个修改键，打印一个“*”号，然后是所有修改键，按字典序从小到大排
 * 列。
 * 如果没有任何修改，输出No changes。
 * 例如，若输入两行分别为{a:3,b:4,c:10,f:6}和{a:3,c:5,d:10,ee:4}，输出为以下3行：
 * +d,ee；-b,f； * c。
 * <p>
 * <p>
 * Updating a Dictionary, UVa12504
 */
public class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        String[] strings = solution.generatorOne();
        solution.detected(strings);
    }

    private String[] generatorOne() {
        String[] result = new String[2];
        result[0] = generatorOneDictionary();
        result[1] = generatorOneDictionary();
        return result;
    }

    private String generatorOneDictionary() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder = stringBuilder.append("{");
        int len = Util.getRandomInteger(1, 10) - 1;
        for (int i = 0; i <= len; i++) {
            stringBuilder = stringBuilder.append(generatorOneKV());
            if (i != len) {
                stringBuilder = stringBuilder.append(",");
            }
        }
        stringBuilder = stringBuilder.append("}");
        return stringBuilder.toString();
    }

    private String generatorOneKV() {
        return Util.generateLetterString(Util.getRandomInteger(1, 10)) + ":" + Util.getRandomInteger(0, 100);
    }

    private void detected(String[] strings) {
        String oldDictStr = strings[0];
        String newDictStr = strings[1];
        System.out.println("before:" + oldDictStr);
        System.out.println("after:" + newDictStr);
        Set<Element> oldDict = parseDict(oldDictStr);
        Set<Element> newDict = parseDict(newDictStr);
        Set<Element> union = new HashSet<>();
        union.addAll(oldDict);
        union.retainAll(newDict);
        Set<Element> forAdds = new HashSet<>();
        forAdds.addAll(newDict);
        forAdds.removeAll(union);
        print("+", getKeys(forAdds));
        Set<Element> forDeletes = new HashSet<>();
        forDeletes.addAll(oldDict);
        forDeletes.removeAll(union);
        print("-", getKeys(forDeletes));
        Set<Element> forUpdates = union.stream().filter(element -> !findElementValue(element, oldDict).equals(findElementValue(element, newDict))).collect(Collectors.toSet());
        print("*", getKeys(forUpdates));
        if (forAdds.size() == 0 && forDeletes.size() == 0 && forUpdates.size() == 0) {
            System.out.println("No changes.");
        }
    }

    private Set<Element> parseDict(String str) {
        Set<Element> set = new HashSet<>();
        int len = str.length();
        if (str.charAt(0) != '{' || str.charAt(len - 1) != '}' || len == 2) {
            throw new IllegalArgumentException("invalid kv string.");
        }
        return parseDict(str, 1, len - 1, set);
    }

    private Set<Element> parseDict(String str, int beginIndex, int endIndex, Set<Element> set) {
        if (beginIndex > endIndex) {
            return set;
        }
        String key = null;
        int value = -1;
        for (int i = beginIndex; i <= endIndex; i++) {
            char c = str.charAt(i);
            if (c == ':') {
                if (key == null) {
                    key = str.substring(beginIndex, i);
                    beginIndex = i + 1;
                } else {
                    throw new IllegalArgumentException("invalid kv string.");
                }
            }
            if (c == ',' || c == '}') {
                if (value < 0) {
                    value = Integer.valueOf(str.substring(beginIndex, i));
                    beginIndex = i + 1;
                } else {
                    throw new IllegalArgumentException("invalid kv string.");
                }
            }
            if (key != null && value >= 0) {
                set.add(new Element(key, value));
                break;
            }
        }
        return parseDict(str, beginIndex, endIndex, set);
    }

    private void print(String prefix, String[] keys) {
        System.out.println(prefix + Arrays.toString(keys));
    }

    private String[] getKeys(Set<Element> set) {
        String[] strings = new String[set.size()];
        int index = 0;
        for (Element e : set) {
            strings[index] = e.getKey();
            index++;
        }
        return Util.orderByDictionaryASC(strings);
    }

    private Integer findElementValue(Element element, Set<Element> set) {
        for (Element e : set) {
            if (e.equals(element)) {
                return e.getValue();
            }
        }
        return -1;
    }


    private class Element {
        private String key;
        private int value;

        public Element(String key, int value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public int getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Element element = (Element) o;
            return !(key != null ? !key.equals(element.key) : element.key != null);
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }
    }
}
