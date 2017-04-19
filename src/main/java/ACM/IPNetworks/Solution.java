package ACM.IPNetworks;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/4/19
 * Time:15:03
 * <p>
 * 可以用一个网络地址和一个子网掩码描述一个子网（即连续的IP地址范围）。其中子网
 * 掩码包含32个二进制位，前32-n位为1，后n位为0，网络地址的前32-n位任意，后n位为0。
 * 所有前32-n位和网络地址相同的IP都属于此网络。
 * 例如，网络地址为194.85.160.176（二进制为11000010|01010101|10100000|10110000），
 * 子网掩码为255.255.255.248（二进制为11111111|11111111|11111111|11111000），则该子网
 * 的IP地址范围是194.85.160.176～194.85.160.183。输入一些IP地址，求最小的网络（即包含IP
 * 地址最少的网络），包含所有这些输入地址。
 * 例如，若输入3个IP地址：194.85.160.177、194.85.160.183和194.85.160.178，包含上述3
 * 个地址的最小网络的网络地址为194.85.160.176，子网掩码为255.255.255.248。
 * <p>
 * IP Networks, ACM/ICPC NEERC 2005, UVa1590
 */
public class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("----------------------------------------------------");
        solution.case2();
    }


    private void case1() {
        String ipStr1 = "194.85.160.176";
        IP ip1 = new IP(ipStr1);
        System.out.println("ip1:" + ip1.toString() + ",binary:" + ip1.toBinaryString());
        String ipStr2 = "255.255.255.248";
        IP ip2 = new IP(ipStr2);
        System.out.println("ip2:" + ip2.toString() + ",binary:" + ip2.toBinaryString());
    }

    private void case2() {
        String[] ipStrArray = {"194.85.160.177", "194.85.160.183", "194.85.160.178"};
        findIPNetWork(ipStrArray);
    }

    private void findIPNetWork(String[] ipStrArray) {
        int len = 32;
        IP min = null;
        IP max = null;
        Set<IP> ips = new HashSet<>();
        for (String anIpStrArray : ipStrArray) {
            IP ip = new IP(anIpStrArray);
            if (min == null) {
                min = ip;
            } else {
                if (min.compareTo(ip) > 0) {
                    min = ip;
                }
            }
            if (max == null) {
                max = ip;
                if (max.compareTo(ip) < 0) {
                    max = ip;
                }
            }
            System.out.println("ip:" + ip.toString() + ",binary:" + ip.toBinaryString());
            ips.add(ip);
        }
        String temp = min.toBinaryString();
        int i = 0;
        while (fullMatch(ips, temp.charAt(i), i)) {
            i++;
        }
        int n = 0;
        String prefix = temp.substring(0, i);
        int left = len - i;
        IP targetMinIP = null;
        while (n <= left) {
            IP[] ranges = getSubIPRange(prefix, left, n);
            if (ranges[0].compareTo(min) <= 0 && ranges[1].compareTo(max) >= 0) {
                targetMinIP = ranges[0];
                break;
            }
            n++;
        }
        if (targetMinIP == null) {
            System.out.print("targetMinIP not found.:");
            System.out.print("mask not found.:");
            return;
        }
        System.out.println("targetMinIP:" + targetMinIP.toString());
        int index = 0;
        StringBuilder mask = new StringBuilder();
        while (index < len) {
            if (index + n < len) {
                mask = mask.append("1");
            } else {
                mask = mask.append("0");
            }
            index++;
        }
        System.out.println("mask:" + new IP(getIPStringFormBinaryString(mask.toString())).toString());
    }

    private IP[] getSubIPRange(String originPrefix, int left, int n) {
        IP[] result = new IP[2];
        String min = originPrefix;
        String max = originPrefix;
        while (left > 0) {
            max += "1";
            if (left > n) {
                min += "1";
            } else {
                min += "0";
            }
            left--;
        }
        result[0] = new IP(getIPStringFormBinaryString(min));
        result[1] = new IP(getIPStringFormBinaryString(max));
        return result;
    }

    private String getIPStringFormBinaryString(String binaryString) {
        int end = binaryString.length();
        int beginIndex = 0;
        int len = 8;
        StringBuilder stringBuilder = new StringBuilder();
        while (beginIndex + len <= end) {
            stringBuilder = stringBuilder.append(Integer.parseInt(binaryString.substring(beginIndex, beginIndex + len), 2));
            stringBuilder = stringBuilder.append(".");
            beginIndex += len;
        }
        String result = stringBuilder.toString();
        if (result.charAt(result.length() - 1) == '.') {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private boolean fullMatch(Set<IP> ips, char c, int i) {
        for (IP ip : ips) {
            if (ip.toBinaryString().charAt(i) != c) {
                return false;
            }
        }
        return true;
    }


    private class IP implements Comparable<IP> {
        private int[] value;
        private String forString;
        private String forBinary;

        public IP(String ipString) {
            String[] array = ipString.split("\\.");
            if (array.length != 4) {
                throw new IllegalArgumentException(ipString + " not a valid ip.");
            }
            int[] temp = new int[4];
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                String s = array[i];
                temp[i] = Integer.valueOf(s);
                if (temp[i] > 255) {
                    throw new IllegalArgumentException(s + " not a valid ip part.");
                }
                String bs = Integer.toBinaryString(temp[i]);
                int left = 8 - bs.length();
                for (int j = 0; j < left; j++) {
                    stringBuilder = stringBuilder.append("0");
                }
                stringBuilder = stringBuilder.append(bs);
            }
            this.value = temp;
            this.forString = ipString;
            this.forBinary = stringBuilder.toString();
        }

        @Override
        public String toString() {
            return forString;
        }

        public String toBinaryString() {
            return forBinary;
        }

        @Override
        public int compareTo(IP other) {
            return this.toBinaryString().compareTo(other.toBinaryString());
        }

        @Override
        public int hashCode() {
            return this.toBinaryString().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof IP) {
                IP other = (IP) obj;
                return this.toBinaryString().equals(other.toBinaryString());
            }
            return false;
        }
    }
}
