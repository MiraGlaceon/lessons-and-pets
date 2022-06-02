import java.io.File;

public class FileUtils {

    public static long calculateFolderSize(String path) {
        File folder = new File(path);
        if (!folder.isDirectory()) {
            return -1; // для потенциальных проверок
        }

        File[] files = folder.listFiles();
        long sizeOfFiles = 0;
        for (File file : files) {
            if (file.isDirectory()) {
                sizeOfFiles += calculateFolderSize(file.getAbsolutePath());
                continue;
            }
            sizeOfFiles += file.length();
        }
        return sizeOfFiles;
    }
}
