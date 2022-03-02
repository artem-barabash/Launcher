package operations;

import model.CityBase;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LandingCrewAndMachine {

    public static Random random = new Random();

    
    public static CityBase searchCityForLanding() {
        List<CityBase> cityBases = new ArrayList<CityBase>();
        CityBase base = null;

        ArrayList<CityBase> currentArray = new ArrayList<CityBase>();// change this arrayList to dinamic massive. And study to make dinamic massive myself

        cityBases.add(new CityBase("Kharkov", "Ukraine", possible()));
        cityBases.add(new CityBase("Paris", "France", possible()));
        cityBases.add(new CityBase("New York", "USA", possible()));
        cityBases.add(new CityBase("Rio de Janeiro", "Brazil", possible()));

        //System.out.println(cityBases.toString().);

        //System.out.println("-----");


        for (CityBase possible : cityBases) {

            if (possible.possibleLanding == true) {
                currentArray.add(possible);
            }
        }


        if(currentArray != null){
            //TODO Exception in thread "AWT-EventQueue-0" java.lang.IllegalArgumentException проблема при выборе города
            base = currentArray.get(random.nextInt(currentArray.size()));
        }
        else {
            JOptionPane.showMessageDialog(null, "All the places for landing are busy.");
        }

        return base;
    }

    //need to create objects form enum


    private static boolean possible() {

        return random.nextBoolean();
    }

}

