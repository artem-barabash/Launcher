package operations;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckTextFile {
    boolean indicatorAll = false;

    public CheckTextFile(String textFile){
        methodCheckCountWords(textFile);
        methodCheckCountSentences(textFile);
    }



    // 1)к-ство слов
     void methodCheckCountWords(String str) {
        String[] currentTextDoc = str.split(" ");

        if(currentTextDoc.length > 100 && currentTextDoc.length < 200){
            indicatorAll = true;
        }else {
            JOptionPane.showMessageDialog(null, "The text must have at least 100, and not more than 200 words!");
            indicatorAll = false;
        }
    }

    //2)к-ств предложений
     void methodCheckCountSentences(String str) {
         char[] currentTextChar = str.toCharArray();
         int countSentence = 0;

         String sentence = "";

         Pattern pat = Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
         Matcher matcher;

         int currentIndexChar = 0;

         for (int i = 0; i < currentTextChar.length; i++) {

             matcher = pat.matcher(String.valueOf(currentTextChar[i]));


             if (matcher.find()) {
                 if (currentTextChar[i + 1] == '.') {
                     countSentence--;
                     currentIndexChar = i + 1;
                 }

                 if(currentTextChar[currentIndexChar + 1] == '\n'){
                     countSentence++;

                 }
                 currentIndexChar = 0;

             }else {
                 if (currentTextChar[i] == '.' || currentTextChar[i] == ':') {
                     countSentence++;
                 }
             }
         }


        //System.out.println(Arrays.toString(currentTextChar));
        //System.out.println("Count sentence = " + countSentence);

        indicatorAll = countSentence >= 12 ? true :  false;
    }
}
