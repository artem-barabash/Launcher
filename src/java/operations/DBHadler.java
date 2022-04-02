package operations;

import model.CityBase;
import model.CityBaseLandingSite;
import model.DateEvent;
import model.LauncherRocketModel;
import model.crewmemebers.CrewMember;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHadler {
    Connection connection;
    Statement statement;

    public  void getConnection(){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite::resource:data/launcher.db");
            //Connection connection = DriverManager.getConnection("jdbc:sqlite:launcher.db");

            statement = connection.createStatement();
            ///System.out.println("Connected");

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

    //INSERT

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
    //2. Добавить время события в бд
    public void methodInsertFactTime(int numberFlight, EventName eventName) throws SQLException {
        getConnection();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        Date date = new Date();

        String queryInsert = "INSERT INTO date_time_table VALUES ("+ numberFlight + ", '" + simpleDateFormat.format(date) + "', '" +eventName +"' )";
        statement.executeUpdate(queryInsert);
    }
    //3.Добавить город запуска/приземления
    public void methodInsertCityBase(int numberFlight, CityBase cityBaseTakeOff, CityBaseLandingSite cityBaseLandingSite) throws SQLException {
        getConnection();
        String queryInsert = "INSERT INTO place VALUES ("+ numberFlight + ", '" + cityBaseTakeOff.getCity() + "', '" + cityBaseTakeOff.getCountry()  +"', '"
                + cityBaseLandingSite.getCity() + "', '" + cityBaseLandingSite.getCountry()  + "')";
        statement.executeUpdate(queryInsert);
    }

    //4. километраж и расход топлива
    public void methodInsertDistanceAndQuelity(int numberFlight, double kilometersThere, double kilometersBack,
                                               double quelityConsumption, double spentConsumption) throws SQLException {
        getConnection();
        String queryInsert = "INSERT INTO distance VALUES ("+ numberFlight + ", " + kilometersThere + ", " +
                kilometersBack + ", " + quelityConsumption + ", " + spentConsumption + ")";
        statement.executeUpdate(queryInsert);
    }

    //5. Список экипажа
    public  void insertArrayListWithCrewMembers(int numberFlight, ArrayList<CrewMember> crewMemberArrayList) throws SQLException {
        getConnection();

        for (CrewMember crewMember : crewMemberArrayList){
            String queryInsert = "INSERT INTO crew_members VALUES(" + numberFlight +  ", " + crewMember.getNumber() +  ", '" + crewMember.getSurname() + "', '" +
                     crewMember.getName() +  "', '" + crewMember.getPosition() + "')";
            statement.executeUpdate(queryInsert);
        }
    }

    //SELECT

    //1.Вывод на экран архива данных
    public ArrayList<LauncherRocketModel> methodSelectLaunchers() throws SQLException {
        getConnection();

        ArrayList<LauncherRocketModel> launcherRocketModelArrayList = new ArrayList<>();

        String querySelect = "SELECT * FROM flights";
        ResultSet resultSet = statement.executeQuery(querySelect);

        while (resultSet.next()){

            int number = resultSet.getInt("id");
            String model  = resultSet.getString("model");
            LauncherRocketModel modelFlight = new LauncherRocketModel(number,model, null, null,0,null, null );
            launcherRocketModelArrayList.add(modelFlight);
        }

        return launcherRocketModelArrayList;
    }
    //2.Вывод спика событий полета для конкретного номера
    public List<DateEvent> selectDateEvents(int numberFlight) throws SQLException, ParseException {
        getConnection();
        String querySelect = "SELECT * FROM date_time_table WHERE flight_id = " + numberFlight;
        ResultSet resultSet = statement.executeQuery(querySelect);
        List<DateEvent> listDataEvent = new ArrayList<>();

        while (resultSet.next()){
            listDataEvent.add(new DateEvent(resultSet.getInt("flight_id"),  resultSet.getString("time_date"),
                    resultSet.getString("event")));
        }
        return listDataEvent;
    }
    //3.Вывод места запуска
    public CityBase selectCityBase(int numberFlight) throws SQLException, ParseException {
        getConnection();
        String querySelect = "SELECT * FROM place WHERE flight_id = " + numberFlight;
        ResultSet resultSet = statement.executeQuery(querySelect);

        while (resultSet.next()){
            CityBase cityBase = new CityBase( resultSet.getString("launch_city"),
                    resultSet.getString("launch_country"));

            return cityBase;
        }
        resultSet.close();

        return null;
    }
    //3.1 Вывод места приземления
    public CityBaseLandingSite selectCityBaseLanding(int numberFlight) throws SQLException, ParseException {
        getConnection();
        CityBaseLandingSite cityBaseLandingSite = null;

        String querySelect = "SELECT receiver_city, receiver_country FROM place WHERE flight_id = " + numberFlight;
        ResultSet resultSet = statement.executeQuery(querySelect);

        while (resultSet.next()){
            cityBaseLandingSite = new CityBaseLandingSite( resultSet.getString("receiver_city"),
                    resultSet.getString("receiver_country"), true);
        }

        return cityBaseLandingSite;
    }

    //4 Вывод данных о дистанции и расходе топлива
    public String selectDataAboutDistanceAndFuelConsumption(int numberFlight) throws SQLException, ParseException {
        getConnection();

        double there_kilometers = 0;
        double back_kilometers = 0;
        double fuel_quantity = 0;
        double spent_consumption = 0;

        String querySelect = "SELECT * FROM distance WHERE flight_id = " + numberFlight;
        ResultSet resultSet = statement.executeQuery(querySelect);

        while (resultSet.next()){
            there_kilometers = resultSet.getDouble("there");
            back_kilometers = resultSet.getDouble("back");
            fuel_quantity = resultSet.getDouble("quelity");
            spent_consumption = resultSet.getDouble("spent_consumption");
        }

        resultSet.close();

        String line = "<p>The starting amount of combustible - " + fuel_quantity + " kg.</p>\n" +
                "<p>The rocket flew " + String.format("%.2f",there_kilometers) +" km. to the orbit.</p>\n" +
                "<p>Tugging distance - "+ String.format("%.2f",back_kilometers) + " km.</p>\n" +
                "<p>Total amount of fuel used - " + spent_consumption + " kg.</p>\n";
        return line;
    }

    public ArrayList<CrewMember> selectArrayListWithCrewMember(int numberFlight) throws SQLException {
        getConnection();
        ArrayList<CrewMember> crewMembers = new ArrayList<>();

        String querySelect = "SELECT * FROM crew_members WHERE flight_id = " + numberFlight;
        ResultSet resultSet = statement.executeQuery(querySelect);

        while (resultSet.next()){
            crewMembers.add(new CrewMember(resultSet.getInt("person_id"), resultSet.getString("surname"),
                    resultSet.getString("name"), resultSet.getString("position")));
        }
        resultSet.close();

        return crewMembers;
    }

    public static void main(String[] args) {

    }

}
