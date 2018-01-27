package classic.hugeFileDereplication;

import basic.Util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
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
 * 100GB的文件，按行去重复
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
    private static final int DEFAULT_FILE_MAX_LINE = 10 * 1024;//子文件的行数上限
//    private static final long DEFAULT_FILE_MAX_LINE = Integer.MAX_VALUE;//子文件的行数上限

    public static void main(String[] args) {
        String filePath = "D://log.txt1";
        String outDir = "D://";
        Solution solution = new Solution();
        solution.reduce(filePath, outDir);


//        BloomFilter<CharSequence> filter = BloomFilter.create(Funnels.stringFunnel(UTF_8), 10000000, 0.001F);
//        filter.mightContain();
//        BitSet bitSet=new BitSet();
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
        boolean clearResult = clear(tempPath);
        if (!clearResult) {
            println("clear failed.return");
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
        BitSet[] bitSet = null;
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
                bitIndex = cover2BitIndex(line.hashCode());
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
            closeBitSet(bitSet);
            closeWriter(bufferedWriter);
        }
        return true;
    }


    private boolean unique(String tempPath, String uniquePath) {
        List<File> files = listFilesOrderByName(tempPath);
        if (files == null || files.isEmpty()) {
            return false;
        }
        boolean copyResult;
        try {
            copyResult = fileCopy(files.get(0), createTempFile(uniquePath, 0));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if (files.size() == 1) {
            return copyResult;
        }
        return copyResult && unique(uniquePath, files, null, 1);
    }

    private boolean unique(String uniquePath, List<File> files, BitSet[] bitSet, int fileNumOffset) {
        if (bitSet == null) {
            bitSet = buildBitSet();
            BufferedWriter bufferedWriter = null;
            try (BufferedReader bufferedReader = buildBufferedReader(files.get(fileNumOffset))) {
                String line;
                int bitIndex, bitSetIndex, lineHashCode;
                bufferedWriter = buildBufferedWriter(createTempFile(uniquePath, fileNumOffset));
                while ((line = bufferedReader.readLine()) != null) {
                    lineHashCode = line.hashCode();
                    bitIndex = cover2BitIndex(line.hashCode());
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
            return unique(uniquePath, files, bitSet, fileNumOffset + 1);
        } else {
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
                closeBitSet(bitSet);
            }
            return unique(uniquePath, files, null, fileNumOffset);
        }
    }

    private boolean merge(String tempPath, String outDir) {
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
        File dir = new File(tempPath);
        File[] files = dir.listFiles();
        if (files == null || files.length <= 0) {
            println("dir.listFiles() is empty.");
            return false;
        }
        ForkJoinPool pool = null;
        try (BufferedWriter bufferedWriter = buildBufferedWriter(resultFile)) {
            MergeFileTask task = new MergeFileTask(bufferedWriter, files, 0, files.length - 1);
            pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 2);
            Boolean isSuccess = pool.invoke(task);
            if (!isSuccess || task.isCompletedAbnormally()) {
                println("MergeFileTask is error.");
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (pool != null) {
                pool.shutdown();
            }
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

    private BitSet[] buildBitSet() {
        BitSet[] bitSets = new BitSet[2];//正+负
        bitSets[0] = new BitSet(Integer.MAX_VALUE / 8); //64MB 内存;
        bitSets[1] = new BitSet(Integer.MAX_VALUE / 8); //64MB 内存;
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

    private void closeBitSet(BitSet... bitSet) {
        if (bitSet == null) {
            return;
        }
        for (BitSet item : bitSet) {
            if (item != null) {
                item.clear();
            }
        }
    }

    private boolean clear(String dir) {
        return clearTemp(new File(dir));
    }

    private boolean clearTemp(File dir) {
        if (dir == null || !dir.exists()) {
            return false;
        }
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files == null) {
                return false;
            }
            for (File item : files) {
                if (!clearTemp(item)) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    private File createTempFile(String tempFilePath) throws IOException {
        File file = new File(tempFilePath);
        if (file.exists()) {
            println("file has existed.file path:" + file.getPath());
            return null;
        }
        if (!file.createNewFile()) {
            println("file create failed.");
            return null;
        }
        return file;
    }

    private File createTempFile(String tempPath, long tempFileNum) throws IOException {
        String tempFilePath = tempPath + tempFileNum;
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
            String sysTempDir = System.getProperty("java.io.tmpdir");
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

    private boolean fileCopy(File source, File target) {
        try (OutputStream outputStream = new FileOutputStream(target)) {
            Files.copy(source.toPath(), outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            println("fileCopy error.source=" + source.getPath() + ",target=" + target.getPath());
            return false;
        }
        return true;
    }

    private List<File> listFilesOrderByName(String filePath) {
        File file = new File(filePath);
        File[] files = file.listFiles();
        if (files == null) {
            return null;
        }
        List<File> fileList = Arrays.asList(files);
        fileList.sort((fileThis, fileOther) -> {
            if (fileThis.isDirectory() && fileOther.isFile())
                return -1;
            if (fileThis.isFile() && fileOther.isDirectory())
                return 1;
            return fileThis.getName().compareTo(fileOther.getName());
        });
        return fileList;
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
        private BufferedWriter bufferedWriter;
        private File[] files;
        private int min;
        private int max;

        MergeFileTask(BufferedWriter bufferedWriter, File[] files, int min, int max) {
            this.bufferedWriter = bufferedWriter;
            this.files = files;
            this.min = min;
            this.max = max;
        }

        @Override
        protected Boolean compute() {
            if (max == min) {
                boolean result = merge(bufferedWriter, files[max]);
                println(files[max].getPath() + " merge completed.result:" + result);
                return result;
            } else {
                int middle = (max + min) / 2;
                MergeFileTask left = new MergeFileTask(bufferedWriter, files, min, middle);
                MergeFileTask right = new MergeFileTask(bufferedWriter, files, middle + 1, max);
                left.fork();
                right.fork();
                left.join();
                right.join();
            }
            return true;
        }

        private boolean merge(BufferedWriter bufferedWriter, File file) {
            try (BufferedReader bufferedReader = buildBufferedReader(file)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }
                bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
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
                left.fork();
                right.fork();
                left.join();
                right.join();
            }
            return true;
        }

        private boolean unique(BitSet[] bitSet, File file) {
            File newFile;
            try {
                newFile = createTempFile(file.getParent(), Long.valueOf(file.getName() + "0"));
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
                    bitIndex = cover2BitIndex(line.hashCode());
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
                Files.delete(file.toPath());
            } catch (IOException e) {
                return false;
            }
            return newFile.renameTo(file);
        }
    }
}