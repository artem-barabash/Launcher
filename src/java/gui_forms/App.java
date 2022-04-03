package gui_forms;

import model.CityBaseLandingSite;
import model.DataFlight;
import model.LauncherRocketModel;
import operations.DBHadler;
import operations.EventName;
import operations.LandingCrewAndMachine;
import threads.LaunchThread;
import threads.MotorThread;
import threads.ReturnOnEarthThread;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class App extends JFrame {

    public static App app;

    public static double quelityConsumption = 0;//количество топлива RP-1 на один мотор
    public static boolean run = false;

    public final static double fuelConsumption = 50; //100 за cекунду RP-1  одного мотора

    public final static double speed = 3.95; // скорость за секунду одного мотора

    public static double distance = 0;

    public static double currentDistance;
    public static double currentQuelityConsumption;


    public JPanel panelMain;
    private JButton leaveButton;
    private JButton stopButton;
    private JTabbedPane tabbedPane1;
    private JButton launchButton;
    JLabel labelDistance;
    JLabel labelConsumption;

    private JPanel centralPanel;


    private JPanel monitorPanel;
    public JList list;

    static JLabel firstPictureOnStart;

    private JPanel consolePanel;
    private JPanel tablePanel;
    private JTextArea textAreaCommander;
    private JButton buttonCommander;
    public   JScrollPane scrollPane;
    private JButton archiveButton;

    static ArrayList<String> listIterations;
    static DefaultListModel listModel;

    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();


    //Один раз включил(запустил)
    private boolean launchFact = false;

    //один раз включил возврат на Землю
    private boolean returnFact = false;

    //факт приземления
    public static boolean landingFact = false;

    //дистанция до стопа
    public static double coveredDistance = 0;

    //номер полета фиксирует для класса Archive
    public static int numberFlight = 0;


    public static LauncherRocketModel launcherRocketModel;
    DBHadler dbHadler = new DBHadler();


    public App(LauncherRocketModel launcherRocketModel) throws IOException, SQLException {

        super("Launcher");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setContentPane(this.panelMain);
        this.pack();

        //TODO Запрет на закрытие окна после пуска



        this.launcherRocketModel = launcherRocketModel;
        numberFlight = launcherRocketModel.getNumberFlight();
        //топлива на один двигатель
        quelityConsumption = launcherRocketModel.getQuelityConsumption() / 2;

        listIterations = new ArrayList<String>();
        listModel = new DefaultListModel();
        list.setModel(listModel);

        firstPictureOnStart = showRocketFirstly();

        monitorPanel.add(firstPictureOnStart);


        if(landingFact) this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //обавляем в бд номер полета
        dbHadler.methodInsertFlightInDB(launcherRocketModel.getNumberFlight(), launcherRocketModel.getModelRocket());
        //добавляем в бд весь экипаж
        dbHadler.insertArrayListWithCrewMembers(launcherRocketModel.getNumberFlight(), launcherRocketModel.getCrewMembers());


        /*this.addWindowStateListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {

            }

        });*/

        launchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(launchFact == false){
                    if (App.run == false) {

                        //Фиксация события старт
                        dbHadler.methodInsertFactTime(launcherRocketModel.getNumberFlight(), EventName.LAUNCH);
                        App.run = true;
                        launchFact = true;
                        new LaunchThread();

                    } else {
                        JOptionPane.showMessageDialog(null, "The rocket has launched already!");
                    }
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (App.run == false) {
                    if(launchFact == false){
                        JOptionPane.showMessageDialog(null, "The rocket hasn't lanched yet.");
                    }
                } else {
                    if(returnFact == false){
                        if(currentDistance > 100){
                            App.run = false;
                            try {
                                dbHadler.methodInsertFactTime(launcherRocketModel.getNumberFlight(), EventName.STOP_ON_ORBITE);
                                changePictureForFlight("satellite.jpg");
                                //Фиксация события стоп на орбите
                                coveredDistance = currentDistance;
                                System.out.println("coveredDistance = " + coveredDistance);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }


                            showDataFlight();

                        } else JOptionPane.showMessageDialog(null, "The distance is too short, that we can stop! If distance more 100 km we can stop.");
                    }
                }
            }
        });
        leaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(launchFact == true){
                    if (App.run == true){
                        if(returnFact) JOptionPane.showMessageDialog(null, "We are returning to the Earth now!");
                        else JOptionPane.showMessageDialog(null, "We need firstly stop rocket!");

                    } else {
                        if(returnFact == false){
                            returnFact = true;
                            CityBaseLandingSite cityBaseLandingSite = LandingCrewAndMachine.searchCityForLanding();
                            if(cityBaseLandingSite != null){
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                                JOptionPane.showMessageDialog(null, cityBaseLandingSite.toString());
                                new ReturnOnEarthThread(currentDistance, currentQuelityConsumption, cityBaseLandingSite, launcherRocketModel.getNumberFlight());

                                try {
                                    // Фиксация события начала возрата на Землю
                                    dbHadler.methodInsertFactTime(launcherRocketModel.getNumberFlight(), EventName.LEFT_ON_EARTH);
                                    launcherRocketModel.setCityBaseLandingSite(cityBaseLandingSite);
                                    //вставлем в бд город запуска, и город куда планируем приземлить
                                    dbHadler.methodInsertCityBase(launcherRocketModel.getNumberFlight(), launcherRocketModel.getCityBaseTakeOff(), launcherRocketModel.getCityBaseLandingSite());
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                            } else{
                                returnFact = false;
                                JOptionPane.showMessageDialog(null, "Try, again!");
                            }
                        }
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "This function isn't available now!!");
                }

            }
        });
        buttonCommander.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(textAreaCommander.getText() != ""){
                    // сообщения от командира станции в реальном режиме
                    Date now = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
                    addItem(simpleDateFormat.format(now) + " " + textAreaCommander.getText());
                    textAreaCommander.setText("");
                }
            }
        });

        archiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                Archive archive = null;
                try {
                    archive = new Archive();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                archive.setSize(700, 500);
                archive.setResizable(false);
                archive.setVisible(true);

            }
        });

    }

    public static void addItem(String it) {
        listIterations.add(it);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void> () {
            @Override
            protected Void doInBackground() throws Exception {

                listModel.clear();
                for(String s : listIterations) {
                    listModel.addElement(s);
                }
                return null;
            }
        };

        java.awt.EventQueue.invokeLater(worker);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                listModel.clear();
                for(String s : listIterations) {
                    listModel.addElement(s);
                }
            }
        });
    }
    /*public static void addItem(String it) {
        //добавляет в коллекцию JList
        listIterations.add(it);
        //метод для обновления списка
        refreshItemsListABC();

    }

    public static void refreshItemsListABC() {
        //удаляет все из списка
        listModel.removeAllElements();

        for (String s : listIterations) {
            //выводит обновленный список, с попередньо добавленыйм элементом
            listModel.addElement(s);
        }
    }*/

    public  JLabel showRocketFirstly() throws IOException {
        BufferedImage backgroundImage = ImageIO.read(getClass().getClassLoader().getResource("image/rocket.jpg"));
        Image scaleBackground = backgroundImage.getScaledInstance(700, screenSize.height / 2, Image.SCALE_SMOOTH);
        Icon imageIcon = new ImageIcon(scaleBackground);

        return new JLabel(imageIcon, 0);
    }

    public static void changePictureForFlight(String pictureOnItem) throws IOException {
        BufferedImage backgroundImage = ImageIO.read(App.class.getClassLoader().getResource("image/" + pictureOnItem));
        Image scaleBackground = backgroundImage.getScaledInstance(700, screenSize.height / 2, Image.SCALE_SMOOTH);
        Icon imageIcon = new ImageIcon(scaleBackground);
        firstPictureOnStart.setIcon(imageIcon);
    }

     void showDataFlight() {
        labelDistance.setText("");
        labelConsumption.setText("");

        labelDistance.setText("1.Rocket has flied " + currentDistance + " km.");
        labelConsumption.setText("2.The fuel left " + currentQuelityConsumption + " kg.");
    }

    private void createUIComponents() throws IOException {

    }

    /*public static void main(String[] args) throws  IOException {
        app = new App();
        app.setSize(screenSize.width, screenSize.height);
        app.setExtendedState(JFrame.MAXIMIZED_BOTH);
        app.setVisible(true);
        //3.TODO последний этап это добавление через код JPanel с видео
        //4.TODO данные в таблицу SQLite, после каждого запуска. Во второй вкладке tablePanel данные о полетах. Сделать таким образом чтобы БД работала на другом компьютере.Ознакомится с информацией каким образом єто можно граматно сделать. Примеры упакованной Java Swing c БД SQLite
        //5.TODO Дополнить TextReader, перед запуском спрашивать файлик с данными о полете
        //6.TODO Написать Unit Test
    }*/

    public static void startMotors() throws InterruptedException, ParseException {

        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");

        final BlockingQueue<DataFlight> queue1 = new ArrayBlockingQueue<DataFlight>(100);
        final BlockingQueue<DataFlight> queue2 = new ArrayBlockingQueue<DataFlight>(100);

        final Thread thread1 = new MotorThread(queue1);
        thread1.start();
        final Thread thread2 = new MotorThread(queue2);
        thread2.start();

        while (true) {
            //сумма  дистанции первого и второго двигателя
            currentDistance = queue1.take().distance + queue2.take().distance;
            //сумма расхода горючего первого и второго двигателя
            currentQuelityConsumption = queue1.take().quelityConsumption + queue2.take().quelityConsumption;
            //добаляем в JList на JPanel

            //date instant now
            app.addItem(simpleDateFormat.format(now) + " - Rocket has flied " + String.format("%.2f", currentDistance) + " km. already.\nThe fuel left " + String.format("%.2f", currentQuelityConsumption) + " kg. yet.");
        }

    }
}

