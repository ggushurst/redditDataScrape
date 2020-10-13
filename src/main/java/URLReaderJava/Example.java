package URLReaderJava;

import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;

public class Example {
    public static void main(String[] args) {
        Config config = new Config();
        RedditService redditService = new RedditService(config.properties);
        Listing<Submission> submissions = redditService.GetFrontPageTitles();
        for (Submission s : submissions) {
            System.out.println(s.getTitle());
        }
    }
}
