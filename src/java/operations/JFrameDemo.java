package operations;

import model.CityBaseLandingSite;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;

public class JFrameDemo extends JFrame {

    JLabel label = new JLabel();
    public JFrameDemo(String title) throws HeadlessException
    {
        super(title);
        setBounds(100, 100, 200, 200);
        Container ControlHost = getContentPane();
        ControlHost.setLayout(new FlowLayout());

        ArrayList<CityBaseLandingSite> citiesArrayList = new ArrayList<CityBaseLandingSite>();
        citiesArrayList.add(new CityBaseLandingSite("New York", "USA", true));
        citiesArrayList.add(new CityBaseLandingSite("Kyiv", "Ukraine", true));
        citiesArrayList.add(new CityBaseLandingSite("Tel-Aviv", "Israel", false));

        JList ListCities = new JList(citiesArrayList.toArray());

        ListCities.setVisibleRowCount(4);

        JScrollPane jcp = new JScrollPane(ListCities);
        ControlHost.add(jcp);
        ControlHost.add(label);

        ListCities.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Object SelectedCity =  ListCities.getSelectedValue();
                label.setText(SelectedCity.toString());
            }
        });
    }

    public static void main(String[] args){
        JFrameDemo jFrameDemo = new JFrameDemo("City");
        jFrameDemo.setSize(500, 500);
        //fixed sizes
        jFrameDemo.setResizable(false);
        jFrameDemo.setVisible(true);
    }
}