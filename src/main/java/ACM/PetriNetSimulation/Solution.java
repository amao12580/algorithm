package ACM.PetriNetSimulation;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/7/12
 * Time:18:07
 * <p>
 * 你的任务是模拟Petri网的变迁。Petri网包含NP个库所（用P1，P2…表示）和NT个变迁
 * （用T1，T2…表示）。0<NP, NT<100。当每个变迁的每个输入库所都至少有一个token时，
 * 变迁是允许的。变迁发生的结果是每个输入库所减少一个token，每个输出库所增加一个
 * token。变迁的发生是原子性的，即所有token的增加和减少应同时进行。注意，一个变迁可
 * 能有多个相同的输入或者输出。如果一个库所在变迁的输入库所列表中出现了两次，则token
 * 会减少两个。输出库所也是类似。如果有多个变迁是允许的，一次只能发生一个。
 * 如图6-24所示，一开始只有T1是允许的，发生一次T1变迁之后有一个token会从P1移动
 * 到P2，但仍然只有T1是允许的，因为T2要求P2有两个token。再发生一次T1变迁之后P1中只
 * 剩一个token，而P2中有两个，因为T1和T2都可以发生。假定T2发生，则P2中不再有token，
 * 而P3中有一个token，因此T1和T3都是允许的。
 * <p>
 * 输入一个Petri网络。初始时每个库所都有一个token。每个变迁用一个整数序列表示，负
 * 数表示输入库所，正数表示输出库所。每个变迁至少包含一个输入和一个输出。最后输入一
 * 个整数NF，表示要发生NF次变迁（同时有多个变迁允许时可以任选一个发生，输入保证这
 * 个选择不会影响最终结果）。
 * <p>
 * Petri Net Simulation, ACM/ICPC World Finals 1998, UVa 804
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=745
 * <p>
 * Sample Input
 * 2
 * 1 0
 * 2
 * -1 2 0
 * -2 1 0
 * 100
 * 3
 * 3 0 0
 * 3
 * -1 2 0
 * -2 -2 3 0
 * -3 1 0
 * 100
 * 0
 * Sample Output
 * Case 1: still live after 100 transitions
 * Places with tokens: 1 (1)
 * Case 2: dead after 9 transitions
 * Places with tokens: 2 (1)
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        solution.case2();
    }

    private void case1() {
        int[] token2P = {1, 0};
        int[][] T2P = {{-1, 2, 0}, {-2, 1, 0}};
        System.out.print("Case 1: ");
        simulation(2, token2P, 2, T2P, 100);
    }

    private void case2() {
        int[] token2P = {3, 0, 0};
        int[][] T2P = {{-1, 2, 0}, {-2, -2, 3, 0}, {-3, 1, 0}};
        System.out.print("Case 2: ");
        simulation(3, token2P, 3, T2P, 100);
    }

    /**
     * 模拟器
     *
     * @param PNum    place总数量
     * @param token2P 每个place的token数量分布
     * @param TNum    transition总数量
     * @param T2P     transition与place的相对位置关系，以及转换关系
     * @param maxNF   最大变换次数
     */
    private void simulation(int PNum, int[] token2P, int TNum, int[][] T2P, int maxNF) {
        Map<Integer, Place> places = new HashMap<>();
        for (int i = 1; i <= PNum; i++) {
            places.put(i, new Place(i, token2P[i - 1]));
        }
        int[] T2PDesc;
        List<Transition> transitions = new ArrayList<>();
        List<Place> inputs, outputs;
        for (int i = 1; i <= TNum; i++) {
            T2PDesc = T2P[i - 1];
            inputs = new LinkedList<>();
            outputs = new LinkedList<>();
            for (int v : T2PDesc) {
                if (v == 0) {
                    break;
                }
                if (v < 0) {
                    inputs.add(places.get(Math.abs(v)));
                } else {
                    outputs.add(places.get(v));
                }
            }
            transitions.add(new Transition(i, inputs, outputs, 1, 1));
        }
        int currentNFOdd = maxNF;
        int effectNF = 0;
        while (currentNFOdd >= 0) {
            Transition firstCanWorkTransition = getFirstCanWorkTransition(transitions);
            if (currentNFOdd == 0) {
                if (firstCanWorkTransition != null) {
                    System.out.println("still live after " + maxNF + " transitions");
                    break;
                }
            }
            if (firstCanWorkTransition == null) {
                System.out.println("dead after " + effectNF + " transitions");
                break;
            }
            if (firstCanWorkTransition.work()) {
                effectNF++;
            }
            currentNFOdd--;
        }
        printTokenLocation(places);
    }

    private void printTokenLocation(Map<Integer, Place> places) {
        Collection<?> values = places.values();
        Place place;
        int num;
        for (Object obj : values) {
            place = (Place) obj;
            num = place.getTokenNum();
            if (num > 0) {
                System.out.println("Places with tokens: " + place.getNo() + " (" + num + ")");
            }
        }
    }


    private Transition getFirstCanWorkTransition(List<Transition> transitions) {
        for (Transition transition : transitions) {
            if (transition.canWork()) {
                return transition;
            }
        }
        return null;
    }
}

class Place {
    private int no;
    private int tokenNum;

    public Place(int no, int tokenNum) {
        this.no = no;
        this.tokenNum = tokenNum;
    }

    public int getNo() {
        return no;
    }

    public int getTokenNum() {
        return tokenNum;
    }

    public void addToken(int num) {
        this.tokenNum += num;
    }

    public void minusToken(int num) {
        this.tokenNum -= num;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Place) {
            Place other = (Place) o;
            return this.hashCode() == other.hashCode();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (no + "").hashCode();
    }
}

class Transition {
    private int no;
    private List<Place> input;
    Map<Place, Integer> inputRepeat;
    private List<Place> output;
    private int inputThreshold;
    private int outNum;

    public Transition(int no, List<Place> input, List<Place> output, int inputThreshold, int outNum) {
        this.no = no;
        this.input = input;
        this.output = output;
        this.inputThreshold = inputThreshold;
        this.outNum = outNum;
    }

    public int getNo() {
        return no;
    }

    public int getInputThreshold() {
        return inputThreshold;
    }

    public int getOutNum() {
        return outNum;
    }

    public List<Place> getInput() {
        return input;
    }

    public List<Place> getOutput() {
        return output;
    }

    private void buildInputRepeat() {
        if (inputRepeat == null) {
            inputRepeat = new HashMap<>();
            for (Place place : input) {
                Integer num = inputRepeat.get(place);
                if (num == null) {
                    inputRepeat.put(place, 1);
                } else {
                    inputRepeat.put(place, num + 1);
                }
            }
        }
    }

    public boolean canWork() {
        buildInputRepeat();
        for (Map.Entry<Place, Integer> entry : inputRepeat.entrySet()) {
            Place place = entry.getKey();
            Integer num = entry.getValue();
            if (place.getTokenNum() < num * getInputThreshold()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 返回值：true表示成功变换一次，false表示失败了
     */
    public boolean work() {
        if (canWork()) {
            List<Place> input = getInput();
            for (Place place : input) {
                place.minusToken(getInputThreshold());
            }
            List<Place> output = getOutput();
            for (Place place : output) {
                place.addToken(getOutNum());
            }
            return true;
        } else {
            return false;
        }
    }
}