package URLReaderJava;

import java.util.Properties;

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


public class RedditService {

    // Config: The config singleton passed to the constructor. It holds all of the configuration.
    private Properties config;
    public RedditClient client;

    public RedditService(Properties properties) {
        config = properties;
        UserAgent userAgent = new UserAgent("bot", "com.example.usefulbot", "v0.1", config.getProperty("reddit.username"));
        Credentials credentials = Credentials.script(
            config.getProperty("reddit.username"),
            config.getProperty("reddit.password"),
            config.getProperty("reddit.client_id"),
            config.getProperty("reddit.client_secret")
        );
        NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);
        client = OAuthHelper.automatic(adapter, credentials);
    }

    public Listing<Submission> GetFrontPageTitles() {
        DefaultPaginator<Submission> frontPage = client.frontPage()
            .sorting(SubredditSort.TOP)
            .timePeriod(TimePeriod.DAY)
            .limit(30)
            .build();

        Listing<Submission> submissions = frontPage.next();
        return submissions;

        // SubredditReference subreddit = client.subreddit("RocketLeague");
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
