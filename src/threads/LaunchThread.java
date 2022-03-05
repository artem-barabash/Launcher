package threads;

import gui_forms.App;

import java.io.IOException;
import java.text.ParseException;

public class LaunchThread implements Runnable{
    Thread t;

    public LaunchThread(){
        t = new Thread(this, "Launcher");
        t.start();
    }

    @Override
    public void run() {
        try{
            for (int i = 3; i > 0; i--){
                App.addItem(String.valueOf(i));
                //System.out.println(i);
                Thread.sleep(500);
            }
            App.changePictureForFlight("launch.jpg");
            Thread.sleep(1500);
        }catch (InterruptedException | IOException e){
            e.printStackTrace();
        }
        App.addItem("LAUNCH");
        //System.out.println("Launch");
        try {
            App.changePictureForFlight("flight.jpg");
            App.startMotors();
        } catch (InterruptedException | IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
