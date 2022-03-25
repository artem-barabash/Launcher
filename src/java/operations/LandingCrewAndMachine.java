package operations;

import model.CityBaseLandingSite;

import javax.swing.*;
import java.util.Random;

public class LandingCrewAndMachine {
    public static Random random = new Random();
    
    public static CityBaseLandingSite searchCityForLanding() {
        CityBaseLandingSite [] cityBaseLandingSites  = {new CityBaseLandingSite("Kharkov", "Ukraine", random.nextBoolean()),
                new CityBaseLandingSite("Paris", "France", random.nextBoolean()),
                new CityBaseLandingSite("New York", "USA", random.nextBoolean()),
                new CityBaseLandingSite("Rio de Janeiro", "Brazil", random.nextBoolean())
        };

        CityBaseLandingSite currentCityBase = null;

        int countTrueCities = 0;
        for(CityBaseLandingSite cityBase: cityBaseLandingSites){
            if(cityBase.possibleLanding) countTrueCities++;
        }
        //System.out.println("countTrueCities = " + countTrueCities);

        if(countTrueCities != 0){
            CityBaseLandingSite [] arrayWithTrueCities = new CityBaseLandingSite[countTrueCities];

            for(int i = 0; i < arrayWithTrueCities.length; i++){
                for(int j = 0; j < cityBaseLandingSites.length; j++){
                    if(cityBaseLandingSites[j].possibleLanding) arrayWithTrueCities[i] = cityBaseLandingSites[j];
                }
            }
            currentCityBase = arrayWithTrueCities[random.nextInt(arrayWithTrueCities.length)];

        }else JOptionPane.showMessageDialog(null, "The all of cities cannot receive accept the ship with crew!");

        return currentCityBase;
    }

    public static void main(String[] args) {
        CityBaseLandingSite cityBaseLandingSite =searchCityForLanding();
        System.out.println(cityBaseLandingSite.toString());
    }
    //need to create objects form enum
}

