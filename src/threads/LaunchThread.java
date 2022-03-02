package threads;

import gui_forms.App;

import java.io.IOException;

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
                Thread.sleep(500);
            }
            App.changePictureForFlight("launch.jpg");
            Thread.sleep(1500);
        }catch (InterruptedException | IOException e){
            e.printStackTrace();
        }
        App.addItem("LAUNCH");
        try {
            App.changePictureForFlight("flight.jpg");
            App.startMotors();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
