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

    public long insertData(redditData redditdata) {
        String SQL = "INSERT INTO redditPosts(id, title, votes, comments, username, date) " +
                "VALUES(?,?,?,?,?,?)";
        long idTrack = 0;

        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(SQL,
                        Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, redditdata.getId());
            pstmt.setString(2, redditdata.getTitle());
            pstmt.setInt(3, redditdata.getVotes());
            pstmt.setInt(4, redditdata.getCommentCount());
            pstmt.setString(5, redditdata.getUserName());
            pstmt.setString(6, redditdata.getDate());

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