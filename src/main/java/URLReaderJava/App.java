package URLReaderJava;

//import java.net.*;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import java.sql.Connection;
import java.sql.DriverManager;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
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

    public static void main(String[] args) throws IOException {

        //Connecting to the website
        Document doc = Jsoup.connect("https://old.reddit.com/").get();
        System.out.println(doc.title()); //Print title of the website, for confirmation

        //Creating the css query paths for various elements
        Elements content = doc.select("#siteTable div div.top-matter p.title a.title");
        Elements votes = doc.select("#siteTable div div.score.unvoted");
        Elements comments = doc.select("#siteTable div div ul");
        Elements userName = doc.select("#siteTable div div p.tagline a.author");

        //Initializing the arrays that characteristic of the posts will be stored in
        ArrayList<String> postTitles = new ArrayList<String>();
        ArrayList<Integer> postVotes = new ArrayList<Integer>();
        ArrayList<Integer> postComments = new ArrayList<Integer>();
        ArrayList<String> postUserNames = new ArrayList<String>();

        //Populating the arrays
        for (Element e: content) {
            postTitles.add(e.text());
        }
        for (Element v: votes) {
            String votesTemp = v.attr("title");
            if(votesTemp.equals("")){ //If there are no comments for the posts, default will be 0
                votesTemp = "0";
            }
            int votesFinal = Integer.parseInt((votesTemp));
            postVotes.add(votesFinal);
        }
        for (Element c: comments) {
            String promoted = c.select("li span.promoted-span").text();
            String text = c.select("li.first a").text();
            text = text.replaceAll("\\D+", ""); //Removing the letters and leaving only integers
            int number;
            if(promoted.equals("promoted")){ //Checking if posts is promotion, so it can be filtered out in postgres
                number = -1;
            }
            else {
                number = Integer.parseInt(text);
            }
            postComments.add(number);
        }
        for (Element u: userName) {
            postUserNames.add(u.text());
        }

        //Creating a JSON file that will be read into database

        JSONObject masterObj = new JSONObject(); //Master object that will be used to write the json file
        JSONArray header = new JSONArray();
        JSONObject data = new JSONObject();


        //Insert header
        data.put("Website", "old.reddit.com");
        header.add(data);

        //Insert header into master object
        masterObj.put("data", header);


        //Initializing JSON objects/arrays
        JSONObject postData = new JSONObject();
        JSONArray postDataList = new JSONArray();

        for(int i = 0; i <= postTitles.size() - 1; i++){
            postData = new JSONObject();
            postData.put("ID", i);
            postData.put("Title", postTitles.get(i));
            postData.put("Votes", postVotes.get(i));
            postData.put("Comments", postComments.get(i));
            postData.put("Username", postUserNames.get(i));
            postDataList.add(postData);


        }
        masterObj.put("posts", postDataList);


        //Write json file
        String time = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
                .format(new java.util.Date()); //Created a timestamp to add to filename to have unique files
        try (FileWriter file = new FileWriter("redditData_" + time + ".json")){
            file.write(masterObj.toJSONString());
            file.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}