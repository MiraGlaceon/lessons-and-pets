
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;


/*Программа в многопоточном режиме формирует карту заданного сайта (список ссылок),
и записывает её в файл. Ссылки на дочерние страницы располагаются в файле с
отступами на одну табуляцию относительно родительских.
Используются ссылки на страницы, размещённые на том же домене.
Ссылки на другие сайты и поддомены, а также на внутренние элементы страниц, исключены*/
public class Main {

    public static final String START_URL = "https://lenta.ru/";

    public static void main(String[] args) {
        Set<String> urlsHaveAlreadyBeen = new HashSet<>();
        urlsHaveAlreadyBeen.add(START_URL);
        ArrayList<String> toWrite = new ArrayList<>();
        try {
            FileWriter writer = new FileWriter("links.txt");
            new ForkJoinPool().invoke(new LinkPull(toWrite, START_URL, 0, urlsHaveAlreadyBeen));
            for (int i = toWrite.size() - 1; i >= 0; i--) {
                writer.write(toWrite.get(i) + "\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
