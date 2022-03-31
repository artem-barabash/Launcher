package operations;

import model.LauncherRocketModel;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DBHadler {
    Connection connection;
    Statement statement;



    public  void getConnection(){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite::resource:data/launcher.db");
            //Connection connection = DriverManager.getConnection("jdbc:sqlite:launcher.db");

            statement = connection.createStatement();
            //System.out.println("Connected");

        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            try {
                if (connection != null){
                    connection.close();
                }
            }catch (SQLException ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    //1. Добавляем полет в базу - таблица flights
    public  void methodInsertFlightInDB(int numberFlight, String modelRocket) throws SQLException {
        getConnection();
        String queryInsert = "INSERT INTO flights VALUES ( " + numberFlight + ", '" + modelRocket + "' )";
        statement.executeUpdate(queryInsert);
    }

    //1.1Проверка указного номера на уникальность в бд
    public boolean  methodCheckFlightInDB(int currentNumber) throws SQLException {
        getConnection();
        String querySelect = "SELECT * FROM flights";
        ResultSet resultSet = statement.executeQuery(querySelect);

        ArrayList<Integer> listNumbers = new ArrayList<>();
        while (resultSet.next()){
            listNumbers.add(resultSet.getInt("id"));
        }

        for(Integer num : listNumbers) if(num.equals(currentNumber)) return true;

        return false;
    }

    private static void convertDateTime(String text) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ").parse(text);
        //System.out.println(date);
    }
    //2. Добавить время события в бд
    public void methodInsertFactTime(int numberFlight, EventName eventName) throws SQLException {
        getConnection();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        Date date = new Date();

        String queryInsert = "INSERT INTO date_time_table VALUES ("+ numberFlight + ", '" + simpleDateFormat.format(date) + "', '" +eventName +"' )";
        statement.executeUpdate(queryInsert);
    }

    //3.Вывод на экран архива данных
    public ArrayList<LauncherRocketModel> methodSelectLaunchers() throws SQLException {
        getConnection();

        ArrayList<LauncherRocketModel> launcherRocketModelArrayList = new ArrayList<>();

        String querySelect = "SELECT * FROM flights";
        ResultSet resultSet = statement.executeQuery(querySelect);

        while (resultSet.next()){

            int number = resultSet.getInt("id");
            String model  = resultSet.getString("model");
            LauncherRocketModel modelFlight = new LauncherRocketModel(number,model, null, null,null,0, null, null );
            launcherRocketModelArrayList.add(modelFlight);
        }

        return launcherRocketModelArrayList;
    }

    //тестим
    public void selectAnyThing() throws SQLException, ParseException {
        //SELECT
        getConnection();
        String querySelect = "SELECT * FROM date_time_table";
        ResultSet resultSet = statement.executeQuery(querySelect);

        while (resultSet.next()){
            System.out.println( "ID - " + resultSet.getInt("flight_id") + ", " + "TIME_DATE - " + resultSet.getString("time_date")
                        + ", EVENT - " + resultSet.getString("event") + ".");
                convertDateTime(resultSet.getString("time_date"));
        }
    }

    public static void main(String[] args) throws SQLException, ParseException {

        DBHadler dbHadler = new DBHadler();
        //dbHadler.methodInsertFlightInDB(1, "falcon 9");
        dbHadler.selectAnyThing();

    }


}
