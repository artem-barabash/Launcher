package gui_forms;

import operations.TextReader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class TextForm extends JFrame {

    static TextForm textForm = new TextForm();
    private JPanel panelMain;
    private JTextArea textArea;
    private JButton enterButton;
    private JButton cancelButton;
    private JLabel resultLabel;


    App app ;
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public TextForm(){
        super("Launcher");
        this.setContentPane(this.panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        textArea.setText(TextReader.read("res/text/example-act.txt"));

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "TEXT FILES", "txt", "text");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(textForm);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    //System.out.println("You chose to open this file: " +chooser.getSelectedFile().getName());
                    resultLabel.setText(chooser.getSelectedFile().getAbsolutePath());
                    TextReader textReader = new TextReader();

                    textReader.runTextReader(chooser.getSelectedFile().getAbsolutePath());

                    textForm.dispose();

                    /*if(textReader.checkTextFile.isIndicatorAll()){

                        try {
                            app = new App();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        app.setSize(screenSize.width, screenSize.height);
                        app.setExtendedState(JFrame.MAXIMIZED_BOTH);
                        app.setVisible(true);

                        //close window
                        textForm.dispose();

                    }else {
                        JOptionPane.showMessageDialog(null, "Data aren't correctly!");
                    }*/

                }else {
                    JOptionPane.showMessageDialog(null, "Please, choose file.");
                }

            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textForm.dispatchEvent(new WindowEvent(textForm, WindowEvent.WINDOW_CLOSING));
            }
        });
    }



    public static void main(String[] args) {
        textForm.setSize(700, 700);

        //fixed sizes
        textForm.setResizable(false);
        textForm.setVisible(true);
    }
}
