package classic.hugeFileDereplication;

import basic.io.BufferedRandomAccessFile;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2018/1/18
 * Time:9:18
 * <p>
 * 100GB的文件，按行去重复, 每行大约为512byte
 * <p>
 * 行数为2亿,内存1GB
 * <p>
 * 定义N为文件行数(与大小成正比例)
 * 时间复杂度:O(N)
 * <p>
 * TODO
 * 1.子文件大小可控，倾斜度  OK
 * 2.vm内存监控，freeMemory
 * 3.断点恢复，checkPoint
 * 4.保持有序性    OK
 * 5.单文件:多线程拆分 OK
 */
public class Solution {
    private static final boolean DEBUG = false;
    private static final Charset DEFAULT_CHARACTER_SET = Charset.forName("UTF-8");//文件字符集
    private static final int DEFAULT_BUFFER_READ_SIZE = 32 * 1024 * 1024;//读缓冲大小
    private static final int DEFAULT_BUFFER_WRITE_SIZE = 32 * 1024 * 1024;//写缓冲大小
    private static final int DEFAULT_PARALLELISM_LEVEL = Runtime.getRuntime().availableProcessors();//并发级别
    //    private static final int DEFAULT_FILE_MAX_SIZE = 1024 * 1024;//子文件的大小上限 1MB
    private static final int DEFAULT_FILE_MAX_SIZE = 128 * 1024 * 1024;//子文件的大小上限 128MB


    public static void main(String[] args) throws IOException {
        String filePath = "D://log.txt";
        String outDir = "D://";
//        System.out.println("sep:" + System.getProperty("line.separator"));
//        System.out.println(Runtime.getRuntime().freeMemory());
//        System.out.println(Runtime.getRuntime().maxMemory());
//        System.out.println(Runtime.getRuntime().totalMemory());
//        System.out.println(Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory());
        long s = System.currentTimeMillis();
        Solution solution = new Solution();
        solution.reduce(filePath, outDir);
        System.out.println("time:" + (System.currentTimeMillis() - s) + "ms");
    }

    /**
     * 去除文件中的重复行
     *
     * @param filePath 文件完整路径
     * @param outDir   输出的结果文件的目录
     */
    private void reduce(String filePath, String outDir) throws IOException {
        if (filePath == null || filePath.isEmpty() || !isFileExist(filePath)) {
            println("filePath is not found..filePath=" + filePath);
            return;
        }
        if (outDir == null || outDir.isEmpty() || !isDirExist(outDir)) {
            println("outDir is not found..outDir=" + outDir);
            return;
        }
        outDir = formatDir(outDir);
        String tempPath = createTempDir(outDir, "temp");
        //拆分
        boolean splitResult = split(filePath, tempPath);
        println("split complete.result:" + splitResult);
        if (!splitResult) {
            println("split failed.return");
            return;
        }
        String uniquePath = createTempDir(outDir, "unique");
        //去重
        boolean uniqueResult = unique(tempPath, uniquePath);
        println("unique complete.result:" + uniqueResult);
        if (!uniqueResult) {
            println("unique failed.return");
            return;
        }
        //合并
        boolean mergeResult = merge(uniquePath, outDir);
        println("merge complete.result:" + mergeResult);
        if (!mergeResult) {
            println("merge failed.return");
            return;
        }
        //清理
        boolean clearResult = clearDir(tempPath, uniquePath);
        if (!clearResult) {
            println("clearDir failed.return");
            return;
        }
        println("reduce success.");
    }

    private boolean split(String filePath, String tempPath) {
        File file = new File(filePath);
        long fileSize;
        fileSize = file.length();
        println("origin file size:" + fileSize);
        List<Long> offsets = new ArrayList<>();
        long offset = 0;
        offsets.add(offset);
        try (BufferedRandomAccessFile reader = new BufferedRandomAccessFile(filePath, "r", DEFAULT_BUFFER_READ_SIZE)) {
            //按照子文件大小上限值，计算offset
            while ((offset = getNextOffset(reader, DEFAULT_FILE_MAX_SIZE, fileSize)) < fileSize) {
                offsets.add(offset);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        offsets.add(fileSize);
        println(offsets.toString());
        return booleanRecursiveTaskExecutor(new SplitFileTask(filePath, tempPath, offsets, 0, offsets.size() - 1));
    }

    private long getNextOffset(BufferedRandomAccessFile reader, int fileMaxSize, long maxOffset) throws IOException {
        long offset = reader.getFilePointer();
        if (offset >= maxOffset) {
            return maxOffset;
        }
        int lineMaxSize = 4 * 1024;//4KB 一行可能超过4KB吗？
        long newOffset = offset + fileMaxSize - lineMaxSize;
        if (newOffset >= maxOffset) {
            return maxOffset;
        }
        reader.seek(offset);
        return getNextOffset(reader, newOffset, lineMaxSize, maxOffset);
    }

    private long getNextOffset(BufferedRandomAccessFile reader, long offset, int lineMaxSize, long maxOffset) throws IOException {
        byte[] bytes = new byte[lineMaxSize];
        int len = reader.read(bytes);
        if (len <= 0) {
            return maxOffset;
        }
        for (int i = 0; i < len; i++) {
            if (bytes[i] == '\n') {
                offset += i;
                reader.seek(offset + 1);
                return offset;
            }
        }
        return getNextOffset(reader, reader.getFilePointer(), lineMaxSize, maxOffset);
    }

    private boolean unique(String tempPath, String uniquePath) {
        List<File> files = listFilesOrderByName(tempPath);
        if (files == null || files.isEmpty()) {
            println("files is empty,may be split failed.");
            return false;
        }
        boolean uniquePhase1 = unique(files);
        if (!uniquePhase1) {
            println("unique files phase 1 failed.");
            return false;
        }
        println("unique files phase 1 success.");
        boolean uniquePhase2 = unique(uniquePath, files, files.size());
        if (!uniquePhase2) {
            println("unique files phase 2 failed.");
            return false;
        }
        println("unique files phase 2 success.");
        return true;
    }

    private boolean unique(List<File> files) {//第一阶段：每个文件局部去重复
        return booleanRecursiveTaskExecutor(new UniqueFileTask(null, files, 0, files.size() - 1));
    }

    private boolean unique(String uniquePath, List<File> files, int max) {//第二阶段，向前去重
        int fileNumOffset = 0;
        List<Set<Integer>> lineHashCodes = null;
        ForkJoinPool pool = null;
        while (fileNumOffset < max) {
            lineHashCodes = buildSet();
            try {
                uniqueTransferFile(files.get(fileNumOffset), createTempFile(uniquePath, fileNumOffset), lineHashCodes, false);
                if (!booleanRecursiveTaskExecutor(new UniqueFileTask(lineHashCodes, files, fileNumOffset, files.size() - 1))) {
                    println("UniqueFileTask is error.");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (pool != null) {
                    pool.shutdown();
                }
                clearSet(lineHashCodes);
            }
            fileNumOffset++;
        }
        clearSet(lineHashCodes);
        return true;
    }

    private boolean merge(String uniquePath, String outDir) {
        String resultFilePath = outDir + "resultFile";
        File resultFile = new File(resultFilePath);
        if (resultFile.exists()) {
            println("resultFile is exist.resultFilePath=" + resultFilePath);
            return false;
        }
        List<File> files = listFilesOrderByName(uniquePath);
        if (files == null || files.isEmpty()) {
            println("listFilesOrderByName is empty.");
            return false;
        }
        List<File> newFileList;
        boolean mergeSuccess;
        while (files.size() > 1) {
            newFileList = new ArrayList<>();
            mergeSuccess = booleanRecursiveTaskExecutor(new MergeFileTask(files, newFileList, 0, files.size() - 1)) && !newFileList.isEmpty();
            if (!mergeSuccess) {
                println("MergeFileTask is error.");
                return false;
            }
            files.clear();
            files = newFileList;
            orderByFileName(files);
        }
        try {
            FileUtils.moveFile(files.get(0), resultFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        println("result file path:" + resultFilePath);
        return true;
    }

    private BufferedReader buildBufferedReader(File file) throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), DEFAULT_CHARACTER_SET), DEFAULT_BUFFER_READ_SIZE);
    }

    private BufferedWriter buildBufferedWriter(File file) throws FileNotFoundException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), DEFAULT_CHARACTER_SET), DEFAULT_BUFFER_WRITE_SIZE);
    }

    private List<Set<Integer>> buildSet() {
        List<Set<Integer>> sets = new ArrayList<>(2);//正+负
        sets.add(new HashSet<>());
        sets.add(new HashSet<>());
        return sets;
    }

    private void clearSet(List<Set<Integer>> list) {
        if (list == null) {
            return;
        }
        for (Set<Integer> item : list) {
            if (item != null) {
                item.clear();
            }
        }
        list.clear();
    }

    private boolean clearDir(String... dir) {
        if (dir == null) {
            return true;
        }
        File file;
        List<File> fileList;
        for (String item : dir) {
            file = new File(item);
            FileUtils.deleteQuietly(file);
            file = new File(item);
            //appendFile().MappedByteBuffer 可能不释放文件句柄 需要vm thread释放
            if (file.exists()) {
                System.gc();
                println("clear temporary dir failed,dir path:" + item);
                fileList = listFilesOrderByName(item);
                if (fileList != null) {
                    for (File f : fileList) {
                        println("clear temporary file failed,file path:" + f.getPath());
                        FileUtils.deleteQuietly(f);
                        f.deleteOnExit();
                    }
                }
                FileUtils.deleteQuietly(file);
                file.deleteOnExit();
            }
        }
        return true;
    }

    private File createTempFile(String tempFilePath) throws IOException {
        File file = new File(tempFilePath);
        if (file.exists()) {
            throw new FileAlreadyExistsException("file has existed.file path:" + file.getPath());
        }
        FileUtils.touch(file);
        return file;
    }

    private File createTempFile(String tempPath, long tempFileNum) throws IOException {
        return createTempFile(tempPath + tempFileNum);
    }

    private File createTempFile(String tempPath, String fileName) throws IOException {
        return createTempFile(tempPath + fileName);
    }

    private String createTempDir(String dirPath, String newDirName) throws IOException {
        StringBuilder tempPathBuilder = new StringBuilder();
        if (isDirExist(dirPath)) {
            tempPathBuilder.append(dirPath);
        } else {
            String sysTempDir = FileUtils.getTempDirectoryPath();
            println("dir doesn't exist.dir:" + dirPath + ",use system temp dir:" + sysTempDir);
            tempPathBuilder.append(sysTempDir);
        }
        tempPathBuilder.append(newDirName).append(File.separator);
        String tempPath = tempPathBuilder.toString();
        if (isDirExist(tempPath)) {
            println(tempPath);
            return tempPath;
        }
        FileUtils.forceMkdir(new File(tempPath));
        return tempPath;
    }

    private boolean appendFile(File source, File target) {
        try (BufferedRandomAccessFile writer = new BufferedRandomAccessFile(target, "rw", DEFAULT_BUFFER_WRITE_SIZE)) {
            writer.seek(target.length());
            MappedByteBuffer buffer = Files.map(source, FileChannel.MapMode.READ_ONLY);
            byte[] bytes = new byte[DEFAULT_BUFFER_WRITE_SIZE];
            int endIndex = 0;
            int maxEndIndex = bytes.length - 1;
            while (buffer.hasRemaining()) {
                bytes[endIndex] = buffer.get();
                endIndex++;
                if (endIndex == maxEndIndex) {
                    writer.write(bytes, 0, endIndex);
                    endIndex = 0;
                }
            }
            if (endIndex > 0) {
                writer.write(bytes, 0, endIndex);
            }
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
            println("appendFile error.source=" + source.getPath() + ",target=" + target.getPath());
            return false;
        }
        return true;
    }

    private List<File> listFilesOrderByName(String filePath) {
        List<File> fileList = new ArrayList<>(FileUtils.listFiles(new File(filePath), null, false));
        orderByFileName(fileList);
        return fileList;
    }

    private void orderByFileName(List<File> fileList) {
        if (fileList.size() <= 1) {
            return;
        }
        fileList.sort(Comparator.comparingLong(fileThis -> Long.valueOf(fileThis.getName())));
    }

    private boolean isDirExist(String dirPath) {
        File file = new File(dirPath);
        return file.exists() && file.isDirectory();
    }

    private boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    private String formatDir(String dirPath) {
        if (!dirPath.endsWith(File.separator)) {
            dirPath = dirPath + File.separator;
        }
        return dirPath;
    }

    private void uniqueTransferFile(File fileThis, File fileOther, List<Set<Integer>> lineHashCodes, boolean readOnly) throws IOException {
        try (BufferedReader reader = buildBufferedReader(fileThis);
             BufferedWriter writer = buildBufferedWriter(fileOther)) {
            String line;
            int setIndex, lineHashCode;
            while ((line = reader.readLine()) != null) {
                lineHashCode = line.hashCode();
                setIndex = lineHashCode >= 0 ? 0 : 1;
                if (readOnly ? !lineHashCodes.get(setIndex).contains(lineHashCode) :
                        lineHashCodes.get(setIndex).add(lineHashCode)) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
    }

    private void println(String s) {
        if (DEBUG) {
            System.out.println(Calendar.getInstance().getTime().toString() + " - " + s);
        }
    }

    private boolean booleanRecursiveTaskExecutor(RecursiveTask<Boolean> task) {
        ForkJoinPool pool = null;
        Boolean executeResult;
        try {
            pool = new ForkJoinPool(DEFAULT_PARALLELISM_LEVEL);
            executeResult = pool.invoke(task);
            executeResult = executeResult != null && executeResult && task.isCompletedNormally();
        } finally {
            if (pool != null) {
                pool.shutdown();
            }
        }
        if (!executeResult) {
            println("task execute is error.");
            return false;
        }
        return true;
    }

    class SplitFileTask extends RecursiveTask<Boolean> {
        private String filePath;
        private String tempPath;
        private List<Long> offsets;
        private int min, max;

        SplitFileTask(String filePath, String tempPath, List<Long> offsets, int min, int max) {
            this.filePath = filePath;
            this.tempPath = tempPath;
            this.offsets = offsets;
            this.min = min;
            this.max = max;
        }

        @Override
        protected Boolean compute() {
            if (max - min == 1) {
                int fileNum = offsets.indexOf(offsets.get(min));
                boolean result = split(offsets.get(min), offsets.get(max), fileNum);
                println("filePath=" + filePath + ",start=" + offsets.get(min) + ",end=" + offsets.get(max) + ",fileNum=" + fileNum + " split completed.result:" + result);
                return result;
            } else {
                int middle = (max + min) / 2;
                SplitFileTask left = new SplitFileTask(filePath, tempPath, offsets, min, middle);
                SplitFileTask right = new SplitFileTask(filePath, tempPath, offsets, middle, max);
                left.fork();
                right.fork();
                left.join();
                right.join();
            }
            return true;
        }

        private boolean split(long start, long end, int fileNum) {
            try (BufferedRandomAccessFile reader = new BufferedRandomAccessFile(filePath, "r", DEFAULT_BUFFER_READ_SIZE);
                 BufferedRandomAccessFile writer = new BufferedRandomAccessFile(createTempFile(tempPath, fileNum), "rw", DEFAULT_BUFFER_WRITE_SIZE)) {
                reader.seek(start);
                byte[] bytes = new byte[DEFAULT_BUFFER_READ_SIZE];
                int readLength;
                while (end - reader.getFilePointer() >= 0 &&
                        (readLength = reader.read(bytes, 0, (int) (end - reader.getFilePointer() > DEFAULT_BUFFER_READ_SIZE ?
                                DEFAULT_BUFFER_READ_SIZE : end - reader.getFilePointer()))) > 0) {
                    writer.write(bytes, 0, readLength);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

    class UniqueFileTask extends RecursiveTask<Boolean> {
        private List<Set<Integer>> lineHashCodes;
        private List<File> files;
        private int min, max;

        UniqueFileTask(List<Set<Integer>> lineHashCodes, List<File> files, int min, int max) {
            this.lineHashCodes = lineHashCodes;
            this.files = files;
            this.min = min;
            this.max = max;
        }

        @Override
        protected Boolean compute() {
            if (max == min) {
                boolean result = unique(lineHashCodes, files.get(max));
                println(files.get(max).getPath() + " unique completed.result:" + result);
                return result;
            } else {
                int middle = (max + min) / 2;
                UniqueFileTask left = new UniqueFileTask(lineHashCodes, files, min, middle);
                UniqueFileTask right = new UniqueFileTask(lineHashCodes, files, middle + 1, max);
                left.fork();
                right.fork();
                left.join();
                right.join();
            }
            return true;
        }

        private boolean unique(List<Set<Integer>> lineHashCodes, File file) {
            File newFile;
            try {
                newFile = createTempFile(formatDir(file.getParent()), file.getName() + ".unique");
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            boolean autoClear = lineHashCodes == null;
            lineHashCodes = autoClear ? buildSet() : lineHashCodes;
            try {
                uniqueTransferFile(file, newFile, lineHashCodes, true);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (autoClear) {
                    clearSet(lineHashCodes);
                }
            }
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return newFile.renameTo(file);
        }
    }

    class MergeFileTask extends RecursiveTask<Boolean> {
        private List<File> files, newFiles;
        private int start, end;

        MergeFileTask(List<File> files, List<File> newFiles, int start, int end) {
            this(files, start, end);
            this.newFiles = newFiles;
        }

        MergeFileTask(List<File> files, int start, int end) {
            this.files = files;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Boolean compute() {
            if (end == start) {
                println("merge all complete,finally file=" + files.get(start).getPath());
                newFiles.add(files.get(start));
                return true;
            }
            if (end - start == 1) {
                File newFile = merge(files.get(start), files.get(end));
                if (newFile == null) {
                    println("merge failed,fileLeft=" + files.get(start).getPath() + ",fileRight=" + files.get(end).getPath());
                    return false;
                }
                println("merge success,fileLeft=" + files.get(start).getPath() + ",fileRight=" + files.get(end).getPath() + ",result=" + newFile.getPath());
                newFiles.add(newFile);
            } else {
                int middle = (start + end) / 2;
                MergeFileTask left = new MergeFileTask(files, newFiles, start, middle);
                MergeFileTask right = new MergeFileTask(files, newFiles, middle + 1, end);
                left.fork();
                right.fork();
                left.join();
                right.join();
            }
            return true;
        }

        private File merge(File left, File right) {
            boolean mergeResult = appendFile(right, left);
            if (!mergeResult) {
                println("merge failed,source=" + left.getPath() + ",target=" + left.getPath());
                return null;
            }
            return left;
        }
    }
}