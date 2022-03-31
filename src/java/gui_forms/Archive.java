package gui_forms;

import model.LauncherRocketModel;
import operations.DBHadler;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class Archive extends JFrame{
    private JPanel panelMain;

    static ArrayList<LauncherRocketModel> launcherRocketModelArrayList;

    private JList listFlights;


    private JLabel labelFlight;



    public Archive() throws SQLException {
        super("Launcher");
        this.setContentPane(this.panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.pack();

        DBHadler dbHadler = new DBHadler();

        launcherRocketModelArrayList = dbHadler.methodSelectLaunchers();
        System.out.println(launcherRocketModelArrayList.size());

        listFlights = new JList(launcherRocketModelArrayList.toArray());

        listFlights.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof LauncherRocketModel) {

                    ((JLabel) renderer).setText(String.valueOf(((LauncherRocketModel) value).getNumberFlight()));
                }
                return renderer;
            }
        });
        //путь правильный простооно не рабоьань

        listFlights.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Object selectedModel =  listFlights.getSelectedValue();
                labelFlight.setText(selectedModel.toString());
            }
        });



    }

    public static void main(String[] args) throws SQLException {
        Archive archive = new Archive();

        archive.setSize(1000, 700);
        //fixed sizes
        archive.setResizable(false);
        archive.setVisible(true);
    }
}
