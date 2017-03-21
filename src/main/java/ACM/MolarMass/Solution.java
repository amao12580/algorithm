package ACM.MolarMass;

import basic.Util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/3/21
 * Time:15:55
 * <p>
 * 给出一种物质的分子式（不带括号），求分子量。本题中的分子式只包含4种原子，分别为C, H, A, N，原子量分别为12.01, 1.008, 16.00, 14.01（单位：g/mol）。
 * 例如，C6H5AH的分子量为94.108g/mol。72.06+6.048+16
 */

public class Solution {
    private static HashMap<Character, Double> atoms = null;
    private static char[] seeds1 = null;
    private static final char[] seeds2 = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static void main(String[] args) {
        //A20CN40A22665N44488    986809.29
        //HHAA32N7020H    98881.224
        String[] target = {"C6H5AH", "A20CN40A22665N44488", "HHAA32N7020H", generatorOne()};
//        String[] target = {"A20CN40A22665N44488"};
        for (String item : target) {
            System.out.print("target:" + item);
            System.out.println(",value:" + MolarMass(item));
        }
    }

    private static double MolarMass(String string) {
        double result = 0;
        int len = string.length();
        Character element = null;
        int sum = 1;
        String sumStr = "";
        Set<Character> elements = atoms.keySet();
        for (int i = 0; i < len; i++) {
            char current = string.charAt(i);
            if (elements.contains(current)) {
                if (element != null) {
                    result += new BigDecimal(atoms.get(element)).multiply(new BigDecimal(sum)).doubleValue();
                    sum = 1;
                    sumStr = "";
                }
                element = current;
            } else {
                sumStr += current;
                sum = Integer.valueOf(sumStr);
            }
            if (i == len - 1 && element != null) {
                result += new BigDecimal(atoms.get(element)).multiply(new BigDecimal(sum)).doubleValue();
                break;
            }
        }
        return result;
    }

    static {
        if (atoms == null) {
            atoms = new HashMap<>();
            atoms.put('C', 12.01);
            atoms.put('H', 1.008);
            atoms.put('A', 16.00);
            atoms.put('N', 14.01);
            Set<Character> set = atoms.keySet();
            seeds1 = new char[set.size()];
            int index = 0;
            for (Character item : set) {
                seeds1[index] = item;
                index++;
            }
        }
    }

    private static char[] seeds = null;

    private static String generatorOne() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder = stringBuilder.append(seeds1[Util.getRandomInteger(0, 3)]);
        if (seeds == null) {
            int len1 = seeds1.length;
            int len2 = seeds2.length;
            seeds = new char[len1 + len2];
            System.arraycopy(seeds1, 0, seeds, 0, len1);
            System.arraycopy(seeds2, 0, seeds, len1, len2);
        }
        int len = Util.getRandomInteger(5, 15);
        char pre = '\u0000';
        for (int i = 0; i < len; ) {
            char c = seeds[Util.getRandomInteger(0, seeds.length - 1)];
            if (!(c == '0' && (atoms.keySet().contains(pre) || pre == c))) {
                pre = c;
                stringBuilder = stringBuilder.append(c);
                i++;
            }
        }
        return stringBuilder.toString();
    }

}
