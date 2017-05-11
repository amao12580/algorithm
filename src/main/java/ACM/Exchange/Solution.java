package ACM.Exchange;

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
 * Exchange, ACM/ICPC NEERC 2006,
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=4473
 */
public class Solution {
}
