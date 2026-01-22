import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class jdbcpostgreSQL {

  public jdbcpostgreSQL() {}

/**
   *
   * @param customerId
   * @param timeLowerBound - MM-dd-yyyy format
   * @param timeUpperBound - MM-dd-yyyy format
   * @return List of titles ordered by date descending
   */
  public List<String> getRecentTitles(String customerId, String timeLowerBound, String timeUpperBound) {
    
    Connection conn = connect();

    int convertedTimeLowerBound = convertTimeToFloat(timeLowerBound);
    int convertedTimeUpperBound = convertTimeToFloat(timeUpperBound);

    List<String> listOfTitles = new LinkedList<String>();
    try{
      PreparedStatement sqlStatement = conn.prepareStatement(
        "SELECT originaltitle FROM ratings, titles " +
        "WHERE movieid = titleid AND " +
        "customerid = ? AND " +
        "date >= ? AND " +
        "date <= ? " +
        "ORDER BY year");
      sqlStatement.setString(1, customerId);
      sqlStatement.setFloat(2, convertedTimeLowerBound);
      sqlStatement.setFloat(3, convertedTimeUpperBound);

      ResultSet result = sqlStatement.executeQuery();
      while (result.next()) {
        listOfTitles.add((result.getString("originaltitle")));
      }
    } catch (Exception e){
      System.err.println(e.getMessage());
      disconnect(conn);
      return Collections.emptyList();
    }
    disconnect(conn);
    return listOfTitles;
  }

/**
   * 
   * @param timeLowerBound - MM-dd-yyyy format
   * @param timeUpperBound - MM-dd-yyyy format
   * @return List of top ten titles
   */
  public List<String> getTopTenTitles(String timeLowerBound, String timeUpperBound) {

    Connection conn = connect();

    int convertedTimeLowerBound = convertTimeToFloat(timeLowerBound);
    int convertedTimeUpperBound = convertTimeToFloat(timeUpperBound);
    
    List<String> listOfTitles = new LinkedList<String>();
    try {
      PreparedStatement sqlStatement = conn.prepareStatement(
        "SELECT originaltitle FROM ratings, titles " +
        "WHERE movieid = titleid AND " +
        "date >= ? AND " +
        "date <= ? " +
        "GROUP BY originaltitle " +
        "ORDER BY MIN(CAST(numvotes AS NUMERIC)) DESC " +
        "LIMIT 10");
      sqlStatement.setFloat(1, convertedTimeLowerBound);
      sqlStatement.setFloat(2, convertedTimeUpperBound);

      ResultSet result = sqlStatement.executeQuery();
      while (result.next()) {
        listOfTitles.add((result.getString("originaltitle")));
      }
    } catch (Exception e){
      System.err.println(e.getMessage());
      disconnect(conn);

      return Collections.emptyList();
    }
    disconnect(conn);
    return listOfTitles;
  }

  private Connection connect() {
    dbSetup my = new dbSetup();
    Connection conn = null;

    try {
      conn = DriverManager.getConnection(
        "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315_908_2db",
          my.user, my.pswd);
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
    }
    System.out.println("Opened database successfully");

    return conn;
  }

  private boolean disconnect(Connection conn) {
    try {
      conn.close();
      return true;
    } catch(Exception e) {
      return false;
    }
  }

  private static int convertTimeToFloat(String time) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
      Date dt = sdf.parse(time);
      long epoch = dt.getTime();
      return (int)(epoch/1000);
    } catch (Exception e) {
      System.err.println(e.getMessage());
      return -1;
    }
  }
}


