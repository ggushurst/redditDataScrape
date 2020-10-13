package URLReaderJava;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class App {
    public static void main(String[] args) throws IOException {
        Config config = new Config();

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

        // Writing into database
        DatabaseRead dbr = new DatabaseRead(
            config.properties.getProperty("db.connection_string"),
            config.properties.getProperty("db.username"),
            config.properties.getProperty("db.password")
        );

        int lastID = dbr.getLastID(); //This is used to set the ID for each new element to a unique int, creating no override for last set of data input
        DatabaseConnection db = new DatabaseConnection(
            config.properties.getProperty("db.connection_string"),
            config.properties.getProperty("db.username"),
            config.properties.getProperty("db.password")
        );

        for(int i = 1; i <= postTitles.size() - 1; i++){
            redditData redditData = new redditData(i+lastID, postTitles.get(i), postVotes.get(i), postComments.get(i), postUserNames.get(i));
            db.insertData(redditData);
        }

        //Reading database

//        DatabaseRead dbr = new DatabaseRead("jdbc:postgresql://localhost/redditData", "greg", "Haha343606");
//        dbr.readAllData(); //This will prompt the user for a SQL input and return the data associated with the output


        //Creating a JSON file

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
