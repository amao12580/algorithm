package classic.hugeFileDereplication;

import org.apache.commons.io.FileUtils;

import java.io.*;
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
 * 5.单文件:多线程拆分  ？
 */
public class Solution {
    private static final Charset DEFAULT_CHARACTER_SET = Charset.forName("UTF-8");//文件字符集
    private static final int DEFAULT_BUFFER_READ_SIZE = 10 * 1024 * 1024;//读缓冲大小
    private static final int DEFAULT_BUFFER_WRITE_SIZE = 10 * 1024 * 1024;//写缓冲大小
    private static final int DEFAULT_PARALLELISM_LEVEL = Runtime.getRuntime().availableProcessors() * 2;//并发级别
    private static final int DEFAULT_FILE_MAX_LINE = 100 * 1024;//子文件的行数上限
//    private static final long DEFAULT_FILE_MAX_LINE = 1024 * 1024;//子文件的行数上限 105w、大小约512MB、文件个数约200

    public static void main(String[] args) throws IOException {
//        String filePath = "E://in/log.txt";
        String filePath = "D://log.txt";
        String outDir = "D://";
//        System.out.println(new File(filePath).getParent());
//        System.out.println(new File(filePath).getName());
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
            int lineNum = 0;
            long fileNum = 0;
            bufferedWriter = buildBufferedWriter(createTempFile(tempPath, fileNum));
            while ((line = bufferedReader.readLine()) != null) {
                if (lineNum >= DEFAULT_FILE_MAX_LINE) {//新文件
                    println("sub file num " + fileNum + " complete.");
                    closeWriter(bufferedWriter);
                    lineNum = 0;
                    fileNum++;
                    bufferedWriter = buildBufferedWriter(createTempFile(tempPath, fileNum));
                }
                bufferedWriter.write(line);
                bufferedWriter.newLine();
                lineNum++;
            }
            println("all sub file num:" + (fileNum + 1));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeWriter(bufferedWriter);
        }
        return true;
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
            try (BufferedReader bufferedReader = buildBufferedReader(files.get(fileNumOffset));
                 BufferedWriter bufferedWriter = buildBufferedWriter(createTempFile(uniquePath, fileNumOffset))) {
                uniqueTransferFile(bufferedReader, bufferedWriter, lineHashCodes);
                closeReader(bufferedReader);
                closeWriter(bufferedWriter);
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
                clearBitSet(lineHashCodes);
            }
            fileNumOffset++;
        }
        clearBitSet(lineHashCodes);
        return true;
    }

    private boolean merge(String uniquePath, String outDir) {
        String resultFilePath = outDir + "resultFile";
        File resultFile;
        try {
            resultFile = createTempFile(resultFilePath);
        } catch (IOException e) {
            e.printStackTrace();
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

    private List<Set<Integer>> buildSet() {
        List<Set<Integer>> sets = new ArrayList<>(2);//正+负
        sets.add(new HashSet<>());
        sets.add(new HashSet<>());
        return sets;
    }

    private void closeReader(Reader... readers) {
        if (readers == null) {
            return;
        }
        for (Reader item : readers) {
            if (item != null) {
                try {
                    item.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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

    private void clearBitSet(List<Set<Integer>> list) {
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

    private void uniqueTransferFile(BufferedReader bufferedReader, BufferedWriter bufferedWriter, List<Set<Integer>> lineHashCodes) throws IOException {
        String line;
        int setIndex, lineHashCode;
        while ((line = bufferedReader.readLine()) != null) {
            lineHashCode = line.hashCode();
            setIndex = lineHashCode >= 0 ? 0 : 1;
            if (lineHashCodes.get(setIndex).add(lineHashCode)) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        }
    }

    private void println(String s) {
        System.out.println(Calendar.getInstance().getTime().toString() + " - " + s);
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
                new MergeFileTask(files, newFiles, start, middle).fork().join();
                new MergeFileTask(files, newFiles, middle + 1, end).fork().join();
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
                new UniqueFileTask(lineHashCodes, files, min, middle).fork().join();
                new UniqueFileTask(lineHashCodes, files, middle + 1, max).fork().join();
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
            lineHashCodes = lineHashCodes == null ? buildSet() : lineHashCodes;
            try (BufferedReader bufferedReader = buildBufferedReader(file);
                 BufferedWriter bufferedWriter = buildBufferedWriter(newFile)) {
                uniqueTransferFile(bufferedReader, bufferedWriter, lineHashCodes);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (autoClear) {
                    clearBitSet(lineHashCodes);
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
}