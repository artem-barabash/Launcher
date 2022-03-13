package operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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






    }



}
