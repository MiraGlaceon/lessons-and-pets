import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class LinkPull extends RecursiveAction {

    public static final int LIMIT = 4; // ограничитель, чтобы не запускать рекурсию очень глубоко
    public static final String START_URL = "https://lenta.ru";

    // содержит ссылки, которые уже были
    private Set<String> urlsHaveAlreadyBeen;

    private int depth;
    private String url;
    private String tab;
    private ArrayList<String> toWrite;

    public LinkPull(ArrayList<String> toWrite, String url, int depth, Set<String> urlsHaveAlreadyBeen) {
        this.urlsHaveAlreadyBeen =urlsHaveAlreadyBeen;
        this.toWrite = toWrite;
        this.url = url;
        this.depth = depth;
        tab = "";
        for (int i = 0; i < depth; i++) {
            tab = tab.concat("\t");
        }
    }

    @Override
    protected void compute() {
        Document document;
        try {
            document = Jsoup.connect(url).get();
            Thread.sleep(150);
        } catch (Exception e) {
            return;
        }

        if (depth == LIMIT) {
            toWrite.add(tab + url);
            return;
        }

        Elements elements = document.getElementsByTag("a");
        if (elements.size() != 0) {
            ForkJoinTask.invokeAll(createSubtasks(elements));
        }
        toWrite.add(tab + url);
    }

    private List<LinkPull> createSubtasks(Elements elements) {
        List<LinkPull> subtasks = new ArrayList<>();
        //счетчик выставлен для примера, чтобы не обходить все ссылки сайта
        int count = 0;
        for (Element element : elements) {
            if (count > 30) {break;}
            count++;
            String link = START_URL + element.attr("href");
            if (urlsHaveAlreadyBeen.contains(link)) {
                continue;
            } else {
                urlsHaveAlreadyBeen.add(link);
            }
            subtasks.add(new LinkPull(toWrite, link, depth + 1, urlsHaveAlreadyBeen));
        }
        return subtasks;
    }
}