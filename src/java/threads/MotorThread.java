package threads;

import gui_forms.App;
import model.DataFlight;

import javax.swing.*;
import java.io.IOException;
import java.util.Queue;

public class MotorThread extends Thread{
    private final Queue<DataFlight> queue;

    public MotorThread (Queue<DataFlight> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {


        while (App.run) {
                App.distance += App.speed;
                App.quelityConsumption -= App.fuelConsumption;


                queue.add(new DataFlight(App.distance, App.quelityConsumption));

                if (App.quelityConsumption <= 2500) {
                    App.run = false;
                    JOptionPane.showMessageDialog(null, "Rocket has stopped, because we have only 5000 kg. consumption");
                    try {
                        App.changePictureForFlight("satellite.jpg");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
