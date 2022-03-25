package model;

public class CityBaseLandingSite extends CityBase {
    public String city;
    public String country;
    public boolean possibleLanding;

    public CityBaseLandingSite(String city, String country, boolean possibleLanding) {
        super(city, country);
        this.city = city;
        this.country = country;
        this.possibleLanding = possibleLanding;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isPossibleLanding() {
        return possibleLanding;
    }

    public void setPossibleLanding(boolean possibleLanding) {
        this.possibleLanding = possibleLanding;
    }

    @Override
    public String toString() {
        if(country == "USA"){
            return "We can land at " + city + " in the " + country;
        }
        return "We can land at " + city + " in " + country;
    }
}
