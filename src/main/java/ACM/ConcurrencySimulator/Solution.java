package ACM.ConcurrencySimulator;

import basic.Util;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/16
 * Time:9:40
 * <p>
 * <p>
 * 你的任务是模拟n个程序（按输入顺序编号为1～n）的并行执行。每个程序包含不超过
 * 25条语句，格式一共有5种：var = constant（赋值）；print var（打印）；lock；unlock；end。
 * 变量用单个小写字母表示，初始为0，为所有程序公有（因此在一个程序里对某个变量
 * 赋值可能会影响另一个程序）。常数是小于100的非负整数。
 * 每个时刻只能有一个程序处于运行态，其他程序均处于等待态。上述5种语句分别需
 * 要t1、t2、t3、t4、t5单位时间。运行态的程序每次最多运行Q个单位时间（称为配额）。当
 * 一个程序的配额用完之后，把当前语句（如果存在）执行完之后该程序会被插入一个等待队
 * 列中，然后处理器从队首取出一个程序继续执行。初始等待队列包含按输入顺序排列的各个
 * 程序，但由于lock/unlock语句的出现，这个顺序可能会改变。
 * lock的作用是申请对所有变量的独占访问。lock和unlock总是成对出现，并且不会嵌套。
 * lock总是在unlock的前面。当一个程序成功执行完lock指令之后，其他程序一旦试图执行lock
 * 指令，就会马上被放到一个所谓的阻止队列的尾部（没有用完的配额就浪费了）。当unlock
 * 执行完毕后，阻止队列的第一个程序进入等待队列的首部。
 * 输入n, t1, t2, t3, t4, t5, Q以及n个程序，按照时间顺序输出所有print语句的程序编号和结
 * 果。
 * <p>
 * <p>
 * Concurrency Simulator, ACM/ICPC World Finals 1991, UVa210
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=146
 * <p>
 * 这道题有一个隐藏点：print与end指令，不受lock指令影响，即：lock指令只影响写操作（assign/lock/unlock）
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("--------------------------------------------------------------------------------");
        Solution solution2 = new Solution();
        solution2.case2();
    }

    private void case2() {
        int n = Util.getRandomInteger(2, 100);
        List<String> statements = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            statements.addAll(generate());
        }
        String[] result = new String[statements.size()];
        result = statements.toArray(result);
        simulator(n, Util.getRandomInteger(1, 100), Util.getRandomInteger(1, 100), Util.getRandomInteger(1, 100), Util.getRandomInteger(1, 100), Util.getRandomInteger(1, 100), Util.getRandomInteger(1, 100), result);
    }

    private List<String> generate() {
        int n = Util.getRandomInteger(1, 25);
        List<String> statements = new LinkedList<>();
        List<String> names = new ArrayList<>();
        boolean lockShowed = false;
        for (int i = 0; i < n - 1; ) {
            StatementType type = StatementType.getRandomType();
            String str = type.toString().toLowerCase();
            if (type.equals(StatementType.ASSIGNMENT)) {
                String name = Util.generateLowerLetterString(1);
                names.add(name);
                int value = Util.getRandomInteger(0, 100);
                str = name + " = " + value;
            } else if (type.equals(StatementType.PRINT)) {
                if (names.isEmpty()) {
                    continue;
                }
                str = str + " " + names.get(Util.getRandomInteger(0, names.size() - 1));
            } else if (type.equals(StatementType.LOCK)) {
                if (lockShowed) {
                    continue;
                }
                lockShowed = true;
            } else if (type.equals(StatementType.UNLOCK)) {
                if (!lockShowed) {
                    continue;
                }
                lockShowed = false;
            } else {
                continue;
            }
            statements.add(str);
            i++;
        }
        if (lockShowed) {
            statements.add(StatementType.UNLOCK.toString().toLowerCase());
        }
        statements.add(StatementType.END.toString().toLowerCase());
        return statements;
    }

    private void case1() {
        String[] statements = new String[26];
        statements[0] = "a = 4";
        statements[1] = "print a";
        statements[2] = "lock";
        statements[3] = "b = 9";
        statements[4] = "print b";
        statements[5] = "unlock";
        statements[6] = "print b";
        statements[7] = "end";

        statements[8] = "a = 3";
        statements[9] = "print a";
        statements[10] = "lock";
        statements[11] = "b = 8";
        statements[12] = "print b";
        statements[13] = "unlock";
        statements[14] = "print b";
        statements[15] = "end";

        statements[16] = "b = 5";
        statements[17] = "a = 17";
        statements[18] = "print a";
        statements[19] = "print b";
        statements[20] = "lock";
        statements[21] = "b = 21";
        statements[22] = "print b";
        statements[23] = "unlock";
        statements[24] = "print b";
        statements[25] = "end";

        simulator(3, 1, 1, 1, 1, 1, 1, statements);
    }

    private Map<Character, Byte> shareVariables = new HashMap<>();
    private Procedure lock = null;

    public boolean isLockedByOther(Procedure procedure) {
        return lock != null && !lock.equals(procedure);
    }

    public boolean lock(Procedure procedure) {
        if (lock == null) {
            lock = procedure;
            return true;
        }
        return false;
    }

    public void unLock() {
        lock = null;
    }

    private LinkedList<Procedure> waitUnlockProcedures = new LinkedList<>();
    private ConcurrentLinkedDeque<Procedure> waitExecuteProcedures = new ConcurrentLinkedDeque<>();

    private int t1;
    private int t2;
    private int t3;
    private int t4;
    private int t5;
    private int Q;

    private void simulator(int n, int t1, int t2, int t3, int t4, int t5, int Q, String[] statements) {
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.t4 = t4;
        this.t5 = t5;
        this.Q = Q;
        System.out.println("n:" + n + ",t1:" + t1 + ",t2:" + t2 + ",t3:" + t3 + ",t4:" + t4 + ",t5:" + t5 + ",Q:" + Q);
        print(statements);
        waitExecuteProcedures = parseProcedure(statements);
        while (!waitExecuteProcedures.isEmpty() || !waitExecuteProcedures.isEmpty()) {
            for (Procedure procedure : waitExecuteProcedures) {
                int ret = procedure.execute();
                waitExecuteProcedures.remove(procedure);
                if (ret == Result.FINISHED) {//全部执行完了
                    continue;
                }
                if (ret == Result.LOCK_BY_OTHER) {//被其他的lock语句阻塞
                    waitUnlockProcedures.add(procedure);
                } else if (ret == Result.HALF_STOP) {//cpu时间配额用完了，但没有执行完
                    waitExecuteProcedures.addLast(procedure);
                } else {
                    throw new IllegalArgumentException("wrong ret:" + ret);
                }
            }
        }
    }

    private void print(String[] statements) {
        System.out.println();
        for (String str : statements) {
            System.out.println(str);
        }
        System.out.println();
    }

    private void unLockWaits() {
        while (!waitUnlockProcedures.isEmpty()) {
            waitExecuteProcedures.addFirst(waitUnlockProcedures.pollLast());
        }
    }

    private class Result {
        public static final int OK = 0;
        public static final int FINISHED = -1;
        public static final int LOCK_BY_OTHER = -2;
        public static final int HALF_STOP = -3;
        public static final int EXCEPTION = -4;
    }

    private ConcurrentLinkedDeque<Procedure> parseProcedure(String[] statements) {
        ConcurrentLinkedDeque<Procedure> result = new ConcurrentLinkedDeque<>();
        String assign = "=";
        String empty = " ";
        LinkedList<Statement> list = new LinkedList<>();
        for (String item : statements) {
            int p = item.indexOf(assign);
            Statement statement;
            int endIndex = item.length() - 1;
            if (p > 0) {
                if (p == endIndex) {
                    throw new IllegalArgumentException(item + " is invalid.");
                }
                String[] parts = item.split(assign);
                if (parts.length != 2) {
                    throw new IllegalArgumentException(item + " is invalid.");
                }
                Object[] param = new Object[2];
                param[0] = parts[0].replace(empty, "").charAt(0);
                param[1] = Byte.valueOf(parts[1].replace(empty, ""));
                statement = new Statement(StatementType.ASSIGNMENT, param);
            } else {
                p = item.indexOf(empty);
                if (p > 0) {
                    if (p == endIndex) {
                        throw new IllegalArgumentException(item + " is invalid.");
                    }
                    String[] parts = item.split(empty);
                    if (parts.length != 2) {
                        throw new IllegalArgumentException(item + " is invalid.");
                    }
                    Object[] param = new Object[2];
                    param[0] = parts[1].charAt(0);
                    statement = new Statement(StatementType.PRINT, param);
                } else {
                    item = item.toUpperCase();
                    if (item.equals(StatementType.LOCK.toString())) {
                        statement = new Statement(StatementType.LOCK, null);
                    } else if (item.equals(StatementType.UNLOCK.toString())) {
                        statement = new Statement(StatementType.UNLOCK, null);
                    } else if (item.equals(StatementType.END.toString())) {
                        statement = new Statement(StatementType.END, null);
                    } else {
                        throw new IllegalArgumentException(item.toLowerCase() + " is invalid.");
                    }
                }
            }
            list.add(statement);
            if (statement.isEnd()) {
                Procedure procedure = new Procedure(list);
                result.addLast(procedure);
                list = new LinkedList<>();
            }
        }
        return result;
    }

    private int nextProcedureId = 0;

    public int getNextProcedureId() {
        nextProcedureId++;
        return nextProcedureId;
    }

    private enum StatementType {
        ASSIGNMENT,
        PRINT,
        LOCK,
        UNLOCK,
        END;

        public static StatementType getRandomType() {
            StatementType[] commands = StatementType.values();
            return commands[Util.getRandomInteger(0, commands.length - 1)];
        }
    }

    private class Statement {
        private StatementType type;
        private Object[] param;

        public Statement(StatementType type, Object[] param) {
            this.type = type;
            this.param = param;
        }

        public boolean isEnd() {
            return type.equals(StatementType.END);
        }

        private int execute(Procedure procedure) {
            Character name;
            Byte oldValue;
            Byte newValue;
            switch (type) {
                case ASSIGNMENT:
                    name = (Character) param[0];
                    newValue = (Byte) param[1];
                    if (isLockedByOther(procedure)) {
                        return Result.LOCK_BY_OTHER;
                    }
                    shareVariables.put(name, newValue);
                    procedure.cutCPUTime(t1);
                    return Result.OK;
                case PRINT:
                    name = (Character) param[0];
                    oldValue = shareVariables.get(name);
                    System.out.println(procedure.getId() + ": " + (oldValue == null ? (name + " is undefined.") : oldValue));
                    procedure.cutCPUTime(t2);
                    return Result.OK;
                case LOCK:
                    if (isLockedByOther(procedure)) {
                        return Result.LOCK_BY_OTHER;
                    } else {
                        lock(procedure);
                    }
                    procedure.cutCPUTime(t3);
                    return Result.OK;
                case UNLOCK:
                    if (isLockedByOther(procedure)) {
                        return Result.LOCK_BY_OTHER;
                    } else {
                        unLock();
                    }
                    unLockWaits();
                    procedure.cutCPUTime(t4);
                    return Result.OK;
                case END:
                    procedure.cutCPUTime(t5);
                    return Result.FINISHED;
                default:
                    return Result.EXCEPTION;
            }
        }
    }

    private class Procedure {
        private int id;
        LinkedList<Statement> statements = new LinkedList<>();
        private int currentExecuteIndex = -1;
        private int executeEndIndex = -1;
        private int currentCPUSumTime = -1;

        public Procedure(LinkedList<Statement> statements) {
            this.id = getNextProcedureId();
            this.statements = statements;
            this.executeEndIndex = statements.size() - 1;
        }

        public int execute() {
            currentCPUSumTime = Q;
            return executeLoop();
        }

        private void cutCPUTime(int time) {
            currentCPUSumTime -= time;
        }

        public int executeLoop() {
            if (currentCPUSumTime < 0) {
                return Result.HALF_STOP;
            }
            if (currentExecuteIndex < 0) {
                currentExecuteIndex = 0;
            }
            if (currentExecuteIndex > executeEndIndex) {
                return Result.FINISHED;
            }
            int ret = statements.get(currentExecuteIndex).execute(this);
            if (ret == Result.OK) {
                currentExecuteIndex++;
                if (currentCPUSumTime <= 0) {
                    return Result.HALF_STOP;
                }
                return executeLoop();
            } else {
                return ret;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Procedure procedure = (Procedure) o;
            return id == procedure.id;
        }

        @Override
        public int hashCode() {
            return id;
        }

        public int getId() {
            return id;
        }
    }
}
