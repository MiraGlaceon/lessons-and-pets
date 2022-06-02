import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

    public static void copyFolder(String fromPath, String toPath) {
        // TODO: write code copy content of sourceDirectory to destinationDirectory
        File fromFolder = new File(fromPath);
        Path toFolder = Paths.get(toPath);

        File[] files = fromFolder.listFiles();
        for (File file : files) {
            try {
                Files.copy(file.toPath(), toFolder.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
                if (file.isDirectory()) {
                    copyFolder(file.getAbsolutePath(), toPath + "/" + file.getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
