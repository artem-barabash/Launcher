package model.crewmemebers;

public class Engineer extends CrewMember implements Work{
    public Engineer(int number, String name, String surname, String position) {
        super(number, name, surname, position);
    }

    @Override
    public String toString() {
        return "Engineer{" +
                "number=" + number +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", position='" + position + '\'' +
                '}';
    }

    @Override
    public  void doResponsibilities() {
        System.out.println("Motors 1 and 2, repair system, research space");
    }

}
