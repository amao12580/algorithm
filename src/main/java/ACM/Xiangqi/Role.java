package ACM.Xiangqi;

import basic.Util;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/4/13
 * Time:16:13
 */
public enum Role {
    NULL,
    J,//黑将
    G {
        @Override
        public boolean canAttack(Role[][] chess, int thisColumnIndex, int thisRowIndex, int otherColumnIndex, int otherRowIndex) {
            return (thisRowIndex == otherRowIndex || thisColumnIndex == otherColumnIndex) && !hasMiddleMen(chess, thisColumnIndex, thisRowIndex, otherColumnIndex, otherRowIndex);
        }
    },//红帅
    R {
        @Override
        public boolean canAttack(Role[][] chess, int thisColumnIndex, int thisRowIndex, int otherColumnIndex, int otherRowIndex) {
            return !hasMiddleMen(chess, thisColumnIndex, thisRowIndex, otherColumnIndex, otherRowIndex);
        }
    },//车
    H {
        @Override
        public boolean canAttack(Role[][] chess, int thisColumnIndex, int thisRowIndex, int otherColumnIndex, int otherRowIndex) {
            int rowMax = chess[0].length - 1;
            int columnMax = chess.length - 1;
            //上左
            if (thisRowIndex - 1 >= 0 && chess[thisRowIndex - 1][thisColumnIndex] == null && thisRowIndex - 2 >= 0 && thisColumnIndex - 1 >= 0 && thisRowIndex - 2 == otherRowIndex && thisColumnIndex - 1 == otherColumnIndex) {
                return true;
            }
            //上右
            if (thisRowIndex - 1 >= 0 && chess[thisRowIndex - 1][thisColumnIndex] == null && thisRowIndex - 2 >= 0 && thisColumnIndex + 1 <= rowMax && thisRowIndex - 2 == otherRowIndex && thisColumnIndex + 1 == otherColumnIndex) {
                return true;
            }
            //下左
            if (thisRowIndex + 1 <= columnMax && chess[thisRowIndex + 1][thisColumnIndex] == null && thisRowIndex + 2 <= columnMax && thisColumnIndex - 1 >= 0 && thisRowIndex + 2 == otherRowIndex && thisColumnIndex - 1 == otherColumnIndex) {
                return true;
            }
            //下右
            if (thisRowIndex + 1 <= columnMax && chess[thisRowIndex + 1][thisColumnIndex] == null && thisRowIndex + 2 <= columnMax && thisColumnIndex + 1 <= rowMax && thisRowIndex + 2 == otherRowIndex && thisColumnIndex + 1 == otherColumnIndex) {
                return true;
            }
            //左上
            if (thisColumnIndex - 1 >= 0 && chess[thisRowIndex][thisColumnIndex - 1] == null && thisColumnIndex - 2 >= 0 && thisRowIndex - 1 >= 0 && thisColumnIndex - 2 == otherColumnIndex && thisRowIndex - 1 == otherRowIndex) {
                return true;
            }
            //左下
            if (thisColumnIndex - 1 >= 0 && chess[thisRowIndex][thisColumnIndex - 1] == null && thisColumnIndex - 2 >= 0 && thisRowIndex + 1 <= columnMax && thisColumnIndex - 2 == otherColumnIndex && thisRowIndex + 1 == otherRowIndex) {
                return true;
            }
            //右上
            if (thisColumnIndex + 1 <= rowMax && chess[thisRowIndex][thisColumnIndex + 1] == null && thisColumnIndex + 2 <= rowMax && thisRowIndex - 1 >= 0 && thisColumnIndex + 2 == otherColumnIndex && thisRowIndex - 1 == otherRowIndex) {
                return true;
            }
            //右下
            return thisColumnIndex + 1 <= rowMax && chess[thisRowIndex][thisColumnIndex + 1] == null && thisColumnIndex + 2 <= rowMax && thisRowIndex + 1 <= columnMax && thisColumnIndex + 2 == otherColumnIndex && thisRowIndex + 1 == otherRowIndex;
        }
    },//马
    C {
        @Override
        public boolean canAttack(Role[][] chess, int thisColumnIndex, int thisRowIndex, int otherColumnIndex, int otherRowIndex) {
            return hasOneMiddleMen(chess, thisColumnIndex, thisRowIndex, otherColumnIndex, otherRowIndex);
        }
    };//炮

    public static Role getRandomOne() {
        Role[] roles = Role.values();
        int endIndex = roles.length - 1;
        return Util.getRandomInteger(0, endIndex + 1) > endIndex - 1 ? roles[Util.getRandomInteger(1, endIndex)] : roles[0];
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public boolean canAttack(Role[][] chess, int thisColumnIndex, int thisRowIndex, int otherColumnIndex, int otherRowIndex) {
        return false;
    }

    protected boolean hasOneMiddleMen(Role[][] chess, int thisColumnIndex, int thisRowIndex, int otherColumnIndex, int otherRowIndex) {
        return findMiddleMenSum(chess, thisColumnIndex, thisRowIndex, otherColumnIndex, otherRowIndex) == 1;
    }

    /**
     * 判断是否2个棋子之间，是否存在棋子
     * <p>
     * 2个紧挨着的棋子，判定为false
     * <p>
     * 存在一个或多个隔断的，判断为true
     */
    protected boolean hasMiddleMen(Role[][] chess, int thisColumnIndex, int thisRowIndex, int otherColumnIndex, int otherRowIndex) {
        return findMiddleMenSum(chess, thisColumnIndex, thisRowIndex, otherColumnIndex, otherRowIndex) > 0;
    }

    protected int findMiddleMenSum(Role[][] chess, int thisColumnIndex, int thisRowIndex, int otherColumnIndex, int otherRowIndex) {
        int num = 0;
        if (thisColumnIndex == otherColumnIndex) {
            int start = -1;
            int end = -1;
            if (thisRowIndex < otherRowIndex) {
                start = thisRowIndex;
                end = otherRowIndex;
            }
            if (thisRowIndex > otherRowIndex) {
                start = otherRowIndex;
                end = thisRowIndex;
            }
            start++;
            for (int i = start; i >= 0 && i < end; i++) {
                if (chess[i][thisColumnIndex] != null) {
                    num++;
                }
            }
        }

        if (thisRowIndex == otherRowIndex) {
            int start = -1;
            int end = -1;
            if (thisColumnIndex < otherColumnIndex) {
                start = thisColumnIndex;
                end = otherColumnIndex;
            }
            if (thisColumnIndex > otherColumnIndex) {
                start = otherColumnIndex;
                end = thisColumnIndex;
            }
            start++;
            for (int i = start; i >= 0 && i < end; i++) {
                if (chess[thisRowIndex][i] != null) {
                    num++;
                }
            }
        }
        return num;
    }
}