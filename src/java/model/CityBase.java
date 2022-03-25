package model;

public class CityBase {
    public String city;
    public String country;

    public CityBase(String city, String country) {
        this.city = city;
        this.country = country;
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

    @Override
    public String toString() {
        return "CityBase{" +
                "city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
