package model.crewmemebers;

public class Commander extends CrewMember implements Work{
    public Commander(int number, String name, String surname, String position) {
        super(number, name, surname, position);
    }

    @Override
    public String toString() {
        return "Commander{" +
                "number=" + number +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", position='" + position + '\'' +
                '}';
    }

    @Override
    public void doResponsibilities() {
        System.out.println("Admin all crew and connect with Earth");
    }
}
