package URLReaderJava;

import java.sql.*;
import java.util.Scanner;

public class DatabaseRead {
    private String url;
    private String user;
    private String password;

    public DatabaseRead(String url, String user, String password){
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(this.url, this.user, this.password);
    }

    // A function that will output all data within the database, based on a user input
    public void readAllData () {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a PostgreSQL query: ");

        String SQL = scanner.nextLine();
        System.out.println("Processing your request, please wait one moment...");

        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt(1) + "\n" +
                        "Title: " + rs.getString(2) + "\n" +
                        "Votes: " + rs.getInt(3) + "\n" +
                        "Comment Count: " + rs.getInt(4) + "\n" +
                        "UserName: " + rs.getString(5));
            }
        }
         catch (SQLException ex){
            System.out.println(ex.getMessage());
        }

    }

    //Finding the last ID assigned so when new data is input, it will not override previous ID #'s
    public int getLastID(){
        String SQL = "SELECT MAX(ID) FROM redditPosts";

        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            if (rs.next()) {
                int lastID = rs.getInt(1);
                return lastID;
            }
        }
        catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    return -1;
    }

}
