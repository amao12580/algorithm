package ACM.Borrowers;

import basic.Util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2017/5/5
 * Time:17:10
 * <p>
 * 你的任务是模拟一个图书管理系统。首先输入若干图书的标题和作者（标题各不相同，
 * 以END结束），然后是若干指令：BORROW指令表示借书，RETURN指令表示还
 * 书，SHELVE指令表示把所有已归还但还未上架的图书排序后依次插入书架并输出图书标题
 * 和插入位置（可能是第一本书或者某本书的后面）。
 * 图书排序的方法是先按作者从小到大排，再按标题从小到大排。在处理第一条指令之
 * 前，你应当先将所有图书按照这种方式排序。
 * <p>
 * Borrowers, ACM/ICPC World Finals 1994, UVa230
 */
public class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.case1();
    }

    private void case1() {
        setBooks(generatorOne());
        printBooks();
        randomCommand();
    }

    private LinkedList<Book> hasBorrow = new LinkedList<>();

    private void randomCommand() {
        int sum = Util.getRandomInteger(10, 100);
        Object param;
        Command command;
        for (int i = 0; i < sum; ) {
            command = Command.getRandomOne();
            if (command.equals(Command.BORROW)) {
                if (this.books.isEmpty()) {
                    continue;
                }
                param = this.books.get(Util.getRandomInteger(0, this.books.size() - 1));
            } else if (command.equals(Command.RETURN)) {
                if (this.hasBorrow.isEmpty()) {
                    continue;
                }
                param = this.hasBorrow.get(Util.getRandomInteger(0, this.hasBorrow.size() - 1));
            } else {
                param = null;
            }
            processCommand(command, param);
            System.out.println("------------------------------------------------------------------");
            i++;
        }
    }

    private void printBooks() {
        System.out.println("All books:" + this.books.toString());
    }

    private Book[] generatorOne() {
        int sum = Util.getRandomInteger(10, 100);
        Book[] books = new Book[sum];
        for (int i = 0; i < sum; i++) {
            books[i] = generatorOneBook();
        }
        return books;
    }

    private Book generatorOneBook() {
        return new Book(Util.generateLetterString(), Util.generateMixedString());
    }


    private LinkedList<Book> books = new LinkedList<>();
    private LinkedList<Book> booksWaitShelve = new LinkedList<>();


    private void processCommand(Command command, Object param) {
        System.out.println("command:" + command.toString() + (param == null ? "" : ",param:" + param.toString()));
        switch (command) {
            case BORROW:
                doBorrow((Book) param);
                break;
            case RETURN:
                doReturn((Book) param);
                break;
            case SHELVE:
                doShelve();
                break;
        }
    }

    private void doShelve() {
        if (this.booksWaitShelve.isEmpty()) {
            System.out.println("No book need shelve.");
            return;
        }
        this.setBooks(this.booksWaitShelve);
        for (Book book : this.booksWaitShelve) {
            int index = Collections.binarySearch(this.books, book);
            if (index >= 0) {
                System.out.println("book:" + book.toString() + " shelve success,new index:" + index);
            } else {
                System.out.println("book:" + book.toString() + " shelve failed.");
            }
        }
        this.booksWaitShelve.clear();
    }

    private void doReturn(Book book) {
        if (this.booksWaitShelve.contains(book)) {
            System.out.println("book:" + book.toString() + " has returned.");
            return;
        }
        this.booksWaitShelve.add(book);
        this.hasBorrow.remove(book);
        System.out.println("book:" + book.toString() + " return success.");
    }

    private void doBorrow(Book book) {
        int p = this.books.indexOf(book);
        if (p < 0) {
            System.out.println("book:" + book.toString() + " is not found.");
            return;
        }
        this.books.remove(p);
        this.hasBorrow.add(book);
        System.out.println("book:" + book.toString() + " borrow success.index:" + p);
    }

    private void setBooks(List<Book> list) {
        this.books.addAll(list);
        Collections.sort(this.books);
    }

    private void setBooks(Book[] books) {
        Collections.addAll(this.books, books);
        Collections.sort(this.books);
    }


    private class Book implements Comparable<Book> {
        private String title;
        private String author;

        @Override
        public String toString() {
            return "{\"title\":" + this.getTitle() + "," + "\"author\":" + this.getAuthor() + "}";
        }

        @Override
        public int compareTo(Book other) {
            int forTitle = this.getTitle().compareTo(other.getTitle());
            if (forTitle != 0) {
                return forTitle;
            }
            return this.getAuthor().compareTo(other.getAuthor());
        }

        @Override
        public int hashCode() {
            return this.getTitle().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                throw new IllegalArgumentException();
            }
            if (obj instanceof Book) {
                Book other = (Book) obj;
                return this.getTitle().equals(other.getTitle());
            }
            return false;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public Book(String title, String author) {
            this.title = title;
            this.author = author;
        }
    }

    private enum Command {
        BORROW,
        RETURN,
        SHELVE;

        public static Command getRandomOne() {
            Command[] commands = Command.values();
            return commands[Util.getRandomInteger(0, commands.length - 1)];
        }
    }
}
