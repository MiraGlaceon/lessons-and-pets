import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*Задача: пропарсить страницу браузера, записать в Json, пропарсить этот Json и вывести в консоль
* количество станций на каждой линии*/
public class Main {

    public static String regexQuotes = "\\\"[^\\\"]*\\\"";
    public static String stationRecognizer = "\\\"[^\\\"\\.]*\\\"";
    public static String regexLines = "/";
    public static String regexStations = ";";
    public static String path = "C:\\Users\\1\\Desktop\\JsonParseReadWrite\\src\\main\\resources\\MetroStations.json";

    public static void main(String[] args) {
        String source = getInfoFromHtml("https://skillbox-java.github.io/");
        createJsonFile("MetroStations.json", source);
        String fileContent = readJsonFile(path);
        printAnswer(fileContent);
    }

    public static String getInfoFromHtml(String link){
        //пропустим проверку link на валидность...
        StringBuilder builder = new StringBuilder();
        String text = "";

        try {
            //я выбрал такой формат создания строки, чтобы позже было проще работать с ней
            Document doc = Jsoup.connect(link).maxBodySize(0).get();
            Elements elements = doc.select("h2").select("div").select("span");
            for (Element element : elements) {
                if (element.hasAttr("data-line")) {
                    builder.append(regexLines + element.attr("data-line") + " ");
                }
                text = element.text();
                if (text.equals("")) {
                    continue;
                }
                if (text.toCharArray()[1] == '.' || text.toCharArray()[2] == '.') {
                    builder.append(text + " ");
                } else {
                    builder.append(text + regexStations);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    public static void createJsonFile(String fileName, String text) {
        //пропустим проверку fileName и text на валидность...
        JSONObject object = new JSONObject();
        String line = "";
        String[] lines = text.split(regexLines);
        //нулевой элемент пропускаем, он пустой
        for (int i = 1; i < lines.length; i++) {
            JSONArray array = new JSONArray();
            String[] stations = lines[i].split(regexStations);
            line = stations[0];
            for (int j = 1; j < stations.length; j++) {
                array.add(stations[j]);
            }
            object.put(line, array);
        }
        try {
            Files.write(Paths.get(fileName), object.toJSONString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readJsonFile(String fileName) {
        try {
            FileReader reader = new FileReader(fileName);
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(reader);
            return object.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void printAnswer(String text) {
        List<String> linesAndStations = new ArrayList<>();
        Pattern pattern = Pattern.compile(regexQuotes);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            linesAndStations.add(matcher.group());
        }

        String lineName = "";
        int countStations = 0;
        for (int i = 1; i < linesAndStations.size(); i++) {
            if (!linesAndStations.get(i).matches(stationRecognizer)) {
                countStations++;
            } else {
                lineName = linesAndStations.get(i - countStations - 1);
                System.out.println("Кол-во станций на линии " + lineName + ": " + countStations);
                countStations = 0;
            }
        }
    }
}
