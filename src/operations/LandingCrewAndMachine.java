package operations;

import model.CityBaseLandingSite;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LandingCrewAndMachine {

    public static Random random = new Random();

    
    public static CityBaseLandingSite searchCityForLanding() {
        List<CityBaseLandingSite> cityBases = new ArrayList<CityBaseLandingSite>();
        CityBaseLandingSite base = null;

        ArrayList<CityBaseLandingSite> currentArray = new ArrayList<CityBaseLandingSite>();// change this arrayList to dinamic massive. And study to make dinamic massive myself

        cityBases.add(new CityBaseLandingSite("Kharkov", "Ukraine", possible()));
        cityBases.add(new CityBaseLandingSite("Paris", "France", possible()));
        cityBases.add(new CityBaseLandingSite("New York", "USA", possible()));
        cityBases.add(new CityBaseLandingSite("Rio de Janeiro", "Brazil", possible()));

        for (CityBaseLandingSite possible : cityBases) {
            if (possible.possibleLanding == true) currentArray.add(possible);
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

