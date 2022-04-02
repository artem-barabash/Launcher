package model;

public class DateEvent {
    int numberFlight;
    String dateEvent;
    String event;

    public DateEvent(int numberFlight, String dateEvent, String event) {
        this.numberFlight = numberFlight;
        this.dateEvent = dateEvent;
        this.event = event;
    }

    public int getNumberFlight() {
        return numberFlight;
    }

    public void setNumberFlight(int numberFlight) {
        this.numberFlight = numberFlight;
    }

    public String getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(String dateEvent) {
        this.dateEvent = dateEvent;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "DateEvent{" +
                "numberFlight=" + numberFlight +
                ", dateEvent=" + dateEvent +
                ", event='" + event + '\'' +
                '}';
    }
}
