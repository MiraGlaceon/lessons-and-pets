package space.mira;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        FilesScanner filesScanner = new FilesScanner();
        List<TextFile> textFiles = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите путь до файла или exit, чтобы завершить программу:");
        String line;
        while (true) {
            line = reader.readLine();
            if (line.equalsIgnoreCase("exit")) {
                break;
            }

            boolean isReadable = filesScanner.findAndCollectTextFiles(line, textFiles);
            if (!isReadable) {
                System.out.println("Введите корректный путь или exit:");
            } else  {
                break;
            }
        }

        Collections.sort(textFiles);

        try (FileWriter writer = new FileWriter("mergedText.txt")){
            for (TextFile file : textFiles) {
                writer.write(file.getText() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Не удалось записать в файл");
        }
    }
}
