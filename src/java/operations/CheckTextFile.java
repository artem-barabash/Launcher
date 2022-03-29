package operations;

import model.CityBase;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckTextFile {
    boolean indicatorAll = false;
    String modelRocket = null;
    static CityBase base = null;

    public CheckTextFile(String textFile) {
        methodCheckCountWords(textFile);
        methodCheckCountSentences(textFile);
        methodCheckParagraghInText(textFile);
        methodCheckCaptions(textFile);
        methodCheckPresenceOfSentenceInTheText(textFile);
        methodCheckModelRocket(textFile);
        //methodCheckCityBaseInTheText(textFile);
        checkPositionAndGradeOfCommanderChief(textFile);
    }


    public boolean isIndicatorAll() {
        return indicatorAll;
    }

    public void setIndicatorAll(boolean indicatorAll) {
        this.indicatorAll = indicatorAll;
    }

    // 1)к-ство слов
    void methodCheckCountWords(String str) {
        String[] currentTextDoc = str.split(" ");

        if (currentTextDoc.length > 100 && currentTextDoc.length < 200) {
            indicatorAll = true;
        } else {
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

        indicatorAll = countSentence >= 12 && countSentence <= 17 ? true : false;
        if (!indicatorAll) {
            JOptionPane.showMessageDialog(null, "The text must have at least 12 sentences! You have to put dots in sentences.");
        }
    }

    // 3)к-ство абзацев
    void methodCheckParagraghInText(String str) {
        char[] currentTextChar = str.toCharArray();
        int countParagragh = 0;

        for (int i = 0; i < currentTextChar.length; i++) {
            if (currentTextChar[i] == '.' || currentTextChar[i] == ':') {
                if (currentTextChar[i + 1] == '\n') {
                    countParagragh++;
                }
            }
        }

        indicatorAll = countParagragh >= 10 && countParagragh <= 15 ? true : false;

        if (!indicatorAll) {
            JOptionPane.showMessageDialog(null, "There must be at least 10 paragraphs in the text!");
        }
    }

    // 4)Заголовки The order  и voucher
    void methodCheckCaptions(String str) {
        String allText = str.toLowerCase();
        String textArray[] = allText.split(" ");
        operations.NumericClass numericClass = new operations.NumericClass();

        boolean resultCheck = false;

        int indexTheOrder = numericClass.searchElement(0, "order", textArray);
        int indexVoucher = numericClass.searchElement(0, "voucher", textArray);

        if (indexTheOrder != -1 && indexTheOrder < 3) {

            if (textArray[indexTheOrder - 1].equals("the") && findIntegerNumber(indexTheOrder, textArray) != 0) {
                if (indexVoucher != -1) {
                    if (findDoubleNumber(indexVoucher, "kg", textArray) == 0) {
                        resultCheck = false;
                        JOptionPane.showMessageDialog(null, "There must be quelity of consumption!");
                    } else {
                        resultCheck = true;
                    }
                } else {
                    resultCheck = false;
                    JOptionPane.showMessageDialog(null, "There must be 2 captions: The order and voucher!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "There must be 2 captions: The order and voucher!");
        }

        indicatorAll = resultCheck;

    }

    // 5) предложения которые начинаються с ключевых слов.
    void methodCheckPresenceOfSentenceInTheText(String str) {
        boolean isEmpty = false;

        String[] listConcreteSentence = {
                "rocket with a satellite on board is headed for a space expedition",
                "The satellite will conduct scientific, economic,\n", "and military work",
                "The satellite is accompanied by a crew of",
                "The Launch  site is", "The Commander in Chief - ", "Ordered to allocate",
                "of fuel for flight №", "The Commander in Chief\n"
        };

        String textArray[] = str.split("");
        String temp = "";

        if (methodCheckSentence(str, listConcreteSentence)) {
            for (int i = 0; i < listConcreteSentence.length; i++) {
                for (int j = 0; j < textArray.length; j++) {

                    temp += textArray[j];
                    //если отрезки пределожений з массива соовпадают в именно заданной последовательности в listConcreteSentence
                    if (temp.contains(listConcreteSentence[i])) {
                        if (j == 0) isEmpty = false;
                        else isEmpty = true;
                        break;
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "The text must be write structured!");
        }
        //System.out.println("isEmpty = " + isEmpty);
        indicatorAll = isEmpty;
        if (!isEmpty) JOptionPane.showMessageDialog(null, "The sentences must be correct order!");

    }

    //5.1 Проверка наличия приделожений
    private boolean methodCheckSentence(String str, String[] listConcreteSentence) {
        for (int i = 0; i < listConcreteSentence.length; i++) {
            if (str.contains(listConcreteSentence[i])) return true;
             else break;
        }
        return false;
    }

    //6 наличее модели ракеты
    void methodCheckModelRocket(String str) {
        String allText = str.toLowerCase();
        String textArray[] = allText.split(" ");

        String modelsRocket[] = {"falcon", "dnipro", "launcher_one"};

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
                indicatorAll = false;
                JOptionPane.showMessageDialog(null, "There is'nt model of rocket!");
            }
        }else {
            //массив чтобы понять исходный регистр букв, если a/the, с мальнькой буквы тогда error
            String textArrayUpperCase[] = str.split(" ");
            if(searchWordEqualsOrContains(textArrayUpperCase[indexNameModel - 1],"A") || searchWordEqualsOrContains(textArrayUpperCase[indexNameModel - 1],"The")){
                int numberModel = findIntegerNumberToSymbol(indexNameModel, "rocket", textArray);

                if(numberModel != 0){
                    modelRocket = textArrayUpperCase[indexNameModel] + " " + numberModel;
                    indicatorAll = true;
                }else {
                    indicatorAll = false;
                    JOptionPane.showMessageDialog(null, "The model haven't number!");
                }
            } else {
                indicatorAll = false;
                JOptionPane.showMessageDialog(null, "There is'nt article before model's name!");
            }
        }
    }

    // 8) число экипажа буквами
    static boolean methodFindQuantityPersons(String str){
        String allText = str.toLowerCase();
        String textArray[] = allText.split(" ");
        operations.NumericClass numericClass = new operations.NumericClass();

        int indexWordPeople = numericClass.searchElement(0, "people", textArray);

        String number = textArray[indexWordPeople - 1];

        String nameNumbers[] = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
        int numberFromList = 0;

        for(int i = 0; i < nameNumbers.length; i++){
            if(nameNumbers[i].equals(number)) numberFromList = i;
        }

        //сравниваем к-сть экипажа с числом в тексте + ищем : возле слова people
        if((operations.TextReader.numberMember == numberFromList) && (searchCharacterInStringIteration(":", textArray[indexWordPeople]) || searchCharacterInStringIteration( ":", textArray[indexWordPeople + 1]))) return true;
        else JOptionPane.showMessageDialog(null, "The quanlity of crew was availabled no correctly!");

        return false;
    }

    // 9)место запуска
    static boolean methodCheckCityBaseInTheText(String str) {
        String allText = str.toLowerCase();
        String textArray[] = allText.split(" ");
        boolean isCorrectly = false;

        operations.NumericClass numericClass = new operations.NumericClass();

        int indexLaunch = numericClass.searchElement(0, "launch", textArray);

        String sentence = "";

        if (indexLaunch != -1) {
            for (int i = indexLaunch; i < textArray.length; i++) {
                if (searchCharacterInStringIteration("\\.", textArray[i])) {
                    sentence += textArray[i] + " ";
                    break;
                } else sentence += textArray[i] + " ";
            }
        } else{
            JOptionPane.showMessageDialog(null, "The text wasn't written correctly!");
        }

        String parseCity = operations.TextReader.parseCityFromSentence(sentence);
        String parseCountry = operations.TextReader.parseCountryFromSentence(sentence);
        System.out.println("methodCheckBaseForLaunch = " + methodCheckBaseForLaunch(parseCity, parseCountry));
        if(parseCity!= null &&  parseCountry!= null){
            isCorrectly = methodCheckBaseForLaunch(parseCity, parseCountry);

            if(isCorrectly) base = new CityBase(parseCity, parseCountry);
            else JOptionPane.showMessageDialog(null, "Please, enter the launch city correctly!\n" +
                    "The list of spaceports:\n"+
                    "Korotych - Ukraine;\n"+ "Paris - France;\n"+
                    "New York - USA;\n" + "Rio de Janeiro - Brazil;\n");
        }

        return isCorrectly;
    }

    //9.1 проверка города, на соответсвие с зарегистрированными базами
     static boolean methodCheckBaseForLaunch(String city, String country){
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
     void checkPositionAndGradeOfCommanderChief(String str){
        String textArray[] = str.split("");

        String titlesForPositionCommander[] = {"The Commander in Chief - ", "The Commander in Chief\n"};
        String temp = "";
        boolean isEmpty = false;

        String tempArrayWithTitlesbyCommader[] = new String[titlesForPositionCommander.length];
        //проверка на наличие предложение в тексте
        if(methodCheckSentence(str, titlesForPositionCommander)){

            for (int i = 0; i < titlesForPositionCommander.length; i++) {
                for (int j = 0; j < textArray.length; j++) {
                    //перебираем текст в виде массива
                    temp += textArray[j];

                    if (temp.contains(titlesForPositionCommander[i])) {
                        //находим, в элементы текста в заданной последовательности массива titlesForPositionCommander
                        if (j == 0) isEmpty = false;
                        else isEmpty = true;
                        //методы parseDataPositionFromString и returnSentenceWithCommander парсят строку
                        // от индекса j до точки, далее текст розпарсенный обрабатуется данними методами
                        tempArrayWithTitlesbyCommader[i] = parseDataPositionFromString(returnSentenceWithCommander(str,j));
                        break;
                    }
                }
            }
            // сравниваем два полученных элемента
            isEmpty = tempArrayWithTitlesbyCommader[0].equals(tempArrayWithTitlesbyCommader[1]);

        }else {
            JOptionPane.showMessageDialog(null, "The order must include the title, name, and surname of the commander-in-chief!");
        }

        indicatorAll = isEmpty;
        if(!indicatorAll) JOptionPane.showMessageDialog(null, "The position of commander is not correct.");
    }

    //10.1
    private String returnSentenceWithCommander(String tempText, int indexFromFirstArray){
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
    private String parseDataPositionFromString(String sentence){
        // звание которое может быть у главнокомандуещего
        String namePositions[] = {"General of Army", "Сolonel of Aviation"};
        String currentDegree = null;
        String first_name = "";
        String last_name = "";

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
                if(!operations.TextReader.methodCheckTextСharacters(name[index]) && operations.TextReader.methodCheckTextStrings(name[index])){
                    first_name = name[index];
                }else {
                    first_name = name[++index];
                }
            }
            last_name = name[index + 1];
        } else {
            JOptionPane.showMessageDialog(null, "The position of commander is not correct.");
        }


        //отдаем обратно для сравнения
        return "first_name:" + first_name + ", last_name:" + last_name + ", position: " + currentDegree;
    }


    //вспомогательные методы

    //Наличение числа int
    static int findIntegerNumber(int index, String textArray[]){
        Pattern pat = Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
        Matcher matcher;

        int result = 0;

        for(int i = index; i < textArray.length; i++){
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

    //Наличение числа int. ищет до конкретного элемент
    int findIntegerNumberToSymbol(int index, String endStrLine, String textArray[]){
        int result = 0;
        Pattern pat = Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
        Matcher matcher;

        String sentence = "";

        for(int i = index; i < textArray.length; i++){
            if(textArray[i].equals(endStrLine) || textArray[i].contains(endStrLine)){
                break;
            } else{
                sentence += textArray[i];
            }
        }
        matcher = pat.matcher(sentence);
        while (matcher.find()) {
            result = Integer.parseInt(matcher.group());
            break;
        };

        return result;
    }

    //наличие числа double
    double findDoubleNumber(int index, String endStrLine, String textArray[]){
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
        };

        return result;
    }

    private static boolean searchWordEqualsOrContains(String line, String element){
         if(line.equals(element)) return true;
         else if(line.contains(element)) return true;

        return false;
    }

    //2.2 Поиск конкретного символа в строке
    private static boolean searchCharacterInStringIteration(String regex, String str){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        boolean result = matcher.find();

        return result;
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
