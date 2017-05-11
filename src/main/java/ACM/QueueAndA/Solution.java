package ACM.QueueAndA;

import basic.Util;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/11
 * Time:13:50
 * <p>
 * 你的任务是模拟一个客户中心运作情况。客服请求一共有n（1≤n≤20）种主题，每种主
 * 题用5个整数描述：tid, num, t0, t, dt，其中tid为主题的唯一标识符，num为该主题的请求个
 * 数，t0为第一个请求的时刻，t为处理一个请求的时间，dt为相邻两个请求之间的间隔（为了
 * 简单情况，假定同一个主题的请求按照相同的间隔到达）。
 * 客户中心有m（1≤m≤5）个客服，每个客服用至少3个整数描述：pid, k, tid 1 , tid 2 , …,
 * tid k ，表示一个标识符为pid的人可以处理k种主题的请求，按照优先级从大到小依次为tid 1 ,
 * tid 2 , …, tid k 。当一个人有空时，他会按照优先级顺序找到第一个可以处理的请求。如果有多
 * 个人同时选中了某个请求，上次开始处理请求的时间早的人优先；如果有并列，id小的优
 * 先。输出最后一个请求处理完毕的时刻。
 * <p>
 * <p>
 * Queue and A, ACM/ICPC World Finals 2000, UVa822
 * <p>
 * 原题：https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=763
 */
public class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
        System.out.println("-----------------------------------------------------");
        solution.case2();
    }

    private void case2() {
        int tn = Util.getRandomInteger(1, 20);
        int[] tids = new int[tn];
        Topic[] topics = new Topic[tn];
        for (int i = 0; i < tn; i++) {
            topics[i] = generatorOneTopic();
            tids[i] = topics[i].getTid();
        }
        int pn = Util.getRandomInteger(1, 5);
        Person[] persons = new Person[pn];
        for (int i = 0; i < pn; i++) {
            persons[i] = generatorOnePerson(tids);
        }
        AllRequestExecutedTime(topics, persons);
    }

    private Person generatorOnePerson(int[] tids) {
        int tidn = Util.getRandomInteger(1, tids.length);
        Set<Integer> hasAdd = new HashSet<>();
        Integer[] myTids = new Integer[tidn];
        for (int i = 0; i < tidn; i++) {
            myTids[i] = getNoRepeat(hasAdd, tids);
        }
        return new Person(Util.getRandomInteger(1, 100), myTids);
    }

    private int getNoRepeat(Set<Integer> hasAdd, int[] tids) {
        int tid = tids[Util.getRandomInteger(0, tids.length - 1)];
        if (!hasAdd.add(tid)) {
            return getNoRepeat(hasAdd, tids);
        }
        return tid;
    }

    private Topic generatorOneTopic() {
        return new Topic(Util.getRandomInteger(1, 1000), Util.getRandomInteger(1, 1000), Util.getRandomInteger(0, 100), Util.getRandomInteger(1, 20), Util.getRandomInteger(1, 20));
    }

    private void case1() {
        Topic[] topics = new Topic[3];
        topics[0] = new Topic(128, 20, 0, 5, 10);
        topics[1] = new Topic(134, 25, 5, 6, 7);
        topics[2] = new Topic(153, 30, 10, 4, 5);
        Person[] persons = new Person[4];
        persons[0] = new Person(10, new Integer[]{2, 128, 134});
        persons[1] = new Person(11, new Integer[]{1, 134});
        persons[2] = new Person(12, new Integer[]{2, 128, 153});
        persons[3] = new Person(13, new Integer[]{1, 153});
        AllRequestExecutedTime(topics, persons);
    }

    private int time = 0;

    private void AllRequestExecutedTime(Topic[] topicArray, Person[] personArray) {
        System.out.println("topic:" + Arrays.toString(topicArray));
        System.out.println("person:" + Arrays.toString(personArray));
        Set<Topic> topics = new HashSet<>();
        for (Topic topic : topicArray) {
            if (time > topic.getT0()) {
                time = topic.getT0();
            }
            topics.add(topic);
        }
        Set<Person> persons = new HashSet<>();
        Collections.addAll(persons, personArray);
        Map<Integer, Topic> availableTopics;
        Map<Topic, List<Person>> tasks;
        Topic last = null;
        while (!topics.isEmpty()) {
            availableTopics = new HashMap<>();
            for (Topic topic : topics) {
                if (time >= topic.getT0()) {
                    availableTopics.put(topic.getTid(), topic);
                }
            }
            if (!availableTopics.isEmpty()) {
                tasks = new HashMap<>();
                List<Person> availablePersons = findAvailable(persons);
                for (Person person : availablePersons) {//匹配客服与任务
                    Topic topic;
                    List<Person> personList;
                    for (Integer tid : person.getTopics()) {
                        topic = availableTopics.get(tid);
                        if (topic != null) {
                            personList = tasks.get(topic);
                            if (personList == null) {
                                personList = new ArrayList<>();
                                personList.add(person);
                                tasks.put(topic, personList);
                            } else {
                                personList.add(person);
                            }
                            break;
                        }
                    }
                }
                Topic topic;
                List<Person> personList;
                for (Map.Entry<Topic, List<Person>> entry : tasks.entrySet()) {//开始做服务
                    topic = entry.getKey();
                    personList = entry.getValue();
                    if (personList.size() > 1) {
                        Collections.sort(personList);
                    }
                    personList.get(0).doService(topic.getT());
//                    System.out.println("time:" + time + ",tid:" + topic.getTid() + ",pick up :" + personList.get(0).getNo());
                    topic.setT0(time + topic.getDt());
                    if (topic.cutNum()) {
                        removeTopic(topic, topics);
                    }
                    last = topic;
                }
            }
            time++;
        }
        time--;
        time += last == null ? 0 : last.getT();
        System.out.println("Scenario 1: All requests are serviced within " + time + " minutes.");
    }

    private void removeTopic(Topic topic, Set<Topic> topics) {
        Iterator<Topic> iterator = topics.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(topic)) {
                iterator.remove();
                return;
            }
        }
    }

    private List<Person> findAvailable(Set<Person> suitPerson) {
        return suitPerson.stream().filter(person -> !person.inService()).collect(Collectors.toList());
    }


    private class Topic {
        private transient String no;
        private int tid;
        private int num;
        private int t0;
        private int t;
        private int dt;

        public Topic(int tid, int num, int t0, int t, int dt) {
            if (t0 < 0 || num < 0 || t <= 0 || dt < 0) {
                throw new IllegalArgumentException();
            }
            this.tid = tid;
            this.num = num;
            this.t0 = t0;
            this.t = t;
            this.dt = dt;
            this.no = "" + tid;
        }

        public String getNo() {
            return no;
        }

        public int getT0() {
            return t0;
        }

        public int getTid() {
            return tid;
        }

        public int getT() {
            return t;
        }

        public int getDt() {
            return dt;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Topic other = (Topic) o;
            return this.getNo().equals(other.getNo());

        }

        @Override
        public int hashCode() {
            return this.getNo().hashCode();
        }

        public void setT0(int t0) {
            this.t0 = t0;
        }

        public boolean cutNum() {
            this.num--;
            return this.num <= 0;
        }

        @Override
        public String toString() {
            return Util.toJson(this);
        }
    }

    private class Person implements Comparable<Person> {
        private transient String no;
        private int pid;
        private transient boolean isFree = true;
        private transient Integer lastServiceTime = null;
        private transient Integer nextFreeTime = null;
        private LinkedList<Integer> topics;

        public boolean inService() {
            if (this.isFree) {
                return false;
            }
            if (time > nextFreeTime) {
                this.isFree = true;
                this.nextFreeTime = null;
                return false;
            }
            return true;
        }

        public Person(int pid, Integer[] topicArray) {
            if (topicArray == null || topicArray.length == 0) {
                throw new IllegalArgumentException();
            }
            this.pid = pid;
            this.no = "" + pid;
            LinkedList<Integer> linkedList = new LinkedList<>();
            Collections.addAll(linkedList, topicArray);
            this.topics = linkedList;
        }

        public String getNo() {
            return no;
        }

        public LinkedList<Integer> getTopics() {
            return topics;
        }

        public void doService(int t) {
            this.lastServiceTime = time;
            this.isFree = false;
            this.nextFreeTime = time + t;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Topic other = (Topic) o;
            return this.getNo().equals(other.getNo());

        }

        @Override
        public int hashCode() {
            return this.getNo().hashCode();
        }

        @Override
        public int compareTo(Person other) {
            Integer tl = this.lastServiceTime;
            Integer ol = other.lastServiceTime;
            if (!(tl == null && ol == null)) {
                if (tl == null) {
                    tl = 0;
                }
                if (ol == null) {
                    ol = 0;
                }
                return tl - ol;
            }
            if (this.pid != other.pid) {
                return this.pid > other.pid ? 1 : -1;
            }
            return 0;
        }

        @Override
        public String toString() {
            return Util.toJson(this);
        }
    }
}
