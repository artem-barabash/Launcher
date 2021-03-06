package operations;

import gui_forms.App;
import gui_forms.TextForm;
import model.CrewMembersList;
import model.LauncherRocketModel;
import model.StatusLaunch;
import model.crewmemebers.*;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextReader {

    static App app;
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static CrewMembersList members = new CrewMembersList();
    static int numberMember = 0;

    //проверка текста на соотвествие правил оформления
    //public  CheckTextFile checkTextFile;
    DBHadler dbHadler = new DBHadler();


    public TextReader() {
    }


    public static String read(String filePath) {
        StringBuilder sb = new StringBuilder();
        try {
            try (BufferedReader in = new BufferedReader(new FileReader(new File(filePath).getAbsoluteFile()))) {
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
    public  void runTextReader(String str) throws Exception {
        String text = read(str);


        methodToolSearchPositionsInTheText(text);
        if(CheckTextFile.isIndicatorAll(text)){
            //1.Находим в списком экипажа по должностям

            System.out.println(members.size());
            if(members.checkIdenticalItemCrewMember() && members.checkListOfMemberAboutPositions()
                    && members.makeNextOperations){
                //3.Находим номер полета и путевку
                int numberFlight = methodSearchNumberFlightInTheText(text, "order");

                if(dbHadler.methodCheckFlightInDB(numberFlight)){
                    //просмотр номера по базе
                    JOptionPane.showMessageDialog(TextForm.textForm, "This order's number was in base!", "Alert", JOptionPane.ERROR_MESSAGE);
                }else{
                    //2.Находим город запуска ракеты
                    String cityBaseTakeOff = methodSearchCityBaseInTheText(text);
                    System.out.println("cityBasetakeOff = " + cityBaseTakeOff);

                    System.out.println("numberFlight = " +numberFlight);
                    //int numberVoucher = methodSearhNumberFlightIntheText(text, "voucher");

                    //4.Находим к-сть топлива
                    double quelityConsumption = methodSearchQuelityConsumption(text);
                    System.out.println("quelityConsumption = " + quelityConsumption);

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");

                    LauncherRocketModel launcherRocketModel = new LauncherRocketModel(numberFlight,  CheckTextFile.modelRocket, String.valueOf(StatusLaunch.SUCCESS), CheckTextFile.base, null, quelityConsumption, members, null);
                    System.out.println("checkTextFile.modelRocket = " + CheckTextFile.modelRocket);
                    System.out.println(launcherRocketModel.toString());
                    try {
                        app = new App(launcherRocketModel);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    app.setSize(screenSize.width, screenSize.height);
                    app.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    app.setVisible(true);
                }
            }
        }

        //TODO  Проверка текстового файла на правильность составления: ключевые слова, абзацы
        //TODO
        // 1)к-ство слов+
        // 2)к-ств предложений+
        // 3)к-ство абзацев+
        // 4)Заголовки The order  и voucher,+
        // 5) предложения которые начинаються с ключевых слов+.
        // 6) наличее модели ракеты+ 7. списка экипажа с номерами.+-
        // 8) число экипажа буквами+
        // 9)место запуска+
        // 10) должность и звание главнокомандуещеего+
        // 11) обьем выделенного горячего+.
        // 12) и полсе только ниже прописанная проверка с внесением данных в перемены и об'єкти.+-
    }

    //1.Находим в списком экипажа по должностям
    public static void methodToolSearchPositionsInTheText(String text) throws Exception {
        String[] listCrewMembersPositions = {"Commander", "Engineer", "Gunner", "Doctor", "Tourist"};

        char[] strToCharArray = text.toCharArray();
        String currentTextDoc = text.toLowerCase();

        int indexWordStart = 0;

        int indexWordEnd = currentTextDoc.indexOf("The Launch".toLowerCase());
        //System.out.println("indexWordEnd = " + indexWordEnd);

        if(indexWordEnd != -1) {
            for (String position : listCrewMembersPositions) {
                String sentance = "";

                indexWordStart = currentTextDoc.indexOf(position.toLowerCase());

                if (indexWordStart != -1) {
                    for (int j = indexWordStart; j < indexWordEnd; j++) {
                        if (strToCharArray[j] != '.') sentance += Character.toString(strToCharArray[j]);
                        else break;
                    }
                    ++numberMember;
                    members.add(searchDataPersonInSentance(numberMember, sentance));
                    //System.out.println(numberMember + " " + sentance);

                    methodForAddOtherSimilarCrewMember(indexWordStart, indexWordEnd, position, strToCharArray, currentTextDoc);
                } else {
                    JOptionPane.showMessageDialog(TextForm.textForm, "There have'nt " + position + " in the list.", "Alert", JOptionPane.ERROR_MESSAGE);
                    //Предотвращает вход на запуск с негативной проверкой
                    members.makeNextOperations = false;
                    break;
                }
            }
        }else {
            JOptionPane.showMessageDialog(TextForm.textForm, "There must be sentance: 'The Launch  site is...' in the text. ", "Alert", JOptionPane.ERROR_MESSAGE);
        }
    }

    //1.1 Если должность n, не у одного члена экипажа.
    private static void methodForAddOtherSimilarCrewMember(int indexWordStart, int indexWordEnd, String position, char[] strToCharArray, String currentTextDoc) throws Exception {
        String sentance;
        int i = 0;
        //берем индекс первого элемента с текущей должностью
        int indexPreviousWord = indexWordStart;
        //номер члена экипажа

        //проходим циклом до конца, то есть The Commander
        while(i < indexWordEnd){
            //добавляем 1 к индексу предыдущего, чтобы найти еще одного члена, с такой же должностью
            int currentWordIndex = currentTextDoc.indexOf(position.toLowerCase(), indexPreviousWord + 1);

            if(currentWordIndex != -1){
                if(currentWordIndex < indexWordEnd){
                    sentance = "";

                    for (int j = currentWordIndex; j < indexWordEnd; j++) {
                        if (strToCharArray[j] != '.') {
                            sentance += Character.toString(strToCharArray[j]);
                            //присваеваим индекс найденой строки с особой и должность.
                            // Когда это значение возвращается в начала получает + 1.
                            // И происходить дальнейший поиск подобных элементов
                            indexPreviousWord = j;
                        } else {
                            break;
                        }
                    }
                    numberMember++;
                    members.add(searchDataPersonInSentance(numberMember,sentance));
                    //System.out.println(numberMember  + " " + sentance);
                }
            }else break;

            i++;
        }
    }

    //1.2. Создание обьекта на основе полученных данных
    private static CrewMember searchDataPersonInSentance(int numberMember, String textSentence){
        String[] arrayPerson = textSentence.split(" ");
        String position = arrayPerson[0];
        String name = "";
        int index = 1;

        for(int i = 0 ; i < arrayPerson.length; i++){
            if((!methodCheckTextCharacters(arrayPerson[index]) && methodCheckTextStrings(arrayPerson[index]))){
                name = arrayPerson[index];
            }else {
                name = arrayPerson[++index];
            }
        }
        String surname = arrayPerson[index + 1];

        CrewMember member = createMemberForHisActivity(new CrewMember(numberMember, name, surname, position));

        return member;
    }

    //1.3 Проверка на наличение символа
     static boolean methodCheckTextCharacters(String s) {
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

    //1.4 Проверка на наличие букв в элементе
    static boolean methodCheckTextStrings(String s) {
        Pattern pattern = Pattern.compile("[a-zA-Z]");
        Matcher matcher = pattern.matcher(s);
        boolean result = matcher.find();

        return result;
    }

    //1.5 присваеваем данные обьекту, с созданием экземпляра класса взависимости от должности
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

        CrewMember crewMember = crewMemberSample;
        return crewMember;
    }



    //2.Находим город запуска ракеты
    private static String methodSearchCityBaseInTheText(String str) {
        String allText = str.toLowerCase();
        String textArray[] = allText.split(" ");
        //System.out.println(Arrays.toString(textArray));

        int indexLaunch = searchElementInArray(0, "launch", textArray);

        String sentence = "";

        if(indexLaunch != -1){
            for(int i = indexLaunch; i < textArray.length; i++){
                if(searchCharacterInStringIteration("\\.", textArray[i])){
                    sentence += textArray[i] + " ";
                    break;
                }else {
                    sentence += textArray[i] + " ";
                }
            }
        }else {
            indexLaunch = searchElementInArrayItem(0, "launch", textArray);
            if (indexLaunch == -1){
                JOptionPane.showMessageDialog(TextForm.textForm, "The text wasn't written correctly!", "Alert", JOptionPane.ERROR_MESSAGE);
            }
        }
        //В БД нужно добавить отдельно город и отдельно страну
        return "Launch place: " + parseCityFromSentence(sentence) + " - " + parseCountryFromSentence(sentence);
    }

    //2.1 индекс слова "launch" в предложении где речь идет о метсе запуска
    //TODO другой алгоритм(более быстрый)
    static int searchElementInArray(int indexStart, String element, String[] arr){
        int index = -1;

        for(int i = indexStart; i < arr.length; i++){
            if(arr[i].equals(element)) index = i;
        }

        return index;
    }
    //2.1 Если элемент массива содержит слово тогда возращает индекс, если нет -1
     static int searchElementInArrayItem(int indexStart, String element, String[] arr){
        int index = -1;

        for(int i = indexStart; i < arr.length; i++){

            if(arr[i].contains(element)){
                index = i;
                break;
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
    static String parseCityFromSentence(String sentence) {
        int startCity = sentence.indexOf("is");
        int endCityComma = sentence.indexOf(",");

        if(startCity != -1 && endCityComma != -1){
            StringBuilder stringBuilder = new StringBuilder(sentence);

            StringBuilder currentResult = new StringBuilder(stringBuilder.substring(startCity, endCityComma));
            currentResult.delete(0, 2);

            String result = new String(currentResult).trim();

            return result.substring(0, 1).toUpperCase() +   result.substring(1);
        }else {
            JOptionPane.showMessageDialog(TextForm.textForm, "Please, enter the launch city correctly!", "Alert", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    //2.4 Взять страну из розпарсенной строки
    static String parseCountryFromSentence(String sentence) {
        int startCountry = sentence.indexOf(",");
        int endCountryStopPoint = sentence.indexOf(".");

        if(startCountry != -1 && endCountryStopPoint != -1){
            StringBuilder stringBuilder = new StringBuilder(sentence);
            StringBuilder currentBuilder = new StringBuilder(stringBuilder.substring(startCountry, endCountryStopPoint));

            currentBuilder.delete(0, 2);

            String result = new String(currentBuilder).trim();

            return result.substring(0, 1).toUpperCase() + result.substring(1);
        }else {
            JOptionPane.showMessageDialog(TextForm.textForm, "Please, enter the launch city correctly!", "Alert", JOptionPane.ERROR_MESSAGE);
            return null;
        }

    }

    //3.Находим номер полета и номер путевки
    private static int methodSearchNumberFlightInTheText(String str, String caption) {
        int result = 0;

        String allText = str.toLowerCase();
        String[] textArray = allText.split(" ");

        Pattern pat = Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
        Matcher matcher;

        int indexOrder = searchElementInArray(0,caption, textArray);
        int indexOrderItem = searchElementInArrayItem(0,caption, textArray);

       if(indexOrder == -1){
           if(indexOrderItem != -1 && indexOrderItem < 3){
               for(int i = indexOrderItem; i < textArray.length; i++){
                   matcher = pat.matcher(textArray[i]);
                   while (matcher.find()) {
                       result = Integer.parseInt(matcher.group());
                       break;
                   }
                   if(result != 0) break;
               }
           }else {
               result = 0;
               //System.out.println("error");
           }
       }else {
           for(int i = indexOrder; i < textArray.length; i++){
               matcher = pat.matcher(textArray[i]);
               while (matcher.find()) {
                   result = Integer.parseInt(matcher.group());
                   break;
               }
               if(result != 0) break;
           }
       }

        return result;
    }

    //4.Находим к-сть топлива
    private static double methodSearchQuelityConsumption(String str) {
        double result = 0;
        String allText = str.toLowerCase();
        String[] textArray = allText.split(" ");

        Pattern pat = Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
        Matcher matcher;
        NumericClass numeric = new NumericClass();

        int voucherIndex = numeric.searchElement(0, "voucher", textArray);
        int indexAllocate = numeric.searchElement(voucherIndex,"allocate", textArray);

        if(indexAllocate != -1){
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
            }
        }
        return result;
    }
}
