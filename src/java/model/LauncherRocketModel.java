package model;

import model.crewmemebers.CrewMember;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LauncherRocketModel{
    int numberFlight;//number flight
    String modelRocket; //модель ракеты
    CityBase cityBaseTakeOff;// место взлета
    CityBaseLandingSite cityBaseLandingSite; // место посадки

    double quelityConsumption; //к-ство топлива

    ArrayList<CrewMember> crewMembers;

    List<DateEvent> listEventDates;
    //date need insert into sqlite!!!!


    public LauncherRocketModel(int numberFlight, String modelRocket, CityBase cityBaseTakeOff, CityBaseLandingSite cityBaseLandingSite,
                              double quelityConsumption, ArrayList<CrewMember> crewMembers, List<DateEvent> listEventDates) {
        this.numberFlight = numberFlight;
        this.modelRocket = modelRocket;
        this.cityBaseTakeOff = cityBaseTakeOff;
        this.cityBaseLandingSite = cityBaseLandingSite;
        this.quelityConsumption = quelityConsumption;
        this.crewMembers = crewMembers;
        this.listEventDates = listEventDates;
    }


    public int getNumberFlight() {
        return numberFlight;
    }

    public void setNumberFlight(int numberFlight) {
        this.numberFlight = numberFlight;
    }

    public String getModelRocket() {
        return modelRocket;
    }

    public void setModelRocket(String modelRocket) {
        this.modelRocket = modelRocket;
    }

    public CityBase getCityBaseTakeOff() {
        return cityBaseTakeOff;
    }

    public void setCityBaseTakeOff(CityBase cityBaseTakeOff) {
        this.cityBaseTakeOff = cityBaseTakeOff;
    }

    public CityBaseLandingSite getCityBaseLandingSite() {
        return cityBaseLandingSite;
    }

    public void setCityBaseLandingSite(CityBaseLandingSite cityBaseLandingSite) {
        this.cityBaseLandingSite = cityBaseLandingSite;
    }

    public double getQuelityConsumption() {
        return quelityConsumption;
    }

    public void setQuelityConsumption(double quelityConsumption) {
        this.quelityConsumption = quelityConsumption;
    }

    public ArrayList<CrewMember> getCrewMembers() {
        return crewMembers;
    }

    public void setCrewMembers(ArrayList<CrewMember> crewMembers) {
        this.crewMembers = crewMembers;
    }

    public List<DateEvent> getListEventDates() {
        return listEventDates;
    }

    public void setListEventDates(List<DateEvent> listEventDates) {
        this.listEventDates = listEventDates;
    }



    @Override
    public String toString() {
        Date date = new Date();
        return "numberFlight=" + numberFlight + "\n" +
                ", modelRocket=" + modelRocket + "\n" +
                ", cityBaseTakeOff=" + cityBaseTakeOff + "\n" +
                ", cityBaseLandingSite=" + cityBaseLandingSite + "\n" +
                ", quelityConsumption=" + quelityConsumption + "\n" +
                ", crewMembers=" + crewMembers + "\n" +
                ", listEventDates=" + listEventDates + "\n" +
                '}';
    }
}
