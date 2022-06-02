
import java.io.File;
import java.util.Arrays;


/*Программа уменьшает изображения в 2 раза без потери качества, при помощи библиотеки imgscalr
*  и с использованием многопоточности*/
public class Main {

    public static final String FROM = "link to folder with images";
    public static final int PROCESSORS_COUNT = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        Resizer resizer;
        File source = new File(FROM);
        File[] files = source.listFiles();
        int step = 0;
        if (files.length < PROCESSORS_COUNT || PROCESSORS_COUNT == 1) {
            resizer = new Resizer(files);
            new Thread(resizer).start();
        } else {
            int border = files.length / PROCESSORS_COUNT;
            int tail = files.length % PROCESSORS_COUNT;
            for (int i = 0; i < PROCESSORS_COUNT; i++) {
                resizer = new Resizer(Arrays.copyOfRange(files, step, step + border));
                new Thread(resizer).start();
                step += border;
            }
            resizer = new Resizer(Arrays.copyOfRange(files, step, step + tail + border));
            new Thread(resizer).start();
        }
    }
}
