package classic.hugeFileDereplication;

import basic.Util;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.List;
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
 * 5.单文件:多线程拆分  ？
 */
public class Solution {
    private static final Charset DEFAULT_CHARACTER_SET = Charset.forName("UTF-8");//文件字符集
    private static final int DEFAULT_BUFFER_READ_SIZE = 10 * 1024 * 1024;//读缓冲大小
    private static final int DEFAULT_BUFFER_WRITE_SIZE = 10 * 1024 * 1024;//写缓冲大小
    //    private static final int DEFAULT_FILE_MAX_LINE = 10 * 1024;//子文件的行数上限
    private static final long DEFAULT_FILE_MAX_LINE = 1024 * 512;//子文件的行数上限、大小约256MB、文件个数约400

    public static void main(String[] args) {
        String filePath = "D://log.txt";
        String outDir = "D://";
//        System.out.println(Runtime.getRuntime().freeMemory());
//        System.out.println(Runtime.getRuntime().maxMemory());
//        System.out.println(Runtime.getRuntime().totalMemory());
//        System.out.println(Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory());
        Solution solution = new Solution();
        solution.reduce(filePath, outDir);
    }

    /**
     * 去除文件中的重复行
     *
     * @param filePath 文件完整路径
     * @param outDir   输出的结果文件的目录
     */
    private void reduce(String filePath, String outDir) {
        String tempPath = createTempDir(outDir, "temp");
        if (tempPath == null) {
            return;
        }
        //拆分
        boolean splitResult = split(filePath, tempPath);
        println("split complete.result:" + splitResult);
        if (!splitResult) {
            println("split failed.return");
            return;
        }
        String uniquePath = createTempDir(outDir, "unique");
        if (uniquePath == null) {
            return;
        }
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
        boolean clearResult = clearDir(tempPath, uniquePath) && closeBitSet();
        if (!clearResult) {
            println("clearDir failed.return");
            return;
        }
        println("reduce success.");
    }

    private BitSet[] bitSet = null;//复用
    private boolean clearBitSet = false;

    private boolean split(String filePath, String tempPath) {
        File file = new File(filePath);
        long fileSize;
        if (file.exists() && file.isFile()) {
            fileSize = file.length();
        } else {
            println("file doesn't exist or is not a file.");
            return false;
        }
        println("origin file size:" + fileSize);
        BufferedWriter bufferedWriter = null;
        try (BufferedReader bufferedReader = buildBufferedReader(file)) {
            String line;
            int bitIndex, lineHashCode;
            int lineNum = 0;
            long fileNum = 0;
            bitSet = buildBitSet();
            int bitSetIndex;
            bufferedWriter = buildBufferedWriter(createTempFile(tempPath, fileNum));
            while ((line = bufferedReader.readLine()) != null) {
                if (lineNum >= DEFAULT_FILE_MAX_LINE) {//新文件
                    closeWriter(bufferedWriter);
                    lineNum = 0;
                    fileNum++;
                    bufferedWriter = buildBufferedWriter(createTempFile(tempPath, fileNum));
                }
                lineHashCode = line.hashCode();
                bitIndex = cover2BitIndex(lineHashCode);
                bitSetIndex = lineHashCode >= 0 ? 0 : 1;
                if (!bitSet[bitSetIndex].get(bitIndex)) {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                    if (fileNum == 0) {
                        bitSet[bitSetIndex].set(bitIndex);
                    }
                }
                lineNum++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            clearBitSet(bitSet);
            closeWriter(bufferedWriter);
        }
        return true;
    }


    private boolean unique(String tempPath, String uniquePath) {
        List<File> files = listFilesOrderByName(tempPath);
        if (files == null || files.isEmpty()) {
            return false;
        }
        try {
            FileUtils.moveFile(files.get(0), new File(uniquePath + 0));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        int size = files.size();
        return size == 1 || unique(uniquePath, files, size);
    }

    private boolean unique(String uniquePath, List<File> files, int max) {
        int fileNumOffset = 1;
        while (fileNumOffset < max) {
            if (clearBitSet) {
                bitSet = bitSet == null ? buildBitSet() : bitSet;
                BufferedWriter bufferedWriter = null;
                try (BufferedReader bufferedReader = buildBufferedReader(files.get(fileNumOffset))) {
                    String line;
                    int bitIndex, bitSetIndex, lineHashCode;
                    bufferedWriter = buildBufferedWriter(createTempFile(uniquePath, fileNumOffset));
                    while ((line = bufferedReader.readLine()) != null) {
                        lineHashCode = line.hashCode();
                        bitIndex = cover2BitIndex(lineHashCode);
                        bitSetIndex = lineHashCode >= 0 ? 0 : 1;
                        if (!bitSet[bitSetIndex].get(bitIndex)) {
                            bufferedWriter.write(line);
                            bufferedWriter.newLine();
                            bitSet[bitSetIndex].set(bitIndex);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    closeWriter(bufferedWriter);
                }
                clearBitSet = false;
            }
            ForkJoinPool pool = null;
            try {
                UniqueFileTask task = new UniqueFileTask(bitSet, files, fileNumOffset, files.size() - 1);
                pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 2);
                Boolean isSuccess = pool.invoke(task);
                if (!isSuccess || task.isCompletedAbnormally()) {
                    println("UniqueFileTask is error.");
                }
            } finally {
                if (pool != null) {
                    pool.shutdown();
                }
                clearBitSet(bitSet);
            }
            fileNumOffset++;
        }
        return true;
    }

    private boolean merge(String uniquePath, String outDir) {
        String resultFilePath = outDir + (outDir.endsWith(File.separator) ? "" : File.separator) + "resultFile";
        File resultFile = null;
        try {
            resultFile = createTempFile(resultFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (resultFile == null) {
            return false;
        }
        List<File> files = listFilesOrderByName(uniquePath);
        if (files == null || files.isEmpty()) {
            println("dir.listFiles() is empty.");
            return false;
        }
        ForkJoinPool pool = null;
        MergeFileTask task;
        List<File> newFileList;
        Boolean mergeSuccess;
        int num = Runtime.getRuntime().availableProcessors() * 2;
        while (files.size() > 1) {
            try {
                newFileList = new ArrayList<>();
                task = new MergeFileTask(files, newFileList);
                pool = new ForkJoinPool(num);
                mergeSuccess = pool.invoke(task);
                if (mergeSuccess == null || !mergeSuccess || newFileList.isEmpty() || task.isCompletedAbnormally()) {
                    println("MergeFileTask is error.");
                    return false;
                }
                files.clear();
                files = newFileList;
                orderByFileName(files);
            } finally {
                if (pool != null) {
                    pool.shutdown();
                }
            }
        }
        try {
            FileUtils.copyFile(files.get(0), resultFile);
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
        return buildBufferedWriter(file, false);
    }


    private BufferedWriter buildBufferedWriter(File file, boolean append) throws FileNotFoundException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), DEFAULT_CHARACTER_SET), DEFAULT_BUFFER_WRITE_SIZE);
    }

    private BitSet[] buildBitSet() {
        BitSet[] bitSets = new BitSet[2];//正+负
        bitSets[0] = new BitSet(Integer.MAX_VALUE / 8); //64MB 内存;
        bitSets[1] = new BitSet(Integer.MAX_VALUE / 8); //64MB 内存;
        println("buildBitSet");
        return bitSets;
    }

    private void closeWriter(Writer... writer) {
        if (writer == null) {
            return;
        }
        for (Writer item : writer) {
            if (item != null) {
                try {
                    item.flush();
                    item.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void clearBitSet(BitSet... bitSet) {
        if (bitSet == null) {
            return;
        }
        for (BitSet item : bitSet) {
            if (item != null) {
                item.clear();
            }
        }
        clearBitSet = true;
        println("clearBitSet" + bitSet.length);
    }

    private boolean clearDir(String... dir) {
        if (dir == null) {
            return true;
        }
        for (String item : dir) {
            try {
                FileUtils.forceDeleteOnExit(new File(item));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private boolean closeBitSet() {
        this.bitSet = null;
        return true;
    }

    private File createTempFile(String tempFilePath) throws IOException {
        File file = new File(tempFilePath);
        if (file.exists()) {
            println("file has existed.file path:" + file.getPath());
            return null;
        }
        FileUtils.forceMkdirParent(file);
        FileUtils.touch(file);
        return file;
    }

    private File createTempFile(String tempPath, long tempFileNum) throws IOException {
        String tempFilePath = tempPath + tempFileNum;
        return createTempFile(tempFilePath);
    }

    private File createTempFile(String tempPath, String fileName) throws IOException {
        String tempFilePath = tempPath + fileName;
        return createTempFile(tempFilePath);
    }

    private String createTempDir(String dirPath, String newDirName) {
        StringBuilder tempPathBuilder = new StringBuilder();
        if (!dirPath.endsWith(File.separator)) {
            dirPath = dirPath + File.separator;
        }
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
        File tempDir = new File(tempPath);
        if (tempDir.mkdirs()) {
            return tempPath;
        } else {
            println("createTempDir mkdirs failed.");
            return null;
        }
    }

    private boolean appendFile(File source, File target) {
        try (BufferedReader bufferedReader = buildBufferedReader(source);
             BufferedWriter bufferedWriter = buildBufferedWriter(target, true)) {
            String line;
            bufferedWriter.newLine();
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
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
        fileList.sort((fileThis, fileOther) -> {
            if (fileThis.isDirectory() && fileOther.isFile())
                return -1;
            if (fileThis.isFile() && fileOther.isDirectory())
                return 1;
            return fileThis.getName().compareTo(fileOther.getName());
        });
    }

    private boolean isDirExist(String dirPath) {
        File file = new File(dirPath);
        return file.exists() && file.isDirectory();
    }

    private int cover2BitIndex(int hashCode) {
        return Util.abs(hashCode);
    }

    private void println(String s) {
        System.out.println(Calendar.getInstance().getTime().toString() + " - " + s);
    }

    class MergeFileTask extends RecursiveTask<Boolean> {
        private List<File> files, newFiles;
        private int start, end;

        MergeFileTask(List<File> files, List<File> newFiles) {
            this(files, 0, files.size() - 1);
            this.newFiles = newFiles;
        }

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
                MergeFileTask t1 = new MergeFileTask(files, newFiles, start, middle);
                MergeFileTask t2 = new MergeFileTask(files, newFiles, middle + 1, end);
                invokeAll(t1, t2);
                if (!(t1.isCompletedNormally() && t2.isCompletedNormally())) {
                    println("merge failed,piece task error.");
                    return false;
                }
            }
            return true;
        }

        private File merge(File left, File right) {
            boolean mergeResult = appendFile(right, left);
            if (!mergeResult) {
                println("merge failed,source=" + left.getPath() + ",target=" + left.getPath());
                return null;
            }
            try {
                FileUtils.forceDelete(right);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return left;
        }
    }

    class UniqueFileTask extends RecursiveTask<Boolean> {
        private BitSet[] bitSet;
        private List<File> files;
        private int min;
        private int max;

        UniqueFileTask(BitSet[] bitSet, List<File> files, int min, int max) {
            this.bitSet = bitSet;
            this.files = files;
            this.min = min;
            this.max = max;
        }

        @Override
        protected Boolean compute() {
            if (max == min) {
                boolean result = unique(bitSet, files.get(max));
                println(files.get(max).getPath() + " unique completed.result:" + result);
                return result;
            } else {
                int middle = (max + min) / 2;
                UniqueFileTask left = new UniqueFileTask(bitSet, files, min, middle);
                UniqueFileTask right = new UniqueFileTask(bitSet, files, middle + 1, max);
                invokeAll(left, right);
            }
            return true;
        }

        private boolean unique(BitSet[] bitSet, File file) {
            File newFile;
            try {
                newFile = createTempFile(file.getAbsolutePath(), file.getName() + ".unique");
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            try (BufferedReader bufferedReader = buildBufferedReader(file);
                 BufferedWriter bufferedWriter = buildBufferedWriter(newFile)) {
                String line;
                int bitIndex, bitSetIndex, lineHashCode;
                while ((line = bufferedReader.readLine()) != null) {
                    lineHashCode = line.hashCode();
                    bitIndex = cover2BitIndex(lineHashCode);
                    bitSetIndex = lineHashCode >= 0 ? 0 : 1;
                    if (!bitSet[bitSetIndex].get(bitIndex)) {
                        bufferedWriter.write(line);
                        bufferedWriter.newLine();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
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
}