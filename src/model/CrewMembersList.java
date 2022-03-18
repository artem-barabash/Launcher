package model;

import model.crewmemebers.CrewMember;

import javax.swing.*;
import java.util.ArrayList;

public class CrewMembersList extends ArrayList<CrewMember> {
    //Предотвращает вход на запуск с негативной проверкой
    public boolean makeNextOperations;

    public CrewMembersList(){
        super();
    }

    public  boolean checkIdeticalItemCrewMember(){
        boolean isEmpty = false;
        for(CrewMember crewMember : this){
            int count = 0;

            for(CrewMember checkNameAndSurname : this){
                if(checkNameAndSurname.getName().equals(crewMember.getName()) && checkNameAndSurname.getSurname().equals(crewMember.getSurname())) count++;
            }

            if (count > 1){
                isEmpty = false;
                break;
            }
            else isEmpty = true;

        }
        if(!isEmpty) JOptionPane.showMessageDialog(null, "One person cannot be listed twice!");
        return isEmpty;
    }

    public  boolean checkListOfMemberAboutPositions(){
        String listCrewMembersPositions[] = {"Commander", "Engineer", "Gunner", "Doctor", "Tourist"};

        boolean isEmptyPosition = true;
        int i = 0;

        while (i < listCrewMembersPositions.length){
            if(checkList(listCrewMembersPositions[0]) != 1){
                isEmptyPosition = false;//commader
                break;
            }else if(checkList(listCrewMembersPositions[1]) != 2){
                isEmptyPosition = false;//enginner
                break;
            }else if(!(checkList(listCrewMembersPositions[2]) >= 1) || !(checkList(listCrewMembersPositions[2]) <= 2)){
                isEmptyPosition = false;//gunner
                break;
            }else if(!(checkList(listCrewMembersPositions[3]) >= 1) || !(checkList(listCrewMembersPositions[3]) <= 2)){
                isEmptyPosition = false;//doctor
                break;
            }else if(!(checkList(listCrewMembersPositions[4]) <= 3)){
                isEmptyPosition = false;//tourist
                break;
            }
            i++;
        }

        if (!isEmptyPosition) JOptionPane.showMessageDialog(null, "The number of crews must be as follows:\n" +
                "Crew commander - 1 person.\n" +
                "Engineer - 2 persons\n" +
                "Gunner - 1 or 2 people\n" +
                "Doctor - 1 or 2 people\n" +
                "Tourist - max 3 people\n");

        return isEmptyPosition;
    }

    private int checkList(String position){
        int count = 0;
        for(CrewMember item : this){
            if(item.getPosition().equals(position)) count++;
        }
        return count;
    }
}
