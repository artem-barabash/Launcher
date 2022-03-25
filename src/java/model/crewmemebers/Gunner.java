package model.crewmemebers;

public class Gunner extends CrewMember implements Work{
    public Gunner(int number, String name, String surname, String position) {
        super(number, name, surname, position);
    }

    @Override
    public void doResponsibilities() {
        System.out.println("We have one space gunner, and two space rifles.");
    }
}
