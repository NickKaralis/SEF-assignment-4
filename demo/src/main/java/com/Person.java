package com;

import java.util.*;
import java.util.HashMap; import java.util.Date;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Person {
private String personID; private String firstName; private String lastName; private String address;
private String birthdate;
private HashMap<Date, Integer> demeritPoints; // A variable that holds the demerit points with the offense day
private boolean isSuspended;

public boolean dateChecker(String dateToCheck){ 

// format cehck
if (dateToCheck == null || dateToCheck.length() != 10) return false;
if (dateToCheck.charAt(2) != '-' || dateToCheck.charAt(5) != '-') return false;
    return true;
}

//Stack overflow special
public static String[] splitByPipe(String address) {
    String[] parts = new String[5];
    int start = 0;
    int partIndex = 0;

    for (int i = 0; i < 4; i++) {  // We expect 5 parts, so 4 pipes to find
        int pipeIndex = address.indexOf('|', start);
        if (pipeIndex == -1) {
            // Not enough parts
            return null; // or handle error
        }
        parts[partIndex++] = address.substring(start, pipeIndex).trim();
        start = pipeIndex + 1;
    }
    // Last part after the last pipe
    parts[partIndex] = address.substring(start).trim();

    return parts;
}


public boolean addPerson() {
// return false;
// Check if personID is 10 characters
if (personID == null || personID.length() != 10) {
    return false;
}

        // Check first two characters are digits 2–9
        char c1 = personID.charAt(0);
        char c2 = personID.charAt(1);
        if (!Character.isDigit(c1) || !Character.isDigit(c2)) return false;
        if (c1 < '2' || c1 > '9' || c2 < '2' || c2 > '9') return false;

// char checker ts
    int specialCount = 0;
    for (int i = 2; i < 8; i++) {
    char ch = personID.charAt(i);
    if (!Character.isLetterOrDigit(ch)) {
        specialCount++;
    }
    }
    if (specialCount < 2) return false;
// format check 1
    char last1 = personID.charAt(8);
    char last2 = personID.charAt(9);
    if (!(Character.isUpperCase(last1) && Character.isUpperCase(last2))) 
    return false;

// format check
    if (address == null) return false;
    String[] addressParts = splitByPipe(address);
    if (addressParts == null || addressParts.length != 5) {
        // invalid address format
    }    
    if (addressParts.length != 5) return false;
    if (!addressParts[3].trim().equals("Victoria")) return false; 
    //coppied, need to specify the twim equals is a stack overflow special
    // some of this above code for parsing  code from ws3 schools, should credit
    //https://stackoverflow.com/questions/4736/learning-regular-expressions 

// format cehck
    if (birthdate == null || birthdate.length() != 10) return false;
    if (birthdate.charAt(2) != '-' || birthdate.charAt(5) != '-') return false;

// date check // TO BE UPDATED @EVERYONE
    try {
        int day = Integer.parseInt(birthdate.substring(0, 2));
        int month = Integer.parseInt(birthdate.substring(3, 5));
        int year = Integer.parseInt(birthdate.substring(6));
        if (day < 1 || day > 31) return false;
        if (month < 1 || month > 12) return false;
        if (year < 1900 || year > 2100) return false;
    } catch (NumberFormatException e) {
        return false;
    }
// make file ts
    try {
        FileOutputStream fos = new FileOutputStream("persons.txt", true);
        PrintStream ps = new PrintStream(fos);
        ps.println("PersonID: " + personID);
        ps.println("First Name: " + firstName);
          ps.println("Last Name: " + lastName);
         ps.println("Address: " + address);
        ps.println("Birthdate: " + birthdate);
// May add more later
// split w comma ts or make into csv instead of txt but nt specified
        ps.close();
        fos.close();
    } catch (IOException e) {
        return false;
    }

    return true;
}
//TODO: This method adds information about a person to a TXT file.
//Condition 1: PersonID should be exactly 10 characters long;
//the first two characters should be numbers between 2 and 9, there should be at least two special characters between characters 3 and 8, //and the last two characters should be upper case letters (A - Z). Example: "56s_d%&fAB"
//Condition 2: The address of the Person should follow the following format: Street Number | Street | City | State | Country.
//The State should be only Victoria. Example: 32 | Highland Street | Melbourne | Victoria | Australia.
//Condition 3: The format of the birth date of the person should follow the following format: DD-MM-YYYY. Example: 15-11-1990
//Instruction: If the Person's information meets the above conditions and any other conditions you may want to consider, //the information should be inserted into a TXT file, and the addPerson function should return true.
//Otherwise, the information should not be inserted into the TXT file, and the addPerson function should return false. return true;

public boolean updatePersonalDetails() {
    return false;
//TODO: This method allows updating a given person's ID, firstName, lastName, address and birthday in a TXT file. //Changing personal details will not affect their demerit points or the suspension status.
// All relevant conditions discussed for the addPerson function also need to be considered and checked in the updatPerson function. //Condition 1: If a person is under 18, their address cannot be changed.
//Condition 2: If a person's birthday is going to be changed, then no other personal detail (i.e, person's ID, firstName, lastName, address) can be changed. //Condition 3: If the first character/digit of a person's ID is an even number, then their ID cannot be changed.
//Instruction: If the Person's updated information meets the above conditions and any other conditions you may want to consider,
// the Person's information should be updated in the TXT file with the updated information, and the updatePersonalDetails function should return true. //Otherwise, the Person's updated information should not be updated in the TXT file, and the updatePersonalDetails function should return false. return true;
}
public String addDemeritPoints()
{
    
    return ""; //remove after finishing
}
}
//TODO: This method adds demerit points for a given person in a TXT file.
//Condition 1: The format of the date of the offense should follow the following format: DD-MM-YYYY. Example: 15-11-1990 
//Condition 2:
// The demerit points must be a whole number between 1-6
//Condition 3: If the person is under 21, the isSuspended variable should be set to true if the total demerit points within two years exceed 6. //If the person is over 21, the isSuspended variable should be set to true if the total demerit points within two years exceed
// 12.
//Instruction: If the above condiations and any other conditions you may want to consider are met, the demerit points for a person should be inserted into the TXT file, //and the addDemerit Points function should return "Sucess". Otherwise, the addDemeritPoints function should return "Failed". return "Sucess";