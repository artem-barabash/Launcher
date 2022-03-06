package operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class TextFileReader extends ArrayList<String> {
    public static String read(String filePath){
        StringBuilder sb = new StringBuilder();
        try{
            BufferedReader in = new BufferedReader(new FileReader(new File(filePath).getAbsoluteFile()));
            try{
                String s;
                while((s = in.readLine()) != null){
                    sb.append(s);
                    sb.append("\n");
                }
            } finally{
                in.close();
            }
        }catch (IOException  e){
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    public static void main(String[] args) {


        String str = read("F:\\java-работы\\text.txt");

        System.out.println(str);

        String versionWord = "version";

        int indexWordStart = str.indexOf(versionWord);
        int indexWordEnd = indexWordStart + versionWord.length();




        StringBuffer stringBuffer = new StringBuffer(str);

        stringBuffer.delete(indexWordStart, indexWordEnd);

        stringBuffer.insert(indexWordStart, "версія");

        System.out.println("New ");
        System.out.println("---------------------------------------");
        System.out.println(stringBuffer.toString());


        System.out.println("-123".matches("-?\\d+"));       // да
        System.out.println("123".matches("-?\\d+"));        // да
        System.out.println("+123".matches("-?\\d+"));       // нет
        System.out.println("+123".matches("(-|\\+)?\\d+"));

        String s = "This is a sample sentence.";
        String[] words = s.split(" ");
        for (int i = 0; i < words.length; i++) {
            // You may want to check for a non-word character before blindly
            // performing a replacement
            // It may also be necessary to adjust the character class
            //words[i] = words[i].replaceAll("[^\\w]", "");
            System.out.println(words[i] + "\n");

        }
        System.out.print(Arrays.toString(words));



    }



}
