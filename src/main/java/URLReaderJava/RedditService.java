package URLReaderJava;

import io.github.cdimascio.dotenv.Dotenv;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.references.SubredditReference;


public class RedditService {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        dotenv.get("REDDIT_USERNAME");
        // System.out.println(dotenv.get("REDDIT_USERNAME"));
        GetFrontPageTitles();
    }

    public static void GetFrontPageTitles() {

        UserAgent userAgent = new UserAgent("bot", "com.example.usefulbot", "v0.1", "pmotard");
        Credentials credentials = Credentials.script("", "", "", "");
        NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);
        RedditClient redditClient = OAuthHelper.automatic(adapter, credentials);
        SubredditReference subreddit = redditClient.subreddit("RocketLeague");

        DefaultPaginator<Submission> frontPage = redditClient.frontPage()
            .sorting(SubredditSort.TOP)
            .timePeriod(TimePeriod.DAY)
            .limit(30)
            .build();

        Listing<Submission> submissions = frontPage.next();
        for (Submission s : submissions) {
            System.out.println(s.getTitle());
        }

        // // Request available user flair
        // List<Flair> userFlairOptions = subreddit.userFlairOptions();

        // if (!userFlairOptions.isEmpty()) {
        //     // Arbitrarily choose a new Flair
        //     Flair newFlair = userFlairOptions.get(0);

        //     System.out.println(newFlair);
        //     // Update the flair on the website
        //     // subreddit.selfUserFlair().updateToTemplate(newFlair.getId(), "");
        // }
    }

}
