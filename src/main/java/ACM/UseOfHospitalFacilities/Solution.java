package ACM.UseOfHospitalFacilities;

import basic.Util;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/13
 * Time:14:48
 * <p>
 * 医院里有n（n≤10）个手术室和m（m≤30）个恢复室。每个病人首先会被分配到一个手
 * 术室，手术后会被分配到一个恢复室。从任意手术室到任意恢复室的时间均为t 1 ，准备一个
 * 手术室和恢复室的时间分别为t 2 和t 3 （一开始所有手术室和恢复室均准备好，只有接待完一
 * 个病人之后才需要为下一个病人准备）。
 * k名（k≤100）病人按照花名册顺序排队，T点钟准时开放手术室。每当有准备好的手术
 * 室时，队首病人进入其中编号最小的手术室。手术结束后，病人应立刻进入编号最小的恢复
 * 室。如果有多个病人同时结束手术，在编号较小的手术室做手术的病人优先进入编号较小的
 * 恢复室。输入保证病人无须排队等待恢复室。
 * 输入n、m、T、t 1 、t 2 、t 3 、k和k名病人的名字、手术时间和恢复时间，模拟这个过程。
 * 输入输出细节请参考原题。
 * 提示：虽然是个模拟题，但是最好先理清思路，减少不必要的麻烦。本题是一个很好的
 * 编程练习，但难度也不小。
 * <p>
 * Use of Hospital Facilities, ACM/ICPC World Finals 1991, UVa212
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=148
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution1 = new Solution();
        solution1.case1();
        System.out.println("-------------------------------------------------------");
        Solution solution2 = new Solution();
        solution2.case2();
    }

    private void case2() {
        int pn = Util.getRandomInteger(1, 100);
        Patient[] patients = new Patient[pn];
        for (int i = 0; i < pn; i++) {
            patients[i] = new Patient(Util.generateMixedString(10), Util.getRandomInteger(1, 100), Util.getRandomInteger(1, 100));
        }
        surgeryAndRecovery(Util.getRandomInteger(1, 10), Util.getRandomInteger(1, 30), Util.getRandomInteger(0, 23), Util.getRandomInteger(5, 60), Util.getRandomInteger(15, 120), Util.getRandomInteger(15, 120), patients);
    }

    private void case1() {
        Patient[] patients = new Patient[16];

        patients[0] = new Patient("Jones", 28, 140);
        patients[1] = new Patient("Smith", 120, 200);
        patients[2] = new Patient("Thompson", 23, 75);
        patients[3] = new Patient("Albright", 19, 82);
        patients[4] = new Patient("Poucher", 133, 209);
        patients[5] = new Patient("Comer", 74, 101);
        patients[6] = new Patient("Perry", 93, 188);
        patients[7] = new Patient("Page", 111, 223);
        patients[8] = new Patient("Roggio", 69, 122);
        patients[9] = new Patient("Brigham", 42, 79);
        patients[10] = new Patient("Nute", 22, 71);
        patients[11] = new Patient("Young", 38, 140);
        patients[12] = new Patient("Bush", 26, 121);
        patients[13] = new Patient("Cates", 120, 248);
        patients[14] = new Patient("Johnson", 86, 181);
        patients[15] = new Patient("White", 92, 140);

        surgeryAndRecovery(5, 12, 7, 5, 15, 10, patients);
    }

    private int startSurgeryHour = 0;

    /**
     * @param surgeryRoomNum                手术室的数量
     * @param recoveryRoomNum               恢复室的数量
     * @param startSurgeryHour              开始第一台手术的时间，这里是小时，24小时制
     * @param surgeryTransferToRecoveryTime 完成手术花后转移到恢复室需要的时间
     * @param prepareOperatingRoomTime      为下一个病人准备手术室的时间
     * @param prepareRecoveryRoomTime       为下一个病人准备恢复室的时间
     * @param patients                      所有病人信息
     */
    private void surgeryAndRecovery(int surgeryRoomNum,
                                    int recoveryRoomNum,
                                    int startSurgeryHour,
                                    int surgeryTransferToRecoveryTime,
                                    int prepareOperatingRoomTime,
                                    int prepareRecoveryRoomTime,
                                    Patient[] patients) {
        System.out.println("surgeryRoomNum:" + surgeryRoomNum + ",recoveryRoomNum:" + recoveryRoomNum
                + ",startSurgeryHour:" + startSurgeryHour
                + ",surgeryTransferToRecoveryTime:" + surgeryTransferToRecoveryTime
                + ",prepareOperatingRoomTime:" + prepareOperatingRoomTime
                + ",prepareRecoveryRoomTime:" + prepareRecoveryRoomTime);
        this.startSurgeryHour = startSurgeryHour;
        List<Patient> AllPatients = new ArrayList<>();
        PriorityQueue<Patient> waitForSurgeryPatients = new PriorityQueue<>();
        Collections.addAll(waitForSurgeryPatients, patients);
        Collections.addAll(AllPatients, patients);
        List<Room> AllRooms = new ArrayList<>();
        List<Room> surgeryRooms = new ArrayList<>();
        for (int i = 0; i < surgeryRoomNum; i++) {
            surgeryRooms.add(new Room(true));
        }
        List<Room> recoveryRooms = new ArrayList<>();
        for (int i = 0; i < recoveryRoomNum; i++) {
            recoveryRooms.add(new Room(false));
        }
        AllRooms.addAll(surgeryRooms);
        AllRooms.addAll(recoveryRooms);
        List<Patient> waitForRecoveryPatients = new ArrayList<>();
        List<Patient> inSurgeryPatients = new ArrayList<>();
        List<Patient> inRecoveryPatients = new ArrayList<>();
        List<Patient> inTransferPatients = new ArrayList<>();
        while (!waitForSurgeryPatients.isEmpty() || !inSurgeryPatients.isEmpty() || !inTransferPatients.isEmpty() || !waitForRecoveryPatients.isEmpty() || !inRecoveryPatients.isEmpty()) {
            while (!waitForSurgeryPatients.isEmpty()) {
                Patient patient = waitForSurgeryPatients.poll();
                if (patient == null) {
                    break;
                }
                Room room = getFirst(surgeryRooms);
                if (room != null) {
                    if (room.isFree()) {
                        patient.inSurgery(room);
                        inSurgeryPatients.add(patient);
                        room.inSurgery(patient.getSurgeryTime(), prepareOperatingRoomTime);
                    } else {
                        waitForSurgeryPatients.add(patient);
                        break;
                    }
                } else {
                    waitForSurgeryPatients.add(patient);
                    break;
                }
            }
            boolean isChange = false;
            List<Patient> needRemove = new ArrayList<>();
            for (Patient patient : inSurgeryPatients) {
                if (patient.hasFinishedSurgery()) {
                    needRemove.add(patient);
                    patient.inTransfer(surgeryTransferToRecoveryTime);
                    inTransferPatients.add(patient);
                    isChange = true;
                }
            }
            remove(inSurgeryPatients, needRemove);

            if (isChange) {
                Collections.sort(inTransferPatients);
            }
            isChange = false;

            for (Patient patient : inTransferPatients) {
                if (patient.hasFinishedTransfer()) {
                    needRemove.add(patient);
                    waitForRecoveryPatients.add(patient);
                    isChange = true;
                }
            }
            remove(inTransferPatients, needRemove);

            if (isChange) {
                Collections.sort(waitForRecoveryPatients);
            }

            for (Patient patient : waitForRecoveryPatients) {
                Room room = getFirst(recoveryRooms);
                if (room != null) {
                    if (room.isFree()) {
                        patient.inRecovery(room);
                        inRecoveryPatients.add(patient);
                        room.inRecovery(patient.getRecoveryTime(), prepareRecoveryRoomTime);
                        needRemove.add(patient);
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            remove(waitForRecoveryPatients, needRemove);
            needRemove.addAll(inRecoveryPatients.stream().filter(Patient::hasFinishedRecovery).collect(Collectors.toList()));
            remove(inRecoveryPatients, needRemove);
            time++;
        }
        time--;
        System.out.println("Patient Operating Room Recovery Room");
        System.out.println("#   Name   Room#   Begin   End   Bed#   Begin   End");
        for (Patient patient : AllPatients) {
            System.out.println(patient.toString());
        }
        System.out.println("***************************************************");
        System.out.println("Facility Utilization");
        System.out.println("Type   #    Minutes   % Used");
        AllRooms.forEach(System.out::println);
    }

    private Room getFirst(List<Room> rooms) {
        if (rooms.isEmpty()) {
            return null;
        }
        if (rooms.size() > 1) {
            Collections.sort(rooms);
        }
        return rooms.get(0);
    }

    private void remove(List<Patient> patients, List<Patient> needRemove) {
        if (!needRemove.isEmpty()) {
            patients.removeAll(needRemove);
            needRemove.clear();
        }
    }

    private int time = 0;

    private int nextSurgeryRoomId = 0;
    private int nextRecoveryRoomId = 0;

    public int getNextSurgeryRoomId() {
        this.nextSurgeryRoomId++;
        return this.nextSurgeryRoomId;
    }

    public int getNextRecoveryRoomId() {
        this.nextRecoveryRoomId++;
        return this.nextRecoveryRoomId;
    }

    private class Room implements Comparable<Room> {
        private int id;
        private int nextFreeTime = -1;//下一次空闲的时间
        private int inUseTime = 0;
        private boolean isSurgery;

        @Override
        public String toString() {
            String rate = this.inUseTime > 0 ? new BigDecimal(this.inUseTime).multiply(rate100).divide(new BigDecimal(time), 2, BigDecimal.ROUND_HALF_EVEN).toString() : "0.0";
            return (this.isSurgery ? "Room" : "Bed") + " " + this.id + "   " + this.inUseTime + "   " + rate;
        }

        public Room(boolean isSurgery) {
            this.isSurgery = isSurgery;
            if (isSurgery) {
                this.id = getNextSurgeryRoomId();
            } else {
                this.id = getNextRecoveryRoomId();
            }
        }

        public void inSurgery(int surgeryTime, int prepareOperatingRoomTime) {
            this.nextFreeTime = time + +prepareOperatingRoomTime + surgeryTime;
            this.inUseTime += surgeryTime;

        }

        public void inRecovery(int recoveryTime, int prepareRecoveryRoomTime) {
            this.nextFreeTime = time + recoveryTime + prepareRecoveryRoomTime;
            this.inUseTime += recoveryTime;
        }

        public boolean isFree() {
            if (this.nextFreeTime < 0) {
                return true;
            }
            if (isSurgery && time >= this.nextFreeTime || !isSurgery && time > this.nextFreeTime) {
                this.nextFreeTime = -1;
                return true;
            }
            return false;
        }

        public int getId() {
            return this.id;
        }

        @Override
        public int compareTo(Room other) {//空闲状态的排在前面，然后再按编号升序排列
            if (this.isFree() && other.isFree()) {
                return this.getId() - other.getId();
            } else if (this.isFree()) {
                return -1;
            } else if (other.isFree()) {
                return 1;
            }
            return 1;
        }
    }

    private BigDecimal rate100 = new BigDecimal(100);

    private int nextPatientId = 0;

    public int getNextPatientId() {
        this.nextPatientId++;
        return this.nextPatientId;
    }

    private String addMinute(int minute) {
        int hour = this.startSurgeryHour;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = new Date(calendar.getTimeInMillis() + minute * 60 * 1000);
        calendar.setTime(date);
        int newHour = calendar.get(Calendar.HOUR_OF_DAY);
        String neeHourStr = (newHour < 10 ? "0" : "") + newHour;
        int newMinute = calendar.get(Calendar.MINUTE);
        String neeMinuteStr = (newMinute < 10 ? "0" : "") + newMinute;
        return neeHourStr + ":" + neeMinuteStr;
    }

    private class Patient implements Comparable<Patient> {
        private int id;
        private String no;
        private String name;
        private int surgeryTime;
        private int recoveryTime;
        private int nextChangeTime = -1;

        private int surgeryRoomId = -1;
        private int surgeryBeginTime = -1;
        private int surgeryEndTime = -1;

        private int recoveryRoomId = -1;
        private int recoveryBeginTime = -1;
        private int recoveryEndTime = -1;

        @Override
        public String toString() {
            return this.id + "   " + this.name + "   " + this.surgeryRoomId + "   " +
                    addMinute(this.surgeryBeginTime) + "   " + addMinute(this.surgeryEndTime) + "   " + this.recoveryRoomId + "   " + addMinute(this.recoveryBeginTime) + "   " + addMinute(this.recoveryEndTime);
        }

        public Patient(String name, int surgeryTime, int recoveryTime) {
            this.id = getNextPatientId();
            this.no = String.valueOf(this.id);
            this.name = name;
            this.surgeryTime = surgeryTime;
            this.recoveryTime = recoveryTime;
        }

        public int getId() {
            return this.id;
        }

        public int getSurgeryTime() {
            return this.surgeryTime;
        }

        public int getRecoveryTime() {
            return this.recoveryTime;
        }

        public int inSurgery(Room room) {
            this.surgeryRoomId = room.getId();
            this.surgeryBeginTime = time;
            this.surgeryEndTime = time + this.surgeryTime;
            this.nextChangeTime = this.surgeryEndTime;
            return this.nextChangeTime;
        }

        public int inTransfer(int transferTime) {
            this.nextChangeTime = time + transferTime;
            return this.nextChangeTime;
        }

        public int inRecovery(Room room) {
            this.recoveryRoomId = room.getId();
            this.recoveryBeginTime = time;
            this.recoveryEndTime = time + this.recoveryTime;
            this.nextChangeTime = this.recoveryEndTime;
            return this.nextChangeTime;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Patient patient = (Patient) o;
            return this.no.equals(patient.no);
        }

        @Override
        public int hashCode() {
            return this.no.hashCode();
        }

        @Override
        public int compareTo(Patient other) {
            return this.getId() - other.getId();
        }

        public boolean hasFinishedSurgery() {
            return time >= this.nextChangeTime;
        }

        public boolean hasFinishedRecovery() {
            return time >= this.nextChangeTime;
        }

        public boolean hasFinishedTransfer() {
            return time >= this.nextChangeTime;
        }
    }
}
