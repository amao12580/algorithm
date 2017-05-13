package ACM.Exchange;

import basic.Util;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/11
 * Time:18:34
 * <p>
 * 你的任务是为交易所设计一个订单处理系统。要求支持以下3种指令。
 * BUY p q：有人想买，数量为p，价格为q。
 * SELL p q：有人想卖，数量为p，价格为q。
 * CANCEL i：取消第i条指令对应的订单（输入保证该指令是BUY或者SELL）。
 * 交易规则如下：对于当前买订单，若当前最低卖价（ask price）低于当前出价，则发生
 * 交易；对于当前卖订单，若当前最高买价（bid price）高于当前价格，则发生交易。发生交
 * 易时，按供需物品个数的最小值交易。交易后，需修改订单的供需物品个数。当出价或价格
 * 相同时，按订单产生的先后顺序发生交易。输入输出细节请参考原题。
 * <p>
 * <p>
 * Sample Input
 * 11
 * BUY 100 35
 * CANCEL 1
 * BUY 100 34
 * SELL 150 36
 * SELL 300 37
 * SELL 100 36
 * BUY 100 38
 * CANCEL 4
 * CANCEL 7
 * BUY 200 32
 * SELL 500 30
 * Sample Output
 * QUOTE 100 35 - 0 99999
 * QUOTE 0 0 - 0 99999
 * QUOTE 100 34 - 0 99999
 * QUOTE 100 34 - 150 36
 * QUOTE 100 34 - 150 36
 * QUOTE 100 34 - 250 36
 * TRADE 100 36
 * QUOTE 100 34 - 150 36   trade 100 36
 * QUOTE 100 34 - 100 36   cancel 4
 * QUOTE 100 34 - 100 36   cancel 7
 * QUOTE 100 34 - 100 36
 * TRADE 100 34
 * TRADE 200 32
 * QUOTE 0 0 - 200 30
 * <p>
 * <p>
 * Exchange, ACM/ICPC NEERC 2006,
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=4473
 */
public class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("*************************************************************************");
        solution.resetCid();
        solution.case2();
    }

    private void resetCid() {
        nextCid = 0;
    }

    private void case2() {
        int cLen = Util.getRandomInteger(10, 100);
        Command[] commands = new Command[cLen];
        List<Integer> cids = new ArrayList<>();
        Set<Integer> cancelCids = new HashSet<>();
        int[] p;
        int[] p1;
        for (int i = 0; i < cLen; ) {
            CommandType type = CommandType.getRandomOne();
            if (type.equals(CommandType.CANCEL)) {
                if (cids.isEmpty()) {
                    continue;
                } else {
                    p = new int[1];
                    p[0] = cids.get(Util.getRandomInteger(0, cids.size() - 1));
                    Command command = new Command(type, p);
                    if (!cancelCids.add(command.getCid())) {
                        continue;
                    }
                    commands[i] = command;
                }
            } else {
                p1 = new int[2];
                p1[0] = Util.getRandomInteger(1, 1000);
                p1[1] = Util.getRandomInteger(1, 100000);
                commands[i] = new Command(type, p1);
                cids.add(commands[i].getCid());
            }
            i++;
        }
        trade(commands);
    }

    private void case1() {
        Command[] commands = new Command[11];
        commands[0] = new Command(CommandType.BUY, new int[]{100, 35});
        commands[1] = new Command(CommandType.CANCEL, new int[]{1});
        commands[2] = new Command(CommandType.BUY, new int[]{100, 34});
        commands[3] = new Command(CommandType.SELL, new int[]{150, 36});
        commands[4] = new Command(CommandType.SELL, new int[]{300, 37});
        commands[5] = new Command(CommandType.SELL, new int[]{100, 36});
        commands[6] = new Command(CommandType.BUY, new int[]{100, 38});
        commands[7] = new Command(CommandType.CANCEL, new int[]{4});
        commands[8] = new Command(CommandType.CANCEL, new int[]{7});
        commands[9] = new Command(CommandType.BUY, new int[]{200, 32});
        commands[10] = new Command(CommandType.SELL, new int[]{500, 30});
        trade(commands);
    }


    private void trade(Command[] commands) {
        for (Command command : commands) {
            System.out.println(command.toString());
        }
        System.out.println("-------------------------------------------------------------");
        for (Command command : commands) {
            trade(command);
        }
    }

    Map<Integer, Order> orders = new LinkedHashMap<>();//所有指令，不删除
    Map<Integer, List<Order>> priceToOrder = new HashMap<>();//有效的价格与订单映射
    PriorityQueue<Order> buys = new PriorityQueue<>();//所有有效的购买订单
    PriorityQueue<Order> sells = new PriorityQueue<>();//所有有效的销售订单

    private void addPriceToOrder(Order order) {
        int price = order.getPrice();
        List<Order> orderList = priceToOrder.get(price);
        if (orderList == null) {
            orderList = new ArrayList<>();
            orderList.add(order);
            priceToOrder.put(price, orderList);
        } else {
            orderList.add(order);
        }
    }

    private int getSamePriceSumSize(int price) {
        List<Order> orderList = priceToOrder.get(price);
        int sum = 0;
        for (Order order : orderList) {
            sum += order.getSize();
        }
        return sum;
    }

    private void removePriceToOrder(Order order) {
        List<Order> orderList = priceToOrder.get(order.getPrice());
        if (orderList != null && !orderList.isEmpty()) {
            Iterator<Order> iterator = orderList.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().equals(order)) {
                    iterator.remove();
                    return;
                }
            }
        }
    }

    private void trade(Command command) {
        int[] param = command.getParam();
        Order order;
        switch (command.getType()) {
            case BUY:
                order = new Order(command.getCid(), CommandType.BUY, param[0], param[1]);
                buys.add(order);
                orders.put(command.getCid(), order);
                addPriceToOrder(order);
                doBuy(buys.peek());
                break;
            case SELL:
                order = new Order(command.getCid(), CommandType.SELL, param[0], param[1]);
                sells.add(order);
                orders.put(command.getCid(), order);
                addPriceToOrder(order);
                doSell(sells.peek());
                break;
            case CANCEL:
                doCancel(param[0]);
                break;
        }
    }


    private void doBuy(Order buyOrder) {
        if (sells.isEmpty()) {
            printQuote(buyOrder.getSize(), buyOrder.getPrice(), 0, 99999);
        } else {
            Order lowestPriceSellOrder = sells.peek();
            if (lowestPriceSellOrder.getPrice() < buyOrder.getPrice()) {
                doDeal(buyOrder, lowestPriceSellOrder, lowestPriceSellOrder.getPrice());
                if (!buys.isEmpty()) {
                    doBuy(buys.peek());
                } else {
                    if (sells.isEmpty()) {
                        printQuote(0, 0, 0, 99999);
                    } else {
                        Order newLowestPriceSellOrder = sells.peek();
                        printQuote(0, 0, newLowestPriceSellOrder.getSize(), newLowestPriceSellOrder.getPrice());
                    }
                }
            } else {
                Order highestPriceBuyOrder = buys.peek();
                Order newLowestPriceSellOrder = sells.peek();
                printQuote(getSamePriceSumSize(highestPriceBuyOrder.getPrice()), highestPriceBuyOrder.getPrice(), getSamePriceSumSize(newLowestPriceSellOrder.getPrice()), newLowestPriceSellOrder.getPrice());
            }
        }
    }

    private void printQuote(int buySize, int buyPrice, int sellSize, int sellPrice) {
        System.out.println("QUOTE " + buySize + " " + buyPrice + " - " + sellSize + " " + sellPrice);
    }

    private void doSell(Order sellOrder) {
        if (buys.isEmpty()) {
            printQuote(0, 0, sellOrder.getSize(), sellOrder.getPrice());
        } else {
            Order highestPriceBuyOrder = buys.peek();
            if (highestPriceBuyOrder.getPrice() > sellOrder.getPrice()) {
                doDeal(highestPriceBuyOrder, sellOrder, highestPriceBuyOrder.getPrice());
                if (!sells.isEmpty()) {
                    doSell(sells.peek());
                } else {
                    if (buys.isEmpty()) {
                        printQuote(0, 0, 0, 99999);
                    } else {
                        Order newHighestPriceBuyOrder = buys.peek();
                        printQuote(0, 0, newHighestPriceBuyOrder.getSize(), newHighestPriceBuyOrder.getPrice());
                    }
                }
            } else {
                Order newHighestPriceBuyOrder = buys.peek();
                Order lowestPriceSellOrder = sells.peek();
                printQuote(getSamePriceSumSize(newHighestPriceBuyOrder.getPrice()), newHighestPriceBuyOrder.getPrice(), getSamePriceSumSize(lowestPriceSellOrder.getPrice()), lowestPriceSellOrder.getPrice());
            }
        }
    }

    private void doDeal(Order buy, Order sell, int price) {
        boolean isBuyOver;//对于购买者，是否完成了采购？
        boolean isSellOver;//对于销售者，是否完成了售卖？
        int size;
        if (buy.getSize() != sell.getSize()) {
            if (buy.getSize() > sell.getSize()) {
                isBuyOver = false;
                isSellOver = true;
                size = sell.getSize();
                buy.setSize(buy.getSize() - sell.getSize());
            } else {
                isBuyOver = true;
                isSellOver = false;
                size = buy.getSize();
                sell.setSize(sell.getSize() - buy.getSize());
            }
        } else {
            size = buy.getSize();
            isBuyOver = true;
            isSellOver = true;
        }
        if (isBuyOver) {
            buys.remove(buy);
            removePriceToOrder(buy);
        }
        if (isSellOver) {
            sells.remove(sell);
            removePriceToOrder(sell);
        }
        System.out.println("TRADE " + size + " " + price);
    }

    private void doCancel(int cid) {
        Order order = orders.get(cid);
        if (order != null && order.canCancel()) {
            buys.remove(order);
            sells.remove(order);
            removePriceToOrder(order);
            if (buys.isEmpty()) {
                if (!sells.isEmpty()) {
                    doSell(sells.peek());
                } else {
                    printQuote(0, 0, 0, 99999);
                }
            } else {
                if (sells.isEmpty()) {
                    doBuy(buys.peek());
                } else {
                    Order pBuy = buys.peek();
                    Order pSell = sells.peek();
                    if (pBuy.compareTo(pSell) <= 0) {
                        doBuy(pBuy);
                    } else {
                        doSell(pSell);
                    }
                }
            }
        } else {
            System.out.println(order == null ? "cid " + cid + " not match any order." : "order " + order.toString() + " not allow cancel.");
        }
    }

    private class Order implements Comparable<Order> {
        private transient String no;
        private transient int cid;
        private CommandType type;
        private int size;
        private int price;

        public Order(int cid, CommandType type, int size, int price) {
            this.cid = cid;
            this.no = cid + "";
            this.type = type;
            this.size = size;
            this.price = price;
        }

        public int getCid() {
            return cid;
        }

        public String getNo() {
            return no;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getPrice() {
            return price;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Order other = (Order) o;
            return this.getNo().equals(other.getNo());
        }

        @Override
        public int hashCode() {
            return this.getNo().hashCode();
        }

        @Override
        public int compareTo(Order other) {
            if (this.type.equals(other.type)) {
                int order;
                if (this.type.equals(CommandType.SELL)) {//按价格升序
                    order = this.getPrice() - other.getPrice();
                } else {//按价格降序
                    order = other.getPrice() - this.getPrice();
                }
                if (order != 0) {
                    return order;
                }
                //价格相等，按照先后 顺序
                return this.getCid() - other.getCid();
            } else {
                return this.getCid() - other.getCid();
            }
        }

        public boolean canCancel() {
            return !this.type.equals(CommandType.CANCEL);
        }

        @Override
        public String toString() {
            return Util.toJson(this);
        }
    }


    private int nextCid = 0;

    public int getNextCid() {
        nextCid++;
        return nextCid;
    }

    private class Command {
        private int cid;
        private CommandType type;
        private int[] param;

        public int getCid() {
            return cid;
        }

        public CommandType getType() {
            return type;
        }

        public int[] getParam() {
            return param;
        }

        public Command(CommandType type, int[] param) {
            this.cid = getNextCid();
            this.type = type;
            this.param = param;
        }

        @Override
        public String toString() {
            return this.type.toString() + " " + paramToString();
        }

        private String paramToString() {
            if (this.type.equals(CommandType.CANCEL)) {
                return this.param[0] + "";
            } else {
                return this.param[0] + " " + this.param[1];
            }
        }
    }

    private enum CommandType {
        BUY,
        SELL,
        CANCEL;

        public static CommandType getRandomOne() {
            CommandType[] commands = CommandType.values();
            return commands[Util.getRandomInteger(0, commands.length - 1)];
        }
    }
}
