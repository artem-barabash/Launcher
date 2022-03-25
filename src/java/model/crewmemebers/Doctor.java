package model.crewmemebers;

public class Doctor extends CrewMember{
    public Doctor(int number, String name, String surname, String position) {
        super(number, name, surname, position);
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "number=" + number +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}
