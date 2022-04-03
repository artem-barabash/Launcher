package gui_forms;

import model.DateEvent;
import model.LauncherRocketModel;
import model.crewmemebers.CrewMember;
import operations.DBHadler;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Archive extends JFrame {
    JLabel labelFlight = new JLabel();
    DBHadler dbHadler = new DBHadler();

    ///текущий полет

    public Archive() throws SQLException {
        setBounds(100, 100, 700, 500);
        Container container = getContentPane();
        container.setLayout(new GridLayout());

        ArrayList<LauncherRocketModel> launcherRocketModelArrayList = dbHadler.methodSelectLaunchers();

        //удаляем из коллекции текущий полет(элемент), т.к он делает ошибку
        launcherRocketModelArrayList.removeIf(n -> n.getNumberFlight() == App.numberFlight);

        JList jListFlights = new JList(launcherRocketModelArrayList.toArray());

        jListFlights.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof LauncherRocketModel) {
                       ((JLabel) renderer).setText(String.valueOf(((LauncherRocketModel) value).getNumberFlight()));

                }
                return renderer;
            }
        });


        JScrollPane jcpList = new JScrollPane(jListFlights);
        container.add(jcpList);


        JPanel jPanel = new JPanel();
        jPanel.add(labelFlight);
        JScrollPane jcpLabel = new JScrollPane(jPanel);


        //labelFlight.setBounds(0, 0, 200, 200);
        container.add(jcpLabel);

        jListFlights.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                LauncherRocketModel selectedFlight = (LauncherRocketModel) jListFlights.getSelectedValue();
                try {
                    labelFlight.setText(methodShowDataLaunchThoughHTML(selectedFlight));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private String methodShowDataLaunchThoughHTML(LauncherRocketModel launcherRocketModel) throws SQLException, ParseException {
        //TODO в jar файле отображаются номера добавленых полетов. Описание полета, можно просмотреть, только изначально зарезанных данных.
        // Те которые были добавлены через jar файл отображают только номер полета. А данные полета не отображаются.
        //stringbuild для того чтобы собрать целую строку со всеми парметрами LauncherModel
        StringBuilder sb = new StringBuilder();
        //подача на вывод в html
        sb.append("Number - " + launcherRocketModel.getNumberFlight() + ".\n" );
        sb.append("Model rocket - " + launcherRocketModel.getModelRocket() + ".\n" );

        //время и события //date_time_table
        //запрос в БД за коллекцией DataEvent
        launcherRocketModel.setListEventDates(dbHadler.selectDateEvents(launcherRocketModel.getNumberFlight()));
        //вывод списка DataEvent
        sb.append("Time intervals: <ul>" + methodAllocatedDateEventToRows(launcherRocketModel.getListEventDates()) + "</ul>");

        //город и страна, базы запуска, и место приземления //table - place
        launcherRocketModel.setCityBaseTakeOff(dbHadler.selectCityBase(launcherRocketModel.getNumberFlight()));
        launcherRocketModel.setCityBaseLandingSite(dbHadler.selectCityBaseLanding(launcherRocketModel.getNumberFlight()));
        //вывод в форму
        sb.append("<p>Rocket launch site is " + launcherRocketModel.getCityBaseTakeOff().city + " - " + launcherRocketModel.getCityBaseTakeOff().country + ".</p>\n");
        sb.append("<p>Landing site is " + launcherRocketModel.getCityBaseLandingSite().city + " - " + launcherRocketModel.getCityBaseLandingSite().country + ".</p>\n");
        sb.append("\n");
        //расстояние и расход топлива //table - distance
        sb.append("<i>" + dbHadler.selectDataAboutDistanceAndFuelConsumption(launcherRocketModel.getNumberFlight()) +  "</i>");
        sb.append("\n");

            //список экипажа с должностями //table -crew_members
        launcherRocketModel.setCrewMembers(dbHadler.selectArrayListWithCrewMember(launcherRocketModel.getNumberFlight()));
        sb.append(methodShowCrewMembersList(launcherRocketModel.getCrewMembers()));

        return "<html><pre>" + sb.toString() +  "</pre></html>";
    }

    //вставка <li></li>
    private String methodAllocatedDateEventToRows(List<DateEvent> listEventDates) {
        String line = "";
        for(DateEvent dateEvent : listEventDates) line+= "<li>" + dateEvent.getEvent() + " - " + dateEvent.getDateEvent() + ".</li>";
        return line;
    }

    private String methodShowCrewMembersList(ArrayList<CrewMember> crewMembers) {
        StringBuilder stringBuilder = new StringBuilder();

        String caption = "<h2>CREW LIST</h2>";

        for(CrewMember crewMember : crewMembers) {
            stringBuilder.append("<p>" + crewMember.getNumber() + ". " + crewMember.getPosition()
                    + " - " + crewMember.getName() + " " + crewMember.getSurname() + ".</p>");
        }

        return "<div>" + caption + stringBuilder.toString() + "</div>";
    }

   public static void main(String[] args) throws SQLException {
        Archive archive = new Archive();

        archive.setSize(700, 500);
        archive.setResizable(false);
        archive.setVisible(true);
    }
}
