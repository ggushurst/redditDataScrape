package URLReaderJava;

//import java.net.*;
import java.io.*;
import java.util.*;

//import jdk.javadoc.internal.doclets.formats.html.markup.Links;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import com.google.gson.*;

public class App {
    public static void ListsToMap(List<String> Titles, List<String> Votes) {
        Map<String, String> Info = new LinkedHashMap<String, String>();

        for (int i=0; i<Titles.size(); i++) {
            Info.put(Titles.get(i), Votes.get(i));
            System.out.println(Titles.get(i) + ":" + Votes.get(i));
        }
        System.out.println(Info);
    }
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://old.reddit.com/").get();
        System.out.println(doc.title());
        Elements content = doc.select("#siteTable div div.top-matter p.title a.title");
        Elements votes = doc.select("#siteTable div div.score.unvoted");
        Elements comments = doc.select("#siteTable div div ul li.first a");
        Elements userName = doc.select("#siteTable div div p.tagline a.author");
        ArrayList<String> Titles = new ArrayList<String>();
        ArrayList<String> Links = new ArrayList<String>();
        ArrayList<String> Votes = new ArrayList<String>();
        ArrayList<Integer> Comments = new ArrayList<Integer>();
        ArrayList<String> userNames = new ArrayList<String>();
        String case1 = "sponsored-indicator rank";
        for (Element e: content) {
            Titles.add(e.text());
            Links.add(e.absUrl("href"));
        }
        for (Element v: votes) {
            Votes.add(v.attr("title"));
        }
        for (Element c: comments) {
            String text = c.text();
            text = text.replaceAll("\\D+","");
            int number = Integer.parseInt(text);
            Comments.add(number);
        }
        for (Element u: userName) {
            userNames.add(u.text());
        }


        //Creating a complete array of information

        ArrayList<String> Data = new ArrayList<String>();

        for(int i=0; i<Titles.size(); i++){
            Data.add(Votes.get(i) + ":" + Titles.get(i));
        }
//        System.out.println(Data);

        //Creating a JSON file that will be read into database

        String redditData = new Gson().toJson(Data);
//        System.out.println(redditData);

//        Write json file
        try (FileWriter file = new FileWriter("redditData.json")){
            file.write(redditData);
            file.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}