package URLReaderJava;

import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import java.util.ArrayList;


public class Example {
    public static void main(String[] args) {
        Config config = new Config();

        //Initializing the arrays that characteristic of the posts will be stored in
        ArrayList<String> postTitles = new ArrayList<String>();
        ArrayList<Integer> postScore = new ArrayList<Integer>();
        ArrayList<Integer> postComments = new ArrayList<Integer>();
        ArrayList<String> postUserNames = new ArrayList<String>();
        ArrayList<String> postDate = new ArrayList<String>();

        //Populate the array with necessary information from the API Wrapper
        RedditService redditService = new RedditService(config.properties);
        Listing<Submission> submissions = redditService.GetFrontPageTitles();
        for (Submission s : submissions) {
            postTitles.add(s.getTitle());
            postScore.add(s.getScore());
            postComments.add(s.getCommentCount());
            postUserNames.add(s.getAuthor());
            postDate.add(s.getCreated().toString());
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
            redditData redditData = new redditData(i+lastID, postTitles.get(i), postScore.get(i), postComments.get(i), postUserNames.get(i), postDate.get(i));
            db.insertData(redditData);
        }

    }
}
