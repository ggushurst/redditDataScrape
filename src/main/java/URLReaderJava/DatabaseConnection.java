package URLReaderJava;

import java.sql.*;

public class DatabaseConnection {
    private String url;
    private String user;
    private String password;

    public DatabaseConnection(String url, String user, String password){ //initialize the instance variables
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection connect() throws SQLException { //establishing the connection
        return DriverManager.getConnection(this.url, this.user, this.password);
    }

    public long insertData(redditData redditData) {
        String SQL = "INSERT INTO redditPosts(id, title, votes, comments, username) " +
                "VALUES(?,?,?,?,?)";
        long idTrack = 0;

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(SQL,
                        Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, redditData.getId());
            pstmt.setString(2, redditData.getTitle());
            pstmt.setInt(3, redditData.getVotes());
            pstmt.setInt(4, redditData.getCommentCount());
            pstmt.setString(5, redditData.getUserName());

            int affectedRows = pstmt.executeUpdate();
            // Check the affected rows
            if(affectedRows > 0){
                // get ID back
                try(ResultSet rs = pstmt.getGeneratedKeys()){
                    if(rs.next()){
                        idTrack = rs.getLong(1);
                    }
                }
                catch (SQLException ex){
                    System.out.println(ex.getMessage());
                }
            }
        }
        catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        return idTrack;
    }
}