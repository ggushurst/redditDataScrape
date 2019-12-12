package URLReaderJava;

//import java.net.*;
import java.io.*;
import java.util.*;


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
        Elements comments = doc.select("#siteTable div div ul");
        Elements userName = doc.select("#siteTable div div p.tagline a.author");
        ArrayList<String> postTitles = new ArrayList<String>();
        ArrayList<String> postLinks = new ArrayList<String>();
        ArrayList<Integer> postVotes = new ArrayList<Integer>();
        ArrayList<Integer> postComments = new ArrayList<Integer>();
        ArrayList<String> postUserNames = new ArrayList<String>();
        String case1 = "sponsored-indicator rank";
        for (Element e: content) {
            postTitles.add(e.text());
            postLinks.add(e.absUrl("href"));
        }
        for (Element v: votes) {
            String votesTemp = v.attr("title");
            if(votesTemp.equals("")){
                votesTemp = "0";
            }
            int votesFinal = Integer.parseInt((votesTemp));
            postVotes.add(votesFinal);
        }
        for (Element c: comments) {
            String promoted = c.select("li span.promoted-span").text();
            String text = c.select("li.first a").text();
            text = text.replaceAll("\\D+", "");
//            System.out.println(promoted + " & " +text);
            int number;
            if(promoted.equals("promoted")){
//                System.out.println("NAY");
                number = -1;
            }
            else {
//              System.out.println("YAY");
                number = Integer.parseInt(text);
            }
            postComments.add(number);
        }
//        System.out.println(postComments);
        for (Element u: userName) {
            postUserNames.add(u.text());
        }

        //Creating a complete array of information
        System.out.println("Titles " + postTitles.size() + " Links " + postLinks.size() +
                " Votes " + postVotes.size() + " Comments " + postComments.size() +
                " UserNames " + postUserNames.size());



//        System.out.println(Data);


        //Creating a JSON file that will be read into database
        JSONObject obj = new JSONObject();
        JSONArray dlist = new JSONArray();
        JSONObject data = new JSONObject();
        //Data
//        data.put("path", redditDataPath);
//        data.put("lastprofile", profileList.getSelectedValue());
        dlist.add(data);

        obj.put("data", dlist);




        JSONObject postData = new JSONObject();
        JSONObject postDataListObj = new JSONObject();
        JSONArray postDataList = new JSONArray();

        for(int i = 0; i <= postTitles.size() - 1; i++){
            postData = new JSONObject();
            postData.put("ID", i);
            postData.put("Title", postTitles.get(i));
            postData.put("Votes", postVotes.get(i));
            postData.put("Comments", postComments.get(i));
            postData.put("Username", postUserNames.get(i));
            postData.put("Link", postLinks.get(i));
            postDataList.add(postData);
            postDataListObj.put("post"+i, postDataList);

        }
        obj.put("posts", postDataList);



//        String redditData = new Gson().toJson(Data);
//        System.out.println(redditData);

//        Write json file
        try (FileWriter file = new FileWriter("redditData.json")){
            file.write(obj.toJSONString());
            file.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}