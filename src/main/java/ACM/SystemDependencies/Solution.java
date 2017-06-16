package ACM.SystemDependencies;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/6/15
 * Time:17:48
 * <p>
 * 软件组件之间可能会有依赖关系，例如，TELNET和FTP都依赖于TCP/IP。你的任务是
 * 模拟安装和卸载软件组件的过程。首先是一些DEPEND指令，说明软件之间的依赖关系（保
 * 证不存在循环依赖），然后是一些INSTALL、REMOVE和LIST指令，如表6-1所示。
 * <p>
 * 在INSTALL指令中提到的组件称为显式安装，这些组件必须用REMOVE指令显式删除。
 * 同样地，被这些显式安装组件所直接或间接依赖的其他组件也不能在REMOVE指令中删除。
 * 每行指令包含不超过80个字符，所有组件名称都是大小写敏感的。指令名称均为大写字
 * 母。
 * <p>
 * System Dependencies, ACM/ICPC World Finals 1997, UVa506
 * <p>
 * Sample Input
 * DEPEND TELNET TCPIP NETCARD
 * DEPEND TCPIP NETCARD
 * DEPEND DNS TCPIP NETCARD
 * DEPEND BROWSER TCPIP HTML
 * INSTALL NETCARD
 * INSTALL TELNET
 * INSTALL foo
 * REMOVE NETCARD
 * INSTALL BROWSER
 * INSTALL DNS
 * LIST
 * REMOVE TELNET
 * REMOVE NETCARD
 * REMOVE DNS
 * REMOVE NETCARD
 * INSTALL NETCARD
 * REMOVE TCPIP
 * REMOVE BROWSER
 * REMOVE TCPIP
 * END
 * <p>
 * <p>
 * <p>
 * <p>
 * Sample Output
 * DEPEND TELNET TCPIP NETCARD
 * DEPEND TCPIP NETCARD
 * DEPEND DNS TCPIP NETCARD
 * DEPEND BROWSER TCPIP HTML
 * INSTALL NETCARD
 * Installing NETCARD
 * INSTALL TELNET
 * Installing TCPIP
 * Installing TELNET
 * INSTALL foo
 * Installing foo
 * REMOVE NETCARD
 * NETCARD is still needed.
 * INSTALL BROWSER
 * Installing HTML
 * Installing BROWSER
 * INSTALL DNS
 * Installing DNS
 * LIST
 * NETCARD
 * TCPIP
 * TELNET
 * foo
 * HTML
 * BROWSER
 * DNS
 * REMOVE TELNET
 * Removing TELNET
 * REMOVE NETCARD
 * NETCARD is still needed.
 * REMOVE DNS
 * Removing DNS
 * REMOVE NETCARD
 * NETCARD is still needed.
 * INSTALL NETCARD
 * NETCARD is already installed.
 * REMOVE TCPIP
 * TCPIP is still needed.
 * REMOVE BROWSER
 * Removing BROWSER
 * Removing HTML
 * Removing TCPIP
 * REMOVE TCPIP
 * TCPIP is not installed.
 * END
 */
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
    }

    private void case1() {
        dependencies(Command.DEPEND, new String[]{"TELNET", "TCPIP", "NETCARD"});
        dependencies(Command.DEPEND, new String[]{"TCPIP", "NETCARD"});
        dependencies(Command.DEPEND, new String[]{"DNS", "TCPIP", "NETCARD"});
        dependencies(Command.DEPEND, new String[]{"BROWSER", "TCPIP", "HTML"});
        dependencies(Command.INSTALL, new String[]{"NETCARD"});
        dependencies(Command.INSTALL, new String[]{"TELNET"});
        dependencies(Command.INSTALL, new String[]{"foo"});
        dependencies(Command.REMOVE, new String[]{"NETCARD"});
        dependencies(Command.INSTALL, new String[]{"BROWSER"});
        dependencies(Command.INSTALL, new String[]{"DNS"});
        dependencies(Command.LIST, null);
        dependencies(Command.REMOVE, new String[]{"TELNET"});
        dependencies(Command.REMOVE, new String[]{"NETCARD"});
        dependencies(Command.REMOVE, new String[]{"DNS"});
        dependencies(Command.REMOVE, new String[]{"NETCARD"});
        dependencies(Command.INSTALL, new String[]{"NETCARD"});
        dependencies(Command.REMOVE, new String[]{"TCPIP"});
        dependencies(Command.REMOVE, new String[]{"BROWSER"});
        dependencies(Command.REMOVE, new String[]{"TCPIP"});
    }

    private Map<String, Set<String>> depends = new LinkedHashMap<>();

    private Set<String> installs = new LinkedHashSet<>();
    private Set<String> installs_explicit = new LinkedHashSet<>();
    private Set<String> installs_implicit = new LinkedHashSet<>();

    private void dependencies(Command command, String[] args) {
        System.out.println(command.toString() + (args == null ? "" : " " + Arrays.toString(args)));
        switch (command) {
            case DEPEND:
                depend(args);
                break;
            case INSTALL:
                install(args);
                break;
            case REMOVE:
                remove(args);
                break;
            case LIST:
                list();
                break;
        }
        System.out.println("----------------------------------------------------------------");
    }

    private void list() {
        installs.forEach(System.out::println);
    }

    private void remove(String[] args) {
        for (String soft : args) {
            remove(soft, false);
        }
    }

    private void remove(String soft, boolean silent) {
        if (!installs.contains(soft)) {
            System.out.println(soft + " is not install.");
            return;
        }
        Set<String> set = new HashSet<>();
        for (Map.Entry<String, Set<String>> entry : depends.entrySet()) {
            set.addAll(entry.getValue());
            if (set.contains(soft) && installs_explicit.contains(entry.getKey())) {
                if (!silent) {
                    System.out.println(soft + " is still needed.");
                }
                return;
            }
        }
        System.out.println("Removing " + soft);
        remove(installs, soft);
        remove(installs_explicit, soft);
        remove(installs_implicit, soft);

        Set<String> depend = depends.get(soft);
        depends.remove(soft);
        if (depend != null) {
            depend.stream().filter(installs::contains).forEach(dep -> remove(dep, true));
        }
    }

    private void remove(Set<String> set, String key) {
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(key)) {
                iterator.remove();
                return;
            }
        }
    }

    private void install(String[] args) {
        for (String soft : args) {
            install(soft, true);
        }
    }

    private void install(String soft, boolean explicited) {
        if (installs.contains(soft)) {
            System.out.println(soft + "  is already installed.");
            return;
        }
        Set<String> depend = depends.get(soft);
        if (depend != null) {
            depend.stream().filter(dep -> !installs.contains(dep)).forEach(dep -> install(dep, false));
        }
        System.out.println("Installing " + soft);
        installs.add(soft);
        if (explicited) {
            installs_explicit.add(soft);
        } else {
            installs_implicit.add(soft);
        }
    }

    private void depend(String[] args) {
        int length = args.length;
        if (length < 2) {
            System.out.println("Wrong args.");
            return;
        }
        String key = args[0];
        Set<String> set = new LinkedHashSet<>();
        set.addAll(Arrays.asList(args).subList(1, length));
        if (depends.containsKey(key)) {
            System.out.println(key + " has defined depends,will be replaced.");
        }
        depends.put(key, set);
    }
}

enum Command {
    DEPEND, INSTALL, REMOVE, LIST;
}
