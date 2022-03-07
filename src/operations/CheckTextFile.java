package operations;

import javax.swing.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckTextFile {
    boolean indicatorAll = false;

    public CheckTextFile(String textFile){
        methodCheckCountWords(textFile);
        methodCheckCountSentences(textFile);
        methodCheckParagraghInText(textFile);
        methodCheckCaptions(textFile);
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

        indicatorAll = countSentence >= 12 && countSentence <= 17 ? true :  false;

         if(!indicatorAll){
             JOptionPane.showMessageDialog(null, "The text must have at least 12 sentences! You have to put dots in sentences.");
         }
    }

    // 3)к-ство абзацев
    void methodCheckParagraghInText(String str){
        char[] currentTextChar = str.toCharArray();
        int countParagragh = 0;

        for(int i = 0; i < currentTextChar.length; i++){
            if(currentTextChar[i] == '.' || currentTextChar[i] == ':'){


                if(currentTextChar[i + 1] == '\n'){
                    countParagragh++;
                }
            }
        }

        indicatorAll = countParagragh >= 10 && countParagragh <= 15 ? true : false;

        if(!indicatorAll){
            JOptionPane.showMessageDialog(null, "There must be at least 10 paragraphs in the text!");
        }
    }

    // 4)Заголовки The order  и voucher
    void methodCheckCaptions(String str){
        //алгоритм поиска

        String [] textInStringArray = str.split(" ");

        String wordTheOrder = "Order";
        String wordTheVoucher = "Voucher";

        int numberFlight = 0;

        System.out.println(Arrays.toString(textInStringArray));

        String currentElement;

        Pattern pat = Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
        Matcher matcher;

        for(int i = 0; i < textInStringArray.length; i++){

            if(wordTheOrder.equals(textInStringArray[i])){

                if("The".equals(textInStringArray[i - 1]) &&  textInStringArray[i + 1].contains("№")){
                    currentElement = textInStringArray[i + 1];
                    numberFlight = parseIntegerFromText(currentElement);
                }
            }
        }

        System.out.println("Number flight -" + numberFlight);


    }


    //вспомогательные методы
    // поиск String
    static boolean searchInStringArray(String element, String arr[]){
        for(String item : arr){
           if(item.contains(element)) return true;
        }

        return false;
    }

    static int parseIntegerFromText(String text){
        StringBuffer num = new StringBuffer();

        for (int i=0; i<text.length(); i++)
        {
            if (Character.isDigit(text.charAt(i)))
                num.append(text.charAt(i));
        }

        return  Integer.parseInt(String.valueOf(num));
    }

    static int searchWordAndReturnIndex(String element, String arr[]){


        return -1;
    }
}
