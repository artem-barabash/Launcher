package gui_forms;

import operations.TextReader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class TextForm extends JFrame {

    public static TextForm textForm;


    private JPanel panelMain;
    private JTextArea textArea;
    private JButton enterButton;
    private JButton cancelButton;
    private JLabel resultLabel;

    public TextForm() throws IOException {
        super("Launcher");
        this.setContentPane(this.panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();



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

                    try {
                        textReader.runTextReader(chooser.getSelectedFile().getAbsolutePath());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    textForm.dispose();

                }else {
                    JOptionPane.showMessageDialog(textForm, "Please, choose file.");
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

    public String  showExampleTextFromResource(String nameTextFile) throws IOException {
        StringBuilder resultSB = new StringBuilder();

        try (
                InputStream inputStream = TextForm.class.getResourceAsStream(nameTextFile);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                Stream<String> lines = bufferedReader.lines();
        ) {
            lines.forEach(x -> resultSB.append(x + "\n"));
        }


        return resultSB.toString();
    }

    public static void main(String[] args) throws IOException {


        textForm = new TextForm();

        textForm.setSize(700, 700);
        //fixed sizes
        textForm.setResizable(false);
        textForm.setVisible(true);

        //TODO 2. Проверка тестового файла не корректная

        textForm.textArea.setText(textForm.showExampleTextFromResource("/text/example-act.txt"));
    }
}
