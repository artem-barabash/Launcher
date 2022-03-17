package operations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBHadler {

    public static void getConnection(){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:launcher.db");
            //System.out.println("Connected");
            Statement statement = connection.createStatement();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
            Date date = new Date();

            String queryInsert = "INSERT INTO date_time_table VALUES (0, '" + simpleDateFormat.format(date) + "', 'NEW DAY' )";
            //statement.executeUpdate(query);
            //System.out.println("Rows added.");

            String querySelect = "SELECT * FROM date_time_table";
            ResultSet resultSet = statement.executeQuery(querySelect);

            while (resultSet.next()){
                System.out.println("ID - " + resultSet.getInt("flight_id") + ", " + "TIME_DATE - " + resultSet.getString("time_date")
                        + ", EVENT - " + resultSet.getString("event") + ".");

                convertDateTime(resultSet.getString("time_date"));
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static void convertDateTime(String text) throws ParseException {

        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ").parse(text);
        System.out.println(date);
    }



    public static void main(String[] args) {
        getConnection();
    }
}
