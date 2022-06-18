package space.mira;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class FilesScanner {
    public static final String REGEX_TXT = ".+\\.txt";

    public boolean findAndCollectTextFiles(String path, List<TextFile> textFiles) {
        File container = new File(path);

        //Если неправильный путь или нет ссылки на список, то прекращаем работу
        if (!container.exists() || textFiles == null) {
            return false;
        }

        if (!container.isDirectory()) {
            String singleFile = container.getName();
            if (isTxt(singleFile)) {
                //сохраняем файл в коллекцию
                TextFile textFile = new TextFile(singleFile, path, getContent(path));
                textFiles.add(textFile);
                return true;
            }
        }

        File[] files = container.listFiles();
        for (File file : files) {
            findAndCollectTextFiles(path + "/" + file.getName(), textFiles);
        }
        return true;
    }

    private String getContent(String path) {
        StringBuilder text = new StringBuilder();
        FileReader reader = null;
        try {
            reader = new FileReader(path);
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        }

        int symbolCode;
        try {
            while ((symbolCode = reader.read()) != -1) {
                text.append(Character.toChars(symbolCode));
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла");
        }

        return text.toString();
    }

    //В этот метод можно добавлять текстовые расширения
    private boolean isTxt(String singleFile) {
        return singleFile.matches(REGEX_TXT);
    }
}
