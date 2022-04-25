package operations;

import gui_forms.TextForm;
import model.CityBase;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckTextFile {
    static String modelRocket = null;
    static CityBase base = null;

    public static boolean isIndicatorAll(String currentText) {
        return methodCheckCountWords(currentText) && methodCheckCountSentences(currentText)
         && methodCheckParagraphsInText(currentText) && methodCheckCaptions(currentText)
        && methodCheckPresenceOfSentenceInTheText(currentText)  && methodCheckModelRocket(currentText)
        && methodCheckCityBaseInTheText(currentText) && methodFindQuantityPersons(currentText) &&
                checkPositionAndGradeOfCommanderChief(currentText);
    }

    // 1)к-ство слов
    public static boolean methodCheckCountWords(String str) {
        String[] currentTextDoc = str.split(" ");

        if (currentTextDoc.length > 100 && currentTextDoc.length < 200) return true;
        else {
            JOptionPane.showMessageDialog(null, "The text must have at least 100, and not more than 200 words!");
            return false;
        }
    }

    //2)к-ств предложений
    public static boolean  methodCheckCountSentences(String str) {
        char[] currentTextChar = str.toCharArray();
        int countSentence = 0;

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
                if (currentTextChar[currentIndexChar + 1] == '\n') {
                    countSentence++;
                }
                currentIndexChar = 0;

            } else {
                if (currentTextChar[i] == '.' || currentTextChar[i] == ':') {
                    countSentence++;
                }
            }
        }

        if (countSentence >= 12 && countSentence <= 17) return true;
        else {
            JOptionPane.showMessageDialog(null, "The text must have at least 12 sentences! You have to put dots in sentences.");
            return false;
        }
    }

    // 3)к-ство абзацев
    public static boolean methodCheckParagraphsInText(String str) {
        char[] currentTextChar = str.toCharArray();
        int countParagraphs = 0;

        for (int i = 0; i < currentTextChar.length; i++) {
            if (currentTextChar[i] == '.' || currentTextChar[i] == ':') {
                if (currentTextChar[i + 1] == '\n') {
                    countParagraphs++;
                }
            }
        }

        if(countParagraphs >= 10 && countParagraphs <= 15) return true;
        else {
            JOptionPane.showMessageDialog(null, "There must be at least 10 paragraphs in the text!");
            return false;
        }
    }

    // 4)Заголовки The order  и voucher
    public static boolean methodCheckCaptions(String str) {
        String allText = str.toLowerCase();
        String[] textArray = allText.split(" ");
        operations.NumericClass numericClass = new operations.NumericClass();

        int indexTheOrder = numericClass.searchElement(0, "order", textArray);
        int indexVoucher = numericClass.searchElement(0, "voucher", textArray);

        if (indexTheOrder != -1 && indexTheOrder < 3) {

            if (textArray[indexTheOrder - 1].equals("the") && findIntegerNumber(indexTheOrder, textArray) != 0) {
                if (indexVoucher != -1) {
                    if (findDoubleNumber(indexVoucher, "kg", textArray) == 0) {
                        JOptionPane.showMessageDialog(TextForm.textForm, "There must be quelity of consumption!","Alert", JOptionPane.ERROR_MESSAGE);
                        return false;

                    } else return true;

                } else {
                    JOptionPane.showMessageDialog(TextForm.textForm, "There must be 2 captions: The order and voucher!","Alert", JOptionPane.ERROR_MESSAGE);
                    return false;

                }
            }
        } else JOptionPane.showMessageDialog(TextForm.textForm, "There must be 2 captions: The order and voucher!","Alert", JOptionPane.ERROR_MESSAGE);


        return false;

    }

    // 5) предложения которые начинаються с ключевых слов.
    public static boolean methodCheckPresenceOfSentenceInTheText(String str) {
        boolean isEmpty = false;

        String[] listConcreteSentence = {
                "rocket with a satellite on board is headed for a space expedition",
                "The satellite will conduct scientific, economic,\n", "and military work",
                "The satellite is accompanied by a crew of",
                "The Launch  site is", "The Commander in Chief - ", "Ordered to allocate",
                "of fuel for flight №", "The Commander in Chief\n"
        };

        String[] textArray = str.split("");
        StringBuilder temp = new StringBuilder();

        if (methodCheckSentence(str, listConcreteSentence)) {
            for (int i = 0; i < listConcreteSentence.length; i++) {
                for (int j = 0; j < textArray.length; j++) {

                    temp.append(textArray[j]);
                    //если отрезки пределожений з массива соовпадают в именно заданной последовательности в listConcreteSentence
                    //TODO работает не корректно
                    if (temp.toString().contains(listConcreteSentence[i])) {
                        if (j == 0) isEmpty = false;
                        else isEmpty = true;
                        break;
                    }
                }
            }
            /*for(int i = 0; i < listConcreteSentence.length; i++){
                if(str.contains(listConcreteSentence[i])){
                    if(i != numericClass.searchElement(0, listConcreteSentence[i], listConcreteSentence)) {
                        isEmpty = false;
                        break;
                    }else isEmpty = true;
                }
            }*/
        } else {
            JOptionPane.showMessageDialog(TextForm.textForm, "The text must be write structured!");
        }
        //System.out.println("isEmpty = " + isEmpty);

        if (!isEmpty) JOptionPane.showMessageDialog(TextForm.textForm, "The sentences must be correct order!");

        return isEmpty;
    }

    //5.1 Проверка наличия приделожений
    public static boolean methodCheckSentence(String str, String[] listConcreteSentence) {
        for (String s : listConcreteSentence) {
            if (str.contains(s)) return true;
            else break;
        }
        return false;
    }

    //6 наличее модели ракеты
    public static boolean methodCheckModelRocket(String str) {
        String allText = str.toLowerCase();
        String[] textArray = allText.split(" ");

        String[] modelsRocket = {"falcon", "dnipro", "launcher_one"};

        int indexNameModel = 0;

        for (String model : modelsRocket) {
            indexNameModel = operations.TextReader.searchElementInArray(0, model, textArray);
            if(indexNameModel != -1) break;
        }

        if(indexNameModel == -1){
            for (String model : modelsRocket) {
                indexNameModel = operations.TextReader.searchElementInArrayItem(0, model, textArray);
                if(indexNameModel != -1) break;
            }
            if (indexNameModel == -1){
                JOptionPane.showMessageDialog(TextForm.textForm, "There is'nt model of rocket!");
                return false;
            }
        }else {
            //массив чтобы понять исходный регистр букв, если a/the, с мальнькой буквы тогда error
            String[] textArrayUpperCase = str.split(" ");
            if(searchWordEqualsOrContains(textArrayUpperCase[indexNameModel - 1],"A") || searchWordEqualsOrContains(textArrayUpperCase[indexNameModel - 1],"The")){
                int numberModel = findIntegerNumberToSymbol(indexNameModel, "rocket", textArray);

                if(numberModel != 0){
                    modelRocket = textArrayUpperCase[indexNameModel] + " " + numberModel;
                    return true;
                }else {
                    JOptionPane.showMessageDialog(TextForm.textForm, "The model haven't number!", "Alert", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(null, "There is'nt article before model's name!");
                return false;
            }
        }
        return false;
    }

    // 8) число экипажа буквами
    public static boolean methodFindQuantityPersons(String str){
        String allText = str.toLowerCase();
        String[] textArray = allText.split(" ");
        NumericClass numericClass = new NumericClass();

        int indexWordPeople = numericClass.searchElement(0, "people", textArray);

        String number = textArray[indexWordPeople - 1];

        String[] nameNumbers = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
        int numberFromList = 0;

        for(int i = 0; i < nameNumbers.length; i++){
            if(nameNumbers[i].equals(number)) numberFromList = i;
        }

        System.out.println("TextReader.numberMember=" + TextReader.numberMember);
        System.out.println("numberFromList=" + numberFromList);

        //сравниваем к-сть экипажа с числом в тексте + ищем : возле слова people
        if((TextReader.numberMember == numberFromList) &&
                (searchCharacterInStringIteration(":", textArray[indexWordPeople]) ||
                        searchCharacterInStringIteration( ":", textArray[indexWordPeople + 1]))) return true;
        else JOptionPane.showMessageDialog(null, "The quanlity of crew was availabled no correctly!");

        return false;
    }

    // 9)место запуска
    public static boolean methodCheckCityBaseInTheText(String str) {
        String allText = str.toLowerCase();
        String[] textArray = allText.split(" ");
        boolean isCorrectly = false;

        operations.NumericClass numericClass = new operations.NumericClass();

        int indexLaunch = numericClass.searchElement(0, "launch", textArray);

        String sentence = "";

        if (indexLaunch != -1) {
            StringBuilder sentenceBuilder = new StringBuilder();
            for (int i = indexLaunch; i < textArray.length; i++) {
                if (searchCharacterInStringIteration("\\.", textArray[i])) {
                    sentenceBuilder.append(textArray[i]).append(" ");
                    break;
                } else sentenceBuilder.append(textArray[i]).append(" ");
            }
            sentence = sentenceBuilder.toString();
        } else{
            JOptionPane.showMessageDialog(null, "The text wasn't written correctly!");
        }

        String parseCity = operations.TextReader.parseCityFromSentence(sentence);
        String parseCountry = operations.TextReader.parseCountryFromSentence(sentence);
        System.out.println("methodCheckBaseForLaunch = " + methodCheckBaseForLaunch(parseCity, parseCountry));
        if(parseCity!= null &&  parseCountry!= null){
            isCorrectly = methodCheckBaseForLaunch(parseCity, parseCountry);

            if(isCorrectly) base = new CityBase(parseCity, parseCountry);
            else JOptionPane.showMessageDialog(null, """
                    Please, enter the launch city correctly!
                    The list of spaceports:
                    Korotych - Ukraine;
                    Paris - France;
                    New York - USA;
                    Rio de Janeiro - Brazil;
                    """);
        }

        return isCorrectly;
    }

    //9.1 проверка города, на соответсвие с зарегистрированными базами
    public static boolean methodCheckBaseForLaunch(String city, String country){
        CityBase citiesBaseArray [] = {
                new CityBase("Korotych", "Ukraine"), new CityBase("Paris", "France"),
                new CityBase("New York", "USA"), new CityBase("Rio de Janeiro", "Brazil")
        };

        for(CityBase currentCityBase : citiesBaseArray) {
            if(currentCityBase.city.equals(city) && currentCityBase.country.equals(country)) return true;

        }

        return false;
    }

    // 10) должность и звание главнокомандуещеего
    public static boolean checkPositionAndGradeOfCommanderChief(String str){
        String[] textArray = str.split("");

        String[] titlesForPositionCommander = {"The Commander in Chief - ", "The Commander in Chief\n"};
        StringBuilder temp = new StringBuilder();
        boolean isEmpty = false;

        String[] tempArrayWithTitlesByCommander = new String[titlesForPositionCommander.length];
        //проверка на наличие предложение в тексте
        if(methodCheckSentence(str, titlesForPositionCommander)){

            for (int i = 0; i < titlesForPositionCommander.length; i++) {
                for (int j = 0; j < textArray.length; j++) {
                    //перебираем текст в виде массива
                    temp.append(textArray[j]);

                    if (temp.toString().contains(titlesForPositionCommander[i])) {
                        //находим, в элементы текста в заданной последовательности массива titlesForPositionCommander
                        if (j == 0) isEmpty = false;
                        else isEmpty = true;
                        //методы parseDataPositionFromString и returnSentenceWithCommander парсят строку
                        // от индекса j до точки, далее текст розпарсенный обрабатуется данними методами
                        tempArrayWithTitlesByCommander[i] = parseDataPositionFromString(returnSentenceWithCommander(str,j));
                        break;
                    }
                }
            }
            // сравниваем два полученных элемента
            isEmpty = tempArrayWithTitlesByCommander[0].equals(tempArrayWithTitlesByCommander[1]);

        }else {
            JOptionPane.showMessageDialog(null, "The order must include the title, name, and surname of the commander-in-chief!");
        }

        if(!isEmpty) JOptionPane.showMessageDialog(null, "The position of commander is not correct.");
        return isEmpty;

    }

    //10.1
    public static String returnSentenceWithCommander(String tempText, int indexFromFirstArray){
        String sentence = "";
        char[] strToCharArray = tempText.toCharArray();

        for(int i = indexFromFirstArray; i < strToCharArray.length; i++){
            // собираем до точки
            if(strToCharArray[i] != '.') sentence += strToCharArray[i];
            else break;
        }

        return sentence;
    }

    //10.2
    public static String parseDataPositionFromString(String sentence){
        // звание которое может быть у главнокомандуещего
        String[] namePositions = {"General of Army", "Сolonel of Aviation"};
        String currentDegree = null;
        String firstName = "";
        String lastName = "";

        for(String degree : namePositions){
            //проверяем на наличение
            if(searchWordEqualsOrContains(sentence, degree)) currentDegree = degree;
        }
        if(currentDegree != null){
            StringBuffer sb = new StringBuffer(sentence);
            //через stringbuffer взяли только часть преложения с именем и фамилией
            String nameAndSurname = sb.substring(currentDegree.length() + 1);

            String [] name = nameAndSurname.trim().split(" ");

            //парсим из предложения имя и фамилию
            int index = 0;
            for(int i = 0; i < name.length; i++){
                if(!operations.TextReader.methodCheckTextCharacters(name[index]) && operations.TextReader.methodCheckTextStrings(name[index])){
                    firstName = name[index];
                }else {
                    firstName = name[++index];
                }
            }
            lastName = name[index + 1];
        } else {
            JOptionPane.showMessageDialog(null, "The position of commander is not correct.");
        }


        //отдаем обратно для сравнения
        return "first_name:" + firstName + ", last_name:" + lastName + ", position: " + currentDegree;
    }


    //вспомогательные методы

    //Наличение числа int
    public static int findIntegerNumber(int index, String textArray[]){
        Pattern pat = Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
        Matcher matcher;

        int result = 0;

        for(int i = index; i < textArray.length; i++){
            matcher = pat.matcher(textArray[i]);
            while (matcher.find()) {
                result = Integer.parseInt(matcher.group());
                break;
            }
            if(result != 0){
                break;
            }
        }

        return result;
    }

    //Наличение числа int. ищет до конкретного элемент
    public static int findIntegerNumberToSymbol(int index, String endStrLine, String textArray[]){
        int result = 0;
        Pattern pat = Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
        Matcher matcher;

        StringBuilder sentence = new StringBuilder();

        for(int i = index; i < textArray.length; i++){
            if(textArray[i].equals(endStrLine) || textArray[i].contains(endStrLine)){
                break;
            } else{
                sentence.append(textArray[i]);
            }
        }
        matcher = pat.matcher(sentence.toString());
        while (matcher.find()) {
            result = Integer.parseInt(matcher.group());
            break;
        };

        return result;
    }

    //наличие числа double
    public static double findDoubleNumber(int index, String endStrLine, String textArray[]){
        double result = 0;
        Pattern pat = Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
        Matcher matcher;

        String sentence = "";

        for(int i = index; i < textArray.length; i++){
            if(!textArray[i].equals(endStrLine)){
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

        return result;
    }

    public static boolean searchWordEqualsOrContains(String line, String element){
         if(line.equals(element)) return true;
         else if(line.contains(element)) return true;

        return false;
    }

    //2.2 Поиск конкретного символа в строке
    public static boolean searchCharacterInStringIteration(String regex, String str){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        return matcher.find();
    }
}
