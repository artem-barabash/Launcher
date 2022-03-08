package operations;

import gui_forms.App;
import model.DateEvent;
import model.LauncherRocketModel;
import model.crewmemebers.*;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextReader {

    static App app;
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    //проверка текста на соотвествие правил оформления
    public  CheckTextFile checkTextFile;


    public TextReader() {
    }

    public CheckTextFile getCheckTextFile() {
        return checkTextFile;
    }

    public void setCheckTextFile(CheckTextFile checkTextFile) {
        this.checkTextFile = checkTextFile;
    }

    public static String read(String filePath) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(new File(filePath).getAbsoluteFile()));
            try {
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    public  void runTextReader(String str) {
        String text = read(str);

        checkTextFile = new CheckTextFile(text);


        if(checkTextFile.indicatorAll){
            //1.Находим в списком экипажа по должностям
            ArrayList<CrewMember> members  = methodToolSearchWordsinTheText(text);
            //2.Находим город запуска ракеты
            String cityBasetakeOff = methodSearchCityBaseInTheText(text);
            //3.Находим номер полета и путевку
            int numberFlight = methodSearhNumberFlightIntheText(text, "order");
            //int numberVoucher = methodSearhNumberFlightIntheText(text, "voucher");

            //4.Находим к-сть топлива
            double quelityConsumption = methodSearchQuelityConsumption(text);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
            //TODO разобраться с simpleDateFormat коректная подача времени
            Date now = new Date();
            simpleDateFormat.format(now);



            List<DateEvent> listEventDates = null;

            LauncherRocketModel launcherRocketModel = new LauncherRocketModel(numberFlight, cityBasetakeOff, null, simpleDateFormat,quelityConsumption, members, listEventDates);

            try {
                app = new App(launcherRocketModel);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            app.setSize(screenSize.width, screenSize.height);
            app.setExtendedState(JFrame.MAXIMIZED_BOTH);
            app.setVisible(true);



            //String [] array = str.split("");
            //System.out.println(Arrays.toString(array));
            /*System.out.println("number Flight = " + numberFlight);
            System.out.println("voucher = " + numberVoucher);
            System.out.println(quelityConsumption);
            System.out.println(cityBasetakeOff);

            String allText = text.toLowerCase();
            String textArray[] = allText.split(" ");
            System.out.println(Arrays.toString(textArray));

            System.out.println(searchElementInArray(0, "voucher", textArray));*/
        }else {
            JOptionPane.showMessageDialog(null, "Data aren't correctly!");
        }



        //TODO  Проверка текстового файла на правильность составления: ключевые слова, абзацы
        //TODO
        // 1)к-ство слов+
        // 2)к-ств предложений+
        // 3)к-ство абзацев+
        // 4)Заголовки The order  и voucher,
        // 5) предложения которые начинаються с ключевых слов.
        // 6) наличее модели ракеты 7. списка экипажа с номерами.
        // 8) число экипажа буквами
        // 9)место запуска
        // 10) должность и звание главнокомандуещеего
        // 11) обьем выделенного горячего.
        // 12) и полсе только ниже прописанная проверка с внесением данных в перемены и об'єкти.


        //System.out.println(CheckTextFile.methodCheckCountWords(str));
        //System.out.println(CheckTextFile.methodCheckCountSentences(str));

    }

    //1.Находим в списком экипажа по должностям
    static ArrayList<CrewMember> methodToolSearchWordsinTheText(String text){

        ArrayList<CrewMember> list = new ArrayList<>();
        String listCrewMembersPositions[] = {"Commander", "Engineer", "Gunner", "Doctor", "Tourist"};
        int numberMember = 0;

        char[] strToCharArray = text.toCharArray();
        String currentTextDoc = text.toLowerCase();

        int indexWordStart = 0;

        int indexWordEnd = currentTextDoc.indexOf("The commander".toLowerCase());

        for(String position : listCrewMembersPositions){
            int currentWordIndex = 0;

            String sentance = "";


            indexWordStart = currentTextDoc.indexOf(position.toLowerCase());

            for(int j = indexWordStart; j < strToCharArray.length; j++) {
                if(strToCharArray[j] != '.'){
                    sentance += Character.toString(strToCharArray[j]);

                }else {
                    break;
                }

            }
            ++numberMember;
            list.add(searchDataPersonInSentance(numberMember, sentance));

            currentWordIndex = currentTextDoc.indexOf(position.toLowerCase(), indexWordStart + 1);


            if(currentWordIndex != -1){

                if(currentWordIndex < indexWordEnd){
                    sentance = "";
                    for (int j = currentWordIndex; j < strToCharArray.length; j++) {
                        if (strToCharArray[j] != '.') {
                            sentance += Character.toString(strToCharArray[j]);

                        } else {
                            break;
                        }
                    }
                    ++numberMember;
                    list.add(searchDataPersonInSentance(numberMember,sentance));
                }
            }
        }

        return list;
    }
    //1.1. Создание обьекта на основе полученных данных
    private static CrewMember searchDataPersonInSentance(int numberMember, String textSentence){

        String arrayPerson[] = textSentence.split(" ");
        String position = arrayPerson[0];
        String name = "";
        int index = 1;

        for(int i = 0 ; i < arrayPerson.length; i++){
            if((!methodCheckTextСharacters(arrayPerson[index]) && methodCheckTextStrings(arrayPerson[index]))){
                name = arrayPerson[index];
            }else {
                name = arrayPerson[++index];
            }
        }
        String surname = arrayPerson[index + 1];

        CrewMember member = createMemberForHisActivity(new CrewMember(numberMember, name, surname, position));

        return member;
    }

    //1.2 Проверка на наличение символа
    private static boolean methodCheckTextСharacters(String s) {
        String specialCharactersString = "!@#$%&*()'+,-./:;<=>?[]^_`{|}";
        boolean result = false;

        for(int i = 0; i < s.length(); i++){
            char ch = s.charAt(i);
            if(specialCharactersString.contains(Character.toString(ch))){
                result = true;
                break;
            }else {
                result = false;
            }
        }
        return result;
    }

    //1.3 Проверка на наличие букв в элементе
    private static boolean methodCheckTextStrings(String s) {
        Pattern pattern = Pattern.compile("[a-zA-Z]");
        Matcher matcher = pattern.matcher(s);
        boolean result = matcher.find();

        return result;
    }

    //1.4 присваеваем данные обьекту, с созданием экземпляра класса взависимости от должности
    static CrewMember createMemberForHisActivity(CrewMember crewMemberSample){

        if(crewMemberSample.getPosition() == "Commander"){
            crewMemberSample = new Commander(crewMemberSample.getNumber(), crewMemberSample.getName(), crewMemberSample.getSurname(), crewMemberSample.getPosition());
        } else if(crewMemberSample.getPosition() == "Engineer"){
            crewMemberSample = new Engineer(crewMemberSample.getNumber(), crewMemberSample.getName(), crewMemberSample.getSurname(), crewMemberSample.getPosition());
        } else if(crewMemberSample.getPosition() == "Gunner"){
            crewMemberSample = new Gunner(crewMemberSample.getNumber(), crewMemberSample.getName(), crewMemberSample.getSurname(), crewMemberSample.getPosition());
        }else if(crewMemberSample.getPosition() == "Doctor"){
            crewMemberSample = new Doctor(crewMemberSample.getNumber(), crewMemberSample.getName(), crewMemberSample.getSurname(), crewMemberSample.getPosition());
        }else if(crewMemberSample.getPosition() == "Tourist"){
            crewMemberSample  = new Tourist(crewMemberSample.getNumber(), crewMemberSample.getName(), crewMemberSample.getSurname(), crewMemberSample.getPosition());
        }

        return crewMemberSample;
    }

    //2.Находим город запуска ракеты
    private static String methodSearchCityBaseInTheText(String str) {
        String allText = str.toLowerCase();
        String textArray[] = allText.split(" ");

        int indexLaunch = searchElementInArray(0, "launch", textArray);
        String sentence = "";


        for(int i = indexLaunch; i < textArray.length; i++){


            if(searchCharacterInStringIteration("\\.", textArray[i])){
                sentence += textArray[i] + " ";
                break;
            }else {
                sentence += textArray[i] + " ";
            }
        }

        return "Launch place: " + parseCityFromSentence(sentence) + " - " + parseCountryFromSentence(sentence);
    }

    //2.1 индекс слова "launch" в предложении где речь идет о метсе запуска
    //TODO другой алгоритм(более быстрый)
    private static int searchElementInArray(int indexStart, String element, String[] arr){
        int index = 0;

        for(int i = indexStart; i < arr.length; i++){

            if(arr[i].equals(element)){
                index = i;
            }
        }

        return index;
    }

    //2.2 Поиск конкретного символа в строке
    private static boolean searchCharacterInStringIteration(String regex, String str){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        boolean result = matcher.find();

        return result;
    }

    //2.3 Взять город из розпарсенной строки
    private static String parseCityFromSentence(String sentence) {
        int  startCity = sentence.indexOf("is");
        int endCityComma = sentence.indexOf(",");

        StringBuilder stringBuilder = new StringBuilder(sentence);

        StringBuilder currentResult = new StringBuilder(stringBuilder.substring(startCity, endCityComma));
        currentResult.delete(0, 2);

        String result = new String(currentResult).trim();

        return result.substring(0, 1).toUpperCase() +   result.substring(1);
    }

    //2.4 Взять страну из розпарсенной строки
    private static String parseCountryFromSentence(String sentence) {
        int startCountry = sentence.indexOf(",");
        int endCountryStopPoint = sentence.indexOf(".");

        StringBuilder stringBuilder = new StringBuilder(sentence);
        StringBuilder currentBuilder = new StringBuilder(stringBuilder.substring(startCountry, endCountryStopPoint));

        currentBuilder.delete(0, 2);

        String result = new String(currentBuilder).trim();

        return result.substring(0, 1).toUpperCase() + result.substring(1);
    }

    //3.Находим номер полета и номер путевки
    private static int methodSearhNumberFlightIntheText(String str, String caption) {
        int result = 0;

        String allText = str.toLowerCase();
        String textArray[] = allText.split(" ");

        Pattern pat = Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
        Matcher matcher;

        int indexOrder = searchElementInArray(0,caption, textArray);

        for(int i = indexOrder; i < textArray.length; i++){
            matcher = pat.matcher(textArray[i]);
            while (matcher.find()) {
                result = Integer.parseInt(matcher.group());
                break;
            };
            if(result != 0){
                break;
            }
        }

        return result;
    }

    //4.Находим к-сть топлива
    private static double methodSearchQuelityConsumption(String str) {
        double result = 0;
        String allText = str.toLowerCase();
        String textArray[] = allText.split(" ");

        Pattern pat = Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
        Matcher matcher;

        int indexAllocate = searchElementInArray(searchElementInArray(0, "voucher", textArray),"allocate", textArray);

        String sentence = "";

        for(int i = indexAllocate; i < textArray.length; i++){
            if(!textArray[i].equals("kg")){
                sentence += textArray[i];
            }else {
                break;
            }
        }
        matcher = pat.matcher(sentence);
        while (matcher.find()) {
            result = Double.parseDouble(matcher.group());
            break;
        };

        return result;
    }
}
