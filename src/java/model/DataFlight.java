package model;

public class DataFlight {
    public double distance;
    public double quelityConsumption;

    public DataFlight(double distance, double quelityConsumption) {
        this.distance = distance;
        this.quelityConsumption = quelityConsumption;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getQuelityConsumption() {
        return quelityConsumption;
    }

    public void setQuelityConsumption(double quelityConsumption) {
        this.quelityConsumption = quelityConsumption;
    }
}
