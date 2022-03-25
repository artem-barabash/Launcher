package threads;

import gui_forms.App;
import model.CityBaseLandingSite;

import javax.swing.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReturnOnEarthThread implements Runnable{
    Thread t;

    double distance;
    CityBaseLandingSite base;

    public ReturnOnEarthThread(double distanceToEarth, CityBaseLandingSite baseOnEarth){
        t = new Thread(this, "tugboat");

        distance = distanceToEarth;
        base = baseOnEarth;

        t.start();
    }

    @Override
    public void run() {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");

        App.run = true;
        try {
            App.changePictureForFlight("return.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (App.run) {

            distance -= App.speed;
            App.quelityConsumption -= App.fuelConsumption;


            if(distance <= 15){
                App.run = false;
                try {
                    App.changePictureForFlight("landed.jpg");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }

            App.addItem(simpleDateFormat.format(now)+  " - The tugboat has flied " + String.format("%.2f",distance ) +  " km. already.\nThe fuel left " +  String.format("%.2f", App.quelityConsumption) + " kg. yet.");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
        new ParachuteFlightThread().run();

    }

    class ParachuteFlightThread implements Runnable{
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");

        @Override
        public void run() {
            JOptionPane.showMessageDialog(null, "My congratulations! Your flight at space passed successful. The city of " + base.city + " will welcome your command.");

            while (distance > 3.95){
                distance -= App.speed;
                App.addItem(simpleDateFormat.format(now)+  " - The parachute has flied " + String.format("%.2f",distance ) +  " km. already.\nThe fuel left " +  String.format("%.2f", App.quelityConsumption) + " kg. yet.");
            }

            JOptionPane.showMessageDialog(null, "The machine landed.");
        }
    }
}
