package threads;

import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

import javax.swing.*;

public class Video extends JPanel {
    private static final long serialVersionUID = 1L;
    //private static final String TITLE = "My First Media Player";
    private static final String VIDEO_PATH = "/res/video/example.mp4";
    public static  EmbeddedMediaPlayerComponent mediaPlayerComponent;



    public  Video() {
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
    }


    public void loadVideo(String path) {
        mediaPlayerComponent.mediaPlayer().media().startPaused(path);
    }


    public void initialize(){
        this.setBounds(100, 100, 600, 400);
        this.setVisible(true);

    }

    public static void playVideoMethod(){
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());

        }
        catch (Exception e) {
            System.out.println(e);
        }
        Video application = new Video();
        application.initialize();
        application.setVisible(true);
        application.loadVideo(VIDEO_PATH);
        application.mediaPlayerComponent.mediaPlayer().controls().play();
    }

    public static void main(String[] args) {
        playVideoMethod();
    }

}
