package gui_forms;

import model.CityBase;
import model.DataFlight;
import operations.ChangeOperationOnMonitor;
import operations.LandingCrewAndMachine;
import threads.LaunchThread;
import threads.MotorThread;
import threads.ReturnOnEarthThread;
import threads.Video;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class App extends JFrame {

    static App app;

    public static double quelityConsumption = 5000;//количество топлива RP-1
    public static boolean run = false;

    public final static double fuelConsumption = 50; //100 за cекунду RP-1  одного мотора


    public final static double speed = 3.95; // скорость за секунду одного мотора

    public static double distance = 0;
    public static JFrame frame;

    public static double currentDistance;
    public static double currentQuelityConsumption;
    private  ChangeOperationOnMonitor changeOperationOnMonitor = new ChangeOperationOnMonitor();

    private Video video = new Video();


    public JPanel panelMain;
    private JButton leaveButton;
    private JButton stopButton;
    private JTabbedPane tabbedPane1;
    private JButton launchButton;
    public JLabel labelDistance;
    public JLabel labelConsumption;

    private JPanel centralPanel;


    private JPanel monitorPanel;
    public JList list;

    static JLabel firstPictureOnStart;

    private JPanel consolePanel;
    private JPanel tablePanel;

    static ArrayList<String> listIterations;
    static DefaultListModel listModel;

    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private boolean launchfact = false;
    private static Date now = new Date();
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");

    public App() throws IOException {
        super("App");
        this.setContentPane(this.panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        listIterations = new ArrayList<String>();
        listModel = new DefaultListModel();
        list.setModel(listModel);

        firstPictureOnStart = showRocketFirstly();


        monitorPanel.add(firstPictureOnStart);

        launchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(launchfact == false){
                    if (App.run == false) {
                        App.run = true;
                        launchfact = true;
                        new LaunchThread();
                    } else {
                        JOptionPane.showMessageDialog(null, "The rocket has launched already!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "You can't launch rocket now! You are at space already.");
                }

            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (App.run == false) {
                    JOptionPane.showMessageDialog(null, "The rocket hasn't lanched yet.");
                } else {
                    App.run = false;
                    try {
                        changePictureForFlight("satellite.jpg");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    showDataFlight();

                }
            }
        });
        leaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (App.run == true) {
                    JOptionPane.showMessageDialog(null, "We need firstly stop rocket!");
                } else {
                    CityBase cityBase = LandingCrewAndMachine.searchCityForLanding();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, cityBase.toString());
                    new ReturnOnEarthThread(App.distance, cityBase);

                }
            }
        });

    }

    public static void addItem(String it) {
        //добавляет в коллекцию JList
        listIterations.add(it);


        //метод для обновления списка
        refreshItemsListABC();


        System.out.println("listIterations.size = " + listIterations.size() + ", listModel.size = " + listModel.size());
    }

    public static void refreshItemsListABC() {
        //удаляет все из списка
        listModel.removeAllElements();
        //listModel.clear();

        for (String s : listIterations) {
            //выводит обновленный список, с попередньо добавленыйм элементом
            listModel.addElement(s);
        }
    }

    public  JLabel showRocketFirstly() throws IOException {
        BufferedImage backgroundImage = ImageIO.read(new File("res/image/rocket.jpg"));
        Image scaleBackground = backgroundImage.getScaledInstance(700, screenSize.height / 2, Image.SCALE_SMOOTH);
        Icon imageIcon = new ImageIcon(scaleBackground);

        return new JLabel(imageIcon, 0);
    }

    public static void changePictureForFlight(String pictureOnItem) throws IOException {
        BufferedImage backgroundImage = ImageIO.read(new File("res/image/" + pictureOnItem));
        Image scaleBackground = backgroundImage.getScaledInstance(700, screenSize.height / 2, Image.SCALE_SMOOTH);
        Icon imageIcon = new ImageIcon(scaleBackground);
        firstPictureOnStart.setIcon(imageIcon);
    }


    void showDataFlight() {
        labelDistance.setText("1.Rocket has flied " + currentDistance + " km.");
        labelConsumption.setText("2.The fuel left " + currentQuelityConsumption + " kg.");

        System.out.println("1.Rocket has flied " + currentDistance + " km.");
        System.out.println("2.The fuel left " + currentQuelityConsumption + " kg.");
    }

    private void createUIComponents() throws IOException {


    }

    public static void main(String[] args) throws  IOException {
        app = new App();
        app.setSize(screenSize.width, screenSize.height);
        app.setExtendedState(JFrame.MAXIMIZED_BOTH);
        app.setVisible(true);
        //3.TODO последний этап это добавление через код JPanel с видео
        //4.TODO данные в таблицу SQLite, после каждого запуска. Во второй вкладке tablePanel данные о полетах. Сделать таким образом чтобы БД работала на другом компьютере.Ознакомится с информацией каким образом єто можно граматно сделать. Примеры упакованной Java Swing c БД SQLite
        //5.TODO Дополнить TextReader, перед запуском спрашивать файлик с данными о полете
        //6.TODO Написать Unit Test
    }

    public static void startMotors() throws InterruptedException {
        final BlockingQueue<DataFlight> queue1 = new ArrayBlockingQueue<DataFlight>(100);
        final BlockingQueue<DataFlight> queue2 = new ArrayBlockingQueue<DataFlight>(100);

        final Thread thread1 = new MotorThread(queue1);
        thread1.start();
        final Thread thread2 = new MotorThread(queue2);
        thread2.start();




        ///1.TODO java.lang.ArrayIndexOutOfBoundsException некорректно работает очередь при добавлении в коллекцию
        while (true) {
            //сумма  дистанции первого и второго двигателя
            currentDistance = queue1.take().distance + queue2.take().distance;
            //сумма расхода горючего первого и второго двигателя
            currentQuelityConsumption = queue1.take().quelityConsumption + queue2.take().quelityConsumption;
            //добаляем в JList на JPanel
            app.addItem(simpleDateFormat.format(now) + " - Rocket has flied " + String.format("%.2f", currentDistance) + " km. already.\nThe fuel left " + String.format("%.2f", currentQuelityConsumption) + " kg. yet.");
        }

    }


}

