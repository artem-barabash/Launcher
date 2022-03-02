package threads;

import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VideoTest extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String TITLE = "My First Media Player";
    private static final String VIDEO_PATH = "/res/video/example.mp4";
    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private JButton playButton;



    public  VideoTest(String title) {
        super(title);
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
    }



    public void initialize() {
        this.setBounds(100, 100, 600, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release();
                System.exit(0);
            }
        });
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(mediaPlayerComponent, BorderLayout.CENTER);

        /*JPanel controlsPane = new JPanel();
        playButton = new JButton("Play");
        controlsPane.add(playButton);
        contentPane.add(controlsPane, BorderLayout.SOUTH);
        playButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
                mediaPlayerComponent.mediaPlayer().controls().play();
            }
        });
        mediaPlayerComponent.mediaPlayer().controls().play();
        this.setContentPane(contentPane);*/
        this.setVisible(true);
    }
    public void loadVideo(String path) {
        mediaPlayerComponent.mediaPlayer().media().startPaused(path);
    }

    public static void playVideoMethod(){
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());

        }
        catch (Exception e) {
            System.out.println(e);
        }
        VideoTest application = new VideoTest(TITLE);
        application.initialize();
        application.setVisible(true);
        application.loadVideo(VIDEO_PATH);
        application.mediaPlayerComponent.mediaPlayer().controls().play();
    }
    public static void main(String[] args) {
        playVideoMethod();
    }


}
