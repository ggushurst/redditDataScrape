package URLReaderJava;

// class used to create a "Row" to be inserted into postgres
public class redditData {
    private int id;
    private String title;
    private int votes;
    private int commentCount;
    private String userName;

    public redditData(int id, String title, int votes, int commentCount, String userName){
        this.id = id;
        this.title = title;
        this.votes = votes;
        this.commentCount = commentCount;
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getVotes() {
        return votes;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public String getUserName() {
        return userName;
    }
}
