import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private static final int DIVISION_UNIT = 1024;

    public static void main(String[] args) {
        // не делаю никаких проверок для упрощения
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите путь до папки:");
        String path = "";
        try {
            path = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long sizeOfFiles = FileUtils.calculateFolderSize(path);
        System.out.println("Размер папки " + path + " составляет " + toSizeFormat(sizeOfFiles));
    }

    public static String toSizeFormat(long size) {
        long remainder = 0;
        int digitalStep = 0;
        while (size / DIVISION_UNIT > 0) {
            digitalStep++;
            remainder = size % DIVISION_UNIT;
            size /= DIVISION_UNIT;
        }

        String additional = "";
        if (remainder / 100 < 10 && remainder / 100 > 0) {
            additional = "." + (remainder / 100);
        }

        switch (digitalStep) {
            case 0:
                return size + " байт";
            case 1:
                return size + additional + " Кб";
            case 2:
                return size + additional + " Mб";
            default:
                return size + additional + " Гб";
        }
    }
}
