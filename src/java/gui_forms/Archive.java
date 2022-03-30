package gui_forms;

import operations.DBHadler;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class Archive extends JFrame{
    private JPanel panelMain;

    static ArrayList<Integer> listIterations;
    static DefaultListModel listModel;

    private JList listFlights;


    private JLabel labelFlight;



    public Archive() throws SQLException {
        super("Launcher");
        this.setContentPane(this.panelMain);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.pack();

        DBHadler dbHadler = new DBHadler();

        listIterations = new ArrayList<Integer>();
        String labels[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
        listIterations.addAll(dbHadler.methodSelectLaunchers());


        listModel = new DefaultListModel();
        listFlights.setModel(listModel);

        for (Integer s : listIterations) {
            listModel.addElement(s);
        }
        //TODO вывод данных по модели LauncherModel

        ListSelectionListener listSelectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                System.out.print("First index: " + listSelectionEvent.getFirstIndex());
                System.out.print(", Last index: " + listSelectionEvent.getLastIndex());
                boolean adjust = listSelectionEvent.getValueIsAdjusting();
                System.out.println(", Adjusting? " + adjust);
                if (!adjust) {
                    JList list = (JList) listSelectionEvent.getSource();
                    int selections[] = list.getSelectedIndices();
                    Object selectionValues[] = list.getSelectedValues();
                    for (int i = 0, n = selections.length; i < n; i++) {
                        if (i == 0) {
                            System.out.print("  Selections: ");
                        }
                        System.out.print(selections[i] + "/" + selectionValues[i] + " ");
                    }
                    System.out.println();
                }
            }
        };
        listFlights.addListSelectionListener(listSelectionListener);

        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                JList theList = (JList) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2) {
                    int index = theList.locationToIndex(mouseEvent.getPoint());
                    if (index >= 0) {
                        Object o = theList.getModel().getElementAt(index);
                        System.out.println("Double-clicked on: " + o.toString());
                    }
                }
            }
        };

        listFlights.addMouseListener(mouseListener);
    }

    public static void main(String[] args) throws SQLException {
        Archive archive = new Archive();

        archive.setSize(1000, 700);
        //fixed sizes
        archive.setResizable(false);
        archive.setVisible(true);
    }
}
