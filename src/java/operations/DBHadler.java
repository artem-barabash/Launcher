package operations;

import model.CityBase;
import model.CityBaseLandingSite;
import model.DateEvent;
import model.LauncherRocketModel;
import model.crewmemebers.CrewMember;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHadler {
    Statement statement;

    public  Connection getConnection(){
        //String url = "jdbc:sqlite::resource:data/launcher.db";
        String url = "jdbc:sqlite:launcher.db";

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    //INSERT

    //1. Добавляем полет в базу - таблица flights
    public  void methodInsertFlightInDB(int numberFlight, String modelRocket) throws SQLException {
        String queryInsert = "INSERT INTO flights(id, model) VALUES (?, ?)";

        try (Connection currentConn = this.getConnection();
        PreparedStatement preparedStatement = currentConn.prepareStatement(queryInsert)){
            preparedStatement.setInt(1, numberFlight);
            preparedStatement.setString(2, modelRocket);

            preparedStatement.executeUpdate();

        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    //1.1Проверка указного номера на уникальность в бд
    public boolean  methodCheckFlightInDB(int currentNumber) throws SQLException {
        ArrayList<Integer> listNumbers = new ArrayList<>();

        String querySelect = "SELECT * FROM flights";

        try(Connection currentConn = this.getConnection();
            Statement statement = currentConn.createStatement();
            ResultSet resultSet = statement.executeQuery(querySelect)){

            while (resultSet.next()){
                listNumbers.add(resultSet.getInt("id"));
            }

        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }

        return listNumbers.contains(currentNumber);
    }
    //2. Добавить время события в бд
    public void methodInsertFactTime(int numberFlight, EventName eventName)  {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        Date date = new Date();

        String queryInsert = "INSERT INTO date_time_table(flight_id, time_date, event) VALUES (?, ?, ?)";

        try (Connection currentConn = this.getConnection();
             PreparedStatement preparedStatement = currentConn.prepareStatement(queryInsert)){
            preparedStatement.setInt(1, numberFlight);
            preparedStatement.setString(2, simpleDateFormat.format(date));
            preparedStatement.setString(3, String.valueOf(eventName));

            preparedStatement.executeUpdate();

        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    //3.Добавить город запуска/приземления
    public void methodInsertCityBase(int numberFlight, CityBase cityBaseTakeOff, CityBaseLandingSite cityBaseLandingSite) throws SQLException {
        String queryInsert = "INSERT INTO place(flight_id, launch_city, launch_country, receiver_city, receiver_country) VALUES (?, ?, ?, ?, ?)";

        try (Connection currentConn = this.getConnection();
             PreparedStatement preparedStatement = currentConn.prepareStatement(queryInsert)){
            preparedStatement.setInt(1, numberFlight);
            preparedStatement.setString(2, cityBaseTakeOff.getCity());
            preparedStatement.setString(3, cityBaseTakeOff.getCountry());
            preparedStatement.setString(4, cityBaseLandingSite.getCity());
            preparedStatement.setString(5, cityBaseLandingSite.getCountry());

            preparedStatement.executeUpdate();
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }

    }

    //4. километраж и расход топлива
    public void methodInsertDistanceAndQuelity(int numberFlight, double kilometersThere, double kilometersBack,
                                               double quelityConsumption, double spentConsumption) throws SQLException {
        String queryInsert = "INSERT INTO distance(flight_id, there, back, quelity, spent_consumption) VALUES (?, ?, ?, ?, ?)";

        try (Connection currentConn = this.getConnection();
             PreparedStatement preparedStatement = currentConn.prepareStatement(queryInsert)){
            preparedStatement.setInt(1, numberFlight);
            preparedStatement.setDouble(2, kilometersThere);
            preparedStatement.setDouble(3, kilometersBack);
            preparedStatement.setDouble(4, quelityConsumption);
            preparedStatement.setDouble(5, spentConsumption);

            preparedStatement.executeUpdate();

        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }

    //5. Список экипажа
    public  void insertArrayListWithCrewMembers(int numberFlight, ArrayList<CrewMember> crewMemberArrayList) throws SQLException {

        String queryInsert = "INSERT INTO crew_members(flight_id, person_id, surname, name, position) VALUES(?, ?, ?, ?, ?)";

        try (Connection currentConn = this.getConnection();
             PreparedStatement preparedStatement = currentConn.prepareStatement(queryInsert)){

            for (CrewMember crewMember : crewMemberArrayList){
                preparedStatement.setInt(1, numberFlight);
                preparedStatement.setInt(2, crewMember.getNumber());
                preparedStatement.setString(3, crewMember.getSurname());
                preparedStatement.setString(4, crewMember.getName());
                preparedStatement.setString(5, crewMember.getPosition());

                preparedStatement.executeUpdate();
            }

        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }


    }

    //SELECT

    //1.Вывод на экран архива данных
    public ArrayList<LauncherRocketModel> methodSelectLaunchers() {
        ArrayList<LauncherRocketModel> launcherRocketModelArrayList = new ArrayList<>();

        String querySelect = "SELECT * FROM flights";

        try(Connection currentConn = this.getConnection();
        Statement statement = currentConn.createStatement();
            ResultSet resultSet = statement.executeQuery(querySelect)){

            while (resultSet.next()){

                int number = resultSet.getInt("id");
                String model  = resultSet.getString("model");
                LauncherRocketModel modelFlight = new LauncherRocketModel(number,model, null, null,0,null, null );
                launcherRocketModelArrayList.add(modelFlight);
            }
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }

        return launcherRocketModelArrayList;
    }
    //2.Вывод спика событий полета для конкретного номера
    public List<DateEvent> selectDateEvents(int numberFlight) {
        String querySelect = "SELECT * FROM date_time_table WHERE flight_id = ?";
        List<DateEvent> listDataEvent = new ArrayList<>();

        try(Connection currentConn = this.getConnection();
        PreparedStatement preparedStatement = currentConn.prepareStatement(querySelect)){

            preparedStatement.setInt(1, numberFlight);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                listDataEvent.add(new DateEvent(resultSet.getInt("flight_id"),  resultSet.getString("time_date"),
                        resultSet.getString("event")));
            }

        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }

        return listDataEvent;
    }
    //3.Вывод места запуска
    public CityBase selectCityBase(int numberFlight) {

        String querySelect = "SELECT * FROM place WHERE flight_id = ?";

        CityBase cityBase = null;

        try(Connection currentConn = this.getConnection();
            PreparedStatement preparedStatement = currentConn.prepareStatement(querySelect)){

            preparedStatement.setInt(1, numberFlight);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cityBase = new CityBase(resultSet.getString("launch_city"),
                        resultSet.getString("launch_country"));
                return cityBase;
            }


        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }

        return cityBase;
    }
    //3.1 Вывод места приземления
    public CityBaseLandingSite selectCityBaseLanding(int numberFlight) {

        String querySelect = "SELECT receiver_city, receiver_country FROM place WHERE flight_id = ?";

        CityBaseLandingSite cityBaseLandingSite = null;

        try(Connection currentConn = this.getConnection();
            PreparedStatement preparedStatement = currentConn.prepareStatement(querySelect)){

            preparedStatement.setInt(1, numberFlight);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cityBaseLandingSite = new CityBaseLandingSite(resultSet.getString("receiver_city"),
                        resultSet.getString("receiver_country"), true);

            }
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        return cityBaseLandingSite;
    }

    //4 Вывод данных о дистанции и расходе топлива
    public String selectDataAboutDistanceAndFuelConsumption(int numberFlight){
        double there_kilometers = 0;
        double back_kilometers = 0;
        double fuel_quantity = 0;
        double spent_consumption = 0;

        String querySelect = "SELECT * FROM distance WHERE flight_id = ?";

        try(Connection currentConn = this.getConnection();
        PreparedStatement preparedStatement = currentConn.prepareStatement(querySelect)){

            preparedStatement.setInt(1, numberFlight);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                there_kilometers = resultSet.getDouble("there");
                back_kilometers = resultSet.getDouble("back");
                fuel_quantity = resultSet.getDouble("quelity");
                spent_consumption = resultSet.getDouble("spent_consumption");
            }
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }

        String line = "<p>The starting amount of combustible - " + fuel_quantity + " kg.</p>\n" +
                "<p>The rocket flew " + String.format("%.2f",there_kilometers) +" km. to the orbit.</p>\n" +
                "<p>Tugging distance - "+ String.format("%.2f",back_kilometers) + " km.</p>\n" +
                "<p>Total amount of fuel used - " + spent_consumption + " kg.</p>\n";
        return line;
    }

    //5. Вывод коллекции с экипажем
    public ArrayList<CrewMember> selectArrayListWithCrewMember(int numberFlight) throws SQLException {
        ArrayList<CrewMember> crewMembers = new ArrayList<>();

        String querySelect = "SELECT * FROM crew_members WHERE flight_id = ?";

        try(Connection currentConn = this.getConnection();
            PreparedStatement preparedStatement = currentConn.prepareStatement(querySelect)){

            preparedStatement.setInt(1, numberFlight);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                crewMembers.add(new CrewMember(resultSet.getInt("person_id"), resultSet.getString("surname"),
                        resultSet.getString("name"), resultSet.getString("position")));
            }

        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }

        return crewMembers;
    }

    public static void main(String[] args) {

    }

}
