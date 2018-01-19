package classic.hugeFileDereplication;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
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
 */
public class Solution {
    private static final Charset DEFAULT_CHARACTER_SET = Charset.forName("UTF-8");//文件字符集
    private static final int DEFAULT_BUFFER_READ_SIZE = 10 * 1024 * 1024;//读缓冲大小
    private static final int DEFAULT_BUFFER_WRITE_SIZE = 10 * 1024 * 1024;//写缓冲大小
    private static final int DEFAULT_FILE_NUM = 100;//拆分文件个数

    public static void main(String[] args) {
        String filePath = "D://log.txt1";
        String outDir = "D://";
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
        String tempPath = createTempDir(outDir);
        if (tempPath == null) {
            return;
        }
        //拆分
        boolean splitResult = split(filePath, tempPath);
        println("split complete.result:" + splitResult);
        //合并
        boolean mergeResult = false;
        if (splitResult) {
            mergeResult = merge(tempPath, outDir);
            println("merge complete.result:" + mergeResult);
            //清理
            clearTemp(new File(tempPath));
        }
        println("reduce " + (mergeResult ? "success" : "failed"));
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
        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultFile), DEFAULT_CHARACTER_SET), DEFAULT_BUFFER_WRITE_SIZE)) {
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
        BufferedWriter bufferedWriter;
        BufferedWriter[] bufferedWriters = null;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), DEFAULT_CHARACTER_SET), DEFAULT_BUFFER_READ_SIZE)) {
            String line;
            bufferedWriters = buildBufferedWriter(createTempFiles(tempPath));
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter = bufferedWriters[Math.abs(line.hashCode() % DEFAULT_FILE_NUM)];
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeWriter(bufferedWriters);
        }
        return true;
    }

    private BufferedWriter[] buildBufferedWriter(File[] files) throws FileNotFoundException {
        int length = files.length;
        BufferedWriter[] result = new BufferedWriter[length];
        for (int i = 0; i < length; i++) {
            result[i] = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(files[i]), DEFAULT_CHARACTER_SET), DEFAULT_BUFFER_WRITE_SIZE);
        }
        return result;
    }

    private File[] createTempFiles(String tempPath) throws IOException {
        File[] files = new File[DEFAULT_FILE_NUM];
        for (int i = 0; i < DEFAULT_FILE_NUM; i++) {
            files[i] = createTempFile(tempPath, i);
        }
        return files;
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

    private File createTempFile(String tempPath, int tempFileNum) throws IOException {
        String tempFilePath = tempPath + "file" + tempFileNum;
        return createTempFile(tempFilePath);
    }

    private String createTempDir(String dir) {
        String tempDirName = "temp";
        StringBuilder tempPathBuilder = new StringBuilder();
        if (isDirExist(dir)) {
            tempPathBuilder.append(dir);
        } else {
            String sysTempDir = System.getProperty("java.io.tmpdir");
            println("dir doesn't exist.dir:" + dir + ",use system temp dir:" + sysTempDir);
            tempPathBuilder.append(sysTempDir);
        }
        tempPathBuilder.append(tempDirName).append(File.separator);
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

    private boolean isDirExist(String dirPath) {
        File file = new File(dirPath);
        return file.exists() && file.isDirectory();
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
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), DEFAULT_CHARACTER_SET), DEFAULT_BUFFER_READ_SIZE)) {
                String line;
                Set<Integer> lineHashCode = new HashSet<>();
                while ((line = bufferedReader.readLine()) != null) {
                    if (lineHashCode.add(line.hashCode())) {
                        bufferedWriter.write(line);
                        bufferedWriter.newLine();
                    }
                }
                bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }
}