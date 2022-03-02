import java.util.regex.Matcher;
import java.util.regex.Pattern;

;

public class Test {



    public static void main(String[] args) {
        String inputString = "Alive";
        String specialCharactersString = "!@#$%&*()'+,-./:;<=>?[]^_`{|}";
        for (int i=0; i < inputString.length() ; i++)
        {
            char ch = inputString.charAt(i);
            if(specialCharactersString.contains(Character.toString(ch))) {
                System.out.println(inputString+ " contains special character");
                break;
            }
            else if(i == inputString.length()-1)
                System.out.println(inputString+ " does NOT contain special character");
        }

        System.out.println("-------------------------");
        //method();
        method1();

    }

    static void method1(){
        String text    =
                "This is the text which is to be searched " +
                        "for occurrences of the word 'is'is";

        String regex = "\\.";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        int count = 0;
        while(matcher.find()) {
            count++;
            System.out.println("found: " + count + " : "
                    + matcher.start() + " - " + matcher.end());
            break;
        }
    }

    static void method(){
        String inputString = "-";
        //Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Pattern pattern = Pattern.compile("[a-zA-Z]");
        Matcher matcher = pattern.matcher(inputString);
        boolean isStringContainsSpecialCharacter = matcher.find();
        if(isStringContainsSpecialCharacter)
            System.out.println(inputString+ " contains special character");
        else
            System.out.println(inputString+ " does NOT contain special character");
    }

}
